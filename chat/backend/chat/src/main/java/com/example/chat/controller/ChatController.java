package com.example.chat.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chat.dto.ChatMessageDTO;
import com.example.chat.entity.ChatMessage;
import com.example.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService service;

	@MessageMapping("/chat")
	@SendTo("/topic/messages")
	public ResponseEntity<?> sendMessage(ChatMessageDTO messageDTO) {
		try {
			ChatMessageDTO dto = new ChatMessageDTO(service.saveMessage(messageDTO));
			return ResponseEntity.ok().body(dto);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Message Saving Error: " + e.getMessage());
		}
	}

	@GetMapping("/messages")
	public ResponseEntity<?> getAllMessages() {
		try {
			List<ChatMessage> entities = service.getAllMessages();
			List<ChatMessageDTO> dtos = entities.stream().map(ChatMessageDTO::new).collect(Collectors.toList());
			return ResponseEntity.ok().body(dtos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Fetching Messages Error: " + e.getMessage());
		}
	}
}
