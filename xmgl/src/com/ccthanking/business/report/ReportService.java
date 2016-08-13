package com.ccthanking.business.report;

import javax.servlet.http.HttpServletRequest;

public interface ReportService {
	//查询报表信息
	String queryReport(HttpServletRequest request) throws Exception;
	//查询报表信息
	String monitor(HttpServletRequest request,String json) throws Exception;
	//部门监控
	String bm_monitor(HttpServletRequest request,String json) throws Exception;
}
