package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class UserRejectedOnFullScoring implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(UserRejectedOnFullScoring.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        UUID appId = UUID.fromString(execution.getBusinessKey());
        logger.info("Заявка {} отклонена-удалена пользователем на полном скоринге", appId);
    }
}
