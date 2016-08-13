package com.ccthanking.business.dtgk;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;


public interface DtgkService {
	
	// 主任界面-动态管控，组合查询统计
	String query(String json, User user, HttpServletRequest request) throws SQLException;
	// 主任界面-动态管控，整体概况（文字描述）
	String ztgkRs(String json, User user) throws SQLException;
	// 主任界面-动态管控，整体进展（几字型统计）
	String ztjzRs(String json, User user) throws SQLException;
	// 主任界面-动态管控，新建续建
	String xjxujRs(String json, User user) throws SQLException;
	
	String queryXx(String json) throws SQLException;
	String queryXmbdxx(String json,String proSql) throws SQLException;
}
