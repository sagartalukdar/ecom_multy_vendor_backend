package com.react.Model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.react.domain.USER_ROLE;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDetails {

	private String businessName;
	private String businessEmail;
	private String businessMobile;
	private String businessAddress;
	private String logo;
	private String banner;
	
}
