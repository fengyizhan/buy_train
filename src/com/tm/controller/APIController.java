package com.tm.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.AsyncContext;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tm.entity.TrainMachine;
import com.tm.entity.TrainOrder;
import com.tm.service.TrainMachineService;
import com.tm.service.TrainOrderService;
import com.tm.servlet.ContextMap;
import com.tm.servlet.MessageEntity;

/**
 * 加解密回调地址
 * @author fyz
 *
 */
@Controller
@RequestMapping("/api")
public class APIController {
	
	static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	@Resource
	private TrainOrderService trainOrderService;
	@Resource
	private TrainMachineService trainMachineService;
	/**
	 * 抢票机获取订单需求
	 */
	@RequestMapping(value="/findByMachineNo")
	public @ResponseBody List<TrainOrder> findByMachineNo(@RequestParam(name="machineNo")String GrapTicketMachineNo){
		List<TrainOrder> orders=trainOrderService.findByMachineNo(GrapTicketMachineNo);
		return orders;
	}
	/**
	 * 抢票机占座成功后调用更新库中数据
	 */
	@RequestMapping(value="/updateOrder",method=RequestMethod.POST)
	public @ResponseBody String updateOrder(@RequestParam(name="order") String order){
		try
		{
			TrainOrder trainOrder=gson.fromJson(order,TrainOrder.class);
			trainOrderService.updateOrder(trainOrder);
			return "true";
		} catch (Exception e) {
			return "false";
		}
	}
	/**
	 * 取消订单需求
	 */
	@RequestMapping(value="/cancelByOrderNumber")
	public @ResponseBody String cancelByOrderNumber(@RequestParam(name="orderNumber") String OrderNumber){
		try {
			trainOrderService.cancelByOrderNumber(OrderNumber);
			return "true";
		} catch (Exception e) {
			return "false";
		}
	}
	/**
	 * 抢票机占座成功后调用更新库中数据
	 */
	@RequestMapping(value="/getMachineInfo",method=RequestMethod.POST)
	public @ResponseBody TrainMachine getMachineInfo(@RequestParam(name="Number") String Number){
		TrainMachine machine=trainMachineService.findMachineByNumber(Number);
		return machine;
	}
	
	public void write(AsyncContext ctx, MessageEntity message) {
		String responseText="";
		try {
			responseText=message.getValue();
			ctx.getResponse().getWriter().write(responseText);
			System.out.println("messageID:"+message.getId()+"，enType:"+message.getEnType()+"，value:"+message.getValue());
			ctx.complete();
			//ContextMap.deleteContext(sessionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 编解码虚拟机主动回调并返回给http request客户端
	 */
	@RequestMapping(value="/code",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})  
	public void code(@RequestBody MessageEntity message){
		if(message!=null)
		{
			String sessionId=message.getId();
			AsyncContext ctx=ContextMap.getContext(sessionId);
			if(ctx!=null)
			{
				write(ctx,message);
			}
		}
	}
	
	/**
	 * 添加订单需求
	 */
	@RequestMapping(value="/saveOrder",method=RequestMethod.POST)  
	public @ResponseBody String saveOrder(@RequestParam(name="order") String order){
		try 
		{
			TrainOrder trainOrder=gson.fromJson(order,TrainOrder.class);
			trainOrderService.insertOrder(trainOrder);
			return "true";
		} catch (Exception e) {
			return "false";
		}
	}

}
