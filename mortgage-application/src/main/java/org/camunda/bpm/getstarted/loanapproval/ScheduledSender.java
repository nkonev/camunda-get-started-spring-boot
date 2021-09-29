package org.camunda.bpm.getstarted.loanapproval;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@ConditionalOnProperty(value = "user.send.debug.messages", matchIfMissing = false)
@Component
public class ScheduledSender {

    @Autowired
    private WebSocketService webSocketService;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledSender.class);

    private final AtomicReference<String> lastUserUuid = new AtomicReference<>();

    @Scheduled(cron = "* * * * * *")
    public void sendToParticularCustomer() {
        logger.info("scheduled iteration");
        final String maybeUuid = lastUserUuid.get();
        if (!StringUtils.isEmpty(maybeUuid)) {
            logger.info("sending debug message to user {}", maybeUuid);
            webSocketService.sendStatusUpdate(UUID.fromString(maybeUuid), new MortgageAppDto());
        }
    }

    public void setLastUserUuid(String id) {
        lastUserUuid.set(id);
    }
}
