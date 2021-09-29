package org.camunda.bpm.getstarted.loanapproval;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

import static org.camunda.bpm.getstarted.loanapproval.CamundaConstants.MORTGAGE_PROCESS;

@EnableScheduling
@EnableJdbcAuditing
@EnableJdbcRepositories
@SpringBootApplication
@EnableProcessApplication
public class WebappExampleProcessApplication implements InitializingBean {

  public static final String MORTGAGE_GROUP = "mortgage";


  @Autowired
  private ManagementService managementService;

  @Autowired
  private IdentityService identityService;

  @Autowired
  private AuthorizationService authorizationService;

  private static final Logger logger = LoggerFactory.getLogger(WebappExampleProcessApplication.class);

  public static void main(String... args) {
    SpringApplication.run(WebappExampleProcessApplication.class, args);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    managementService.toggleTelemetry(false);
    List.of(
            "underwriter",
            "manager",
            "employee"
    ).forEach(login -> {
      ensureUser(MORTGAGE_GROUP, login);
    });

    List.of("broker", "investor").forEach(login -> {
      ensureUser("broker", login);
    });

  }

  private void ensureUser(String groupName, String userId) {
    Group group = identityService.createGroupQuery().groupId(groupName).singleResult();
    if (group == null) {
       group = identityService.newGroup(groupName);
       identityService.saveGroup(group);

      // GUI https://docs.camunda.org/manual/7.15/webapps/admin/authorization-management/
      // https://docs.camunda.org/manual/7.15/user-guide/process-engine/authorization-service/#resources
      {
        final Authorization newAuthorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        newAuthorization.setGroupId(groupName);
        newAuthorization.setResource(Resources.APPLICATION);
        newAuthorization.setResourceId("tasklist");
        newAuthorization.addPermission(Permissions.ALL);
        final Authorization authorization = authorizationService.saveAuthorization(newAuthorization);
        logger.info("Authorization {} successfully saved", authorization);
      }

      if (MORTGAGE_GROUP.equals(groupName))
      {
        final Authorization newAuthorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        newAuthorization.setGroupId(groupName);
        newAuthorization.setResource(Resources.TASK);
        newAuthorization.setResourceId(MORTGAGE_PROCESS);
        newAuthorization.addPermission(Permissions.ALL);
        final Authorization authorization = authorizationService.saveAuthorization(newAuthorization);
        logger.info("Authorization {} successfully saved", authorization);
      }

      {
        final Authorization newAuthorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        newAuthorization.setGroupId(groupName);
        newAuthorization.setResource(Resources.FILTER);
        newAuthorization.setResourceId("*");
        newAuthorization.addPermission(Permissions.ALL);
        final Authorization authorization = authorizationService.saveAuthorization(newAuthorization);
        logger.info("Authorization {} successfully saved", authorization);
      }

    }

    if (identityService.createUserQuery().userId(userId).singleResult() == null) {
      final User user = identityService.newUser(userId);
      user.setPassword(userId);
      user.setEmail(userId+"@example.com");
      final String capitalized = StringUtils.capitalize(userId);
      user.setFirstName(capitalized);
      user.setLastName(capitalized);
      identityService.saveUser(user);
      logger.info("User {} successfully saved", userId);

      identityService.createMembership(userId, groupName);



    } else {
      logger.info("User {} already present", userId);
    }

  }
}