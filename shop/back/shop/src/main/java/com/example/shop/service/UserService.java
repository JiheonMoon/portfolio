package com.example.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shop.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	
}
