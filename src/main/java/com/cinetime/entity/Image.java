package com.cinetime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {  //Base entity gerek yok.Cunku sadece depo gorevi gorecek

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;

        @Column(nullable = false)
        private  String name;  //Resim adi


    private String type;  //Dosya turu(image/jpeg/image/png)


    //LOB = Large Object resim verisi burada tutulacak
    //Length parametresi ,buyuk dosyalar icin ayrilir

    @Lob
    @Column(name = "imagedata", length = 5000000)//50MB limit
    private byte[] imageData;
}
