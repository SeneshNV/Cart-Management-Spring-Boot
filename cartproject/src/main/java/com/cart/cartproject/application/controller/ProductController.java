package com.cart.cartproject.application.controller;

import com.cart.cartproject.application.dto.requestDto.ProductDTO.AddProductDTO;
import com.cart.cartproject.application.dto.requestDto.ProductDTO.HoldDTO;
import com.cart.cartproject.application.dto.requestDto.ProductDTO.UpdateProductDTO;
import com.cart.cartproject.application.dto.responseDto.ProductDTO.ViewProductDTO;
import com.cart.cartproject.domain.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    // Buyers can view products
    @GetMapping("/view/{id}")
    public ResponseEntity<ViewProductDTO> getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    // Buyers can view all products
    @GetMapping("/view")
    public ResponseEntity<List<ViewProductDTO>> getAllProduct() {
        return productService.getAllProduct();
    }

    // Only admins (admin1, admin2) can add products
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN1', 'ADMIN2')")
    public ResponseEntity<AddProductDTO> addProduct(@RequestBody AddProductDTO addProductDTO) {
        return productService.postProduct(addProductDTO);
    }

    // Only admins (admin1, admin2) can update products
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN1', 'ADMIN2')")
    public ResponseEntity<UpdateProductDTO> updateProduct(@RequestBody UpdateProductDTO updateProductDTO) {
        return productService.putProduct(updateProductDTO);
    }

    // Only admins (admin1, admin2) can hold products
    @PutMapping("/hold")
    @PreAuthorize("hasAnyRole('ADMIN1', 'ADMIN2')")
    public ResponseEntity<HoldDTO> holdProduct(@RequestBody HoldDTO holdDTO) {
        return productService.holdProduct(holdDTO);
    }

    // Only admins (admin1, admin2) can delete products
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN1', 'ADMIN2')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}
