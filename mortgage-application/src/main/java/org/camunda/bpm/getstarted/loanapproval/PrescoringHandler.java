package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

import static org.camunda.bpm.getstarted.loanapproval.CamundaConstants.PRESCORING_SUCCESS;
import static org.camunda.bpm.getstarted.loanapproval.CamundaConstants.PROCESS_VARIABLE_APP_ID;

@Component("prescoringService")
public class PrescoringHandler implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(PrescoringHandler.class);

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private MortgageApplicationRepository mortgageApplicationRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info("Начало прескоринга");
        UUID appId = (UUID) execution.getVariable(PROCESS_VARIABLE_APP_ID);
        final MortgageApplication mortgageApplication = mortgageApplicationRepository.findById(appId).orElseThrow();
        if (mortgageApplication.getPrice().compareTo(new BigDecimal("400000")) > 0) {
            logger.info("Заявка {} успешно прошла прескоринг", mortgageApplication.getId());
            execution.setVariable(PRESCORING_SUCCESS, true);
        } else {
            logger.info("Заявка {} подозрительна и не прошла прескоринг", mortgageApplication.getId());
            execution.setVariable(PRESCORING_SUCCESS, false);
        }

        webSocketService.sendStatusUpdate(mortgageApplication.getUserId(), mortgageApplication.toDto());
    }
}
