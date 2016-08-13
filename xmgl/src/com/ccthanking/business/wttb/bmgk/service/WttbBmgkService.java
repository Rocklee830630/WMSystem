package com.ccthanking.business.wttb.bmgk.service;

import javax.servlet.http.HttpServletRequest;

public interface WttbBmgkService {
	String queryWtTjgk(HttpServletRequest request)throws Exception;
	String queryWttbTjgkNew(HttpServletRequest request)throws Exception;
	String queryWtxzfbChart(HttpServletRequest request)throws Exception;
	String queryWtcqqkChart(HttpServletRequest request)throws Exception;
	String queryWtjjcdChart(HttpServletRequest request)throws Exception;
	String queryWtjjcdEChart(HttpServletRequest request)throws Exception;
	String queryWtrqfbChart(HttpServletRequest request)throws Exception;
	String queryWtlbfbChart(HttpServletRequest request)throws Exception;
	String queryWtmycdChart(HttpServletRequest request)throws Exception;
	String queryWtlbfbxxChart(HttpServletRequest request)throws Exception;
	String queryZbbmChart(HttpServletRequest request)throws Exception;
	String queryZbbmEChart(HttpServletRequest request)throws Exception;
	String queryZbbmMycdChart(HttpServletRequest request)throws Exception;
	String queryNdwttbqkChart(HttpServletRequest request)throws Exception;
	String queryNdwttbqkEChart(HttpServletRequest request)throws Exception;
	String queryWtzdxm(HttpServletRequest request)throws Exception;
	String queryYcsjzcwt(HttpServletRequest request)throws Exception;
	String queryJjwtzdbm(HttpServletRequest request)throws Exception;
	String queryJjxlzgbm(HttpServletRequest request)throws Exception;
	String queryInfoTable(String json, HttpServletRequest request) throws Exception;
	String queryDrillingData(HttpServletRequest request,String json) throws Exception;
	String queryDrillingDataJh(HttpServletRequest request,String json) throws Exception;
	String queryZbldChart(HttpServletRequest request)throws Exception;
	String queryZbldEChart(HttpServletRequest request)throws Exception;
	String queryWttcChart(HttpServletRequest request)throws Exception;
	String queryWttcbtChart(HttpServletRequest request)throws Exception;
	String queryWtjsbtChart(HttpServletRequest request)throws Exception;
}
