package com.sellbycar.marketplace.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "colors")
@Data
public class Color
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "color", fetch = FetchType.EAGER)
    private List<Car> cars;
}
