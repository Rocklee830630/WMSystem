package com.ccthanking.business.bzjkCommon.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhaiyl
 * @date 2014-12-15
 */
public interface SjbzjkCommonService {

	/**
	 * 设计部监控-设计管理的详细页面统计
	 * @param request
	 * @param msg
	 * @return
	 */
	String querySjglList(HttpServletRequest request, String msg);
	
	/**
	 * 设计部监控-设计管理的详细页面统计
	 * @param request
	 * @param msg
	 * @return
	 */
	String querySjJJGglList(HttpServletRequest request, String msg);

}
