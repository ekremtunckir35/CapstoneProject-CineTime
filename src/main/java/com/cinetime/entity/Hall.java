package com.cinetime.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "halls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hall  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; //ornek : "Salon 1 "ÏMAX Hall "

    @Column(nullable = false)
    private Integer seatCapacity; // koltuk kapasitesi sayisi

    @Builder.Default
    private Boolean isSpecial =false; //IMAX,4DX  gibi ozel bir salon mu

    // ILISKI yani Bir salon ,Bir sinemaya aittir
    @ManyToOne
    @JoinColumn(name = "cinema_id",nullable = false)
    private Cinema cinema;
}
