package com.ccthanking.framework.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.Log;
import com.ccthanking.framework.common.User;

/**
 * @auther xhb 
 */
public interface LogService extends BaseService<Log> {
	
	/***
	 * 查询用户的操作日志
	 * @author xhb
	 * @param String json 查询条件和排序Json字符串
	 * @param User user 用户实体Bean
	 * @return String 查询的结果集以json串形式返回
	 */
	String queryLogin(String json, User user) throws SQLException ;
	ResultSet queryExportLogin(String json, User user) throws SQLException ;
	
	/***
	 * 查询用户的操作日志
	 * @author xhb
	 * @param String json 查询条件和排序Json字符串
	 * @param User user 用户实体Bean
	 * @return String 查询的结果集以json串形式返回
	 */
	String queryLogOperation(String json, User user) throws SQLException;
	
	String queryLogDetail(String json,HttpServletRequest request) throws SQLException;
}
