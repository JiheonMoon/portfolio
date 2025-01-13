package com.example.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shop.dto.UserDTO;
import com.example.shop.entity.UserEntity;
import com.example.shop.repo.UserRepository;
import com.example.shop.security.CookieUtil;
import com.example.shop.security.TokenProvider;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	public UserEntity signup(UserDTO dto) {
		if (repository.existsByUsername(dto.getUsername())) {
	        throw new RuntimeException("Username already exists");
	    }
	    if (repository.existsByEmail(dto.getEmail())) {
	        throw new RuntimeException("Email already registered");
	    }
	    if (repository.existsByNickname(dto.getEmail())) {
	        throw new RuntimeException("Nickname already registered");
	    }
		
		UserEntity user = UserEntity.builder().username(dto.getUsername()).email(dto.getEmail()).password(passwordEncoder.encode(dto.getPassword())).nickname(dto.getNickname()).build();
		return repository.save(user);
	}
	
	public UserEntity signin(UserDTO dto, HttpServletResponse response) {
		UserEntity user = repository.findByUsername(dto.getUsername())
				.orElseThrow(() -> new RuntimeException("Invalid username or password"));
		
		if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
			throw new RuntimeException("Invalid username or password");
		}
		
		String accessToken = tokenProvider.createAccessToken(Integer.toString(user.getUserId()));
		String refreshToken = tokenProvider.createRefreshToken(Integer.toString(user.getUserId()));
		
		user.setRefreshToken(refreshToken);
        
        
        ResponseCookie accessTokenCookie = CookieUtil.createCookie(
                "access_token", accessToken, 24 * 60 * 60, false, "Strict");
        ResponseCookie refreshTokenCookie = CookieUtil.createCookie(
                "refresh_token", refreshToken, 7 * 24 * 60 * 60, false, "Strict");

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        
        return repository.save(user);
	}
	
	public void logout(UserDTO dto, HttpServletResponse response) {
        UserEntity user = repository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Refresh Token 제거
        user.setRefreshToken(null);
        repository.save(user);
        
        ResponseCookie accessTokenCookie = CookieUtil.deleteCookie("access_token");
        ResponseCookie refreshTokenCookie = CookieUtil.deleteCookie("refresh_token");

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
