<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common.jsp"%>
<%@page import="com.tm.entity.*" %>
<%@page import="com.tm.utils.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>
  <body>
	<%
		List<GrapTicketMachine> storageMachineList=(List<GrapTicketMachine>)request.getAttribute("machineList");
		List<String> newNodes=(List<String>)request.getAttribute("newNodes");
		Integer unnormalNumbers=(Integer)request.getAttribute("unnormalNumbers");
		
	%>
<div class="kafkaStructure commonArea">
		<table border="1" width="98%" class="groupTable">
			<thead>
				<tr>
					<th colspan="7">
					共<%=storageMachineList.size() %>个入库程序，非正常 [<%=unnormalNumbers %>] 个
					</th>
				</tr>
				<tr>
					<th>
					编号
					</th>
					<th>
					服务器IP
					</th>
					<th>
					车载机监听端口
					</th>
					<th>
					状态
					</th>
					<th>
					节点数据
					</th>
					<th colspan="2">
					操作
					</th>
				</tr>
			</thead>
			<tbody>
			<% 
			for(int i=0;i<storageMachineList.size();i++)
			{
				GrapTicketMachine machine=storageMachineList.get(i);
				%>
				<tr>
				<td class="group-title">
				<%=i+1%>
				</td>
				<td>
				<%=machine.getIp()%>
				</td>
				<td>
				<%=machine.getPort()%>
				</td>
				<td>
				<%=machine.getContent()%>
				</td>
				<td>
				<%=machine.getNodeData()%>
				</td>
				<td>
				<% if("停止状态".equals(machine.getContent()))
				{
					%>
				<a href="<%=path%>/manage/restartStorageProgram?ip=<%=machine.getIp()%>&port=<%=machine.getPort()%>">重启服务</a>
					<%	
				}else{
				%>				
				<a href="<%=path%>/manage/restartStorageProgram?ip=<%=machine.getIp()%>&port=<%=machine.getPort()%>">强制重启</a>
				<%
				} 
				%>
				</td>
				<td>
					<a href="<%=path%>/manage/deleteStorageMachine?ip=<%=machine.getIp()%>&port=<%=machine.getPort()%>">删除</a>
				</td>
			</tr>
			<%
		}
		%>
		</tbody>
	</table>
	
	<table border="1" width="98%" class="groupTable">
			<thead>
				<tr>
					<th colspan="7">
					共<%=newNodes.size() %>个未受管入库程序
					</th>
				</tr>
				<tr>
					<th>
					编号
					</th>
					<th>
					服务器IP
					</th>
					<th>
					车载机监听端口
					</th>
				</tr>
			</thead>
			<tbody>
			<%
				for(int i=0;i<newNodes.size();i++)
				{
					String node=newNodes.get(i);
					String nodeinfo[]=node.split("-");
					String ip=nodeinfo[0];
					String port=nodeinfo[1];
					%>
					<tr>
						<td>
						<%=i+1 %>
						</td>
						<td>
						<%=ip %>
						</td>
						<td>
						<%=port %>
						</td>
					</tr>
					<%
				}
			%>
			</tbody>
			</table>
	</div>
  </body>
</html>