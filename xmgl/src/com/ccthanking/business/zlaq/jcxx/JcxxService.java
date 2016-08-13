package com.ccthanking.business.zlaq.jcxx;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;

public interface JcxxService {
	
	String query_jc(String json,User user,String flag,String zt) throws Exception;//普通查询
	String query_jc_xxk(String json,User user,String jclx,String id) throws Exception;//信息卡列表查询
	String jc_zg_xxtj(User user,String nd) throws Exception;//质量安全部概况检查整改信息监控
	String zlaqqk(User user,String nd) throws Exception;//各项目公司质量安全情况
	String sgdw_top(User user,String nd) throws Exception;//涉及整改最多的施工单位(TOP5)
	String jldw_top(User user,String nd) throws Exception;//涉及整改最多的监理单位(TOP5)
	String query_jcbh(String jclx,String jcrq,String jc) throws Exception;//生成检查编号
	String insert_jc(String json,User user,String ywid,String flag) throws Exception ;//维护插入
	String update_jc(String json,User user,String flag,String ywid) throws Exception ;//维护修改
	String delete_jc(String json,User user) throws Exception ;//删除检查信息
	String bt_zljc(User user,String id);//质量检查饼图
	String bt_aqjc(User user,String id);//安全检查饼图
	List<autocomplete> xmmcAutoComplete(autocomplete json,User user)  throws Exception;//项目名称自动补全
	String queryById(String json,HttpServletRequest request) throws Exception;//根据ID查询信息
}
