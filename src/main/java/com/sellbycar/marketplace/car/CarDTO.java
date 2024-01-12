package com.sellbycar.marketplace.car;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarDTO {

    private String brand;
    private String model;
    private String vin;
    private Integer year;
    private BigDecimal price;
    private String licensePlate;
    private Integer mileage;
    private String transmissionType;
    private String engineType;
    private Double engineVolume;
    private String technicalState;
    private String bodyType;
    private String driveType;
    private String color;
}
