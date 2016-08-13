package com.ccthanking.business.xtbg.gwgl.service;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.business.xtbg.gwgl.vo.SwglVo;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface SwglService extends BaseService<SwglVo> {

	/**
	 * 收文信息查询
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public abstract String querySw(String json, User user) throws SQLException;

	/**
	 * 修改发文信息：包括添加、修改、删除，分别用1、2、3表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param operatorSign 操作标识
	 * @param ywid
	 * @return
	 * @throws Exception
	 */
	public abstract String executeSw(String json, User user,
			String operatorSign, String ywid) throws Exception;
	
	//历史查询
	public abstract String queryLs(String json, User user) throws SQLException;

	String queryUnique(HttpServletRequest request, User user);
	
	
	String querySwbh(String swlb, User user);
	
	String deleteSw(String id);
}