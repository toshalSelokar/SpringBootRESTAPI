# Spring Boot Dependencies Guide

This document provides detailed explanations and code examples for the key dependencies used in our Spring Boot project.

## Table of Contents
- [Spring Boot Starter Web](#spring-boot-starter-web)
- [Spring Boot Starter Data JPA](#spring-boot-starter-data-jpa)
- [Spring Boot Starter Actuator](#spring-boot-starter-actuator)
- [Spring Boot Starter Validation](#spring-boot-starter-validation)
- [Spring Session JDBC](#spring-session-jdbc)
- [H2 Database](#h2-database)
- [Spring Boot Starter Test](#spring-boot-starter-test)

## Spring Boot Starter Web

### Overview
Spring Boot Starter Web is used for building web applications, including RESTful applications using Spring MVC. It uses Tomcat as the default embedded container.

### Dependency in build.gradle
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
}
```

### Key Components
- Spring MVC for handling HTTP requests
- Embedded Tomcat server
- JSON serialization/deserialization support

### Code Example: Creating a REST Controller

```java
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Spring Boot!";
    }
    
    @GetMapping("/hello/{name}")
    public String sayHelloToUser(@PathVariable String name) {
        return "Hello, " + name + "!";
    }
}
```

### Usage Notes
- `@RestController` combines `@Controller` and `@ResponseBody` annotations
- `@RequestMapping` defines the base URL path for all methods in the controller
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` handle specific HTTP methods
- Spring automatically converts return values to JSON/XML based on content negotiation

## Spring Boot Starter Data JPA

### Overview
Spring Boot Starter Data JPA simplifies database operations using Java Persistence API (JPA) with Hibernate as the default implementation.

### Dependency in build.gradle
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
}
```

### Key Components
- Spring Data JPA repositories
- Entity management
- Transaction management
- Hibernate ORM

### Code Example: Entity and Repository

```java
// Entity class
package com.example.SpringBootDemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    
    private String description;
    
    @Min(value = 0, message = "Price should not be negative")
    private double price;
    
    // Constructors
    public Product() {}
    
    public Product(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
}
```

```java
// Repository interface
package com.example.SpringBootDemo.repository;

import com.example.SpringBootDemo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Custom query methods
    List<Product> findByNameContaining(String name);
    
    List<Product> findByPriceLessThan(double price);
}
```

```java
// Service class
package com.example.SpringBootDemo.service;

import com.example.SpringBootDemo.entity.Product;
import com.example.SpringBootDemo.repository.ProductRepository;
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
}
```

### Usage Notes
- `@Entity` marks a class as a JPA entity
- `@Id` specifies the primary key
- `@GeneratedValue` configures the way of incrementing the primary key
- Spring Data JPA automatically implements CRUD operations
- Custom query methods can be defined by method naming conventions

## Spring Boot Starter Actuator

### Overview
Spring Boot Actuator provides production-ready features to help monitor and manage your application.

### Dependency in build.gradle
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
}
```

### Key Features
- Health checks
- Metrics
- Environment information
- Application information
- Thread dump
- HTTP trace

### Configuration in application.properties
```properties
# Enable all actuator endpoints
management.endpoints.web.exposure.include=*

# Enable specific endpoints
# management.endpoints.web.exposure.include=health,info,metrics

# Customize info endpoint
info.app.name=Spring Boot Demo
info.app.description=Spring Boot Demo Application
info.app.version=1.0.0

# Health endpoint details
management.endpoint.health.show-details=always
```

### Code Example: Custom Health Indicator

```java
package com.example.SpringBootDemo.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        boolean isHealthy = checkIfSystemIsHealthy();
        
        if (isHealthy) {
            return Health.up()
                    .withDetail("customKey", "Everything is working fine!")
                    .withDetail("description", "Custom health indicator for demonstration")
                    .build();
        } else {
            return Health.down()
                    .withDetail("customKey", "Something went wrong!")
                    .withDetail("description", "Custom health indicator showing DOWN status")
                    .build();
        }
    }
    
    private boolean checkIfSystemIsHealthy() {
        // Add your health check logic here
        // For demonstration, we'll return true
        return true;
    }
}
```

### Usage Notes
- Access actuator endpoints at `/actuator/{endpoint-name}`
- Common endpoints: `/actuator/health`, `/actuator/info`, `/actuator/metrics`
- Secure actuator endpoints in production environments
- Custom health indicators can be created by implementing the `HealthIndicator` interface

## Spring Boot Starter Validation

### Overview
Spring Boot Starter Validation provides support for Java Bean Validation with Hibernate Validator.

### Dependency in build.gradle
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-validation'
}
```

### Key Features
- Bean validation annotations
- Method parameter validation
- Custom validation constraints

### Code Example: Validation in Entity and Controller

```java
// Entity with validation
package com.example.SpringBootDemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

