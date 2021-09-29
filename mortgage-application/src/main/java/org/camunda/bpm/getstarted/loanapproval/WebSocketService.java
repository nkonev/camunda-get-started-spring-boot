package org.camunda.bpm.getstarted.loanapproval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    public void sendStatusUpdate(UUID userId, MortgageAppDto mortgageAppDto) {
        // https://stackoverflow.com/questions/22367223/sending-message-to-specific-user-on-spring-websocket
        template.convertAndSendToUser(userId.toString(), "/queue/mortgage.application.status.update", mortgageAppDto);
    }

}
