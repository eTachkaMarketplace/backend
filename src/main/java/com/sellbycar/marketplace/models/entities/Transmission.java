package com.sellbycar.marketplace.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "transmissions")
@Data
public class Transmission
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "transmission", fetch = FetchType.EAGER)
    private List<Car> cars;
}
