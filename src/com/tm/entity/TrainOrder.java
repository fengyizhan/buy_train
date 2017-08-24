package com.tm.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 订单需求
 * @author fyz
 */
public class TrainOrder implements Serializable{
	private static final long serialVersionUID = -5473754872657603585L;
	/*
	PKId
	AgentId
	OrderNumber
	OrderNumberAgent
	OrderNumber12306
	OrderState
	OfficialOrderState
	from_station
	to_station
	from_station_name
	to_station_name
	StartDate
	StartTime
	SecondTrainNo
	SecondSeats
	SecondStartDate
	OfficialAccount
	OfficialPassword
	GrapTicketMachineNo
	NeedNoSeat
	DeadlineTime
	TotalPrice
	*/
	private Long PKID;
	private Integer AgentId;
	private String OrderNumber;
	private String OrderNumberAgent;
	private String OrderNumber12306;
	public String getOrderNumber12306() {
		return OrderNumber12306;
	}
	public void setOrderNumber12306(String orderNumber12306) {
		OrderNumber12306 = orderNumber12306;
	}
	private Integer OrderState;
	private Integer OfficialOrderState;
	private String From_station;
	private String To_station;
	private String From_station_name;
	private String To_station_name;
	private String StartDate;
	private String StartTime;
	private String SecondTrainNo;
	private String SecondSeats;
	private Date SecondStartDate;
	private String OfficialAccount;
	private String OfficialPassword;
	private String GrapTicketMachineNo;
	private Integer NeedNoSeat;
	public Long getPKID() {
		return PKID;
	}
	public void setPKID(Long PKID) {
		this.PKID = PKID;
	}
	public Integer getAgentId() {
		return AgentId;
	}
	public void setAgentId(Integer agentId) {
		AgentId = agentId;
	}
	public String getOrderNumber() {
		return OrderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		OrderNumber = orderNumber;
	}
	public String getOrderNumberAgent() {
		return OrderNumberAgent;
	}
	public void setOrderNumberAgent(String orderNumberAgent) {
		OrderNumberAgent = orderNumberAgent;
	}
	public Integer getOrderState() {
		return OrderState;
	}
	public void setOrderState(Integer orderState) {
		OrderState = orderState;
	}
	public Integer getOfficialOrderState() {
		return OfficialOrderState;
	}
	public void setOfficialOrderState(Integer officialOrderState) {
		OfficialOrderState = officialOrderState;
	}
	public String getFrom_station() {
		return From_station;
	}
	public void setFrom_station(String from_station) {
		From_station = from_station;
	}
	public String getTo_station() {
		return To_station;
	}
	public void setTo_station(String to_station) {
		To_station = to_station;
	}
	public String getFrom_station_name() {
		return From_station_name;
	}
	public void setFrom_station_name(String from_station_name) {
		From_station_name = from_station_name;
	}
	public String getTo_station_name() {
		return To_station_name;
	}
	public void setTo_station_name(String to_station_name) {
		To_station_name = to_station_name;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getSecondTrainNo() {
		return SecondTrainNo;
	}
	public void setSecondTrainNo(String secondTrainNo) {
		SecondTrainNo = secondTrainNo;
	}
	public String getSecondSeats() {
		return SecondSeats;
	}
	public void setSecondSeats(String secondSeats) {
		SecondSeats = secondSeats;
	}
	public Date getSecondStartDate() {
		return SecondStartDate;
	}
	public void setSecondStartDate(Date secondStartDate) {
		SecondStartDate = secondStartDate;
	}
	public String getOfficialAccount() {
		return OfficialAccount;
	}
	public void setOfficialAccount(String officialAccount) {
		OfficialAccount = officialAccount;
	}
	public String getOfficialPassword() {
		return OfficialPassword;
	}
	public void setOfficialPassword(String officialPassword) {
		OfficialPassword = officialPassword;
	}
	public String getGrapTicketMachineNo() {
		return GrapTicketMachineNo;
	}
	public void setGrapTicketMachineNo(String grapTicketMachineNo) {
		GrapTicketMachineNo = grapTicketMachineNo;
	}
	public Integer getNeedNoSeat() {
		return NeedNoSeat;
	}
	public void setNeedNoSeat(Integer needNoSeat) {
		NeedNoSeat = needNoSeat;
	}
	public Timestamp getDeadlineTime() {
		return DeadlineTime;
	}
	public void setDeadlineTime(Timestamp deadlineTime) {
		DeadlineTime = deadlineTime;
	}
	public Double getTotalPrice() {
		return TotalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		TotalPrice = totalPrice;
	}
	private Timestamp DeadlineTime;
	private Double TotalPrice;
	private Timestamp CreateTime;
	public Timestamp getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Timestamp createTime) {
		CreateTime = createTime;
	}
	List<TrainPassenger> passengers=new ArrayList<TrainPassenger>();
	public List<TrainPassenger> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<TrainPassenger> passengers) {
		this.passengers = passengers;
	}
}
