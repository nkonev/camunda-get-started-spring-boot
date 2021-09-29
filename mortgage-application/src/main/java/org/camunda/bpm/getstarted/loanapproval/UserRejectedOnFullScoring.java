package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.camunda.bpm.getstarted.loanapproval.CamundaConstants.PROCESS_VARIABLE_APP_ID;

@Component
public class UserRejectedOnFullScoring implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(UserRejectedOnFullScoring.class);

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private MortgageApplicationRepository mortgageApplicationRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        UUID appId = (UUID) execution.getVariable(PROCESS_VARIABLE_APP_ID);
        logger.info("Заявка {} отклонена пользователем на полном скоринге", appId);

        mortgageApplicationRepository.findById(appId).ifPresent(mortgageApplication -> {
            webSocketService.sendStatusUpdate(mortgageApplication.getUserId(), mortgageApplication.toDto());
        });

    }
}
