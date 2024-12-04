package com.react.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.Exception.SellerException;
import com.react.Model.Seller;
import com.react.Model.UserOrder;
import com.react.Service.OrderService;
import com.react.Service.SellerService;
import com.react.domain.OrderStatus;

@RestController
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private SellerService sellerService;
	
	@GetMapping("/all")
	public ResponseEntity<List<UserOrder>> getAllSellerOrders(@RequestHeader("Authorization")String jwt) throws SellerException{
		Seller seller=sellerService.getSellerProfile(jwt);
		return new ResponseEntity<List<UserOrder>>(orderService.sellerOrder(seller.getId()),HttpStatus.OK);
	}
	
	@PutMapping("/{orderId}/status/{orderStatus}")
	public ResponseEntity<UserOrder> updateOrder(@PathVariable Long orderId,@PathVariable OrderStatus orderStatus) throws Exception{
		return new ResponseEntity<UserOrder>(orderService.updateOrderStatus(orderId, orderStatus),HttpStatus.OK);
	}
}
