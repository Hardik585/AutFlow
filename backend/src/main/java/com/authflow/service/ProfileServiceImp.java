package com.authflow.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authflow.dto.ProfileRequestDTO;
import com.authflow.dto.ProfileResponseDTO;
import com.authflow.entity.UserEntity;
import com.authflow.exception.EmailAlreadyExist;
import com.authflow.mapper.UserMapper;
import com.authflow.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImp implements ProfileService {

	private final UserRepo repo;
	private final PasswordEncoder pswdEncoder;

	
	@Override
	public ProfileResponseDTO getProfile(String email) {
		UserEntity existingUser = repo.findByEmail(email)
				                      .orElseThrow(()-> new UsernameNotFoundException("user does not exist"+ email));
		return UserMapper.toResponseDTO(existingUser);
	}

	@Override
	public ProfileResponseDTO createProfile(ProfileRequestDTO reqDTO) {
		if (repo.existsByEmail(reqDTO.getEmail())) {
			throw new EmailAlreadyExist("Emial is Already Exist or in used ");
		}
		String encodedPswd = pswdEncoder.encode(reqDTO.getPassword());
		UserEntity entity = UserMapper.toEntity(reqDTO ,encodedPswd );
		UserEntity savedUser = repo.save(entity);
		ProfileResponseDTO responseDTO = UserMapper.toResponseDTO(savedUser);
		return responseDTO;
	}

}
