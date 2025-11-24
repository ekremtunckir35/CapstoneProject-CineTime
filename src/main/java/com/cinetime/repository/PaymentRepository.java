package com.cinetime.repository;

import com.cinetime.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {


    List<Payment> findAllByUser_Id(Long userId);
}