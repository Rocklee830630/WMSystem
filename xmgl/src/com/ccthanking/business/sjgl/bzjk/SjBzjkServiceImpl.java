package com.ccthanking.business.sjgl.bzjk;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;


@Service
public  class SjBzjkServiceImpl implements SjBzjkService {
	private String ywlx=YwlxManager.GC_SJ;
	@Override
	//统计概况查询
	public String queryTjgk(HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and jhsj.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="tongjigaikuang";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			bs.setFieldDecimals("JBGH_CENT");
			bs.setFieldDecimals("TJRWS_CENT");
			bs.setFieldDecimals("QDKCBG_CENT");
			bs.setFieldDecimals("WCSJ_CENT");
			domresult = bs.getJson();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	/**
	 * 读取配置文件
	 * @param url文件路径
	 * @param name 名字
	 * @return  
	 * @throws UnsupportedEncodingException 
	 */
	private String queryProperties(String url,String name) throws UnsupportedEncodingException {
		Properties pp = new Properties();
		InputStream in = null;
		in = SjBzjkServiceImpl.class.getResourceAsStream(url);
		 Reader reader=new InputStreamReader(in,"UTF-8");
		try {
			pp.load(reader);
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		in = null;
		String sql = pp.getProperty(name);
		pp = null;
		return sql;
	}
	@Override
	public String queryCQT(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and jhsj.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="cqtSql";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			bs.setFieldDecimals("CQTSJWC_CENT");
			bs.setFieldDecimals("CQTAQWC_CENT");
			bs.setFieldDecimals("CQTCQWC_CENT");
			domresult = bs.getJson();
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryPQT(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and jhsj.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="pqtSql";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			bs.setFieldDecimals("PQTSJWC_CENT");
			bs.setFieldDecimals("PQTAQWC_CENT");
			bs.setFieldDecimals("PQTCQWC_CENT");
			domresult = bs.getJson();
		
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String querySGT(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and jhsj.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="sgtSql";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			
			bs.setFieldDecimals("SGTSJWC_CENT");
			bs.setFieldDecimals("SGTAQWC_CENT");
			bs.setFieldDecimals("SGTCQWC_CENT");
			domresult = bs.getJson();
		
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryZJXX(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and S.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="zjsql";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("%ndCondition%", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			bs.setFieldDecimals("LBJ_CENT");
			bs.setFieldThousand("YWCGSJE");
			bs.setFieldThousand("CGSJE");
			bs.setFieldThousand("YZCGSJE");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryJJG(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and S.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="jjg";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("%ndCondition%", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			bs.setFieldDecimals("YBZLBJBFB");
			bs.setFieldDecimals("JIAOG_CENT");
			bs.setFieldDecimals("JUNG_CENT");
			domresult = bs.getJson();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryJCJC(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and jhsj.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="jcjc";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			bs.setFieldDecimals("ZJJC_CENT");
			bs.setFieldDecimals("HFTS_CENT");
			bs.setFieldDecimals("DJZ_CENT");
			domresult = bs.getJson();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String querySJBG(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and jhsj.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="sjbgsql";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			bs.setFieldThousand("BGFY");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String querySJXM(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and jhsj.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="SJBGXM";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String querySJBD(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and jhsj.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="SJBGBD";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String querySJY(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and jhsj.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="SJY";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryJCLX(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		String result="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and jhsj.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="jclx";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			domresult = bs.getJson();
			result=ChartUtil.makePieChartJsonString(domresult,null,null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return result;
	}
	@Override
	public String querysjxms(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		String result="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and jhsj.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="sjxms";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			domresult = bs.getJson();
			result=ChartUtil.makeColumn2DChartJsonString(domresult,null,null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return result;
	}
	@Override
	public String queryzlsf(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		String result="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String preNd=(Pub.empty(nd))?"":"and jhsj.nd ='"+nd+"'";
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String url="/com/ccthanking/properties/business/bmgk/sj.properties";
			String name="zlsf";
			String ceshi_sql = queryProperties(url,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			domresult = bs.getJson();
			result=ChartUtil.makeMSStackedColumn2DChartJsonString(domresult,null,null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return result;
	}

}
