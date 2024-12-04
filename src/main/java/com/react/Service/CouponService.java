package com.react.Service;

import java.util.List;

import com.react.Model.Cart;
import com.react.Model.Coupon;
import com.react.Model.User;

public interface CouponService {

	public Cart applyCoupon(String code,double orderValue,User user) throws Exception;
	public Cart removeCoupon(String code,User user) throws Exception;
	public Coupon findCouponById(Long id) throws Exception;
	public Coupon createCoupon(Coupon coupon);
	public List<Coupon> findAllCoupon();
	public void deleteCoupon(Long id) throws Exception;
}
