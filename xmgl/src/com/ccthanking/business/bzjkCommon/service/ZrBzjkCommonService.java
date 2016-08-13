package com.ccthanking.business.bzjkCommon.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhaiyl
 * @date 2014-12-15
 */
public interface ZrBzjkCommonService {

	String queryYkgxmqkColumn2d(HttpServletRequest request, String msg);

	String queryJhssxmColumn2d(HttpServletRequest request, String msg);

	String queryList(HttpServletRequest request, String msg);

	String queryNDRW(HttpServletRequest request, String msg);

	public String queryFxwtTjgk(HttpServletRequest request, String msg);
	public String queryZbldChart(HttpServletRequest request)throws Exception;
	public String queryZbbmChart(HttpServletRequest request)throws Exception;

	String queryListQqsg(HttpServletRequest request, String msg);

	String queryListLx(HttpServletRequest request, String msg);

	String querySjglList(HttpServletRequest request, String msg);

}
