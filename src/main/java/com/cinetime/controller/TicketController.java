package com.cinetime.controller;

import com.cinetime.dto.request.BuyTicketRequest;
import com.cinetime.entity.Ticket;
import com.cinetime.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // PDF: T04 - Bilet Satın Al (Satış - PAID)
    @PostMapping("/buy")
    public ResponseEntity<Ticket> buyTicket(@Valid @RequestBody BuyTicketRequest request) {
        return new ResponseEntity<>(ticketService.buyTicket(request), HttpStatus.CREATED);
    }

    // PDF: T03 - Bilet Rezerve Et (Rezervasyon - RESERVED)
    // --- YENİ EKLENEN ENDPOINT ---
    @PostMapping("/reserve")
    public ResponseEntity<Ticket> reserveTicket(@Valid @RequestBody BuyTicketRequest request) {
        return new ResponseEntity<>(ticketService.reserveTicket(request), HttpStatus.CREATED);
    }

    // PDF: T01 - Aktif Biletleri Getir (Gelecek Seanslar)
    @GetMapping("/current/{userId}")
    public ResponseEntity<List<Ticket>> getCurrentTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getCurrentTicket(userId));
    }

    // PDF: T02 - Geçmiş Biletleri Getir (İzlenmiş Filmler)
    @GetMapping("/passed/{userId}")
    public ResponseEntity<List<Ticket>> getPastTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getPastTickets(userId));
    }

    // Admin İçin: Bir kullanıcının tüm biletleri
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getAllTicketsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getAllTicketByUser(userId));
    }
}