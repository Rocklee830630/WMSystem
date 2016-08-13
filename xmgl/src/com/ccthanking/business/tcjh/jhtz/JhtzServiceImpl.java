package com.ccthanking.business.tcjh.jhtz;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.common.BusinessUtil;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @author xiahongbo
 * @date 2014-8-8
 */
@Service
public class JhtzServiceImpl implements JhtzService {

	@Override
	public String query(HttpServletRequest request, User user, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
	        condition += BusinessUtil.getSJYXCondition("");
	        condition += orderFilter;
			
			page = page == null ? new PageManager() : page;
			page.setFilter(condition);
				
			String sql = "select JHTZ_ID,JHTZ_ND,JHTZ_NR1,JHTZ_NR2 where GC_JHTZ ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	@Override
	public String saveOrUpdate(HttpServletRequest request, User user, String json) {
		Connection conn = null;
		String resultVo = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);

			JhtzVO vo = new JhtzVO();
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			
			String nd = vo.getJhtz_nd();
			String sql = "select count(*) cnt from GC_JHTZ where JHTZ_ND='"+nd+"'";
			String[][] rs = DBUtil.query(conn, sql);
			if("0".equals(rs[0][0])) {
				BaseDAO.insert(conn, vo);
			} else {
				BaseDAO.update(conn, vo);
			}
			
			resultVo = vo.getRowJson();
			conn.commit();

			LogManager.writeUserLog(nd, "",
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "计划调整添加成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}

	@Override
	public String queryById(HttpServletRequest request, User user) {
		Connection conn = null;
		JSONObject obj = new JSONObject();
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			String nd = request.getParameter("nd");
			String ndSql = nd == null ? " where JHTZ_ND='"+DateTimeUtil.getCurrentYear()+"'" : " where JHTZ_ND='"+nd+"'";
			
			String sql = "select JHTZ_ND,JHTZ_NR1,JHTZ_NR2 from GC_JHTZ " + ndSql;
			List<Map<String, String>> rs = DBUtil.queryReturnList(conn, sql);
			if(rs != null && rs.size() != 0) {
				Map<String, String> map = rs.get(0);
				obj.put("JHTZ_ND", map.get("JHTZ_ND"));
				obj.put("JHTZ_NR1", map.get("JHTZ_NR1"));
				obj.put("JHTZ_NR2", map.get("JHTZ_NR2"));
			} else {
				obj.put("JHTZ_ND", nd);
				obj.put("JHTZ_NR1", "");
				obj.put("JHTZ_NR2", "");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj.toString();
	}

}
