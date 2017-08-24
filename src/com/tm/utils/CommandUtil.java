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
 * �빫������������һ��ġ����Ź���
 * ���������������س���
 * @author fyz
 */
public class CommandUtil
{
//	static JafkaManager jafkaManager=new JafkaManager();
	public static String COMMAND_RestartStorageServer="restartStorageServer";
	/**
	 * ������ȡ����ִ�н��
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
		 * ����ÿһ�е��߼�
		 * @param line ��
		 */
		public void processLine(String line)
		{
			
		}
		/**
		 * ����ִ�к���߼�
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
	 * Ĭ�������������˿�
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
			System.out.println(new Date().toLocaleString()+"ִ����� " + commond);
			Process proc = rt.exec(commond);
			// ���� output���
			StreamGobbler outputGobbler = new StreamGobbler(
					proc.getInputStream(), businessType);
			outputGobbler.start();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * �رղ�������ط���
	 * @param targetExe Ŀ������exe�ļ���Ϣ��֧��*����tongxun188.exe����tongxun*.exe
	 */
	public static void restartService(String targetExe)
	{
		restartService(targetExe,true,2);
	}
	/**
	 * �ر���ط���
	 * @param targetExe Ŀ������exe�ļ���Ϣ��֧��*����tongxun188.exe����tongxun*.exe
	 * @param needReboot �Ƿ���Ҫ��������
	 * @param latency ��������ִ���ӳ�
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
						System.out.println("û�з�����Ŀ�꡾"+targetExe+"��ƥ������г��򣬳���C����Ѱ�ҡ�");
						lines.add(new String[]{"",""});
					}
					File executableBatFile=new File("C:/"+targetExe+".bat");
					if(!executableBatFile.exists())
					{
						System.out.println("Ŀ�꡾"+targetExe+"����C���²����������ű������顣");
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
								System.out.println(new Date().toLocaleString()+" killStorageServer�� " + killCommand);
								Runtime kill_rt = Runtime.getRuntime();
								Process kill_process=kill_rt.exec(killCommand);
								InputReader kill_inputReader=new InputReader();
								kill_inputReader.read(kill_process.getInputStream());
							}
							/*
							//�ر�jafka Server����
							System.out.println(new Date().toLocaleString()+" stopJafkaServer");
							Runtime stop_jafka = Runtime.getRuntime();
							Process stop_jafka_process=stop_jafka.exec("cmd /c net stop jafka");
							InputReader stop_jafka_inputReader=new InputReader();
							stop_jafka_inputReader.read(stop_jafka_process.getInputStream());
							
							//����jafka Server����
							System.out.println(new Date().toLocaleString()+" startJafkaServer");
							Runtime start_jafka = Runtime.getRuntime();
							Process start_jafka_process=start_jafka.exec("cmd /c net start jafka");
							InputReader start_jafka_inputReader=new InputReader();
							start_jafka_inputReader.read(start_jafka_process.getInputStream());
							*/
							
							//�޸�������topic��
//							String topicName=executableFile.getName().substring(0,executableFile.getName().indexOf("."));
//							System.out.println(new Date().toLocaleString()+" fixed problem topic["+topicName+"] data start");
//							jafkaManager.createNewTopicAndCopyMessage(topicName);
//							System.out.println(new Date().toLocaleString()+" fixed problem topic["+topicName+"] data end");
							
							//����������
							System.out.println(new Date().toLocaleString()+" startStorageServer");
							Desktop.getDesktop().open(executableBatFile);
						
						}
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		//����Ŀ������������Ѱ�ҵ�����ġ�ִ��·������������ID�������ں�������֮��
//		String command="cmd /c wmic process get executablepath,processid/format:csv|findstr \""+targetExe+"\"";
		String command="cmd /c jps -mlv|findstr \""+targetExe+"\"";
		try
		{
			Runtime rt = Runtime.getRuntime();
			System.out.println(new Date().toLocaleString()+"ִ����� " + command);
			Process proc = rt.exec(command);
			// ���� output���
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(),"restartStorageServer");
			outputGobbler.start();
//			// ����ִ���쳣
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
	      System.out.println(new Date().toLocaleString()+"��������������񣬶˿ڣ�"+port);
	      while(true)
	      {
				try
				{//�����ȴ���������
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
			System.out.println("Ӧ���쳣��س��������쳣�����顣");
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
			      System.out.println("���յ��ͻ�����������: " + receiveMessage);
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
