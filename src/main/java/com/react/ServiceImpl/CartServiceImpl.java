package com.react.ServiceImpl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Model.Cart;
import com.react.Model.CartItem;
import com.react.Model.Product;
import com.react.Model.User;
import com.react.Repository.CartItemRepository;
import com.react.Repository.CartRepository;
import com.react.Service.CartService;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartItemRepository cartItemRepository;
	
	public int calculateDiscountPercentage(int mrpPrice,int sellingPrice) {
		if(mrpPrice<=0) {
			 return 0;
		}
		double discount=mrpPrice-sellingPrice;
		double discountPercentage=(discount/mrpPrice)*100;
		return (int) discountPercentage;
	}
	
	@Override
	public CartItem addCartItem(User user, Product product, String size, int quantity) {
       Cart cart=findUserCartForAddItem(user);
       
       CartItem isPresent=cartItemRepository.findByCartAndProductAndSize(cart, product, size);
       if(isPresent==null) {
    	   CartItem cartItem=new CartItem();
    	   cartItem.setProduct(product);
    	   cartItem.setQuantity(quantity);
    	   cartItem.setUserId(user.getId());
    	   cartItem.setSize(size);
    	   
    	   int totalPrice=quantity*product.getSellingPrice();
    	   int totalMrpPrice=quantity*product.getMrpPrice();
    	   
    	   cartItem.setMrpPrice(totalMrpPrice);
    	   cartItem.setSellingPrice(totalPrice);
    	   cartItem.setCart(cart);
//    	   hibernate will update the cart automatically
    	   
//    	   CartItem savedCartItem= cartItemRepository.save(cartItem);
//    	   cart.getCartItems().add(savedCartItem);	   
//    	   cartRepository.save(cart);
    	   
    	   return cartItemRepository.save(cartItem);
       }
       return isPresent;
	}
	
	@Override
	public Cart findUserCartForFetchCart(User user) {
		Cart cart=cartRepository.findByUserId(user.getId());
		
		int totalPrice=0;
		int totalDiscountedPrice=0;
		int totalItem=0;
		
		Set<CartItem>cartItems=cartItemRepository.findByCart(cart);
		cart.setCartItems(cartItems);
		for(CartItem cartItem:cart.getCartItems()) {
			totalPrice+=cartItem.getMrpPrice();
			totalDiscountedPrice+=cartItem.getSellingPrice();
			totalItem+=cartItem.getQuantity();
		}
		cart.setTotalMrpPrice(totalPrice);
		cart.setTotalSellingPrice(totalDiscountedPrice);
		cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));
		cart.setTotalItem(totalItem);
		return cart;
	}
	
	@Override
	public Cart findUserCartForAddItem(User user) {
		Cart cart=cartRepository.findByUserId(user.getId());
		
		int totalPrice=0;
		int totalDiscountedPrice=0;
		int totalItem=0;
		
		for(CartItem cartItem:cart.getCartItems()) {
			totalPrice+=cartItem.getMrpPrice();
			totalDiscountedPrice+=cartItem.getSellingPrice();
			totalItem+=cartItem.getQuantity();
		}
		cart.setTotalMrpPrice(totalPrice);
		cart.setTotalSellingPrice(totalDiscountedPrice);
		cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));
		cart.setTotalItem(totalItem);
		return cart;
	}
	
	
}
