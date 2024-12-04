package com.react.Service;

import java.util.List;

import com.react.Model.Deal;

public interface DealService {

	public List<Deal> getDeals();
	public Deal createDeal(Deal deal);
	public Deal updateDeal(Deal deal,Long id) throws Exception;
	public void deleteDeal(Long id) throws Exception;
}
