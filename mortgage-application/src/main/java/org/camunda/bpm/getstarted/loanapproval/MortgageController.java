package org.camunda.bpm.getstarted.loanapproval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/mortgage-application")
public class MortgageController {

    private static final String USER_ID_HEADER = "x-kratos-authenticated-identity-id";

    private static final Logger LOGGER = LoggerFactory.getLogger(MortgageController.class);

    @Autowired
    private MortgageApplicationRepository mortgageApplicationRepository;

    @PostMapping
    public MortgageAppDto create(@RequestHeader(USER_ID_HEADER)String userId, @RequestBody MortgageAppDto createDto) {
        return mortgageApplicationRepository.save(createDto.toEntityForCreate(UUID.fromString(userId))).toDto();
    }

    @PatchMapping
    public MortgageAppDto update(@RequestHeader(USER_ID_HEADER)String userId, @RequestBody MortgageAppDto createDto) {
        return mortgageApplicationRepository.save(createDto.toEntityForUpdate(UUID.fromString(userId))).toDto();
    }

    @GetMapping
    public MortgageAppDto get(@RequestHeader(USER_ID_HEADER)String userId) {
        return mortgageApplicationRepository.findLastActual(UUID.fromString(userId)).map(
                MortgageApplication::toDto).orElse(null);
    }
}

