package com.react.Service;

import com.react.Request.LoginRequest;
import com.react.Request.SignUpRequest;
import com.react.Response.AuthResponse;
import com.react.domain.USER_ROLE;

public interface AuthService {

	public void sentLoginOtp(String email,USER_ROLE role) throws Exception;
	
	public String createUser(SignUpRequest req) throws Exception;
	
	public AuthResponse signin(LoginRequest req);
}
