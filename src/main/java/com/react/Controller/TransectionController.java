package com.react.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.Exception.SellerException;
import com.react.Model.Seller;
import com.react.Model.Transaction;
import com.react.Service.SellerService;
import com.react.Service.TransectionService;

@RestController
@RequestMapping("/api/transection")
public class TransectionController {
	
	@Autowired
	private TransectionService transectionService;
	@Autowired
	private SellerService sellerService;

	@GetMapping("/seller")
	public ResponseEntity<List<Transaction>> getTransectionBySeller(@RequestHeader("Authorization")String jwt) throws SellerException{
		Seller seller=sellerService.getSellerProfile(jwt);
		List<Transaction> transactions=transectionService.getTransectionBySellerId(seller);
		return new ResponseEntity<List<Transaction>>(transactions,HttpStatus.OK);
	}
	@GetMapping("/all")
	public ResponseEntity<List<Transaction>> getAllTransection(){
		return new ResponseEntity<List<Transaction>>(transectionService.getAllTransections(),HttpStatus.OK);
	}
}
