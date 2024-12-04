package com.react.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Exception.CartItemException;
import com.react.Model.CartItem;
import com.react.Model.User;
import com.react.Repository.CartItemRepository;
import com.react.Service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService{
	
	@Autowired
	private CartItemRepository cartItemRepository;

	@Override
	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException {
		CartItem item=findCartItemById(id);
	    
		User cartItemUser=item.getCart().getUser();
		if(cartItemUser.getId().equals(userId)) {
			item.setQuantity(cartItem.getQuantity());
			item.setMrpPrice(item.getQuantity()*item.getProduct().getMrpPrice());
			item.setSellingPrice(item.getQuantity()*item.getProduct().getSellingPrice());
			return cartItemRepository.save(item);
		}
		throw new CartItemException("you can't update this cartItem");	
	}

	@Override
	public void removeCartItem(Long userId, Long id) throws CartItemException {
		CartItem item=findCartItemById(id);
		User cartItemUser=item.getCart().getUser();
		if(cartItemUser.getId().equals(userId)) {
			cartItemRepository.delete(item);
		}else {
			throw new CartItemException("you can't delete this");
		}
	}

	@Override
	public CartItem findCartItemById(Long id) throws CartItemException {
		return cartItemRepository.findById(id).orElseThrow(()->
		new CartItemException("cartitem not found with id :"+id)
				);
	}

}
