package com.pwm.aws.crud.lambda.api.model;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBService {

    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDB dynamoDB;

    @Autowired
    public DBService(AmazonDynamoDB amazonDynamoDB, DynamoDB dynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.dynamoDB = dynamoDB;
    }

    public Table getTable(String tableName) {
        return dynamoDB.getTable(tableName);
    }

    public void saveProduct(Product product, String tableName) {
        Table table = dynamoDB.getTable(tableName);
        table.putItem(new Item()
                .withString("name", product.getName())
                .withString("email", product.getEmail())
                .withString("phone", product.getPhone())
                .withString("message", product.getMessage()));
    }
}
