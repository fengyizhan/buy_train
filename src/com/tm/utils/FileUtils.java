package com.tm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class FileUtils {
	
	public static void main(String[] args) {
		String path_in = "C:/Users/chen/Downloads/i_20121106_VGOP2-R2.10-97206_00_001.dat";
		long begin = new Date().getTime();
		FileUtils.readDat(new File(path_in));
        System.out.println("运行时间为："+ (new Date().getTime() - begin));
	}

	/**
	 * 根据€符号拆分
	 * new String( new byte[] { (byte) 0x80 })
	 * @param file
	 * @return
	 */
	public static List<String> readDat(File file){
		List<String> list = new ArrayList<String>();
		if (file.exists() && file.isFile()){
			BufferedReader read = null;
			try {
				read = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
				String temp = null;
				while ((temp = read.readLine()) != null) {
					list.add(temp);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try { 
					if(read != null){
						read.close(); 
					}
				} catch (IOException ignore) {}
			}
		}
		return list;
	}
	/**
	 * 获取文件名（不包含扩展名）
	 * @param fileName
	 * @return
	 */
	public static String getFilePrefix(String fileName){
		int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex);
	}
	/**
	 * 获取文件扩展名
	 * @param fileName
	 * @return
	 */
	public static String getFileSufix(String fileName){
		int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
	}
	/**
	 * 文件复制
	 * @param inputFile
	 * @param outputFile
	 * @throws FileNotFoundException
	 */
	public static void copyFile(String inputFile,String outputFile) throws FileNotFoundException{
		File sFile = new File(inputFile);
		File tFile = new File(outputFile);
		FileInputStream fis = new FileInputStream(sFile);
		FileOutputStream fos = new FileOutputStream(tFile);
		int temp = 0;  
        try {  
			while ((temp = fis.read()) != -1) {  
			    fos.write(temp);  
			}
        } catch (IOException e) {  
           throw new RuntimeException("文件读写错误",e);
        } finally{
            try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        } 
	}
	/**
	 * 获取文件的编码格式
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String getEncoding(String file) throws Exception{
		InputStream inputStream = new FileInputStream(file);
		byte[] head = new byte[3];  
        inputStream.read(head);   
        String code = "";  
        code = "gb2312";  
        if (head[0] == -1 && head[1] == -2 ){
            code = "UTF-16";
        }
        if (head[0] == -2 && head[1] == -1 ){ 
            code = "Unicode"; 
        }  
        if(head[0]==-17 && head[1]==-69 && head[2] ==-65){  
            code = "UTF-8";  
        }
        inputStream.close();
        return code;
	}
	/**
	 * 将文件转换成指定编码格式
	 * @param fileName		文件地址
	 * @param encoding		编码
	 * @return				转换过后的文件地址，如果TXT文件为UTF-8格式，则返回fileName
	 * @throws Exception 
	 */
	public static String convertEncoding(String fileName, String encoding) throws Exception {
		if(StringUtils.isBlank(fileName)){
			throw new Exception("文件地址不能为空");
		}
		if(StringUtils.isBlank(encoding)){
			throw new Exception("文件编码不能为空");
		}
		if(!fileName.toLowerCase().endsWith(".txt")){
			throw new Exception("请转换TXT文件");
		}
		File inFile = new File(fileName);
		if(!inFile.exists()){
			throw new Exception("要转换文件不存在");
		}
		String encod = getEncoding(fileName);
		System.out.println("要转换的文件编码格式为：" + encod);
		String outName = fileName;
		if(!encod.toLowerCase().equals("utf-8")){
			System.out.println("TXT编码格式转换开始...");
        	outName = FileUtils.getFilePrefix(fileName) + "_temp.txt";
        	File outFile = new File(outName);
        	FileInputStream fi = new FileInputStream(fileName);
            InputStreamReader ir = new InputStreamReader(fi, getEncoding(fileName));
            FileOutputStream fo = new FileOutputStream(outFile);
            OutputStreamWriter or = new OutputStreamWriter(fo,encoding);
            int i = 0;
            System.out.println("开始读取");
            while((i = ir.read()) != -1){
            	or.write(i);
            }
            System.out.println("写入完成");
            fi.close();
            ir.close();
            or.close();
            fo.close();
            System.out.println("TXT格式转换完成。");
		}
		return outName;
    }
}
