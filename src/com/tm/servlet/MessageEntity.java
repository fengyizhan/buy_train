package com.tm.servlet;

import java.io.Serializable;

/**
 * 编解码消息体
 * @author fyz
 */

public class MessageEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	public String Id;
    public int enType;
    public String value;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getEnType() {
        return enType;
    }

    public void setEnType(int enType) {
        this.enType = enType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
