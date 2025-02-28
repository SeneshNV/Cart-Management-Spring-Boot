package com.cart.cartproject.application.controller;

import com.cart.cartproject.application.dto.requestDto.CartDTO.AddToCartDTO;
import com.cart.cartproject.application.dto.requestDto.CartDTO.DeleteCartDTO;
import com.cart.cartproject.application.dto.responseDto.CartDTO.ViewCartDTO;
import com.cart.cartproject.domain.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
public class CartController {
    private CartService cartService;


    @PostMapping("/add")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<AddToCartDTO> addToCart(@RequestBody AddToCartDTO addToCartDTO) {
        return cartService.addToCart(addToCartDTO);
    }

    // Buyers can delete cart
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        return cartService.deleteCart(id);
    }

    // Admins view all carts
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN1', 'ADMIN2')")
    public ResponseEntity<List<ViewCartDTO>> viewAllCarts() {
        return cartService.getAllCarts();
    }

    // Buyers cart
    @GetMapping("/me")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<List<ViewCartDTO>> viewUserCart() {
        return cartService.getUserCart();
    }

    // Admins can view the most selected products
    @GetMapping("/most-selected")
    @PreAuthorize("hasAnyRole('ADMIN1', 'ADMIN2')")
    public ResponseEntity<List<Object[]>> getMostSelectedProducts() {
        return cartService.getMostSelectedProducts();
    }
}
