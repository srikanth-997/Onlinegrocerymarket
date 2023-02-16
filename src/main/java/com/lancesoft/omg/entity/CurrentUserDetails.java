package com.lancesoft.omg.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.lancesoft.omg.dto.JwtToken;

import lombok.Data;

@Data
public class CurrentUserDetails {
	
	private String userName;
	private String role;
	private JwtToken token;

}
