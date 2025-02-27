package com.cart.cartproject.application.dto.responseDto.UserDTO;

import lombok.Data;

@Data
public class ViewUsersDTO {
    private Long id;
    private String username;
    private String email;
    private String userRole;
    private boolean activeStatus;
    private String accountStatus;
}
