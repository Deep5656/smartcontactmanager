package com.example.demo.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.smart.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
