package com.cart.cartproject.application.dto.responseDto;

import lombok.Data;

@Data
public class ViewUsersDTO {
    private Integer id;
    private String username;
    private String email;
    private String userRole;
    private String activeStatus;
    private String accountStatus;
}
