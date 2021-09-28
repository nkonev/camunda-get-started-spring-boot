package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@EnableJdbcAuditing
@EnableJdbcRepositories
@SpringBootApplication
@EnableProcessApplication
public class WebappExampleProcessApplication implements InitializingBean {

  @Autowired
  private ManagementService managementService;

  public static void main(String... args) {
    SpringApplication.run(WebappExampleProcessApplication.class, args);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    managementService.toggleTelemetry(false);
  }
}