package com.sellbycar.marketplace.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    @Size(min = 2, max = 20)
    @NotNull
    private String name;

    @NotBlank
    @Size(min = 5, max = 40)
    @NotNull
    private String password;

    @NotBlank
    @Size(max = 50)
    @Email
    @NotNull
    private String email;
}
