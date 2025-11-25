package com.cinetime.repository;

import com.cinetime.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    //Koltuk dolu mu kontrolu

    boolean existsByShowtime_IdAndSeatLetterAndSeatNumber(Long showtimeId, String seatLetter, Integer seatNumber);

    boolean existsByUser_Id(Long userId);

    //1-Kullanicinin Tum Biletlerin Getirme

    List<Ticket>findAllByUser_Id(Long userId);

    //2-Aktif Biletler(Gelecek Seanslar)
    //Yani seans tarihi bugunden buyuk olanlar ya da saati gecmemis olanlar

    @Query("SELECT t FROM Ticket t WHERE t.user.id = :userId AND " +
            "(t.showtime.date > :date OR (t.showtime.date = :date AND t.showtime.startTime > :time))")
    List<Ticket> findCurrentTickets(Long userId, LocalDate date, LocalTime time);


    //3-Gecmis Biletler(Izlenmis Filmler)

 //Seans tarihi bugunden kucuk olalnlar ya da bugun olup saati gecmis olanlar

    @Query("SELECT t FROM Ticket t WHERE t.user.id = :userId AND " +
            "(t.showtime.date < :date OR (t.showtime.date = :date AND t.showtime.startTime < :time))")
    List<Ticket> findPastTickets(Long userId, LocalDate date, LocalTime time);
}


