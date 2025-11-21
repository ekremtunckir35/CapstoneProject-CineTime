package com.cinetime.repository;


import com.cinetime.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// seans deposu


@Repository
public interface ShowtimeRepository  extends  JpaRepository<Showtime, Long> {

    //Bir filmin tum seasnlarini getirmek icin

    List<Showtime>findAllByMovie_Id(Long movieId);
}
