package com.ccthanking.business.xmcbk.bmgk.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xmcbk.bmgk.service.XmcbkBmgkService;
import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

//按所属地区
@Service
public class XmcbkBmgkServiceImpl implements XmcbkBmgkService{
	private String propertyFileName = "com.ccthanking.properties.business.bmgk.xmcbk";
	private String propertyFileName_List = "com.ccthanking.properties.business.bmgk.xmcbk_xx";
	@Override
	public String queryTableAssdq(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			page.setPageRows(100);
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "CBK_ASSDQ_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("ZTZE");
			bs.setFieldDic("QY","QY");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String queryTableAssdqAll(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			page.setPageRows(100);
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "CBK_ASSDQ_SQL_ALL");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("ZTZE");
			bs.setFieldDic("QY","QY");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	//按部门
	@Override
	public String queryTableAzrbm(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			page.setPageRows(100);
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "CBK_ABM_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("ZTZE");
			bs.setFieldDic("ZRBM", "ZRBM");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}


	//按项目类型
	@Override
	public String queryTableAxmlx(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "CBK_AXMLX_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("ZTZE");
			bs.setFieldDic("XMLX", "XMLX");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//按总投资
	@Override
	public String queryTableAztz(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "CBK_AZTZ_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("ZTZE");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//政府—年度投资
	@Override
	public String queryZf_NdChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "CBK_Zf_Nd_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = ChartUtil.makePieChartJsonString(domresult, null, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//市场化—年度投资
	@Override
	public String querySch_NdChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "CBK_Sch_Nd_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = ChartUtil.makePieChartJsonString(domresult, null, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	

	//两委一局饼图3
	@Override
	public String queryLwyjChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "LWYJ_LWYJBT_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap chartMap = new HashMap();
			chartMap.put("pieRadius", "70");
			chartMap.put("showLegend", "1");
			chartMap.put("showLabels", "0");
			List list = new ArrayList();
			list.add(ChartUtil.chartColor4);
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor3);
			list.add(ChartUtil.chartColor1);
			list.add(ChartUtil.chartColor5);
			list.add(ChartUtil.chartColor6);
			HashMap rowMap = new HashMap();
			rowMap.put("COLOR", list);
			domresult = ChartUtil.makePieChartJsonString(domresult, chartMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
//政府市场化
	@Override
	public String queryZf_Sch(HttpServletRequest request, String json) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		String result="";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}	;
			String sql = Pub.getPropertiesString(propertyFileName, "JHZX_NCJH_ZFSC_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			domresult = bs.getJson();
			HashMap rowMap = new HashMap();
			rowMap.put("BREAKNUM", "1");
			rowMap.put("TOTALNUM", "2");
			HashMap chartMap = new HashMap();
			chartMap.put("showlegend", "1");
			chartMap.put("showvalues", "0");
//			chartMap.put("showsum", "0");
			chartMap.put("subCaption", "总体投资   单位（亿元）");
			List list = new ArrayList();
			list.add(ChartUtil.chartColor4);
			list.add(ChartUtil.chartColor2);
			rowMap.put("COLOR", list);
			domresult = ChartUtil.makeMSStackedColumn2DChartJsonString(domresult, chartMap, rowMap);
			//result=ChartUtil.makeColumn2DChartJsonString(domresult,null,null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryJhzxNcjhZftzChart(HttpServletRequest request, String json) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		try {
			PageManager page=new PageManager();
			page.setPageRows(100);
			conn.setAutoCommit(false);
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}	;
			String sql = Pub.getPropertiesString(propertyFileName, "JHZX_NCJH_ZFTZ_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			domresult = bs.getJson();
			HashMap rowMap = new HashMap();
			rowMap.put("BREAKNUM", "1");
			rowMap.put("TOTALNUM", "2");
			HashMap chartMap = new HashMap();
			chartMap.put("showlegend", "1");
			chartMap.put("showvalues", "0");
//			chartMap.put("showsum", "0");
			chartMap.put("subCaption", "政府投资   单位（亿元）");
			List list = new ArrayList();
			list.add(ChartUtil.chartColor4);
			list.add(ChartUtil.chartColor2);
			rowMap.put("COLOR", list);
			domresult = ChartUtil.makeMSStackedColumn2DChartJsonString(domresult, chartMap, rowMap);
			//result=ChartUtil.makeColumn2DChartJsonString(domresult,null,null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryJhzxNcjhSctzChart(HttpServletRequest request, String json) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		try {
			PageManager page=new PageManager();
			page.setPageRows(100);
			conn.setAutoCommit(false);
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}	;
			String sql = Pub.getPropertiesString(propertyFileName, "JHZX_NCJH_SCTZ_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			domresult = bs.getJson();
			HashMap rowMap = new HashMap();
			rowMap.put("BREAKNUM", "1");
			rowMap.put("TOTALNUM", "2");
			HashMap chartMap = new HashMap();
			chartMap.put("showlegend", "1");
			chartMap.put("showvalues", "0");
//			chartMap.put("showsum", "0");
			chartMap.put("subCaption", "市场化投资   单位（亿元）");
			List list = new ArrayList();
			list.add(ChartUtil.chartColor4);
			list.add(ChartUtil.chartColor2);
			rowMap.put("COLOR", list);
			domresult = ChartUtil.makeMSStackedColumn2DChartJsonString(domresult, chartMap, rowMap);
			//result=ChartUtil.makeColumn2DChartJsonString(domresult,null,null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String queryCbkTjgk(HttpServletRequest request,String json)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "CBK_TJGK_SQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and ND='"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd);
			//page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			bs.setFieldMoveToLeft("LWYJ_XJJHZTZE");
			bs.setFieldMoveToLeft("LWYJ_JHZTZE");
			bs.setFieldMoveToLeft("LWYJ_XUJJHZTZE");
			bs.setFieldMoveToLeft("LWYJ_NDGS");
			bs.setFieldMoveToLeft("LWYJ_ZFTZJHZTZE");
			bs.setFieldMoveToLeft("LWYJ_SCHJHZTZE");
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
	public String queryJhxdNcjh(HttpServletRequest request,String json)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(100);
			String sql = Pub.getPropertiesString(propertyFileName, "JHXD_NCJH_TABLE_SQL");
			//拼接年度查询条件
			String otherNd = Pub.empty(nd)?"":" and to_char(XDRQ, 'yyyy')='"+nd+"'";
			nd = Pub.empty(nd)?"":" and ND='"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd);
			sql=sql.replaceAll("%ndCondition%", otherNd);
			//page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("XDRQ", "yyyy年MM月dd日");
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
	public String queryJhxdZjjh(HttpServletRequest request,String json)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(100);
			String sql = Pub.getPropertiesString(propertyFileName, "JHXD_ZJJH_TABLE_SQL");
			//拼接年度查询条件
			String otherNd = Pub.empty(nd)?"":" and to_char(XDRQ, 'yyyy')='"+nd+"'";
			nd = Pub.empty(nd)?"":" and ND='"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd);
			sql=sql.replaceAll("%ndCondition%", otherNd);
			//page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("XDRQ", "yyyy年MM月dd日");
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
	public String queryJhxdTjgk(HttpServletRequest request,String json)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "JHXD_TJGK_SQL");
			//拼接年度查询条件 
			String otherNd = Pub.empty(nd)?"":" and to_char(XDRQ, 'yyyy')='"+nd+"'";
			String _nd = Pub.empty(nd)?"":" and ND='"+nd+"'";
			sql=sql.replaceAll("%Condition%", _nd);
			sql=sql.replaceAll("%ndCondition%", otherNd);
			sql=sql.replaceAll("%otherNd%", otherNd);
			//page.setFilter(nd);
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
	@Override
	public String queryJhzxTjgk(HttpServletRequest request,String json)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "JHZX_TJGK_SQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and ND='"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd);
			//page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldMoveToLeft("JHZX_CNT_LJ_WCTZ");
			bs.setFieldMoveToLeft("JHZX_CNT_ZFLJ_WCTZ");
			bs.setFieldMoveToLeft("JHZX_CNT_ZFZJLJ_WCTZ");
			bs.setFieldMoveToLeft("JHZX_CNT_SCLJ_WCTZ");
			bs.setFieldMoveToLeft("JHZX_CNT_SCZJLJ_WCTZ");
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
	public String queryJhtzTjgk(HttpServletRequest request,String json)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = "select JHTZ_NR1,JHTZ_NR2 from gc_jhtz ";
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":"JHTZ_ND='"+nd+"'";
			page.setFilter(nd);
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
	
	//查询计划下达年初计划饼图
	@Override
	public String queryJhxdNcjhChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "JHXD_NCJH_SQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and nd='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%Condition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap<String,String> chartMap = new HashMap();
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

