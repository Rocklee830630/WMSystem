package com.ccthanking.business.xtbg.zxxw.service;

import java.sql.SQLException;

import com.ccthanking.business.xtbg.zxxw.vo.ZxxwVo;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface ZxxwService extends BaseService<ZxxwVo> {

	/**
	 * 查询新闻信息
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public abstract String queryXw(String json, User user) throws SQLException;
	/**
	 * 查询首页更多新闻信息
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	
	public abstract String queryMoreXw(String json, User user) throws SQLException;
	

	/**
	 * 修改公告信息：包括添加、修改、删除，分别用1、2、3表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param operatorSign 操作标识
	 * @param ywid
	 * @return
	 * @throws Exception
	 */
	public String executeXw(String json, User user, String operatorSign, String ywid)
			throws Exception;
}