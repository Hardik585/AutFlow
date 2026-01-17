package com.authflow.service;

import com.authflow.dto.ProfileRequestDTO;
import com.authflow.dto.ProfileResponseDTO;

public interface ProfileService {

	public ProfileResponseDTO createProfile(ProfileRequestDTO reqDTO);
	
	public ProfileResponseDTO getProfile(String email);
}
