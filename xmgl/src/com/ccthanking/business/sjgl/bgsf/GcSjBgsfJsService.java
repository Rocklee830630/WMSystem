package com.ccthanking.business.sjgl.bgsf;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;
import com.ccthanking.business.sjgl.bgsf.GcSjBgsfJsVO;

public interface GcSjBgsfJsService {
	//查询综合信息
	String querySf(HttpServletRequest request,String msg) throws Exception;
	//查询接收信息
	String queryJs(HttpServletRequest request,String msg) throws Exception;
	String insertSf(HttpServletRequest request,String json) throws Exception;
	String updateSf(HttpServletRequest request,String json) throws Exception;
	String querySfxx(HttpServletRequest request,String msg) throws Exception;
	String insertSfxx(HttpServletRequest request,String json) throws Exception;
	String updateSfxx(HttpServletRequest request,String json) throws Exception;
	String deleteJS(HttpServletRequest request, String msg) throws Exception;
	String deleteLQ(HttpServletRequest request, String msg);

}
