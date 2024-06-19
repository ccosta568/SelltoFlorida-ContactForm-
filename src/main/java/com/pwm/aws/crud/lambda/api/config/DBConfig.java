package com.pwm.aws.crud.lambda.api.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")  // Replace us-west-2 with your desired AWS region
                .build();
    }

    @Bean
    public DynamoDB dynamoDB(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDB(amazonDynamoDB);
    }
    private JSONObject getCorsHeaders() {
        JSONObject headers = new JSONObject();
        headers.put("Access-Control-Allow-Origin", "http://localhost:4200");
        headers.put("Access-Control-Allow-Methods", "OPTIONS, POST, PUT");
        headers.put("Access-Control-Allow-Headers", "Content-Type");
        return headers;
    }
}

