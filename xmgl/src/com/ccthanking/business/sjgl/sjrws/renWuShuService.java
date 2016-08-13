package com.ccthanking.business.sjgl.sjrws;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;
import com.ccthanking.business.zjb.lbj.dyqk.GcZjbDyqkVO;;

public interface renWuShuService{

	String queryRenWuShu(String json,User user) throws Exception;
	String insertRenWuShu(String json,User user,HttpServletRequest request) throws Exception;
	String updateRenWuShu(String json,User user,HttpServletRequest request) throws Exception;
	String deleteRenWuShu(String msg, User user) throws Exception;

}
