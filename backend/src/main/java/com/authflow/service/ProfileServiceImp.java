package com.authflow.service;

import org.springframework.stereotype.Service;

import com.authflow.dto.ProfileRequestDTO;
import com.authflow.dto.ProfileResponseDTO;
import com.authflow.entity.UserEntity;
import com.authflow.exception.EmailAlreadyExist;
import com.authflow.mapper.UserMapper;
import com.authflow.repo.UserRepo;

@Service
public class ProfileServiceImp implements ProfileService {

	private final UserRepo repo;

	public ProfileServiceImp(UserRepo repo) {
		this.repo = repo;
	}

	@Override
	public ProfileResponseDTO createProfile(ProfileRequestDTO reqDTO) {
		if (repo.existsByEmail(reqDTO.getEmail())) {
			throw new EmailAlreadyExist("Emial is Already Exist or in used ");
		}
		UserEntity entity = UserMapper.toEntity(reqDTO);
		UserEntity savedUser = repo.save(entity);
		ProfileResponseDTO responseDTO = UserMapper.toResponseDTO(savedUser);
		return responseDTO;
	}

}
