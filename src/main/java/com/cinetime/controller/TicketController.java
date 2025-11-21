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
}