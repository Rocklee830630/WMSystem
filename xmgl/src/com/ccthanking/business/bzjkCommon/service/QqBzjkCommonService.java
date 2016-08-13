package com.ccthanking.business.bzjkCommon.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiahongbo
 * @date 2014-12-12
 */
public interface QqBzjkCommonService {


	public String queryListQqsg(HttpServletRequest request, String json);

	public String queryList_lxky(HttpServletRequest request, String msg);

	public String queryList_tdsx(HttpServletRequest request, String msg);

	public String queryList_ghsx(HttpServletRequest request, String msg);
}
