package com.sellbycar.marketplace.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "volume")
    private String volume;

    @JsonIgnore
    @OneToMany(mappedBy = "engine", fetch = FetchType.EAGER)
    private List<Car> cars;
}
