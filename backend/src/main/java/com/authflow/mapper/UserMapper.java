package com.authflow.mapper;

import java.util.UUID;

import com.authflow.dto.ProfileRequestDTO;
import com.authflow.dto.ProfileResponseDTO;
import com.authflow.entity.UserEntity;

public class UserMapper {

	public static UserEntity toEntity(ProfileRequestDTO request) {
		return UserEntity.builder()
				         .userId(UUID.randomUUID().toString())
				         .name(request.getName())
				         .email(request.getEmail())
				         .password(request.getPassword())
				         .verifyOtp(null)
				         .verifyOtpExpireAt(0L)
				         .isAccountVerified(false)
				         .resetOtp(null)
				         .resetOtpExpireAt(0L)
				         .build();
	}

	public static ProfileResponseDTO toResponseDTO(UserEntity entity) {
		return ProfileResponseDTO.builder()
				.email(entity.getEmail())
				.isAccountVerified(entity.getIsAccountVerified())
				.name(entity.getName())
				.userId(entity.getUserId())
				.build();
	}

}
