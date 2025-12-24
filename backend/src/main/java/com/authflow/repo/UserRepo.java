package com.authflow.repo;

import org.springframework.data.jpa.repository.JpaRepository;


import com.authflow.entity.UserEntity;
import java.util.Optional;


public interface UserRepo extends JpaRepository<UserEntity, Integer>{

	Boolean existsByEmail(String email);
	Optional<UserEntity> findByEmail(String email);
	
}
