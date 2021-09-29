package org.camunda.bpm.getstarted.loanapproval;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

import static org.camunda.bpm.getstarted.loanapproval.MortgageController.USER_ID_HEADER;

@Component
public class UserIdFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(UserIdFilter.class);

    @Autowired
    private ObjectProvider<ScheduledSender> maybeScheduledSender;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        final String maybeHeaderValue = httpRequest.getHeader(USER_ID_HEADER);
        if (!StringUtils.isEmpty(maybeHeaderValue)){
            final UUID userId = UUID.fromString(maybeHeaderValue);
            maybeScheduledSender.ifAvailable(scheduledSender -> scheduledSender.setLastUserUuid(maybeHeaderValue));
            logger.info("Authenticating http with userId={}", userId);
            filterChain.doFilter(new SimpleAuthenticatedRequest(httpRequest, new SimplePrincipal(maybeHeaderValue)), servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}

