package org.camunda.bpm.getstarted.loanapproval;

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

}
