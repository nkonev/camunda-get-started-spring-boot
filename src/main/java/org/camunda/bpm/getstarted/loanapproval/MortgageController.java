package org.camunda.bpm.getstarted.loanapproval;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/mortgage-application")
public class MortgageController {

    private static final String USER_ID_HEADER = "X-USER-ID";

    @Autowired
    private MortgageApplicationRepository mortgageApplicationRepository;

    @PostMapping
    public MortgageAppDto create(@RequestHeader(USER_ID_HEADER)String userId, @RequestBody MortgageAppDto createDto) {
        return mortgageApplicationRepository.save(createDto.toEntityForCreate(UUID.fromString(userId))).toDto();
    }

    @GetMapping
    public MortgageAppDto get(@RequestHeader(USER_ID_HEADER)String userId) {
        return mortgageApplicationRepository.findLastActual(UUID.fromString(userId)).map(
                MortgageApplication::toDto).orElse(null);
    }
}

class MortgageAppDto {
    private UUID id;
    private UUID userId;
    private String property;
    private Instant createdDateTime;

    public MortgageAppDto() {
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @JsonIgnore
    public MortgageApplication toEntityForCreate(UUID userId) {
        var ret = new MortgageApplication();
        ret.setProperty(this.property);
        ret.setUserId(userId);
        ret.setCreatedDateTime(this.createdDateTime);
        return ret;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Instant getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Instant createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
