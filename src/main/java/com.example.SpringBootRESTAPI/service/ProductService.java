package com.example.SpringBootRESTAPI.service;

import com.example.SpringBootRESTAPI.entity.Product;
import com.example.SpringBootRESTAPI.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public List<Product> findProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }
    
    public List<Product> findProductsByPriceLessThan(double price) {
        return productRepository.findByPriceLessThan(price);
    }
}
