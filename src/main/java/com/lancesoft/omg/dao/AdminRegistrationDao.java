package com.lancesoft.omg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.AdminRegistrationEntity;
import com.lancesoft.omg.entity.RegistrationEntity;

public interface AdminRegistrationDao extends JpaRepository<AdminRegistrationEntity, Integer> {
	AdminRegistrationEntity findByUserName(String useName);
	boolean existsByUserName(String userName);
	boolean existsByPhoneNumber(String userName);
	AdminRegistrationEntity findByPhoneNumber(String phoneNumber);
	
}
