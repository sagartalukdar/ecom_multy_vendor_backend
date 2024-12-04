package com.react.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.Model.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>{

	public Coupon findByCode(String code);
}
