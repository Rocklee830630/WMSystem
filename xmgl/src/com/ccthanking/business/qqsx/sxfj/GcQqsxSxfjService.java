package com.ccthanking.business.qqsx.sxfj;

import java.sql.SQLException;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;
import com.ccthanking.business.qqsx.sxfj.GcQqsxSxfjVO;

public interface GcQqsxSxfjService {

	String queryConditionSxfj(String json) throws Exception;
	String insertSxfj(String json,User user) throws Exception;
	String updateSxfj(String json,User user) throws Exception;
	String queryBanLiXiangById(String ywid, User user) throws SQLException;

}
