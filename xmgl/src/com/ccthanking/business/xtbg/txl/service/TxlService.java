package com.ccthanking.business.xtbg.txl.service;

import java.sql.SQLException;

import com.ccthanking.business.xtbg.txl.vo.TxlVo;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface TxlService extends BaseService<TxlVo> {

	/**
	 * 查询个人通讯录
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public abstract String queryPrivateTxl(String json, User user) throws SQLException;

	/**
	 * 修改个人通讯录：包括添加、修改、删除，分别用1、2、3表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param id
	 * @param exeSign 操作标识
	 * @return
	 * @throws Exception
	 */
	public abstract String executePrivateTxl(String json, User user, String id,
			String exeSign) throws Exception;

	/**
	 * 查询公共通讯录
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public abstract String queryPublicTxl(String json, User user) throws SQLException;
	
	/**
	 * 修改公共通讯录：包括添加、修改、删除，分别用1、2、3表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param id
	 * @param exeSign
	 * @return
	 * @throws Exception
	 */
	public abstract String executePublicTxl(String json, User user, String id,
			String exeSign) throws Exception;
	
	/**
	 * 查询通讯录的组
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param isPublic 是否是公共的组 1=私有2=公共
	 * @return
	 * @throws SQLException
	 */
	public abstract String queryTxlGroup(String json, User user, String isPublic) throws SQLException;
	
	/**
	 * 修改公共通讯录（组）：包括添加、修改、删除，分别用1、2、3表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param id
	 * @param exeSign 操作标识
	 * @param isPublic 是否是公共的组 1=私有2=公共
	 * @return
	 * @throws Exception
	 */
	public String executeTxlGroup(String json, User user, String id, String isPublic) throws Exception;
	
	public String deleteTxlGroup(String json, User user) throws Exception;
	
	public String[][] showPrivateGroup(User user);
	public String[][] showPublicGroup(User user);
}