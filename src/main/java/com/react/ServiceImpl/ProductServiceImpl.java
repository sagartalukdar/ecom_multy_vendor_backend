package com.react.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.react.Exception.ProductException;
import com.react.Model.Category;
import com.react.Model.Product;
import com.react.Model.Seller;
import com.react.Repository.CategoryRepository;
import com.react.Repository.ProductRepository;
import com.react.Request.ProductRequest;
import com.react.Service.ProductService;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public Product createProduct(ProductRequest req, Seller seller) {
		Category category1=categoryRepository.findByCategoryId(req.getCategory1());
		if(category1==null) {
			Category category=new Category();
			category.setCategoryId(req.getCategory1());
			category.setLevel(1);
			category1=categoryRepository.save(category);
		}
		Category category2=categoryRepository.findByCategoryId(req.getCategory2());
		if(category2==null) {
			Category category=new Category();
			category.setCategoryId(req.getCategory2());
			category.setLevel(2);
			category.setParentCategory(category1);
			category2=categoryRepository.save(category);
		}
		Category category3=categoryRepository.findByCategoryId(req.getCategory3());
		if(category3==null) {
			Category category=new Category();
			category.setCategoryId(req.getCategory3());
			category.setLevel(3);
			category.setParentCategory(category2);
			category3=categoryRepository.save(category);
		}
		
		int discountPercentage=calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());
		
		Product product=new Product();
		product.setSeller(seller);
		product.setCategory(category3);
		product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setSellingPrice(req.getSellingPrice());
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setSizes(req.getSizes());
        product.setDiscountedPercent(discountPercentage);
        
        return productRepository.save(product);
	}
	
	public int calculateDiscountPercentage(int mrpPrice,int sellingPrice) {
		if(mrpPrice<=0) {
		   return 0;
		}
		double discount=mrpPrice-sellingPrice;
		double discountPercentage=(discount/mrpPrice)*100;
		return (int) discountPercentage;
	}

	@Override
	public void deleteProduct(Long productId) {
		productRepository.deleteById(productId);		
	}

	@Override
	public Product updateProduct(Long productId, Product product) {
	     product.setId(productId);
	     return productRepository.save(product);
	}

	@Override
	public Product findProductById(Long productId) throws ProductException {
	   return productRepository.findById(productId).orElseThrow(()->new ProductException("product not found with id :"+productId));
	}

	@Override
	public List<Product> searchProduct(String query) {
		return productRepository.searchProduct(query);
	}

	@Override
	public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
		
		Specification<Product> spec =(root,query,criteriaBuilder)->{
			  List<Predicate> predicates=new ArrayList<>();
			  if(category!=null) {
				  Join<Product,Category> productCategoryJoin=root.join("category");
				  predicates.add(criteriaBuilder.equal(productCategoryJoin.get("categoryId"), category));
			  }
			  if(colors!=null && !colors.isEmpty()) {
				  predicates.add(criteriaBuilder.equal(root.get("color"),colors));
			  }
			  if(sizes!=null && !sizes.isEmpty()) {
				  predicates.add(criteriaBuilder.equal(root.get("size"),sizes));
			  }
			  if(minPrice!=null) {
				  predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"),minPrice));
			  }
			  if(maxPrice!=null) {
				  predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"),maxPrice));
			  }
			  if(minDiscount!=null) {
				  predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountedPercent"),minDiscount));
			  }
			  if(stock!=null) {
				  predicates.add(criteriaBuilder.equal(root.get("stock"),stock));
			  }
			  return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		  };
		  Pageable pageable;
		  if(sort!=null && !sort.isEmpty()) {
			  switch (sort) {
			case "price_low": {
				pageable=PageRequest.of(pageNumber!=null?pageNumber:0, 10,
						Sort.by("sellingPrice").ascending());
				break;
			}
			case "price_high": {
				pageable=PageRequest.of(pageNumber!=null?pageNumber:0, 10,
						Sort.by("sellingPrice").descending());
				break;
			}
			default:
				pageable=PageRequest.of(pageNumber!=null?pageNumber:0, 10,
						Sort.unsorted());
				break;
			}
		  }else {
				pageable=PageRequest.of(pageNumber!=null?pageNumber:0, 10,
						Sort.unsorted());
		  }
		  return productRepository.findAll(spec, pageable);
	}

	@Override
	public List<Product> getProductsBySellerId(Long sellerId) {
		return productRepository.findBySellerId(sellerId);
	}

	
}
