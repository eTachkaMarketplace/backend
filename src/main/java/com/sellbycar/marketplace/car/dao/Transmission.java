package com.sellbycar.marketplace.car.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sellbycar.marketplace.car.CarDAO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "transmissions")
@Data
public class Transmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "transmission", fetch = FetchType.EAGER)
    private List<CarDAO> cars;
}
