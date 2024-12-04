package com.react.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.Model.Cart;
import com.react.Model.CartItem;
import com.react.Model.Product;
import com.react.Model.User;
import com.react.Request.AddItemRequest;
import com.react.Response.ApiResponse;
import com.react.Service.CartItemService;
import com.react.Service.CartService;
import com.react.Service.ProductService;
import com.react.Service.UserService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	@Autowired
	private CartItemService cartItemService;
	@Autowired
	private UserService userService;
	@Autowired
	private ProductService productService;
	
	@GetMapping("/")
	public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization")String jwt) throws Exception{
		User user=userService.findUserByJwt(jwt);
		Cart cart=cartService.findUserCartForFetchCart(user);
		return new ResponseEntity<Cart>(cart,HttpStatus.OK);
	}
	@PutMapping("/add")
	public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req,@RequestHeader("Authorization")String jwt) throws Exception{
		User user=userService.findUserByJwt(jwt);
		Product product=productService.findProductById(req.getProductId());
		CartItem item=cartService.addCartItem(user, product, req.getSize(), req.getQuantity());
		ApiResponse res=new ApiResponse();
		res.setMessage("item added to cart");
		return new ResponseEntity<CartItem>(item,HttpStatus.ACCEPTED);		
	}
	@DeleteMapping("/cartItem/{cartItemId}")
	public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long cartItemId,@RequestHeader("Authorization")String jwt) throws Exception{
		User user=userService.findUserByJwt(jwt);
		cartItemService.removeCartItem(user.getId(), cartItemId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("item removed from cart"),HttpStatus.OK);
	}
	
	@PutMapping("/cartItem/{cartItemId}")
	public ResponseEntity<CartItem> updateCartItem(@PathVariable Long cartItemId,@RequestHeader("Authorization")String jwt,
			@RequestBody CartItem cartItem
			) throws Exception{
		User user=userService.findUserByJwt(jwt);
		
		CartItem updateCartItem=null;
		if(cartItem.getQuantity()>0) {
			updateCartItem=cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
		}
		return new ResponseEntity<CartItem>(updateCartItem,HttpStatus.OK);
	}
	

}
