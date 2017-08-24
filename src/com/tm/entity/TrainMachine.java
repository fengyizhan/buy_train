package com.tm.entity;

import java.util.ArrayList;
import java.util.List;

import java.sql.Timestamp;

/**
 * 抢票机器
 * @author fyz
 */
public class TrainMachine {
	/*
	PKID
	Number
	LastSyncDate
	Enabled
	*/
	private Long PKID;
	private String Number;
	private Timestamp LastSyncDate;
	private Integer Enabled;
	private Integer OrderLimit;
	private List<TrainConfig> configs=new ArrayList<TrainConfig>();
	public Integer getOrderLimit() {
		return OrderLimit;
	}
	public void setOrderLimit(Integer orderLimit) {
		OrderLimit = orderLimit;
	}
	public Long getPKID() {
		return PKID;
	}
	public void setPKID(Long pKID) {
		PKID = pKID;
	}
	public String getNumber() {
		return Number;
	}
	public void setNumber(String number) {
		Number = number;
	}
	public Timestamp getLastSyncDate() {
		return LastSyncDate;
	}
	public void setLastSyncDate(Timestamp lastSyncDate) {
		LastSyncDate = lastSyncDate;
	}
	public Integer getEnabled() {
		return Enabled;
	}
	public void setEnabled(Integer enabled) {
		Enabled = enabled;
	}
	public List<TrainConfig> getConfigs() {
		return configs;
	}
	public void setConfigs(List<TrainConfig> configs) {
		this.configs = configs;
	}
}
