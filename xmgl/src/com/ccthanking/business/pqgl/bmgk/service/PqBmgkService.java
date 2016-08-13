package com.ccthanking.business.pqgl.bmgk.service;

import javax.servlet.http.HttpServletRequest;

public interface PqBmgkService {
	public String queryPqTjgk(HttpServletRequest request) throws Exception;
	public String queryPqRwxx(HttpServletRequest request) throws Exception;
	public String queryPqChart(HttpServletRequest request) throws Exception;
	public String queryPqNyxx(HttpServletRequest request) throws Exception;
}
