package com.chat.momo.controller;

import com.chat.momo.model.Message;
import com.chat.momo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public Message sendMessage(@Payload Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageService.saveMessage(message);
        return message;
    }

    @GetMapping("/messages/{senderId}/{receiverId}")
    public List<Message> getMessages(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return messageService.getMessagesBetweenUsers(senderId, receiverId);
    }
}
