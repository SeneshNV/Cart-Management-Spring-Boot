package com.cart.cartproject.domain.service;

import com.cart.cartproject.application.dto.requestDto.CreateUserDTO;
import com.cart.cartproject.application.dto.requestDto.LoginUserDTO;
import com.cart.cartproject.application.dto.responseDto.ViewUsersDTO;
import com.cart.cartproject.config.jwt.JwtUtil;
import com.cart.cartproject.domain.entity.User;
import com.cart.cartproject.external.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private  JwtUtil jwtUtil;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<ViewUsersDTO> getUsers(Integer id) {

        ViewUsersDTO viewUsersDTO = new ViewUsersDTO();

        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){

            User user = optionalUser.get();

            viewUsersDTO.setId(user.getId());
            viewUsersDTO.setUsername(user.getUsername());
            viewUsersDTO.setEmail(user.getEmail());
            viewUsersDTO.setUserRole(user.getUserRole());
            viewUsersDTO.setActiveStatus(user.getActiveStatus());
            viewUsersDTO.setAccountStatus(user.getAccountStatus());

            return ResponseEntity.ok(viewUsersDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<CreateUserDTO> postSigup(CreateUserDTO createUserDTO) {

        if (userRepository.existsByUsername(createUserDTO.getUsername())) {
            return ResponseEntity.status(409).body(null);
        }
        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            return ResponseEntity.status(409).body(null);
        }

        String hashedPassword = passwordEncoder.encode(createUserDTO.getPassword());
        System.out.println("Hashed Password: " + hashedPassword);

        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(hashedPassword);
        user.setUserRole("Buyer");
        user.setAccountStatus("Active");
        user.setActiveStatus("true");

        try {
            userRepository.save(user);
            return ResponseEntity.status(201).body(createUserDTO);
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }

    }

    public ResponseEntity<LoginUserDTO> postLogin(LoginUserDTO loginUserDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(loginUserDTO.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (passwordEncoder.matches(loginUserDTO.getPassword(), user.getPassword())) {

                String token = jwtUtil.generateToken(user.getEmail()); // Generate JWT token

                LoginUserDTO response = new LoginUserDTO();
                response.setEmail(user.getEmail());
                response.setPassword(token);


                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(null);
            }
        } else {
            return ResponseEntity.status(404).body(null);
        }

    }
}
