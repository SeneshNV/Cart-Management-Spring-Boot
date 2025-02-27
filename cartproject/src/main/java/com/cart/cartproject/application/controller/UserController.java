package com.cart.cartproject.application.controller;

import com.cart.cartproject.application.dto.requestDto.CreateUserDTO;
import com.cart.cartproject.application.dto.requestDto.LoginUserDTO;
import com.cart.cartproject.application.dto.responseDto.ViewUsersDTO;
import com.cart.cartproject.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/view/{id}")
    public ResponseEntity<ViewUsersDTO> getUsers(@PathVariable Integer id){
        return userService.getUsers(id);
    }

    // buyer signup
    @PostMapping("/signup")
    public ResponseEntity<CreateUserDTO> signup(@RequestBody CreateUserDTO createUserDTO){
        return  userService.postSigup(createUserDTO);
    }

    // buyer login
    @PostMapping("/login")
    public ResponseEntity<LoginUserDTO> login(@RequestBody LoginUserDTO loginUserDTO){
        return  userService.postLogin(loginUserDTO);
    }


}
