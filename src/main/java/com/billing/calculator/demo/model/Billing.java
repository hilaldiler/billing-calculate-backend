package com.billing.calculator.demo.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Billing {
	
	private Double totalPrice;
	private Double totalPriceWithTax;
	private Double totalPriceWithDiscount;
	private Double taxPrice;
	private Double discPrice;
	private List<Product> products;

}
