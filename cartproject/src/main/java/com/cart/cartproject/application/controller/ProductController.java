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
    public String test(){
        return "Testing";
    }

    // Buyers view product
//    @GetMapping("/view/{id}")
//    public ResponseEntity<String> getProduct(@PathVariable Long id) {
//        return productService.getProduct(id);
//    }

    // Buyers view all products
    @GetMapping("/view")
    public ResponseEntity<?> getAllProduct() {
        return productService.getAllProduct();
    }

    // Only admins (admin1, admin2) add products
    @PostMapping("/add")
//    @PreAuthorize("hasAnyRole('ADMIN1', 'ADMIN2')")
    public ResponseEntity<String> addProduct(@RequestBody AddProductDTO addProductDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated User: " + authentication.getName());
        System.out.println("Authenticated Roles: " + authentication.getAuthorities());

        return productService.postProduct(addProductDTO);
    }

    // Only admins (admin1, admin2) update products
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN1', 'ADMIN2')")
    public ResponseEntity<String> updateProduct(@RequestBody UpdateProductDTO updateProductDTO) {
        return productService.putProduct(updateProductDTO);
    }

    // Only admins (admin1, admin2) hold products
    @PutMapping("/hold")
    @PreAuthorize("hasAnyRole('ADMIN1', 'ADMIN2')")
    public ResponseEntity<String> holdProduct(@RequestBody HoldDTO holdDTO) {
        return productService.holdProduct(holdDTO);
    }

    // Only admins (admin1, admin2) delete products
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN1', 'ADMIN2')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}
