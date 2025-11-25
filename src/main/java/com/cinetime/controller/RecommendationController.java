package com.cinetime.controller;

import com.cinetime.entity.Movie; // <-- Movie entity kullanacağız
import com.cinetime.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Movie>> getRecommendations(@PathVariable Long userId) {
        // Artık RecommendationResponse değil, direkt Movie listesi dönüyoruz
        return ResponseEntity.ok(recommendationService.getRecommendations(userId));
    }
}