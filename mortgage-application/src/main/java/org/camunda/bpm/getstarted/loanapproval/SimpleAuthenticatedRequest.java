package org.camunda.bpm.getstarted.loanapproval;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

class SimpleAuthenticatedRequest extends HttpServletRequestWrapper {

    private final SimplePrincipal simplePrincipal;

    public SimpleAuthenticatedRequest(HttpServletRequest request, SimplePrincipal simplePrincipal) {
        super(request);
        this.simplePrincipal = simplePrincipal;
    }

    @Override
    public Principal getUserPrincipal() {
        return simplePrincipal;
    }
}
