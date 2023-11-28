package com.sellbycar.marketplace.models.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String email;
    private String phone;
}
