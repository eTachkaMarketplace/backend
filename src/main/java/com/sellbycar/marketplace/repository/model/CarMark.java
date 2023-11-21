package com.sellbycar.marketplace.repository.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car_marks")
@Getter
@Setter
@NoArgsConstructor
public class CarMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

//    @OneToOne
//    @JoinColumn(name = "image_id")
//    private Images images;

    @OneToMany(mappedBy = "carMark", cascade = CascadeType.ALL)
    private List<CarModel> carModel = new ArrayList<>();

}
