package com.ccthanking.business.zjb.lbj;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;
import com.ccthanking.business.zjb.lbj.GcZjbLbjbVO;

public interface LbjService {

	String queryConditionLbj(String json,User user,String IsNull) throws Exception;
	String insertLbj(HttpServletRequest request,String json,User user) throws Exception;
	String updateLbj(HttpServletRequest request,String json,User user) throws Exception;
	String updateZt(HttpServletRequest request, String msg, User user) throws Exception;

}
