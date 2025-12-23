package com.authflow.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authflow.entity.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Integer>{

}
