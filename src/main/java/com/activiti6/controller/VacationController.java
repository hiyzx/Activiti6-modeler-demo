package com.activiti6.controller;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

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
        vars.put("args", day);
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
    public void image(@RequestParam String processInstanceId, HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream()) {
            InputStream is = getDiagram(processInstanceId);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            out.write(getImgByte(is));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InputStream getDiagram(String processInstanceId) {
        // 查询流程实例
        ProcessInstance pi = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();
        // 查询流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(pi.getProcessDefinitionId()).singleResult();
        // 获取 BPMN 模型对象
        BpmnModel model = repositoryService.getBpmnModel(pd.getId());
        // 定义使用宋体
        String fontName = "宋体";
        // 获取流程实例当前的节点，需要高亮显示
        List<String> currentActs = runtimeService.getActiveActivityIds(pi.getId());
        // BPMN模型对象、图片类型、显示的节点
        ProcessDiagramGenerator processDiagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        return processDiagramGenerator.generateDiagram(model, "png", currentActs, new ArrayList<>(), fontName,
                fontName, fontName, null, 10.0);
    }

    // 将输入流转换为 byte 数组
    private byte[] getImgByte(InputStream is) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int b;
        while ((b = is.read()) != -1) {
            byteStream.write(b);
        }
        byte[] bs = byteStream.toByteArray();
        byteStream.close();
        return bs;
    }

}
