package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;
import static org.camunda.bpm.getstarted.loanapproval.CamundaConstants.*;

@RestController
@RequestMapping("/api/mortgage-application")
public class MortgageController {

    private static final String USER_ID_HEADER = "x-kratos-authenticated-identity-id";

    private static final Logger LOGGER = LoggerFactory.getLogger(MortgageController.class);

    @Autowired
    private MortgageApplicationRepository mortgageApplicationRepository;

    @Autowired
    private ProcessEngine processEngine;

    @PostMapping
    public MortgageAppDto create(@RequestHeader(USER_ID_HEADER)String userId, @RequestBody MortgageAppDto createDto) {
        final MortgageAppDto returnDto = mortgageApplicationRepository.save(createDto.toEntityForCreate(UUID.fromString(userId))).toDto();

        final ProcessInstance mortgageProcessInstance = processEngine.getRuntimeService().startProcessInstanceByKey(MORTGAGE_PROCESS, Map.of(PROCESS_VARIABLE_APP_ID, returnDto.getId()));
        LOGGER.info("Started camunda process with processInstanceId={}, suspended={}", mortgageProcessInstance.getProcessInstanceId(), mortgageProcessInstance.isSuspended());
        return returnDto;
    }

    @DeleteMapping("/{appId}")
    public void delete(@RequestHeader(USER_ID_HEADER)String userId, @PathVariable("appId") String appId) {
        mortgageApplicationRepository.deleteByUserIdAndAppId(UUID.fromString(userId), UUID.fromString(appId));
        // TODO camunda
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

