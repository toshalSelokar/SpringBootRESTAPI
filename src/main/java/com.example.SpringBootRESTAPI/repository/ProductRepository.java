package com.example.SpringBootRESTAPI.repository;

import com.example.SpringBootRESTAPI.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Custom query methods
    List<Product> findByNameContaining(String name);
    
    List<Product> findByPriceLessThan(double price);
}
