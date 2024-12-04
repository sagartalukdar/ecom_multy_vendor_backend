package com.react.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.Model.HomeCategory;

@Repository
public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long>{

	
}
