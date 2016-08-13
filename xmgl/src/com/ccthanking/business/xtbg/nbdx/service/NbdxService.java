package com.ccthanking.business.xtbg.nbdx.service;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.business.xtbg.nbdx.vo.NbdxVo;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface NbdxService extends BaseService<NbdxVo> {

	/**
	 * 查询内部短信
	 * @param json 页面传过来的json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public abstract String queryNbdx(String json, User user)
			throws SQLException;

	/**
	 * 修改内部短信方法，包括添加（1）、修改（2）、删除（3）
	 * @param json 页面传过来的json
	 * @param user
	 * @param operatorSign 操作标识
	 * @return
	 * @throws Exception
	 */
	public abstract String executeNbdx(HttpServletRequest request, String json, User user,
			String operatorSign) throws Exception;

}