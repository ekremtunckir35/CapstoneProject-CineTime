package com.cinetime.repository;

import com.cinetime.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {

    //1-Kullanici giris yaparken(Login) email ile bulmak icin
    //SQL de---> SELECT * FROM users WHERE email = ?

    Optional<User> findByEmail(String email);


    //2- Yeni kayit olurken(Register)"Bu email zaten var mi?"kontrolu icin
    //SQL de---> SELECT  COUNT(*) > 0 FROM users WHERE email = ?

    boolean existsByEmail(String email);

}
