package com.cinetime.service;

import com.cinetime.dto.request.BuyTicketRequest;
import com.cinetime.entity.Payment;
import com.cinetime.entity.Showtime;
import com.cinetime.entity.Ticket;
import com.cinetime.entity.User;
import com.cinetime.entity.enums.PaymentStatus;
import com.cinetime.entity.enums.TicketStatus;
import com.cinetime.repository.ShowtimeRepository;
import com.cinetime.repository.TicketRepository;
import com.cinetime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;
    private final PaymentService paymentService; // <-- BU SERVİSİ EKLİYORUZ

    // Bilet Satın Alma (PAID)
    public Ticket buyTicket(BuyTicketRequest request) {
        checkSeatAvailability(request);

        // 1. Bileti Kaydet
        Ticket savedTicket = saveTicket(request, TicketStatus.PAID);

        // 2. ÖDEME KAYDINI OLUŞTUR (Eksik olan kısım buydu!)
        Payment payment = Payment.builder()
                .amount(request.getPrice())
                .paymentStatus(PaymentStatus.SUCCESS)
                .paymentDate(LocalDateTime.now())
                .user(savedTicket.getUser())
                .ticket(savedTicket)
                .build();

        paymentService.savePayment(payment); // Ödemeyi veritabanına yazıyoruz

        return savedTicket;
    }

    // --- YENİ EKLENEN: Rezervasyon Yapma (RESERVED) ---
    public Ticket reserveTicket(BuyTicketRequest request) {
        checkSeatAvailability(request);
        return saveTicket(request, TicketStatus.RESERVED);
    }

    // Ortak Koltuk Kontrolü
    private void checkSeatAvailability(BuyTicketRequest request) {
        if (ticketRepository.existsByShowtime_IdAndSeatLetterAndSeatNumber(
                request.getShowtimeId(), request.getSeatLetter(), request.getSeatNumber())) {
            throw new RuntimeException("Bu koltuk maalesef dolu!");
        }
    }

    // Ortak Kayıt Metodu
    private Ticket saveTicket(BuyTicketRequest request, TicketStatus status) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Seans bulunamadı!"));

        Ticket ticket = Ticket.builder()
                .user(user)
                .showtime(showtime)
                .seatLetter(request.getSeatLetter())
                .seatNumber(request.getSeatNumber())
                .price(request.getPrice())
                .status(status)
                .build();

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getCurrentTicket(Long userId) {
        return ticketRepository.findCurrentTickets(userId, java.time.LocalDate.now(), java.time.LocalTime.now());
    }

    public List<Ticket> getPastTickets(Long userId) {
        return ticketRepository.findPastTickets(userId, java.time.LocalDate.now(), java.time.LocalTime.now());
    }

    public List<Ticket> getAllTicketByUser(Long userId) {
        return ticketRepository.findAllByUser_Id(userId);
    }
}