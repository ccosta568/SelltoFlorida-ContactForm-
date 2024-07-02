package com.pwm.aws.crud.lambda.api.model;

import com.google.gson.Gson;

import javax.persistence.*;


@Entity
@Table(name = "contacts")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "phone")
	private String phone;
	@Column(name = "message")
	private String message;

	// Default constructor
	public Product() {
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

	@Override
	public String toString() {
		return "Product{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}