package com.react.Service;

import com.react.Exception.CartItemException;
import com.react.Model.CartItem;

public interface CartItemService {

	public CartItem updateCartItem(Long userId,Long id,CartItem cartItem) throws CartItemException;
	public void removeCartItem(Long userId,Long id) throws CartItemException;
	public CartItem findCartItemById(Long id) throws CartItemException;
}
