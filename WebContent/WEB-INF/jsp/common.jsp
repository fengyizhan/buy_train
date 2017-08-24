<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="<%=path%>/js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="<%=path%>/js/message/xcConfirm.js"></script>
<link type="text/css" rel="stylesheet" href="<%=path%>/js/message/xcConfirm.css" />
<script type="text/javascript" src="<%=path%>/js/echarts.js"></script>
<script type="text/javascript" src="<%=path%>/js/vintage.js"></script>
<link type="text/css" rel="stylesheet" href="<%=path%>/js/datetime/jquery.datetimepicker.css" />
<script type="text/javascript" src="<%=path%>/js/datetime/jquery.datetimepicker.js"></script>
<link type="text/css" rel="stylesheet" href="<%=path%>/css/style.css" />
<script>
	window.contextPath='<%=path%>';
</script>
