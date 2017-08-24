package com.tm.utils;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

/**
 * ����Ϳͻ��ˣ������롾���Ź�������ͨѶ�·����� 
 * @author fyz
 *
 */
public class CommandClient {
	/**
	 * ���������ip
	 */
	String host="127.0.0.1";
	/**
	 * �����˿�
	 */
	int port=8888;
	/**
	 * TCP����ʧ�ܺ����Դ���
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
	 * ��������������Ź����ػ�����
	 * @param command ������
	 * @param retryTimes ���Դ���
	 */
	public void sendCommand(String command)
	{
		sendCommand(command,0);
	}
	/**
	 * ��������������Ź����ػ�����
	 * @param command ������
	 * @param retryTimes ���Դ���
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
		    	{//�������Դ������˳�
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
	 * �����������������
	 * @param topic ��������
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
