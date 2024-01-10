package com.sellbycar.marketplace.ad;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sellbycar.marketplace.car.CarDAO;
import com.sellbycar.marketplace.car.dao.Region;
import com.sellbycar.marketplace.image.ImageDAO;
import com.sellbycar.marketplace.user.UserDAO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "advertisements")
@Getter
@Setter
@NoArgsConstructor
public class AdvertisementDAO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description")
    private String description;

    @Column(name = "owner_name")
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "date_added", columnDefinition = "DATE")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dateAdded;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST
            , CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserDAO user;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private CarDAO car;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ImageDAO> images = new ArrayList<>();

    public void addImageToAdvertisement(ImageDAO image) {
        image.setAdvertisement(this);
        images.add(image);
    }
}
