package com.cart.cartproject.application.dto.requestDto;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String username;
    private String email;
    private String password;
}
