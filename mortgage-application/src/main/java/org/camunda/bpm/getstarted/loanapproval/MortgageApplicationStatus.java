package org.camunda.bpm.getstarted.loanapproval;

public enum MortgageApplicationStatus {
    NEW,
    PRESCORING_FAILED,
    FULLSCORING_FAILED,
    AWAITING_MANUAL_FULL_SCORING,
    USER_CANCELED,
    COMPLETED
}
