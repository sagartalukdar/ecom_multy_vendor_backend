package com.react.Service;

import com.react.Model.Product;
import com.react.Model.User;
import com.react.Model.WishList;

public interface WishListService {

	public WishList createWishList(User user);
	
	public WishList getWishListByUser(User user);
	
	public WishList addProductToWishList(User user,Product product);
}
