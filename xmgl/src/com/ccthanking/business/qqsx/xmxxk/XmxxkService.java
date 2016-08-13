package com.ccthanking.business.qqsx.xmxxk;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface XmxxkService {

	//查询功能
	String exist(HttpServletRequest request,String json) throws Exception;
	//手续信息查询
	String querySxxx(HttpServletRequest request,String json) throws Exception;

}
