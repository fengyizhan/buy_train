<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String message=(String)request.getAttribute("message");
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<link rel="stylesheet" href="<%=path %>/css/login.css">
<style>
#main {position: absolute;width:400px;height:261px;left:50%;top:50%; 
margin-left:-200px;margin-top:-131px;} 
fieldset
{
	border:1px solid #ffffff;
	padding-left:30px;
	padding-top:60px;
	padding-bottom:80px;
	color:#ffffff;
	font-weight:600;
	font-size:24px;
	width:400px;
	border-radius:50px;
}
legend
{
	padding-left:10px;
	padding-right:10px;
}
</style>
  </head>
  
  <body>
  <div id="main">
  	<fieldset>
    <legend>智能监控平台登陆</legend>
  
     <form method="post" action="<%=path %>/loginSubmit" class="login">
    <p>
      <label for="login">账号:</label>
      <input type="text" name="username" id="login">
    </p>

    <p>
      <label for="password">密码:</label>
      <input type="password" name="password" id="password">
    </p>

    <p class="login-submit">
      <button type="submit" class="login-button">登陆</button>
    </p>
  </form>
  <%
  	if(message!=null)
  	{
  	%>
		<section class="about">
		    <p class="about-author">
		       <%=message%>
		  	</p>
	  	</section>
  	<%	
  	}
  %>
  </fieldset>
  </div>
  </body>
  
</html>
