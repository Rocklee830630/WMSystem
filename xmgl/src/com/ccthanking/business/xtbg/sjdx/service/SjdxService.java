package com.ccthanking.business.xtbg.sjdx.service;

import java.sql.SQLException;

import com.ccthanking.business.xtbg.sjdx.vo.SjdxVo;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface SjdxService extends BaseService<SjdxVo> {

	/**
	 * 查询手机短信，查询所有，已接收，已发送短信。分别用空，receive，sended表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param mark 用此参数判断查询条件
	 * @return
	 * @throws SQLException
	 */
	public abstract String querySms(String json, User user, String mark)
			throws SQLException;
	
	
	/**
	 * 操作手机短信
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String executeSms(String json, User user, String id) throws Exception ;
}