package com.react.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.Model.Seller;
import com.react.domain.AccountStatus;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long>{

	public Seller findByEmail(String email);
	
	public List<Seller> findByAccountStatus(AccountStatus accountStatus);
}
