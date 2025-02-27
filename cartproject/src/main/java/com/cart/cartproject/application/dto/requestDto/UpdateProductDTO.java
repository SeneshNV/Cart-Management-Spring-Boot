package com.cart.cartproject.application.dto.requestDto;

import lombok.Data;

@Data
public class UpdateProductDTO {
    private Long id;
    private String productCode;
    private String productName;
    private String productDescription;
    private String url;
    private Integer quantity;
    private Double price;
    private String productStatus;
}
