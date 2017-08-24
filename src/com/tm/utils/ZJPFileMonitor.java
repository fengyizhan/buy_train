package com.tm.utils;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class ZJPFileMonitor {  
    public static Long interval=2000l;
    public static String listeningPath="C:/dumps";
    FileAlterationMonitor monitor = null;  
    public ZJPFileMonitor(long interval) {  
        try{
        	monitor = new FileAlterationMonitor(interval);
        	File dumpRootDir=new File(listeningPath);
        	if(!dumpRootDir.exists())
        	{
        		dumpRootDir.mkdirs();
        	}
        }catch(Exception e){e.printStackTrace();}
    }  
  
    public void monitor(String path, FileAlterationListener listener) {
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));  
        monitor.addObserver(observer);
        observer.addListener(listener);
    }
    public void stop() throws Exception{  
        monitor.stop();
    }  
    public void start() throws Exception {  
        monitor.start();
    }  
    public static void main(String[] args) throws Exception {  
        ZJPFileMonitor m = new ZJPFileMonitor(2000);
        m.monitor("C:/dumps",new ZJPFileListener());
        m.start();
    }
}
