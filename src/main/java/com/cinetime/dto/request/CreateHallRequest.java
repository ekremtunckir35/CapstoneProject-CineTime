package com.cinetime.dto.request;

//Kullanicidan (Swaggerden)veri aliirken kullanacagimiz sinif

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateHallRequest {

    @NotBlank(message = "Salon adi bos birakilmaz")
    private String name;

    @NotNull
    private  Integer seatCapacity;

    private Boolean isSpecial; //IMAX VB..

    @NotNull(message = "Sinema ID'si bos birakilmaz")
    private Long cinemaId; //Hasngi sinemada oldugunu ID ile baglayacagiz

}
