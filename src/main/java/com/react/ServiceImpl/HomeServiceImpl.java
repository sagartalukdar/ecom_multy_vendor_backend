package com.react.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Model.Deal;
import com.react.Model.Home;
import com.react.Model.HomeCategory;
import com.react.Repository.DealRepository;
import com.react.Repository.HomeCategoryRepository;
import com.react.Service.HomeService;
import com.react.domain.HomeCategorySection;

@Service
public class HomeServiceImpl implements HomeService{

	@Autowired
	private HomeCategoryRepository homeCategoryRepository;
	@Autowired
	private DealRepository dealRepository;
	
	@Override
	public Home createHomePageData(List<HomeCategory> categories) {
		
		List<HomeCategory> gridCategories=categories.stream().
				filter(category->category.getSection()==HomeCategorySection.GRID)
				.collect(Collectors.toList());
		
		List<HomeCategory> shopByCategories=categories.stream().
				filter(category->category.getSection()==HomeCategorySection.SHOP_BY_CATEGORIES)
				.collect(Collectors.toList());
		
		List<HomeCategory> electricCategories=categories.stream().
				filter(category->category.getSection()==HomeCategorySection.ELECTRICS_CATEGORIES)
				.collect(Collectors.toList());
		
		List<HomeCategory> dealCategories=categories.stream().
				filter(category->category.getSection()==HomeCategorySection.DEALS)
				.toList();
		
		List<Deal> createdDeals=new ArrayList<>();
		if(dealRepository.findAll().isEmpty()) {
			List<Deal> deals=categories.stream().
					filter(category->category.getSection()==HomeCategorySection.DEALS)
					.map(category->new Deal(null, 10, category))
					.collect(Collectors.toList());
			createdDeals=dealRepository.saveAll(deals);
		}else {
			createdDeals=dealRepository.findAll();
		}
		
		Home home=new Home();
		home.setGrid(gridCategories);
		home.setElectricCategories(electricCategories);
		home.setShopByCategories(shopByCategories);
		home.setDeals(createdDeals);
		home.setDealCategories(dealCategories);
		
		return home;
		
	}

}
