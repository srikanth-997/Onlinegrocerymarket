package com.lancesoft.omg.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.dao.RegistrationDao;
import com.lancesoft.omg.dto.Authorities;
import com.lancesoft.omg.entity.RegistrationEntity;

@Service("securityUserDetailsService")
public class SecurityUserDetailsService implements UserDetailsService 
{

	@Autowired
	private RegistrationDao registrationDao;
	private static Logger logger=Logger.getLogger(SecurityUserDetailsService.class);
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
	
		logger.info("LoadUserByUserName method start..");
		
		RegistrationEntity registrationEntity =  registrationDao.findByUserName(userName);
		System.out.println("Inside load username >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		 List<Authorities> ud=registrationEntity.getAuthorities();
		 System.out.println(ud);
	
		SecurityUserDetails securityUserDetails=null;
		if(registrationEntity!=null)
		{
			securityUserDetails=new SecurityUserDetails();
			securityUserDetails.setRegistrationEntity(registrationEntity);
			
	
		}
		else
		{
			throw new UsernameNotFoundException("User not exist with name :"+ userName);
		}
		logger.info("LoadUserByUserName method end..");
		return securityUserDetails;
	}
	

}
