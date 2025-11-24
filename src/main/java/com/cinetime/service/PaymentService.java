package com.cinetime.service;

import com.cinetime.entity.Payment;
import com.cinetime.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    // Save Payment (Called from TicketService)
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    // Get Payment Details by ID
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found!"));
    }

    // Get Payments by User
    public List<Payment> getPaymentsByUser(Long userId) {
        return paymentRepository.findAllByUser_Id(userId);
    }
}