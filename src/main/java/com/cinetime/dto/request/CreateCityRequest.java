package com.cinetime.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
//kullanicidanyani Swaggerden veri alirken kullanacagimiz sinif

@Data
public class CreateCityRequest {

    @NotBlank(message = "Sehir  adi bos birakilmaz")
    private String name;
}
