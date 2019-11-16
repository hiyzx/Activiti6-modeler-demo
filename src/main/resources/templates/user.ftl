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
    <a href='/index'>流程列表</a>
</h2>
<h2>
    <a href='/user/addPage'>创建用户</a>
</h2>
<div>
	<table width="100%">
	    <tr>
	        <td width="30%">用户id</td>
	        <td width="10%">用户名</td>
	        <td width="10%">用户姓</td>
	        <td width="10%">密码</td>
	        <td width="40%">操作</td>
	    </tr>
	        <#list userList as user>
	        <tr>
	            <td width="10%">${user.id}</td>
	            <td width="10%">${user.firstName}</td>
	            <td width="20%">${user.lastName}</td>
	            <td width="20%">${user.password}</td>
	            <td width="40%">
	             <a href="/user/task/toDoList?userId=${user.id}">代办任务</a>
				 <a href="/user/task/doList?userId=${user.id}">历史任务</a>
	             <a href="/vacation/page?userId=${user.id}">请假</a>
	            </td>
	        </tr>
	       </#list>
	</table>
</div>
</body>
</html>