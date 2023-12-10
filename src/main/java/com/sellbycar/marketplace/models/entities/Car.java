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
    private Long id;

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

    @ManyToOne
    @JoinTable(
            name = "transmissions",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private Transmission transmission;

    @ManyToOne
    @JoinTable(
            name = "engines",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private Engine engine;

    @ManyToOne
    @JoinTable(
            name = "technical_states",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private TechnicalState technicalState;

    @ManyToOne
    @JoinTable(
            name = "body_types",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private BodyType bodyType;

    @ManyToOne
    @JoinTable(
            name = "drive_types",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private DriveType driveType;

    @ManyToOne
    @JoinTable(
            name = "colors",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private Color color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cars_marks",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private CarMark carMark;
}
