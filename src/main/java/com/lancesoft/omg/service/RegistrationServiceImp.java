package com.lancesoft.omg.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.dao.RegistrationDao;
import com.lancesoft.omg.dto.Authorities;
import com.lancesoft.omg.dto.RegistrationDto;
import com.lancesoft.omg.entity.RegistrationEntity;
import com.lancesoft.omg.exception.InvalidEnteredPassword;
import com.lancesoft.omg.exception.NotValidOTPException;
import com.lancesoft.omg.exception.UserAlreadyExist;

@Service
public class RegistrationServiceImp implements RegistationService {
	@Autowired
	private RegistrationDao registrationDao;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SmsService service;
	@Autowired
	private HttpSession httpSession;
	@Autowired
	OtpGenerator otpGenerator;
	
	private static Logger logger= Logger.getLogger(RegistrationServiceImp.class);

	public RegistrationEntity saveUser(RegistrationDto registrationdto) {

		
		ModelMapper modelMapper = new ModelMapper();
		RegistrationEntity registrationEntity = new RegistrationEntity();
		
		
		 otpGenerator.setOtp();
		 
		if (!validateOTP(registrationdto.getPhoneNumber(), registrationdto.getUserOtp())) {
			throw new NotValidOTPException("Invalid OTP or Phone number");
		} 
		else {
			if (registrationdto == null)

			    
				throw new RuntimeException("null found in registration plss check");

			else
				modelMapper.map(registrationdto, registrationEntity);
				
			if (registrationDao.existsByUserName(registrationEntity.getUserName())||registrationDao.existsByPhoneNumber(registrationEntity.getPhoneNumber())) {
				throw new UserAlreadyExist("UserName or Phone number is already exists");
			}

			Authorities authorities = new Authorities();
			authorities.setRole("USER");
			List<Authorities> authority = new ArrayList<Authorities>();
			authority.add(authorities);
			registrationEntity.setAuthorities(authority);

			
			if(!(registrationEntity.getPassword().equals(registrationEntity.getConfirmPassword())))
			{
				throw new InvalidEnteredPassword("Password and confirm password must be match");
			}
			registrationEntity.setPassword(passwordEncoder.encode(registrationEntity.getPassword()));
			registrationEntity.setConfirmPassword(passwordEncoder.encode(registrationEntity.getConfirmPassword()));

			logger.info("End of save user method !!");
			
			return registrationDao.save(registrationEntity);
		}

	}

	public boolean validateOTP(String phoneNum, Integer otpNumber) {
		logger.info("User validate Otp method start.. ");
		// get OTP from cache
		
		
		Integer cacheOTP = (Integer) httpSession.getAttribute("otpGntd");
		
		
		System.out.println(cacheOTP+">>>>>>>>>>>>>>>>>>>>>>");
		
		String phoneNumInSession = (String) httpSession.getAttribute("phoneNumber");
		
		if (cacheOTP != null && cacheOTP.equals(otpNumber) && phoneNum.equals(phoneNumInSession)) {
			httpSession.invalidate();

			logger.info("End of validate Otp method(Valid Otp) !!");
			return true;
		}
		logger.info("End of validate Otp method (Invalid Otp)!!");
		return false;
	}

}
