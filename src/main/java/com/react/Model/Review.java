package com.react.Model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data 
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String reviewText;
	
	@Column(nullable = false)
	private double rating;
	
	@ElementCollection
	private List<String> productImages;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(nullable = false)
	private Product product;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private User user;

	@Column(nullable = false)
	private LocalDateTime createdAt=LocalDateTime.now();
	
}
