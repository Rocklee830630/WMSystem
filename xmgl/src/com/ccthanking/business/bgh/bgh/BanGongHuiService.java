package com.ccthanking.business.bgh.bgh;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

public interface BanGongHuiService  {

	String querybanGongHuiHuiCi(HttpServletRequest request) throws Exception;

	String querybanGongHuiList(HttpServletRequest request, String json);

	String insertBanGongHui(String msg, HttpServletRequest request);

	String updateBanGongHui(String msg, HttpServletRequest request);

	String queryHyj(HttpServletRequest request);

	String queryZXHC(HttpServletRequest request);

	String queryBGHWT(HttpServletRequest request);
	
	String delete(HttpServletRequest request);
	
	String queryDshyt(HttpServletRequest request,String json) throws Exception;
}
