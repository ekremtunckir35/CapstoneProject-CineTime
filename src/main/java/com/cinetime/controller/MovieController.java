package com.cinetime.controller;

import com.cinetime.dto.request.CreateMovieRequest;
import com.cinetime.entity.Movie;
import com.cinetime.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // 1. Film Ekle
    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody CreateMovieRequest request) {
        return new ResponseEntity<>(movieService.createMovie(request), HttpStatus.CREATED);
    }

    // 2. Toplu Ekle
    @PostMapping("/bulk")
    public ResponseEntity<List<Movie>> createBulkMovies(@RequestBody List<CreateMovieRequest> requests) {
        return new ResponseEntity<>(movieService.createMovies(requests), HttpStatus.CREATED);
    }

    // 3. Gelişmiş Listeleme (Arama & Sayfalama)
    @GetMapping
    public ResponseEntity<Page<Movie>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort,
            @RequestParam(defaultValue = "asc") String type,
            @RequestParam(required = false) String q
    ) {
        return ResponseEntity.ok(movieService.getAllMovies(q, page, size, sort, type));
    }

    // --- YENİ EKLENENLER ---

    // 4. ID ile Film Getir (PDF: M09)
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    // 5. Film Sil (PDF: M13)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Film başarıyla silindi. ID: " + id);
    }

    // 6. Film Güncelle (PDF: M12)
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @Valid @RequestBody CreateMovieRequest request) {
        return ResponseEntity.ok(movieService.updateMovie(id, request));
    }
    // -----------------------

    // 7. Vizyondakiler
    @GetMapping("/in-theaters")
    public ResponseEntity<List<Movie>> getInTheaters() {
        List<Movie> movies = movieService.getMoviesByStatus(com.cinetime.entity.enums.MovieStatus.IN_THEATERS);
        return ResponseEntity.ok(movies);
    }

    // 8. Yakındakiler
    @GetMapping("/coming-soon")
    public ResponseEntity<List<Movie>> getComingSoon() {
        List<Movie> movies = movieService.getMoviesByStatus(com.cinetime.entity.enums.MovieStatus.COMING_SOON);
        return ResponseEntity.ok(movies);
    }
}