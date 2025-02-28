package com.cart.cartproject.domain.service;

import com.cart.cartproject.application.dto.requestDto.UserDTO.CreateUserDTO;
import com.cart.cartproject.application.dto.requestDto.UserDTO.LoginUserDTO;
import com.cart.cartproject.application.dto.responseDto.UserDTO.ViewUsersDTO;
import com.cart.cartproject.config.jwt.JwtUtil;
import com.cart.cartproject.domain.entity.User;
import com.cart.cartproject.domain.entity.UserRole;
import com.cart.cartproject.external.UserRepository;
import com.cart.cartproject.external.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private JwtUtil jwtUtil;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository; // Add this repository

    public ResponseEntity<ViewUsersDTO> getUsers(Long id) {
        ViewUsersDTO viewUsersDTO = new ViewUsersDTO();

        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            viewUsersDTO.setId(user.getId());
            viewUsersDTO.setUsername(user.getUsername());
            viewUsersDTO.setEmail(user.getEmail());
            viewUsersDTO.setUserRole(user.getUserRoleCode().getUserRoleCode());
//            viewUsersDTO.setActiveStatus(user.getActiveStatus());
            viewUsersDTO.setAccountStatus(user.getAccountStatus());

            return ResponseEntity.ok(viewUsersDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<CreateUserDTO> postSignup(CreateUserDTO createUserDTO) {
        try {
            // Check if username exists
            if (userRepository.existsByUsername(createUserDTO.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(null);
            }

            // Check if email exists
            if (userRepository.existsByEmail(createUserDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(null);
            }

            // Fetch the UserRole entity
            UserRole userRole = userRoleRepository.findByUserRoleCode(createUserDTO.getUserRoleCode())
                    .orElseThrow(() -> new RuntimeException("User role not found: " + createUserDTO.getUserRoleCode()));

            // Create the user
            User user = new User();
            user.setUsername(createUserDTO.getUsername());
            user.setEmail(createUserDTO.getEmail());
            user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
            user.setUserRoleCode(userRole); // Set the UserRole entity
            user.setActiveStatus(true);
            user.setAccountStatus("Active");

            // Save the user
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(createUserDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    public ResponseEntity<CreateUserDTO> createAdmin(CreateUserDTO createUserDTO) {
        if (userRepository.existsByUsername(createUserDTO.getUsername())) {
            return ResponseEntity.status(409).body(null);
        }
        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            return ResponseEntity.status(409).body(null);
        }

        String hashedPassword = passwordEncoder.encode(createUserDTO.getPassword());
        System.out.println("Hashed Password: " + hashedPassword);

        // Ensure only admin accounts are created via this endpoint
        UserRole adminRole = (UserRole) userRoleRepository.findByUserRoleCode("ADMIN1")
                .orElseThrow(() -> new RuntimeException("Admin role not found"));

        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(hashedPassword);
        user.setUserRoleCode(adminRole);
        user.setAccountStatus("Active");
        user.setActiveStatus(true);

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

    public ResponseEntity<LoginUserDTO> postAdminLogin(LoginUserDTO loginUserDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(loginUserDTO.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if the user has ADMIN1 or ADMIN2 role
            if (!user.getUserRoleCode().getUserRoleCode().equals("ADMIN1") &&
                    !user.getUserRoleCode().getUserRoleCode().equals("ADMIN2")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            if (passwordEncoder.matches(loginUserDTO.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getEmail());

                LoginUserDTO response = new LoginUserDTO();
                response.setEmail(user.getEmail());
                response.setPassword(token); // Consider renaming this to 'token'

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }
}
