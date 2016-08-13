package com.ccthanking.business.gcgl.gcsx.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;


/**
 * @author sunjl
 * @dateTime 2013-8-8
 */
public interface GcsxService {

	String query(String json, HttpServletRequest request) throws Exception;
	
	String insert(String json, HttpServletRequest request) throws Exception;
	
	List<autocomplete> xmmcAutoCompleteByYw(autocomplete json,User user)  throws Exception;
	
	String delete(HttpServletRequest request,String json) throws Exception;

}
