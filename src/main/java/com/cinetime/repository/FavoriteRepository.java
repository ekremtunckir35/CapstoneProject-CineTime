package com.cinetime.repository;

import com.cinetime.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    //Kullanicinin tum favorilerini getirme

    List<Favorite> findAllByUser_Id(Long userId);

    //Ayni favorinin  daha once eklenip eklenmediginin kontrolu
    //Ayni kayittan iki defa olmamasi icin

    boolean existsByUser_Id_AndMovie_Id(Long userId, Long movieId);


}
