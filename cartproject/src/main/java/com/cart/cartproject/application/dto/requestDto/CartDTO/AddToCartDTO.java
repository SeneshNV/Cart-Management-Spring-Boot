package com.cart.cartproject.application.dto.requestDto.CartDTO;


import lombok.Data;

@Data
public class AddToCartDTO {
    private Long productId;
    private Long userId;
}
