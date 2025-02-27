package com.cart.cartproject.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userRoleCode; //ADMIN1, ADMIN2, BUYER

    @Column(nullable = false, unique = true)
    private String userRole; //Admin 1, Admin 2, Buyer
}
