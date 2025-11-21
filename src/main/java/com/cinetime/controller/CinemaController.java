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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cinemas")
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    // 1. ŞEHİR EKLEME (Bu zaten vardı)
    @PostMapping("/city")
    public ResponseEntity<City> createCity(@Valid @RequestBody CreateCityRequest request) {
        return new ResponseEntity<>(cinemaService.createCity(request), HttpStatus.CREATED);
    }

    // 2. SİNEMA EKLEME (Bu Eksikti)
    @PostMapping
    public ResponseEntity<Cinema> createCinema(@Valid @RequestBody CreateCinemaRequest request) {
        return new ResponseEntity<>(cinemaService.createCinema(request), HttpStatus.CREATED);
    }

    // 3. SALON EKLEME (Bu Eksikti)
    @PostMapping("/hall")
    public ResponseEntity<Hall> createHall(@Valid @RequestBody CreateHallRequest request) {
        return new ResponseEntity<>(cinemaService.createHall(request), HttpStatus.CREATED);
    }
}