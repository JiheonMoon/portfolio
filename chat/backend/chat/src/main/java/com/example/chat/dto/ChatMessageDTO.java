package com.example.chat.dto;

import com.example.chat.entity.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
	private String sender;
	private String content;
	private String timestamp;

	public ChatMessageDTO(ChatMessage chatMessage) {
		this.sender = chatMessage.getSender();
		this.content = chatMessage.getContent();
		this.timestamp = chatMessage.getTimestamp();
	}
	
}
