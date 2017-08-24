package com.tm.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class IPUtils {
	
	private static final String UNKNOWN = "unknown";
	private static final String LOCALHOST = "0:0:0:0:0:0:0:1";
	/**
	 * 根据请求获取客户端真实ip
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		if(StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
			ip = request.getHeader("HTTP_CLIENT_IP");		
		}
		if(StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
			ip = request.getHeader("X_FORWARDED_FOR");		
		}
		if(StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		if(LOCALHOST.equals(ip)){
			return "127.0.0.1";
		}else{
			int index = ip.indexOf(" ");
			if(index >= 0){
				ip = ip.substring(0,index);
			}
			return ip;
		}
	}
	
}
