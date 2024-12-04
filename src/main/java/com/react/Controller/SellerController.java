package com.react.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.react.Exception.SellerException;
import com.react.Model.Seller;
import com.react.Model.SellerReport;
import com.react.Model.VerificationCode;
import com.react.Repository.VerificationCodeRepository;
import com.react.Request.LoginRequest;
import com.react.Response.ApiResponse;
import com.react.Response.AuthResponse;
import com.react.Service.AuthService;
import com.react.Service.SellerReportService;
import com.react.Service.SellerService;
import com.react.ServiceImpl.EmailService;
import com.react.Util.OtpUtil;
import com.react.domain.AccountStatus;

@RestController
public class SellerController {

	@Autowired
	private SellerService sellerService;
	@Autowired
	private AuthService authService;
	@Autowired
	private VerificationCodeRepository verificationCodeRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private SellerReportService sellerReportService;
	
	@PostMapping("/sellers/signin")
	public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception{
		String otp=req.getOtp();
		String email=req.getEmail();
		req.setEmail("seller_"+email);
		return new ResponseEntity<AuthResponse>(authService.signin(req),HttpStatus.OK);
	}
	@PutMapping("/sellers/verify/{otp}")
	public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception{
		VerificationCode verificationCode=verificationCodeRepository.findByOtp(otp);
		if(verificationCode==null) {
			throw new BadCredentialsException("wrong otp");
		}
		Seller seller=sellerService.verifyEmail(verificationCode.getEmail());
		return new ResponseEntity<Seller>(seller,HttpStatus.OK);
	}
	@PostMapping("/sellers/create")
	public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception{
		Seller savedSeller=sellerService.createSeller(seller);
		String otp=OtpUtil.generateOtp();
		VerificationCode verificationCode=new VerificationCode();
		verificationCode.setOtp(otp);
		verificationCode.setEmail(seller.getEmail());
		verificationCodeRepository.save(verificationCode);
		
		String subject="Ecommerce email verification code";
		String text="Welcome to ecommerce verify your account using this link :";
		String url="http://localhost:3000/verify-seller/";
		emailService.sendVerificationOtpEmail(seller.getEmail(),verificationCode.getOtp(), subject, text+url);
		return new ResponseEntity<Seller>(savedSeller,HttpStatus.CREATED);
	}
	@GetMapping("/api/sellers/{id}")
	public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception{
		return new ResponseEntity<Seller>( sellerService.getSellerById(id),HttpStatus.ACCEPTED);
	}
	@GetMapping("/api/sellers/profile")
	public ResponseEntity<Seller> getSellerProfile(@RequestHeader("Authorization")String jwt ) throws Exception{
		return new ResponseEntity<Seller>( sellerService.getSellerProfile(jwt),HttpStatus.ACCEPTED);
	}
	@GetMapping("/api/sellers/all")
	public ResponseEntity<List<Seller>> getAllSeller(@RequestParam(required = false)AccountStatus status){
		return new ResponseEntity<List<Seller>>(sellerService.getAllSellers(status),HttpStatus.OK);
	}
	@PutMapping("/api/sellers/update")
	public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization")String jwt,@RequestBody Seller seller) throws Exception {
		Seller reqSeller=sellerService.getSellerProfile(jwt);
		return new ResponseEntity<Seller>(sellerService.updateSeller(reqSeller.getId(), seller),HttpStatus.OK);
	}
	@DeleteMapping("/api/sellers/delete/{id}")
	public ResponseEntity<ApiResponse> deleteSeller(@PathVariable Long id) throws Exception{
		sellerService.deleteSeller(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("seller deleted with id :"+id),HttpStatus.OK);
	}
	@GetMapping("/report")
	public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization")String jwt) throws SellerException{
		Seller seller=sellerService.getSellerProfile(jwt);
		return new ResponseEntity<SellerReport>(sellerReportService.getSellerReport(seller),HttpStatus.OK);
	}
	
	
	
	
}
