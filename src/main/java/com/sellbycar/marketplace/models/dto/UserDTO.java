package com.sellbycar.marketplace.models.dto;

import lombok.Data;

@Data
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String photo;
}
