package com.ccthanking.business.zjb.lbj.dyqk;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;
import com.ccthanking.business.zjb.lbj.dyqk.GcZjbDyqkVO;;

public interface DyqkService{

	String queryConditionDyqk(String json,User user) throws Exception;
	String insertDyqk(String json,User user,String ywid) throws Exception;
	String updateDyqk(String json,User user) throws Exception;
	String deleteDyqk(String msg, User user) throws Exception;

}
