package com.ccthanking.business.zjb.jswj;

import java.sql.SQLException;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface ZaoJiaRenWuService  {

	String queryZaoJiaRenWu(String msg, User user);

	String insertZaoJiaRenWu(String msg, User user, String ywid) throws Exception;

	String updateZaoJiaRenWu(String msg, User user) throws Exception;


}
