package com.ccthanking.business.xmcbk.bmgk.service;

import javax.servlet.http.HttpServletRequest;

public interface XmcbkBmgkService {
	public String queryTableAssdq(HttpServletRequest request,String json) throws Exception;
	public String queryTableAssdqAll(HttpServletRequest request,String json) throws Exception;
	public String queryTableAzrbm(HttpServletRequest request,String json) throws Exception;
	public String queryTableAxmlx(HttpServletRequest request,String json) throws Exception;
	public String queryTableAztz(HttpServletRequest request,String json) throws Exception;
	public String queryZf_NdChart(HttpServletRequest request,String json) throws Exception;
	public String querySch_NdChart(HttpServletRequest request,String json) throws Exception;
	public String queryLwyjChart(HttpServletRequest request,String json) throws Exception;
	public String queryZf_Sch(HttpServletRequest request,String json) throws Exception;
	public String queryJhzxNcjhZftzChart(HttpServletRequest request, String json);
	public String queryJhzxNcjhSctzChart(HttpServletRequest request, String json);
	public String queryCbkTjgk(HttpServletRequest request,String json) throws Exception;
	public String queryJhtzTjgk(HttpServletRequest request,String json)throws Exception;
	public String queryJhxdTjgk(HttpServletRequest request,String json)throws Exception;
	public String queryJhxdNcjhChart(HttpServletRequest request)throws Exception;
	public String queryJhxdZjjhChart(HttpServletRequest request)throws Exception;
	public String queryJhzxZjjhChart(HttpServletRequest request)throws Exception;
	public String queryJhzxTjgk(HttpServletRequest request,String json)throws Exception;
	public String queryTabLwyjZf(HttpServletRequest request,String json) throws Exception;
	public String queryTabLwyjSc(HttpServletRequest request,String json) throws Exception;
	public String queryJhxdNcjh(HttpServletRequest request,String json)throws Exception;
	public String queryJhxdZjjh(HttpServletRequest request,String json)throws Exception;
	public String queryList(HttpServletRequest request, String json) throws Exception;
	public String queryKwgjs(HttpServletRequest request, String json) throws Exception;
}
