package com.cinetime.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbMovie {

    private  Long id; //TMDB ID si

    private String title;

    private String overview; //OZET


    @JsonProperty("release_date")
    private String releaseDate; //YYYY-MM-DD formatinda gelir

    @JsonProperty("poster_path")
    private String posterPath;

    //TMDB turleri ID olarak verir(Daha basit olsun diye bunu kullanmayip
    // manuel atayacagiz)
    private List<Integer> genreIds;

    //Manuel Getter'ler (Garanti olsun)

    public String getTitle(){return title;}
    public String getOverview(){return overview;}
    public String getReleaseDate(){return releaseDate;}
    public String getPosterPath(){return posterPath;}



}
