package com.sellbycar.marketplace.persistance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sellbycar.marketplace.persistance.enums.EngineType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "autos")
@Getter
@Setter
@NoArgsConstructor
public class Auto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String vinNumber;

    @Column
    private String yearToCreate;

    @Column
    private String carNumber;

    @Column
    private Integer mileage;

    @JsonBackReference
    @OneToOne(mappedBy = "auto",fetch = FetchType.EAGER)
    private Advertisement advertisement;

    @Column
    @Enumerated(value = EnumType.STRING)
    private EngineType engineType;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_mark_id")
    private CarMark carMark;
}
