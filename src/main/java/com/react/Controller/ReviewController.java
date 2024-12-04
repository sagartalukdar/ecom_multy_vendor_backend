package com.react.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.Model.Product;
import com.react.Model.Review;
import com.react.Model.User;
import com.react.Request.createReviewRequest;
import com.react.Response.ApiResponse;
import com.react.Service.ProductService;
import com.react.Service.ReviewService;
import com.react.Service.UserService;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private UserService userService;
	@Autowired
	private ProductService productService;
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Review>> getReviewByProductId(@PathVariable Long productId){
		return new ResponseEntity<List<Review>>(reviewService.getReviewByProductId(productId),HttpStatus.OK);
	}
	
	@PostMapping("/product/{productId}")
	public ResponseEntity<Review> writeReview(@RequestBody createReviewRequest req,@PathVariable Long productId,@RequestHeader("Authorization")String jwt) throws Exception{
		User user=userService.findUserByJwt(jwt);
		Product product=productService.findProductById(productId);
		
		Review review=reviewService.createReview(req, user, product);
		return new ResponseEntity<Review>(review,HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{reviewId}")
	public ResponseEntity<Review> updateReview(@RequestBody createReviewRequest req,@PathVariable Long reviewId,@RequestHeader("Authorization")String jwt) throws Exception{
		User user=userService.findUserByJwt(jwt);
	    return new ResponseEntity<Review>(reviewService.updateReview(reviewId, req.getReviewText(), req.getRating(), user.getId()),HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{reviewId}")
	public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId,@RequestHeader("Authorization")String jwt) throws Exception{
		User user=userService.findUserByJwt(jwt);
		reviewService.deleteReview(reviewId, user.getId());
        ApiResponse res=new ApiResponse();
        res.setMessage("review deleted successfully");
   
        return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
	}
	
}
