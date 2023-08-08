package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.User;

// CRUD 함수를 JpaRepository가 들고 있음
// @Repository라는 어노테이션이 없어도 IOC 안에 담깁니다.
// JpaRepository를 상속했기 때문에..
public interface UserRepository extends JpaRepository<User, Integer>{
	//findby 규칙 -> username 문법
	// select * from user where username = #{username}?
	public User findByUsername(String username); //Jpa Query Method
	
}
