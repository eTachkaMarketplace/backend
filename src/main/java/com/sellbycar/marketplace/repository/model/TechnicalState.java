package com.sellbycar.marketplace.repository.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class TechnicalState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String name;

    
}
