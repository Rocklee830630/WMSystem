package com.ccthanking.business.bzwd.service;

import javax.servlet.http.HttpServletRequest;


public interface BzwdService {
	String queryWdTree(HttpServletRequest request,String json) throws Exception;

	String insertBzwd(String msg, HttpServletRequest request) throws Exception;

	String updateBzwd(String msg, HttpServletRequest request);

	String queryOneById(HttpServletRequest request);

}
