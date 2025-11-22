package com.cinetime.controller;

import com.cinetime.entity.Favorite;
import com.cinetime.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // Favoriye Ekle
    @PostMapping("/movie/{movieId}")
    public ResponseEntity<Favorite> addFavoriteMovie(@PathVariable Long movieId, @RequestParam Long userId) {
        // DÜZELTME: Service'teki ismin aynısını (addMovieToFavorites) çağırıyoruz
        return new ResponseEntity<>(favoriteService.addMovieToFavorites(userId, movieId), HttpStatus.CREATED);
    }

    // Favorileri Listele
    @GetMapping("/{userId}")
    public ResponseEntity<List<Favorite>> getUserFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getUserFavorites(userId));
    }
}