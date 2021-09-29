package org.camunda.bpm.getstarted.loanapproval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// https://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html#websocket-server
// We need to authenticate StandardWebSocketSession in AbstractStandardUpgradeStrategy#upgrade, or in AbstractHandshakeHandler#doHandshake
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp").setAllowedOrigins("*");
    }

    @Configuration
    @ConfigurationProperties(prefix = "custom.stomp.broker")
    public static class StompConfig {
        private String virtualHost;
        private String clientLogin;
        private String clientPassword;
        private String systemLogin;
        private String systemPassword;
        private String host;
        private int port;

        public StompConfig() { }

        public String getVirtualHost() {
            return virtualHost;
        }

        public void setVirtualHost(String virtualHost) {
            this.virtualHost = virtualHost;
        }

        public String getClientLogin() {
            return clientLogin;
        }

        public void setClientLogin(String clientLogin) {
            this.clientLogin = clientLogin;
        }

        public String getClientPassword() {
            return clientPassword;
        }

        public void setClientPassword(String clientPassword) {
            this.clientPassword = clientPassword;
        }

        public String getSystemLogin() {
            return systemLogin;
        }

        public void setSystemLogin(String systemLogin) {
            this.systemLogin = systemLogin;
        }

        public String getSystemPassword() {
            return systemPassword;
        }

        public void setSystemPassword(String systemPassword) {
            this.systemPassword = systemPassword;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    @Autowired
    private StompConfig stompConfig;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config
                // allowed prefixes here http://www.rabbitmq.com/stomp.html#d
                .enableStompBrokerRelay("/queue/", "/topic/", "/exchange/", "/amq/queue/", "/temp-queue/")
                .setVirtualHost(stompConfig.getVirtualHost()) // vhost in RabbitMQ
                .setClientLogin(stompConfig.getClientLogin())
                .setClientPasscode(stompConfig.getClientPassword())
                .setSystemLogin(stompConfig.getSystemLogin())
                .setSystemPasscode(stompConfig.getSystemPassword())
                .setRelayHost(stompConfig.getHost())
                .setRelayPort(stompConfig.getPort())
        ;

        // use the /app prefix for others
        config.setApplicationDestinationPrefixes("/app");
    }

}

