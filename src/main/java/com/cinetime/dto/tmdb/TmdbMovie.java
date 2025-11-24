package com.cinetime.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data; // <-- Getter/Setter'lar için şart
import java.util.List;

@Data
public class TmdbMovie {

    private Long id;

    private String title;

    private String overview;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
}