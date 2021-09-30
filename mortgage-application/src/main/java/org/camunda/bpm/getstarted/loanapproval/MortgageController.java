package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
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

    public static final String USER_ID_HEADER = "x-kratos-authenticated-identity-id";

    private static final Logger logger = LoggerFactory.getLogger(MortgageController.class);

    @Autowired
    private MortgageApplicationRepository mortgageApplicationRepository;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @PostMapping
    public MortgageAppDto create(@RequestHeader(USER_ID_HEADER)String userId, @RequestBody MortgageAppDto createDto) {
        final MortgageAppDto returnDto = mortgageApplicationRepository.save(createDto.toEntityForCreate(UUID.fromString(userId))).toDto();

        return returnDto;
    }

    @DeleteMapping("/{appId}")
    public void delete(@RequestHeader(USER_ID_HEADER)String userId, @PathVariable("appId") String appId) {
        mortgageApplicationRepository.deleteByUserIdAndAppId(UUID.fromString(userId), UUID.fromString(appId));

        if (runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(appId, MORTGAGE_PROCESS).count() != 0) {
            MessageCorrelationResult result = runtimeService.createMessageCorrelation(USER_REJECT_MESSAGE)
                    .processInstanceBusinessKey(appId)
                    .correlateWithResult();
        }
    }

    @PatchMapping
    public MortgageAppDto update(@RequestHeader(USER_ID_HEADER)String userId, @RequestBody MortgageAppDto createDto) {
        var userIdUuid = UUID.fromString(userId);
        checkAppBelongs(createDto.getId(), userIdUuid);
        return mortgageApplicationRepository.save(createDto.toEntityForUpdate(userIdUuid)).toDto();
    }

    @GetMapping
    public MortgageAppDto get(@RequestHeader(USER_ID_HEADER)String userId) {
        return mortgageApplicationRepository.findLastActual(UUID.fromString(userId)).map(
                MortgageApplication::toDto).orElse(null);
    }

    @PutMapping("/send/{appId}")
    public MortgageAppDto send(@RequestHeader(USER_ID_HEADER)String userId, @PathVariable("appId") String appId) {
        var userIdUuid = UUID.fromString(userId);
        var appIdUuid = UUID.fromString(appId);
        checkAppBelongs(appIdUuid, userIdUuid);

        final MortgageApplication mortgageApplication = mortgageApplicationRepository.findById(appIdUuid).orElseThrow();
        mortgageApplication.setSent(true);
        mortgageApplicationRepository.save(mortgageApplication);

        final ProcessInstance mortgageProcessInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(
                        MORTGAGE_PROCESS,
                        mortgageApplication.getId().toString(),
                        Map.of(PROCESS_VARIABLE_APP_ID, mortgageApplication.getId(), PRESCORING_SUCCESS, false)
                );
        logger.info("Started camunda process with processInstanceId={}, suspended={}", mortgageProcessInstance.getProcessInstanceId(), mortgageProcessInstance.isSuspended());
        final MortgageApplication actualMortgageApplication = mortgageApplicationRepository.findById(appIdUuid).orElseThrow();
        return actualMortgageApplication.toDto();
    }

    private void checkAppBelongs(UUID appId, UUID userIdUuid) {
        mortgageApplicationRepository.findByIdAndUserId(userIdUuid, appId).orElseThrow(() -> new RuntimeException("Mortgage application is not found"));
    }

}

