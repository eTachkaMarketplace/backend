package com.sellbycar.marketplace.car;

import com.sellbycar.marketplace.ad.AdvertisementDAO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cars")
public class CarDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "vin")
    private String vin;

    @Column(name = "year")
    private Integer year;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "mileage")
    private Integer mileage;

    @Column(name = "transmission_type")
    private String transmissionType;

    @Column(name = "engine_type")
    private String engineType;

    @Column(name = "engine_volume")
    private Double engineVolume;

    @Column(name = "technical_state")
    private String technicalState;

    @Column(name = "body_type")
    private String bodyType;

    @Column(name = "drive_type")
    private String driveType;

    @Column(name = "color")
    private String color;

    @OneToOne(mappedBy = "car", fetch = FetchType.LAZY)
    private AdvertisementDAO advertisement;
}
