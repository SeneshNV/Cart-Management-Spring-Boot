package com.cart.cartproject.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user", indexes = @Index(name = "idx_username", columnList = "username", unique = true))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_role_id", nullable = false)
    private UserRole userRoleCode;

    @Column(nullable = false)
    private boolean activeStatus; // Changed to boolean

    @Column(nullable = false)
    private String accountStatus;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime accountCreateDate;
}
