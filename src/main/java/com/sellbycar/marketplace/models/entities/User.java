package com.sellbycar.marketplace.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sellbycar.marketplace.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "unique_email_constraint"))
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column(unique = true)
    private String email;

    @JsonIgnore
    @Column
    private String password;
    @Column
    private String phone;

    @JsonIgnore
    @Column
    private Boolean enabled;
    @Column
    private String uniqueCode;
    @Column
    private String photo;

    public User(String email) {
        this.email = email;
    }

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Advertisement> advertisement = new ArrayList<>();

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> authority = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "users_advertisements",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "advertisements_id")
    )
    private Set<Advertisement> favoriteCars;
}
