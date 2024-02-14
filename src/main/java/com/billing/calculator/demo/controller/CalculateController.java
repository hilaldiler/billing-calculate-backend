package com.billing.calculator.demo.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billing.calculator.demo.model.Billing;
import com.billing.calculator.demo.model.Product;
import com.billing.calculator.demo.request.BillingRequest;

@RestController
@RequestMapping
@CrossOrigin(origins = "https://sernteklifal.vercel.app")
public class CalculateController {

	@PostMapping("/calculateBilling")
	public ResponseEntity<Billing> calculateBilling(@RequestBody BillingRequest request) throws IOException {
		try{
			Billing billing = new Billing();
			Double totalPrice = 0.00;
			Double totalPriceWithTax = 0.00;
			Double totalPriceWithDiscount = 0.00;
			Double tax = 0.00;
			
			List<Product> items = request.getItems();
			String taxRate = request.getTaxRate();
			String discRate = request.getDiscRate();
			for(Product item : items) {
				Integer count = item.getProductCount();
				Double unitPrice = item.getPrice();
				Double itemTotalPrice = formatDoubleValue(unitPrice*count);
				totalPrice = formatDoubleValue(totalPrice + itemTotalPrice);
			}
			if(!"".equals(taxRate)) {
				tax = formatDoubleValue(calculateRate(Double.parseDouble(taxRate), totalPrice));
				billing.setTaxPrice(tax);
			}
			if(!"".equals(discRate)) {
				double disc = formatDoubleValue(calculateRate(Double.parseDouble(discRate), totalPrice));
				billing.setDiscPrice(disc);
				totalPriceWithDiscount = formatDoubleValue(totalPrice-disc);
			}
			if(!"".equals(taxRate) && !"".equals(discRate)) {
				totalPriceWithTax = formatDoubleValue(totalPriceWithDiscount + tax); 
			}else if(!"".equals(taxRate) &&  "".equals(discRate)) {
				totalPriceWithTax = formatDoubleValue(totalPrice + tax); 
			}
			
			if(tax==0) {
				totalPriceWithTax = formatDoubleValue(totalPrice); 
			}
			
			
			billing.setTotalPrice(formatDoubleValue(totalPrice));
			billing.setTotalPriceWithDiscount(totalPriceWithDiscount);
			billing.setTotalPriceWithTax(totalPriceWithTax);
			billing.setProducts(items);
			
			return ResponseEntity.ok(billing);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	private double calculateRate(double obtained, double total) {
        return obtained * total / 100;
    }
	
	
	private Double formatDoubleValue(Double value) {
		BigDecimal bigDecimalValue = new BigDecimal(String.valueOf(value));
        String formattedValue = bigDecimalValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        return Double.parseDouble(formattedValue);
	}
}
