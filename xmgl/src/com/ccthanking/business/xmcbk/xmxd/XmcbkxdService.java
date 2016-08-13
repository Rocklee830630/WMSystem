package com.ccthanking.business.xmcbk.xmxd;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;


public  interface XmcbkxdService{
	/**
	 * @author Quinn He
	 * @dateTime 2012-6-8 下午3:17:28
	 * @param username
	 * @param password
	 * @return me.gall.quinn.model.User
	 * @throws Exception 
	 */
	String query_jzxd(String json,User user,String ids) throws Exception;//普通查询
	String query_maxpch(String json,User user,String xdlx,String xdnf) throws Exception;//最大pch查询	
	String query_zc(String json,User user) throws Exception;//暂存查询
	String query_sp_info(String json,User user) throws Exception;//审批信息询
	String query_sp(String json,User user,String spxxid) throws Exception;//审批项目查询
	String update_spzt(String json,User user,String spzt) throws Exception;//更新审批状态
	String insert_sp(String json,User user,String ids,String spxxid) throws Exception ;//审批项目插入
	String insert_sp_info(String json,User user) throws Exception ;//审批信息插入
	String insert_xd(String json,User user,String ids,HttpServletRequest request) throws Exception ;//下达插入
	String insert_zc(String json,User user,String ids,String dczlx) throws Exception ;//暂存插入
	String insert_jz(String json,User user,String ids,String year) throws Exception;//结转插入
	String update_sp_info(String json,User user) throws Exception;//审批信息修改
	String delete_sp(String json,User user,String cbkid) throws Exception;//删除审批项目
	String xmSptg(String json,HttpServletRequest request) throws Exception;//通过
	String verificationXmnd(String json,HttpServletRequest request) throws Exception;//验证项目年度是否统一
	String deleteSp(String msg, HttpServletRequest request) throws Exception;;
}
