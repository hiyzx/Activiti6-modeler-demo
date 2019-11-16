<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"/>
<title>Activiti6流程设计器</title>

</head>
<body>
<h2>
    <a href='/create?name=activiti&key=123456'>绘制流程</a>
</h2>
<h2>
    <a href='/user/index'>用户列表</a>
</h2>
<div>
	<table width="100%">
	    <tr>
	        <td width="10%">任务id</td>
	        <td width="10%">任务名称</td>
            <td width="10%">申请人</td>
	        <td width="10%">请假天数</td>
            <td width="20%">请假原因</td>
            <td width="10%">完成时间</td>
	        <td width="20%"><#if (url) == 'todo'>操作<#else></#if></td>
	    </tr>
	        <#list taskList as task>
	        <tr>
	            <td width="10%">${task.id}</td>
	            <td width="10%">${task.name}</td>
                <td width="10%">${task.applyName}</td>
                <td width="10%">${task.day}</td>
                <td width="20%">${task.remark}</td>
                <td width="10%"><#if (task.endTime)??>${task.endTime?string('yyyy-MM-dd HH:mm:ss')}<#else></#if></td>
				<#if (url) == 'todo'>
					<td width="20%">
					 <a href="/vacation/approval?userId=${userId}&taskId=${task.id}">通过</a>
					</td>
				<#else></#if>
	        </tr>
	       </#list>
	</table>
</div>
</body>
</html>