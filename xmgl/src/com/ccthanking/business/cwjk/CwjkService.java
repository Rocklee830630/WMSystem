package com.ccthanking.business.cwjk;

import javax.servlet.http.HttpServletRequest;

public interface CwjkService {
	public String queryJbqk(HttpServletRequest request,String json) throws Exception;
	public String queryZfqkColumn2d(HttpServletRequest request,String json) throws Exception;
}
