package com.cinetime.controller;

import com.cinetime.dto.request.BuyTicketRequest;
import com.cinetime.entity.Ticket;
import com.cinetime.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // <-- BU ÇOK ÖNEMLİ!
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/buy")
    public ResponseEntity<Ticket> buyTicket(@Valid @RequestBody BuyTicketRequest request) {
        return new ResponseEntity<>(ticketService.buyTicket(request), HttpStatus.CREATED);
    }

    //Aktif Biletler

    @GetMapping("/current/{userId}")
    public ResponseEntity<java.util.List<Ticket>>getCurrentTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getCurrentTicket(userId));
    }

    //Gecmis Biletler

    public ResponseEntity<java.util.List<Ticket>>getPastTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getPastTickets(userId));
    }

}