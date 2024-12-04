package com.react.Request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

	private String title;
	
	private String description;
	
	private int mrpPrice;
	
	private int sellingPrice;
	
	private int discountedPercent;
	
	private int quantity;
	
	private String color;
	
	private List<String> images;
	
	private String category1;
	private String category2;
	private String category3;
	
	private String sizes;
}
