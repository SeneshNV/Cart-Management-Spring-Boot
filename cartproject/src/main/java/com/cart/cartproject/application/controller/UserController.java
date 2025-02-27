package com.cart.cartproject.application.controller;


import com.cart.cartproject.application.dto.requestDto.UserDTO.CreateUserDTO;
import com.cart.cartproject.application.dto.requestDto.UserDTO.LoginUserDTO;
import com.cart.cartproject.application.dto.responseDto.UserDTO.ViewUsersDTO;
import com.cart.cartproject.domain.service.UserService;
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
    public ResponseEntity<ViewUsersDTO> getUsers(@PathVariable Long id){
        return userService.getUsers(id);
    }

    // Buyer signup
    @PostMapping("/signup")
    public ResponseEntity<CreateUserDTO> signup(@RequestBody CreateUserDTO createUserDTO){
        return userService.postSignup(createUserDTO);
    }

    // Buyer login
    @PostMapping("/login")
    public ResponseEntity<LoginUserDTO> login(@RequestBody LoginUserDTO loginUserDTO){
        return userService.postLogin(loginUserDTO);
    }

    // Admin login
    @PostMapping("/admin/login")
    public ResponseEntity<LoginUserDTO> adminLogin(@RequestBody LoginUserDTO loginUserDTO){
        return userService.postAdminLogin(loginUserDTO);
    }

    // Private endpoint for admin creation
    @PostMapping("/admin/create")
    @PreAuthorize("hasAnyRole('ADMIN1')")
    public ResponseEntity<CreateUserDTO> createAdmin(@RequestBody CreateUserDTO createUserDTO) {
        return userService.createAdmin(createUserDTO);
    }
}