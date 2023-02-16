package com.lancesoft.omg.entity;

import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lancesoft.omg.dto.Authorities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName="build")
@NoArgsConstructor
@Entity
@Table(name="registration_entity")
public class RegistrationEntity{
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer user_Id;
	private String firstName;
	private String lastName;

	private String userName;
	private String password;
	private String confirmPassword;
	private String gender;
	private String email;
	private String phoneNumber;
	private String  dob;

	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name="user_role", joinColumns = @JoinColumn(name="user_Id"), inverseJoinColumns = @JoinColumn(name="role_id"))
	private List<Authorities> authorities;  

	
	
}

	