package com.react.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.Model.User;
import com.react.Model.VerificationCode;
import com.react.Request.LoginOtpRequest;
import com.react.Request.LoginRequest;
import com.react.Request.SignUpRequest;
import com.react.Response.ApiResponse;
import com.react.Response.AuthResponse;
import com.react.Service.AuthService;
import com.react.domain.USER_ROLE;


@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/sent/login-signup-otp")
	public ResponseEntity<ApiResponse> sentOtp(@RequestBody LoginOtpRequest req) throws Exception{
       authService.sentLoginOtp(req.getEmail(),req.getRole());
       ApiResponse res=new ApiResponse("otp sent successfully");
       return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignUpRequest req) throws Exception{
	  String jwt=authService.createUser(req);
	  AuthResponse authResponse=new AuthResponse(jwt,"register successfull",USER_ROLE.ROLE_CUSTOMER);
	  return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest req) throws Exception{
	  AuthResponse authResponse=authService.signin(req);
	  return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);
	}
}
