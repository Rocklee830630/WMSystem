package com.ccthanking.business.ztb.bmjk.service;

import javax.servlet.http.HttpServletRequest;

public interface ZtbBmjkService {
	//查询表单信息
	String queryZtbFormInfo(HttpServletRequest request) throws Exception;
	//查询表格信息
	String queryZtbTableInfo(HttpServletRequest request) throws Exception;
	//招投标需求--数据钻取
	String queryDrillingDataZbxqzh(HttpServletRequest request,String json) throws Exception;
	
}
