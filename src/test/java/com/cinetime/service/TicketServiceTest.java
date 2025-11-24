package com.cinetime.service;

import com.cinetime.dto.request.BuyTicketRequest;
import com.cinetime.entity.Payment;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock // --- EKSİK OLAN KISIM BURASIYDI ---
    private PaymentService paymentService;

    @InjectMocks
    private TicketService ticketService;

    private BuyTicketRequest request;
    private User user;
    private Showtime showtime;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        request = new BuyTicketRequest();
        request.setUserId(1L);
        request.setShowtimeId(1L);
        request.setSeatLetter("A");
        request.setSeatNumber(1);
        request.setPrice(100.0);

        user = User.builder().id(1L).name("Test User").build();
        showtime = Showtime.builder().id(1L).build();

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
        // Senaryo 1: Başarılı Satış
        when(ticketRepository.existsByShowtime_IdAndSeatLetterAndSeatNumber(anyLong(), anyString(), anyInt()))
                .thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Payment servisi void döndüğü için "doNothing" varsayılandır ama açıkça belirtelim
        doNothing().when(paymentService).savePayment(any(Payment.class));

        Ticket result = ticketService.buyTicket(request);

        assertNotNull(result);
        assertEquals(TicketStatus.PAID, result.getStatus());
        assertEquals(100L, result.getTicketId());

        verify(ticketRepository).save(any(Ticket.class));
        // Ödemenin kaydedildiğini de doğrula
        verify(paymentService).savePayment(any(Payment.class));
    }

    @Test
    void buyTicket_Fail_SeatTaken() {
        // Senaryo 2: Koltuk Dolu
        when(ticketRepository.existsByShowtime_IdAndSeatLetterAndSeatNumber(anyLong(), anyString(), anyInt()))
                .thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.buyTicket(request);
        });

        assertEquals("Bu koltuk maalesef dolu!", exception.getMessage());
        verify(ticketRepository, never()).save(any(Ticket.class));
        // Hata varsa ödeme de çağrılmamalı
        verify(paymentService, never()).savePayment(any(Payment.class));
    }

    @Test
    void reserveTicket_Success() {
        // Senaryo 3: Rezervasyon
        when(ticketRepository.existsByShowtime_IdAndSeatLetterAndSeatNumber(anyLong(), anyString(), anyInt()))
                .thenReturn(false);
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
        // Senaryo 4: Aktif Biletler
        when(ticketRepository.findCurrentTickets(eq(1L), any(LocalDate.class), any(LocalTime.class)))
                .thenReturn(List.of(ticket));

        List<Ticket> results = ticketService.getCurrentTicket(1L);

        assertFalse(results.isEmpty());
    }

    @Test
    void getPastTickets_Success() {
        // Senaryo 5: Geçmiş Biletler
        when(ticketRepository.findPastTickets(eq(1L), any(LocalDate.class), any(LocalTime.class)))
                .thenReturn(List.of(ticket));

        List<Ticket> results = ticketService.getPastTickets(1L);

        assertFalse(results.isEmpty());
    }
}