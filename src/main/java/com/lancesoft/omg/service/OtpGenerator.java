package com.lancesoft.omg.service;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class OtpGenerator {

	@Autowired
	private HttpSession session;
	int Otp;
	String phoneNumber;
	
	private static Logger logger=Logger.getLogger(OtpGenerator.class);
	
	public Integer generateOTP(String phoneNumber)
	{
		logger.info("Generate Otp method start");
		this.phoneNumber=phoneNumber;
	
		int min = 100000;
		int max = 999999;
		
		int Otp = (int) (Math.random() * (max - min + 1) + min);
		this.Otp=Otp;
		logger.info("Generate Otp end..");
		return Otp;
	}
	public void setOtp()
	{
		logger.info("setOtp Otp executed..");
	
		session.setAttribute("otpGntd", Otp);
		session.setAttribute("phoneNumber", phoneNumber);
		
		
	}
}
