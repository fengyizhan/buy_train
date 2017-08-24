package com.tm.utils;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class ZJPFileListener implements FileAlterationListener {
	@Override
	public void onStart(FileAlterationObserver observer) {
//		System.out.println("onStart");
	}

	@Override
	public void onDirectoryCreate(File directory) {
		System.out.println(new Date().toLocaleString()+" onDirectoryCreate:" + directory.getName());
	}

	@Override
	public void onDirectoryChange(File directory) {
		System.out.println(new Date().toLocaleString()+" onDirectoryChange:" + directory.getName());
	}

	@Override
	public void onDirectoryDelete(File directory) {
		System.out.println(new Date().toLocaleString()+" onDirectoryDelete:" + directory.getName());
	}

	@Override
	public void onFileCreate(File file) {
		System.out.println(new Date().toLocaleString()+" onFileCreate:" + file.getName());
		if(file.isFile()&&file.getName().lastIndexOf(".hprof")>0)
		{
			File parentDir=file.getParentFile();
			String fileIdentify=parentDir.getName();
			if("dumps".equals(fileIdentify))
			{//忽略主目录变化
				return;
			}
			System.out.println(new Date().toLocaleString()+" 应用【"+fileIdentify+"】dump文件创建："+file.getName());
			CommandClient commandClient=new CommandClient();
			commandClient.fixedProblemTopicCommand(fileIdentify);
			
			String time = DateUtils.dateToStr(new Date(),
					DateUtils.YYYY_MM_DD_HH_MM_SS);
			String message = time + "，应用程序发生OOM异常，dump文件所在目录："
					+ file.getAbsolutePath() + "。";
			String localIp=KafkaManager.getLocalIp();
			Long listenPort=Long.parseLong(fileIdentify.replaceAll("[a-zA-Z]",""));
			try {
				String path = KafkaManager.STORAGE_ALL + "/"
						+ localIp + "-"
						+ listenPort.longValue();
				KafkaManager.getInstance().getZkClient().writeData(path, message.getBytes("UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*
			try {
				DefaultEmailSender mail = new DefaultEmailSender();
				StringBuffer mailContent=new StringBuffer();
				mailContent.append("服务器【"+localIp+"】").append(message).append("请及时关注并处理");
				mail.send(mail.oomInit(mailContent));
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			try{
				String url=KafkaManager.messageUrl;
				String param=KafkaManager.oommessageParam;
				String ip=localIp.replaceAll("\\.","_");
				String params=param+"&name="+ip+"&port="+listenPort.longValue();
				HttpRequest.sendPost(url,params);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onFileChange(File file) {
		System.out.println(new Date().toLocaleString()+" onFileCreate : " + file.getName());
	}

	@Override
	public void onFileDelete(File file) {
		System.out.println(new Date().toLocaleString()+" onFileDelete :" + file.getName());
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
//		System.out.println("onStop");
	}

}
