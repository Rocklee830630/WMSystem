package com.ccthanking.business.sjgl.gs;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.BaseService;
import com.ccthanking.business.sjgl.gs.GcSjCbsjpfVO;

public interface GcSjCbsjpfService {

	String queryGs(HttpServletRequest request,String json) throws Exception;
	String insertGs(HttpServletRequest request,String json,String ywid) throws Exception;
	String updateGs(HttpServletRequest request,String json) throws Exception;
	//查询信息
	String getSx(HttpServletRequest request,String json) throws Exception;
	//查询不办理信息
	String getBblsx(HttpServletRequest request,String json) throws Exception;
	//获得前期手续信息
	String getQqsx(HttpServletRequest request,String json) throws Exception;
}
