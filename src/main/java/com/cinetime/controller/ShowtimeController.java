package com.cinetime.controller;

import com.cinetime.dto.request.CreateShowtimeRequest;
import com.cinetime.entity.Showtime;
import com.cinetime.service.ShowtimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // <-- BU ANOTASYON OLMAZSA SWAGGER GÖRMEZ!
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    // Seans Ekle (POST /api/showtimes)
    @PostMapping
    public ResponseEntity<Showtime> createShowtime(@Valid @RequestBody CreateShowtimeRequest request) {
        return new ResponseEntity<>(showtimeService.createShowtime(request), HttpStatus.CREATED);
    }

    // Bir filmin seanslarını getir (GET /api/showtimes/movie/{movieId})
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Showtime>> getShowtimesByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showtimeService.getShowtimesByMovie(movieId));
    }
    public ResponseEntity<List<Showtime>>getAllShowtimes(){
        return ResponseEntity.ok(showtimeService.getAllShowtimes());
    }
}