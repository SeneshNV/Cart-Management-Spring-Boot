package com.cart.cartproject.domain.service;

import com.cart.cartproject.application.dto.requestDto.CartDTO.AddToCartDTO;
import com.cart.cartproject.application.dto.requestDto.ProductDTO.AddProductDTO;
import com.cart.cartproject.application.dto.requestDto.ProductDTO.HoldDTO;
import com.cart.cartproject.application.dto.requestDto.ProductDTO.UpdateProductDTO;
import com.cart.cartproject.application.dto.responseDto.ProductDTO.ViewProductDTO;
import com.cart.cartproject.domain.entity.Product;
import com.cart.cartproject.external.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ResponseEntity<ViewProductDTO> getProduct(Long id) {
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
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<ViewProductDTO>> getAllProduct() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
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

    public ResponseEntity<AddProductDTO> postProduct(AddProductDTO addProductDTO) {
        if (productRepository.existsByProductCode(addProductDTO.getProductCode())) {
            return ResponseEntity.status(409).body(null);
        }

        Product product = new Product();
        product.setProductCode(addProductDTO.getProductCode());
        product.setProductName(addProductDTO.getProductName());
        product.setProductDescription(addProductDTO.getProductDescription());
        product.setQuantity(addProductDTO.getQuantity());
        product.setPrice(addProductDTO.getPrice());
        product.setProductStatus("Active");

        try {
            productRepository.save(product);
            return ResponseEntity.status(201).body(addProductDTO);
        } catch (Exception e) {
            System.err.println("Error saving product: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    

    public ResponseEntity<HoldDTO> holdProduct(HoldDTO holdDTO) {
        Optional<Product> optionalProduct = productRepository.findByProductCode(holdDTO.getProductCode());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setProductStatus(holdDTO.getProductStatus());
            productRepository.save(product);
            return ResponseEntity.status(201).body(holdDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.delete(optionalProduct.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<UpdateProductDTO> putProduct(UpdateProductDTO updateProductDTO) {
        Optional<Product> optionalProduct = productRepository.findById(updateProductDTO.getId());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();


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
                product.setProductStatus(updateProductDTO.getProductStatus());
            }

            productRepository.save(product);
            return ResponseEntity.status(200).body(updateProductDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
