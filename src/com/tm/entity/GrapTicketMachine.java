package com.tm.entity;

/**
 * 入库程序服务器条目
 * @author fyz
 * @param <T>
 */
public class GrapTicketMachine implements Comparable<GrapTicketMachine>{
	/**
	 * 服务器IP
	 */
	private String ip="";
	/**
	 * 监听车载机端口号
	 */
	private String port="";
	/**
	 * 编号
	 */
	private String index="";
	/**
	 * 其他内容，可以为json
	 */
	private String content="";
	/**
	 * 节点数据，用作存储一些公共信息
	 */
	private String nodeData="";
	public String getNodeData() {
		return nodeData;
	}
	public void setNodeData(String nodeData) {
		this.nodeData = nodeData;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	@Override
	public int compareTo(GrapTicketMachine other) {
		int ipCom=ip.compareTo(other.getIp());
		if(ipCom==0)
		{
			return port.compareTo(other.getPort());
		}else
		{
			return ipCom;
		}
	}
}
