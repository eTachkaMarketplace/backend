package com.sellbycar.marketplace.persistance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car_marks")
@Getter
@Setter
@NoArgsConstructor
public class CarMark implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

//    @OneToMany(mappedBy = "carMark", cascade = CascadeType.ALL)
//    private List<CarModel> carModel = new ArrayList<>();


}
