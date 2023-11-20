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


    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private Auto auto;

    @ElementCollection(targetClass = Transmission.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "advertisement_transmissions", joinColumns = @JoinColumn(name = "advertisement_id"))
    @Enumerated(EnumType.STRING)
    private Set<Transmission> transmissions = new HashSet<>();

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Images> images = new ArrayList<>();

}
