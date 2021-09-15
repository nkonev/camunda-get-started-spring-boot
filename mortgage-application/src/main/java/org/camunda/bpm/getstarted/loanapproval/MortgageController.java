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

