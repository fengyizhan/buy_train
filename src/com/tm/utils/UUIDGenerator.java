package com.tm.utils;

import java.util.UUID;

import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * UUID主键生成器
 * @author chen
 *
 */
public class UUIDGenerator {

	private UUIDGenerator() {
		super();
	}
	
	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static void main(String[] args){ 
        StandardPasswordEncoder encoder = new org.springframework.security.crypto.password.StandardPasswordEncoder();
        
        String p1 = encoder.encode("xny119");
        System.out.println(p1);
        
        System.out.println(encoder.matches("xny119", p1));
    } 

}
