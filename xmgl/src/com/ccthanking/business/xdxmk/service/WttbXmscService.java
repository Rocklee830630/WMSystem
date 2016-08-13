package com.ccthanking.business.xdxmk.service;

import javax.servlet.http.HttpServletRequest;

public interface WttbXmscService {
	String queryWtTjgk(HttpServletRequest request)throws Exception;
	String queryWtxzfbChart(HttpServletRequest request)throws Exception;
	String queryWtlbfbChart(HttpServletRequest request)throws Exception;
	String queryJjqkfbChart(HttpServletRequest request)throws Exception;
	String queryInfoTable(String json, HttpServletRequest request) throws Exception;
	String queryDrillingData(HttpServletRequest request,String json) throws Exception;
}
