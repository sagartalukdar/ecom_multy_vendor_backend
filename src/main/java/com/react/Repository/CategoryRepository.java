package com.react.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.Model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

	public Category findByCategoryId(String id);
}
