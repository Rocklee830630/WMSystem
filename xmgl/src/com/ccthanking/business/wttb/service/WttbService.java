package com.ccthanking.business.wttb.service;


import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;

public interface WttbService{
	//插入信息主表
	String insertInfo(String json,User user) throws Exception;
	
	String querySomeData(String id,User user,PageManager page) throws Exception;
	//根据问题ID查询事件表
	String queryPsnList(String json,User user,PageManager page) throws Exception;
	/**
	 * 退回 方法
	 * @param json
	 * @param user
	 * @return
	 * @throws Exception
	 */
	String doSendBack(HttpServletRequest request,String json) throws Exception ;
	/**
	 * 撤销方法
	 * @param json
	 * @param user
	 * @return
	 * @throws Exception
	 */
	String doRevoke(String json, User user) throws Exception;
	
	String doSugg(HttpServletRequest request,String json) throws Exception;
	
	String doConfirm(HttpServletRequest request,String json) throws Exception;
	//催办
	String doUrge(HttpServletRequest request,String json) throws Exception;
	
	String doSupplySugg(String json, User user) throws Exception;
	//转发
	String doTransfer(HttpServletRequest request,String json) throws Exception;
	//确认提报
	String doConfirmSend(HttpServletRequest request,String json) throws Exception;
	
	
	//发起人查询信息主表
	String queryInfoFqr(String json,HttpServletRequest request) throws Exception;
	//接收人查询信息主表
	String queryInfoJsr(String json,HttpServletRequest request) throws Exception;
	//发起人查询信息主表
	String queryInfoBlr(String json,HttpServletRequest request) throws Exception;
	//查询已处理的问题
	 String queryInfoYcl(String json, HttpServletRequest request) throws Exception;
	//发送问题提报
	String sendReport(HttpServletRequest request,String json) throws Exception;
	//重新发送
	String doResend(HttpServletRequest request,String json) throws Exception;
	//查询流转历史
	String queryLzls(HttpServletRequest request,String json) throws Exception;
	//查询流转历史
	String queryPfqk(HttpServletRequest request,String json) throws Exception;
	
	String getColor(HttpServletRequest request,String json) throws Exception;
	//确认提报
	String queryInfoByWtid(HttpServletRequest request,String json) throws Exception;
	//查询部门领导
	String queryLeader(HttpServletRequest request,String json) throws Exception;
	String queryLeaderFlag(HttpServletRequest request, String json) throws Exception;
	//查询当前登录人对选中问题的办理人角色
	String queryBlrjs(HttpServletRequest request,String json) throws Exception;
	//查询当前登录人需要办理的问题的数量
	String getWtCounts(HttpServletRequest request,String json) throws Exception;
	//查询当前登录人需要办理的问题的列表
	String getWtList(HttpServletRequest request,String json) throws Exception;
	//问题提报删除功能
	String deleteWttb(HttpServletRequest request, String json) throws Exception;
	//问题提报发送到会议中心
	public String doSendToHyzx(String json, HttpServletRequest request) throws Exception;
	public String getAccepter(String json, HttpServletRequest request) throws Exception;
	
}
