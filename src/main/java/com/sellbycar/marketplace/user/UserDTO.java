package com.sellbycar.marketplace.user;

import lombok.Data;

import java.time.Instant;

@Data
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String photo;
    private Instant createdTimestamp;
}
