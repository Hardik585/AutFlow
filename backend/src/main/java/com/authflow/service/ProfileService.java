package com.authflow.service;

import com.authflow.dto.ProfileRequestDTO;
import com.authflow.dto.ProfileResponseDTO;

public interface ProfileService {
	
	public void verifyOtp(String email , String otp);
	
	public void sendOpt(String email);
	
	public void resetPassword(String email , String otp , String newPassword);

	public void sendResetOtp(String email);
	
	public ProfileResponseDTO createProfile(ProfileRequestDTO reqDTO);
	
	public ProfileResponseDTO getProfile(String email);
	
}
