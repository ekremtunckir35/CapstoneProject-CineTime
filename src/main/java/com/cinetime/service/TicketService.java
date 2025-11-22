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

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;

    public Ticket buyTicket(BuyTicketRequest request) {
        // 1. Koltuk Kontrolü
        if (ticketRepository.existsByShowtime_IdAndSeatLetterAndSeatNumber(
                request.getShowtimeId(), request.getSeatLetter(), request.getSeatNumber())) {
            throw new RuntimeException("Bu koltuk dolu!");
        }

        // 2. Verileri Bul
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Seans bulunamadı!"));

        // 3. Kaydet
        Ticket ticket = Ticket.builder()
                .user(user)
                .showtime(showtime)
                .seatLetter(request.getSeatLetter())
                .seatNumber(request.getSeatNumber())
                .price(request.getPrice())
                .status(TicketStatus.PAID)
                .build();

        return ticketRepository.save(ticket);
    }

    //Aktif olan biletleri getirme

    public java.util.List<Ticket>getCurrentTicket(Long userId) {
        return ticketRepository.findCurrentTickets(userId,java.time.LocalDate.now(),
                java.time.LocalTime.now());
    }

    //Gecmis Biletleri Getirme
    public java.util.List<Ticket>getPastTickets(Long userId) {
        return ticketRepository.findPastTickets(userId,java.time.LocalDate.now(),
                java.time.LocalTime.now());

    }

    //Tumunu Getirme (Admin Icin)
    public java.util.List<Ticket>getAllTicketByUser(Long userId){
        return ticketRepository.findAllByUser_Id(userId);
    }
}