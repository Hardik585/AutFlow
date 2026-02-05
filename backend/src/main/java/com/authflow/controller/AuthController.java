package com.authflow.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.authflow.dto.AuthRequestDTO;
import com.authflow.dto.AuthResponseDTO;
import com.authflow.dto.ResetPasswordRequest;
import com.authflow.service.AppUserDetailsService;
import com.authflow.service.ProfileServiceImp;
import com.authflow.util.JWTUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final AppUserDetailsService appUserDetailsService;
	private final JWTUtil jwtUtil;
	private final ProfileServiceImp profileService;
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response){
		  ResponseCookie cookie = ResponseCookie.from("jwt" , "")
		  .httpOnly(true)
		  .secure(false) //make sure to make it true in the production
		  .path("/")
		  .maxAge(0)
		  .sameSite("Lax")  // safer for frontend-backend setups
		  .build();
		  
		  return ResponseEntity.ok()
				  .header(HttpHeaders.SET_COOKIE, cookie.toString())
				  .body("Logged out succefully");
	}

	@PostMapping("/verify-otp")
	public void verifyEmail(@RequestBody Map<String, Object> request,
			@CurrentSecurityContext(expression = "authentication?.name") String email) {
		if (request.get("otpCode").toString() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing details ");
		}
		try {
			profileService.verifyOtp(email, request.get("otpCode").toString());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@PostMapping("/send-otp")
	public void sendVerifyOtp(@CurrentSecurityContext(expression = "authentication?.name") String email) {
		try {
			profileService.sendOpt(email);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@PostMapping("/reset-password")
	public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
		try {
			profileService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@PostMapping("send-reset-otp")
	public void sendResetOTP(@RequestParam String email) {
		try {
			profileService.sendResetOtp(email);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}

	@GetMapping("/is-authenticated")
	public ResponseEntity<Boolean> isAuthenticated(
			@CurrentSecurityContext(expression = "authentication?.name") String email) {
		return ResponseEntity.ok(email != null);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequestDTO request) {
		try {
			authenticate(request.getEmail(), request.getPassword());
			// JWT token generation part
			final UserDetails userDetails = appUserDetailsService.loadUserByUsername(request.getEmail());
			final String token = jwtUtil.generateToken(userDetails);
			// Cookies work (this is how the test url getting jwt in the form of cookies )
			ResponseCookie cookie = ResponseCookie.from("jwt", token).httpOnly(true).path("/")
					.maxAge(Duration.ofDays(1)).sameSite("Strict").build();
			return ResponseEntity.ok().header("Set-cookie", cookie.toString())
					.body(new AuthResponseDTO(request.getEmail(), token));

		} catch (BadCredentialsException ex) {
			Map<String, Object> errors = new HashMap<>();
			errors.put("error", true);
			errors.put("message", "either email is wrong or password is wrong");
			return new ResponseEntity<Map<String, Object>>(errors, HttpStatus.BAD_REQUEST);
		} catch (DisabledException ex) {
			Map<String, Object> errors = new HashMap<>();
			errors.put("error", true);
			errors.put("message", "User Account is disabled");
			return new ResponseEntity<Map<String, Object>>(errors, HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			Map<String, Object> errors = new HashMap<>();
			errors.put("error", true);
			errors.put("message", "Authentication failed");
			return new ResponseEntity<Map<String, Object>>(errors, HttpStatus.UNAUTHORIZED);
		}
	}

	private void authenticate(String email, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

	}
}
