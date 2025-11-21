package com.cinetime.entity;


//Bu class veritabaninda movies tablosunu olusturacak

import com.cinetime.entity.enums.MovieStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie  extends BaseEntity {   //creatAt ve updateAt otomatik gelir

       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;

       @Column(nullable = false,length = 100)
       private String title;   //[Cite:8]

     @Column(nullable = false,length = 20)
     private String slug;

     @Column(nullable = false)
     private String summary; //ozet [Cite:8]

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate releaseDate; // vizyon tarihi

    @Column(nullable = false)
    private Integer duration; //Dakika

    private  Double rating; //puan

    @Column(nullable = false)
    private String director; //yonetmen

    //Liste verilerini veritabaninda tutmak icin @ElementCollection kullanilir.
    @ElementCollection
    @CollectionTable(name = "movie_cast", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "movie_actor")
    private List<String> cast; //oyuncular

    @ElementCollection
    private List<String>formats; // orn:IMAX

    @Column(nullable = false)
    private String genre; //tur

    @Column(nullable = false)
    private String poster; //Afis yolu

    @Enumerated(EnumType.ORDINAL) //veritabanina 0,1,2 olarak kaydeder
    @Column(nullable = false)
    private MovieStatus status;

    private String specialHalls; //ozel salonlar
}
