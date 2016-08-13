package com.ccthanking.business.tcjh.jhjk;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

//按所属地区
@Service
public class JhjkServiceImpl implements JhjkService{	
	private String propertyFileName = "com.ccthanking.properties.business.bmjk.bmjk_jh";

	//项目分类
	@Override
	public String queryXMFLChart(HttpServletRequest request, String json)
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
				ndtj=" and z.nd='"+nd+"'  ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "BMJK_JH_XMFL");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("", "XMLX");
			domresult = bs.getJson();
		    HashMap linkMap = new HashMap();
	    	linkMap.put("LINKFUNCTION", "javascript:xmfl");
			List paraList = new ArrayList(); 
			paraList.add("GC_CJJH_ID");
			linkMap.put("LINKPARAM", paraList);
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
			linkMap.put("COLOR", list);
			domresult = ChartUtil.makePieChartJsonString(domresult, chartMap, linkMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	//计划编制
	@Override
	public String queryJHBZChart(HttpServletRequest request, String json)
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
				ndtj=" and x.nd='"+nd+"'  ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "BMJK_JH_JHBZ");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap linkMap = new HashMap();
	    	linkMap.put("LINKFUNCTION", "javascript:jhbz");
			List paraList = new ArrayList(); 
			paraList.add("XH");
			linkMap.put("LINKPARAM", paraList);
			HashMap chartMap = new HashMap();
			chartMap.put("pieRadius", "70");
			chartMap.put("showLegend", "1");
			chartMap.put("showLabels", "0");
			List list = new ArrayList();
			list.add(ChartUtil.chartWarnColor5);
			list.add(ChartUtil.chartColor4);
			list.add(ChartUtil.chartColor2);
			linkMap.put("COLOR", list);
			domresult = ChartUtil.makePieChartJsonString(domresult, chartMap, linkMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//按项目法人
	@Override
	public String queryXMFR(HttpServletRequest request,String json) throws Exception {
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
			String sql = Pub.getPropertiesString(propertyFileName, "BMJK_JH_XMFR");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMFR", "XMFR");
			bs.setFieldThousand("JHZTZE");
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
	public String queryZRBM(HttpServletRequest request, String json)
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
			String sql = Pub.getPropertiesString(propertyFileName, "BMJK_JH_ZRBM");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//bs.setFieldDic("ZRBM", "XMGLGS");
			bs.setFieldThousand("JHZTZE");
			bs.setFieldTranslater("ZRBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			//bs.setFieldDic("ZRBM", "ZRBM");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	//按项目进度
	@Override
	public String queryXMJD(HttpServletRequest request, String json)
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
			String sql = Pub.getPropertiesString(propertyFileName, "BMJK_JH_XMJD");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldThousand("JHZTZE");
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
	public String queryXMLX(HttpServletRequest request,String json) throws Exception {
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
			String sql = Pub.getPropertiesString(propertyFileName, "BMJK_JH_XMLX");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMLX","XMLX");
			bs.setFieldThousand("JHZTZE");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}


	//按项目性质
	@Override
	public String queryXMXZ(HttpServletRequest request, String json)
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
			String sql = Pub.getPropertiesString(propertyFileName, "BMJK_JH_XMXZ");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldThousand("JHZTZE");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	public String queryJhgzColumn2d(HttpServletRequest request, String json) {
	
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "BMJK_JH_JHGZ");
			//拼接年度查询条件
			
			String nd2 = Pub.empty(nd)?"":" and t.nd='"+nd+"' ";
			sql = sql.replaceAll("%Condition%", nd2);
			
			page.setPageRows(1000);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap<String, Object> rowMap = new HashMap<String, Object>();
			rowMap.put("BREAKNUM", "every");
			rowMap.put("TOTALNUM", "4");
			rowMap.put("flag", "1");
			
			
			rowMap.put("LINKFUNCTION", "javascript:jhgz");
			List paraList = new ArrayList(); 
			paraList.add("XH");
			paraList.add("JDXH");
			rowMap.put("LINKPARAM", paraList);
			
			List<String> colorList = new ArrayList<String>();
			colorList.add(ChartUtil.chartColor4);	// 计划完成-------蓝色
			colorList.add(ChartUtil.chartColor2); // 正常完成-------绿色
			colorList.add(ChartUtil.chartColor1); // 超期完成-------黄色
			colorList.add(ChartUtil.chartWarnColor5); // 未完成---------红色
			rowMap.put("COLOR", colorList);
					
			// 覆盖默认属性，显示图例
			HashMap<String, Object> charMap = new HashMap<String, Object>();
			charMap.put("showlegend", "1");
			charMap.put("showsum", "0");
			charMap.put("baseFontColor", "141414");
			domresult = ChartUtil.makeMSStackedColumn2DChartJsonString(domresult, charMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryJhxms(HttpServletRequest request) 
		throws Exception {
			Connection conn = null;
			String domresult = "";
			try {
				conn = DBUtil.getConnection();
				PageManager page = null;
				String nd=request.getParameter("nd");
				String ndtj="";
				if(!Pub.empty(nd))
				{
					ndtj=" and t.nd='"+nd+"'  ";
				}	
				String sql = Pub.getPropertiesString(propertyFileName, "BMJK_JH_JHXM");
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
	public String queryJhgk(HttpServletRequest request) {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = null;
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and t.nd='"+nd+"'  ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "JH_TCJH");
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
	public String queryJhbzTjgk(HttpServletRequest request) {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = null;
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and x.nd='"+nd+"'  ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "BMJK_JHBZ_TJGK");
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

	//按项目法人
	@Override
	public String queryJhgzlb(HttpServletRequest request,String json) throws Exception {
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
				ndtj=" t.nd='"+nd+"'  ";
			}
			String sql = Pub.getPropertiesString(propertyFileName, "BMJK_JHGZ_LB");
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
}
