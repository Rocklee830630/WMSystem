package com.ccthanking.business.qqsx.lsgl.lxsxfj;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;
import com.ccthanking.business.qqsx.lsgl.lxsxfj.LxkySxfjVO;

public interface LxkySxfjService {

	//查询功能
	String queryXmxx(HttpServletRequest request,String json) throws Exception;
	//查询功能
	String querySxfj(HttpServletRequest request,String json) throws Exception;
	//插入功能
	String insertSxfj(HttpServletRequest request,String json) throws Exception;
	//修改功能
	String updateSxfj(HttpServletRequest request,String json) throws Exception;
	//反馈功能
	String feedbackQqsx(HttpServletRequest request,String json) throws Exception;
	//删除功能
	String deleteSxfj(HttpServletRequest request,String json) throws Exception;
	//计算个数
	String getCounts(HttpServletRequest request,String json) throws Exception;
	//查询时间
	String getDate(HttpServletRequest request,String json) throws Exception;

}
