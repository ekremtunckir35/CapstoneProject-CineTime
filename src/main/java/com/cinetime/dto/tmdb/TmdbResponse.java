package com.cinetime.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbResponse {

    //JSON daki "result"listesini buraya esle

    @JsonProperty("results")
    private List<TmdbMovie> results;

    //Lombok calismazsa ddiye manuel getter ekliyoruz

    public List<TmdbMovie> getResults() {
        return results;
    }

    public void setResults(List<TmdbMovie> results) {
        this.results = results;
    }


}
