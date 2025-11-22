package com.cinetime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @ManyToOne
      @JoinColumn(name = "user_id",nullable = false)
      private  User user;

      @ManyToOne
      @JoinColumn(name = "movie_id")
      private Movie movie;

      @ManyToOne
      @JoinColumn(name = "cinema_id")
      private  Cinema cinema;

}
