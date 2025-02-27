package com.cart.cartproject.application.controller;

import com.cart.cartproject.application.dto.requestDto.AddProductDTO;
import com.cart.cartproject.application.dto.requestDto.HoldDTO;
import com.cart.cartproject.application.dto.requestDto.UpdateProductDTO;
import com.cart.cartproject.application.dto.responseDto.ViewProductDTO;
import com.cart.cartproject.domain.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @GetMapping("/view/{id}")
    public ResponseEntity<ViewProductDTO> getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }

    @GetMapping("/view")
    public ResponseEntity<List<ViewProductDTO>> getAllProduct(){
        return productService.getAllProduct();
    }

    @PostMapping("/add")
    public ResponseEntity<AddProductDTO> addProduct(@RequestParam AddProductDTO addProductDTO){
        return productService.postProduct(addProductDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateProductDTO> updateProduct(@RequestParam UpdateProductDTO updateProductDTO){
        return productService.putProduct(updateProductDTO);
    }

    @PutMapping("/hold")
    public ResponseEntity<HoldDTO> holdProduct(@RequestParam HoldDTO holdDTO){
        return productService.holdProduct(holdDTO);
    }


}
