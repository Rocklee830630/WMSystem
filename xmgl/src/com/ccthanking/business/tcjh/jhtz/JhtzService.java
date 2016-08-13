package com.ccthanking.business.tcjh.jhtz;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;

/**
 * @author xiahongbo
 * @date 2014-8-8
 */
public interface JhtzService {

	String saveOrUpdate(HttpServletRequest request, User user, String json);
	
	String query(HttpServletRequest request, User user, String json);
	
	String queryById(HttpServletRequest request, User user);
}
