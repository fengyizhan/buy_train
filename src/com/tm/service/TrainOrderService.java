package com.tm.service;

import java.util.List;

import com.tm.entity.TrainOrder;

/**
 * 订单处理业务类
 * @author fyz
 */
public interface TrainOrderService {
	/**
	 * 插入新订单需求
	 * @param order 订单需求
	 * @return 插入记录ID
	 */
	Long insertOrder(TrainOrder order);
	/**
	 * 根据订单号得到记录详情
	 * @param OrderNumber 订单号
	 * @return 订单详情
	 */
	TrainOrder findByOrderNumber(String OrderNumber);
	/**
	 * 根据【抢票机】编号，获取所有分配订单需求
	 * @param GrapTicketMachineNo 抢票机编号
	 * @return
	 */
	List<TrainOrder> findByMachineNo(String GrapTicketMachineNo);
	/**
	 * 根据【订单号】更新记录内容
	 * @param map 订单号、12306订单号、订单状态
	 */
	void updateOrder(TrainOrder order);
	/**
	 * 取消订单需求
	 * @param OrderNumber 订单编号s
	 */
	void cancelByOrderNumber(String OrderNumber);
}
