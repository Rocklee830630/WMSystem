package com.ccthanking.business.jhfk.service;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;


public interface JhfkService {
	public String doFkjh(HttpServletRequest request, String json) throws Exception;
	public String doFkjhBatch(HttpServletRequest request,String jhsjid,Connection conn)throws Exception;
	public String queryFormInfo(HttpServletRequest request, String json)throws Exception;
	//获取计划反馈次数
	String getJhfkCounts(HttpServletRequest request,String json) throws Exception;
	String queryTabList(HttpServletRequest request,String json) throws Exception;
	//获取是否需要反馈的标志位
	String getFkFlag(HttpServletRequest request, String json) throws Exception;
	//获取业务表的反馈日期
	String getFkFkrq(HttpServletRequest request, String json) throws Exception;
	//获取计划数据表的计划日期
	String getFkJhrq(HttpServletRequest request, String json) throws Exception;
	String queryXMFkxx(HttpServletRequest request, String json) throws Exception;
	//获取项目信息
	String queryXmxx(HttpServletRequest request, String json) throws Exception;
}
