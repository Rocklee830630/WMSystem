package com.ccthanking.business.zjb.jsgl;

import java.sql.SQLException;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface JieSuanService  {

	String queryConditionJieSuan(String json,User user) throws Exception;

	String insertdemo(String msg, User user,String ywid) throws SQLException, Exception;

	String updatedemo(String msg, User user) throws SQLException, Exception;

	String queryConditionHeTong(String msg,User user) throws SQLException;

	String queryhtxx(String msg, User user) throws Exception;

	String queryhtxxzs(String msg, User user) throws Exception;

	String deleteDyqk(String msg, User user);

	String updateHeTongZT(String msg, User user) throws Exception;

}
