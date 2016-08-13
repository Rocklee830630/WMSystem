package com.ccthanking.business.xmglgs.xxjd.service;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;

/**
 * @author sunjl
 * @dateTime 2013-10-9
 */
public interface XxjdService {

	String query(String json,HttpServletRequest request) throws Exception;
	
	String insert(String json, HttpServletRequest request) throws Exception;
	
	String queryJhbzByJhid(String json,HttpServletRequest request) throws Exception;
	
	String queryByJhid(String json,HttpServletRequest request) throws Exception;
	
	String insertJhbz(String json, HttpServletRequest request) throws Exception;
	
	String queryJhbzByJhfkid(String json,HttpServletRequest request) throws Exception;
	
	String insertJhfk(String json, HttpServletRequest request) throws Exception;
	
	String queryZdyjdByJhbzid(String json,HttpServletRequest request) throws Exception;
	
	String delete(HttpServletRequest request,String json) throws Exception;
	
	String ggtById(String id,User user);
}
