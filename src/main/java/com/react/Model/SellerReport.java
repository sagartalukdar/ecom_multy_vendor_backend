package com.react.Model;

import com.react.domain.AccountStatus;
import com.react.domain.USER_ROLE;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class SellerReport {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne
	private Seller seller;
	
	private Long totalEarnings=0L;
	
	private Long totalSales=0L;
	
	private Long totalRefunds=0L;
	
	private Long totalTax=0L;

	private Long netEarnings=0L;
	
	private Integer totalOrders=0;
	
	private Integer canceledOrders=0;
	
	private Integer totalTransactions=0;

}
