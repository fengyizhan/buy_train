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
	Object pm;	//springע������ô洢��ʵ��
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
	 * ����ж���������Ҫ�����������
	 * @return ip�б�
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
	 * ��������ȡIP
	 * @return ��������IP
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
	 * �ػ����̵���ʽ�����������ǰjvm�������̶߳���ֹͣ��������Ҳ�˳�����ʱ�ڵ��Զ���ʧ
	 * Ӧ������ʱ��[�󶨵�ǰ���ò���]����zookeeper�д���������ڵ�ע����Ϣ
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
	 * ��ʼ��ִ��ҵ��
	 * @param param_index ָ���ġ�ip���һ�Ρ�
	 * @param param_port ָ���ġ������˿ڡ�
	 */
	public void register(String param_index,String param_port)
	{
		String index="";
		String port="";
		String storage_program_identify="";
		if(pm!=null)
		{
			/**
			 * ����ParameterModel�е�index��topic��
			 * ��Ϊ���ɳ�������ʱ��Ψһ��ʾ��,�����á����Ź���������ͣ
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
		 * ��������ʱ�ڵ㡿������monitor��س����ء������򡿵�״̬
		 */
        
        //��ȡ�����Ź������ڵ�IP
        String localIp=getLocalIp();
//		String localIp=param_index;
		byte[] node_data=localIp.getBytes();
		//monitor��ص����ڵ㣬ÿ�������������󶼹�����������ڵ���
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
			//monitor��ص����ڵ㣬ÿ�������������󶼹�����������ڵ���
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
	 * ������������������ýڵ�
	 * @param node �ڵ�
	 * @param data ����
	 * @return �Ƿ�ɹ�
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
	 * ɾ����������������ýڵ�
	 * @param node �ڵ�
	 * @return �Ƿ�ɹ�
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
