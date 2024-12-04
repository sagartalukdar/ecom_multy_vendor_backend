package com.react.Model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.react.domain.USER_ROLE;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data 
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String code;
	
	private double discountedPercentage;
	
	private LocalDate validityStartDate;
	
	private LocalDate validityEndDate;
	
	private double minimumOrderValue;
	
	private boolean isActive=true;
	
	@ManyToMany(mappedBy = "usedCoupons")
	private Set<User> usedByUsers=new HashSet<>(); 
	
}
