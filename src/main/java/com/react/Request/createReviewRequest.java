package com.react.Request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class createReviewRequest {

	private String reviewText;
	private double rating;
	private List<String> productImages;
}
