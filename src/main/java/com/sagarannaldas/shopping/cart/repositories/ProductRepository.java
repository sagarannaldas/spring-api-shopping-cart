package com.sagarannaldas.shopping.cart.repositories;

import com.sagarannaldas.shopping.cart.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
