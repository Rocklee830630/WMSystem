package com.ccthanking.business.sjgl.newbmgk.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiahongbo
 * @date 2014-9-20
 */
public interface SjgkService {
	public String queryTxfx(HttpServletRequest request, String json);
	public String queryQll(HttpServletRequest request, String json);
	public String queryDll(HttpServletRequest request, String json);
	public String querySjglTcgk(HttpServletRequest request, String json);
	public String queryJgjvgTcgk(HttpServletRequest request, String json);
}
