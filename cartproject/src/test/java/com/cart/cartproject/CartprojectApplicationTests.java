package com.cart.cartproject;

import com.cart.cartproject.application.dto.requestDto.ProductDTO.UpdateProductDTO;
import com.cart.cartproject.application.dto.requestDto.UserDTO.CreateUserDTO;
import com.cart.cartproject.application.dto.responseDto.ApiResponse;
import com.cart.cartproject.domain.service.CartService;
import com.cart.cartproject.domain.service.ProductService;
import com.cart.cartproject.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@Slf4j
@SpringBootTest
class CartprojectApplicationTests {

	@Test
	void contextLoads() {
	}


	@Autowired
	private UserService userService;

	@Test
	void testGetUsers(){
		log.info("=================== Get User Initialize =========================");
		userService.getUsers(29L);
		log.info(userService.getUsers(29L).toString());
		log.info("=================== Get User Completed =========================");
	}

	private CreateUserDTO validUserDTO;

	@BeforeEach
	void setUp() {
		validUserDTO = new CreateUserDTO();
		validUserDTO.setUsername("testuser");
		validUserDTO.setEmail("testuser@example.com");
		validUserDTO.setPassword("Test@1234");
		validUserDTO.setUserRoleCode("BUYER");
	}

	@Test
	void testPostSignup(){
		log.info("=================== Get Post Signup Initialize =========================");

		ResponseEntity<ApiResponse> response = userService.postSignup(validUserDTO);
		log.info(userService.postSignup(validUserDTO).toString());
		log.info("=================== Get Post Signup Completed =========================");
	}


	@Autowired
	private ProductService productService;

	private UpdateProductDTO validUpdateProductDTO;

	@BeforeEach
	void setUp2() {
		validUpdateProductDTO = new UpdateProductDTO();
		validUpdateProductDTO.setProductName("testuser");
		validUpdateProductDTO.setProductCode("testuser@example.com");
		validUpdateProductDTO.setProductDescription("Test@1234");
		validUpdateProductDTO.setProductStatus("BUYER");
		validUpdateProductDTO.setPrice(2000.0);
		validUpdateProductDTO.setQuantity(50);
		validUpdateProductDTO.setProductAddedBy(31L);
	}

	@Test
	void testGetAllProduct(){
		log.info("=================== Get All Product Initialize =========================");
		productService.getAllProduct();
		log.info(productService.getAllProduct().getBody().toString());
		log.info("=================== Get All Product Completed =========================");
	}

	@Test
	void testPutProduct() {
		log.info("=================== Get Update Product Initialize =========================");
		ResponseEntity<String> stringResponseEntity = productService.putProduct(validUpdateProductDTO);
		log.info(stringResponseEntity.getBody().toString());
		log.info("=================== Get Update Product Completed =========================");
	}


	@Autowired
	private CartService cartService;

	@Test
	void testGetAllCarts(){
		log.info("=================== Get ALL Carts Initialize =========================");
		cartService.getAllCarts();
		log.info(cartService.getAllCarts().toString());
		log.info("=================== Get All Carts Initialize =========================");
	}

	@Test
	void testGetUserCart(){
		log.info("=================== Get ALL Carts Initialize =========================");
		cartService.getUserCart(2L);
		log.info(cartService.getUserCart(2L).toString());
		log.info("=================== Get All Carts Initialize =========================");
	}



}
