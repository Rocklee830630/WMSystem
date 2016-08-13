package com.ccthanking.business.xmglgs.gcjlwh;

import java.sql.SQLException;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface GongChengJiLiangService   {

	String insert(String msg, User user)throws SQLException, Exception;
	String updatedemo(String msg, User user)throws SQLException, Exception;
	String queryConditionXiangMu(String msg) throws SQLException;
	String queryBanLiXiangById(String msg, User user) throws SQLException;
	String queryzhouYueBao(String msg,String shijianid) throws SQLException;
/*	String queryConditionJieSuan(String json) throws Exception;

	String insertdemo(String msg, GcZgbQqsxVO user) 

	String queryByIddemo(String msg, GcZgbQqsxVO user) throws SQLException, Exception;

	String updatedemo(String msg, GcZgbQqsxVO user) throws SQLException, Exception;

	String queryConditionHeTong(String msg) throws SQLException;*/
	String insertShiJian(String msg, User user) throws SQLException, Exception;
	String queryShiJian(String msg) throws SQLException;
	String updatedShiJian(String msg, User user) throws SQLException, Exception;

}
