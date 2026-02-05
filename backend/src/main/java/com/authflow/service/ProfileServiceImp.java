package com.authflow.service;

import java.util.concurrent.ThreadLocalRandom;

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
	private final EmailService emailService;

	@Override
	public void verifyOtp(String email, String otp) {
		UserEntity existEntity = repo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("user does not exist by this email : " + email));
		if (existEntity.getVerifyOtp() == null || !existEntity.getVerifyOtp().equals(otp)) {
			throw new RuntimeException("Invalid OTP");
		}
		if(existEntity.getVerifyOtpExpireAt() < System.currentTimeMillis()) {
			throw new RuntimeException("Verify Otp Time is expired");
		}
		
		existEntity.setIsAccountVerified(true);
		existEntity.setVerifyOtp(null);
		existEntity.setVerifyOtpExpireAt(0L);
		repo.save(existEntity);
	}

	@Override
	public void sendOpt(String email) {
		UserEntity existEntity = repo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("user does not exist by this email : " + email));
		if (existEntity.getIsAccountVerified() != null && existEntity.getIsAccountVerified()) {
			return;
		}

		// Generate 6 digit Otp
		String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
		// calculate Expire Time(around 24 hours (in milisecound) is given to expire the
		// otp)
		long expireTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);

		// update the profile
		existEntity.setVerifyOtp(otp);
		existEntity.setVerifyOtpExpireAt(expireTime);

		// save to DB
		repo.save(existEntity);

		// send Verify OTP to Email
		try {
			emailService.sendOtpEmail(existEntity.getEmail(), otp);
		} catch (Exception e) {
			throw new RuntimeException("not able to send the reset otp to email ");
		}

	}

	@Override
	public void resetPassword(String email, String otp, String newPassowrd) {
		UserEntity existUser = repo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("user does not exist by this email : " + email));
		if (existUser.getResetOtp() == null || !existUser.getResetOtp().equals(otp)) {
			throw new RuntimeException("Otp is not valid");
		}
		if (existUser.getResetOtpExpireAt() < System.currentTimeMillis()) {
			throw new RuntimeException(" Reset Otp is expired ");
		}

		existUser.setPassword(pswdEncoder.encode(newPassowrd));
		existUser.setResetOtp(null);
		existUser.setResetOtpExpireAt(0L);

		repo.save(existUser);

	}

	@Override
	public void sendResetOtp(String email) {
		UserEntity existEntity = repo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("user does not exist " + email));
		// Generate 6 digit Otp
		String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
		// calculate Expire Time(around 15 min(in milisecound) is given to expire the
		// otp)
		long expireTime = System.currentTimeMillis() + (15 * 60 * 1000);
		// update the profile
		existEntity.setResetOtp(otp);
		existEntity.setResetOtpExpireAt(expireTime);
		// save the update profile into DB
		repo.save(existEntity);
		// send reset otp to the email
		try {
			emailService.sendResetOtp(existEntity.getEmail(), otp);
		} catch (Exception e) {
			throw new RuntimeException("not able to send the reset otp to email ");
		}

	}

	@Override
	public ProfileResponseDTO getProfile(String email) {
		UserEntity existingUser = repo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("user does not exist" + email));
		return UserMapper.toResponseDTO(existingUser);
	}

	@Override
	public ProfileResponseDTO createProfile(ProfileRequestDTO reqDTO) {
		if (repo.existsByEmail(reqDTO.getEmail())) {
			throw new EmailAlreadyExist("Emial is Already Exist or in used ");
		}
		String encodedPswd = pswdEncoder.encode(reqDTO.getPassword());
		UserEntity entity = UserMapper.toEntity(reqDTO, encodedPswd);
		UserEntity savedUser = repo.save(entity);
		ProfileResponseDTO responseDTO = UserMapper.toResponseDTO(savedUser);
		return responseDTO;
	}

}
