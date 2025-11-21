package com.cinetime.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BuyTicketRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long showtimeId;

    @NotNull
    private String seatLetter;

    @NotNull
    private Integer seatNumber;

    @NotNull
    private  Double price;

}
