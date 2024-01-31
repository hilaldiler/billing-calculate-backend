package com.billing.calculator.demo.controller.product;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.billing.calculator.demo.model.Product;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class ProductController {
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/getProduct")
	public ResponseEntity<Product> getProduct(@RequestParam MultipartFile file, @RequestParam String productCode, @RequestParam String customerType) throws IOException {
		try (InputStream inputStream = file.getInputStream()) {
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Product product = findProductByProductCode(sheet, productCode, customerType);
			return ResponseEntity.ok(product);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/searchProduct")
	public ResponseEntity<List<Product>> searchProduct(@RequestParam MultipartFile file, @RequestParam String filteredProductName, @RequestParam String customerType) throws IOException {
		try (InputStream inputStream = file.getInputStream()) {
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			List<Product> productInfo = findSearchedByProductNameValue(sheet, filteredProductName, customerType);
			return ResponseEntity.ok(productInfo);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private int findProductNameIndex(Sheet sheet) {

		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals("ürün adı")) {
					return cell.getColumnIndex();
				}
			}
		}
		return 0;
	}

	private int findProductCodeIndex(Sheet sheet, String productCode) {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(productCode)) {
					return cell.getRowIndex();
				}
			}
		}
		return 0;
	}

	private Product findProductByProductCode(Sheet sheet, String productCode, String customerType) {
		int codeIndex = findProductCodeIndex(sheet, productCode);
		int productNameIndex = findProductNameIndex(sheet);

		String productName = sheet.getRow(codeIndex).getCell(productNameIndex).getStringCellValue();
		Double price = findIntersectionUnitPriceCellValue(sheet, productCode, customerType);
		return new Product(productCode, productName, price);
	}
	
	private List<Product> findSearchedByProductNameValue(Sheet sheet, String productName, String customerType) {
		try {
			Iterator<Row> iterator = sheet.iterator();
			List<Product> productList = new ArrayList<>();
			int productNameIndex = findProductNameIndex(sheet);
			int customerTypeIndex = findCustomerTypeIndex(sheet, customerType);

			while (iterator.hasNext()) {
				Row row = iterator.next();
				Cell cell = row.getCell(0);

				if (cell != null) {
					String contained = sheet.getRow(cell.getRowIndex()).getCell(productNameIndex).getStringCellValue();
					if (contained.toLowerCase().contains(productName) || contained.toUpperCase().contains(productName)) {
						int codeIndex = findProductCodeIndex(sheet, cell.getStringCellValue());
						Double unitPriceVal = sheet.getRow(codeIndex).getCell(customerTypeIndex).getNumericCellValue();
						productList.add(
								new Product(cell.getStringCellValue(), contained, formatDoubleValue(unitPriceVal)));
					}

				}
			}
		return productList;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		
	}
	
	private Double findIntersectionUnitPriceCellValue(Sheet sheet, String productCode, String customerType) {
		int codeIndex = findProductCodeIndex(sheet, productCode);
		int customerTypeIndex = findCustomerTypeIndex(sheet, customerType);
		Double unitPriceVal = sheet.getRow(codeIndex).getCell(customerTypeIndex).getNumericCellValue();

        Double unitPrice = formatDoubleValue(unitPriceVal);
        
		return unitPrice;
	}
	
	private int findCustomerTypeIndex(Sheet sheet, String customerType) {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase(customerType)) {
					return cell.getColumnIndex();
				}
			}
		}
		return 0;
	}
	
	private Double formatDoubleValue(Double value) {
		BigDecimal bigDecimalValue = new BigDecimal(String.valueOf(value));
        String formattedValue = bigDecimalValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        return Double.parseDouble(formattedValue);
	}

}
