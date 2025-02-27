package com.cart.cartproject.application.dto.requestDto.ProductDTO;

import lombok.Data;

@Data
public class AddProductDTO {
    private Long id;
    private String productCode;
    private String productName;
    private String productDescription;
    private String url;
    private Integer quantity;
    private Double price;

}
