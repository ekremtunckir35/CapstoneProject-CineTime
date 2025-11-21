package com.cinetime.entity;
//

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {  //City tablosu sabit oldugundan BaseEntity gerek duyulmadi,ama ekleyebiliriz

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 50)
    private  String name;  //orn:Istanbul,Ankara gibi
}
