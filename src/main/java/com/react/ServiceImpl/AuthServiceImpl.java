package com.react.ServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.react.Config.JwtProvider;
import com.react.Model.Cart;
import com.react.Model.Seller;
import com.react.Model.User;
import com.react.Model.VerificationCode;
import com.react.Repository.CartRepository;
import com.react.Repository.SellerRepository;
import com.react.Repository.UserRepository;
import com.react.Repository.VerificationCodeRepository;
import com.react.Request.LoginRequest;
import com.react.Request.SignUpRequest;
import com.react.Response.AuthResponse;
import com.react.Service.AuthService;
import com.react.Util.OtpUtil;
import com.react.domain.USER_ROLE;

@Service
public class AuthServiceImpl implements AuthService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private VerificationCodeRepository verificationCodeRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private CustomUserServiceImpl customUserServiceImpl;
	@Autowired
	private SellerRepository sellerRepository;
	
	@Override
	public void sentLoginOtp(String email,USER_ROLE role) throws Exception {
		String SIGNING_PREFIX="signin_";
		if(email.startsWith(SIGNING_PREFIX)) {
			email=email.substring(SIGNING_PREFIX.length());
			
			if(role.equals(USER_ROLE.ROLE_CUSTOMER)) {
				User user=userRepository.findByEmail(email);
				if(user==null) {
					throw new Exception("user not found with email --"+email);
				}
			}else {
				Seller seller=sellerRepository.findByEmail(email);
				if(seller==null) {
					throw new Exception("seller not found with email --"+email);
				}
			}		
		}
		
		VerificationCode isExist=verificationCodeRepository.findByEmail(email);
		if(isExist!=null) {
			verificationCodeRepository.delete(isExist);
		}
		String opt=OtpUtil.generateOtp();
		VerificationCode verificationCode=new VerificationCode();
		verificationCode.setOtp(opt);
		verificationCode.setEmail(email);
		verificationCodeRepository.save(verificationCode);
		
		String subject="Ecommerce Login/SignUp OTP";
		String text="your login/signup otp is -"+opt;
		emailService.sendVerificationOtpEmail(email, opt, subject, text);
		
	}

	@Override
	public String createUser(SignUpRequest req) throws Exception {
		
		VerificationCode verificationCode=verificationCodeRepository.findByEmail(req.getEmail());
		if(verificationCode==null || !verificationCode.getOtp().equals(req.getOtp())) {
			throw new Exception("wrong otp ..");
		}
		
		User user=userRepository.findByEmail(req.getEmail());
		
		if(user==null) {
			User createdUser=new User();
			createdUser.setEmail(req.getEmail());
			createdUser.setFullName(req.getFullName());
			createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
			createdUser.setMobile("1234567890");
			createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
			
			user=userRepository.save(createdUser);
			
			Cart cart=new Cart();
			cart.setUser(user);
			cartRepository.save(cart);
		}
		List<GrantedAuthority>authorities=new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));
		Authentication authentication=new UsernamePasswordAuthenticationToken(req.getEmail(),null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return jwtProvider.generateToken(authentication);		
		
	}

	@Override
	public AuthResponse signin(LoginRequest req) {
		String email=req.getEmail();
		String otp=req.getOtp();
		
		Authentication authentication=authenticate(email,otp);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt=jwtProvider.generateToken(authentication);
		AuthResponse authResponse=new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("login successfull");
		
		Collection<? extends GrantedAuthority> auths=authentication.getAuthorities();
		String roleName=auths.isEmpty()?null:auths.iterator().next().getAuthority();
		
		authResponse.setRole(USER_ROLE.valueOf(roleName));		
		return authResponse;
	}
	
	private Authentication authenticate(String email,String otp) {
		UserDetails userDetails=customUserServiceImpl.loadUserByUsername(email);
		
		String SELLER_PREFIX="seller_";
		if(email.startsWith(SELLER_PREFIX)) {
			email=email.substring(SELLER_PREFIX.length());
		}
		
		if(userDetails==null) {
			throw new BadCredentialsException("invalid username or password");
		}
		VerificationCode verificationCode=verificationCodeRepository.findByEmail(email);
		if(verificationCode==null || !verificationCode.getOtp().equals(otp)) {
			throw new BadCredentialsException("wrong otp");
		}
		return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
	}

	
	
}
