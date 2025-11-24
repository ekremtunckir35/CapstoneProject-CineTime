package com.cinetime.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RecommendationResponse {

    private Long id;
    private String title;
    private  String genre;

    @JsonProperty("similarity_score")//Python'daki 'similarity_score' ismiyle eşleşir
    private Double similarityScore;
}
