package com.cinetime.entity;

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
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Başlık uzun olabilir, 255 yaptık
    @Column(nullable = false)
    private String title;

    // Slug 20 karakterden kesinlikle uzun olabilir. Sınırı kaldırdık (Default 255)
    @Column(nullable = false)
    private String slug;

    // Özet 300 karaktere sığmaz. 2000 yaptık (veya columnDefinition="TEXT" de olur)
    @Column(nullable = false, length = 2000)
    private String summary;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate releaseDate;

    @Column(nullable = false)
    private Integer duration;

    private Double rating;

    @Column(nullable = false)
    private String director;

    @ElementCollection
    @CollectionTable(name = "movie_cast", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "movie_actor")
    private List<String> cast;

    @ElementCollection
    private List<String> formats;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String poster;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private MovieStatus status;

    private String specialHalls;
}