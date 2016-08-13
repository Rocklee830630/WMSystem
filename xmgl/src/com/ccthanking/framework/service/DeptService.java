package com.ccthanking.framework.service;

import java.sql.SQLException;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.org_dept.OrgDeptVO;

public interface DeptService extends BaseService<OrgDeptVO> {

	/***
	 * 查询部门信息
	 * @author xhb
	 * @param String json 查询条件和排序Json字符串
	 * @param User user 用户实体Bean
	 * @return String 查询的结果集以json串形式返回
	 */
	public abstract String queryDept(String json, User user)
			throws SQLException;

	/**
	 * 修改部门信息方法，包括添加（1）、修改（2）、删除（3）
	 * @param json 页面传过来的json
	 * @param user
	 * @param operatorSign 操作标识
	 * @return
	 * @throws Exception
	 */
	public abstract String executeDept(String json, User user,
			String operatorSign) throws Exception;
	

	public String loadAllDept(String s,String dept)throws Exception;
	
	public abstract String leaderSave(String json, User user) throws Exception;
}