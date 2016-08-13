package com.ccthanking.business.xdxmk.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ccthanking.business.xdxmk.service.JhbmjkService;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class JhbmjkServiceImpl implements JhbmjkService {
	
	@Override
	public String queryJdzx(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				conn.setAutoCommit(false);
				String nd = request.getParameter("nd");
				String sql = "SELECT COUNT(GC_JH_ZT_ID)AS pc_num FROM GC_JH_ZT WHERE JHPCH not like '%零星%' and ND ='"+nd+"' AND SFYX = '1' AND SPZT = '3'";			
				String result[][] = DBUtil.query(conn, sql);
				if(Pub.emptyArray(result)){		
					domresult =result[0][0];
				}	
				else{
					domresult ="0";
				}	
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	@Override
	public String query_lxxmps(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				conn.setAutoCommit(false);
				String nd = request.getParameter("nd");
				String sql = "SELECT COUNT(GC_JH_ZT_ID)AS lx_num FROM GC_JH_ZT WHERE JHPCH  like '%零星%' and ND ='"+nd+"' AND SFYX = '1' AND SPZT = '3'";			
				String result[][] = DBUtil.query(conn, sql);
				if(Pub.emptyArray(result)){		
					domresult =result[0][0];
				}	
				else{
					domresult ="0";
				}	
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String query_ybxms(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				//PageManager page = new PageManager();
				conn.setAutoCommit(false);
				String nd = request.getParameter("nd");
				String sql = "SELECT COUNT(GC_JH_SJ_ID)AS sum_num FROM GC_JH_SJ WHERE ND ='"+nd+"' AND SFYX = '1' AND XMBS = '0'";		
				//BaseResultSet bs = DBUtil.query(conn, sql, page);
				String result[][] = DBUtil.query(conn, sql);
				if(Pub.emptyArray(result)){		
					domresult =result[0][0];
				}	
				else{
					domresult ="0";
				}
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String query_wc(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String nd = request.getParameter("nd");
			String countXmzs = request.getParameter("countXmzs");
			StringBuffer sbSql = new StringBuffer();
			String sqlContion = "";	
			if(!countXmzs.equals("0")){
				sqlContion = ",COUNT(GC_JH_SJ_ID)/"+countXmzs+"*100 AS QBWC_BFB ";
			}else{
				sqlContion = ",'0'";
			}
				
				sbSql.append("SELECT COUNT(GC_JH_SJ_ID)AS QBWC"+sqlContion+" ");
	   	        sbSql.append("FROM GC_JH_SJ T ");
	   	        sbSql.append("WHERE ");
	   	        sbSql.append("((T.ISKGSJ = '1' AND T.KGSJ IS NOT NULL) OR (T.ISKGSJ = '0')) ");
	   	        sbSql.append("AND ((T.ISWGSJ = '1' AND T.WGSJ IS NOT NULL) OR (T.ISWGSJ = '0')) ");
	   	      	sbSql.append("AND ((T.ISKYPF = '1' AND T.KYPF IS NOT NULL) OR (T.ISKYPF = '0')) ");
	   	   		sbSql.append("AND ((T.ISHPJDS = '1' AND T.HPJDS IS NOT NULL) OR (T.ISHPJDS ='0')) ");
		       	sbSql.append("AND ((T.ISGCXKZ = '1' AND T.GCXKZ IS NOT NULL) OR (T.ISGCXKZ = '0')) ");
		       	sbSql.append("AND ((T.ISSGXK = '1' AND T.SGXK IS NOT NULL) OR (T.ISSGXK ='0')) ");
		       	sbSql.append("AND ((T.ISCBSJPF = '1' AND T.CBSJPF IS NOT NULL) OR (T.ISCBSJPF = '0')) ");
		       	sbSql.append("AND ((T.ISCQT = '1' AND T.CQT IS NOT NULL) OR (T.ISCQT = '0')) ");
		       	sbSql.append("AND ((T.ISPQT = '1' AND T.PQT IS NOT NULL) OR (T.ISPQT = '0')) ");
		       	sbSql.append("AND ((T.ISSGT = '1' AND T.SGT IS NOT NULL) OR (T.ISSGT = '0')) ");
		       	sbSql.append("AND ((T.ISTBJ = '1' AND T.TBJ IS NOT NULL) OR (T.ISTBJ = '0')) ");
		       	sbSql.append("AND ((T.ISCS = '1' AND T.CS IS NOT NULL) OR (T.ISCS = '0')) ");
		       	sbSql.append("AND ((T.ISJLDW = '1' AND T.JLDW IS NOT NULL) OR (T.ISJLDW  = '0')) ");
		       	sbSql.append("AND ((T.ISSGDW = '1' AND T.SGDW IS NOT NULL) OR (T.ISSGDW = '0')) ");
		       	sbSql.append("AND ((T.ISZC = '1' AND T.ZC IS NOT NULL) OR (T.ISZC = '0')) ");
		       	sbSql.append("AND ((T.ISPQ = '1' AND T.PQ IS NOT NULL) OR (T.ISPQ = '0')) ");
		       	sbSql.append("AND ((T.ISJG = '1' AND T.JG IS NOT NULL) OR (T.ISJG = '0')) ");
		       	sbSql.append("AND T.ND = '"+nd+"' AND T.SFYX = '1' AND XMBS='0'");
		       	BaseResultSet bs = DBUtil.query(conn, sbSql.toString(), page);
				/*if(Pub.emptyArray(result)){	
					if(result[0][0].equals("0")){
						domresult ="0[0%]";
					}else{
						domresult =result[0][0]+"["+Pub.DecimalsFormat(result[0][1])+"%]";
					}
				}	
				else{
					domresult ="0[0%]";
				}	*/
		       	bs.setFieldDecimals("QBWC_BFB");
				domresult = bs.getJson();
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String query_wbz(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = new PageManager();
				conn.setAutoCommit(false);
				String nd = request.getParameter("nd");
				String countXmzs = request.getParameter("countXmzs");
				StringBuffer sbSql = new StringBuffer();
				String sqlContion = "";	
				if(!countXmzs.equals("0")){
					sqlContion = ",COUNT(GC_JH_SJ_ID)/"+countXmzs+"*100 AS WBZ_BFB";
				}else{
					sqlContion = ",'0'";
				}
				
		       	sbSql.append("SELECT COUNT(GC_JH_SJ_ID)AS WBZ"+sqlContion+" ");
				sbSql.append("FROM GC_JH_SJ T ");
				sbSql.append("WHERE ");
				sbSql.append("T.KGSJ IS NULL ");
				sbSql.append("AND T.WGSJ IS NULL ");
				sbSql.append("AND T.KYPF IS NULL ");
				sbSql.append("AND T.HPJDS IS NULL ");
				sbSql.append("AND T.GCXKZ IS NULL ");
				sbSql.append("AND T.SGXK IS NULL ");
				sbSql.append("AND T.CBSJPF IS NULL ");
				sbSql.append("AND T.CQT IS NULL ");
				sbSql.append("AND T.PQT IS NULL ");
				sbSql.append("AND T.SGT IS NULL ");
				sbSql.append("AND T.TBJ IS NULL ");
				sbSql.append("AND T.CS IS NULL ");
				sbSql.append("AND T.JLDW IS NULL ");
				sbSql.append("AND T.SGDW IS NULL ");
				sbSql.append("AND T.ZC IS NULL ");
				sbSql.append("AND T.PQ IS NULL ");
				sbSql.append("AND T.JG IS NULL ");
				sbSql.append("AND T.ND = '"+nd+"' AND T.SFYX = '1' AND XMBS='0'");
		       	
				BaseResultSet bs = DBUtil.query(conn, sbSql.toString(), page);
				/*if(Pub.emptyArray(result)){	
					if(result[0][0].equals("0")){
						domresult ="0[0%]";
					}else{
						domresult =result[0][0]+"["+Pub.DecimalsFormat(result[0][1])+"%]";
					}
				}	
				else{
					domresult ="0[0%]";
				}	*/
				bs.setFieldDecimals("WBZ_BFB");
				domresult = bs.getJson();
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String query_wtqk(String json,HttpServletRequest request) throws Exception {
		String result = "0";
		try {
				String sql = "SELECT COUNT(WTTB_INFO_ID) FROM WTTB_INFO where 1=1 ";
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				sql += StringUtils.isBlank(condition)?"":(" and " + condition);
				String results[][] = DBUtil.query(sql);
				if(null != results && results.length > 0){
					result = results[0][0];
				}
		}  catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
		}
		return result;
	}
	
	@Override
	public String query_wtqk_qt(String json,HttpServletRequest request) throws Exception {
		String result = "0";
		try {
				String sql = "SELECT COUNT(WTTB_INFO_ID) FROM WTTB_INFO where 1=1 AND WTLX NOT IN('18','20','12','21','17','22','26')  ";
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				sql += StringUtils.isBlank(condition)?"":(" and " + condition);
				String results[][] = DBUtil.query(sql);
				if(null != results && results.length > 0){
					result = results[0][0];
				}
		}  catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
		}
		return result;
	}
}
