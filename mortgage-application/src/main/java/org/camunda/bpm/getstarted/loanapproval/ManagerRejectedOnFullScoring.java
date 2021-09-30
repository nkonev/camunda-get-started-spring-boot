package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class ManagerRejectedOnFullScoring implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(ManagerRejectedOnFullScoring.class);

    @Autowired
    private MortgageApplicationRepository mortgageApplicationRepository;

    @Autowired
    private WebSocketService webSocketService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        UUID appId = UUID.fromString(execution.getBusinessKey());
        mortgageApplicationRepository.findById(appId).ifPresent(mortgageApplication -> {
            mortgageApplication.setStatus(MortgageApplicationStatus.FULLSCORING_FAILED);
            final MortgageApplication saved = mortgageApplicationRepository.save(mortgageApplication);
            webSocketService.sendStatusUpdate(saved.getUserId(), saved.toDto());
        });
        logger.info("Заявка {} отклонена менеджером на полном скоринге", appId);
    }
}
