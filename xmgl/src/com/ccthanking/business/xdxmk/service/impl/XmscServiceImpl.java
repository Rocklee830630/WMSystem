package com.ccthanking.business.xdxmk.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xdxmk.service.XmscService;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class XmscServiceImpl implements XmscService {
	
	@Override
	public String queryJdzx(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
	        condition += BusinessUtil.getSJYXCondition(null);
	        condition += BusinessUtil.getCommonCondition(user,null);
	        String id = request.getParameter("id");
	        String sql = "";
			if(Pub.empty(id)){
				id = "";
			}
			
			
	        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(null);
			conn.setAutoCommit(false);
			BaseResultSet bs = null;
			String xmOrbd ="SELECT ISNOBDXM FROM GC_JH_SJ WHERE XMID = '"+id+"' and xmbs = '0' ";
			String[][] max_pch = DBUtil.query(conn, xmOrbd);
			String isnobdxm = "0";
			if(null!= max_pch && max_pch.length > 0){
				isnobdxm = max_pch[0][0];
			}
			if(isnobdxm.equals("0")){
				sql = "select bdmc||'-'||bdbh as xmmc,ZC,ZC_SJ,PQ,PQ_SJ,KGSJ,KGSJ_SJ,WGSJ,WGSJ_SJ,(wgsj-kgsj)AS GQ,ISZC,ISPQ,ISKGSJ,ISWGSJ " +
						"from gc_jh_sj where xmbs = '1' and  xmid = '"+id+"' " +
						"order by GC_JH_SJ_ID";
			}else{
				sql = "select XMMC as xmmc,ZC,ZC_SJ,PQ,PQ_SJ,KGSJ,KGSJ_SJ,WGSJ,WGSJ_SJ,(wgsj-kgsj)AS GQ,ISZC,ISPQ,ISKGSJ,ISWGSJ " +
						"from gc_jh_sj where xmid = '"+id+"' " +
						"order by GC_JH_SJ_ID";
			}
			 bs = DBUtil.query(conn, sql, page);
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
	public String queryXmwgById(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		PageManager page = null;
		String id = request.getParameter("id");
		if(Pub.empty(id)){
			id = "";
		}
		try {
			if (page == null)
				page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "SELECT WGSJ_SJ " +
					" FROM GC_JH_SJ" +
					" WHERE XMID = '"+id+"' AND XMBS = '0' ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			domresult = bs.getJson();
			conn.commit();
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String queryJdjhjzx(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
	        condition += BusinessUtil.getSJYXCondition(null);
	        condition += BusinessUtil.getCommonCondition(user,null);
	        String id = request.getParameter("id");
	        String sql = "";
	        String title = "";
	        String where ="";
			if(Pub.empty(id)){
				id = "";
			}
	        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(null);
			conn.setAutoCommit(false);
			BaseResultSet bs = null;
			String xmOrbd ="SELECT ISNOBDXM FROM GC_JH_SJ WHERE XMID = '"+id+"' and xmbs = '0' ";
			String[][] max_pch = DBUtil.query(conn, xmOrbd);
			String isnobdxm = "0";
			if(null!= max_pch && max_pch.length > 0){
				isnobdxm = max_pch[0][0];
			}
			if(isnobdxm.equals("0")){
				title = "SELECT GC_JH_SJ_ID,BDMC||'|'||BDBH AS XMMC,";
				where = " XMBS = '1' ";
			}else{
				title = "SELECT GC_JH_SJ_ID,XMMC,";
				where = " XMBS = '0' ";
			}
			sql = "SELECT XMMC,LX,KYPF,HPJDS,GCXKZ,SGXK,CBSJPF,CQT,PQT,SGT,TBJ,CS,JLDW,SGDW,ISKYPF,ISHPJDS,ISGCXKZ,ISSGXK,ISCBSJPF,ISCQT,ISPQT,ISSGT,ISTBJ,ISCS,ISJLDW,ISSGDW FROM (" +
					title+
					"'计划' as LX,1 as XH,"+
					"KYPF AS KYPF,HPJDS AS HPJDS,GCXKZ AS GCXKZ,SGXK AS SGXK,CBSJPF AS CBSJPF,CQT AS CQT,PQT AS PQT,SGT AS SGT,TBJ AS TBJ,CS AS CS,JLDW AS JLDW,SGDW AS SGDW," +
					"ISKYPF,ISHPJDS,ISGCXKZ,ISSGXK,ISCBSJPF,ISCQT,ISPQT,ISSGT,ISTBJ,ISCS,ISJLDW,ISSGDW" +
					" FROM GC_JH_SJ WHERE XMID ='"+id+"' AND"+where +
					"union all "+
					title+
					"'实际' as LX,2 as XH,"+
					"KYPF_SJ AS KYPF,HPJDS_SJ AS HPJDS,GCXKZ_SJ AS GCXKZ,SGXK_SJ AS SGXK,CBSJPF_SJ AS CBSJPF,CQT_SJ AS CQT,PQT_SJ AS PQT,SGT_SJ AS SGT,TBJ_SJ AS TBJ,CS_SJ AS CS,JLDW_SJ AS JLDW,SGDW_SJ AS SGDW," +
					"ISKYPF,ISHPJDS,ISGCXKZ,ISSGXK,ISCBSJPF,ISCQT,ISPQT,ISSGT,ISTBJ,ISCS,ISJLDW,ISSGDW" +
					" FROM GC_JH_SJ WHERE XMID ='"+id+"' AND"+where +
					"union all "+
					title+
					"'反馈' as LX,3 as XH,"+
					"(select fkrq from GC_JH_FKQK WHERE FKLX = '1003001' AND JHSJID = GC_JH_SJ_ID AND ZXBZ = '1')AS KYPF,"+
					"(select fkrq from GC_JH_FKQK WHERE FKLX = '1003003' AND JHSJID = GC_JH_SJ_ID AND ZXBZ = '1')as HPJDS,"+
					"(select fkrq from GC_JH_FKQK WHERE FKLX = '1003005' AND JHSJID = GC_JH_SJ_ID AND ZXBZ = '1')as GCXKZ,"+
					"(select fkrq from GC_JH_FKQK WHERE FKLX = '1003007' AND JHSJID = GC_JH_SJ_ID AND ZXBZ = '1')as SGXK,"+
					"(select fkrq from GC_JH_FKQK WHERE FKLX = '1007001' AND JHSJID = GC_JH_SJ_ID AND ZXBZ = '1')as CBSJPF,"+
					"(select fkrq from GC_JH_FKQK WHERE FKLX = '1007003' AND JHSJID = GC_JH_SJ_ID AND ZXBZ = '1')as CQT,"+
					"(select fkrq from GC_JH_FKQK WHERE FKLX = '1007005' AND JHSJID = GC_JH_SJ_ID AND ZXBZ = '1')as PQT,"+
					"(select fkrq from GC_JH_FKQK WHERE FKLX = '1007007' AND JHSJID = GC_JH_SJ_ID AND ZXBZ = '1')as SGT,"+
					"(select fkrq from GC_JH_FKQK WHERE FKLX = '2001001' AND JHSJID = GC_JH_SJ_ID AND ZXBZ = '1')as TBJ,"+
					"(select fkrq from GC_JH_FKQK WHERE FKLX = '2001101' AND JHSJID = GC_JH_SJ_ID AND ZXBZ = '1')as CS,"+
					"(select fkrq from GC_JH_FKQK WHERE FKLX = '1011001' AND JHSJID = GC_JH_SJ_ID AND ZXBZ = '1')as JLDW,"+
					"(select fkrq from GC_JH_FKQK WHERE FKLX = '1010001' AND JHSJID = GC_JH_SJ_ID AND ZXBZ = '1')as SGDW,"+
					"ISKYPF,ISHPJDS,ISGCXKZ,ISSGXK,ISCBSJPF,ISCQT,ISPQT,ISSGT,ISTBJ,ISCS,ISJLDW,ISSGDW" +
					" FROM GC_JH_SJ WHERE XMID ='"+id+"' AND"+where+ ")"+
					"order by GC_JH_SJ_ID,XH";
         
			 bs = DBUtil.query(conn, sql, page);
			 domresult = bs.getJson(); 
			
			
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
