package com.cart.cartproject.domain.service;

import com.cart.cartproject.application.dto.requestDto.AddProductDTO;
import com.cart.cartproject.application.dto.requestDto.HoldDTO;
import com.cart.cartproject.application.dto.requestDto.UpdateProductDTO;
import com.cart.cartproject.application.dto.responseDto.ViewProductDTO;
import com.cart.cartproject.domain.entity.Product;
import com.cart.cartproject.external.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ResponseEntity<ViewProductDTO> getProduct(Long id) {
        ViewProductDTO viewProductDTO = new ViewProductDTO();

        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()){

            Product product = optionalProduct.get();

            viewProductDTO.setId(product.getId());
            viewProductDTO.setProductCode(product.getProductCode());
            viewProductDTO.setProductName(product.getProductName());
            viewProductDTO.setProductDescription(product.getProductDescription());
            viewProductDTO.setQuantity(product.getQuantity());
            viewProductDTO.setPrice(product.getPrice());

            return ResponseEntity.ok(viewProductDTO);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<ViewProductDTO>> getAllProduct() {
        ViewProductDTO viewProductDTO = new ViewProductDTO();

        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no products exist
        }

        List<ViewProductDTO> productDTOs = products.stream().map(product -> {
            ViewProductDTO dto = new ViewProductDTO();
            dto.setId(product.getId());
            dto.setProductCode(product.getProductCode());
            dto.setProductName(product.getProductName());
            dto.setProductDescription(product.getProductDescription());
            dto.setQuantity(product.getQuantity());
            dto.setPrice(product.getPrice());

            return dto;

        }).collect(Collectors.toList());

        return ResponseEntity.ok(productDTOs);
    }

    public ResponseEntity<AddProductDTO> postProduct(AddProductDTO addProductDTO) {
        if(productRepository.existsByProductCode(addProductDTO.getProductCode())){
            return  ResponseEntity.status(409).body(null);
        }

        Product product = new Product();
        product.setProductCode(addProductDTO.getProductCode());
        product.setProductName(addProductDTO.getProductName());
        product.setProductDescription(addProductDTO.getProductDescription());
        product.setQuantity(addProductDTO.getQuantity());
        product.setPrice(addProductDTO.getPrice());
        product.setProductStatus("Active");

        try{
            productRepository.save(product);
            return ResponseEntity.status(201).body(addProductDTO);
        }catch (Exception e){
            System.err.println("Error saving product: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    public ResponseEntity<UpdateProductDTO> putProduct(UpdateProductDTO updateProductDTO) {
        Optional<Product> optionalProduct = productRepository.findByProductCode(updateProductDTO.getProductCode());
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            product.setProductCode(updateProductDTO.getProductCode());
            product.setProductName(updateProductDTO.getProductName());
            product.setProductDescription(updateProductDTO.getProductDescription());
            product.setQuantity(updateProductDTO.getQuantity());
            product.setPrice(updateProductDTO.getPrice());
            product.setProductStatus(updateProductDTO.getProductStatus());

            productRepository.save(product);
            return ResponseEntity.status(201).body(null);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<HoldDTO> holdProduct(HoldDTO holdDTO) {
        Optional<Product> optionalProduct = productRepository.findByProductCode(holdDTO.getProductCode());

        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            product.setProductStatus(holdDTO.getProductStatus());

            productRepository.save(product);
            return ResponseEntity.status(201).body(null);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
