<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common.jsp"%>
<%@page import="com.tm.entity.*" %>
<%@page import="com.tm.utils.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>注册入库程序</title>
    
	<script type="text/javascript">
	function addStorageMachine(){
		var form = document.forms[0];
		form.action = "/manage/addStorageMachine";
		form.method="post";
		form.submit();
	}
</script>

  </head>
  
  <body>
    <h1>注册入库程序</h1>
	<form action="" name="userForm">
		部署IP：<input type="text" name="ip">
		监听端口号：<input type="text" name="port">
		<input type="button" value="添加" onclick="addStorageMachine()">
	</form>
  </body>
</html>
