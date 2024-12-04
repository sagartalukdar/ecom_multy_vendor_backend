package com.react.Repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.Model.Cart;
import com.react.Model.CartItem;
import com.react.Model.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	public CartItem findByCartAndProductAndSize(Cart cart,Product product,String size);
	
	public Set<CartItem> findByCart(Cart cart);
}
