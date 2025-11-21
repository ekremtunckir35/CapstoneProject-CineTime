package com.cinetime.dto.request;

//Kullanicidan veri alacagimiz istek kutusu


import com.cinetime.entity.enums.MovieStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMovieRequest {

    @NotBlank(message = "Film adı boş bırakılamaz")
    private String title;

    @NotBlank(message = "Film özeti boş bırakılamaz")
    private String  summary;

    @NotNull(message = "Film vizyon tarihi zorunludur")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate releaseDate;

    @NotNull(message = "Film süresi zorunludur")
    @Min(value = 1,message = "Sure en az 1 dk. olmalidir")
    private  Integer duration;

    @NotBlank(message = "Yönetmen bilgisi zorunludur")
    private String director;

    private List<String> cast;
    private List<String> formats;

    @NotBlank(message = "Film türü zorunludur")
    private String genre;

    private String poster;

    // 0: COMING_SOON, 1:IN_THEATERS , 2:PRESALE
       private MovieStatus status;

}
