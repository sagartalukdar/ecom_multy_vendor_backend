package com.react.Service;

import java.util.List;

import com.react.Exception.SellerException;
import com.react.Model.Seller;
import com.react.domain.AccountStatus;

public interface SellerService {

	public Seller getSellerProfile(String jwt) throws SellerException;
	
	public Seller createSeller(Seller seller) throws SellerException;
	
	public Seller getSellerById(Long id) throws SellerException;
	
	public Seller getSellerByEmail(String email) throws SellerException;
	
	public List<Seller> getAllSellers(AccountStatus status);
	
	public Seller updateSeller(Long sellerId,Seller seller) throws SellerException;
	
	public void deleteSeller(Long sellerId) throws SellerException;
	
	public Seller verifyEmail(String email) throws SellerException;
	
	public Seller updateSellerAccountStatus(Long sellerId,AccountStatus status) throws SellerException;
}
