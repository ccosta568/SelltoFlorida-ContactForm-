package com.pwm.aws.crud.lambda.api;

import com.amazonaws.AmazonClientException;
import com.pwm.aws.crud.lambda.api.model.DBService;
import com.pwm.aws.crud.lambda.api.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductSpringController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @GetMapping("/{name}")
    public Product getProductByName(@PathVariable String name) {
        return productRepository.findById(name).orElse(null);
    }

    @PutMapping("/{name}")
    public Product updateProduct(@PathVariable String name, @RequestBody Product productDetails) {
        Product product = productRepository.findById(name).orElse(null);
        product.setEmail(productDetails.getEmail());
        product.setPhone(productDetails.getPhone());
        product.setMessage(productDetails.getMessage());
        return productRepository.save(product);
    }

    @DeleteMapping("/{name}")
    public void deleteProduct(@PathVariable String name) {
        productRepository.deleteById(name);
    }
}
