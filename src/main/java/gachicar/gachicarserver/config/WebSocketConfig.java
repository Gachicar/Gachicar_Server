package gachicar.gachicarserver.config;

import gachicar.MyWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;    // JWT 토큰 검증을 위해 생성

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 엔드포인트 설정
        registry.addEndpoint("/ws/carSharing")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // 발행자가 '/topic' 경로로 메시지를 주면 구독자들에게 전달
        registry.setApplicationDestinationPrefixes("/app"); // 발행자가 '/app' 경로로 메시지를 주면 가공을 해서 구독자들에게 전달
        registry.setUserDestinationPrefix("/user");
    }

    /* 메시지가 Connection을 맺을 때 header에서 user 정보를 뽑고 session에 저장해주기 */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

    @Bean
    public MyWebSocketHandler myWebSocketHandler() {
        return new MyWebSocketHandler();
    }

    @Bean
    public UndertowServletWebServerFactory undertowServletWebServerFactory() {
        // Undertow에 대한 추가적인 설정이 필요하다면 여기서 설정
        return new UndertowServletWebServerFactory();
    }

}