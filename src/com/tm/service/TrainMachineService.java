package com.tm.service;

import com.tm.entity.TrainMachine;

/**
 * 抢票机处理业务类
 * @author fyz
 */
public interface TrainMachineService {
	/**
	 * 添加抢票机
	 * @param trainMachine
	 * @return
	 */
	Integer insertMachine(TrainMachine trainMachine);
	/**
	 * 更新抢票机
	 * @param trainMachine
	 */
	void updateMachine(TrainMachine trainMachine);
	/**
	 * 查询抢票机
	 * @param Number 机器编码
	 * @return
	 */
	TrainMachine findMachineByNumber(String Number);
	/**
	 * 更新抢票机最后同步数据时间
	 * @param trainMachine 抢票机信息
	 */
	void updateMachineLastSyncDate(TrainMachine trainMachine);
}