//			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
//			List paramList = new ArrayList();
//			paramList.add("LABEL");
//			paramList.add("FIELDNAME");
//			rowMap.put("LINKPARAM", paramList);
			//加入二级饼的查询方法
//			rowMap.put("CATEGORYFUNCTION", "javascript:showDataDetail");
//			List categoryParamList = new ArrayList();
//			categoryParamList.add("CATEGORY");
//			categoryParamList.add("CATEGORYFIELDNAME");
//			rowMap.put("CATEGORYPARAM", categoryParamList);
			rowMap.put("caption", "年初计划");
			domresult = ChartUtil.makeMultiLevelPieChartJsonString(domresult, chartMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询计划下达追加计划饼图
	@Override
	public String queryJhxdZjjhChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "JHXD_ZJJH_SQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and nd='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%Condition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap<String,String> chartMap = new HashMap();
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

//			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
//			List paramList = new ArrayList();
//			paramList.add("LABEL");
//			paramList.add("FIELDNAME");
//			rowMap.put("LINKPARAM", paramList);
			//加入二级饼的查询方法
//			rowMap.put("CATEGORYFUNCTION", "javascript:showDataDetail");
//			List categoryParamList = new ArrayList();
//			categoryParamList.add("CATEGORY");
//			categoryParamList.add("CATEGORYFIELDNAME");
//			rowMap.put("CATEGORYPARAM", categoryParamList);
			rowMap.put("caption", "追加计划");
			domresult = ChartUtil.makeMultiLevelPieChartJsonString(domresult, chartMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询计划执行追加计划饼图
	@Override
	public String queryJhzxZjjhChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "JHZX_ZJJH_SQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and nd='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%Condition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap<String,String> chartMap = new HashMap();
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

//			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
//			List paramList = new ArrayList();
//			paramList.add("LABEL");
//			paramList.add("FIELDNAME");
//			rowMap.put("LINKPARAM", paramList);
			//加入二级饼的查询方法
//			rowMap.put("CATEGORYFUNCTION", "javascript:showDataDetail");
//			List categoryParamList = new ArrayList();
//			categoryParamList.add("CATEGORY");
//			categoryParamList.add("CATEGORYFIELDNAME");
//			rowMap.put("CATEGORYPARAM", categoryParamList);
			rowMap.put("caption", "追加计划");
			domresult = ChartUtil.makeMultiLevelPieChartJsonString(domresult, chartMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryTabLwyjZf(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(100);
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}
			String sql = Pub.getPropertiesString(propertyFileName, "CBK_lwyj_Zf_Nd_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryTabLwyjSc(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(100);
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and nd='"+nd+"'  ";
			}
			String sql = Pub.getPropertiesString(propertyFileName, "CBK_lwyj_Sc_Nd_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryList(HttpServletRequest request, String json) throws Exception {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			PageManager page = RequestUtil.getPageManager(json);
			String ndSql = Pub.empty(nd) ? "" : " and nd='"+nd+"'";
			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			String arr[] = proKey.split("\\*\\*");
			String querySql = Pub.getPropertiesSqlValue(propertyFileName_List, arr[0]);
			String condiSql = Pub.getPropertiesSqlValue(propertyFileName_List, arr[0]+"_"+arr[1]);

			querySql = querySql.replaceAll("%condition_sql%", condiSql);
			querySql = querySql.replaceAll("%ndCondition%", ndSql);
			querySql = querySql.replaceAll("%ndCondition2%", ndSql2);
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldThousand("JH_XJ");	//计划总投资-小计
			bs.setFieldThousand("JH_GC");	//计划总投资-工程
			bs.setFieldThousand("JH_ZQ");	//计划总投资-征地排迁
			bs.setFieldThousand("JH_QT");	//计划总投资-其他
			bs.setFieldThousand("ND_GC");	//年度计划投资-工程
			bs.setFieldThousand("ND_ZQ");	//年度计划投资-工程
			bs.setFieldThousand("ND_QT");	//年度计划投资-工程
			bs.setFieldThousand("ND_XJ");	//年度计划投资-工程
			bs.setFieldThousand("ND_XJ");	//年度计划投资-工程
			bs.setFieldDic("ZRBM", "ZRBM");
			bs.setFieldDic("XMFR", "XMFR");
			bs.setFieldDic("XMJSJD", "XMJSJD");
			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldDic("NDMB", "NDMB");//年度目标
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("SSXZ", "XMSX");
			bs.setFieldDic("CJXMSX", "CJXMSX");
			bs.setFieldDic("ISXD", "SF");
			bs.setFieldDic("QY", "QY");
			bs.setFieldDic("ISBT", "SF");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	@Override
	public String queryKwgjs(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domString = null;
		try {
			String nd = request.getParameter("nd");
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String propertiesFileName_gc = "com.ccthanking.properties.business.bmgk.gcb";
			String propertyFileName_zj = "com.ccthanking.properties.business.bmjk.bmjk_zjb";
			String sql = Pub.getPropertiesString(propertiesFileName_gc, "GC_JDGL_TCGK");
			String ndSql_gc = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			String ndSql_zj = Pub.empty(nd)?"":" and a.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql_gc);
			List<String> list = new ArrayList<String>();
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			list.add(bs.getJson());
			//第一段结束--工程部
			sql = Pub.getPropertiesString(propertyFileName_zj, "GCJS_TJGK_SQL");
			sql = sql.replaceAll("%Condition%", ndSql_zj);
			String ltNdSql = Pub.empty(nd)?"":" and ND<='"+nd+"'";
			sql=sql.replaceAll("%ltCondition%", ltNdSql);
			BaseResultSet bs2 = DBUtil.query(conn, sql, page);
			list.add(bs2.getJson());
			//第二段结束--造价部
			
			domString = Pub.toBaseResultSetJsonString(list);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString.toString();
	}
}
