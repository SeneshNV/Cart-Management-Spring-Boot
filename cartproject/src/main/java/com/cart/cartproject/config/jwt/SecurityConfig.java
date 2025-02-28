package com.cart.cartproject.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/user/signup").permitAll()
                        .requestMatchers("/api/v1/user/login").permitAll()
                        .requestMatchers("/api/v1/user/logout").permitAll()
                        .requestMatchers("/api/v1/user/admin/login").permitAll()

//                        .requestMatchers("/api/v1/user/admin/create").hasRole("ADMIN1")
                        .requestMatchers("/api/v1/user/admin/create").permitAll()

                        .requestMatchers("/api/v1/product/view/**").permitAll()
                        .requestMatchers("/api/v1/product/add").hasAnyRole("ADMIN1", "ADMIN2")
                        .requestMatchers("/api/v1/product/update").hasAnyRole("ADMIN1", "ADMIN2")
                        .requestMatchers("/api/v1/product/hold").hasAnyRole("ADMIN1", "ADMIN2")
                        .requestMatchers("/api/v1/product/delete/**").hasAnyRole("ADMIN1", "ADMIN2")

                        .requestMatchers("/api/v1/cart/add").hasRole("BUYER")
                        .requestMatchers("/api/v1/cart/delete/**").hasRole("BUYER")
                        .requestMatchers("/api/v1/cart/me").hasRole("BUYER")
                        .requestMatchers("/api/v1/cart/all").hasAnyRole("ADMIN1", "ADMIN2")
                        .requestMatchers("/api/v1/cart/most-selected").hasAnyRole("ADMIN1", "ADMIN2")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
