package com.lancesoft.omg.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dto.RegistrationDto;
import com.lancesoft.omg.entity.RegistrationEntity;
import com.lancesoft.omg.entity.SmsEntity;
import com.lancesoft.omg.service.RegistrationServiceImp;
import com.lancesoft.omg.service.SmsService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class RegistrationController {

	private RegistrationServiceImp registrationServiceImp;

	public RegistrationController(RegistrationServiceImp registrationServiceImp) {
		super();
		this.registrationServiceImp = registrationServiceImp;
	}

	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private SmsService service;
	private final String TOPIC_DESTINATION = "/lesson/sms";

	@PostMapping("/user/sendOtp")
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

	@PostMapping("/user/register")
	public ResponseEntity<RegistrationEntity> createUser(@RequestBody @Valid RegistrationDto registrationdto) {

		return new ResponseEntity<>(registrationServiceImp.saveUser(registrationdto), HttpStatus.CREATED);
	}

}
