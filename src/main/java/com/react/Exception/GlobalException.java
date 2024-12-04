package com.react.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(SellerException.class)
	public ResponseEntity<ErrorDetails> sellerExceptionHandler(SellerException se,WebRequest req){
		return new ResponseEntity<ErrorDetails>(new ErrorDetails(se.getMessage(),req.getDescription(false),LocalDateTime.now()),HttpStatus.OK);
	}
	
	@ExceptionHandler(ProductException.class)
	public ResponseEntity<ErrorDetails> productExceptionHandler(ProductException pe,WebRequest req){
		return new ResponseEntity<ErrorDetails>(new ErrorDetails(pe.getMessage(),req.getDescription(false),LocalDateTime.now()),HttpStatus.OK);
	}
	
	@ExceptionHandler(CartItemException.class)
	public ResponseEntity<ErrorDetails> cartItemExceptionHandler(CartItemException cie,WebRequest req){
		return new ResponseEntity<ErrorDetails>(new ErrorDetails(cie.getMessage(),req.getDescription(false),LocalDateTime.now()),HttpStatus.OK);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> allExceptionHandler(Exception e,WebRequest req){
		return new ResponseEntity<ErrorDetails>(new ErrorDetails(e.getMessage(),req.getDescription(false),LocalDateTime.now()),HttpStatus.OK);
	}
}
