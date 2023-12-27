package com.sellbycar.marketplace.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "vin_number")
    private String vinNumber;

    @Column(name = "year_to_create")
    private String yearToCreate;

    @Column(name = "car_number")
    private String carNumber;

    @Column(name = "mileage")
    private Integer mileage;

    @JsonBackReference
    @OneToOne(mappedBy = "car", fetch = FetchType.EAGER)
    private Advertisement advertisement;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "transmission_id", referencedColumnName = "id")
    private Transmission transmission;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "engine_id", referencedColumnName = "id")
    private Engine engine;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "technical_state_id", referencedColumnName = "id")
    private TechnicalState technicalState;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "body_type_id", referencedColumnName = "id")
    private BodyType bodyType;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "drive_type_id", referencedColumnName = "id")
    private DriveType driveType;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    private Color color;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_mark_id", referencedColumnName = "id")
    private CarMark carMark;
}
