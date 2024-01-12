package com.sellbycar.marketplace.user;

import com.sellbycar.marketplace.ad.AdvertisementDAO;
import com.sellbycar.marketplace.image.ImageDAO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serializable;
import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class UserDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JdbcTypeCode(Types.BIGINT)
    @JoinColumn(name = "photo")
    private ImageDAO photo;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "unique_code", unique = true)
    private String uniqueCode;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(Types.ARRAY)
    @Column(name = "authorities")
    private Set<UserAuthority> authorities;

    @Column(name = "created_timestamp")
    private Instant createdTimestamp;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_favorite_advertisements",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "advertisement_id")
    )
    private Set<AdvertisementDAO> favorites;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<AdvertisementDAO> advertisements;

    public void addFavorite(AdvertisementDAO advertisement) {
        if (favorites == null) {
            favorites = Set.of(advertisement);
        } else {
            favorites.add(advertisement);
        }
    }

    public void removeFavorite(AdvertisementDAO advertisement) {
        if (favorites != null) {
            favorites.remove(advertisement);
        }
    }
}
