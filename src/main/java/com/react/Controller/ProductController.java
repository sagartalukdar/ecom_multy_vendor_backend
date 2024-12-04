package com.react.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.react.Exception.ProductException;
import com.react.Model.Product;
import com.react.Service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/{productId}")
	public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws ProductException{
		return new ResponseEntity<Product>(productService.findProductById(productId),HttpStatus.OK);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) {
		return new ResponseEntity<List<Product>>(productService.searchProduct(query),HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<Page<Product>> getAllProducts(
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String brand,
			@RequestParam(required = false) String color,
			@RequestParam(required = false) String size,
			@RequestParam(required = false) Integer minPrice,
			@RequestParam(required = false) Integer maxPrice,
			@RequestParam(required = false) Integer minDiscount,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false) String stock,
			@RequestParam(defaultValue = "0") Integer pageNumber
			){
		
		return new ResponseEntity<Page<Product>>(
				productService.getAllProducts(category, brand,
						color, size, minPrice, maxPrice, 
						minDiscount, sort, stock, pageNumber),
				HttpStatus.OK
				);
	}
	
	

}