@Entity
public class AppUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @Min(value = 18, message = "Age should not be less than 18")
    private int age;
    
    // Constructors
    public AppUser() {}
    
    public AppUser(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
}
```

```java
// Controller with validation
package com.example.SpringBootDemo.controller;

import com.example.SpringBootDemo.entity.AppUser;
import com.example.SpringBootDemo.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AppUserController {
    
    private final AppUserService appUserService;
    
    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }
    
    @PostMapping
    public ResponseEntity<AppUser> createUser(@Valid @RequestBody AppUser user) {
        AppUser savedUser = appUserService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
```

### Usage Notes
- `@Valid` triggers validation of the annotated parameter
- Common validation annotations: `@NotNull`, `@NotEmpty`, `@NotBlank`, `@Size`, `@Min`, `@Max`, `@Email`
- Custom validation constraints can be created using `@Constraint` and `ConstraintValidator`
- `@ExceptionHandler` can be used to handle validation exceptions

## Spring Session JDBC

### Overview
Spring Session JDBC provides support for managing user sessions using a relational database.

### Dependency in build.gradle
```gradle
dependencies {
    implementation 'org.springframework.session:spring-session-jdbc'
}
```

### Configuration in application.properties
```properties
# Session store type
spring.session.store-type=jdbc

# Session timeout (in seconds)
server.servlet.session.timeout=1800

# Session table initialization
spring.session.jdbc.initialize-schema=always

# Session table name prefix (optional)
spring.session.jdbc.table-name=SPRING_SESSION
```

### Code Example: Session Configuration

```java
package com.example.SpringBootDemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableJdbcHttpSession
public class SessionConfig {
    
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("SESSION");
        serializer.setCookiePath("/");
        // Uncomment for domain-specific configuration
        // serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        return serializer;
    }
}
```

```java
// Controller using session
package com.example.SpringBootDemo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    
    @PostMapping("/set")
    public Map<String, String> setSessionAttribute(HttpSession session, 
                                                  @RequestParam String key, 
                                                  @RequestParam String value) {
        session.setAttribute(key, value);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Session attribute set successfully");
        response.put("sessionId", session.getId());
        return response;
    }
    
    @GetMapping("/get")
    public Map<String, Object> getSessionAttribute(HttpSession session, 
                                                  @RequestParam String key) {
        Object value = session.getAttribute(key);
        
        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put(key, value);
        return response;
    }
    
    @GetMapping("/invalidate")
    public Map<String, String> invalidateSession(HttpSession session) {
        String sessionId = session.getId();
        session.invalidate();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Session invalidated");
        response.put("sessionId", sessionId);
        return response;
    }
}
```

### Usage Notes
- Spring Session JDBC creates tables to store session data
- Session data is persisted across application restarts
- Enables session sharing across multiple instances of the application
- Provides session timeout management
- Supports custom cookie configuration

## H2 Database

### Overview
H2 is a fast, in-memory database that provides a JDBC API and supports embedded and server modes.

### Dependency in build.gradle
```gradle
dependencies {
    runtimeOnly 'com.h2database:h2'
}
```

### Configuration in application.properties
```properties
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console Configuration
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Code Example: Data Initialization

```java
// Data initialization using CommandLineRunner
package com.example.SpringBootDemo.config;

import com.example.SpringBootDemo.entity.AppUser;
import com.example.SpringBootDemo.entity.Product;
import com.example.SpringBootDemo.repository.AppUserRepository;
import com.example.SpringBootDemo.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    
    @Bean
    public CommandLineRunner initData(ProductRepository productRepository, AppUserRepository appUserRepository) {
        return args -> {
            // Create and save sample products
            productRepository.save(new Product("Laptop", "High-performance laptop", 1299.99));
            productRepository.save(new Product("Smartphone", "Latest smartphone model", 899.99));
            productRepository.save(new Product("Tablet", "Lightweight tablet", 499.99));
            productRepository.save(new Product("Headphones", "Noise-cancelling headphones", 199.99));
            
            // Create and save sample users
            appUserRepository.save(new AppUser("John Doe", "john@example.com", 30));
            appUserRepository.save(new AppUser("Jane Smith", "jane@example.com", 25));
            appUserRepository.save(new AppUser("Bob Johnson", "bob@example.com", 40));
            
            System.out.println("Sample data initialized!");
        };
    }
}
```

