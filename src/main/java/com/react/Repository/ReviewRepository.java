package com.react.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.Model.Review;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

	public List<Review> findByProductId(Long id);
}
