package com.cinetime.entity;


import com.cinetime.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private  Double amount; //Tutar

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus; //Pending , Success ,Failed

    private LocalDateTime paymentDate;

    //Odeme hangi kullaniciya ait
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    //Odeme hangi bilete ait
    @OneToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;


}
