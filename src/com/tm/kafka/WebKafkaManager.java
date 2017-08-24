package com.tm.kafka;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import com.github.zkclient.ZkClient;


public class WebKafkaManager {
	private static Logger logger = Logger.getLogger(WebKafkaManager.class);
	Object pm;	//spring注入的配置存储类实例
	private ZkClient zkClient=null;
	private static WebKafkaManager kafkaManager = new WebKafkaManager();
	public static String STORAGE_ALL="/storage/all/workers";
	public static String STORAGE_TEMP="/storage/temp/workers";
	public ZkClient getZkClient() {
		return zkClient;
	}
	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}
	public Object getPm() {
		return pm;
	}
	public void setPm(Object pm) {
		this.pm = pm;
	}
	public static WebKafkaManager getInstance()
	{
		return kafkaManager;
	}
	private WebKafkaManager()
	{
		Properties properties = new Properties();
		try
		{
			InputStream in=new BufferedInputStream(WebKafkaManager.class.getClassLoader().getResourceAsStream("config/application.properties"));
			properties.load(in);
			String zkConnect=properties.getProperty("consumer.zookeeper.connect");
	        String zkConnectTimeOut=properties.getProperty("consumer.zookeeper.connection.timeout.ms");
	        synchronized(this)
	        {	
	        	if(zkClient==null)
	        	{
	        		zkClient=new ZkClient(zkConnect,Integer.parseInt(zkConnectTimeOut),Integer.parseInt(zkConnectTimeOut));
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private Watcher watcher =  new Watcher() {  
		  
        public void process(WatchedEvent event) {
        	logger.info("process : " + event.getType());  
        }  
    };  
	/**
	 * 如果有多网卡，需要调用这个方法
	 * @return ip列表
	 */
	public static List<String> getAllIps(){
	    List<String> ips=new ArrayList<String>();
		try {
	    	Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); 
	        while (en.hasMoreElements()) {
	            NetworkInterface intf = (NetworkInterface) en.nextElement();
	            Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
	            while (enumIpAddr.hasMoreElements()) {
	                 InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
	                 if (!inetAddress.isLoopbackAddress()  && !inetAddress.isLinkLocalAddress() 
	                		 	&& inetAddress.isSiteLocalAddress()) {
	                	 ips.add(inetAddress.getHostAddress().toString());
	                 }
	             }
	          }
	    } catch (Exception e) {  }
	    return ips;
	}
	/**
	 * 单网卡获取IP
	 * @return 本地连接IP
	 */
	public static String getLocalIp()
	{
		InetAddress ia=null;
        try 
        {
            ia=InetAddress.getLocalHost();
            String localIp=ia.getHostAddress();
            return localIp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
	}
	/**
	 * 守护进程的形式启动，如果当前jvm中其他线程都已停止，本程序也退出，临时节点自动消失
	 * 应用启动时，[绑定当前配置参数]，在zookeeper中创建入库程序节点注册信息
	 */
	public void appBind()
	{
		if(pm!=null)
		{
			kafkaManager.setPm(pm);
		}
		Thread registerThread=new Thread()
		{
			@Override
			public void run() {
				super.run();
				kafkaManager.register(null, null);
			}
		};
		registerThread.setDaemon(true);
		registerThread.start();
	}
	/**
	 * 初始化执行业务
	 * @param param_index 指定的【ip最后一段】
	 * @param param_port 指定的【监听端口】
	 */
	public void register(String param_index,String param_port)
	{
		String index="";
		String port="";
		String storage_program_identify="";
		if(pm!=null)
		{
			/**
			 * 加载ParameterModel中的index和topic，
			 * 作为构成程序运行时的唯一标示符,便于让【看门狗】控制启停
			 */
			try {
				Method m_index=pm.getClass().getMethod("getIndex",null);
				Method m_port=pm.getClass().getMethod("getPort",null);
//				index=(String)m_index.invoke(pm,null);
				index=getLocalIp();
				port=(String)m_port.invoke(pm,null);
				storage_program_identify=index+"-"+port;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else
		{
			index=param_index;
			port=param_port;
			storage_program_identify=index+"-"+port;
		}
		/**
		 * 创建【临时节点】，便于monitor监控程序监控【入库程序】的状态
		 */
        
        //获取【看门狗】所在的IP
        String localIp=getLocalIp();
//		String localIp=param_index;
		byte[] node_data=localIp.getBytes();
		//monitor监控的主节点，每个入库程序启动后都挂载在这个根节点上
		try {
			boolean monitorAllRootNode=zkClient.exists(this.STORAGE_ALL);
			if(!monitorAllRootNode)
			{
				zkClient.createPersistent(this.STORAGE_ALL,true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//monitor监控的主节点，每个入库程序启动后都挂载在这个根节点上
			boolean monitorTempRootNode=zkClient.exists(this.STORAGE_TEMP);
			if(!monitorTempRootNode)
			{
				zkClient.createPersistent(this.STORAGE_TEMP,true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			zkClient.createEphemeral(this.STORAGE_TEMP+"/"+storage_program_identify,node_data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int i=0;
		while(true)
		{
			i++;
			try {
				Thread.sleep(1000);
				if(i%60==0)
				{
					logger.info("zk temp node connect heart beat:"+new Date().toLocaleString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 创建入库程序服务器永久节点
	 * @param node 节点
	 * @param data 数据
	 * @return 是否成功
	 */
	public boolean createStoragePersistentNode(String node,String data)
	{
		try 
		{
			zkClient.createPersistent(STORAGE_ALL+"/"+node,data.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 删除入库程序服务器永久节点
	 * @param node 节点
	 * @return 是否成功
	 */
	public boolean deleteStoragePersisitentNode(String node)
	{
		return zkClient.delete(STORAGE_ALL+"/"+node);
	}
	public static void main(String[] args) throws Exception{
		WebKafkaManager kafkaManager=WebKafkaManager.getInstance();
		kafkaManager.register(kafkaManager.getLocalIp(),"13568");
		for(int i=0;i<100;i++)
		{
			Thread.sleep(1000);
		}
	}

}
