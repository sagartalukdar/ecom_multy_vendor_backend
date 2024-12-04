package com.react.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Model.Product;
import com.react.Model.User;
import com.react.Model.WishList;
import com.react.Repository.WishListRepository;
import com.react.Service.WishListService;

@Service
public class WishListServiceImpl implements WishListService{

	@Autowired
	private WishListRepository wishListRepository;
	
	@Override
	public WishList createWishList(User user) {
		WishList wishList=new WishList();
		wishList.setUser(user);
		return wishListRepository.save(wishList);
	}

	@Override
	public WishList getWishListByUser(User user) {
		WishList wishList=wishListRepository.findByUserId(user.getId());
		if(wishList==null) {
			wishList=createWishList(user);
		}
		return wishList;
	}

	@Override
	public WishList addProductToWishList(User user, Product product) {
		WishList wishList=getWishListByUser(user);
		if(wishList.getProducts().contains(product)) {
			wishList.getProducts().remove(product);
		}else {
			wishList.getProducts().add(product);
		}
		return wishListRepository.save(wishList);
	}

}
