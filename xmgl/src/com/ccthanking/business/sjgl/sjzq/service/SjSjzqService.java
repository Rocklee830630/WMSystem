package com.ccthanking.business.sjgl.sjzq.service;

import javax.servlet.http.HttpServletRequest;

public interface SjSjzqService {
/*	public String singleHead(HttpServletRequest request) throws Exception;
	public String doubleHead(HttpServletRequest request) throws Exception;*/
	String getValue(HttpServletRequest request,String json) throws Exception;
	String getTable(HttpServletRequest request) throws Exception;
	
	String jcjcValue(HttpServletRequest request,String json) throws Exception;
	String sjbgValue(HttpServletRequest request,String json) throws Exception;
	String queryDrillingDataZh(HttpServletRequest request,String json) throws Exception;
	String queryDrillingDataLbj(HttpServletRequest request,String json) throws Exception;
	String queryDrillingDataJjg(HttpServletRequest request,String json) throws Exception;
	String queryDrillingDataJcjc(HttpServletRequest request,String json) throws Exception;
}
