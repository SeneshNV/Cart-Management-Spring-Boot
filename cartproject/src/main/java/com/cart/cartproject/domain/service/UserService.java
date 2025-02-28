package com.cart.cartproject.domain.service;

import com.cart.cartproject.application.dto.requestDto.UserDTO.CreateUserDTO;
import com.cart.cartproject.application.dto.requestDto.UserDTO.LoginUserDTO;
import com.cart.cartproject.application.dto.responseDto.UserDTO.ViewUsersDTO;
import com.cart.cartproject.config.jwt.JwtUtil;
import com.cart.cartproject.domain.entity.User;
import com.cart.cartproject.domain.entity.UserRole;
import com.cart.cartproject.external.UserRepository;
import com.cart.cartproject.external.UserRoleRepository;
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

    public String getLoggedInUserRole(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getUserRoleCode().getUserRoleCode();
        }
        throw new RuntimeException("User not found");
    }


    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }

    public ResponseEntity<ViewUsersDTO> getUsers(Long id) {
        ViewUsersDTO viewUsersDTO = new ViewUsersDTO();

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            viewUsersDTO.setId(user.getId());
            viewUsersDTO.setUsername(user.getUsername());
            viewUsersDTO.setEmail(user.getEmail());
            viewUsersDTO.setUserRole(user.getUserRoleCode().getUserRoleCode());
            viewUsersDTO.setAccountStatus(user.getAccountStatus());

            return ResponseEntity.ok(viewUsersDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public ResponseEntity<String> postSignup(CreateUserDTO createUserDTO) {
        try {

            if (!isValidEmail(createUserDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
            }


            if (!isValidPassword(createUserDTO.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be at least 8 characters long");
            }


            if (!createUserDTO.getUserRoleCode().equals("BUYER")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only BUYER role is allowed for signup");
            }


            if (userRepository.existsByUsername(createUserDTO.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }


            if (userRepository.existsByEmail(createUserDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
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

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    public ResponseEntity<String> createAdmin(CreateUserDTO createUserDTO) {
        try {
            // Get the email of currently log
            String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            // Fetch the logged-in user's role
            String loggedInUserRole = getLoggedInUserRole(loggedInUserEmail);

            System.out.println("Logged-in user role: " + loggedInUserRole);

            // Check if the logged-in user is ADMIN1
            if (!loggedInUserRole.equals("ADMIN1")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN1 can create admin accounts");
            }

            // Validate email format
            if (!isValidEmail(createUserDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
            }

            // Validate password length
            if (!isValidPassword(createUserDTO.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be at least 8 characters long");
            }

            // Check if username already exists
            if (userRepository.existsByUsername(createUserDTO.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }

            // Check if email already exists
            if (userRepository.existsByEmail(createUserDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }

            // Fetch admin role
            UserRole adminRole = userRoleRepository.findByUserRoleCode(createUserDTO.getUserRoleCode())
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            // Create and save admin user
            User user = new User();
            user.setUsername(createUserDTO.getUsername());
            user.setEmail(createUserDTO.getEmail());
            user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
            user.setUserRoleCode(adminRole);
            user.setAccountStatus("Active");
            user.setActiveStatus(true);

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("Admin created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    public ResponseEntity<String> postLogin(LoginUserDTO loginUserDTO, HttpServletResponse response) {
        try {

            if (!isValidEmail(loginUserDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
            }

            Optional<User> optionalUser = userRepository.findByEmail(loginUserDTO.getEmail());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();


                if (!user.getUserRoleCode().getUserRoleCode().equals("BUYER")) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only BUYER role is allowed for login");
                }


                if (passwordEncoder.matches(loginUserDTO.getPassword(), user.getPassword())) {
                    String token = jwtUtil.generateToken(user.getEmail());

                    // Set the JWT token in a cookie
                    Cookie cookie = new Cookie("jwt", token);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    response.addCookie(cookie);

                    return ResponseEntity.ok("Login successful");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    public ResponseEntity<String> postAdminLogin(LoginUserDTO loginUserDTO, HttpServletResponse response) {
        try {

            if (!isValidEmail(loginUserDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
            }

            Optional<User> optionalUser = userRepository.findByEmail(loginUserDTO.getEmail());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();


                if (!user.getUserRoleCode().getUserRoleCode().equals("ADMIN1") &&
                        !user.getUserRoleCode().getUserRoleCode().equals("ADMIN2")) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN roles are allowed for admin login");
                }


                if (passwordEncoder.matches(loginUserDTO.getPassword(), user.getPassword())) {
                    String token = jwtUtil.generateToken(user.getEmail());


                    Cookie cookie = new Cookie("jwt", token);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    response.addCookie(cookie);

                    return ResponseEntity.ok("Admin login successful");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
