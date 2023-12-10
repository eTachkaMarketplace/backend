package com.sellbycar.marketplace.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "engines")
@Data
public class Engine
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "engine", fetch = FetchType.EAGER)
    private List<Car> cars;
}
