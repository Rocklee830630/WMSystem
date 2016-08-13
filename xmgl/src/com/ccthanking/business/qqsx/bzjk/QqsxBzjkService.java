package com.ccthanking.business.qqsx.bzjk;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;

public interface QqsxBzjkService {
	String querySx(HttpServletRequest request) throws Exception;
	String querySxZxtColumn2d(HttpServletRequest request, String json);
	//施工进展情况查询
	String querySg(HttpServletRequest request) throws Exception;
	String querySgZxtColumn2d(HttpServletRequest request, String json);
	//立项进展情况查询
	String queryLx(HttpServletRequest request) throws Exception;
	String queryLxZxtColumn2d(HttpServletRequest request, String json);
	//土地进展情况查询
	String queryTd(HttpServletRequest request) throws Exception;
	String queryTdZxtColumn2d(HttpServletRequest request, String json);
	//规划进展情况查询
	String queryGh(HttpServletRequest request) throws Exception;
	String queryGhZxtColumn2d(HttpServletRequest request, String json);
	//时间查询
	String queryDate(HttpServletRequest request) throws Exception;
	//时间查询
	String queryCount(User user,String nd) throws Exception;
	
	String queryZtqk(HttpServletRequest request) throws Exception;
	String queryQqsx_DST1(HttpServletRequest request) throws Exception;
	String queryQqsx_DST2(HttpServletRequest request) throws Exception;
}
