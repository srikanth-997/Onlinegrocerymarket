package com.lancesoft.omg.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.dao.AdminRegistrationDao;
import com.lancesoft.omg.dto.AdminRegistrationDto;
import com.lancesoft.omg.dto.Authorities;
import com.lancesoft.omg.dto.ChangePasswordDto;
import com.lancesoft.omg.entity.AdminRegistrationEntity;
import com.lancesoft.omg.entity.ChangePasswordEntity;
import com.lancesoft.omg.exception.InvalidEnteredPassword;
import com.lancesoft.omg.exception.InvalidSession;
import com.lancesoft.omg.exception.NotValidOTPException;
import com.lancesoft.omg.exception.UserAlreadyExist;
import com.lancesoft.omg.jwt.JwtUtil;

@Service
public class AdminRegistrationServiceImpl implements AdminRegistrationService {

	@Autowired
	private AdminRegistrationDao adminRegistrationDao;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SmsService service;
	@Autowired
	private HttpSession httpSession;
	@Autowired
	OtpGenerator otpGenerator;
	@Autowired
	JwtUtil jwtUtil;
	private static Logger logger= Logger.getLogger(AdminRegistrationServiceImpl.class);


	public AdminRegistrationEntity saveUser(AdminRegistrationDto adminRegistrationDto) {

		logger.info("Admin save user method started...");
		ModelMapper modelMapper = new ModelMapper();
		AdminRegistrationEntity adminRegistrationEntity = new AdminRegistrationEntity();
		otpGenerator.setOtp();
		if (!validateOTP(adminRegistrationDto.getPhoneNumber(), adminRegistrationDto.getUserOtp())) {
			throw new NotValidOTPException("Please enter a valid OTP");
		} else {

			if (adminRegistrationDto == null)

				throw new RuntimeException("null found in registration plss check");

			else
				modelMapper.map(adminRegistrationDto, adminRegistrationEntity);

			if (adminRegistrationDao.existsByUserName(adminRegistrationEntity.getUserName())
					|| adminRegistrationDao.existsByPhoneNumber(adminRegistrationEntity.getPhoneNumber())) {
				throw new UserAlreadyExist("UserName is already exists");
			}

			Authorities authorities = new Authorities();
			authorities.setRole("ADMIN");
			List<Authorities> authority = new ArrayList<Authorities>();
			authority.add(authorities);
			adminRegistrationEntity.setAuthorities(authority);

			if (!(adminRegistrationEntity.getPassword().equals(adminRegistrationEntity.getConfirmPassword()))) {
				throw new InvalidEnteredPassword("Password and confirm password must be match");
			}
			adminRegistrationEntity.setPassword(passwordEncoder.encode(adminRegistrationEntity.getPassword()));
			adminRegistrationEntity
					.setConfirmPassword(passwordEncoder.encode(adminRegistrationEntity.getConfirmPassword()));

			logger.info("Admin save user method end....");
			return adminRegistrationDao.save(adminRegistrationEntity);
		}
	}

	public boolean validateOTP(String phoneNum, Integer otpNumber) {
		// get OTP from cache
		Integer cacheOTP = (Integer) httpSession.getAttribute("otpGntd");
		String phoneNumInSession = (String) httpSession.getAttribute("phoneNumber");
		if (cacheOTP != null && cacheOTP.equals(otpNumber) && phoneNum.equals(phoneNumInSession)) {
			httpSession.invalidate();

			logger.info("Admin validate otp method end..");
			return true;
		}
		logger.info("Admin validate otp method end..");
		return false;
	}

	public AdminRegistrationEntity getAdmin(String userName) {

		return adminRegistrationDao.findByUserName(userName);

	}

	public AdminRegistrationEntity updateUser(AdminRegistrationEntity adminRegistrationEntity) {

		logger.info("Admin update user method started..");
		/*
		 * int userId=adminRegistrationDto.getAdminId(); AdminRegistrationEntity
		 * adminRegistrationEntity= adminRegistrationDao.findByAdminId(userId);
		 * ModelMapper modelMapper = new ModelMapper();
		 * 
		 * 
		 * if (adminRegistrationDto == null)
		 * 
		 * throw new RuntimeException("null found in registration plss check");
		 * 
		 * else modelMapper.map(adminRegistrationDto, adminRegistrationEntity);
		 */
		String password=adminRegistrationEntity.getPassword();
		String confirmPassword=adminRegistrationEntity.getConfirmPassword();
		adminRegistrationEntity.setPassword(password);
		adminRegistrationEntity.setConfirmPassword(confirmPassword);
		logger.info("Admin update user method end..");
		
		return adminRegistrationDao.save(adminRegistrationEntity);
	}

	public String getMyToken(HttpServletRequest httpServletRequest) {
		logger.info("Start of get my token method..");
		String authorizationHeader = httpServletRequest.getHeader("Authorization");

		String token = null;
		String userName = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
			if (!jwtUtil.isTokenExpired(token)) {
				userName = jwtUtil.extractUsername(token);
			} else
				throw new InvalidSession("Invalid Session please login ");
		} else
			throw new InvalidSession("Invalid Session please login ");
		return userName;
	}

	public boolean changePassword(AdminRegistrationEntity adminRegistrationEntity,
			ChangePasswordDto changePasswordDto) {
        logger.info("Admin change password method start..");
		ModelMapper mapper = new ModelMapper();
		ChangePasswordEntity changePasswordEntity = new ChangePasswordEntity();
		if (!(changePasswordDto == null)) {
			mapper.map(changePasswordDto, changePasswordEntity);
		} else
			throw new RuntimeException("null found in registration plss check");

		String oldPassword = adminRegistrationEntity.getPassword();

		String enterOldPassword = passwordEncoder.encode(changePasswordEntity.getOldPassword());
		boolean isMatches = passwordEncoder.matches(changePasswordEntity.getOldPassword(), oldPassword);

		if (isMatches) {
			if (changePasswordEntity.getNewPassword().equals(changePasswordEntity.getConfirmPassword())) {
				String enterNewPassword = passwordEncoder.encode(changePasswordEntity.getNewPassword());
				String enterConfirmPassword = passwordEncoder.encode(changePasswordEntity.getConfirmPassword());

				adminRegistrationEntity.setPassword(enterNewPassword);
				adminRegistrationEntity.setConfirmPassword(enterNewPassword);
				adminRegistrationDao.save(adminRegistrationEntity);
				logger.info("Admin change password end..");
				return true;
			} else
				throw new InvalidEnteredPassword("New and confirm password must be match");
		} else
			throw new InvalidEnteredPassword("Old password must be match");
	}

}
