package com.tm.utils;


import java.io.Serializable;

public class AjaxResultJson implements Serializable {
	private static final long serialVersionUID = 6447549039085924898L;

	private boolean state = true;
	
	private Integer errorCode;

	private String message;

	private Object data;
	
	public AjaxResultJson(boolean state, int errorCode, String message, Object data) {
		this.state = state;
		this.errorCode = errorCode;
		this.message = message;
		this.data = data;
	}

	public AjaxResultJson(boolean state, String message, Object data) {
		this.state = state;
		this.message = message;
		this.data = data;
	}
	
	public AjaxResultJson(boolean state, int errorCode, String message) {
		this.state = state;
		this.errorCode = errorCode;
		this.message = message;
	}

	public AjaxResultJson(boolean state, String message) {
		this.state = state;
		this.message = message;
	}
	
	public AjaxResultJson(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public AjaxResultJson(String message) {
		this.message = message;
	}

	public AjaxResultJson(Object data) {
		this.state = true;
		this.data = data;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
