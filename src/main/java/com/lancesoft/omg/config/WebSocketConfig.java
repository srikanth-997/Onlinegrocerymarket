package com.lancesoft.omg.config;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private static Logger logger=Logger.getLogger(WebSocketConfig.class);
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config)
	{
		logger.info("Web socket configure");
		config.enableSimpleBroker("/lesson");
		config.setApplicationDestinationPrefixes("/app");
		
	}
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry)
	{
		logger.info("Stomp end point config method");
		registry.addEndpoint("/gs-guide-websocket").withSockJS();
	}
	
	
	
}
