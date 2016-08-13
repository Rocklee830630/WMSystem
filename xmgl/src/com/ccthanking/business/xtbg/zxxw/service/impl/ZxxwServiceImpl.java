package com.ccthanking.business.xtbg.zxxw.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xtbg.zxxw.service.ZxxwService;
import com.ccthanking.business.xtbg.zxxw.vo.ZxxwVo;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class ZxxwServiceImpl implements ZxxwService {
	

	/**
	 * 查询新闻信息
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String queryXw(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user, null);
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT NEWSID, XWBT, XWLB, FBR, FBSJ, YDCS, ZT, " 
						+ "ZHHFR, ZHHFSJ, HFCS, YWLX, SJBH, BZ, LRR, LRSJ, LRBM, "
					+ "LRBMMC, GXR, GXSJ, GXBM, GXBMMC, SJMJ, SFYX FROM XTBG_XXZX_ZXXW";
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
	 * 修改公告信息：包括添加、修改、删除，分别用1、2、3表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param operatorSign 操作标识
	 * @param ywid
	 * @return
	 * @throws Exception
	 */
	@Override
	public String executeXw(String json, User user, String operatorSign, String ywid)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		int sign = Integer.parseInt(operatorSign);
		try {
			ZxxwVo zxxxVo = new ZxxwVo();
			conn.setAutoCommit(false);
			JSONArray list = zxxxVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			jsonObj.put("YWLX", YwlxManager.OA_XXZX_ZXXW);
			jsonObj.put("FBR", user.getName());
			
			switch (sign) {
			case 1:
				msg = "添加";
				if (!"".equals(ywid)) {
					FileUploadService.updateFjztByYwid(ywid);
					jsonObj.put("NEWSID", ywid);
				} else {
					jsonObj.put("NEWSID", new RandomGUID().toString());
				}
				zxxxVo.setValueFromJson(jsonObj);
				Calendar cal = Calendar.getInstance(); 
				int year = cal.get(Calendar.YEAR); 
				zxxxVo.setNf(String.valueOf(year));
				BusinessUtil.setInsertCommonFields(zxxxVo, user);
				BaseDAO.insert(conn, zxxxVo);
				
				break;

			case 2:
				msg = "修改";
				zxxxVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(zxxxVo, user);
				BaseDAO.update(conn, zxxxVo);
				break;
				
			case 3:

				msg = "删除";
				jsonObj.put("SFYX", "0");
				zxxxVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(zxxxVo, user);
				BaseDAO.update(conn, zxxxVo);
				break;
			
			default:
				break;
			}
			
			

			resultVo = zxxxVo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_TXL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "新闻成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_TXL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "新闻失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
	
	/**
	 * 查询首页更多新闻信息
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String queryMoreXw(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user, null);
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT NEWSID, XWBT, XWLB, FBR, TO_CHAR(fbsj,'yyyy\"年\"fmmm\"月\"dd\"日\"') as FBSJ_SN, YDCS, ZT, " 
						+ "ZHHFR, ZHHFSJ, HFCS, YWLX, SJBH, BZ, LRR, LRSJ, LRBM, "
					+ "LRBMMC, GXR, GXSJ, GXBM, GXBMMC, SJMJ, SFYX,trunc(sysdate - fbsj) as ts  FROM XTBG_XXZX_ZXXW";
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
	public ZxxwVo findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ZxxwVo> find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ZxxwVo> find(List<Integer> ids) {
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
	public int update(ZxxwVo bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(ZxxwVo bean) {
		// TODO Auto-generated method stub
		return 0;
	}
}
