package com.ccthanking.business.xdxmk.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sunjl
 * @dateTime 2013-7-23
 */
public interface JhbmjkService {
	
	String queryJdzx(String json,HttpServletRequest request) throws Exception;
	String query_lxxmps(String json,HttpServletRequest request) throws Exception;
	String query_ybxms(String json,HttpServletRequest request) throws Exception;
	String query_wc(String json,HttpServletRequest request) throws Exception;
	String query_wbz(String json,HttpServletRequest request) throws Exception;
	String query_wtqk(String json,HttpServletRequest request) throws Exception;
	String query_wtqk_qt(String json,HttpServletRequest request) throws Exception;
	
}
