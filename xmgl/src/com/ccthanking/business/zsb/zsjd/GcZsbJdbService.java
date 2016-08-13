package com.ccthanking.business.zsb.zsjd;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;
import com.ccthanking.business.zsb.zsjd.GcZsbJdbVO;

public interface GcZsbJdbService {

	String queryJdb(HttpServletRequest request,String json) throws Exception;
	String insertJdb(HttpServletRequest request,String json) throws Exception;
	String updateJdb(HttpServletRequest request,String json) throws Exception;
	String queryJdxx(HttpServletRequest request,String json) throws Exception;
	String deleteJdxx(HttpServletRequest request,String json) throws Exception;
	//获取当前时间下是否有数据
	String getZsjd(HttpServletRequest request,String json) throws Exception;

}
