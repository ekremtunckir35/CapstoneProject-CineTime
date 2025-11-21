package com.cinetime.repository;

import com.cinetime.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    //Koltuk dolu mu kontrolu

    boolean existsByShowtime_IdAndSeatLetterAndSeatNumber(Long showtimeId, String seatLetter, Integer seatNumber);
}

