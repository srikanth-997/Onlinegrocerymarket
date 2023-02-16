package com.lancesoft.omg.security;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import com.lancesoft.omg.jwt.JwtFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService securityUserDetailsService;
	@Autowired
	private JwtFilter jwtFilter;

	private static Logger logger = Logger.getLogger(MySecurityConfig.class);

	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		logger.info("Inside authencationManagerBuilder");
		auth.userDetailsService(securityUserDetailsService).passwordEncoder(getPassWordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors();
		http.headers().addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Headers", "Authorization"));

		http.csrf().disable();
		logger.info("Inside HttpSecurity");
		http.authorizeRequests().antMatchers("/api/login","/api/user/sendOtp","/api/user/register","/api/admin/sendOtp","/api/admin/register","/api/admin/getAllProducts").permitAll().anyRequest().authenticated().and()
				.exceptionHandling().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		;       
		

	}

	@Bean
	public PasswordEncoder getPassWordEncoder() {
		logger.info("Inside Password encoder..");
		return new BCryptPasswordEncoder();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManager();
	}
}
