package com.ccthanking.business.cwjk;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

//财务监控
@Service
public class CwjkServiceImpl implements CwjkService{	
	private String propertyFileName = "com.ccthanking.properties.business.bmjk.bmjk_cw";

	//基本情况
	@Override
	public String queryJbqk(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String nd = request.getParameter("nd");
			String sql = Pub.getPropertiesString(propertyFileName, "BMJK_CW_JBQK");
			nd = Pub.empty(nd)?"":" and ND='"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	public String queryZfqkColumn2d(HttpServletRequest request, String json) {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "BMJK_CW_ZDQK");
			//拼接年度查询条件
			sql = sql.replaceAll("%Condition%", nd);
			
			page.setPageRows(1000);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			//System.out.println(domresult);
			HashMap<String, Object> rowMap = new HashMap<String, Object>();
			rowMap.put("BREAKNUM", "every");
			rowMap.put("TOTALNUM", "3");
			
			// 覆盖默认属性，显示图例
			HashMap<String, Object> charMap = new HashMap<String, Object>();
			charMap.put("showlegend", "1");
			domresult = ChartUtil.makeMSStackedColumn2DChartJsonString(domresult, charMap, rowMap);
			//System.out.println(domresult);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
