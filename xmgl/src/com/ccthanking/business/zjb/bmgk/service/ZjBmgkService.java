package com.ccthanking.business.zjb.bmgk.service;

import javax.servlet.http.HttpServletRequest;

public interface ZjBmgkService {
	public String queryJsljList(HttpServletRequest request, String json) throws Exception;
	public String queryZjbzTjgk(HttpServletRequest request,String json) throws Exception;
	public String queryFxglCgfbChart(HttpServletRequest request, String json) throws Exception;
	public String queryFxglCgfbTable(HttpServletRequest request, String json) throws Exception;
	public String queryFxglTjgk(HttpServletRequest request,String json)throws Exception;
	
	public String queryGcjsTjgk(HttpServletRequest request,String json)throws Exception;
	public String queryGcjsTxfx(HttpServletRequest request,String json)throws Exception;
	public String queryGcjsList(HttpServletRequest request, String msg)throws Exception;
	public String queryList_zjbz(HttpServletRequest request, String msg);
	public String queryJslbljList(HttpServletRequest request, String msg);
}
