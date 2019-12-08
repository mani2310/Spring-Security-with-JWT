package com.springjwt;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springjwt.models.AuthenticationRequest;
import com.springjwt.models.AuthenticationResponse;
import com.springjwt.services.MyUserDetailsService;
import com.springjwt.util.SecurityUtil;

@RestController
public class HelloUser {

	@Autowired
	private AuthenticationManager authenticatioManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private SecurityUtil util;
	
	@RequestMapping("/hello")
	public String sayHello() {
		return "Hello User!";
	}
	
	@RequestMapping(value = "/authenticate",method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception
	{
		try {
			authenticatioManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()));
		
		}
		catch(BadCredentialsException e)
		{
			throw new Exception("Incorrect details!!");
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		final String jwt = util.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
		
		}

}
