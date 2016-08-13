package com.ccthanking.business.xtbg.sjdx.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xtbg.sjdx.service.SjdxService;
import com.ccthanking.business.xtbg.sjdx.vo.SjdxVo;
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
public class SjdxServiceImpl implements SjdxService {
	
	/**
	 * 查询手机短信，查询所有，已接收，已发送短信。分别用空，receive，sended表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param mark 用此参数判断查询条件
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String querySms(String json, User user, String mark) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += "sended".equals(mark) ? (" AND FSR = '" + user.getName() + "'") 
					: ("receive".equals(mark) ? (" AND JSR = '" + user.getName() + "'") : "");
			
			condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user, null);
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT SMSID, JSR, JSRID, JSRHM, FSR, FSRID, " 
					+ "FSSJ, FSXX, ZT FROM XTBG_SJDX";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}

	@Override
	public SjdxVo findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SjdxVo> find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SjdxVo> find(List<Integer> ids) {
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
	public int update(SjdxVo bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(SjdxVo bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 操作手机短信
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public String executeSms(String json, User user, String id)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		SjdxVo sjdx = new SjdxVo();
		try {
			conn.setAutoCommit(false);
			JSONArray list = sjdx.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			// id为空，添加短信
			if (id == null || "null".equals(id) || "undefined".equals(id)) {
				msg = "添加";

				jsonObj.put("YWLX", YwlxManager.OA_TXGL_SJDX);
				jsonObj.put("FSR", user.getName());
				
				String[] jsr = jsonObj.getString("JSR").split(",");
				String[] sjhm = jsonObj.getString("SJHM").split(",");
				for (int i = 0; i < jsr.length; i++) {
					jsonObj.put("SMSID", new RandomGUID().toString());
					
					jsonObj.put("JSR", jsr[i]);
					jsonObj.put("SJHM", sjhm[i]);
					

					sjdx.setValueFromJson(jsonObj);
					BusinessUtil.setInsertCommonFields(sjdx, user);
					BaseDAO.insert(conn, sjdx);
				}
				
			} else {

				msg = "删除";
				jsonObj.put("SFYX", "0");
				sjdx.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(sjdx, user);
				BaseDAO.update(conn, sjdx);
			}
			resultVo = sjdx.getRowJson();
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_SJDX,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "批量" + msg + "短信成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_SJDX,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "批量" + msg + "短信失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
	
}
