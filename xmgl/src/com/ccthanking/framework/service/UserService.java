
package com.ccthanking.framework.service;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;


public interface UserService extends BaseService<User> {


	User getUserByUsernameAndPassword(final String username, final String password) throws Exception;
	String insertdemo(String json,User user) throws Exception;
	String updatedemo(String json,User user) throws Exception;
	String queryConditiondemo(String json,User user) throws Exception;
	
	String queryUser(String json, User user) throws Exception;
	String executeUser(String json, User user, String id, String update) throws Exception;
	String queryUnique(String account, User user);
	public String[][] queryRole(String account);
	public String[][] queryPersonRole(String account);
	public void awardRoleToUser(String account, String[] roleNameAndId, User user) throws Exception;
	public String loadAllUser(HttpServletRequest request)throws Exception;
	public String loadDeptUser(HttpServletRequest request)throws Exception;

	public void awardRoleToPerson(String roleid,String rolename, String[] accountNameAndId, User user) throws Exception;
	
	String queryUserFile(HttpServletRequest request,requestJson js) throws Exception;
	//重置密码
	String resetPw(String account) throws Exception;
	//查询个人信息（主要是首页修改个人信息的时候使用，author by liujs）
	String personInfo(HttpServletRequest request,String json) throws Exception;
}
