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
    <form  action="/vacation/apply">
        <h2 >请假申请</h2><label>请假用户id</label>
        <input type="text" name="userId" required autofocus value="${userId}">
		<br/>
        <label>请假天数</label>
        <input type="text" name="day" required autofocus>
        <br>
        <label>请假理由</label>
        <input type="text" name="remark" required>
        <br>
        <button type="submit">提交</button>
    </form>

</div>
</body>
</html>