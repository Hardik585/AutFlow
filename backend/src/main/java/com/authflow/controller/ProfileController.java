package com.authflow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.authflow.dto.ProfileRequestDTO;
import com.authflow.dto.ProfileResponseDTO;
import com.authflow.service.ProfileServiceImp;

import jakarta.validation.Valid;

@RestController
public class ProfileController {

	private final ProfileServiceImp service;
	
	public ProfileController(ProfileServiceImp service) {
		this.service = service;
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<ProfileResponseDTO> register(@Valid @RequestBody ProfileRequestDTO request){
		ProfileResponseDTO profile = service.createProfile(request);
//		TODO send welcome to email 
		return new ResponseEntity<>(profile , HttpStatus.CREATED);
	}
	
	@GetMapping("/profile")
	public ProfileResponseDTO getProfile(@CurrentSecurityContext(expression ="authentication?.name") String email) {
		return service.getProfile(email);
	}
	
	/*
	 * Above method can be write like this also easy for debug and log maintainable
	 * 
	 * @GetMapping("/getprofile")
	 *  public ProfileResponseDTO getProfileTwo(Authentication auth) { 
	 *   return service.getProfile(auth.getName()); 
	 *  }
	 */
}
