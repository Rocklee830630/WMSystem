package com.ccthanking.business.xdxmk.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sunjl
 * @dateTime 2013-7-23
 */
public interface XmscService {
	
	String queryJdzx(String json,HttpServletRequest request) throws Exception;
	
	String queryXmwgById(String json,HttpServletRequest request) throws Exception;
	
	String queryJdjhjzx(String json,HttpServletRequest request) throws Exception;
}
