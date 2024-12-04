package com.react.Service;

import java.util.List;

import com.react.Model.Product;
import com.react.Model.Review;
import com.react.Model.User;
import com.react.Request.createReviewRequest;

public interface ReviewService {

	public Review createReview(createReviewRequest req,User user,Product product);
	public List<Review> getReviewByProductId(Long productId);
	public Review updateReview(Long reviewId,String reviewText,double rating,Long userId) throws Exception;
	public void deleteReview(Long reviewId,Long userId) throws Exception;
	public Review getReviewById(Long id) throws Exception;
}
