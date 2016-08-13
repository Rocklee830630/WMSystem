package com.ccthanking.business.wttb.bmgk.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.wttb.bmgk.service.WttbBmgkService;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.CommonChart.showchart.chart.EChartUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class WttbBmgkServiceImpl implements WttbBmgkService{
	private static String propertyFileName = "com.ccthanking.properties.business.bmgk.wttb";
	@Override
	public String queryWtTjgk(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTGKSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":"ND='"+nd+"'";
			page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDecimals("LJJQYZWTBFB");	//累计极其严重问题百分比
			bs.setFieldDecimals("LJYZWTBFB");	//累计严重问题百分比
			bs.setFieldDecimals("LJYBWTBFB");	//累计一般问题百分比
			bs.setFieldDecimals("NDJQYZWTBFB");	//按年度极其严重问题百分比
			bs.setFieldDecimals("NDYZWTBFB");	//年度严重问题百分比
			bs.setFieldDecimals("NDYBWTBFB");	//年度一般问题百分比
			bs.setFieldDic("WTZDLX", "WTLX");		//问题最多的类型
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryWttbTjgkNew(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTGKSQL_NEW");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and B.ND='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("WTZDLX1", "WTLX");		//问题最多的类型
			bs.setFieldDic("WTZDLX2", "WTLX");		//问题最多的类型
			bs.setFieldDic("WTZDLX3", "WTLX");		//问题最多的类型
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
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTXZFBSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap rowMap = new HashMap();
			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			List list = new ArrayList();
			list.add(ChartUtil.chartWarnColor1);
			list.add(ChartUtil.chartWarnColor4);
			list.add(ChartUtil.chartWarnColor5);
			rowMap.put("COLOR", list);
			domresult = ChartUtil.makePieChartJsonString(domresult, null, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询解决程度分布饼图
	@Override
	public String queryWtjjcdChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTJJCDSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and ND='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap rowMap = new HashMap();
			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			List list = new ArrayList();
			list.add(ChartUtil.chartWarnColor1);
			list.add(ChartUtil.chartWarnColor5);
			list.add(ChartUtil.chartWarnColor4);
			rowMap.put("COLOR", list);
			domresult = ChartUtil.makePieChartJsonString(domresult, null, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//查询解决程度分布饼图
	@Override
	public String queryWtjjcdEChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTJJCDSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and ND='"+nd+"' ";
//				page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();

			HashMap chartMap=new HashMap();
			chartMap.put("pieRadius","30");
			chartMap.put("titleText","解决程度");
			chartMap.put("x","center");
			chartMap.put("seriesName","问题数量");
			
			List list = new ArrayList();
			list.add(EChartUtil.chartWarnColor1);
			list.add(EChartUtil.chartWarnColor5);
			list.add(EChartUtil.chartWarnColor4);
			chartMap.put("COLOR", list);
			domresult = EChartUtil.makePieEChartJsonString(domresult, chartMap, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
		
	//查询问题超期情况分布饼图
	@Override
	public String queryWtcqqkChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTCQQKSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap chartMap = new HashMap();
			chartMap.put("pieRadius", "62");
			HashMap rowMap = new HashMap();
			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			List list = new ArrayList();
			list.add(ChartUtil.chartWarnColor5);
			list.add(ChartUtil.chartWarnColor1);
			rowMap.put("COLOR", list);
			domresult = ChartUtil.makePieChartJsonString(domresult, chartMap, rowMap);
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
	public String queryWtrqfbChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTRQFBSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
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
	//查询问题类别分布饼图
	@Override
	public String queryWtlbfbChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTLBFBSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap rowMap = new HashMap();
			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			domresult = ChartUtil.makePieChartJsonString(domresult, null, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询满意程度分布饼图
	@Override
	public String queryWtmycdChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTMYCDSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap rowMap = new HashMap();
			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			List list = new ArrayList();
			list.add(ChartUtil.chartWarnColor5);
			list.add(ChartUtil.chartWarnColor4);
			list.add(ChartUtil.chartWarnColor1);
			rowMap.put("COLOR", list);
			domresult = ChartUtil.makePieChartJsonString(domresult, null, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询问题类别分布详细叠加柱形图
	@Override
	public String queryWtlbfbxxChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTLBFBXXSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = ChartUtil.makeMSStackedColumn2DChartJsonString(domresult, null, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询主办部门叠加柱形图
	@Override
	public String queryZbbmChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "ZBBMSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			List list = new ArrayList();
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor6);
			HashMap rowMap = new HashMap();
			rowMap.put("COLOR", list);
			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			domresult = ChartUtil.makeMSStackedColumn2DChartJsonString(domresult, null, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//查询主办部门叠加柱形图
	@Override
	public String queryZbbmEChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "ZBBMSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			
			HashMap chartMap = new HashMap();
			chartMap.put("pieRadius","30");
			chartMap.put("titleText","各部门问题接收情况");
			chartMap.put("x","center");
			chartMap.put("seriesType","bar");
			chartMap.put("gridX", "90px");
			chartMap.put("gridX2", "10px");
			
			chartMap.put("order", true);

			chartMap.put("yAxisType", "category");
			chartMap.put("xAxisType", "value");
			
			List list = new ArrayList();
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor6);
			chartMap.put("COLOR", list);
			HashMap<String, Object> rowMap = new HashMap<String, Object>();
			HashMap stackMap = new HashMap();
			stackMap.put("未解决", "2");
			stackMap.put("解决", "2");
			rowMap.put("STACK", stackMap);
			rowMap.put("seriesItemStyle", "seriesItemStyle");
			domresult = EChartUtil.makeBarEChartJsonString(domresult, chartMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询主办部门满意程度叠加柱形图
	@Override
	public String queryZbbmMycdChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "ZBBMMYCDSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			List list = new ArrayList();
			list.add(ChartUtil.chartWarnColor5);
			list.add(ChartUtil.chartWarnColor4);
			list.add(ChartUtil.chartWarnColor1);
			HashMap rowMap = new HashMap();
			rowMap.put("COLOR", list);
			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			domresult = ChartUtil.makeMSStackedColumn2DChartJsonString(domresult, null, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询主办领导叠加柱形图
	@Override
	public String queryZbldChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "ZBLDSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			List list = new ArrayList();
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor6);
			HashMap rowMap = new HashMap();
			rowMap.put("COLOR", list);
			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			domresult = ChartUtil.makeMSStackedColumn2DChartJsonString(domresult, null, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询主办领导叠加柱形图
		@Override
		public String queryZbldEChart(HttpServletRequest request)throws Exception {
			Connection conn = null;
			String domresult = "";
			String nd = request.getParameter("nd");
			try { 
				conn = DBUtil.getConnection();
				PageManager page = new PageManager();
				page.setPageRows(1000);
				String sql =Pub.getPropertiesSqlValue(propertyFileName, "ZBLDSQL");
				//拼接年度查询条件
				nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
				sql = sql.replaceAll("%ndCondition%", nd);
				conn.setAutoCommit(false);
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				domresult = bs.getJson();

				HashMap chartMap=new HashMap();
				chartMap.put("titleText","中心领导问题接收情况");
				chartMap.put("x","center");
				chartMap.put("seriesType","bar");
				chartMap.put("gridX", "50px");
				chartMap.put("gridX2", "10px");
				
				chartMap.put("yAxisType", "value");
				chartMap.put("xAxisType", "category");
				
				chartMap.put("order", false);
				chartMap.put("yAxisType", "category");
				chartMap.put("xAxisType", "value");
				
				List list = new ArrayList();
				list.add(ChartUtil.chartColor6);
				list.add(ChartUtil.chartColor2);
				chartMap.put("COLOR", list);
				HashMap<String, Object> rowMap = new HashMap<String, Object>();
				HashMap stackMap = new HashMap();
				stackMap.put("未解决", "2");
				stackMap.put("解决", "2");
				rowMap.put("STACK", stackMap);
				rowMap.put("seriesItemStyle", "seriesItemStyle");
				domresult = EChartUtil.makeBarEChartJsonString(domresult, chartMap, rowMap);
			} catch (Exception e) {
				e.printStackTrace(System.out);
				throw e;
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
	//查询问题提出各部门叠加柱形图
	@Override
	public String queryWttcChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTTCSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			List list = new ArrayList();
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor6);
			HashMap rowMap = new HashMap();
			rowMap.put("COLOR", list);
			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			domresult = ChartUtil.makeMSStackedColumn2DChartJsonString(domresult, null, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询问题提出各部门问题类型分布饼图
	@Override
	public String queryWttcbtChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTTCBTSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap<String,String> chartMap = new HashMap();
//			chartMap.put("caption", "问题提出");
			chartMap.put("bgcolor", "FFFFFF");
			chartMap.put("pieborderthickness", "2");
			chartMap.put("piebordercolor", "FFFFFF");
			chartMap.put("basefontsize", "9");
			chartMap.put("usehovercolor", "1");
			chartMap.put("hover", "CCCCCC");
			chartMap.put("showlabels", "1");
			chartMap.put("showvalue", "1");
			chartMap.put("showvalueintooltip", "1");
			chartMap.put("hoverfillcolor", "CCCCCC");
			chartMap.put("showBorder", "0");
			chartMap.put("pieRadius", "150");
			List list = new ArrayList();
			list.add(ChartUtil.chartColor4);
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor3);
			HashMap rowMap = new HashMap();
			rowMap.put("COLOR", list);

			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			//加入二级饼的查询方法
			rowMap.put("CATEGORYFUNCTION", "javascript:showDataDetail");
			List categoryParamList = new ArrayList();
			categoryParamList.add("CATEGORY");
			categoryParamList.add("CATEGORYFIELDNAME");
			rowMap.put("CATEGORYPARAM", categoryParamList);
			rowMap.put("caption", "问题提出");
			domresult = ChartUtil.makeMultiLevelPieChartJsonString(domresult, chartMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询问题提出各部门问题类型分布饼图
	@Override
	public String queryWtjsbtChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTJSBTSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap<String,String> chartMap = new HashMap();
//			chartMap.put("caption", "问题接收");
			chartMap.put("bgcolor", "FFFFFF");
			chartMap.put("pieborderthickness", "2");
			chartMap.put("piebordercolor", "FFFFFF");
			chartMap.put("basefontsize", "9");
			chartMap.put("usehovercolor", "1");
			chartMap.put("hover", "CCCCCC");
			chartMap.put("showlabels", "1");
			chartMap.put("showvalue", "1");
			chartMap.put("showvalueintooltip", "1");
			chartMap.put("hoverfillcolor", "CCCCCC");
			chartMap.put("showBorder", "0");
			chartMap.put("pieRadius", "150");
			List list = new ArrayList();
			list.add(ChartUtil.chartColor4);
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor3);
			HashMap rowMap = new HashMap();
			rowMap.put("COLOR", list);

			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			//加入二级饼的查询方法
			rowMap.put("CATEGORYFUNCTION", "javascript:showDataDetail");
			List categoryParamList = new ArrayList();
			categoryParamList.add("CATEGORY");
			categoryParamList.add("CATEGORYFIELDNAME");
			rowMap.put("CATEGORYPARAM", categoryParamList);
			rowMap.put("caption", "问题接收");
			domresult = ChartUtil.makeMultiLevelPieChartJsonString(domresult, chartMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询年度问题提报情况折线图
	@Override
	public String queryNdwttbqkChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "NDWTTBQK");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = ChartUtil.makeMSLineChartJsonString(domresult, null, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//查询年度问题提报情况折线图
	@Override
	public String queryNdwttbqkEChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "NDWTTBQK");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//				page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();

			HashMap chartMap=new HashMap();
			chartMap.put("pieRadius","30");
			chartMap.put("titleText","问题情况月度分部");
			chartMap.put("x","center");
			chartMap.put("seriesType","line");
			chartMap.put("gridX", "30px");
			chartMap.put("gridX2", "10px");
			
			chartMap.put("yAxisType", "value");
			chartMap.put("xAxisType", "category");
			
			chartMap.put("order", true);
			
			HashMap rowMap = new HashMap();
			domresult = EChartUtil.makeBarEChartJsonString(domresult, chartMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询问题最多项目
	@Override
	public String queryWtzdxm(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(5);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "WTZDXMSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and J.ND='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
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
	//查询延迟时间最长问题
	@Override
	public String queryYcsjzcwt(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(5);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "YCSJZCWTSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
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
	//查询解决问题最多部门
	@Override
	public String queryJjwtzdbm(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(5);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "JJWTZDBM");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
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
	//查询解决效率最高部门或领导
	@Override
	public String queryJjxlzgbm(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(5);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "JJXLZGBM");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDecimals("NUM");	//累计极其严重问题百分比
			domresult = bs.getJson();
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
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += BusinessUtil.getSJYXCondition("I") + BusinessUtil.getCommonCondition(user,null);
			String roleCond = "";
			String sql = "";
			//发起人查询
			sql = "select distinct I.wttb_info_id, I.jhsjid, I.wtlx, I.wtbt, I.yjsj, I.sjsj, I.sjzt, I.wtxz, I.cqbz, I.lrr, I.lrsj, I.lrbm, I.lrbmmc, I.gxr, I.gxsj, I.gxbm, I.gxbmmc, I.ywlx, I.sjbh, I.sjmj, I.sfyx,I.xmmc,I.bdmc " +
					",'' BLQK,L.JSR,XWWCSJ,L.JSBM ,P.NAME as LRRMC,CBCS,D1.EXTEND1 D1EXTEND1,D2.EXTEND1 D2EXTEND1,decode(JHSJID,null,'0','1') SJXM,L.HFQK,MYCD " +
					" from WTTB_INFO I,(select WTID,JSR,JSBM,XWWCSJ,HFQK,MYCD " +
						"from WTTB_LZLS L " +
						"where L.BLRJS = '1') L,FS_ORG_PERSON P,FS_ORG_DEPT D1,FS_ORG_DEPT D2 ";
			roleCond = " and I.WTTB_INFO_ID=L.WTID(+) and I.LRR=P.ACCOUNT(+) and D1.ROW_ID=I.LRBM and D2.ROW_ID=L.JSBM and I.SJZT in ('2','3','4','6')";
			condition +=roleCond;
			condition +=orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldOrgDept("JSBM");
			bs.setFieldUserID("LRR");
			bs.setFieldDic("WTLX", "WTLX");
			bs.setFieldDic("SJZT", "WTZT");
			bs.setFieldDic("CQBZ", "CQBZ");
			bs.setFieldDic("HFQK", "HFQK");
			bs.setFieldDic("MYCD", "MYCD");
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
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition = Pub.empty(condition)?"":" and "+condition;
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "THEADSQL");
			sql = sql.replace("%CONDSQL%",Pub.getPropertiesSqlValue(propertyFileName, proKey));
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			sql += condition;
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
	/**
	 * 数据钻取查询查询_给计划写的
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryDrillingDataJh(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		String flag = request.getParameter("flag");
		String nd = request.getParameter("nd");
		String wtxz = request.getParameter("wtxz");
		String wtlx = request.getParameter("wtlx");
		String sjzt = request.getParameter("sjzt");
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "THEADSQL");
//			String condSql = "select distinct I.wttb_info_id from WTTB_INFO I " +
//					"where to_char(lrsj,'yyyy')='"+nd+"' and wtxz='"+wtxz+"'" ;
			//modify by zhangbr@ccthanking.com 暂时去掉年度查询条件，保证两面数据一致
			String condSql = "select distinct I.wttb_info_id from WTTB_INFO I " +
			"where wtxz='"+wtxz+"'" ;
			if(sjzt!="" && sjzt!="undefined" && !"undefined".equals(sjzt)){
				condSql += " and sjzt='"+sjzt+"' ";
			}
			if(flag=="2" || "2".equals(flag)){
				condSql += " and WTLX not in ('18','20','12','21','17','22','23') ";
			}else {
				condSql += " and wtlx='"+wtlx+"' ";
			}
			sql = sql.replace("%CONDSQL%",condSql);
			//发起人查询
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
