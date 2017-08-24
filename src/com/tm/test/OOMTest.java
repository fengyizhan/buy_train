package com.tm.test;

public class OOMTest {

	public static void main(String[] args) {
		String s="adfsfdsfdsfsdfdsfdsfdsfsdfdsf";
		for(;;)
		{
			s=s+s;
		}

	}

}
