package com.ccthanking.business.xtbg.ndxxgx;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;

public interface NdxxgxService  {

	String deleteNdxxgx(String msg, User user)throws Exception;

	String updateNdxxgx(String msg, User user)throws Exception;

	String insertNdxxgx(String msg, User user, String ywid)throws Exception;

	String queryNdxxgx(String msg, User user) throws Exception;
	
	public String queryMoreNdxxgx(String json,HttpServletRequest request) throws Exception;
}
