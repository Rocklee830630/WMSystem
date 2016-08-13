package com.ccthanking.business.htgl.bmgk.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiahongbo
 * @date 2014-9-15
 */
public interface GcHtBmgkService {

	public String queryHtglBmlb(HttpServletRequest request, String json);
	public String queryHtglTjgk(HttpServletRequest request, String json);
	public String queryHtglZbsg(HttpServletRequest request, String json);
	public String queryHtglZbjl(HttpServletRequest request, String json);
	public String queryZgbsjzbChart(HttpServletRequest request, String json) throws Exception;
	public String queryZgbqqzbChart(HttpServletRequest request, String json) throws Exception;	
	public String queryZgbzjbzbChart(HttpServletRequest request, String json) throws Exception;
	public String queryZgbztbzbChart(HttpServletRequest request, String json) throws Exception;
	public String queryZgbpqzbChart(HttpServletRequest request, String json) throws Exception;
	public String queryZgbzszbChart(HttpServletRequest request, String json) throws Exception;	
	public String queryZgbsgzbChart(HttpServletRequest request, String json) throws Exception;
	public String queryZgbjlzbChart(HttpServletRequest request, String json) throws Exception;
	public String queryZgbzlaqzbChart(HttpServletRequest request, String json) throws Exception;
	public String queryZbzt(HttpServletRequest request) throws Exception;
	
	public String queryZbglZbxqColumn2d(HttpServletRequest request, String json);
	public String queryZbsjList(HttpServletRequest request, String json);
	public String queryHtsjList(HttpServletRequest request, String json);
	public String queryZBXQList(HttpServletRequest request, String json);
}
