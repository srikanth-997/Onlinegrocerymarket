package com.lancesoft.omg.service;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.dao.RegistrationDao;
import com.lancesoft.omg.entity.RegistrationEntity;

@Service
public class OtpService {

	@Autowired
	private HttpSession httpSession;
	@Autowired
	private OtpGenerator otpGenerator;
	@Autowired
	private RegistrationDao registrationDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static Logger logger=Logger.getLogger(OtpService.class);
	public boolean validateOTP(String newPassword, Integer otpNumber) {
		
		logger.info("start of validate otp method in otp service ");
		//Set otp to session
		otpGenerator.setOtp();
		// get OTP from cache
		Integer cacheOTP = (Integer) httpSession.getAttribute("otpGntd");
		String phoneNumInSession = (String) httpSession.getAttribute("phoneNumber");
		httpSession.setMaxInactiveInterval(5*60);
		boolean isNumExist = registrationDao.existsByPhoneNumber(phoneNumInSession);

		if (cacheOTP != null && cacheOTP.equals(otpNumber) && isNumExist) 
		{
			httpSession.invalidate();
			RegistrationEntity registrationEntity = registrationDao.findByPhoneNumber(phoneNumInSession);
			registrationEntity.setPassword(passwordEncoder.encode(newPassword));
			registrationEntity.setConfirmPassword(passwordEncoder.encode(newPassword));
			registrationDao.save(registrationEntity);
			
			logger.info("End of validate otp method in otp service ");

			return true;
		}
		logger.info("End of validate otp method in otp service ");

		return false;
	}
	

	

}
