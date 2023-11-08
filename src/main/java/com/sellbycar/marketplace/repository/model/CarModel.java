package com.sellbycar.marketplace.repository.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "car_models")
@Getter
@Setter
@NoArgsConstructor
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "car_mark_id")
    private CarMark carMark;
}
