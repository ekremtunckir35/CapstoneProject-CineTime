package com.cinetime.repository;

import com.cinetime.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {


   //Sinemaya gore salonlari getir

    List<Hall> findAllByCinema_Id(Long cinemaId);

     //Ozel salonlari getirme(IMAX vbb)
    List<Hall> findAllByIsSpecialTrue();
}
