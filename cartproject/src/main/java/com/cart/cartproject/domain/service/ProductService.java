package com.cart.cartproject.domain.service;

import com.cart.cartproject.application.dto.requestDto.ProductDTO.AddProductDTO;
import com.cart.cartproject.application.dto.requestDto.ProductDTO.HoldDTO;
import com.cart.cartproject.application.dto.requestDto.ProductDTO.UpdateProductDTO;
import com.cart.cartproject.application.dto.responseDto.ProductDTO.ViewProductDTO;
import com.cart.cartproject.domain.entity.Product;
import com.cart.cartproject.domain.entity.ProductStatus;
import com.cart.cartproject.domain.entity.User;
import com.cart.cartproject.external.ProductRepository;
import com.cart.cartproject.external.ProductStatusRepository;
import com.cart.cartproject.external.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductStatusRepository productStatusRepository;

    // View a product by ID
    public ResponseEntity<?> getProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            ViewProductDTO viewProductDTO = new ViewProductDTO();
            viewProductDTO.setId(product.getId());
            viewProductDTO.setProductCode(product.getProductCode());
            viewProductDTO.setProductName(product.getProductName());
            viewProductDTO.setProductDescription(product.getProductDescription());
            viewProductDTO.setQuantity(product.getQuantity());
            viewProductDTO.setPrice(product.getPrice());
            return ResponseEntity.ok(viewProductDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    // View all products
    public ResponseEntity<List<ViewProductDTO>> getAllProduct() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<ViewProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ViewProductDTO viewProductDTO = new ViewProductDTO();
            viewProductDTO.setId(product.getId());
            viewProductDTO.setProductCode(product.getProductCode());
            viewProductDTO.setProductName(product.getProductName());
            viewProductDTO.setProductDescription(product.getProductDescription());
            viewProductDTO.setQuantity(product.getQuantity());
            viewProductDTO.setPrice(product.getPrice());
            productDTOs.add(viewProductDTO);
        }
        return ResponseEntity.ok(productDTOs);
    }

    // Add a new product
    public ResponseEntity<String> postProduct(AddProductDTO addProductDTO) {
        // Validate product code
        if (addProductDTO.getProductCode() == null || addProductDTO.getProductCode().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product code is required");
        }

        // Validate product name
        if (addProductDTO.getProductName() == null || addProductDTO.getProductName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product name is required");
        }

        // Validate quantity
        if (addProductDTO.getQuantity() == null || addProductDTO.getQuantity() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity must be a positive number");
        }

        // Validate price
        if (addProductDTO.getPrice() == null || addProductDTO.getPrice() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Price must be a positive number");
        }

        // Check if product code already exists
        if (productRepository.existsByProductCode(addProductDTO.getProductCode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product code already exists");
        }

        // Fetch the User entity
        Optional<User> optionalUser = userRepository.findById(addProductDTO.getProductAddedBy());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        User user = optionalUser.get();

        // Fetch the ProductStatus entity
        ProductStatus productStatus = productStatusRepository.findByproductStatusCode(addProductDTO.getProductStatus())
                .orElseThrow(() -> new RuntimeException("Product status not found: " + addProductDTO.getProductStatus()));

        // Create and save the product
        Product product = new Product();
        product.setProductCode(addProductDTO.getProductCode());
        product.setProductName(addProductDTO.getProductName());
        product.setProductDescription(addProductDTO.getProductDescription());
        product.setUrl(addProductDTO.getUrl());
        product.setQuantity(addProductDTO.getQuantity());
        product.setPrice(addProductDTO.getPrice());
        product.setProductStatus(productStatus); // Set the ProductStatus entity
        product.setProductAddedBy(user); // Set the User entity

        try {
            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Update a product
    public ResponseEntity<String> putProduct(UpdateProductDTO updateProductDTO) {
        // Validate product ID
        if (updateProductDTO.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product ID is required");
        }

        // Fetch the product
        Optional<Product> optionalProduct = productRepository.findById(updateProductDTO.getId());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        Product product = optionalProduct.get();

        ProductStatus productStatus = productStatusRepository.findByproductStatusCode(updateProductDTO.getProductStatus())
                .orElseThrow(() -> new RuntimeException("Product status not found"));

        // Update fields if provided
        if (updateProductDTO.getProductCode() != null) {
            product.setProductCode(updateProductDTO.getProductCode());
        }
        if (updateProductDTO.getProductName() != null) {
            product.setProductName(updateProductDTO.getProductName());
        }
        if (updateProductDTO.getProductDescription() != null) {
            product.setProductDescription(updateProductDTO.getProductDescription());
        }
        if (updateProductDTO.getQuantity() != null) {
            product.setQuantity(updateProductDTO.getQuantity());
        }
        if (updateProductDTO.getPrice() != null) {
            product.setPrice(updateProductDTO.getPrice());
        }
        if (updateProductDTO.getProductStatus() != null) {
            product.setProductStatus(productStatus);
        }

        try {
            productRepository.save(product);
            return ResponseEntity.ok("Product updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Hold a product
    public ResponseEntity<String> holdProduct(HoldDTO holdDTO) {
        // Validate product code
        if (holdDTO.getProductCode() == null || holdDTO.getProductCode().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product code is required");
        }

        // Fetch the product
        Optional<Product> optionalProduct = productRepository.findByProductCode(holdDTO.getProductCode());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        ProductStatus productStatus = productStatusRepository.findByproductStatusCode(holdDTO.getProductStatusCode())
                .orElseThrow(() -> new RuntimeException("Product status not found"));

        Product product = optionalProduct.get();
        product.setProductStatus(productStatus);

        try {
            productRepository.save(product);
            return ResponseEntity.ok("Product status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Delete a product
    public ResponseEntity<String> deleteProduct(Long id) {
        // Validate product ID
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product ID is required");
        }

        // Fetch the product
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        try {
            productRepository.delete(optionalProduct.get());
            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
