package com.cart.cartproject.application.controller;

import com.cart.cartproject.application.dto.requestDto.ProductDTO.AddProductDTO;
import com.cart.cartproject.application.dto.requestDto.ProductDTO.HoldDTO;
import com.cart.cartproject.application.dto.requestDto.ProductDTO.UpdateProductDTO;
import com.cart.cartproject.application.dto.responseDto.ProductDTO.ViewProductDTO;
import com.cart.cartproject.domain.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/test")
    public String test() {
        return "Testing";
    }

    // Buyers view all products
    @GetMapping("/view")
    public ResponseEntity<List<ViewProductDTO>> getAllProduct() {
        return productService.getAllProduct();
    }

    // Only admins (ADMIN1, ADMIN2) can add products
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN1', 'ADMIN2')")
    public ResponseEntity<String> addProduct(@RequestBody AddProductDTO addProductDTO) {
        return productService.postProduct(addProductDTO);
    }

    // Only admins (ADMIN1, ADMIN2) can update products
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN1', 'ADMIN2')")
    public ResponseEntity<String> updateProduct(@RequestBody UpdateProductDTO updateProductDTO) {
        return productService.putProduct(updateProductDTO);
    }
}
