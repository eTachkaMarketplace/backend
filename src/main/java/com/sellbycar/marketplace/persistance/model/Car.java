package com.sellbycar.marketplace.persistance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sellbycar.marketplace.persistance.enums.EngineType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
public class Car implements Serializable {

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
    @OneToOne(mappedBy = "car", fetch = FetchType.EAGER)
    private Advertisement advertisement;

    @ElementCollection(targetClass = EngineType.class)
    @CollectionTable(name = "engine_types", joinColumns = @JoinColumn(name = "auto_id"))
    @Enumerated(EnumType.STRING)
    private Set<EngineType> engineType = new HashSet<>();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_mark_id")
    private CarMark carMark;
}
