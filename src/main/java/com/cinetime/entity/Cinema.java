package com.cinetime.entity;
//Alisveri merkezinde veya caddedeki ofiziksel sinema binasi

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cinemas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(nullable = false)
    private String name;

    private String address; //acik adres

    //iliski bir sinema bir sehre aittir

    @ManyToOne
    @JoinColumn(name = "city_id",nullable = false)
    private City city;
}
