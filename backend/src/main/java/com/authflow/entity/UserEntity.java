package com.authflow.entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name="auth_user")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(unique = true)
	private String userId;
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
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updateAt;
	
	
}
