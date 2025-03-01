package com.cart.cartproject.domain.service;

import com.cart.cartproject.application.dto.requestDto.UserDTO.CreateUserDTO;
import com.cart.cartproject.application.dto.requestDto.UserDTO.LoginUserDTO;
import com.cart.cartproject.application.dto.responseDto.ApiResponse;
import com.cart.cartproject.application.dto.responseDto.UserDTO.ViewUsersDTO;
import com.cart.cartproject.config.jwt.JwtUtil;
import com.cart.cartproject.domain.entity.User;
import com.cart.cartproject.domain.entity.UserRole;
import com.cart.cartproject.external.UserRepository;
import com.cart.cartproject.external.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRoleRepository userRoleRepository;

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }

    public ResponseEntity<ApiResponse> getUsers(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            ViewUsersDTO viewUsersDTO = new ViewUsersDTO();
            viewUsersDTO.setId(user.getId());
            viewUsersDTO.setUsername(user.getUsername());
            viewUsersDTO.setEmail(user.getEmail());
            viewUsersDTO.setUserRole(user.getUserRoleCode().getUserRoleCode());
            viewUsersDTO.setAccountStatus(user.getAccountStatus());

            ApiResponse response = new ApiResponse("User retrieved successfully", viewUsersDTO, "success");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse("User not found", null, "error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<ApiResponse> postSignup(CreateUserDTO createUserDTO) {
        try {
            if (!isValidEmail(createUserDTO.getEmail())) {
                ApiResponse response = new ApiResponse("Invalid email format", null, "error");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (!isValidPassword(createUserDTO.getPassword())) {
                ApiResponse response = new ApiResponse("Password must be at least 8 characters long", null, "error");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (!createUserDTO.getUserRoleCode().equals("BUYER")) {
                ApiResponse response = new ApiResponse("Only BUYER role is allowed for signup", null, "error");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            if (userRepository.existsByUsername(createUserDTO.getUsername())) {
                ApiResponse response = new ApiResponse("Username already exists", null, "error");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            if (userRepository.existsByEmail(createUserDTO.getEmail())) {
                ApiResponse response = new ApiResponse("Email already exists", null, "error");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            UserRole userRole = userRoleRepository.findByUserRoleCode(createUserDTO.getUserRoleCode())
                    .orElseThrow(() -> new RuntimeException("User role not found: " + createUserDTO.getUserRoleCode()));

            User user = new User();
            user.setUsername(createUserDTO.getUsername());
            user.setEmail(createUserDTO.getEmail());
            user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
            user.setUserRoleCode(userRole);
            user.setActiveStatus(true);
            user.setAccountStatus("Active");

            userRepository.save(user);

            ApiResponse response = new ApiResponse("User registered successfully", null, "success");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("An error occurred: " + e.getMessage(), null, "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<ApiResponse> createAdmin(CreateUserDTO createUserDTO) {
        try {
            String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            if (!isValidEmail(createUserDTO.getEmail())) {
                ApiResponse response = new ApiResponse("Invalid email format", null, "error");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (!isValidPassword(createUserDTO.getPassword())) {
                ApiResponse response = new ApiResponse("Password must be at least 8 characters long", null, "error");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (userRepository.existsByUsername(createUserDTO.getUsername())) {
                ApiResponse response = new ApiResponse("Username already exists", null, "error");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            if (userRepository.existsByEmail(createUserDTO.getEmail())) {
                ApiResponse response = new ApiResponse("Email already exists", null, "error");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            UserRole adminRole = userRoleRepository.findByUserRoleCode(createUserDTO.getUserRoleCode())
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            User user = new User();
            user.setUsername(createUserDTO.getUsername());
            user.setEmail(createUserDTO.getEmail());
            user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
            user.setUserRoleCode(adminRole);
            user.setAccountStatus("Active");
            user.setActiveStatus(true);

            userRepository.save(user);

            ApiResponse response = new ApiResponse("Admin created successfully", null, "success");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("An error occurred: " + e.getMessage(), null, "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<ApiResponse> postLogin(LoginUserDTO loginUserDTO, HttpServletResponse response) {
        try {
            if (!isValidEmail(loginUserDTO.getEmail())) {
                ApiResponse apiResponse = new ApiResponse("Invalid email format", null, "error");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
            }

            Optional<User> optionalUser = userRepository.findByEmail(loginUserDTO.getEmail());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (!user.getUserRoleCode().getUserRoleCode().equals("BUYER")) {
                    ApiResponse apiResponse = new ApiResponse("Only BUYER role is allowed for login", null, "error");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
                }

                if (passwordEncoder.matches(loginUserDTO.getPassword(), user.getPassword())) {
                    String token = jwtUtil.generateToken(user.getEmail(), "BUYER");

                    // Set the JWT token in a cookie
                    Cookie cookie = new Cookie("jwt", token);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    cookie.setSecure(true);
                    response.addCookie(cookie);

                    ApiResponse apiResponse = new ApiResponse("Login successful", null, "success");
                    return ResponseEntity.ok(apiResponse);
                } else {
                    ApiResponse apiResponse = new ApiResponse("Invalid password", null, "error");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
                }
            } else {
                ApiResponse apiResponse = new ApiResponse("User not found", null, "error");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("An error occurred: " + e.getMessage(), null, "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    public ResponseEntity<ApiResponse> postAdminLogin(LoginUserDTO loginUserDTO, HttpServletResponse response) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(loginUserDTO.getEmail());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getUserRoleCode().getUserRoleCode().equals("ADMIN1") ||
                        user.getUserRoleCode().getUserRoleCode().equals("ADMIN2")) {

                    String token = jwtUtil.generateToken(user.getEmail(), user.getUserRoleCode().getUserRoleCode());

                    // Set the JWT token in a cookie
                    Cookie cookie = new Cookie("jwt", token);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    cookie.setSecure(true);
                    response.addCookie(cookie);

                    ApiResponse apiResponse = new ApiResponse("Admin login successful", null, "success");
                    return ResponseEntity.ok(apiResponse);
                } else {
                    ApiResponse apiResponse = new ApiResponse("Only ADMIN roles are allowed for admin login", null, "error");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
                }
            } else {
                ApiResponse apiResponse = new ApiResponse("Admin not found", null, "error");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("An error occurred: " + e.getMessage(), null, "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
}
