package com.ccthanking.business.sjgl.sjzq.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.sjgl.sjzq.service.SjSjzqService;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class SjSjzqServiceImpl implements SjSjzqService{
	private static String propertyFileName = "com.ccthanking.properties.business.bmjk.bmjk_sj";
	@Override
	//统计概况查询
	public String getValue(HttpServletRequest request,String json) throws Exception {
		Connection conn = DBUtil.getConnection();
		PageManager page = RequestUtil.getPageManager(json);
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String name=request.getParameter("name");
		String index=request.getParameter("index");
		String preNd="";
		if(!Pub.empty(nd)){
			preNd=" and "+index+".nd='"+nd+"'";
		}
		try {
			conn.setAutoCommit(false);
			String ceshi_sql = Pub.getPropertiesSqlValue(propertyFileName,name);
			String sql=ceshi_sql.replaceAll("preNd", preNd);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			
			bs.setFieldDic("BGLB", "BGLB2");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJY", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JSR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

		@Override
		//统计概况查询
		public String sjbgValue(HttpServletRequest request,String json) throws Exception {
			Connection conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String domresult ="";
			//计划筛选后的个数
			String nd= request.getParameter("nd");
			String name=request.getParameter("name");
			String index=request.getParameter("index");
			String preNd="";
			if(!Pub.empty(nd)){
				preNd=" and "+index+".nd='"+nd+"'";
			}
			try {
				conn.setAutoCommit(false);
				String ceshi_sql = Pub.getPropertiesSqlValue(propertyFileName,name);
				String sql=ceshi_sql.replaceAll("preNd", preNd);
				BaseResultSet bs = DBUtil.query(conn, sql,page);
				
				bs.setFieldDic("BGLB", "BGLB2");
				bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("SJY", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JSR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				
				domresult = bs.getJson();
			} catch (Exception e) {
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
		
		@Override
		//监测检测查询
		public String jcjcValue(HttpServletRequest request,String json) throws Exception {
			Connection conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String domresult ="";
			//计划筛选后的个数
			String nd= request.getParameter("nd");
			String name=request.getParameter("name");
			String index=request.getParameter("index");
			String preNd="";
			if(!Pub.empty(nd)){
				preNd=" and "+index+".nd='"+nd+"'";
			}
			try {
				conn.setAutoCommit(false);
				String ceshi_sql = Pub.getPropertiesSqlValue(propertyFileName,name);
				String sql=ceshi_sql.replaceAll("preNd", preNd);
				BaseResultSet bs = DBUtil.query(conn, sql,page);
				bs.setFieldDic("BGLB", "BGLB");
				bs.setFieldTranslater("JSR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				domresult = bs.getJson();
			} catch (Exception e) {
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
		
	//获取表头信息
	@Override
	//统计概况查询
	public String getTable(HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String nd= request.getParameter("nd");
		String name=request.getParameter("name");
		String index=request.getParameter("index");
		String preNd="";
		if(!Pub.empty(nd)){
			preNd=" and "+index+".nd='"+nd+"'";
		}
		try {
			conn.setAutoCommit(false);
			PageManager page=new PageManager();
			String ceshi_sql = Pub.getPropertiesSqlValue(propertyFileName,name);
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
	/**
	 * 数据钻取查询设计综合
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryDrillingDataZh(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "THEADSQL_ZH");
			sql = sql.replace("%CONDSQL%",Pub.getPropertiesSqlValue(propertyFileName, proKey));
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and S.ND='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			sql += orderFilter;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("GYS");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
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
	 * 数据钻取查询拦标价
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryDrillingDataLbj(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "THEADSQL_LBJ");
			sql = sql.replace("%CONDSQL%",Pub.getPropertiesSqlValue(propertyFileName, proKey));
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and S.ND='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			sql += orderFilter;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("SBCSZ"); 
			bs.setFieldThousand("CSSDZ"); 
			bs.setFieldThousand("SJZ"); 
			bs.setFieldThousand("ZDJ");
			 bs.setFieldTranslater("ZXGS", "GC_CJDW", "GC_CJDW_ID", "DWMC");
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
	 * 数据钻取查询交竣工
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryDrillingDataJjg(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "THEADSQL_JJG");
			sql = sql.replace("%CONDSQL%",Pub.getPropertiesSqlValue(propertyFileName, proKey));
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and S.ND='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			sql += orderFilter;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldFileUpload("JIAOGONGFJ", "0028");
			bs.setFieldFileUpload("JUNGONGFJ", "0029");
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("ND","XMNF");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("ISXD","XMZT");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("SFYX", "SF");
			bs.setFieldDic("ND","XMNF");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
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
	 * 数据钻取查询交竣工
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryDrillingDataJcjc(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "THEADSQL_JCJC");
			sql = sql.replace("%CONDSQL%",Pub.getPropertiesSqlValue(propertyFileName, proKey));
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and S.ND='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			sql += orderFilter;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldFileUpload("ZJJC", "0300");
			bs.setFieldFileUpload("HFTSJC", "0301");
			bs.setFieldFileUpload("DJZSYJC", "0302");
			bs.setFieldDic("BGLB", "BGLB");
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
