package com.ccthanking.common;

/**
 * 各部长链接路径变量
 * 
 * @author hongpeng.dong
 */
public  class BzjkCommon {

    public static String luJing (String mc){
    	// 設計部properties add by xiahongbo 2014-10-23
    	if("SJ".equals(mc)) {
    		return "com.ccthanking.properties.business.bzjklj.sj_xx";
    	}
    	if("ZJB".equals(mc)){
    		return "com.ccthanking.properties.business.bzjklj.zjb_xx";
    	}
    	if("ZTB".equals(mc)){
    		return "com.ccthanking.properties.business.bzjklj.ztb_xx";
    	}
    	if("GCB".equals(mc)){
    		return "com.ccthanking.properties.business.bzjklj.gcb_xx";
    	}
    	if("JH".equals(mc)){
    		return "com.ccthanking.properties.business.bzjklj.jh_xx";
    	}
    	if("COMMON".equals(mc)){
    		return "com.ccthanking.properties.business.bzjklj.common_xx";
    	}
    	else{
    		return "";
    	}
    	
    }
    
}
