package com.sellbycar.marketplace.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 2, message = "login.minzize")
    @NotNull
    private String username;

    @Size(min = 6, message = "password.minsize")
    @Size(max = 72, message = "password.maxsize")
    @Pattern(regexp = "^\\S*$", message = "password.spaces")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$^#!%*?&()])[A-Za-z\\d@$^#!%*?&()]+$", message = "password.pattern")
    private String password;

    @NotBlank(message = "email.notblank")
    @Size(max = 50, message = "email.maxsize")
    @Email(message = "email.email", regexp = "^(?=.{1,50}$)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$")
    private String email;
    @NotBlank
    @Size(max = 13)
    private String phone;


}
