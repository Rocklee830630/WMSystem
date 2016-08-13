package com.ccthanking.business.xtbg.txl.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xtbg.txl.service.TxlService;
import com.ccthanking.business.xtbg.txl.vo.TxlGroupVo;
import com.ccthanking.business.xtbg.txl.vo.TxlVo;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class TxlServiceImpl implements TxlService {

	private static final String PRIVATE = "1";
	private static final String PUBLIC = "2";
	
	/**
	 * 查询个人通讯录
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String queryPrivateTxl(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += " AND USERID = '" + user.getAccount() + "'";
			condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user, null);
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT TXID, XM, NC, XB, CSRQ, DW, ZW, GZDH, " 
					+ "GZCZ, GZDZ, YX, SJHM, ZZDH, ZZDZ, QQ, MSN, XMJP, BS, " 
					+ "USERID, ORDERNUM, BZ, YWLX FROM XTBG_TXL";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDic("XB", "XB");
			
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	/**
	 * 修改个人通讯录：包括添加、修改、删除，分别用1、2、3表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param id
	 * @param exeSign 操作标识
	 * @return
	 * @throws Exception
	 */
	@Override
	public String executePrivateTxl(String json, User user, String id, String exeSign) throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		TxlVo txl = new TxlVo();
		int sign = Integer.parseInt(exeSign);
		try {
			conn.setAutoCommit(false);
			JSONArray list = txl.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			
			switch (sign) {
			case 1:
				msg = "添加";
				jsonObj.put("TXID", new RandomGUID().toString());
				jsonObj.put("USERID", user.getAccount());
				jsonObj.put("BS", PRIVATE);
				jsonObj.put("YWLX", YwlxManager.OA_TXGL_TXL);
				txl.setValueFromJson(jsonObj);	
				BusinessUtil.setInsertCommonFields(txl, user);
				BaseDAO.insert(conn, txl);
				break;
			case 2:
				msg = "修改";
				jsonObj.put("YWLX", YwlxManager.OA_TXGL_TXL);
				txl.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(txl, user);
				BaseDAO.update(conn, txl);
				break;
			case 3:
				jsonObj.put("SFYX", "0");
				jsonObj.put("YWLX", YwlxManager.OA_TXGL_TXL);
				txl.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(txl, user);
				BaseDAO.update(conn, txl);
				break;

			default:
				break;
			}
			

			resultVo = txl.getRowJson();
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_TXL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "个人通讯录信息成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_TXL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "个人通讯录信息失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}

	@Override
	public TxlVo findById(int id) {
		return null;
	}

	@Override
	public List<TxlVo> find() {
		return null;
	}

	@Override
	public List<TxlVo> find(List<Integer> ids) {
		return null;
	}

	@Override
	public int remove(int id) {
		return 0;
	}

	@Override
	public int remove(List<Integer> ids) {
		return 0;
	}

	@Override
	public int update(TxlVo bean) {
		return 0;
	}

	@Override
	public int insert(TxlVo bean) {
		return 0;
	}

	/**
	 * 查询公共通讯录
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String queryPublicTxl(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += " AND BS = '2' ";
			condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);

			String querySql = "SELECT TXID, XM, NC, XB, CSRQ, DW, ZW, GZDH, " 
					+ "GZCZ, GZDZ, YX, SJHM, ZZDH, ZZDZ, QQ, MSN, XMJP, BS, " 
					+ "USERID, ORDERNUM, BZ, YWLX FROM XTBG_TXL";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDic("XB", "XB");
			
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}

	/**
	 * 修改公共通讯录：包括添加、修改、删除，分别用1、2、3表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param id
	 * @param exeSign
	 * @return
	 * @throws Exception
	 */
	@Override
	public String executePublicTxl(String json, User user, String id,
			String exeSign) throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		TxlVo txl = new TxlVo();
		int sign = Integer.parseInt(exeSign);
		try {
			conn.setAutoCommit(false);
			JSONArray list = txl.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			
			switch (sign) {
			case 1:
				msg = "添加";
				jsonObj.put("TXID", new RandomGUID().toString());
				jsonObj.put("BS", PUBLIC);
				jsonObj.put("YWLX", YwlxManager.OA_TXGL_TXL);
				txl.setValueFromJson(jsonObj);
				BusinessUtil.setInsertCommonFields(txl, user);
				BaseDAO.insert(conn, txl);
				break;
			case 2:
				msg = "修改";
				jsonObj.put("YWLX", YwlxManager.OA_TXGL_TXL);
				txl.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(txl, user);
				BaseDAO.update(conn, txl);
				break;
			case 3:
				msg = "删除";
				jsonObj.put("SFYX", "0");
				jsonObj.put("YWLX", YwlxManager.OA_TXGL_TXL);
				txl.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(txl, user);
				BaseDAO.update(conn, txl);
				break;
			default:
				break;
			}
			

			resultVo = txl.getRowJson();
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_TXL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "公共通讯录信息成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_TXL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "公共通讯录信息失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
	
	/**
	 * 查询通讯录的组
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param isPublic 是否是公共的组 1=私有2=公共
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String queryTxlGroup(String json, User user, String isPublic) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += " AND ISPUBLIC = '" + isPublic + "'";
			condition += "1".equals(isPublic) ? (" AND USERID = '" + user.getAccount() + "'") : "";
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT GROUPID, ZMC, USERID, TXID, ISPUBLIC FROM XTBG_TXL_GROUP";
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
	 * 修改公共通讯录（组）：包括添加、修改、删除，分别用1、2、3表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param id
	 * @param exeSign 操作标识
	 * @param isPublic 是否是公共的组 1=私有2=公共
	 * @return
	 * @throws Exception
	 */
	@Override
	public String executeTxlGroup(String json, User user, String id, String isPublic) throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		TxlGroupVo txlGroupVo = new TxlGroupVo();
		try {
			conn.setAutoCommit(false);
			JSONArray list = txlGroupVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			if (id == null || "null".equals(id) || "undefined".equals(id)) {
				msg = "添加";
				jsonObj.put("GROUPID", new RandomGUID().toString());
				jsonObj.put("USERID", user.getAccount());
				jsonObj.put("ISPUBLIC", isPublic);
				txlGroupVo.setValueFromJson(jsonObj);
				BaseDAO.insert(conn, txlGroupVo);
			} else {
				msg = "修改";
				txlGroupVo.setValueFromJson(jsonObj);
				BaseDAO.update(conn, txlGroupVo);
			}

			resultVo = txlGroupVo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_TXL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "通讯录（组）成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_TXL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "通讯录（组）失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}

	@Override
	public String deleteTxlGroup(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVo = null;
		TxlGroupVo txlGroupVo = new TxlGroupVo();
		try {
			conn.setAutoCommit(false);
			JSONArray list = txlGroupVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);

			txlGroupVo.setValueFromJson(jsonObj);
			BaseDAO.delete(conn, txlGroupVo);
			
			resultVo = txlGroupVo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_TXL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+  "删除通讯录（组）成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_TXL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除通讯录（组）失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}

	@Override
	public String[][] showPrivateGroup(User user) {
		Connection conn = DBUtil.getConnection();
		String[][] groupRs = null;
		try {
			String sql = "SELECT GROUPID, ZMC FROM XTBG_TXL_GROUP WHERE ISPUBLIC='1' AND USERID='" + user.getAccount() + "'";
			groupRs = DBUtil.querySql(conn, sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return groupRs;
	}

	@Override
	public String[][] showPublicGroup(User user) {
		Connection conn = DBUtil.getConnection();
		String[][] groupRs = null;
		try {
			String sql = "SELECT GROUPID, ZMC FROM XTBG_TXL_GROUP WHERE ISPUBLIC='2'";
			groupRs = DBUtil.querySql(conn, sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return groupRs;
	}
}
