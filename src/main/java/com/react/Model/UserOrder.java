package com.react.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.react.domain.OrderStatus;
import com.react.domain.PaymentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data 
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String orderId;
	
	@ManyToOne
	private User user;
	
	private Long sellerId;
	
	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<OrderItem> orderItems=new ArrayList<>();
	
	@ManyToOne
	private Address shippingAddress;
	
	@Embedded
	private PaymentDetails paymentDetails=new PaymentDetails();
	
	private double totalMrpPrice;
	
	private Integer totalSellingPrice;
	
	private Integer discount;
	
	private OrderStatus orderStatus;
	
	private int totalItem;
		
	private LocalDateTime orderDate=LocalDateTime.now();
	
	private LocalDateTime deliverDate=orderDate.plusDays(7);
}