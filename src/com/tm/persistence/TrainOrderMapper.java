package com.tm.persistence;

import java.util.List;
import java.util.Map;

import com.tm.entity.TrainOrder;

public interface TrainOrderMapper {
	Long insertOrder(TrainOrder order);
	TrainOrder findByOrderNumber(String OrderNumber);
	List<TrainOrder> findByMachineNo(String GrapTicketMachineNo);
	void updateOrder(TrainOrder trainOrder);
	void cancelByOrderNumber(String OrderNumber);
}
