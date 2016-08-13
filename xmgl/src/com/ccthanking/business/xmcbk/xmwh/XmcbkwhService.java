package com.ccthanking.business.xmcbk.xmwh;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;

public interface XmcbkwhService {

	String query_cbk(String json,User user) throws Exception;//普通查询
	String query_cbkxx_ty(String json,User user) throws Exception;//通用详细查询
	String query_xdrq(String json,User user,String pcid) throws Exception;//年度查询
	String query_xmbh(String json,User user,String year) throws Exception;//项目编号
	String query_xmbh_jy(String json,User user,String year,String xmbh,String id) throws Exception;//校验项目编号
	String query_xmpc(String json,User user) throws Exception;//批次查询
	String query_newpch(String json,User user) throws Exception;//查询出最新pch
	String query_cbkxx(String json,User user,String id) throws Exception;//详细信息查询
	String insert_cbk(String json,User user) throws Exception ;//维护插入
	String update_ckb(String json,User user,String id) throws Exception ;//维护修改
	String update_pxh(String json,User user) throws Exception ;//排序号维护修改
	String delete(String json,User user) throws Exception ;//删除未下达项目信息
	List<autocomplete> xmmcAutoComplete(autocomplete json,User user)  throws Exception;//项目名称自动补全
	String queryCbkGz(String json,HttpServletRequest request) throws Exception;//普通查询
}
