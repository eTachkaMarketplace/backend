package com.sellbycar.marketplace.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "cars_marks")
@Getter
@Setter
@NoArgsConstructor
public class CarMark implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_model_id", referencedColumnName = "id")
    private CarModel carModel;
}