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
    <form  action="/user/add">
        <h2 >添加用户</h2>
        <label>名</label>
        <input type="text" name="firstName" required autofocus>
		<br/>
        <label>姓</label>
        <input type="text" name="lastName" required autofocus>
        <br>
        <label>密码</label>
        <input type="text" name="password" required>
        <br>
        <button type="submit">提交</button>
    </form>

</div>
</body>
</html>