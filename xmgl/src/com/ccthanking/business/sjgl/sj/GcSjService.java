package com.ccthanking.business.sjgl.sj;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;
import com.ccthanking.business.sjgl.sj.GcSjVO;

public interface GcSjService {
	//查询
	String querySj(HttpServletRequest request,String json) throws Exception;
	//反馈
	String Feedback(HttpServletRequest request,String json) throws Exception;
	//概算反馈
	String gsFeedback(HttpServletRequest request,String json) throws Exception;
}
