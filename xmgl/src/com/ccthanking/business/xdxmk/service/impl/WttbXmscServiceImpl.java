package com.ccthanking.business.xdxmk.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xdxmk.service.WttbXmscService;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;
@Service
public class WttbXmscServiceImpl implements WttbXmscService {
	private static String propertyFileName = "com.ccthanking.properties.business.bmgk.wttb";
	@Override
	public String queryWtTjgk(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String xmid = request.getParameter("xmid");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "XMSC_WTGKSQL");
			//拼接年度查询条件
			sql = sql.replace("%ndCondition%", " and XMID='"+xmid+"'");
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询问题性质分布饼图
	@Override
	public String queryWtxzfbChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String xmid = request.getParameter("xmid");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTXZFBSQL");
			//拼接年度查询条件
			xmid = Pub.empty(xmid)?"":" and XMID='"+xmid+"' ";
			sql = sql.replace("%ndCondition%", xmid);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = ChartUtil.makePieChartJsonString(domresult, null, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	//查询问题日期分布饼图
	@Override
	public String queryWtlbfbChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("xmid");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTLBFBSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and I.XMID='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = ChartUtil.makePieChartJsonString(domresult, null, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询问题解决情况分布饼图
	@Override
	public String queryJjqkfbChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("xmid");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "XMSC_JJQKFBSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and I.XMID='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = ChartUtil.makePieChartJsonString(domresult, null, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryInfoTable(String json, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String orderFlag = request.getParameter("orderFlag");
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = "";
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += BusinessUtil.getSJYXCondition("I") + BusinessUtil.getCommonCondition(user,null);
			String roleCond = "";
			String sql = "";
			//发起人查询
			sql = "select distinct I.wttb_info_id, I.jhsjid, I.wtlx, I.wtbt, I.yjsj, I.sjsj, I.sjzt, I.wtxz, I.cqbz, I.lrr, I.lrsj, I.lrbm, I.lrbmmc, I.gxr, I.gxsj, I.gxbm, I.gxbmmc, I.ywlx, I.sjbh, I.sjmj, I.sfyx,I.xmmc,I.bdmc " +
					",'' BLQK,L.JSR,XWWCSJ,HFQK " +
					" from WTTB_INFO I,(select WTID,JSR,XWWCSJ,HFQK " +
						"from WTTB_LZLS L,WTTB_INFO I " +
						"where L.WTID(+)=I.WTTB_INFO_ID " +
						"and L.BLRJS = '1' " +
						"and L.lrsj in (select max(lrsj) from WTTB_LZLS group by WTID)) L";
			roleCond = " and I.WTTB_INFO_ID=L.WTID";
			condition +=roleCond;
			if("1".equals(orderFlag)){
				orderFilter = " order by WTXZ,CQBZ desc";
			}else{
				orderFilter = " order by SJSJ desc";
			}
			condition +=orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			bs.setFieldDic("WTLX", "WTLX");
			bs.setFieldDic("SJZT", "WTZT");
			bs.setFieldDic("CQBZ", "CQBZ");
			bs.setFieldDic("HFQK", "HFQK");
			bs.setFieldUserID("JSR");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	/**
	 * 数据钻取查询查询
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryDrillingData(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("xmid");
		String proKey = request.getParameter("proKey");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "THEADSQL");
			sql = sql.replace("%CONDSQL%",Pub.getPropertiesSqlValue(propertyFileName, proKey));
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and I.XMID='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			bs.setFieldDic("WTLX", "WTLX");
			bs.setFieldDic("SJZT", "WTZT");
			bs.setFieldUserID("JSR");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
