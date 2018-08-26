<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
   
  <div style="color:red;">${msg }</div>
  <form action="${pageContext.request.contextPath}/user/login" method="post">
    <input type="hidden" name="returnUrl" value="${returnUrl }" >
    <label>用户名：<input type="text" name="username" ></label><br>
    <label>密码：<input type="password" name="password" ></label><br>
    <button type="submit">登录</button>
  </form>

</body>
</html>