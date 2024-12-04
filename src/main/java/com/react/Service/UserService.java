package com.react.Service;

import com.react.Model.User;

public interface UserService {

	public User findUserByJwt(String jwt) throws Exception;
	
	public User findUserByEmail(String email) throws Exception;
	
	public User findUserById(Long userId);
}
