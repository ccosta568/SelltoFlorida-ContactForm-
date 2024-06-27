package com.pwm.aws.crud.lambda.api.Controller;

import com.pwm.aws.crud.lambda.api.Service.ProductService;
import com.pwm.aws.crud.lambda.api.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "Content-Type")
@RequestMapping("/api")
public class ProductSpringController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/contract")
    public ResponseEntity<Map<String, String>> saveProduct(@RequestBody Product product) {
        System.out.println("Received contact request: " + product);
        productService.saveProduct(product);
        Map<String, String> response = new HashMap<>();
        response.put("message", "New Contact Registered");
        return ResponseEntity.ok(response);
    }
}