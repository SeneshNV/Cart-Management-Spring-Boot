package com.cart.cartproject.application.dto.responseDto.ProductDTO;

import lombok.Data;

@Data
public class ViewProductDTO {
    private Long id;
    private String productCode;
    private String productName;
    private String productDescription;
    private Integer quantity;
    private Double price;
    private String productStatus;
}
