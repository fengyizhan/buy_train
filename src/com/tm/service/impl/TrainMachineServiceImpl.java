package com.tm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.entity.TrainConfig;
import com.tm.entity.TrainMachine;
import com.tm.persistence.TrainConfigMapper;
import com.tm.persistence.TrainMachineMapper;
import com.tm.service.TrainMachineService;



@Service
@Transactional
//此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class TrainMachineServiceImpl implements TrainMachineService {
	
	@Resource
	private TrainMachineMapper machineMapper;
	
	@Resource
	private TrainConfigMapper configMapper;

	@Override
	public Integer insertMachine(TrainMachine trainMachine) {
		return machineMapper.insertMachine(trainMachine);
	}

	@Override
	public void updateMachine(TrainMachine trainMachine) {
		machineMapper.updateMachine(trainMachine);
	}
	@Override
	public void updateMachineLastSyncDate(TrainMachine trainMachine) {
		machineMapper.updateMachineLastSyncDate(trainMachine);
	}
	@Override
	public TrainMachine findMachineByNumber(String Number) {
		List<TrainConfig> configs=configMapper.findConfig();
		TrainMachine machine=machineMapper.findMachineByNumber(Number);
		machine.setConfigs(configs);
		return machine;
	}

}
