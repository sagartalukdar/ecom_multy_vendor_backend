package com.react.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.Exception.SellerException;
import com.react.Model.Seller;
import com.react.Service.SellerService;
import com.react.domain.AccountStatus;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private SellerService sellerService;
	
	@PutMapping("/update-seller/{id}/status/{status}")
	public ResponseEntity<Seller> updateSellerStatus(@PathVariable Long id,@PathVariable AccountStatus status) throws SellerException{
		return new ResponseEntity<Seller>(sellerService.updateSellerAccountStatus(id, status),HttpStatus.OK);
	}
	
	
}
