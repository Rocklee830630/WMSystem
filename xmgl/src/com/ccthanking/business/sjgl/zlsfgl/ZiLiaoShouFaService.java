package com.ccthanking.business.sjgl.zlsfgl;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface ZiLiaoShouFaService {

	String insert(HttpServletRequest request,String msg, User user)throws SQLException, Exception;
	String updatedemo(HttpServletRequest request,String msg, User user)throws SQLException, Exception;
	String queryConditionZiLiaoShouFa(String msg,String  sjwybh,String nd,User user) throws SQLException;
	String insertLingQu(String msg, User user)throws SQLException, Exception;
	String updateLingQu(String msg, User user)throws SQLException, Exception;
	String queryConditionShouQu(String msg, String ZLLJSDID,User user)throws SQLException;
	String deleteJS(HttpServletRequest request, String msg);
	String deleteLQ(HttpServletRequest request, String msg);
	String jSLQquery(String msg, String jHSJID, User user);

}
