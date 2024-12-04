package com.react.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.react.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,JpaSpecificationExecutor<Product> {

	public List<Product> findBySellerId(Long id);
	
	@Query("Select p From Product p Where :query is null or (p.title Like %:query% or p.category.name Like %:query%)")
	public List<Product> searchProduct(String query);
}
