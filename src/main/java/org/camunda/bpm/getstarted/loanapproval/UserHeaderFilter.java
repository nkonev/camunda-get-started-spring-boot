package org.camunda.bpm.getstarted.loanapproval;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.stream.Collectors;

class HeaderAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        String user = request.getHeader("X-USER");
        if (user == null) {
            return null;
        }
        final var headers = Collections.list(request.getHeaders("X-ROLE"))
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(user, null, headers);
    }
}

@Component
public class UserHeaderFilter extends AuthenticationFilter {
    public UserHeaderFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager, new HeaderAuthenticationConverter());
    }
}
