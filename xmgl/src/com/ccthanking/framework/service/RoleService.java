package com.ccthanking.framework.service;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;

public interface RoleService {

	public abstract String queryRole(String json, User user)
			throws SQLException;

	public abstract String executeRole(String json, User user,
			String operatorSign) throws Exception;
	
	public String queryUnique(String roleName, User user);
	//查询树形角色列表
	public String queryRoleTree(HttpServletRequest request,String json);
	//根据角色类别树形字典查询角色列表
	public String queryRoleByTreeNode(HttpServletRequest request, String json) throws SQLException;
	//移入
	public String doMoveInTreeNode(HttpServletRequest request, String json)throws Exception;
	//移出
	public String doMoveOutTreeNode(HttpServletRequest request, String json)throws Exception;
	//获取已存在的角色ID
	public String getExistsNodeID(HttpServletRequest request, String json)throws Exception;
	
	

}