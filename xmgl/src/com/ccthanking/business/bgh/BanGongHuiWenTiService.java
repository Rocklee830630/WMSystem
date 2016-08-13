package com.ccthanking.business.bgh;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface BanGongHuiWenTiService  {

	String querybanGongHuiWen(String json,User user) throws Exception;
	String insertBanGongHuiWenTi(String msg, HttpServletRequest request) throws Exception;
	String updateBanGongHuiWenTi(String msg, HttpServletRequest request) throws Exception;
	String deleteGongHuiWenTi(String msg, User user);
	String updatebatchdata(String msg, User user, HttpServletRequest request);

}
