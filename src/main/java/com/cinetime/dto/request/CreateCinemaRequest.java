package com.cinetime.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
//Kullanicidan(Swaggerdan)veri alirken kullanacagimiz sinif

@Data
public class CreateCinemaRequest {

    @NotBlank(message = "Sinema adi bos birakilmaz")
     private String name;


    private String address;

    @NotNull(message = "Sehir ID'si bos birakilmaz")
    private Long cityId;  //Hangi sehirde oldugunu ID ile baglayacagiz
}
