package org.camunda.bpm.getstarted.loanapproval;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    public void sendStatusUpdate(UUID userId, MortgageAppDto mortgageAppDto) {
        // https://stackoverflow.com/questions/22367223/sending-message-to-specific-user-on-spring-websocket
        try {
            logger.info("Sending to user {} message {}", userId, objectMapper.writeValueAsString(mortgageAppDto));
        } catch (Exception ignore) {}
        template.convertAndSendToUser(userId.toString(), "/queue/mortgage.application.status.update", mortgageAppDto);
    }

}
