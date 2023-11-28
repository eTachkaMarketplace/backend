package com.sellbycar.marketplace.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Column(unique = true)
    private String email;
    @Column
    private String password;
    @Column
    private String phone;
    @Column
    private Boolean enabled;
    @Column
    private String uniqueCode;

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Advertisement> advertisement = new ArrayList<>();

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> authority = new HashSet<>();

    public User(String email) {
        this.email = email;
    }

}
