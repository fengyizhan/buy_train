package com.tm.persistence;

import java.util.List;

import com.tm.entity.TrainConfig;

public interface TrainConfigMapper {
	List<TrainConfig> findConfig();
}
