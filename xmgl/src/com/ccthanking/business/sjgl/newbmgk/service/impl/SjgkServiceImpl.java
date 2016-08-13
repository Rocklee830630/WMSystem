package com.ccthanking.business.sjgl.newbmgk.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.sjgl.newbmgk.service.SjgkService;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;

/**
 * @author xiahongbo
 * @date 2014-9-20
 */
@Service
public class SjgkServiceImpl implements SjgkService {

	private static String propertiesFileName = "com.ccthanking.properties.business.bmjk.bmjk_sj_new";
	//图形分析
	@Override
	public String queryTxfx(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String nd = request.getParameter("nd");
			String sql = Pub.getPropertiesString(propertiesFileName, "SJ_SJGL_TXFX");
			String sql_new1 = Pub.getPropertiesString(propertiesFileName, "SJ_SJGL_TXFX_NEW1");
			String sql_new2 = Pub.getPropertiesString(propertiesFileName, "SJ_SJGL_TXFX_NEW2");
			String sql_new3 = Pub.getPropertiesString(propertiesFileName, "SJ_SJGL_TXFX_NEW3");
			
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			String glNdSql = Pub.empty(nd) ? "":" AND T.ND<='"+nd+"'";
			sql=sql.replaceAll("%Condition%", ndSql);
			sql=sql.replaceAll("%glNdCon%", glNdSql);
			
			sql_new1=sql_new1.replaceAll("%Condition%", ndSql);
			sql_new1=sql_new1.replaceAll("%glNdCon%", glNdSql);
			
			sql_new2=sql_new2.replaceAll("%Condition%", ndSql);
			sql_new2=sql_new2.replaceAll("%glNdCon%", glNdSql);
			
			sql_new3=sql_new3.replaceAll("%Condition%", ndSql);
			sql_new3=sql_new3.replaceAll("%glNdCon%", glNdSql);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			BaseResultSet bs_new1 = DBUtil.query(conn, sql_new1, page);
			BaseResultSet bs_new2 = DBUtil.query(conn, sql_new2, page);
			BaseResultSet bs_new3 = DBUtil.query(conn, sql_new3, page);
			domString = bs.getJson();
			String tempDom1 = bs_new1.getJson();
			String tempDom2 = bs_new2.getJson();
			String tempDom3 = bs_new3.getJson();
			
			List<String> jsonList = new ArrayList<String>();
			jsonList.add(domString);
			jsonList.add(tempDom1);
			jsonList.add(tempDom2);
			jsonList.add(tempDom3);
			domString = Pub.toBaseResultSetJsonString(jsonList);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	//桥梁类
	@Override
	public String queryQll(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String nd = request.getParameter("nd");
			String sql = Pub.getPropertiesString(propertiesFileName, "SJ_SJGL_QLL");
			nd = Pub.empty(nd)?"":" and t.nd='"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	//道路类
	@Override
	public String queryDll(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String nd = request.getParameter("nd");
			String sql = Pub.getPropertiesString(propertiesFileName, "SJ_SJGL_DLL");
			nd = Pub.empty(nd)?"":" and t.nd='"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	
	//设计部门-设计管理-统筹概况
	@Override
	public String querySjglTcgk(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String nd = request.getParameter("nd");
			String sql = Pub.getPropertiesString(propertiesFileName, "SJ_SJGL_TCGK");
			nd = Pub.empty(nd)?"":" and t.nd='"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	
	//设计部门-交（竣）工验收管理-统筹概况
	@Override
	public String queryJgjvgTcgk(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String nd = request.getParameter("nd");
			String sql = Pub.getPropertiesString(propertiesFileName, "JS_JGJVG_TCGK_NEW");
			
			String ndSql = Pub.empty(nd)?"":" and t.nd='"+nd+"'";
			String glNdSql = Pub.empty(nd)?"":" and t.nd<='"+nd+"'";
			sql=sql.replaceAll("%Condition%", ndSql);
			sql=sql.replaceAll("%glNdCon%", glNdSql);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("SJBG_JE");	//年度计划投资-工程
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
}
