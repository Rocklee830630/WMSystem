package com.ccthanking.business.zsb.zsgl.xxb;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.BaseService;
import com.ccthanking.business.zsb.zsgl.xxb.GcZsbXxbVO;

public interface GcZsbXxbService {

	String queryXxb(HttpServletRequest request,String json) throws Exception;
	String insertXxb(HttpServletRequest request,String json) throws Exception;
	String updateXxb(HttpServletRequest request,String json) throws Exception;
	//添加删除功能
	String deleteZsxx(HttpServletRequest request,String json) throws Exception;
	//联想查询
	List<autocomplete> xmmcAutoComplete(HttpServletRequest request,autocomplete json)  throws Exception;//项目名称自动补全
	String updateXXB(HttpServletRequest request, String msg) throws Exception;
}
