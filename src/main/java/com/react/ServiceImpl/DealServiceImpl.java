package com.react.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Model.Deal;
import com.react.Model.HomeCategory;
import com.react.Repository.DealRepository;
import com.react.Repository.HomeCategoryRepository;
import com.react.Service.DealService;

@Service
public class DealServiceImpl implements DealService{

	@Autowired
	private DealRepository dealRepository;
	@Autowired
	private HomeCategoryRepository homeCategoryRepository;
	
	@Override
	public List<Deal> getDeals() {
		return dealRepository.findAll();
	}

	@Override
	public Deal createDeal(Deal deal) {
		HomeCategory homeCategory=homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
		deal.setCategory(homeCategory);
		return dealRepository.save(deal);
	}

	@Override
	public Deal updateDeal(Deal deal,Long id) throws Exception {
		Deal existingDeal=dealRepository.findById(id).orElse(null);
		HomeCategory homeCategory=homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);

		if(existingDeal!=null) {
			if(deal.getDiscount()!=null) {
				existingDeal.setDiscount(deal.getDiscount());
			}
			if(homeCategory!=null) {
				existingDeal.setCategory(homeCategory);
			}
			return dealRepository.save(existingDeal);
		}
		throw new Exception("deal not found");
	}

	@Override
	public void deleteDeal(Long id) throws Exception {
	  Deal deal=dealRepository.findById(id).orElseThrow(()->new Exception("deal not found "));	
	  dealRepository.delete(deal);
	}

}
