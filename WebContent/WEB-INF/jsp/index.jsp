<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common.jsp"%>
<%@page import="com.tm.entity.*" %>
<%@page import="com.tm.utils.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<script type="text/javascript" src="<%=path%>/js/index.js"></script>
    <title>抢票监控</title>
	<script type="text/javascript">
	$(document).ready(function()
	{
		$('#start_msg_timestam').datetimepicker({format:"Y-m-d H:i",step:15});
		$('#end_msg_timestam').datetimepicker({format:"Y-m-d H:i",step:15});
		window.kafka.searchTopicForm.queryTopic();
	});
</script>
  </head>
  <%
	  String start_msg_timestam=(String)request.getAttribute("start_msg_timestam");
	  String end_msg_timestam=(String)request.getAttribute("end_msg_timestam");
  	  
  %>
  <style>

  </style>
  <body>
		<ul class="tabs">
		<li><input type="radio" name="tabs" id="tab1" checked/> <label
				for="tab1">抢票程序监控</label>
				<div id="tab-content1" class="tab-content">
				
						<div class="commonArea">
							<table border="1" class="searchTable">
								<tr>
										<td>
											服务器列表刷新设置：
										</td>
										<td>
										<label for="storageMachineRefreshEnable">启用自动刷新</label><input type="checkbox" id="storageMachineRefreshEnable"/>
											<select id="storageMachineRefreshTime">
												<option value="10">10秒</option>
												<option value="30">30秒</option>
												<option value="60">60秒</option>
												<option value="120">120秒</option>
											</select>
										</td>
										<td>
											<input type="button" value="手动刷新" class="actionBtn" onclick="refreshStorageMachineList();"/>
										</td>
								</tr>
							</table>
						<form method="post" action="<%=path%>/manage/addStorageMachineList" target="storageMachineList" id="storageMachineListForm">
							<table border="1" class="searchTable">
								<tr>
										<td>
											部署服务器IP：
										</td>
										<td>
											<input type="text" id="deploy_server_ip" name="deploy_server_ip" value="192.168." maxlength="15"/>
										</td>
										<td>
											监听端口号：
										</td>
										<td>
											<input type="text" id="listening_port" name="listening_port" value="" maxlength="6"/>
										</td>
										<td><input type="button" value="添加" class="actionBtn" onclick="addStorageMachine();"/>
											<input type="hidden" name="random" value="<%=new Date().getTime()%>"/>
										</td>
								</tr>
							</table>
						</form>
						<iframe src="<%=path %>/manage/machineList?random=<%=new Date().getTime()%>" id="storageMachineList" name="storageMachineList" style="width:100%;min-height:1000px;height:1000px;" scrolling="no" frameborder="0" align="center" onload="setIframeHeight(this)"></iframe>
					</div>
					
				</div></li>

			
		</ul>
				
  </body>
</html>
