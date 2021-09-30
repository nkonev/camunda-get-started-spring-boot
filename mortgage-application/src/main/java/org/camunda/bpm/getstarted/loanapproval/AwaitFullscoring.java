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
public class AwaitFullscoring implements JavaDelegate {
    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private MortgageApplicationRepository mortgageApplicationRepository;

    private Logger logger = LoggerFactory.getLogger(AwaitFullscoring.class);
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        UUID appId = (UUID) execution.getVariable(PROCESS_VARIABLE_APP_ID);
        final MortgageApplication mortgageApplication = mortgageApplicationRepository.findById(appId).orElseThrow();
        mortgageApplication.setStatus(MortgageApplicationStatus.AWAITING_MANUAL_FULLSCORING);
        final MortgageApplication saved = mortgageApplicationRepository.save(mortgageApplication);
        webSocketService.sendStatusUpdate(saved.getUserId(), saved.toDto());
    }
}
