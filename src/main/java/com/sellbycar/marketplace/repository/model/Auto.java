package com.sellbycar.marketplace.repository.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Embeddable
@Getter
@Setter
public class Auto {

    @Column
    private String vinNumber;

    @Column
    private String yearToCreate;

    @Column
    private String carNumber;

    @Column
    private Integer mileage;


    @ManyToOne
    @JoinColumn(name = "car_mark_id")
    private CarMark carMark;
}
