package com.billing.calculator.demo.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product implements Serializable{
	
	private String productCode;
	private String productName;
	private Integer productCount;
	private String price;
	private Double totalPriceOfProduct;
	
	public Product(String productCode, String productName, String price) {
		this.productCode = productCode;
		this.productName = productName;
		this.price = price;
	}
	

}
