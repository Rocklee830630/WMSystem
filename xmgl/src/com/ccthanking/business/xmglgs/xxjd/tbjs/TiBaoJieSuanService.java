package com.ccthanking.business.xmglgs.xxjd.tbjs;

import java.sql.SQLException;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface TiBaoJieSuanService  {

	String queryTiBaoJieSuan(String msg, User user);

	String insertTBJSZT(String msg, User user, String ywid);


}