### Usage Notes
- H2 provides a web console accessible at `/h2-console` (when enabled)
- Perfect for development and testing environments
- Data is lost when the application is restarted (when using in-memory mode)
- Can be configured to persist data to disk
- Supports most SQL features

## Spring Boot Starter Test

### Overview
Spring Boot Starter Test provides a comprehensive testing framework for Spring Boot applications.

### Dependency in build.gradle
```gradle
dependencies {
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

### Key Components
- JUnit 5 for unit testing
- Spring Test for integration testing
- AssertJ for fluent assertions
- Hamcrest for matchers
- Mockito for mocking
- JSONassert for JSON assertions
- JsonPath for JSON parsing

### Code Example: Unit and Integration Tests

```java
// Unit test for service
package com.example.SpringBootDemo.service;

import com.example.SpringBootDemo.entity.Product;
import com.example.SpringBootDemo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductService productService;
    
    private Product product1;
    private Product product2;
    
    @BeforeEach
    void setUp() {
        product1 = new Product("Test Product 1", "Description 1", 10.0);
        product1.setId(1L);
        
        product2 = new Product("Test Product 2", "Description 2", 20.0);
        product2.setId(2L);
    }
    
    @Test
    void shouldReturnAllProducts() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
        
        // When
        List<Product> products = productService.getAllProducts();
        
        // Then
        assertThat(products).hasSize(2);
        assertThat(products).contains(product1, product2);
        verify(productRepository, times(1)).findAll();
    }
    
    @Test
    void shouldReturnProductById() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        
        // When
        Optional<Product> foundProduct = productService.getProductById(1L);
        
        // Then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("Test Product 1");
        verify(productRepository, times(1)).findById(1L);
    }
    
    @Test
    void shouldSaveProduct() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(product1);
        
        // When
        Product savedProduct = productService.saveProduct(product1);
        
        // Then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Test Product 1");
        verify(productRepository, times(1)).save(product1);
    }
}
```

```java
// Integration test for controller
package com.example.SpringBootDemo.controller;

import com.example.SpringBootDemo.entity.Product;
import com.example.SpringBootDemo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ProductService productService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void shouldGetAllProducts() throws Exception {
        // Given
        Product product1 = new Product("Test Product 1", "Description 1", 10.0);
        product1.setId(1L);
        Product product2 = new Product("Test Product 2", "Description 2", 20.0);
        product2.setId(2L);
        
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));
        
        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Product 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Test Product 2")));
    }
    
    @Test
    void shouldGetProductById() throws Exception {
        // Given
        Product product = new Product("Test Product", "Description", 10.0);
        product.setId(1L);
        
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));
        
        // When & Then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.price", is(10.0)));
    }
    
    @Test
    void shouldCreateProduct() throws Exception {
        // Given
        Product product = new Product("New Product", "New Description", 15.0);
        Product savedProduct = new Product("New Product", "New Description", 15.0);
        savedProduct.setId(1L);
        
        when(productService.saveProduct(any(Product.class))).thenReturn(savedProduct);
        
        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Product")))
                .andExpect(jsonPath("$.description", is("New Description")))
                .andExpect(jsonPath("$.price", is(15.0)));
    }
}
```

### Usage Notes
- Use `@SpringBootTest` for full application context testing
- Use `@WebMvcTest` for testing controllers in isolation
- Use `@DataJpaTest` for testing JPA repositories
- Use `@MockBean` to replace beans with mock implementations
- Use `MockMvc` for testing web endpoints without starting a server
- Use `TestRestTemplate` or `WebTestClient` for testing REST endpoints with a running server

## Conclusion

This guide provides a comprehensive overview of the key dependencies used in our Spring Boot project. Each dependency serves a specific purpose and together they form a robust foundation for building modern web applications.

For more information, refer to the official Spring Boot documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/
