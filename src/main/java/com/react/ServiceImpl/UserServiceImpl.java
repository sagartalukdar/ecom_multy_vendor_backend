package com.react.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Config.JwtProvider;
import com.react.Model.User;
import com.react.Repository.UserRepository;
import com.react.Service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtProvider jwtProvider;

	@Override
	public User findUserByJwt(String jwt) throws Exception {
		String email=jwtProvider.getEmailFromJwt(jwt);
		if(email!=null) {
           return findUserByEmail(email);
		}
		throw new Exception("user not found");
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user=userRepository.findByEmail(email);
		if(user==null) throw new Exception("user not found with email :"+email);
		return user;
	}

	@Override
	public User findUserById(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
