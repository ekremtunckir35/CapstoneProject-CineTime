package com.cinetime.service;

import com.cinetime.dto.request.BuyTicketRequest;
import com.cinetime.entity.Showtime;
import com.cinetime.entity.Ticket;
import com.cinetime.entity.User;
import com.cinetime.entity.enums.TicketStatus;
import com.cinetime.repository.ShowtimeRepository;
import com.cinetime.repository.TicketRepository;
import com.cinetime.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ShowtimeRepository showtimeRepository;

    @InjectMocks
    private TicketService ticketService;

    private BuyTicketRequest request;
    private User user;
    private Showtime showtime;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        // Test verilerini hazırlıyoruz
        request = new BuyTicketRequest();
        request.setUserId(1L);
        request.setShowtimeId(1L);
        request.setSeatLetter("A");
        request.setSeatNumber(1);
        request.setPrice(100.0);

        // Mock Nesneler (id alanlarını senin entity yapına göre ayarladık)
        user = User.builder().id(1L).email("test@test.com").build();
        showtime = Showtime.builder().id(1L).build();

        // DİKKAT: Ticket entity'sinde ID alanı 'ticketId' olduğu için builder'da onu kullanıyoruz
        ticket = Ticket.builder()
                .ticketId(100L)
                .user(user)
                .showtime(showtime)
                .seatLetter("A")
                .seatNumber(1)
                .price(100.0)
                .status(TicketStatus.PAID)
                .build();
    }

    @Test
    void buyTicket_Success() {
        // 1. Senaryo: Her şey yolunda, bilet satılıyor.
        when(ticketRepository.existsByShowtime_IdAndSeatLetterAndSeatNumber(1L, "A", 1)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket result = ticketService.buyTicket(request);

        assertNotNull(result);
        assertEquals(TicketStatus.PAID, result.getStatus());
        assertEquals(100L, result.getTicketId()); // getTicketId() kullanıyoruz
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void buyTicket_Fail_SeatTaken() {
        // 2. Senaryo: Koltuk dolu, hata vermeli.
        when(ticketRepository.existsByShowtime_IdAndSeatLetterAndSeatNumber(1L, "A", 1)).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.buyTicket(request);
        });

        assertEquals("Bu koltuk maalesef dolu!", exception.getMessage());
        // Hata olduğu için save metodu hiç çağrılmamalı
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void reserveTicket_Success() {
        // 3. Senaryo: Rezervasyon işlemi
        when(ticketRepository.existsByShowtime_IdAndSeatLetterAndSeatNumber(1L, "A", 1)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));

        Ticket reservedTicket = Ticket.builder().ticketId(101L).status(TicketStatus.RESERVED).build();
        when(ticketRepository.save(any(Ticket.class))).thenReturn(reservedTicket);

        Ticket result = ticketService.reserveTicket(request);

        assertNotNull(result);
        assertEquals(TicketStatus.RESERVED, result.getStatus());
    }

    @Test
    void getCurrentTicket_Success() {
        // 4. Senaryo: Aktif biletleri getirme
        // Tarih parametreleri dinamik olduğu için any() kullanıyoruz
        when(ticketRepository.findCurrentTickets(eq(1L), any(LocalDate.class), any(LocalTime.class)))
                .thenReturn(List.of(ticket));

        List<Ticket> result = ticketService.getCurrentTicket(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getPastTickets_Success() {
        // 5. Senaryo: Geçmiş biletleri getirme
        when(ticketRepository.findPastTickets(eq(1L), any(LocalDate.class), any(LocalTime.class)))
                .thenReturn(List.of(ticket));

        List<Ticket> result = ticketService.getPastTickets(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}