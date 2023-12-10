package com.sellbycar.marketplace.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars_marks")
@Getter
@Setter
@NoArgsConstructor
public class CarMark implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "carMark", cascade = CascadeType.ALL)
    private List<CarModel> carModel = new ArrayList<>();
}