package com.ccthanking.business.xdxmk.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;

/**
 * @author sunjl
 * @dateTime 2013-7-23
 */
public interface XdxmkService {

	String insert(String json, User user,HttpServletRequest request) throws Exception;

	String query(String json,User user,String ids, HttpServletRequest request) throws Exception;
	String queryXx(String json,User user, HttpServletRequest request) throws Exception;
	
	String query_bd(String json,User user, HttpServletRequest request) throws Exception;
	
	String query_bdxj(String json,User user, HttpServletRequest request) throws Exception;
	
	String query_countbd(String json,User user, HttpServletRequest request) throws Exception;

	String insertJhAxm(String json, User user,String ids,String ywid) throws Exception;

	String queryById(String json, User user) throws Exception;

	String insertJhApc(String json, HttpServletRequest request) throws Exception;
	
	String insertDjz(String json, User user,String ids) throws Exception;
	
	String queryDjz(String json,User user) throws Exception;
	
	List<autocomplete> xmmcAutoCompleteToXmxdk(autocomplete json,User user)  throws Exception;
	
	String queryMaxPch(String xflx,String nd,User user) throws Exception;
	
	String queryHTSJ(String json, User user, Map map) throws Exception;
	
	String queryHtSum(String json,User user,Map params) throws Exception;
	
	String queryJhpc(String json, HttpServletRequest request) throws Exception;
	
	String insertJhsp(String json, HttpServletRequest request) throws Exception;
	
	String queryXmxxByPch(String json, HttpServletRequest request) throws Exception;
	
	String insertXmxdkSp(String json, HttpServletRequest request) throws Exception;
	
	String deleteXdkxmSp(String json, HttpServletRequest request) throws Exception;
	
	String queryXmxxSp(String json, HttpServletRequest request) throws Exception;
	
	String insertXmxdSpAxm(String json, HttpServletRequest request) throws Exception;
	
	String queryApc(String json,HttpServletRequest request) throws Exception;
	
	String verificationXmnd(String json,HttpServletRequest request) throws Exception;
	
	String xmSptg(String json,HttpServletRequest request) throws Exception;

	String deleteSp(String msg, HttpServletRequest request);
}
