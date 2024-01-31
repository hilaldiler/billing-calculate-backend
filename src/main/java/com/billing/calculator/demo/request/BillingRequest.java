package com.billing.calculator.demo.request;

import java.util.List;

import com.billing.calculator.demo.model.Product;

import lombok.Data;

@Data
public class BillingRequest {
	private List<Product> items;
    private String taxRate;
    private String discRate;
}
