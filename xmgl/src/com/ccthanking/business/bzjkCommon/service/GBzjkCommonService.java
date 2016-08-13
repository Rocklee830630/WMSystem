package com.ccthanking.business.bzjkCommon.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhaiyl
 * @date 2014-12-15
 */
public interface GBzjkCommonService {

	String queryList_Kfg(HttpServletRequest request, String msg);

	String querytcjhgtt(HttpServletRequest request, String msg);

	String queryLybzjList(HttpServletRequest request, String msg);

}
