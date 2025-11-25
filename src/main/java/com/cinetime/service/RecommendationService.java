package com.cinetime.service;

import com.cinetime.dto.response.RecommendationResponse;
import com.cinetime.entity.Movie;
import com.cinetime.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final MovieRepository movieRepository; // <-- Veritabanı erişimi ekledik
    private final RestTemplate restTemplate = new RestTemplate();
    private final String AI_SERVICE_URL = "http://localhost:5001/recommend/";

    // Dönüş tipini 'List<Movie>' yaptık (Daha zengin veri)
    public List<Movie> getRecommendations(Long userId) {
        try {
            String url = AI_SERVICE_URL + userId;

            // 1. Python'dan önerilen ID'leri al
            ResponseEntity<List<RecommendationResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<RecommendationResponse>>() {}
            );

            List<RecommendationResponse> aiRecommendations = response.getBody();

            if (aiRecommendations == null || aiRecommendations.isEmpty()) {
                return new ArrayList<>();
            }

            // 2. Sadece ID'leri bir listeye çıkar
            List<Long> movieIds = aiRecommendations.stream()
                    .map(RecommendationResponse::getId)
                    .collect(Collectors.toList());

            // 3. Bu ID'lere sahip filmleri veritabanından (Resim, Puan vb. ile) çek
            return movieRepository.findAllById(movieIds);

        } catch (Exception e) {
            System.err.println("AI Servisi Hatası (Python çalışıyor mu?): " + e.getMessage());
            // Hata olursa boş liste dön, uygulama çökmesin
            return new ArrayList<>();
        }
    }
}