package com.react.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.react.Config.JwtProvider;
import com.react.Exception.SellerException;
import com.react.Model.Address;
import com.react.Model.Seller;
import com.react.Repository.AddressRepository;
import com.react.Repository.SellerRepository;
import com.react.Service.SellerService;
import com.react.domain.AccountStatus;
import com.react.domain.USER_ROLE;

@Service
public class SellerServiceImpl implements SellerService{
	
	@Autowired
	private SellerRepository sellerRepository;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Seller getSellerProfile(String jwt) throws SellerException {
		return getSellerByEmail(jwtProvider.getEmailFromJwt(jwt));
	}

	@Override
	public Seller createSeller(Seller seller) throws SellerException {
		Seller sellerExist=sellerRepository.findByEmail(seller.getEmail());
		if(sellerExist!=null) {
			throw new SellerException("seller already exist with "+seller.getEmail()+",use different email .");
		}
		Address savedAddress=addressRepository.save(seller.getPickupAddress());
		Seller newSeller=new Seller();
		newSeller.setEmail(seller.getEmail());
		newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
		newSeller.setSellerName(seller.getSellerName());
		newSeller.setPickupAddress(savedAddress);
		newSeller.setGstin(seller.getGstin());
		newSeller.setRole(USER_ROLE.ROLE_SELLER);
		newSeller.setMobile(seller.getMobile());
		newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());
        return sellerRepository.save(newSeller);
	}

	@Override
	public Seller getSellerById(Long id) throws SellerException {
		return sellerRepository.findById(id).orElseThrow(()->new SellerException("seller not found with id :"+id));
	}

	@Override
	public Seller getSellerByEmail(String email) throws SellerException {
		Seller seller=sellerRepository.findByEmail(email);
		if(seller==null) {
			throw new SellerException("seller not foud with email :"+email);
		}
		return seller;
	}

	@Override
	public List<Seller> getAllSellers(AccountStatus status) {
		return sellerRepository.findByAccountStatus(status);
	}

	@Override
	public Seller updateSeller(Long sellerId, Seller seller) throws SellerException {
		Seller existingSeller=getSellerById(sellerId);
		
		if(seller.getSellerName()!=null) {
			existingSeller.setSellerName(seller.getSellerName());
		}
		if(seller.getMobile()!=null) {
			existingSeller.setMobile(seller.getMobile());
		}
		if(seller.getBusinessDetails()!=null && seller.getBusinessDetails().getBusinessName()!=null) {
			existingSeller.getBusinessDetails().setBusinessName(
					seller.getBusinessDetails().getBusinessName()
		    );
		}
		if(seller.getBankDetails()!=null
			&& seller.getBankDetails().getAccountHolderName()!=null
			&& seller.getBankDetails().getIfscCode()!=null
			&& seller.getBankDetails().getAccountNumber()!=null
		  ) {
			existingSeller.getBankDetails().setAccountHolderName(
					seller.getBankDetails().getAccountHolderName()
					);
			existingSeller.getBankDetails().setAccountNumber(
					seller.getBankDetails().getAccountNumber()
					);
			existingSeller.getBankDetails().setIfscCode(
					seller.getBankDetails().getIfscCode()
					);
		}
		if(seller.getPickupAddress() !=null
				&& seller.getPickupAddress().getAddress()!=null
				&& seller.getPickupAddress().getMobile()!=null
				&& seller.getPickupAddress().getCity()!=null
				&& seller.getPickupAddress().getState()!=null
				&& seller.getPickupAddress().getPinCode()!=null
		) {
			existingSeller.getPickupAddress().setAddress(
					seller.getPickupAddress().getAddress()
					);
			existingSeller.getPickupAddress().setCity(
					seller.getPickupAddress().getCity()
					);
			existingSeller.getPickupAddress().setState(
					seller.getPickupAddress().getState()
					);
			existingSeller.getPickupAddress().setMobile(
					seller.getPickupAddress().getMobile()
					);
			existingSeller.getPickupAddress().setPinCode(
					seller.getPickupAddress().getPinCode()
					);
		}
		if(seller.getGstin()!=null) {
			existingSeller.setGstin(seller.getGstin());
		}
		return sellerRepository.save(existingSeller);
	}

	@Override
	public void deleteSeller(Long sellerId) throws SellerException  {
		Seller seller=getSellerById(sellerId);
		sellerRepository.delete(seller);
	}

	@Override
	public Seller verifyEmail(String email) throws SellerException  {
		Seller seller=getSellerByEmail(email);
		seller.setEmailVarified(true);
		return sellerRepository.save(seller);
	}

	@Override
	public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws SellerException  {
		Seller seller=getSellerById(sellerId);
		seller.setAccountStatus(status);
		return sellerRepository.save(seller);
	}

}
