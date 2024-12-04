package com.react.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.Exception.SellerException;
import com.react.Model.Product;
import com.react.Model.Seller;
import com.react.Request.ProductRequest;
import com.react.Service.ProductService;
import com.react.Service.SellerService;

@RestController
@RequestMapping("/api/sellers/products")
public class SellerProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private SellerService sellerService;
	
	@GetMapping("/")
	public ResponseEntity<List<Product>> getProductsBySellerId(@RequestHeader("Authorization")String jwt) throws SellerException{
		return new ResponseEntity<List<Product>>(
				 productService.getProductsBySellerId(sellerService.getSellerProfile(jwt).getId())
				 ,HttpStatus.OK
				);
	}
	@PostMapping("/create")
	public ResponseEntity<Product> createProduct(@RequestBody ProductRequest req,@RequestHeader("Authorization")String jwt) throws SellerException{
		Seller seller=sellerService.getSellerProfile(jwt);
		return new ResponseEntity<Product>(productService.createProduct(req, seller),HttpStatus.CREATED);
	}
	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
		try {
			productService.deleteProduct(productId);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	@PutMapping("/update/{productId}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long productId ,@RequestBody Product product){
		Product updateProduct=productService.updateProduct(productId, product);
		return new ResponseEntity<Product>(updateProduct,HttpStatus.OK);
	}

}
