package com.lancesoft.omg.controller;

import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dao.RegistrationDao;
import com.lancesoft.omg.dto.Authorities;
import com.lancesoft.omg.dto.JwtToken;
import com.lancesoft.omg.dto.LoginRequest;
import com.lancesoft.omg.entity.CurrentUserDetails;
import com.lancesoft.omg.entity.RegistrationEntity;
import com.lancesoft.omg.exception.CustomException;
import com.lancesoft.omg.jwt.JwtUtil;

@RestController()
@RequestMapping("/api")
@CrossOrigin("*")
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailsService securityUserDetailsService;

	@Autowired
	RegistrationDao registrationDao;

	@PostMapping("/login")
	public ResponseEntity<CurrentUserDetails> login(@RequestBody LoginRequest loginRequest,
			CurrentUserDetails currentUserDetails) throws Exception {

		try {

			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

		} catch (Exception e) {
			throw new CustomException("invalid username or password");
		}
		JwtToken jwtToken = new JwtToken();
		jwtToken.setJwtToken(jwtUtil.generateToken(loginRequest.getUserName()));

		RegistrationEntity registrationEntity = registrationDao.findByUserName(loginRequest.getUserName());
		currentUserDetails.setUserName(registrationEntity.getUserName());

	     Iterator iterator=registrationEntity.getAuthorities().iterator();
	    
	     
	    	 Authorities authorities=(Authorities) iterator.next();
	    	 currentUserDetails.setRole(authorities.getRole());
	     
	     

		// currentUserDetails.setAuthority();
		currentUserDetails.setToken(jwtToken);
		
		return new ResponseEntity<CurrentUserDetails>(currentUserDetails, HttpStatus.OK);

	}

	@GetMapping("/hello")
	public String getHelloworld() {
		return "Hello World";
	}

	@GetMapping("/bye")
	public String getBye() {
		return "Bye";
	}

}
