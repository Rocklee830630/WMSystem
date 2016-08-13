package com.ccthanking.business.ztb.zbxq;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;

public interface ZhaoBiaoXuQiuService  {

	String queryConditionZhaobiaoxuqiu(HttpServletRequest request,String json) throws Exception;
	String insertdemo(HttpServletRequest request,String msg) throws SQLException, Exception;
	String queryZhaoBiaoXiangMu(String msg, User user);
	String updateZhaobiaoxuqiu(HttpServletRequest request, String msg, User user,String jhsjids) throws Exception;
	String queryMoreXiangMu(String msg, User user) throws Exception;
	String queryConditionZhaotoubiao(HttpServletRequest request,String s) throws Exception;
	String doQdzb(HttpServletRequest request,String s) throws Exception;
	String doSubmit(HttpServletRequest request,String s) throws Exception;
	String deleteZtbxq(HttpServletRequest request,String s) throws Exception;

	String queryConditionZhaobiaoxuqiuNd(HttpServletRequest request,String json) throws Exception;
	String updatebatchdataNobg(String json, User user) throws Exception;
	String getFormValueByID(HttpServletRequest request,String json) throws Exception;
	String getCountZbcs(HttpServletRequest request,String json) throws Exception;
}
