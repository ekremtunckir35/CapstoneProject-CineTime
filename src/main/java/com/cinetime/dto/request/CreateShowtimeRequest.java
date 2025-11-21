package com.cinetime.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data; // <-- Bu import GETTER'ları üretir

import java.time.LocalDate;
import java.time.LocalTime;

@Data // <-- Bu anotasyon ŞART
public class CreateShowtimeRequest {

    @NotNull(message = "Film ID zorunludur")
    private Long movieId;

    @NotNull(message = "Salon ID zorunludur")
    private Long hallId;

    @NotNull(message = "Tarih zorunludur")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    @NotNull(message = "Başlangıç saati zorunludur")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
}