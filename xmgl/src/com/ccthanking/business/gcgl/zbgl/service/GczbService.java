package com.ccthanking.business.gcgl.zbgl.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;

/**
 * @author sunjl
 * @dateTime 2013-8-7
 */
public interface GczbService {

	String query(String json,HttpServletRequest request) throws Exception;//首页查询，查询出最新周报
	String query_zb(String json,User user,String xmid) throws Exception;//确定是否存在周报
	String query_tj(String json,User user,String xmid,String bdid) throws Exception;//周报统计查询，弹出列表查询
	String query_gk(String json,HttpServletRequest request) throws Exception;//项目管理公司概况
	String query_yd(String json,HttpServletRequest request) throws Exception;//月度信息
	String query_bz(String json,User user,String year,String xmglgs) throws Exception;//项目管理公司周报本周完成
	String query_bylj(String json,User user,String time,String deptId) throws Exception;//项目管理公司本月累计完成
	String fzr_ztxx(User user,String nd,String deptId) throws Exception;//项目负责人以及总体概况
	String zgxm_xm_top(User user,String nd,String deptId) throws Exception;//整改最多的项目(TOP5)
	String zgxm_bd_top(User user,String nd,String deptId) throws Exception;////整改最多的标段(TOP5)
	String zbxq_bg(User user,String nd,String deptId) throws Exception;//招标需求表格
	String xmht_bg(User user,String nd,String deptId) throws Exception;//项目合同表格
	String gcb_ztgk(User user,String nd) throws Exception;//工程部总体概况
	String qsxm_xm_top(User user,String nd) throws Exception;//洽商最多的项目数(TOP5)
	String qsxm_bd_top(User user,String nd) throws Exception;//洽商最多的标段数(TOP5)
	String query_ksjs_sj(String json,User user,String kssj,String jssj,String jhsjid) throws Exception;//新增时校验开始时间和结束时间是否重复
	String query_sj(String json,User user,String ksORjs,String kssj,String jssj,String jhsjid,String zbid) throws Exception;//修改时校验开始时间和结束时间是否重复
	String insert(String json, User user,String ywid) throws Exception;//插入周报
	String update_zb(String json, User user,String ywid) throws Exception;//更新周报
	String delete(String json,User user) throws Exception ;//删除周报信息
	List<autocomplete> xmmcAutoComplete(autocomplete json,User user)  throws Exception;//周报管理项目名称自动补全
	List<autocomplete> xmmcAutoComplete_tj(autocomplete json,User user)  throws Exception;//周报统计项目名称自动补全
	String queryZbsj(String json,User user) throws Exception;
	String insertZbsj(String json, User user) throws Exception;
	
}
