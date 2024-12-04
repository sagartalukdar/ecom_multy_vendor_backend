package com.react.Service;

import com.react.Model.Cart;
import com.react.Model.CartItem;
import com.react.Model.Product;
import com.react.Model.User;

public interface CartService {

	public CartItem addCartItem(User user,Product product,String size,int quantity);
	public Cart findUserCartForFetchCart(User user);
	public Cart findUserCartForAddItem(User user);
}
