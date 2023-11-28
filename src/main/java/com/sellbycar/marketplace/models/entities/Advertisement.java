package com.sellbycar.marketplace.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sellbycar.marketplace.models.enums.Transmission;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "advertisements")
@Getter
@Setter
@NoArgsConstructor
public class Advertisement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column
    private String name;

    @Column
    private Integer price;

    @Column
    private boolean change = false;

    @Column
    private boolean bargain = false;

    @Column
    private boolean crashed = false;


    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Car car;

    @ElementCollection(targetClass = Transmission.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "transmissions", joinColumns = @JoinColumn(name = "advertisement_id"))
    @Enumerated(EnumType.STRING)
    private Set<Transmission> authority = new HashSet<>();

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Images> images = new ArrayList<>();

}
