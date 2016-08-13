package com.ccthanking.business.xmglgs.xmxxgl.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sunjl
 * @dateTime 2013-8-7
 */
public interface XmxxService {

	String query(String json,HttpServletRequest request) throws Exception;
	
	String insert(String json, HttpServletRequest request) throws Exception;
	
	String queryBdById(String json,HttpServletRequest request) throws Exception;
	
	String insertBdxx(String json, HttpServletRequest request) throws Exception;
	
	String queryXdkById(String json,HttpServletRequest request) throws Exception;
}
