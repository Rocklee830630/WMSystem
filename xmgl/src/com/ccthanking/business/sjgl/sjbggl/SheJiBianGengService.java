package com.ccthanking.business.sjgl.sjbggl;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface SheJiBianGengService {

	String insert(HttpServletRequest request,String msg, User use,String ywid)throws SQLException, Exception;
	String updatedemo(String msg, User user)throws SQLException, Exception;
	String queryConditionSheJiBianGeng(String msg,String  sjwybh,String nd,User user) throws SQLException;
	String insertLingQu(String msg, User user)throws SQLException, Exception;
	String updateLingQu(String msg, User user)throws SQLException, Exception;
	String queryConditionShouQu(String msg, String SJBGID,User user)throws SQLException;
	String deleteJS(HttpServletRequest request, String msg);
	String deleteLQ(HttpServletRequest request, String msg);

}
