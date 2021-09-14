package org.camunda.bpm.getstarted.loanapproval;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

class CreateUserDto {
    private String login;
    private String password;
    private MortgageApp app;

    public CreateUserDto() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MortgageApp getApp() {
        return app;
    }

    public void setApp(MortgageApp app) {
        this.app = app;
    }
}

class MortgageApp {
    private String property;

    public MortgageApp() {
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}

@RestController("/mortgage")
public class MortgageController {
    @PostMapping
    public void register(@RequestBody CreateUserDto createUserDto) {

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public MortgageApp get() {
        return new MortgageApp();
    }
}
