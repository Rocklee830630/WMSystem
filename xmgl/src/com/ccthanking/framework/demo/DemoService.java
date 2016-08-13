
package com.ccthanking.framework.demo;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface DemoService extends BaseService<User> {


	User getUserByUsernameAndPassword(final String username, final String password) throws Exception;
	String insertdemo(String json,User user) throws Exception;
	String updatedemo(String json,User user) throws Exception;
	String updatebatchdemo(String json,User user) throws Exception;
	String queryConditiondemo(String json,User user) throws Exception;
	String deletedemo(String json,User user) throws Exception;
	String queryAttchdemo(String json,User user) throws Exception;
	
}
