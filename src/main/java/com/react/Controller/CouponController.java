package com.react.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.react.Model.Cart;
import com.react.Model.Coupon;
import com.react.Model.User;
import com.react.Response.ApiResponse;
import com.react.Service.CartService;
import com.react.Service.CouponService;
import com.react.Service.UserService;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

	@Autowired
	private CouponService couponService;
	@Autowired
	private UserService userService;
	@Autowired
	private CartService cartService;
	
	@PostMapping("/apply")
	public ResponseEntity<Cart> applyCoupon(@RequestParam String apply,@RequestParam String code,@RequestParam double orderValue, @RequestHeader("Authorization")String jwt) throws Exception{
		User user=userService.findUserByJwt(jwt);
		Cart cart;
		if(apply.equals("true")) {
			cart=couponService.applyCoupon(code, orderValue, user);
		}else {
			cart=couponService.removeCoupon(code, user);
		}
		return new ResponseEntity<Cart>(cart,HttpStatus.OK);
	}
	
	@PostMapping("/admin/create")
	public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon){
		return new ResponseEntity<Coupon>(couponService.createCoupon(coupon),HttpStatus.OK);
	}
	@DeleteMapping("/admin/delete/{id}")
	public ResponseEntity<ApiResponse> deleteCoupon(@PathVariable Long id) throws Exception{
		couponService.deleteCoupon(id);
		ApiResponse res=new ApiResponse("coupon deleted");
		return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Coupon>> getAllCoupon(){
		return new ResponseEntity<List<Coupon>>(couponService.findAllCoupon(),HttpStatus.OK);
	}
	
}
