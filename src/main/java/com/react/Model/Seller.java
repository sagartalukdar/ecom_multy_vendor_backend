package com.react.Model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.react.domain.AccountStatus;
import com.react.domain.USER_ROLE;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@EqualsAndHashCode
public class Seller {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String sellerName;
	
	private String mobile;
	
	@Column(unique = true,nullable = false)
	private String email;
	
	private String password;
	
	@Embedded
	private BusinessDetails businessDetails=new BusinessDetails();
	
	@Embedded
	private BankDetails bankDetails=new BankDetails();
	
	@OneToOne(cascade = CascadeType.ALL)
	private Address pickupAddress=new Address();
	
	private String gstin;
	
	private USER_ROLE role=USER_ROLE.ROLE_SELLER;
	
	private boolean isEmailVarified=false;
	
	private AccountStatus accountStatus=AccountStatus.PENDING_VERIFICATION;
	
	

}
