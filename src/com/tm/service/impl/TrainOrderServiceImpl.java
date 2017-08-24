package com.tm.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.entity.TrainMachine;
import com.tm.entity.TrainOrder;
import com.tm.entity.TrainPassenger;
import com.tm.persistence.TrainMachineMapper;
import com.tm.persistence.TrainOrderMapper;
import com.tm.persistence.TrainPassengerMapper;
import com.tm.service.TrainOrderService;



@Service
@Transactional
//此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class TrainOrderServiceImpl implements TrainOrderService {
	
	@Resource
	private TrainOrderMapper mapper;
	@Resource
	private TrainPassengerMapper passengerMapper;
	@Resource
	private TrainMachineMapper machineMapper;

	@Override
	public Long insertOrder(TrainOrder order) {
		mapper.insertOrder(order);
		Long orderId= order.getPKID();
		List<TrainPassenger> passengers=order.getPassengers();
		if(passengers!=null&&passengers.size()>0)
		{
			for(TrainPassenger p:passengers)
			{
				p.setFKOrderId(orderId);
				passengerMapper.insertPassenger(p);
			}
		}
		return orderId;
	}

	@Override
	public TrainOrder findByOrderNumber(String OrderNumber) {
		return mapper.findByOrderNumber(OrderNumber);
	}

	@Override
	public List<TrainOrder> findByMachineNo(String GrapTicketMachineNo) {
		TrainMachine machine=new TrainMachine();
		machine.setLastSyncDate(new Timestamp(new Date().getTime()));
		machine.setNumber(GrapTicketMachineNo);
		machineMapper.updateMachineLastSyncDate(machine);
		return mapper.findByMachineNo(GrapTicketMachineNo);
	}

	@Override
	public void updateOrder(TrainOrder order) {
		mapper.updateOrder(order);
		List<TrainPassenger> passengers=order.getPassengers();
		if(passengers!=null&&passengers.size()>0)
		{
			for(TrainPassenger p:passengers)
			{
				passengerMapper.updatePassenger(p);
			}
		}
	}

	@Override
	public void cancelByOrderNumber(String OrderNumber) {
		mapper.cancelByOrderNumber(OrderNumber);
	}
}
