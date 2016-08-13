package com.ccthanking.business.zjb.sjgl;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface ShenJiGuanLiService  {

	String querysjList(String json,User user) throws Exception;

	String queryJS(String msg, User user,HttpServletRequest request) throws Exception;

	String insert_sj(HttpServletRequest request, String msg ) throws Exception;

	String update_js(HttpServletRequest request, String msg)throws Exception;

	String querysjxx(HttpServletRequest request, User user)throws Exception;

	String update_jss(HttpServletRequest request, String msg)throws Exception;

	String updateJSById(HttpServletRequest request ) throws Exception;



}
