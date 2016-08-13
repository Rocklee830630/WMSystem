package com.ccthanking.business.gcgl.kfgl.service;

import javax.servlet.http.HttpServletRequest;


/**
 * @author sunjl
 * @dateTime 2013-8-8
 */
public interface KfglService {

	String query(String json,HttpServletRequest request) throws Exception;
	
	String queryXdxmk(String json,HttpServletRequest request) throws Exception;
	
	String insert(String json, HttpServletRequest request) throws Exception;
	
	String queryKfgById(String json,HttpServletRequest request) throws Exception;
	
	String update(String json, HttpServletRequest request) throws Exception;
	
	String insertZt(String json, HttpServletRequest request) throws Exception;
	
	String queryKfglByXm(String json,HttpServletRequest request) throws Exception;
	
	String delete(HttpServletRequest request,String json) throws Exception;

}
