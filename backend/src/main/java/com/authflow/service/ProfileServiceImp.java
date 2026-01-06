package com.authflow.service;

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

//	public ProfileServiceImp(UserRepo repo) {
//		this.repo = repo;
//	}	

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
