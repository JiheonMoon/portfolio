package com.example.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.chat.dto.ChatMessageDTO;
import com.example.chat.entity.ChatMessage;
import com.example.chat.repo.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatMessageRepository repository;

    public ChatMessage saveMessage(ChatMessageDTO messageDTO) {
        ChatMessage message = ChatMessage.builder()
            .sender(messageDTO.getSender())
            .content(messageDTO.getContent())
            .timestamp(messageDTO.getTimestamp())
            .build();
        return repository.save(message);
    }

    public List<ChatMessage> getAllMessages() {
        return repository.findAll();
    }
}

