package com.tm.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认邮件发送者实现
 *
 * @author sunfangjin
 *
 */
public class DefaultEmailSender implements EmailSender {

	private static Logger logger = LoggerFactory.getLogger(DefaultEmailSender.class);
	private EmailParams emailParams;
	private static String emails="";
	private static String mobiles="";
	@Override
	public void send(EmailParams emailParams) {
		if (null != emailParams) {
			// 获取接收人地址
			List<String> recipients = emailParams.getRecipients();
			for (String s : recipients) {
				try {
					HtmlEmail email = new HtmlEmail();
					email.setSslSmtpPort("25");
					email.setSSLOnConnect(true);
					//自己的邮箱,密码
					email.setAuthentication(emailParams.getUserName(), emailParams.getPassword()); // 设置你的邮箱帐号以及密码
					email.setHostName(emailParams.getHostname()); // 设置SMTP
					email.addTo(s); // 收件箱
					email.setFrom(emailParams.getFrom(), "系统告警");// 发件人地址
					email.setCharset("utf-8"); // 设置编码
					email.setSubject(emailParams.getSubject()); // 邮件标题
					email.setHtmlMsg(emailParams.getContext()); // 邮件内容
					email.send();
				} catch (Exception e) {
					logger.error("发送邮件失败", e);
				}
			}
			;
		}

	}

	public EmailParams getEmailParams() {
		return emailParams;
	}

	public void setEmailParams(EmailParams emailParams) {
		this.emailParams = emailParams;
	}

	public EmailParams init(StringBuffer sb){
		EmailParams mailPara = new EmailParams();
		mailPara.setContext(sb.toString());
		mailPara.setFrom("mayanlu@tiamaes.com"); // 发送人
		mailPara.setHostname("192.168.38.68");// 邮件服务器IP
		mailPara.setUserName("mayanlu@tiamaes.com");// 发送者用户名
		mailPara.setPassword("TMabc@123456");// 发件人密码
		List<String> recipients = new ArrayList<>();
		//收件人的地址
		recipients.add("tianxiaokang@tiamaes.com");
		recipients.add("sunfangjin@tiamaes.com");
		mailPara.setRecipients(recipients);// 收件人列表
		mailPara.setSubject("重要且紧急:公交入库程序消息积压,请关注!");// 主题
		return mailPara;
	}
	public EmailParams oomInit(StringBuffer sb){
		EmailParams mailPara = new EmailParams();
		mailPara.setContext(sb.toString());
		//收件人的地址
		if("".equals(emails))
		{
			Properties properties = new Properties();
			try
			{
				InputStream in=new BufferedInputStream(DefaultEmailSender.class.getClassLoader().getResourceAsStream("config/message.properties"));
				properties.load(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String emails=properties.getProperty("emails");
			String fromuser=properties.getProperty("fromuser");
			String fromuserpassword=properties.getProperty("fromuserpassword");
			String server=properties.getProperty("server");
			mailPara.setFrom(fromuser); 			// 发送人
			mailPara.setHostname(server);			// 邮件服务器IP
			mailPara.setUserName(fromuser);			// 发送者用户名
			mailPara.setPassword(fromuserpassword);	// 发件人密码
			List<String> config_emails=Arrays.asList(StringUtils.split(emails, ","));
			mailPara.setRecipients(config_emails);// 收件人列表
		}
		
		
		mailPara.setSubject("重要且紧急:公交入库程序内存溢出,请关注!");// 主题
		return mailPara;
	}
	public static void main(String[] args) {
		logger.info("test");
//		DefaultEmailSender mail = new DefaultEmailSender();
//		mail.send(mail.oomInit(new StringBuffer().append("这是一封测试邮件!")));
	}

}
