package com.cinetime.repository;

import com.cinetime.entity.Movie;
import com.cinetime.entity.enums.MovieStatus; // Bu import önemli!
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {


      // Spring Data JPA bu isme bakarak SQL'i otomatik yazar:
      // SELECT * FROM movies WHERE status = ?

      List<Movie> findAllByStatus(MovieStatus status);

             // --------------------------------

      // Aynı isimde film var mı kontrolü
      boolean existsByTitle(String title);

             // --------------------------------


      //ARAMA VE SAYFALAMA
      //ContaigingIgnoreCase :Buyuk/kucuk harf fark etmeksizin,icinde kelime geceni bul
      Page<Movie>findByTitleContainingIgnoreCase(String title, Pageable pageable);
}