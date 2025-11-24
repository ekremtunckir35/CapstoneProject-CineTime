package com.cinetime.service;

import com.cinetime.dto.response.RecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {


    private final RestTemplate restTemplate = new RestTemplate();
    // Python servisinin adresi (Port 5001 demiştik)
    private final String AI_SERVICE_URL = "http://localhost:5001/recommend/";

    public List<RecommendationResponse> getRecommendations(Long userId) {
        try {
            String url = AI_SERVICE_URL + userId;

            // Python'dan gelen JSON Listesini Java Listesine çeviriyoruz
            ResponseEntity<List<RecommendationResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<RecommendationResponse>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            // Python servisi kapalıysa veya hata verirse boş liste dönelim, uygulama çökmesin
            System.err.println("AI Servisi Hatası: " + e.getMessage());
            return List.of();
        }
    }
}

