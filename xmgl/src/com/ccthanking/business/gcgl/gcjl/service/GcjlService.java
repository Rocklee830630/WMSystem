package com.ccthanking.business.gcgl.gcjl.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sunjl
 * @dateTime 2013-8-7
 */
public interface GcjlService {

	String query(String json,HttpServletRequest request) throws Exception;
	
	String insert(String json, HttpServletRequest request) throws Exception;
	
	String queryByTcjhId(String json,HttpServletRequest request) throws Exception;

	String queryMaxMonth(String json,HttpServletRequest request) throws Exception;
	
	String queryTjxx(String json,HttpServletRequest request) throws Exception;
	
	String queryDate(String json,HttpServletRequest request) throws Exception;
	
	String delete(HttpServletRequest request,String json) throws Exception;
}
