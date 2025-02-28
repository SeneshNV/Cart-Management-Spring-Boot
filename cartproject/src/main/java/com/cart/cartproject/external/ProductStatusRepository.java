package com.cart.cartproject.external;

import com.cart.cartproject.domain.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductStatusRepository extends JpaRepository<ProductStatus, Long> {
    Optional<ProductStatus> findByproductStatusCode(String productStatusCode);
}
