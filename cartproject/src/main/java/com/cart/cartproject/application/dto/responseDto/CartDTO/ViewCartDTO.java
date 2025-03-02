package com.cart.cartproject.application.dto.responseDto.CartDTO;

import com.cart.cartproject.application.dto.responseDto.ProductDTO.ViewProductDTO;
import com.cart.cartproject.application.dto.responseDto.UserDTO.ViewUsersDTO;
import lombok.Data;

@Data
public class ViewCartDTO {
    private Long id;
    private ViewProductDTO product;
    private ViewUsersDTO user;
}
