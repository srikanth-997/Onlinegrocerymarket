package com.lancesoft.omg.dto;

import java.util.Set;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Setter
@Getter
@Table(name = "registration_entity")
public class RegistrationDto {

	private Integer user_Id;

	@NotBlank(message = "First name should not be null")
	@Size(min = 2, max = 15, message = "First name  should contain 2-15 characters")
	private String firstName;

	@NotBlank(message = "Last name should not be null")
	@Size(min = 2, max = 15, message = "Last name  should contain 2-15 characters")
	private String lastName;

	@NotBlank(message = "userName cannot be empty")
	@Column(name = "userName", nullable = false, unique = true)
	private String userName;

	@NotBlank(message = "Password can't be blank")
	private String password;

	@NotBlank(message = "Password can't be blank")
	private String confirmPassword;

	private String gender;

	@Email(message = "Invalid email address")
	private String email;

	
	//@Pattern(regexp = "[89][0-9]{9}", message = "Invalid mobile number entered")
	private String phoneNumber;

	private String dob;

	// @NotEmpty(message = "Please enter your OTP")
	private Integer userOtp;

}
