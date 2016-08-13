package com.ccthanking.business.xtbg.ggtz.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.business.xtbg.ggtz.vo.GgtzVo;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

/**
 * @author xhb
 */
public interface GgtzService extends BaseService<GgtzVo> {

	/**
	 * 查询公告信息
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public abstract String queryGgtz(String json, User user)
			throws SQLException;
	
	/**
	 * 查询首页更多公告信息
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public abstract String queryMoreGg(String json, User user)
			throws SQLException;


	/**
	 * 阅读公告，每次浏览此公告，阅读次数加一
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String readGgtz(String json, User user) throws Exception;
	
	/**
	 * 修改公告信息：包括添加、修改、删除，分别用1、2、3表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param operatorSign 操作标识
	 * @param ywid
	 * @return
	 * @throws Exception
	 */
	public String executeGg(String json, User user, String operatorSign,HttpServletRequest request, String ywid,String sffb) throws Exception;
	
	public void readGgtz(String ggid, String ydcs, User user) throws Exception;
	
	public String[] publishGg(String ggid, User user, HttpServletRequest request);
	public void doggsh(String ggid,String shyj,String shjg,String taskid,String sjbh, User user, HttpServletRequest request);

	public String[] cqsh(String ggid,String sjbh,String ywlx, User user, HttpServletRequest request) throws Exception;

	public String frameGgtz(User user) throws Exception;

}