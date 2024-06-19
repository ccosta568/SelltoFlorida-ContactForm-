package com.pwm.aws.crud.lambda.api;

import com.pwm.aws.crud.lambda.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}