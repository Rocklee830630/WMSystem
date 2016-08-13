package com.ccthanking.business.gcgl.cjdw.service;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;


/**
 * @author sunjl
 * @dateTime 2013-8-8
 */
public interface CjdwService {

	String query(String json,HttpServletRequest request) throws Exception;
	
	String insert(String json, HttpServletRequest request) throws Exception;

	String queryByMingChen(HttpServletRequest request);
	
	String queryZtbList(String json,HttpServletRequest request) throws Exception;

	String queryXMXXList(String msg, HttpServletRequest request);

	String deleteCJDW(String msg, User user);

}
