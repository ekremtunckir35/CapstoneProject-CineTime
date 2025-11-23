package com.cinetime.service;

import com.cinetime.dto.request.BuyTicketRequest;
import com.cinetime.entity.Showtime;
import com.cinetime.entity.Ticket;
import com.cinetime.entity.User;
import com.cinetime.entity.enums.TicketStatus;
import com.cinetime.repository.ShowtimeRepository;
import com.cinetime.repository.TicketRepository;
import com.cinetime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;

    // Bilet Satın Alma (PAID)
    public Ticket buyTicket(BuyTicketRequest request) {
        checkSeatAvailability(request); // Ortak kontrol metodunu aşağıda yazdım
        return saveTicket(request, TicketStatus.PAID);
    }

    // --- YENİ EKLENEN: Rezervasyon Yapma (RESERVED) ---
    public Ticket reserveTicket(BuyTicketRequest request) {
        checkSeatAvailability(request);
        return saveTicket(request, TicketStatus.RESERVED);
    }

    // Ortak Koltuk Kontrolü (Kod tekrarını önlemek için)
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
                .status(status) // PAID veya RESERVED
                .build();

        return ticketRepository.save(ticket);
    }

    // Aktif olan biletleri getirme
    public List<Ticket> getCurrentTicket(Long userId) {
        return ticketRepository.findCurrentTickets(userId, java.time.LocalDate.now(), java.time.LocalTime.now());
    }

    // Geçmiş Biletleri Getirme
    public List<Ticket> getPastTickets(Long userId) {
        return ticketRepository.findPastTickets(userId, java.time.LocalDate.now(), java.time.LocalTime.now());
    }

    // Tümünü Getirme (Admin İçin)
    public List<Ticket> getAllTicketByUser(Long userId) {
        return ticketRepository.findAllByUser_Id(userId);
    }
}