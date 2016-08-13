package com.ccthanking.business.gcgl.gcgk.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sunjl
 * @dateTime 2014-1-14
 */
public interface GcgkService {

	String gcb_ztgk(String json,HttpServletRequest request) throws Exception;

	String gcJdglLb(String json, HttpServletRequest request);
	String gcGcjlzfTcgk(String json, HttpServletRequest request);
	String gcGcjlzfZtqk(String json, HttpServletRequest request);
	String gcGcglTcgk(String json, HttpServletRequest request);
	String gcLybzjTcgk(String json, HttpServletRequest request);
	String gcGcsxTcgk(String json, HttpServletRequest request);
	String gcGcqsTxfx(String json, HttpServletRequest request);
	String gcGcqsTcgk(String json, HttpServletRequest request) throws Exception;

	String queryJdgl1(HttpServletRequest request);
	String queryJdgl2(HttpServletRequest request);
	String gcJdglTcgk(String json, HttpServletRequest request);
	
	// 履约保证金列表
	String gcLybzjLb(String json, HttpServletRequest request);
}
