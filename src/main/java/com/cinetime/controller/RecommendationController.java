package com.cinetime.controller;

import com.cinetime.dto.response.RecommendationResponse;
import com.cinetime.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    // Değişken adı: 'recommendationService' (Tekil)
    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<RecommendationResponse>> getRecommendations(@PathVariable Long userId) {
        // Burada yukarıdaki değişken adının AYNISINI kullanıyoruz
        return ResponseEntity.ok(recommendationService.getRecommendations(userId));
    }
}