package com.react.Model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne
	private User user;
	
	@OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
	private Set<CartItem>cartItems=new HashSet<>();
	
	private double totalSellingPrice;
	
	private int totalItem;
	
	private int totalMrpPrice;
	
	private int discount;
	
	private String couponCode;
}
