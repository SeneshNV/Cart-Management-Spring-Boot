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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productCode;

    @Column(nullable = false)
    private String productName;

    @Column(columnDefinition = "TEXT")
    private String productDescription;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ProductStatus productStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User productAddedBy;

}
