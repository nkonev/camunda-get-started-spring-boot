package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MortgageService {
    @Autowired
    private MortgageApplicationRepository mortgageApplicationRepository;

    // https://docs.camunda.org/manual/7.15/user-guide/process-engine/expression-language/#availability-of-variables-and-functions-inside-expression-language
    public String getPrescoredAddress(DelegateExecution execution) {
        final String businessKey = execution.getBusinessKey();
        final MortgageApplication mortgageApplication = mortgageApplicationRepository.findById(UUID.fromString(businessKey)).orElseThrow();
        return mortgageApplication.getProperty();
    }

    public Long getPrescoredPrice(DelegateExecution execution) {
        final String businessKey = execution.getBusinessKey();
        final MortgageApplication mortgageApplication = mortgageApplicationRepository.findById(UUID.fromString(businessKey)).orElseThrow();
        return mortgageApplication.getPrice().longValue();
    }

}
