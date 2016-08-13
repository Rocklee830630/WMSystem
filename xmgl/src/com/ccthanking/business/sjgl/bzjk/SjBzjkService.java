package com.ccthanking.business.sjgl.bzjk;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;

public interface SjBzjkService {
	//施工进展情况查询
	String queryTjgk(HttpServletRequest request) throws Exception;

	String queryCQT(HttpServletRequest request);

	String queryPQT(HttpServletRequest request);

	String querySGT(HttpServletRequest request);

	String queryZJXX(HttpServletRequest request);

	String queryJJG(HttpServletRequest request);

	String queryJCJC(HttpServletRequest request);

	String querySJBG(HttpServletRequest request);

	String querySJXM(HttpServletRequest request);

	String querySJBD(HttpServletRequest request);

	String querySJY(HttpServletRequest request);

	String queryJCLX(HttpServletRequest request);

	String querysjxms(HttpServletRequest request);

	String queryzlsf(HttpServletRequest request);
}
