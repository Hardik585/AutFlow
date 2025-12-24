package com.authflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProfileRequestDTO {

	@NotBlank(message= "name should be present")
	private String name;
	@Email(message="enter valid email address")
	@NotNull(message="email can't be empty")
	private String email;
	@NotNull
	@Min(value=6, message="password must be atleast 6 characters")
	private String password;
	
}
