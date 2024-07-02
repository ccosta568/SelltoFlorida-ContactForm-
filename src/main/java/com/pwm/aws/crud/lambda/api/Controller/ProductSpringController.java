package com.pwm.aws.crud.lambda.api.Controller;

import com.pwm.aws.crud.lambda.api.Repository.ProductRepository;
import com.pwm.aws.crud.lambda.api.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "Content-Type")
@RequestMapping("/api")
public class ProductSpringController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/getContacts")
    public ResponseEntity<List<Product>> getAllContacts() {
        try {
            List<Product> productList = new ArrayList<>(productRepository.findAll());

            if (productList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(productList, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getContactById/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> productObj = productRepository.findById(id);
        if (productObj.isPresent()) {
            return new ResponseEntity<>(productObj.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/contract")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            Product productObj = productRepository.save(product);
            return new ResponseEntity<>(productObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}