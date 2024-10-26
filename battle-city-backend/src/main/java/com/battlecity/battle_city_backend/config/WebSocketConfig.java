package com.battlecity.battle_city_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefijo de las rutas donde se envían mensajes a los clientes
        config.enableSimpleBroker("/topic");
        // Prefijo para los mensajes enviados desde el cliente al backend
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
                     //       /game
        //registry.addEndpoint("/battle-city-websocket").setAllowedOrigins("*").withSockJS(); // Endpoint WebSocket con SockJS
        registry.addEndpoint("/battle-city-websocket")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();
    }
}
