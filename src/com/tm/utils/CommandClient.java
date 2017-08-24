package com.tm.utils;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

/**
 * 命令发送客户端，用来与【看门狗】程序通讯下发命令 
 * @author fyz
 *
 */
public class CommandClient {
	/**
	 * 请求服务器ip
	 */
	String host="127.0.0.1";
	/**
	 * 监听端口
	 */
	int port=8888;
	/**
	 * TCP请求失败后重试次数
	 */
	int retryTimes=3;
	public int getRetryTimes() {
		return retryTimes;
	}
	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * 发送命令给【看门狗】守护程序
	 * @param command 命令行
	 * @param retryTimes 重试次数
	 */
	public void sendCommand(String command)
	{
		sendCommand(command,0);
	}
	/**
	 * 发送命令给【看门狗】守护程序
	 * @param command 命令行
	 * @param retryTimes 重试次数
	 */
	public void sendCommand(String command,int retryTimeNumber)
	{
		Writer writer=null;
		Socket client=null;
		try {
		     client = new Socket(host, port);
		     writer = new OutputStreamWriter(client.getOutputStream());
		     writer.write(command);
		     writer.flush();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	if(retryTimeNumber>=this.getRetryTimes())
		    	{//超过重试次数，退出
		    		return;
		    	}
		    	sendCommand(command,(retryTimeNumber+1));
		    }
		finally
		{
			try {
				writer.close();
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 处理问题主题的命令
	 * @param topic 主题名称
	 */
	public void fixedProblemTopicCommand(String topic)
	{
		sendCommand(CommandUtil.COMMAND_RestartStorageServer+":"+topic);
	}
	public static void main(String[] args) {
		CommandClient commandClient=new CommandClient();
		commandClient.fixedProblemTopicCommand("tongxun10011");
	}
}
