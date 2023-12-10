package com.sellbycar.marketplace.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "drive_types")
@Data
public class DriveType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "driveType", fetch = FetchType.EAGER)
    private List<Car> cars;
}
