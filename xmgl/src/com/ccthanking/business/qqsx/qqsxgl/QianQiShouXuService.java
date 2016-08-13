package com.ccthanking.business.qqsx.qqsxgl;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface QianQiShouXuService {

	String queryConditionQianQiShouXu(String msg,User user) throws SQLException;
	String insert(HttpServletRequest request,String msg, User user,String ywid,String xmmc)throws SQLException, Exception;
	String updatedemo(HttpServletRequest request,String msg, User user,String xmmc)throws SQLException, Exception;
	String querySxfjzj(HttpServletRequest request, String msg,String dfl);
}
