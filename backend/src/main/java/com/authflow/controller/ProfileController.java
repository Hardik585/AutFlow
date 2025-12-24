package com.authflow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authflow.dto.ProfileRequestDTO;
import com.authflow.dto.ProfileResponseDTO;
import com.authflow.service.ProfileServiceImp;

@RestController
@RequestMapping("/api/v1.0")
public class ProfileController {

	private final ProfileServiceImp service;
	
	public ProfileController(ProfileServiceImp service) {
		this.service = service;
	}
	
	@PostMapping("/register")
	public ResponseEntity<ProfileResponseDTO> register(@RequestBody ProfileRequestDTO request){
		ProfileResponseDTO profile = service.createProfile(request);
		return new ResponseEntity<>(profile , HttpStatus.CREATED);
	}
}
