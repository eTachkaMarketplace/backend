package com.sellbycar.marketplace.car.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sellbycar.marketplace.car.CarDAO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "colors")
@Data
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "color", fetch = FetchType.EAGER)
    private List<CarDAO> cars;
}
