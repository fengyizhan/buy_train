package com.tm.utils;


import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 与公交入库程序部署在一起的【看门狗】
 * 用来管理和启动相关程序
 * @author fyz
 */
public class CommandUtil
{
//	static JafkaManager jafkaManager=new JafkaManager();
	public static String COMMAND_RestartStorageServer="restartStorageServer";
	/**
	 * 用来读取命令执行结果
	 * @author fyz
	 */
	static class InputReader
	{
		LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
		List<String> lines=new ArrayList<String>();
		public LinkedHashMap<String, String> getMap() {
			return map;
		}
		public void setMap(LinkedHashMap<String, String> map) {
			this.map = map;
		}
		public List<String> getLines() {
			return lines;
		}
		public void setLines(List<String> lines) {
			this.lines = lines;
		}
		/**
		 * 处理每一行的逻辑
		 * @param line 行
		 */
		public void processLine(String line)
		{
			
		}
		/**
		 * 命令执行后的逻辑
		 */
		public void afterExecuted()
		{
			
		}
		public void read(InputStream is)
		{
			InputStreamReader isr=null;
			BufferedReader br=null;
			try {
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null)
				{
					if("".equals(line.trim()))
					{
						continue;
					}
					try
					{
						processLine(line);
						System.out.println(line);
					} catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				try
				{
					afterExecuted();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception ioe) {
				ioe.printStackTrace();
			}finally
			{
				try {
					br.close();
				} catch (Exception e) {
				}
				try {
					isr.close();
				} catch (Exception e) {
				}
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
	}
	/**
	 * 默认命令服务监听端口
	 */
	int port = 8888;
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public static void execute(String commond,String targetExe,String businessType)
	{
		class StreamGobbler extends Thread {
			InputStream is;
			String type;
			
			StreamGobbler(InputStream is, String type) {
				this.is = is;
				this.type = type;
			}

			public void run()
			{
				InputReader inputReader=new InputReader()
				{
					@Override
					public void processLine(String line)
					{
						String propertyArray[]=line.split("=");
						lines.add(line);
						map.put(propertyArray[0],propertyArray[1]);
					}
					@Override
					public void afterExecuted()
					{
						System.out.println(map);
					}
				};
				inputReader.read(is);
			}
		}
		try
		{
			Runtime rt = Runtime.getRuntime();
			System.out.println(new Date().toLocaleString()+"执行命令： " + commond);
			Process proc = rt.exec(commond);
			// 输入 output监控
			StreamGobbler outputGobbler = new StreamGobbler(
					proc.getInputStream(), businessType);
			outputGobbler.start();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * 关闭并重启相关服务
	 * @param targetExe 目标宿主exe文件信息，支持*，如tongxun188.exe或者tongxun*.exe
	 */
	public static void restartService(String targetExe)
	{
		restartService(targetExe,true,2);
	}
	/**
	 * 关闭相关服务
	 * @param targetExe 目标宿主exe文件信息，支持*，如tongxun188.exe或者tongxun*.exe
	 * @param needReboot 是否需要重启程序
	 * @param latency 后续命令执行延迟
	 */
	public static void restartService(final String targetExe,Boolean needReboot,long latency)
	{
		class StreamGobbler extends Thread {
			InputStream is;
			String type;
			
			StreamGobbler(InputStream is, String type) {
				this.is = is;
				this.type = type;
			}

			public void run()
			{
				StringBuffer result=new StringBuffer("");
				List<String[]> lines=new ArrayList<String[]>();
				try {
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
					String line = null;
					while ((line = br.readLine()) != null)
					{
						if("".equals(line.trim()))
						{
							continue;
						}
						result.append(line+"\n");
						String propertyArray[]=line.split(" ");
						System.out.println(line);
						
						lines.add(propertyArray);
					}
					String executeResult=result.toString().trim();
					if("".equals(executeResult))
					{
						System.out.println("没有发现与目标【"+targetExe+"】匹配的运行程序，尝试C盘下寻找。");
						lines.add(new String[]{"",""});
					}
					File executableBatFile=new File("C:/"+targetExe+".bat");
					if(!executableBatFile.exists())
					{
						System.out.println("目标【"+targetExe+"】在C盘下不存在启动脚本，请检查。");
						return;
					}
					if(COMMAND_RestartStorageServer.equals(type))
					{
						for(String[] propertys:lines)
						{
							String todayDate=DateUtils.dateFormat(new Date(),DateUtils.YYYYMMDD);
							String processid=propertys[0];
							if(!"".equals(processid))
							{
								String killCommand="cmd /c taskkill /F /PID "+processid+" /T";
								System.out.println(new Date().toLocaleString()+" killStorageServer： " + killCommand);
								Runtime kill_rt = Runtime.getRuntime();
								Process kill_process=kill_rt.exec(killCommand);
								InputReader kill_inputReader=new InputReader();
								kill_inputReader.read(kill_process.getInputStream());
							}
							/*
							//关闭jafka Server服务
							System.out.println(new Date().toLocaleString()+" stopJafkaServer");
							Runtime stop_jafka = Runtime.getRuntime();
							Process stop_jafka_process=stop_jafka.exec("cmd /c net stop jafka");
							InputReader stop_jafka_inputReader=new InputReader();
							stop_jafka_inputReader.read(stop_jafka_process.getInputStream());
							
							//启动jafka Server服务
							System.out.println(new Date().toLocaleString()+" startJafkaServer");
							Runtime start_jafka = Runtime.getRuntime();
							Process start_jafka_process=start_jafka.exec("cmd /c net start jafka");
							InputReader start_jafka_inputReader=new InputReader();
							start_jafka_inputReader.read(start_jafka_process.getInputStream());
							*/
							
							//修复【问题topic】
//							String topicName=executableFile.getName().substring(0,executableFile.getName().indexOf("."));
//							System.out.println(new Date().toLocaleString()+" fixed problem topic["+topicName+"] data start");
//							jafkaManager.createNewTopicAndCopyMessage(topicName);
//							System.out.println(new Date().toLocaleString()+" fixed problem topic["+topicName+"] data end");
							
							//重启入库程序
							System.out.println(new Date().toLocaleString()+" startStorageServer");
							Desktop.getDesktop().open(executableBatFile);
						
						}
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		//根据目标程序的特征，寻找到程序的【执行路径】，【进程ID】，便于后续操作之用
//		String command="cmd /c wmic process get executablepath,processid/format:csv|findstr \""+targetExe+"\"";
		String command="cmd /c jps -mlv|findstr \""+targetExe+"\"";
		try
		{
			Runtime rt = Runtime.getRuntime();
			System.out.println(new Date().toLocaleString()+"执行命令： " + command);
			Process proc = rt.exec(command);
			// 输入 output监控
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(),"restartStorageServer");
			outputGobbler.start();
//			// 命令执行异常
//			int exitVal = proc.waitFor();
//			System.out.println("ExitValue: " + exitVal);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	public void receiveCommand()
	{
	    try 
	    {
	      ServerSocket server = new ServerSocket(port);
	      System.out.println(new Date().toLocaleString()+"启动命令监听服务，端口："+port);
	      while(true)
	      {
				try
				{//阻塞等待命令请求
					Socket socket = server.accept();
					ServerThread receiveThread = new ServerThread();
					receiveThread.setSocket(socket);
					receiveThread.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	public void startAppMointor()
	{
		ZJPFileMonitor m = new ZJPFileMonitor(ZJPFileMonitor.interval.longValue());
        m.monitor(ZJPFileMonitor.listeningPath,new ZJPFileListener());  
        try {
			m.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("应用异常监控程序启动异常，请检查。");
		}
	}
	class ServerThread extends Thread
	{
		Socket socket=null;
		public Socket getSocket() {
			return socket;
		}

		public void setSocket(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			Reader reader =null;
			try
			{
			   	  reader = new InputStreamReader(socket.getInputStream());
			      char chars[] = new char[1024];
			      int len;
			      StringBuilder builder = new StringBuilder();
			      while ((len=reader.read(chars)) != -1)
			      {
			        builder.append(new String(chars, 0, len));
			      }
			      String receiveMessage=builder.toString().trim();
			      if(!"".equals(receiveMessage))
				  {
			    	  String content[]=receiveMessage.split(":");
			    	  String action=content[0];
			    	  String params=content[1];
			    	  if("restartStorageServer".startsWith(action))
			    	  {
			    		  String topic=params;
			    		  restartService(topic);
			    	  }
				  }
			      System.out.println("接收到客户端命令请求: " + receiveMessage);
			   } catch (Exception e) {
		      e.printStackTrace();
		    }finally
		    {
			      try 
			      {
			    	  reader.close();
					  socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		}
		
	}
	public static void main(String args[])
	{
		CommandUtil commandUtil=new CommandUtil();
		if(args!=null&&args.length>0)
		{
			String port=args[0];
			if(port!=null)
			{
				commandUtil.setPort(Integer.parseInt(port));
			}
		}
		commandUtil.startAppMointor();
		commandUtil.receiveCommand();
	}
}
