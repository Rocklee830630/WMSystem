package com.ccthanking.business.bdhf.bdwh;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;

public interface BdwhService {
	String queryList(String json,User user) throws Exception;//普通查询
	String querydemo_bd(String json,String xmid,User user) throws Exception;//点击项目列表查询标段
	String insertdemo(String json,User user,String flag,HttpServletRequest request) throws Exception ;//维护插入
	String update_bdbd(String json,HttpServletRequest request) throws Exception;//标段表单维护
	String queryBdxxByBdid(String json,HttpServletRequest request) throws Exception;//根据标段ID查询标段信息
}
