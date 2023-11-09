package com.sellbycar.marketplace.repository.model;

import com.sellbycar.marketplace.repository.enums.Transmission;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "advertisements")
@Getter
@Setter
@NoArgsConstructor
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String description;
    @Column
    private String name;
    @Column
    private String year;
    @Column
    private Integer price;
    @Column
    private String carNumber;
    @Column
    private String vinNumber;
    @Column
    private boolean change = false;
    @Column
    private boolean bargain = false;
    @Column
    private boolean crashed = false;
    @Column
    private Integer mileage;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private Auto auto;

    @ElementCollection(targetClass = Transmission.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "transmission", joinColumns = @JoinColumn(name = "advertisement_id"))
    @Enumerated(EnumType.STRING)
    private Set<Transmission> transmissions = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "image_id")
    private List<Images> images = new ArrayList<>();
}
