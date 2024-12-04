package com.react.Service;

import java.util.List;

import com.react.Model.HomeCategory;

public interface HomeCategoryService {

	public HomeCategory createHomeCategory(HomeCategory homeCategory);
	public List<HomeCategory> createCategories(List<HomeCategory> categories);
	public HomeCategory updateHomeCategory(HomeCategory homeCategory,Long id) throws Exception;
	public List<HomeCategory> getAllHomeCategories();

}
