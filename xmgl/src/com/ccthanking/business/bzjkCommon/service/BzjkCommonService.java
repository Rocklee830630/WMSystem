package com.ccthanking.business.bzjkCommon.service;

import javax.servlet.http.HttpServletRequest;

public interface BzjkCommonService {

	String queryList(HttpServletRequest request, String msg);
	String queryList_Qq(HttpServletRequest request, String msg);
	// 履约保证金列表
	String queryList_gc_lybzj(HttpServletRequest request, String msg);
	// 工程洽商列表
	String queryList_gc_gcqs(HttpServletRequest request, String msg);
	// 工程甩项列表
	String queryList_gc_gcsx(HttpServletRequest request, String msg);
	String queryGcqsPass(HttpServletRequest request, String msg);
	String querySJBGList(HttpServletRequest request, String msg);
	String queryJHBZList(HttpServletRequest request, String msg);
	String queryJHGZList(HttpServletRequest request, String msg);
	String queryXDKList(HttpServletRequest request, String msg);
	String queryZBXQList(HttpServletRequest request, String msg);
	String queryHtList(HttpServletRequest request, String msg);
}
