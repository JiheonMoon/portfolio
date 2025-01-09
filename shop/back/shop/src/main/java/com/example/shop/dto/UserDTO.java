package com.example.shop.dto;

import com.example.shop.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private String username;
    private String password;
    private String email;
    private String nickname;
    
    public UserDTO(UserEntity entity) {
    	this.username = entity.getUsername();
    	this.password = entity.getPassword();
    	this.email = entity.getPassword();
    	this.nickname = entity.getNickname();
    	
    }
}
