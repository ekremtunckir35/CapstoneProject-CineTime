package com.cinetime.controller;

import com.cinetime.dto.request.CreateMovieRequest;
import com.cinetime.entity.Movie;
import com.cinetime.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // TEK FİLM EKLEME (POST /api/movies)
    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody CreateMovieRequest request) {
        return new ResponseEntity<>(movieService.createMovie(request), HttpStatus.CREATED);
    }

    // --- İŞTE EKSİK OLAN KISIM BURASI ---
    // TOPLU FİLM EKLEME (POST /api/movies/bulk)
    @PostMapping("/bulk")
    public ResponseEntity<List<Movie>> createBulkMovies(@RequestBody List<CreateMovieRequest> requests) {
        return new ResponseEntity<>(movieService.createMovies(requests), HttpStatus.CREATED);
    }
    // ------------------------------------

    // VİZYONDAKİLER (GET /api/movies/in-theaters)
    @GetMapping("/in-theaters")
    public ResponseEntity<List<Movie>> getInTheaters() {
        List<Movie> movies = movieService.getMoviesByStatus(com.cinetime.entity.enums.MovieStatus.IN_THEATERS);
        return ResponseEntity.ok(movies);
    }

    // YAKINDAKİLER (GET /api/movies/coming-soon)
    @GetMapping("/coming-soon")
    public ResponseEntity<List<Movie>> getComingSoon() {
        List<Movie> movies = movieService.getMoviesByStatus(com.cinetime.entity.enums.MovieStatus.COMING_SOON);
        return ResponseEntity.ok(movies);
    }
}