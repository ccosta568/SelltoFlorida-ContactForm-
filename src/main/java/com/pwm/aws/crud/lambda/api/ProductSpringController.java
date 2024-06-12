package com.pwm.aws.crud.lambda.api;

import com.amazonaws.AmazonClientException;
import com.pwm.aws.crud.lambda.api.model.DBService;
import com.pwm.aws.crud.lambda.api.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductSpringController {

    @Autowired
    private DBService dbService; // A service to handle DynamoDB interactions

    @PostMapping
    public ResponseEntity<String> saveProduct(@RequestBody Product product) {
        try {
            dbService.saveProduct(product, "Products");
            return ResponseEntity.ok("New Contact Registered");
        } catch (AmazonClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // Additional endpoints if needed
}
