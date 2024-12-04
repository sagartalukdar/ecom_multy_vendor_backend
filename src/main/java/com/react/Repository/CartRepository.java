package com.react.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.Model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

	public Cart findByUserId(Long id);
}
