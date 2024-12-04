package com.react.ServiceImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.react.Model.Cart;
import com.react.Model.Coupon;
import com.react.Model.User;
import com.react.Repository.CartRepository;
import com.react.Repository.CouponRepository;
import com.react.Repository.UserRepository;
import com.react.Service.CouponService;

@Service
public class CouponServiceImpl implements CouponService{

	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
		Coupon coupon=couponRepository.findByCode(code);
		Cart cart=cartRepository.findByUserId(user.getId());
		if(coupon==null) {
			throw new Exception("coupon not valid");
		}
		if(user.getUsedCoupons().contains(coupon)) {
			throw new Exception("coupon already used");
		}
		if(orderValue<coupon.getMinimumOrderValue()) {
			throw new Exception("valid for minimum order value "+coupon.getMinimumOrderValue());
		}
		if(coupon.isActive() && LocalDate.now().isAfter(coupon.getValidityStartDate()) &&
				LocalDate.now().isBefore(coupon.getValidityEndDate())) {
			user.getUsedCoupons().add(coupon);
			userRepository.save(user);
			double discountedPrice=(cart.getTotalSellingPrice()*coupon.getDiscountedPercentage())/100;
			cart.setTotalSellingPrice(cart.getTotalSellingPrice()-discountedPrice);
			cart.setCouponCode(code);
			cartRepository.save(cart);
			return cart;
		}
		throw new Exception("coupon not vsalid"); 
	}

	@Override
	public Cart removeCoupon(String code, User user) throws Exception {
		Coupon coupon=couponRepository.findByCode(code);
		Cart cart=cartRepository.findByUserId(user.getId());
		if(coupon==null) {
			throw new Exception("coupon not find for "+code);
		}
		double discountedPrice=(cart.getTotalSellingPrice()*coupon.getDiscountedPercentage())/100;
		cart.setTotalSellingPrice(cart.getTotalSellingPrice()+discountedPrice);
        cart.setCouponCode(null);
        return cartRepository.save(cart);
        
		
	}

	@Override
	public Coupon findCouponById(Long id) throws Exception {
		return couponRepository.findById(id).orElseThrow(()->
		new Exception("coupon not found with id :"+id)
				);
	}

	@Override
	@PreAuthorize("hasRole ('ADMIN')")
	public Coupon createCoupon(Coupon coupon) {
		return couponRepository.save(coupon);
	}

	@Override
	public List<Coupon> findAllCoupon() {
		return couponRepository.findAll();
	}

	@Override
	@PreAuthorize("hasRole ('ADMIN')")
	public void deleteCoupon(Long id) throws Exception {
		Coupon coupon=findCouponById(id);
		couponRepository.delete(coupon);
	}

}
