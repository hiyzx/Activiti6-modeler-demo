package com.activiti6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.activiti6.vo.TaskVo;
import com.activiti6.vo.UserDto;
import com.activiti6.vo.UserVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @author yezhaoxing
 * @date 2019/11/16
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IdentityService identityService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    @GetMapping("/index")
    public ModelAndView list(ModelAndView modelAndView) {
        List<User> userList = identityService.createUserQuery().list();
        List<UserVo> rtn = userList.stream()
                .map(o -> new UserVo(o.getId(), o.getFirstName(), o.getLastName(), o.getPassword()))
                .collect(Collectors.toList());

        modelAndView.setViewName("user");
        modelAndView.addObject("userList", rtn);
        return modelAndView;
    }

    @GetMapping("/addPage")
    public String add() {
        return "userAdd";
    }

    @GetMapping("/add")
    public void add(HttpServletResponse response, @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String password) throws IOException {
        if (identityService.createUserQuery().userFirstName(firstName).count() > 0) {
            throw new RuntimeException("用户已经存在");
        }
        String id = UUID.randomUUID().toString();
        User user = identityService.newUser(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        identityService.saveUser(user);
        response.sendRedirect("/user/index");
    }

    @GetMapping("/task/toDoList")
    public ModelAndView toDoList(ModelAndView modelAndView, @RequestParam String userId) {
        User user = identityService.createUserQuery().userId(userId).singleResult();

        List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).list();

        List<TaskVo> rtn = new ArrayList<>();
        taskList.forEach(o -> {
            Map<String, Object> variables = taskService.getVariables(o.getId());
            Long day = (Long) variables.get("day");
            String applyName = StringUtils.isEmpty((String) variables.get("applyName")) ? ""
                    : (String) variables.get("applyName");
            String remark = (String) variables.get("remark");
            rtn.add(new TaskVo(o.getId(), o.getName(), applyName, day, remark, null));
        });
        modelAndView.setViewName("task");
        modelAndView.addObject("url", "todo");
        modelAndView.addObject("taskList", rtn);
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

    @GetMapping("/task/doList")
    public ModelAndView doList(ModelAndView modelAndView, @RequestParam String userId) {
        User user = identityService.createUserQuery().userId(userId).singleResult();

        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(user.getId()).finished().list();

        List<TaskVo> rtn = new ArrayList<>();
        taskList.forEach(o -> {
            Long day = 0L;
            String remark = "";
            String applyName = "";
            try {

                List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
                        .processInstanceId(o.getProcessInstanceId()).list();
                for (HistoricVariableInstance variable : variables) {
                    if (variable.getVariableName().equals("day")) {
                        day = (Long) variable.getValue();
                    }
                    if (variable.getVariableName().equals("remark")) {
                        remark = (String) variable.getValue();
                    }
                    if (variable.getVariableName().equals("applyName")) {
                        applyName = (String) variable.getValue();
                    }
                }

            } catch (Exception ex) {

            }
            rtn.add(new TaskVo(o.getId(), o.getName(), applyName, day, remark, o.getEndTime()));
        });
        modelAndView.setViewName("task");
        modelAndView.addObject("url", "do");
        modelAndView.addObject("taskList", rtn);
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

}
