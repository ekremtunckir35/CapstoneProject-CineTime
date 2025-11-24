package com.cinetime.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data; // <-- Bu anotasyon 'getResults()' metodunu otomatik üretir
import java.util.List;

@Data
public class TmdbResponse {

    @JsonProperty("results")
    private List<TmdbMovie> results;
}