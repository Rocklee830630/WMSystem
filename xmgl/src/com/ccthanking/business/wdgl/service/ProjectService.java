package com.ccthanking.business.wdgl.service;

import java.sql.SQLException;

import com.ccthanking.framework.common.User;

public interface ProjectService {

	public abstract String getAllProject(String prjName);
	public abstract String getChildProject(String json,String parent) throws Exception;
	public abstract String getDocumentDir(String prjName,String prjId);
	public abstract String getProjectfl(String prjName);
	public abstract String addFl(String parent,String flmc,String operatorSign,String nd,User user) throws SQLException;
	public abstract String addZxm(String parent,String ids,String nd,User user) throws SQLException;

}