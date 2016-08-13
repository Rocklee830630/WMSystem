package com.ccthanking.business.tcjh.jhjk;

import javax.servlet.http.HttpServletRequest;

public interface JhjkService {
	public String queryXMFLChart(HttpServletRequest request,String json) throws Exception;
	public String queryJHBZChart(HttpServletRequest request,String json) throws Exception;
	public String queryXMFR(HttpServletRequest request,String json) throws Exception;
	public String queryZRBM(HttpServletRequest request,String json) throws Exception;
	public String queryXMJD(HttpServletRequest request,String json) throws Exception;
	public String queryXMLX(HttpServletRequest request,String json) throws Exception;
	public String queryXMXZ(HttpServletRequest request,String json) throws Exception;
	String queryJhgzColumn2d(HttpServletRequest request, String json);
	public String queryJhxms(HttpServletRequest request) throws Exception;
	public String queryJhgk(HttpServletRequest request);
	public String queryJhbzTjgk(HttpServletRequest request);
	public String queryJhgzlb(HttpServletRequest request,String json) throws Exception;
}
