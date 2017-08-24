package com.tm.utils;

import java.util.ArrayList;
import java.util.List;

public class EmailParams {

	private String from; // 发送人
	private String hostname; // 服务器地址
	private String userName; // 发送者用户名
	private String password; // 发送者服务器密码
	private String subject; // 邮件主题
	private String context; // 邮件内容
	private List<String> recipients = new ArrayList<String>(); // 邮件接收者

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}
	
	

}
