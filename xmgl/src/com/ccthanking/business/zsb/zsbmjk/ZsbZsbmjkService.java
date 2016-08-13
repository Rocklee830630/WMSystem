package com.ccthanking.business.zsb.zsbmjk;

import javax.servlet.http.HttpServletRequest;

public interface ZsbZsbmjkService {
	//统计概况查询
	String queryTjgk(HttpServletRequest request,String json) throws Exception;
	//居民拆迁进展
	String queryJmcqjz(HttpServletRequest request,String json) throws Exception;
	//企业拆迁进展
	String queryQycqjz(HttpServletRequest request,String json) throws Exception;
	//征地拆迁进展
	String queryZdcqjz(HttpServletRequest request,String json) throws Exception;
	//拆迁进展详情
	String queryCqjzxq(HttpServletRequest request,String json) throws Exception;

}
