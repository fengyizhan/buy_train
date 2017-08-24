package com.tm.entity;

import java.io.Serializable;

/**
 * 抢票乘客
 * @author fyz
 */
public class TrainPassenger implements Serializable{
	private static final long serialVersionUID = -1642517880686486558L;
	/*
	PKID
	FKOrderId
	Name
	IdentifyType
	IdentifyNumber
	TicketType
	Price
	SeatType
	Room_no
	Seat_name
	*/
	private Long PKID;
	public Long getPKID() {
		return PKID;
	}
	public void setPKID(Long pKID) {
		PKID = pKID;
	}
	public Long getFKOrderId() {
		return FKOrderId;
	}
	public void setFKOrderId(Long fKOrderId) {
		FKOrderId = fKOrderId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Integer getIdentifyType() {
		return IdentifyType;
	}
	public void setIdentifyType(Integer identifyType) {
		IdentifyType = identifyType;
	}
	public String getIdentifyNumber() {
		return IdentifyNumber;
	}
	public void setIdentifyNumber(String identifyNumber) {
		IdentifyNumber = identifyNumber;
	}
	public String getTicketType() {
		return TicketType;
	}
	public void setTicketType(String ticketType) {
		TicketType = ticketType;
	}
	public Double getPrice() {
		return Price;
	}
	public void setPrice(Double price) {
		Price = price;
	}
	public String getSeatType() {
		return SeatType;
	}
	public void setSeatType(String seatType) {
		SeatType = seatType;
	}
	public String getRoom_no() {
		return Room_no;
	}
	public void setRoom_no(String room_no) {
		Room_no = room_no;
	}
	public String getSeat_name() {
		return Seat_name;
	}
	public void setSeat_name(String seat_name) {
		Seat_name = seat_name;
	}
	private Long FKOrderId;
	private String Name;
	private Integer IdentifyType;
	private String IdentifyNumber;
	private String TicketType;
	private Double Price;
	private String SeatType;
	private String Room_no;
	private String Seat_name;
}
