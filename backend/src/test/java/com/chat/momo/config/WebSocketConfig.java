package com.chat.momo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Configure the message broker
        config.enableSimpleBroker("/topic", "/queue"); // Define the destinations for messages
        config.setApplicationDestinationPrefixes("/app"); // Define the prefix for client messages
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the STOMP endpoints
        registry.addEndpoint("/ws") // The WebSocket endpoint
                .setAllowedOrigins("http://localhost:3000") // Allow cross-origin requests
                .withSockJS(); // Enable SockJS fallback options
    }
    
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/ws").allowedOrigins("http://localhost:3000");
        }
    }
}
