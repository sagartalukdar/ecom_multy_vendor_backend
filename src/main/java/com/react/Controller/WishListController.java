package com.react.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.Model.Product;
import com.react.Model.User;
import com.react.Model.WishList;
import com.react.Service.ProductService;
import com.react.Service.UserService;
import com.react.Service.WishListService;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {
	
	@Autowired
	private WishListService wishListService;	
	@Autowired
	private UserService userService;
	@Autowired
	private ProductService productService;
	
	@GetMapping("/user")
	public ResponseEntity<WishList> getWishListByUser(@RequestHeader("Authorization") String jwt) throws Exception{
		User user=userService.findUserByJwt(jwt);
		return new ResponseEntity<WishList>(wishListService.getWishListByUser(user),HttpStatus.OK);
	}
	@PutMapping("/add-product/{productId}")
	public ResponseEntity<WishList> addProductToWishList(@PathVariable Long productId,@RequestHeader("Authorization") String jwt) throws Exception{
		User user=userService.findUserByJwt(jwt);
		Product product=productService.findProductById(productId);
		WishList wishList=wishListService.addProductToWishList(user, product);
		return new ResponseEntity<WishList>(wishList,HttpStatus.OK);
	}

}
