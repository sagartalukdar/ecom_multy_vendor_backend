package com.react.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.react.Exception.ProductException;
import com.react.Model.Product;
import com.react.Model.Seller;
import com.react.Request.ProductRequest;

public interface ProductService {

	public Product createProduct(ProductRequest req,Seller seller);	
	public void deleteProduct(Long productId);
	public Product updateProduct(Long productId,Product product);
	public Product findProductById(Long productId) throws ProductException;
	public List<Product> searchProduct(String query);
	public Page<Product> getAllProducts(String category ,
			String brand,String colors,
			String sizes,Integer minPrice,
			Integer maxPrice,Integer minDiscount,
			String sort,String stock,
			Integer pageNumber
			);
	public List<Product> getProductsBySellerId(Long sellerId);
}
