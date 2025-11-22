package com.cinetime.repository;

import com.cinetime.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

       //Isme gore resim bulmak icin
      Optional<Image> findByName(String name);
}
