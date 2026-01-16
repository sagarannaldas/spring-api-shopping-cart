package com.sagarannaldas.shopping.cart.dtos;

import lombok.Data;

@Data  // - getter,setter,tostring,hashcode
public class RegisterUserRequest {
    private String name;
    private String email;
    private String password;
}
