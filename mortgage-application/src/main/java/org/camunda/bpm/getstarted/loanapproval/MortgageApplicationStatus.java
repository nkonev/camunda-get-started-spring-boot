package org.camunda.bpm.getstarted.loanapproval;

public enum MortgageApplicationStatus {
    NEW,
    PRESCORING_FAILED,
    FULLSCORING_FAILED,
    AWAITING_MANUAL_FULLSCORING,
    USER_CANCELED,
    COMPLETED
}
