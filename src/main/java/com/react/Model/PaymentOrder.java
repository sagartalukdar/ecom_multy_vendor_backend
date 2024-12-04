package com.react.Model;

import java.util.HashSet;
import java.util.Set;

import com.react.domain.PaymentMethod;
import com.react.domain.PaymentOrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Long amount;
	
	private PaymentOrderStatus status=PaymentOrderStatus.PENDING;
	
	private PaymentMethod paymentMethod;
	
	private String paymentLinkId;
	
	@ManyToOne
	private User user;
	
	@OneToMany
	private Set<UserOrder> orders=new HashSet<>();


}
