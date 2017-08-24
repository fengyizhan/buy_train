package com.tm.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.zkclient.ZkClient;
import com.tm.entity.GrapTicketMachine;
import com.tm.kafka.WebKafkaManager;
import com.tm.service.TrainOrderService;
import com.tm.utils.DateUtils;


@Controller
@RequestMapping("/manage")
public class ManageController {

	@Autowired
	private TrainOrderService trainOrderService;
	/**
	 * 跳转到主界面
	 */
	@RequestMapping("")
	public String toIndex(HttpServletRequest request,HttpServletResponse response){
		Calendar today=Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY,23);
		today.set(Calendar.MINUTE,59);
		today.set(Calendar.SECOND,59);
		Date end_msg_timestam_date=today.getTime();
		String end_msg_timestam=DateUtils.dateToStr(end_msg_timestam_date,6);
		
		today.add(Calendar.DATE,-7);
		today.set(Calendar.HOUR_OF_DAY,0);
		today.set(Calendar.MINUTE,0);
		today.set(Calendar.SECOND,0);
		Date start_msg_timestam_date=today.getTime();
		String start_msg_timestam=DateUtils.dateToStr(start_msg_timestam_date,6);
		request.setAttribute("start_msg_timestam", start_msg_timestam);
		request.setAttribute("end_msg_timestam", end_msg_timestam);
		
		return "/index";
	}
	
	
	/**
	 * 程序服务器列表显示页面
	 */
	@RequestMapping("/machineList")
	public String toMachineList(HttpServletRequest request,HttpServletResponse response){
		WebKafkaManager kafkaManager =WebKafkaManager.getInstance();
		ZkClient zkClient=kafkaManager.getZkClient();
		List<String> allNodes=zkClient.getChildren(WebKafkaManager.STORAGE_ALL);
		List<String> tempNodes=zkClient.getChildren(WebKafkaManager.STORAGE_TEMP);
		List<GrapTicketMachine> machineList=new ArrayList<GrapTicketMachine>();
		int normalNumbers=0;
		for(String node:allNodes)
		{
			try {
					GrapTicketMachine machine=new GrapTicketMachine();
					String node_data="";
					try {
						node_data=new String(zkClient.readData(WebKafkaManager.STORAGE_ALL+"/"+node),"UTF-8");
					} catch (Exception e) {
						e.printStackTrace();
					}
				String node_info_array[]=node.split("-");
				String ip=node_info_array[0];
				String port=node_info_array[1];
				machine.setIp(ip);
				machine.setPort(port);
				machine.setNodeData(node_data);
				if(!tempNodes.contains(node))
				{//如果发现异常现象，直接提示
					machine.setContent("停止状态");
				}else
				{
					machine.setContent("正常");
					normalNumbers++;
				}
				machineList.add(machine);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<String> newNodes=new ArrayList<String>();
		for(String node:tempNodes)
		{
			if(!allNodes.contains(node))
			{
				newNodes.add(node);
			}
		}
		int unnormalNumbers=allNodes.size()-normalNumbers;
		Collections.sort(machineList);
		request.setAttribute("machineList",machineList);
		request.setAttribute("allNodes",allNodes);
		request.setAttribute("tempNodes",tempNodes);
		request.setAttribute("newNodes",newNodes);
		request.setAttribute("unnormalNumbers",unnormalNumbers);
		return "/machineList";
	}
	
	/**
	 * 程序服务器添加
	 */
	@RequestMapping("/addMachine")
	public String addMachine(HttpServletRequest request,HttpServletResponse response){
		String url = "redirect:/manage/machineList";
		
		String deploy_server_ip=request.getParameter("deploy_server_ip");
		String listening_port=request.getParameter("listening_port");
		
		Pattern pattern = Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
		if (StringUtils.isBlank(deploy_server_ip) || StringUtils.isBlank(listening_port) || !pattern.matcher(deploy_server_ip).matches()) {
			return url;
		}
		
		WebKafkaManager kafkaManager =WebKafkaManager.getInstance();
		kafkaManager.createStoragePersistentNode(deploy_server_ip+"-"+listening_port, listening_port);
		return url;
	}
	
	/**
	 * 程序服务器删除
	 */
	@RequestMapping("/deleteMachine")
	public String deleteMachine(HttpServletRequest request,HttpServletResponse response){
		String deploy_server_ip=request.getParameter("ip");
		String listening_port=request.getParameter("port");
		WebKafkaManager kafkaManager =WebKafkaManager.getInstance();
		kafkaManager.deleteStoragePersisitentNode(deploy_server_ip+"-"+listening_port);
		return "redirect:/manage/machineList";
	}

}
