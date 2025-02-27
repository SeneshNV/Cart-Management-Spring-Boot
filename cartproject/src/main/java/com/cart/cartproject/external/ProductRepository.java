package com.cart.cartproject.external;

import com.cart.cartproject.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductCode(String productCode);
    Optional<Product> findByProductCode(String productCode);
}
