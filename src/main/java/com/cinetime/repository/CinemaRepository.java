package com.cinetime.repository;

import com.cinetime.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

     //Sehre gore sinemalar getirme
    List<Cinema> findAllByCity_Id(Long cityId);

}
