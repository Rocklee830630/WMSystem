package com.ccthanking.business.ztb.zbgl;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface ZhaotoubiaoService  {

	String queryConditionZhaotoubiao(String json,User user) throws Exception;
	String insertdemo(String msg, User user,String ZTBXQID) throws SQLException, Exception;
	String updatedemo(String msg, User user) throws SQLException, Exception;
	String queryConditionZhaobiaoxuqiu(String json,User user) throws Exception;
	String queryConditionZhaobiaoxiangmu(String json,User user,String ZTBSJID) throws Exception;
	String queryZhongbiaojia(String json,User user,String ZTBSJID) throws Exception;
	String updateZhongbiaojia(String msg, User user) throws SQLException, Exception;
	String updateZhaotoubiao(String msg, User user,String ZTBSJID) throws SQLException, Exception;
	String queryZtbsptg(String json,User user) throws Exception;
	String updateZhaotoubiaoZT(String msg, User user) throws SQLException, Exception;
	String deleteZtbsj(HttpServletRequest request,String msg) throws SQLException, Exception;
	String getWhfxmCount(HttpServletRequest request,String msg) throws SQLException, Exception;
	String queryZxmListWithXqid(HttpServletRequest request,String msg) throws SQLException, Exception;
	String queryLanBiaoJia(HttpServletRequest request) throws SQLException, Exception;
	String doCheck(HttpServletRequest request) throws SQLException, Exception;
	String getxmZJECount(HttpServletRequest request) throws Exception;
}
