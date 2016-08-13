package com.ccthanking.business.xdxmk.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sunjl
 * @dateTime 2013-12-25
 */
public interface SjzqService {
	
	String query_pcxx(String json,HttpServletRequest request) throws Exception;
	
	String query_tcjh(String json,HttpServletRequest request) throws Exception;
	
	String query_kfgl(String json,HttpServletRequest request) throws Exception;
	
	String query_xxjd(String json,HttpServletRequest request) throws Exception;
	
	String query_gcqs(String json,HttpServletRequest request) throws Exception;
	
	String query_lybzj(String json,HttpServletRequest request) throws Exception;
	
	String query_tqk(String json,HttpServletRequest request) throws Exception;
	
}
