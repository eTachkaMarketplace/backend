package com.sellbycar.marketplace.car.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sellbycar.marketplace.car.CarDAO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "body_types")
@Data
public class BodyType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "bodyType", fetch = FetchType.EAGER)
    private List<CarDAO> cars;
}
