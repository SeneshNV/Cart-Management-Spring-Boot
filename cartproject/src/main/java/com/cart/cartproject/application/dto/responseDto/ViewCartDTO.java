package com.cart.cartproject.application.dto.responseDto;

import com.cart.cartproject.domain.entity.Product;
import com.cart.cartproject.domain.entity.User;
import lombok.Data;

@Data
public class ViewCartDTO {
    private Long id;
    private Product productCode;
    private User productAddedBy;
}
