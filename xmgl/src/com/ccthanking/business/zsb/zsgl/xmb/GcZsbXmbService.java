package com.ccthanking.business.zsb.zsgl.xmb;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;
import com.ccthanking.business.zsb.zsgl.xmb.GcZsbXmbVO;

public interface GcZsbXmbService {

	String queryConditionXmb(HttpServletRequest request,String json) throws Exception;
	String insertXmb(HttpServletRequest request,String json) throws Exception;
	String updateXmb(HttpServletRequest request,String json) throws Exception;
	String queryJdxx(HttpServletRequest request,String json) throws Exception;
	String queryXmb(HttpServletRequest request,String json) throws Exception;
	//计划反馈
	String doJhsjfk(HttpServletRequest request,String json) throws Exception;
	//计划反馈状态
	String getZt(HttpServletRequest request,String json) throws Exception;

}
