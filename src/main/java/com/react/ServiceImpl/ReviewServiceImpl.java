package com.react.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Model.Product;
import com.react.Model.Review;
import com.react.Model.User;
import com.react.Repository.ProductRepository;
import com.react.Repository.ReviewRepository;
import com.react.Request.createReviewRequest;
import com.react.Service.ProductService;
import com.react.Service.ReviewService;
import com.react.Service.UserService;

@Service
public class ReviewServiceImpl implements ReviewService{

	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public Review createReview(createReviewRequest req, User user, Product product) {
		Review review=new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReviewText(req.getReviewText());
		review.setRating(req.getRating());
		review.setProductImages(req.getProductImages());
		review=reviewRepository.save(review);
		product.getReviews().add(review);
		productRepository.save(product);
		return review;
	}

	@Override
	public List<Review> getReviewByProductId(Long productId) {
		return reviewRepository.findByProductId(productId);
	}

	@Override
	public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {
		Review review=getReviewById(reviewId);
		if(!review.getUser().getId().equals(userId)) {
			throw new Exception("you can't update another user's "
					+ "review because your userid is not mathced"
					+ "with user's id who has create this review");
		}else {
			review.setReviewText(reviewText);
			review.setRating(rating);
			return reviewRepository.save(review);
		}
	}

	@Override
	public void deleteReview(Long reviewId, Long userId) throws Exception {
		Review review=getReviewById(reviewId);
		if(!review.getUser().getId().equals(userId)) {
			throw new Exception("you can't delete another user's "
					+ "review because your userid is not mathced"
					+ "with user's id who has create this review");
		}
		reviewRepository.delete(review);
	}

	@Override
	public Review getReviewById(Long id) throws Exception {
		return reviewRepository.findById(id).orElseThrow(()->
		new Exception("review not found with id :"+id)
				);
	}

	
}
