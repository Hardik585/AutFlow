package com.authflow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Auth_User")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	@Column(unique = true)
	private String email;
	private Long mobile_no;
	private String password;
	private String verifyOtp;
	private Boolean isAccountVerified;
	private Long verifyOtpExpireAt;
	private String resetOtp;
	private Long resetOtpExpireAt;
	
	
}
