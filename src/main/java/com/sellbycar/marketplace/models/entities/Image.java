package com.sellbycar.marketplace.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private Long size;
    @Column
    private String contentType;
    @Column
    private boolean isPreviewImage;
    @Lob
    private byte[] resource;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;
}
