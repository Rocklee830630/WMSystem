package com.ccthanking.business.pqgl.bmgk.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.pqgl.bmgk.service.PqBmgkService;
import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;

@Service
public class PqBmgkServiceImpl implements PqBmgkService{
	private String propertyFileName;
    private ResourceBundle resourceBundle;
    public PqBmgkServiceImpl() {
    	Locale locale = Locale.getDefault();
        propertyFileName = "com.ccthanking.properties.business.bmgk.pq";
        resourceBundle = ResourceBundle.getBundle(propertyFileName,locale);
    }
    public String getString(String key) {
        if (key == null || key.equals("") || key.equals("null")) {
            return "";
        }
        String result = "";
        try {
            result = new String(resourceBundle.getString(key).getBytes("iso-8859-1"),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static void main(String args[]){
    	PqBmgkServiceImpl p = new PqBmgkServiceImpl();
    }
	@Override
	public String queryPqTjgk(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = this.getString("TJGKSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":"ND='"+nd+"'";
			page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDecimals("JBPQTXMBFB");	//具备排迁图项目百分比
			bs.setFieldDecimals("YWCPQXMBFB");	//已完成排迁项目百分比
			bs.setFieldDecimals("CDYJXMBFB");	//场地移交项目百分比
			bs.setFieldDecimals("AQWCXMBFB");	//按期完成项目百分比
			bs.setFieldDecimals("YQWCXMBFB");	//延期完成项目百分比
			bs.setFieldDecimals("CQWCXMBFB");	//超期完成项目百分比
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
	public String queryPqRwxx(HttpServletRequest request) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = this.getString("PQRWSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":"ND='"+nd+"'";
			page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDecimals("SJYKGRWBFB");	//实际已开工任务百分比
			bs.setFieldDecimals("YQKGBFB");		//延期开工百分比
			bs.setFieldDecimals("SJYWGRWBFB");	//实际已完工任务百分比
			bs.setFieldDecimals("YQWGBFB");		//延期完工百分比
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
	public String queryPqChart(HttpServletRequest request) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = this.getString("PQCHARTSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and a.ND='"+nd+"'";
			sql = sql.replaceAll("\\*ndCondition\\*", nd);
			page.setPageRows(1000);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap rowMap = new HashMap();
			rowMap.put("BREAKNUM", "2");
			rowMap.put("TOTALNUM", "4");
			List list = new ArrayList();
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartWarnColor5);
			list.add(ChartUtil.chartColor3);
			list.add(ChartUtil.chartWarnColor5);
			rowMap.put("COLOR", list);
			
					
			// 覆盖默认属性，显示图例
			HashMap<String, Object> charMap = new HashMap<String, Object>();
			charMap.put("showlegend", "1");
			charMap.put("showsum", "0");
			charMap.put("baseFontColor", "141414");
			
			domresult = ChartUtil.makeMSStackedColumn2DChartJsonString(domresult, charMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryPqNyxx(HttpServletRequest request) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = this.getString("PQNYSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":"ND='"+nd+"'";
			page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("YSBYSGJ");	//已上报预算共计
			bs.setFieldThousand("YQDHTGJ");	//已签订合同共计
			bs.setFieldThousand("SDZ");	//审定值
			bs.setFieldThousand("HJ");	//核减
			bs.setFieldThousand("YSZHTGJ");	//预算值合同共计
			bs.setFieldThousand("SDZHTGJ");	//审定值合同共计
			bs.setFieldThousand("YWCZF");	//已完成支付
			bs.setFieldDecimals("WCPSBFB");	//完成评审百分比
			bs.setFieldDecimals("HJBFB");	//核减百分比
			bs.setFieldDecimals("ZFL");	//核减百分比
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
