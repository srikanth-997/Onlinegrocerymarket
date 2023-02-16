package com.lancesoft.omg.service;

import org.springframework.stereotype.Service;

import com.lancesoft.omg.dto.RegistrationDto;
import com.lancesoft.omg.entity.RegistrationEntity;
import com.lancesoft.omg.exception.UserAlreadyExist;
@Service
public interface RegistationService {
	
	RegistrationEntity saveUser(RegistrationDto registrationdto) throws UserAlreadyExist;
	
}
