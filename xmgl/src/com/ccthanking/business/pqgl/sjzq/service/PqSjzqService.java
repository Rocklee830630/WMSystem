package com.ccthanking.business.pqgl.sjzq.service;

import javax.servlet.http.HttpServletRequest;

public interface PqSjzqService {
	String queryDrillingDataZxm(HttpServletRequest request,String json) throws Exception;
	String queryDrillingDataZh(HttpServletRequest request,String json) throws Exception;
}
