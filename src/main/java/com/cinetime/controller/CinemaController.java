package com.cinetime.controller;

import com.cinetime.dto.request.CreateCinemaRequest;
import com.cinetime.dto.request.CreateCityRequest;
import com.cinetime.dto.request.CreateHallRequest;
import com.cinetime.entity.Cinema;
import com.cinetime.entity.City;
import com.cinetime.entity.Hall;
import com.cinetime.service.CinemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    // --- POST METODLARI (EKLEME) ---
    @PostMapping("/city")
    public ResponseEntity<City> createCity(@Valid @RequestBody CreateCityRequest request) {
        return new ResponseEntity<>(cinemaService.createCity(request), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<Cinema> createCinema(@Valid @RequestBody CreateCinemaRequest request) {
        return new ResponseEntity<>(cinemaService.createCinema(request), HttpStatus.CREATED);
    }

    @PostMapping("/hall")
    public ResponseEntity<Hall> createHall(@Valid @RequestBody CreateHallRequest request) {
        return new ResponseEntity<>(cinemaService.createHall(request), HttpStatus.CREATED);
    }

    // --- YENİ GET METODLARI (LİSTELEME) ---

    // Tüm Şehirleri Listele
    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cinemaService.getAllCities());
    }

    // Sinemaları Listele (Şehre göre opsiyonel)
    // Örn: /api/cinemas?cityId=1
    @GetMapping
    public ResponseEntity<List<Cinema>> getCinemas(@RequestParam(required = false) Long cityId) {
        return ResponseEntity.ok(cinemaService.getCinemas(cityId));
    }

    // Bir Sinemanın Salonlarını Getir
    // Örn: /api/cinemas/1/halls
    @GetMapping("/{cinemaId}/halls")
    public ResponseEntity<List<Hall>> getHallsByCinema(@PathVariable Long cinemaId) {
        return ResponseEntity.ok(cinemaService.getHallsByCinema(cinemaId));
    }

    // Özel Salonları Getir (PDF: C05)
    @GetMapping("/special-halls")
    public ResponseEntity<List<Hall>> getSpecialHalls() {
        return ResponseEntity.ok(cinemaService.getSpecialHalls());

    }

    // Sinema Detayı (GET /api/cinemas/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Cinema> getCinemaById(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.getCinemaById(id));
    }
}