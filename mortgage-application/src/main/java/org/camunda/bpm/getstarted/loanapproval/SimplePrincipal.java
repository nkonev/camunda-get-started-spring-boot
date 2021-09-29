package org.camunda.bpm.getstarted.loanapproval;

import javax.security.auth.Subject;
import java.security.Principal;

class SimplePrincipal implements Principal {

    private final String userId;

    public SimplePrincipal(String userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return userId;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
