package com.react.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Model.HomeCategory;
import com.react.Repository.HomeCategoryRepository;
import com.react.Service.HomeCategoryService;

@Service
public class HomeCategoryServiceImpl implements HomeCategoryService{

	@Autowired
	private HomeCategoryRepository homeCategoryRepository;

	@Override
	public HomeCategory createHomeCategory(HomeCategory homeCategory) {
		return homeCategoryRepository.save(homeCategory);
	}

	@Override
	public List<HomeCategory> createCategories(List<HomeCategory> categories) {
		if(homeCategoryRepository.findAll().isEmpty()) {
			return homeCategoryRepository.saveAll(categories);
		}
		return homeCategoryRepository.findAll();
	}

	@Override
	public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception {
		HomeCategory existingCategory=homeCategoryRepository.findById(id).
				orElseThrow(()->new Exception("category not found"));
		if(homeCategory.getImage()!=null) {
			existingCategory.setImage(homeCategory.getImage());
		}
		if(homeCategory.getCategoryId()!=null) {
			existingCategory.setCategoryId(homeCategory.getCategoryId());
		}
		return homeCategoryRepository.save(existingCategory);
	}

	@Override
	public List<HomeCategory> getAllHomeCategories() {
		return homeCategoryRepository.findAll();
	}
	
	
}
