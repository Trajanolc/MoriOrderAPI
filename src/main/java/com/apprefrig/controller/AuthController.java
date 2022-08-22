package com.apprefrig.controller;



import com.apprefrig.services.TokenServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
	
	@Autowired
	TokenServices tokenService;
	
	@GetMapping("/auth")
	public ResponseEntity<String> auth(@AuthenticationPrincipal UserDetails activeUser){
		
		
		String token = tokenService.createToken(activeUser.getUsername());
		
		return ResponseEntity.ok(token);
		
	}

}
