package com.ccthanking.business.tcjh.jhgl.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;

/**
 * @author sunjl
 * @dateTime 2013-7-31
 */
public interface TcjhService {

	String insertBgbb(String json, User user, String xmbhs) throws Exception;

	String query(String json,User user) throws Exception;

	String xdtcjhByPc(String json, HttpServletRequest request) throws Exception;
	String xdtcjhByXm(String json, HttpServletRequest request) throws Exception;
	String queryMoreXiangMu(String json, HttpServletRequest request) throws Exception;

	String queryById(String json, User user) throws Exception;

	String queryXfjh(String json,User user) throws Exception;

	String ggtById(String json,User user) throws Exception;
	
	String ggtById_xxjd(String json,User user) throws Exception;

	String queryBgxx(String json,User user) throws Exception;

	String queryBgxm(String json,User user) throws Exception;

	String updatebatchdata(String json,User user,String ywid,String jhid,String bgsm,String tids) throws Exception;
	
	List<autocomplete> xmmcAutoComplete(autocomplete json,User user)  throws Exception;
	
	String updatebatchdataNobg(String json,User user,String jhid) throws Exception;
	
	String queryJh(String json,User user) throws Exception;
	
	List<autocomplete> xmmcAutoQueryByXmglgs(autocomplete json,HttpServletRequest request)  throws Exception;
	
	String deleteJh(HttpServletRequest request, User user);
}
