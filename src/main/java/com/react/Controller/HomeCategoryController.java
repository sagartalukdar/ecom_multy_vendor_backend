package com.react.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.Model.Home;
import com.react.Model.HomeCategory;
import com.react.Repository.HomeCategoryRepository;
import com.react.Service.HomeCategoryService;
import com.react.Service.HomeService;

@RestController
@RequestMapping("/api")
public class HomeCategoryController {

	@Autowired
	private HomeCategoryRepository homeCategoryRepository;
	@Autowired
	private HomeCategoryService homeCategoryService;
	@Autowired
	private HomeService homeService;
	
	@PostMapping("/homecategory")
	public ResponseEntity<Home> createHomeCategories(@RequestBody List<HomeCategory> homeCategories){
		List<HomeCategory> categories=homeCategoryService.createCategories(homeCategories);
		Home home=homeService.createHomePageData(categories);
		return new ResponseEntity<Home>(home,HttpStatus.CREATED);
	}
	
	@GetMapping("/admin/homecategory")
	public ResponseEntity<List<HomeCategory>> getHomeCategories(){
		return new ResponseEntity<List<HomeCategory>>(homeCategoryService.getAllHomeCategories(),HttpStatus.OK);
	} 
	
	@PutMapping("/admin/homecategory/{id}")
	public ResponseEntity<HomeCategory> updateHomeCategory(@PathVariable Long id,@RequestBody HomeCategory homeCategory) throws Exception{
       return new ResponseEntity<HomeCategory>(homeCategoryService.updateHomeCategory(homeCategory, id),HttpStatus.OK);
	} 
	
}
