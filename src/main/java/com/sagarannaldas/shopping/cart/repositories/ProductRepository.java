package com.sagarannaldas.shopping.cart.repositories;

import com.sagarannaldas.shopping.cart.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "category")
    List<Product> getProductsByCategoryId(Byte categoryId);

    @EntityGraph(attributePaths = "category")
    @Query(value = "select * from products", nativeQuery = true)
//    @Query("select p from products p join fetch p.category")
    List<Product> findAllWithCategory();
}
