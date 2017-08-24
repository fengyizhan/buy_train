package com.tm.persistence;

import com.tm.entity.TrainMachine;

public interface TrainMachineMapper {
	Integer insertMachine(TrainMachine trainMachine);
	void updateMachine(TrainMachine trainMachine);
	void updateMachineLastSyncDate(TrainMachine trainMachine);
	TrainMachine findMachineByNumber(String Number);
}
