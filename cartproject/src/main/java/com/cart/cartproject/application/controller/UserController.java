package com.cart.cartproject.application.controller;


import com.cart.cartproject.application.dto.requestDto.UserDTO.CreateUserDTO;
import com.cart.cartproject.application.dto.requestDto.UserDTO.LoginUserDTO;
import com.cart.cartproject.application.dto.responseDto.ApiResponse;
import com.cart.cartproject.application.dto.responseDto.UserDTO.ViewUsersDTO;
import com.cart.cartproject.domain.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/view/{id}")
    public ResponseEntity<?> getUsers(@PathVariable Long id) {
        return userService.getUsers(id);
    }

    // Buyer signup
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody CreateUserDTO createUserDTO) {
        return userService.postSignup(createUserDTO);
    }

    // Buyer login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginUserDTO loginUserDTO, HttpServletResponse response) {
        return userService.postLogin(loginUserDTO, response);
    }

    // Admin login
    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponse> adminLogin(@RequestBody LoginUserDTO loginUserDTO, HttpServletResponse response) {
        return userService.postAdminLogin(loginUserDTO, response);
    }

    // Private endpoint for admin creation
    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN1')")
    public ResponseEntity<ApiResponse> createAdmin(@RequestBody CreateUserDTO createUserDTO) {
        return userService.createAdmin(createUserDTO);
    }

    // Logout
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // Clear the JWT cookie
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Set the cookie's max age to 0 to delete it
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }
}