package com.ccthanking.framework.log;

import org.apache.log4j.Logger;

public class log {
	
	/*
	 * 获取Logger对象
	 */
	public static Logger getLogger(String classname){
		return Logger.getLogger(classname);
	}
	
	public static Logger getLogger(Class classobj){
		return Logger.getLogger(classobj);
	}
	
}
