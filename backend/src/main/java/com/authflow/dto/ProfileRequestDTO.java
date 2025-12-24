package com.authflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileRequestDTO {

	private String name;
	private String email;
	private String password;
	
}
