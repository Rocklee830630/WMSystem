package com.ccthanking.business.dtgk;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.common.BusinessUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb
 */
@Service
public class DtgkServiceImpl implements DtgkService {
	
	private static final String ND = "ccthanking_nd";
	private static final String DTGL_PROPERTIES = "com.ccthanking.properties.business.bmjk.bmjk_dtgl";

	@Override
	public String query(String json, User user, HttpServletRequest request) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user, null);
			
			
			
			String querySql = "SELECT GC_TCJH_XMXDK_ID XMID, " 
					// 项目编号
					+ "XMBH, " 
					// 项目名称
					+ "XMMC, " 
					// 项目年度
					+ "ND XMND, " 
					// 项目性质
					+ "XJXJ XMXZ, " 
					// 项目类型
					+ "XMLX, " 
					// 项目地址【建设位置】
					+ "XMDZ JSWZ, " 
					// 项目属性
					+ "XMSX, " 
					// 是否BT
					+ "ISBT SFBT, " 
					// 项目管理公司
					+ "XMGLGS XGGS," 
					// 业主代表
					+ "YZDB," 
					// 建设目标【年度目标】
					+ "JSMB NDMB," 
					// 工程，征拆，其他
					+ "GC,ZC,QT,"
					// 计划总投资额【合计】
					+ "JHZTZE , " 
					+ "(select sum(GC) from GC_TCJH_XMXDK where "+condition+") GCZJ,"
					+ "(select sum(ZC) from GC_TCJH_XMXDK where "+condition+") ZCZJ,"
					+ "(select sum(QT) from GC_TCJH_XMXDK where "+condition+") QTZJ,"
					+ "(select sum(JHZTZE) from GC_TCJH_XMXDK where "+condition+") ZJ "
					// 
					+ "FROM GC_TCJH_XMXDK XMXDK" ;
			
			condition += orderFilter;
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			
			BaseResultSet bs = DBUtil.query(conn, querySql, page);

            bs.setFieldDic("XMXZ", "XMXZ");// 项目性质
			bs.setFieldDic("XMLX", "XMLX");//项目类型
			bs.setFieldDic("XMSX", "XMSX");//项目属性
			bs.setFieldDic("XMXZ", "XMXZ");//项目属性
			bs.setFieldDic("SFBT", "SF");//是否BT
            bs.setFieldThousand("JHZTZE");// 年度总投资
            bs.setFieldThousand("GC");// 
            bs.setFieldThousand("ZC");// 
            bs.setFieldThousand("QT");// 
            bs.setFieldThousand("GCZJ");// 
            bs.setFieldThousand("ZCZJ");// 
            bs.setFieldThousand("QTZJ");// 
            bs.setFieldThousand("ZJ");// 
            bs.setFieldTranslater("XGGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");// 项目管理公司
			bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
            
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}

	@Override
	public String ztgkRs(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domresult = null;
		try {
			String currentNd = Pub.getDate("yyyy");
			String sql = Pub.getPropertiesSqlValue(DTGL_PROPERTIES, "BMJK_DTGL_ZTGK_SQL");
			sql = sql.replace(ND, currentNd);

			PageManager page=new PageManager();
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String ztjzRs(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domresult = null;
		try {
			String currentNd = Pub.getDate("yyyy");
			String sql = Pub.getPropertiesSqlValue(DTGL_PROPERTIES, "BMJK_DTGL_ZTJZQK_SQL");
			sql = sql.replace(ND, currentNd);

			PageManager page=new PageManager();
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String xjxujRs(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domresult = null;
		try {
			String currentNd = Pub.getDate("yyyy");
			String sql = Pub.getPropertiesSqlValue(DTGL_PROPERTIES, "BMJK_DTGL_XJXUJ_SQL");
			sql = sql.replace(ND, currentNd);

			PageManager page=new PageManager();
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryXx(String json) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domresult = null;
		try {
			String sql =Pub.getPropertiesSqlValue(DTGL_PROPERTIES, "XUJ_WPQ_CNT_SQL");
			
			//拼接年度查询条件
			String currentNd = Pub.getDate("yyyy");
			sql = sql.replaceAll("%ndCondition%", " and T.ND='"+currentNd+"' ");
			sql = sql.replace(ND, currentNd);
			conn.setAutoCommit(false);
			PageManager page = RequestUtil.getPageManager(json);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryXmbdxx(String json,String proSql) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domresult = null;
		try {
			String sql =Pub.getPropertiesSqlValue(DTGL_PROPERTIES, proSql);
			
			//拼接年度查询条件
			String currentNd = Pub.getDate("yyyy");
			sql = sql.replaceAll("%ndCondition%", " and T.ND='"+currentNd+"' ");
			sql = sql.replace(ND, currentNd);
			conn.setAutoCommit(false);
			PageManager page = RequestUtil.getPageManager(json);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

}
