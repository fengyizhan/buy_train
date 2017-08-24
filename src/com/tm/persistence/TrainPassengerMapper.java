package com.tm.persistence;

import com.tm.entity.TrainPassenger;

public interface TrainPassengerMapper {
	Integer insertPassenger(TrainPassenger trainPassenger);
	void updatePassenger(TrainPassenger passenger);
}
