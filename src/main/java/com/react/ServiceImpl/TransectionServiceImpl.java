package com.react.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Model.Seller;
import com.react.Model.Transaction;
import com.react.Model.UserOrder;
import com.react.Repository.SellerRepository;
import com.react.Repository.TransectionRepository;
import com.react.Service.TransectionService;

@Service
public class TransectionServiceImpl implements TransectionService{

	@Autowired
	private TransectionRepository transectionRepository;
	@Autowired
	private SellerRepository sellerRepository;
	
	@Override
	public Transaction createTransection(UserOrder order) {
		Seller seller=sellerRepository.findById(order.getSellerId()).get();
		Transaction transaction=new Transaction();
		transaction.setSeller(seller);
		transaction.setCustomer(order.getUser());
		transaction.setOrder(order);
		return transectionRepository.save(transaction);
	}

	@Override
	public List<Transaction> getTransectionBySellerId(Seller seller) {
		return transectionRepository.findBySellerId(seller.getId());
	}

	@Override
	public List<Transaction> getAllTransections() {
		return transectionRepository.findAll();
	}

	
}
