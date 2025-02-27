package com.cart.cartproject.application.dto.responseDto.CartDTO;

import com.cart.cartproject.domain.entity.Product;
import com.cart.cartproject.domain.entity.User;
import lombok.Data;

@Data
public class ViewCartDTO {
    private Long id;
    private Product product;
    private User user;
}
