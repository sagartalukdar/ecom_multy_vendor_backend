package com.react.Request;

import com.react.domain.USER_ROLE;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginOtpRequest {
	
	private String email;
	private USER_ROLE role;
}
