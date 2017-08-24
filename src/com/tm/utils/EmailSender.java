package com.tm.utils;


/**
 * 邮件发送者
 *
 * @author sunfangjin
 *
 */
public interface EmailSender {
	
	public void send(EmailParams emailParams);
	
}
