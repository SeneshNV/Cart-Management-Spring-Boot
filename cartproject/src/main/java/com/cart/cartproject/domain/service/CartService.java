package com.cart.cartproject.domain.service;

import com.cart.cartproject.application.dto.requestDto.CartDTO.AddToCartDTO;
import com.cart.cartproject.application.dto.responseDto.CartDTO.ViewCartDTO;
import com.cart.cartproject.application.dto.responseDto.ProductDTO.ViewProductDTO;
import com.cart.cartproject.application.dto.responseDto.UserDTO.ViewUsersDTO;
import com.cart.cartproject.domain.entity.Cart;
import com.cart.cartproject.domain.entity.Product;
import com.cart.cartproject.domain.entity.User;
import com.cart.cartproject.external.CartRepository;
import com.cart.cartproject.external.ProductRepository;
import com.cart.cartproject.external.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public ResponseEntity<AddToCartDTO> addToCart(AddToCartDTO addToCartDTO) {
        Optional<Product> optionalProduct = productRepository.findById(addToCartDTO.getProductId());
        Optional<User> optionalUser = userRepository.findById(addToCartDTO.getUserId());

        if (optionalProduct.isPresent() && optionalUser.isPresent()) {
            Product product = optionalProduct.get();
            User user = optionalUser.get();

            Cart cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);

            cartRepository.save(cart);
            return ResponseEntity.status(201).body(addToCartDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<Void> deleteCart(Long id) {
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (optionalCart.isPresent()) {
            cartRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<List<ViewCartDTO>> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        List<ViewCartDTO> cartDTOs = new ArrayList<>();

        for (Cart cart : carts) {
            ViewCartDTO viewCartDTO = new ViewCartDTO();
            viewCartDTO.setId(cart.getId());

            ViewProductDTO viewProductDTO = new ViewProductDTO();
            viewProductDTO.setId(cart.getId());
            viewProductDTO.setProductCode(cart.getProduct().getProductCode());
            viewProductDTO.setProductName(cart.getProduct().getProductName());
            viewProductDTO.setProductDescription(cart.getProduct().getProductDescription());
            viewProductDTO.setQuantity(cart.getProduct().getQuantity());
            viewProductDTO.setPrice(cart.getProduct().getPrice());
            viewProductDTO.setProductStatus(cart.getProduct().getProductStatus().getProductStatusCode());

            viewCartDTO.setProduct(viewProductDTO);

            ViewUsersDTO viewUsersDTO = new ViewUsersDTO();
            viewUsersDTO.setId(cart.getId());
            viewUsersDTO.setUsername(cart.getUser().getUsername());
            viewUsersDTO.setEmail(cart.getUser().getEmail());
            viewUsersDTO.setUserRole(cart.getUser().getUserRoleCode().getUserRoleCode());
            viewUsersDTO.setAccountStatus(cart.getUser().getAccountStatus());

            viewCartDTO.setUser(viewUsersDTO);
            cartDTOs.add(viewCartDTO);
        }

        return ResponseEntity.ok(cartDTOs);
    }


    // specific buyer get cart
    public ResponseEntity<List<ViewCartDTO>> getUserCart(Long id) {


        List<Cart> carts = cartRepository.findByUserId(id);
        List<ViewCartDTO> cartDTOs = new ArrayList<>();

        for (Cart cart : carts) {

            ViewCartDTO viewCartDTO = new ViewCartDTO();
            viewCartDTO.setId(cart.getId());

            ViewProductDTO viewProductDTO = new ViewProductDTO();
            viewProductDTO.setId(cart.getId());
            viewProductDTO.setProductCode(cart.getProduct().getProductCode());
            viewProductDTO.setProductName(cart.getProduct().getProductName());
            viewProductDTO.setProductDescription(cart.getProduct().getProductDescription());
            viewProductDTO.setQuantity(cart.getProduct().getQuantity());
            viewProductDTO.setPrice(cart.getProduct().getPrice());
            viewProductDTO.setProductStatus(cart.getProduct().getProductStatus().getProductStatusCode());

            viewCartDTO.setProduct(viewProductDTO);

            ViewUsersDTO viewUsersDTO = new ViewUsersDTO();
            viewUsersDTO.setId(cart.getId());
            viewUsersDTO.setUsername(cart.getUser().getUsername());
            viewUsersDTO.setEmail(cart.getUser().getEmail());
            viewUsersDTO.setUserRole(cart.getUser().getUserRoleCode().getUserRoleCode());
            viewUsersDTO.setAccountStatus(cart.getUser().getAccountStatus());

            viewCartDTO.setUser(viewUsersDTO);

            cartDTOs.add(viewCartDTO);
        }

        return ResponseEntity.ok(cartDTOs);
    }


    public ResponseEntity<List<Object[]>> getMostSelectedProducts() {
        List<Object[]> mostSelectedProducts = cartRepository.findMostSelectedProducts();
        return ResponseEntity.ok(mostSelectedProducts);
    }
}
