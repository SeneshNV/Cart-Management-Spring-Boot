package com.cart.cartproject.application.dto.requestDto.ProductDTO;

import com.cart.cartproject.domain.entity.User;
import lombok.Data;

@Data
public class AddProductDTO {
    private String productCode;
    private String productName;
    private String productDescription;
    private String url;
    private Integer quantity;
    private Double price;
    private String productStatus;
    private Long productAddedBy;
}
