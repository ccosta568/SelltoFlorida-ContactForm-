package com.pwm.aws.crud.lambda.api.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.google.gson.Gson;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "Product")
@DynamoDBTable(tableName = "Product")
public class Product {
	@Id
	@Column(name = "name")
	private String name;

	@Column(name = "email")
	private String email;

	@Column(name = "phone")
	private String phone;

	@Column(name = "message")
	private String message;

	public Product() {
		// Default constructor for JPA
	}

	public Product(String name, String email, String phone, String message) {
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.message = message;
	}

	public Product(String json) {
		Gson gson = new Gson();
		Product tempProduct = gson.fromJson(json, Product.class);
		this.name = tempProduct.name;
		this.email = tempProduct.email;
		this.phone = tempProduct.phone;
		this.message = tempProduct.message;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail(){
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
