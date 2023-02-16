package com.lancesoft.omg.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dto.AdminRegistrationDto;
import com.lancesoft.omg.dto.ChangePasswordDto;
import com.lancesoft.omg.entity.AdminRegistrationEntity;
import com.lancesoft.omg.entity.RegistrationEntity;
import com.lancesoft.omg.entity.SmsEntity;
import com.lancesoft.omg.service.AdminRegistrationServiceImpl;
import com.lancesoft.omg.service.SmsService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class AdminRegistrationController {

	private AdminRegistrationServiceImpl adminRegistrationServiceImp;

	public AdminRegistrationController(AdminRegistrationServiceImpl adminRegistrationServiceImp) {
		super();
		this.adminRegistrationServiceImp = adminRegistrationServiceImp;
	}

	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private SmsService service;
	private final String TOPIC_DESTINATION = "/lesson/sms";

	@PostMapping("/admin/sendOtp")
	public ResponseEntity<Boolean> smsSubmit(@RequestBody SmsEntity sms) {
		try {

			service.send(sms);

		} catch (Exception e) {
			return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		webSocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": SMS has been sent !: " + sms.getPhoneNumber());

		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	private String getTimeStamp() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
	}

	@PostMapping("/admin/register")
	public ResponseEntity<RegistrationEntity> createUser(
			@RequestBody @Valid AdminRegistrationDto adminregistrationdto) {

		return new ResponseEntity(adminRegistrationServiceImp.saveUser(adminregistrationdto), HttpStatus.CREATED);
	}

	@GetMapping("/admin/myProfile")
	public ResponseEntity<AdminRegistrationEntity> getMyProfile(HttpServletRequest httpServletRequest) {
		String userName = adminRegistrationServiceImp.getMyToken(httpServletRequest);
		return new ResponseEntity(adminRegistrationServiceImp.getAdmin(userName), HttpStatus.OK);
	}

	@PutMapping("/admin/update")
	public ResponseEntity<RegistrationEntity> updateUser(@RequestBody @Valid AdminRegistrationEntity adminregistrationEntity) {

		return new ResponseEntity(adminRegistrationServiceImp.updateUser(adminregistrationEntity), HttpStatus.CREATED);
	}

	@PostMapping("/admin/changePassword")
	public String changePassword(@RequestBody ChangePasswordDto changePasswordDto,Authentication authentication,HttpServletRequest httpServletRequest) 
	{
		String userName = adminRegistrationServiceImp.getMyToken(httpServletRequest);
		//String userName = authentication.getName();
		System.out.println(userName+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		AdminRegistrationEntity adminRegistrationEntity=adminRegistrationServiceImp.getAdmin(userName);
		boolean passwordChanged=adminRegistrationServiceImp.changePassword(adminRegistrationEntity, changePasswordDto);
		if(passwordChanged)
		{
			return "Password has been changed";
		}
		return "Error in changing password";
	}

}
