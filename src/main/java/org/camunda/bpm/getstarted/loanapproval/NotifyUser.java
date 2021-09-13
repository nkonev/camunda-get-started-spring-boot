package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotifyUser implements JavaDelegate {
    private static Logger logger = LoggerFactory.getLogger(NotifyUser.class);
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String username = (String) execution.getVariable("username");
        boolean decision = (boolean) execution.getVariable("decision");

        if (decision) {
            logger.info("Пользователь {} создан и мы отсылаем письмо о создании", username);
        } else {
            logger.info("Пользователю {} отказано в регистрации и мы отсылаем письмо о отказе", username);
        }
    }
}
