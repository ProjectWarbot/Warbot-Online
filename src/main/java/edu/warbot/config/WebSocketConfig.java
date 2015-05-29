package edu.warbot.config;

import edu.warbot.Application;
import edu.warbot.editor.CodeEditorListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by beugnon on 05/04/15.
 */
@Configuration
@EnableWebSocketMessageBroker
@ComponentScan(basePackageClasses = Application.class)
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/warbot").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue/", "/game/", "/editor/");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Bean
    public CodeEditorListener codeEditorListener(SimpMessagingTemplate messagingTemplate) {
        CodeEditorListener codeEditorListener = new CodeEditorListener(messagingTemplate);
        codeEditorListener.setAccess("/editor/register");
        return codeEditorListener;
    }

}
