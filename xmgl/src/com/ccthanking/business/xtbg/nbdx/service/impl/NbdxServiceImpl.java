package com.ccthanking.business.xtbg.nbdx.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xtbg.nbdx.service.NbdxService;
import com.ccthanking.business.xtbg.nbdx.vo.NbdxVo;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class NbdxServiceImpl implements NbdxService {
	
	/**
	 * 查询内部短信
	 * @param json 页面传过来的json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String queryNbdx(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
		//	condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user, null);
			condition += " AND STATE='1' ";
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT OPID, USERFROM, USERFROMNAME, USERTO, USERTONAME, TITLE, " 
					+ "CONTENT, OPTIME, SYSMESSAGE, EMAILMESSAGE, SMSMESSAGE, DELOPER, DELTIME, " 
					+ "STATE, ACCESSORY, LINKURL, SJBH, YWLX FROM FS_MESSAGE_INFO";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	
	/**
	 * 修改内部短信方法，包括添加（1）、修改（2）、删除（3）
	 * @param json 页面传过来的json
	 * @param user
	 * @param operatorSign 操作标识
	 * @return
	 * @throws Exception
	 */
	@Override
	public String executeNbdx(HttpServletRequest request, String json, User user, String operatorSign)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		int sign = Integer.parseInt(operatorSign);
		try {
			NbdxVo nbdxVo = new NbdxVo();
			conn.setAutoCommit(false);
			JSONArray list = nbdxVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			
			switch (sign) {
			case 1:
				msg = "添加";
				// 发信人
				jsonObj.put("USERFROM", user.getAccount());
				// 发信人姓名
				jsonObj.put("USERFROMNAME", user.getName());
				// 状态是1表示正常
				jsonObj.put("STATE", "1");
				jsonObj.put("YWLX", YwlxManager.OA_TXGL_NBDX);
				
				String[] usertoname = jsonObj.getString("USERTONAME").split(",");
				String[] userto = jsonObj.getString("USERTO").split(",");
				for (int i = 0; i < userto.length; i++) {
					jsonObj.put("OPID", new RandomGUID().toString());
					
					jsonObj.put("USERTONAME", usertoname[i]);
					jsonObj.put("USERTO", userto[i]);
					
					nbdxVo.setValueFromJson(jsonObj);
			//		BaseDAO.insert(conn, nbdxVo);
				}
				
				String title = jsonObj.getString("TITLE");
				String content = jsonObj.getString("CONTENT");

				PushMessage.push(conn, request, content, "", "", YwlxManager.OA_TXGL_NBDX, "", userto, title);
				break;

			case 2:

				msg = "修改";
				nbdxVo.setValueFromJson(jsonObj);
				BaseDAO.update(conn, nbdxVo);
				break;
				
			case 3:

				msg = "删除";
				jsonObj.put("DELOPER", user.getName());
				jsonObj.put("DELTIME", new Date());
				// 状态是2表示删除
				jsonObj.put("STATE", "2");
				nbdxVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(nbdxVo, user);
				BaseDAO.update(conn, nbdxVo);
				break;
				
			default:
				break;
			}
			
			resultVo = nbdxVo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_NBDX,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "内部短信成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_NBDX,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "内部短信失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}


	@Override
	public NbdxVo findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<NbdxVo> find() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<NbdxVo> find(List<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int remove(int id) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int remove(List<Integer> ids) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int update(NbdxVo bean) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int insert(NbdxVo bean) {
		// TODO Auto-generated method stub
		return 0;
	}
}
