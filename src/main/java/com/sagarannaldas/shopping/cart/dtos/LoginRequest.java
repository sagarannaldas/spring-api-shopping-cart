package com.sagarannaldas.shopping.cart.dtos;

import com.sagarannaldas.shopping.cart.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "email is required")
    @Email(message = "Email must be valid")
    @Lowercase(message = "email must be in lower case")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 25, message = "password must be at least 6 to 25 characters long")
    private String password;
}
