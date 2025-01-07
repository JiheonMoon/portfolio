package com.example.chat.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chat.entity.ChatMessage;
import com.example.chat.repo.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatMessageRepository repository;
	
	@MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        repository.save(message); // 메시지 저장
        return message;
    }

    @GetMapping("/messages")
    public List<ChatMessage> getAllMessages() {
        return repository.findAll(); // 모든 메시지 조회
    }
}
