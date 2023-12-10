package com.sellbycar.marketplace.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "cars_models")
@Getter
@Setter
@NoArgsConstructor
public class CarModel implements Serializable {
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
