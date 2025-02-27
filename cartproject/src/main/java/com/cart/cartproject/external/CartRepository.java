package com.cart.cartproject.external;

import com.cart.cartproject.domain.entity.Cart;
import com.cart.cartproject.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long userId);
    @Query("SELECT c.product, COUNT(c.product) AS count FROM Cart c GROUP BY c.product ORDER BY count DESC")
    List<Object[]> findMostSelectedProducts();
}
