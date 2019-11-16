package com.activiti6.controller;

import org.activiti.engine.*;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yezhaoxing
 * @date 2019/11/16
 */
@Controller
@RequestMapping("/vacation")
public class VacationController {

    @Autowired
    private IdentityService identityService;
    @Autowired
    private FormService formService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;

    @GetMapping("/page")
    public ModelAndView page(@RequestParam String userId, ModelAndView modelAndView) {
        modelAndView.setViewName("vacation");
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

    @GetMapping("/apply")
    @Transactional
    public void apply(HttpServletResponse response, @RequestParam String userId, @RequestParam Long day,
            @RequestParam String remark) throws IOException {
        // 查找流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey("vacation")
                .latestVersion().singleResult();

        User user = identityService.createUserQuery().userId(userId).singleResult();

        // 初始化任务参数
        Map<String, Object> vars = new HashMap<>();
        vars.put("day", day);
        vars.put("remark", remark);
        vars.put("applyName", user.getFirstName());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(pd.getKey());
        Task firstTask = taskService.createTaskQuery().processInstanceId(processInstance.getId())
                .taskCandidateOrAssigned(userId).singleResult();
        if (firstTask == null) {
            throw new RuntimeException("没有权限");
        }
        // 设置任务受理人
        taskService.setAssignee(firstTask.getId(), userId);
        // 完成任务
        taskService.complete(firstTask.getId(), vars);
        response.sendRedirect("/user/index");
    }

    @GetMapping("/approval")
    public void approval(HttpServletResponse response, @RequestParam String userId, @RequestParam String taskId)
            throws IOException {
        // 设置任务受理人
        taskService.setAssignee(taskId, userId);
        // 完成任务
        taskService.complete(taskId);
        response.sendRedirect("/user/task/toDoList?userId=" + userId);
    }

    @GetMapping("/image")
    public void image(@RequestParam String taskId) throws IOException {

    }

}
