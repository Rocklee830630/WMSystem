package com.ccthanking.business.bmjkcommon;

import java.sql.Connection;

import org.springframework.stereotype.Service;

import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class BmjkCommonServiceImpl {
	private static String theadPropertyFileName = "com.ccthanking.properties.business.bmjk.bmjk_gc";
	/**
	 * 数据钻取查询--统筹计划管理
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	public String queryXmxx(String json,String bmjkLx, String ywlx, String nd, String tableName, String xmglgs) throws Exception {
		Connection conn = null;
		String domresult = "";
		try { 
			String propertyFileName = getPropertiesName(ywlx);
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String sql =Pub.getPropertiesSqlValue(theadPropertyFileName, "THEADSQL_TCJHGL");
			sql = sql.replace("%CONDSQL%",Pub.getPropertiesSqlValue(propertyFileName, bmjkLx));
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and T.ND='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			sql += orderFilter;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldDic("ISKGSJ", "SF");
			bs.setFieldDic("ISWGSJ", "SF");
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
	 * 数据钻取查询--统筹计划跟踪
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	public String queryXmxxBdxx(String json,String bmjkLx, String ywlx, String nd, String tableName, String xmglgs) throws Exception {
		Connection conn = null;
		String domresult = "";
		try { 
			String propertyFileName = getPropertiesName(ywlx);
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String sql =Pub.getPropertiesSqlValue(theadPropertyFileName, "THEADSQL_TCJHGZ");
			sql = sql.replace("%CONDSQL%",Pub.getPropertiesSqlValue(propertyFileName, bmjkLx));
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and T.ND='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			xmglgs = Pub.empty(xmglgs)?"":" and T.xmglgs='"+xmglgs+"' ";
			sql = sql.replaceAll("%xmglgsCondition%", xmglgs);
			tableName = Pub.empty(tableName)?"":" and JHPCH='"+tableName+"' ";
			sql = sql.replaceAll("%pchCondition%", tableName);
			conn.setAutoCommit(false);
			sql += orderFilter;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	private String getPropertiesName(String ywlx) {
		String propertiesName = "";
		try {
			if ("040001".equals(ywlx)) {  // 部门监控-设计监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_sj";
			} else if ("070001".equals(ywlx)) { // 部门监控-前期监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_qq";
			} else if ("050001".equals(ywlx)) { // 部门监控-工程监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_gc";
			} else if ("090001".equals(ywlx)) { // 部门监控-造价监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_zj";
			} else if ("100001".equals(ywlx)) { // 部门监控-征收监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_zs";
			} else if ("030001".equals(ywlx)) { // 部门监控-排迁监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_pq";
			}else if ("060001".equals(ywlx)) { // 部门监控-项管理公司监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_xmglgs";
			}else if ("020001".equals(ywlx)) { // 部门监控-办公室监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_bgs";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return propertiesName;
	}
}
