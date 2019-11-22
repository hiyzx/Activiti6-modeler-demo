package com.activiti6.task;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * @author yezhaoxing
 * @date 2019/11/19
 */
@Component
@Slf4j
public class TransferTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("银行转账");
    }
}
