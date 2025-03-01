package com.cart.cartproject.application.dto.requestDto.ProductDTO;

import lombok.Data;

@Data
public class UpdateProductDTO {
    private Long id;
    private String productCode;
    private String productName;
    private String productDescription;
    private Integer quantity;
    private Double price;
    private String productStatus;
    private Long productAddedBy;
}
