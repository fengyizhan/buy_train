package com.tm.entity;

/**
 * 系统配置信息
 * @author fyz
 */
public class TrainConfig {
	/*
	PKID
	Name
	Value
	*/
	private Long PKID;
	private String Name;
	private String Value;
	public Long getPKID() {
		return PKID;
	}
	public void setPKID(Long pKID) {
		PKID = pKID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
}
