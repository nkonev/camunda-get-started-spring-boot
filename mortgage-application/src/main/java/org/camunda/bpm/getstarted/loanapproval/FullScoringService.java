package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.camunda.bpm.getstarted.loanapproval.WebappExampleProcessApplication.MORTGAGE_GROUP;

@Component
public class FullScoringService {

    @Autowired
    private IdentityService identityService;

    private static final Logger logger = LoggerFactory.getLogger(FullScoringService.class);

    public String findManagerForTask() {
        List<String> potentialUsers = identityService.createUserQuery().memberOfGroup(MORTGAGE_GROUP).list().stream().map(User::getId).collect(Collectors.toList());
        logger.info("Список потенциальных исполнителей {} для полного скоринга", potentialUsers);
        int randomElementIndex = ThreadLocalRandom.current().nextInt(potentialUsers.size());
        final String login = potentialUsers.get(randomElementIndex);
        logger.info("Выбран пользователь {} для полного скоринга", login);
        return login;
    }
}
