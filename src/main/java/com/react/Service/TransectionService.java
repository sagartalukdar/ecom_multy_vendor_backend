package com.react.Service;

import java.util.List;

import com.react.Model.Seller;
import com.react.Model.Transaction;
import com.react.Model.UserOrder;

public interface TransectionService {

	public Transaction createTransection(UserOrder order);
	public List<Transaction> getTransectionBySellerId(Seller seller);
	public List<Transaction> getAllTransections();
}
