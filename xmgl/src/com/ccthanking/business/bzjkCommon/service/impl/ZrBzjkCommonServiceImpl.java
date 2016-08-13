package com.ccthanking.business.bzjkCommon.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.bzjkCommon.service.ZrBzjkCommonService;
import com.ccthanking.common.BzjkCommon;
import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @author xiahongbo
 * @date 2014-12-16
 */
@Service
public class ZrBzjkCommonServiceImpl implements ZrBzjkCommonService {
	private static String propertiesFileName = "com.ccthanking.properties.business.bmjk.bmjk_zr";
	private static String propertiesFileName_wt = "com.ccthanking.properties.business.bmgk.wttb";
	private static String propertiesFileName_lj = "com.ccthanking.properties.business.bzjklj.zr_xx";
	@Override
	public String queryFxwtTjgk(HttpServletRequest request, String msg) {
		Connection conn = null;
		String domresult = null;
		try {
			conn = DBUtil.getConnection();
			PageManager page = null;
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd)){
				ndtj=" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
			}	
			String sql = Pub.getPropertiesString(propertiesFileName, "FXWT_TJGK_SQL");
			sql=sql.replaceAll("%ndCondition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询主办领导叠加柱形图
	@Override
	public String queryZbldChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertiesFileName_wt, "ZBLDSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			List list = new ArrayList();
			HashMap rowMap = new HashMap();
			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			HashMap chartMap = new HashMap();
			chartMap.put("legend-x", "left");
			domresult = ChartUtil.makeBarEChartJsonString(domresult, chartMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//查询主办领导叠加柱形图
	@Override
	public String queryZbbmChart(HttpServletRequest request)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			String sql =Pub.getPropertiesSqlValue(propertiesFileName_wt, "ZBBMSQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(I.LRSJ,'yyyy')='"+nd+"' ";
//			page.setFilter(nd);
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			List list = new ArrayList();
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor6);
			HashMap rowMap = new HashMap();
			rowMap.put("LINKFUNCTION", "javascript:showDataDetail");
			List paramList = new ArrayList();
			paramList.add("LABEL");
			paramList.add("FIELDNAME");
			rowMap.put("LINKPARAM", paramList);
			rowMap.put("COLOR", list);
			HashMap chartMap = new HashMap();
			chartMap.put("legend-x", "left");
			chartMap.put("EFFECTIVE", true);
			domresult = ChartUtil.makeBarEChartJsonString(domresult, chartMap, rowMap);
			if(domresult.equals("{}")){
				domresult="0";	
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryYkgxmqkColumn2d(HttpServletRequest request, String msg) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			
			String sql = Pub.getPropertiesString(propertiesFileName, "YKGXMQK");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
			HashMap chartMap = new HashMap();
			chartMap.put("pieRadius", "70");
			chartMap.put("showLegend", "1");
			chartMap.put("showLabels", "0");
			HashMap rowMap = new HashMap();
			List list = new ArrayList();
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor1);
			list.add(ChartUtil.chartColor4);
			rowMap.put("COLOR", list);
			domString = ChartUtil.makePieEChartJsonString(domString, chartMap, rowMap);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	@Override
	public String queryJhssxmColumn2d(HttpServletRequest request, String msg) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			
			String sql = Pub.getPropertiesString(propertiesFileName, "JHSSXMQK");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			sql = sql.replaceAll("%_nd%", nd);
			BaseResultSet bs = DBUtil.query(conn, sql, page);

			domString = bs.getJson();
			HashMap chartMap = new HashMap();
			chartMap.put("pieRadius", "70");
			chartMap.put("showLegend", "1");
			chartMap.put("showLabels", "0");
			HashMap rowMap = new HashMap();
			List list = new ArrayList();
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor1);
			rowMap.put("COLOR", list);
			domString = ChartUtil.makePieEChartJsonString(domString, chartMap, rowMap);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	@Override
	public String queryList(HttpServletRequest request, String msg) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertiesFileName, "JHJZLB");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	@Override
	public String queryNDRW(HttpServletRequest request, String msg) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertiesFileName, "NDRW");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and T.ND='"+nd+"'";
			String XndSql = Pub.empty(nd)?"":" and x.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			sql = sql.replaceAll("%xCondition%", XndSql);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//bs.setFieldMoveToLeft("LWYJ_JHZTZE");
			domString = bs.getJson();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String queryListQqsg(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		String sgflag = request.getParameter("sgflag");
		//页面查询条件施工和监理模糊查询
		String sgjddw= request.getParameter("SGJLDW");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String condiSql="";
			String flagCondition="";
			String proKeyCondition=" ";
			if("1".equals(sgflag)){
				flagCondition= " and  A.WCZS >= ZS ";  
			}else{
				flagCondition= " and  (A.WCZS < ZS or WCZS is null )";  
			}
			if("SGXKVALUE".equals(proKey)){
				proKeyCondition= "0022";  
			}else{
				proKeyCondition= "0015";  
			}
			 condiSql = Pub.getPropertiesString(propertiesFileName_lj, "sgList");
			String ndSql = Pub.empty(nd)?"":" and T.ND='"+nd+"'";
			condiSql = condiSql.replaceAll("%Condition%", ndSql);
			condiSql = condiSql.replaceAll("%proKeyCondition%", proKeyCondition);
			condiSql = condiSql.replaceAll("%flagCondition%", flagCondition);
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			String orderFilter = RequestUtil.getOrderFilter(json);
			condition += " and qqsx.SJWYBH(+)=jhsj.SJWYBH and sx.SJWYBH(+) = jhsj.SJWYBH";
			condition += " and jhsj.bdbs=1 and jhsj.xmid in (select xmid from GC_JH_SJ where gc_jh_sj_id in("+condiSql+")) ";
			condition += orderFilter;
			PageManager page = RequestUtil.getPageManager(json);
			
			page.setFilter(condition);
			String sql="select distinct JHSJ.XMBS,jhsj.pxh,jhsj.xmbh,jhsj.bdbh,jhsj.xmmc,jhsj.bdmc,qqsx.gc_zgb_qqsx_id as ywbid,qqsx.xdkid,jhsj.xmid,jhsj.bdid,jhsj.gc_jh_sj_id as jhsjid,jhsj.nd,qqsx.SGXKBJSJ as bjsj, qqsx.SGXKBBLSX as bblsx," +
					"sx.BJFK, sx.QTSXFK, sx.ZLJDFK, sx.AQJDFK,sx.ZJGLFK,sx.STQFK, sx.ZFJCFK,sx.SGXKFK ,sx.CZWT, " +
					"sx.BJ , sx.QTSX , sx.ZLJD , sx.AQJD ,jhsj.SJWYBH, sx.ZJGL , sx.STQ , sx.ZFJC , sx.SGXK,(case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ " +
					"from (select SJWYBH, " +
					"max(decode(fjlx, '0015', jhsjid, '')) BJ," +
					"max(decode(fjlx, '0015', blsj, '')) BJFK, " +
					"max(decode(fjlx, '0016', jhsjid, '')) QTSX, " +
					"max(decode(fjlx, '0016', blsj, '')) QTSXFK, " +
					"max(decode(fjlx, '0017', jhsjid, '')) ZLJD, " +
					"max(decode(fjlx, '0017', blsj, '')) ZLJDFK, " +
					"max(decode(fjlx, '0018', jhsjid, '')) AQJD," +
					"max(decode(fjlx, '0018', blsj, '')) AQJDFK, " +
					"max(decode(fjlx, '0019', jhsjid, '')) ZJGL, " +
					"max(decode(fjlx, '0019', blsj, '')) ZJGLFK, " +
					"max(decode(fjlx, '0020', jhsjid, '')) STQ, " +
					"max(decode(fjlx, '0020', blsj, '')) STQFK, " +
					"max(decode(fjlx, '0021', jhsjid, '')) ZFJC," +
					"max(decode(fjlx, '0021', blsj, '')) ZFJCFK, " +
					"max(decode(fjlx, '0022', jhsjid, '')) SGXK," +
					"max(decode(fjlx, '0022', blsj, '')) SGXKFK, " +
					
					"(select czwt from " +
					"(select czwt,SJWYBH, " +
					"row_number() over(partition by SJWYBH order by blsj desc) as line " +
					"from GC_QQSX_SXFJ  where dfl = '0' and sfyx = '1') sx2 " +
					
					"where line = '3' and sx2.SJWYBH(+)=sx1.SJWYBH) as czwt from GC_QQSX_SXFJ sx1 " +
					"where sfyx='1' group by sjwybh) sx,  (select * from VIEW_GC_JH_SJ )  jhsj,(select * from Gc_Zgb_Qqsx where sfyx='1') qqsx  ";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("BBLSX", "SGXKSX");
			bs.setFieldFileUpload("BJ", "0015");
			bs.setFieldFileUpload("QTSX", "0016");
			bs.setFieldFileUpload("ZLJD", "0017");
			bs.setFieldFileUpload("AQJD", "0018");
			bs.setFieldFileUpload("ZJGL", "0019");
			bs.setFieldFileUpload("STQ", "0020");
			bs.setFieldFileUpload("ZFJC", "0021");
			bs.setFieldFileUpload("SGXK", "0022");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String queryListLx(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		String sgflag = request.getParameter("sgflag");
		//页面查询条件施工和监理模糊查询
		String sgjddw= request.getParameter("SGJLDW");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			PageManager page = RequestUtil.getPageManager(json);
			
			String condiSql="";
			String flagCondition="";
			String proKeyCondition=" ";
			String proKeySxCondition=" ";
			if("1".equals(sgflag)){
				flagCondition= " and  a.GC_JH_SJ_ID is not null  ";  
			}else{
				flagCondition= " and  a.GC_JH_SJ_ID is  null ";  
			}
			if("TDVALUE".equals(proKey)){
				proKeyCondition= "2022"; 
				proKeySxCondition="LXKYBBLSX";
				
			}else if("GHVALUE".equals(proKey)){
				proKeyCondition= "0008";  
				proKeySxCondition="GHSPBBLSX";
			}
			else if("KYPFVALUE".equals(proKey)){
				proKeyCondition= "2024"; 
				proKeySxCondition="LXKYBBLSX";
			}
			else if("XMJYSVALUE".equals(proKey)){
				proKeyCondition= "2020";
				proKeySxCondition="LXKYBBLSX";
			}
			 condiSql = Pub.getPropertiesString(propertiesFileName_lj, "lxList");
			String ndSql = Pub.empty(nd)?"":" and T.ND='"+nd+"'";
			condiSql = condiSql.replaceAll("%Condition%", ndSql);
			condiSql = condiSql.replaceAll("%proKeySxCondition%", proKeySxCondition);
			condiSql = condiSql.replaceAll("%proKeyCondition%", proKeyCondition);
			condiSql = condiSql.replaceAll("%flagCondition%", flagCondition);
		
			if("GHVALUE".equals(proKey)){
				String querySqlGH="select distinct jhsj.pxh,jhsj.xmbh,jhsj.lrsj,jhsj.xmmc,qqsx.gc_zgb_qqsx_id as ywbid,qqsx.xdkid,jhsj.xmid,jhsj.gc_jh_sj_id as jhsjid,jhsj.nd,qqsx.ghspbjsj as bjsj, qqsx.ghspbblsx as bblsx,sx.XZYJS,sx.JSXZXMYJS,sx.JSYDGHXKZ,jhsj.SJWYBH,sx.YDXKZ,sx.JSGCGHXKZ,sx.GCXKZ,sx.CZWT,(case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ from (select sjwybh, max(decode(fjlx, '0007', jhsjid, '')) JSXZXMYJS,max(decode(fjlx, '0007', blsj, '')) XZYJS, max(decode(fjlx, '0008', jhsjid, '')) JSYDGHXKZ, max(decode(fjlx, '0008', blsj, '')) YDXKZ, max(decode(fjlx, '0009', jhsjid, '')) JSGCGHXKZ, max(decode(fjlx, '0009', blsj, '')) GCXKZ, (select czwt from (select czwt,sjwybh, row_number() over(partition by SJWYBH order by blsj desc) as line from GC_QQSX_SXFJ  where dfl = '1' and sfyx = '1') sx2 where line = '1' and sx2.sjwybh(+)=sx1.sjwybh) as czwt from GC_QQSX_SXFJ sx1 where sfyx='1' group by sjwybh) sx, (select * from GC_JH_SJ where sfyx='1')  jhsj,(select * from Gc_Zgb_Qqsx where sfyx='1') qqsx" +
						"  where  qqsx.sjwybh(+)=jhsj.sjwybh and sx.sjwybh(+) = jhsj.sjwybh and jhsj.bdid is null and jhsj.GC_JH_SJ_ID IN ("+condiSql+")  "+condition +" order by  jhsj.xmbh,jhsj.pxh asc ";
				BaseResultSet bs1 = DBUtil.query(conn, querySqlGH, page);
				
				bs1.setFieldDic("BBLSX", "GHSX");
				bs1.setFieldFileUpload("JSXZXMYJS", "0007");
				bs1.setFieldFileUpload("JSYDGHXKZ", "0008");
				bs1.setFieldFileUpload("JSGCGHXKZ", "0009");
				domString = bs1.getJson();	
			}else{
				String querySql="select distinct jhsj.pxh,jhsj.lrsj,jhsj.xmbh,jhsj.xmmc,qqsx.gc_zgb_qqsx_id as ywbid,qqsx.xdkid,jhsj.xmid,jhsj.gc_jh_sj_id as jhsjid,jhsj.nd,qqsx.LXKYBJSJ as bjsj, qqsx.LXKYBBLSX as bblsx,sx.XMJYSPF,sx.JYSFK,sx.HPPF,sx.HPFK,sx.TDYJH,jhsj.SJWYBH,sx.TDYJFK,sx.GDZCTZXM,sx.JNFK,sx.KYPF,sx.KYFK,sx.CZWT,(case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ from (select sjwybh,max(decode(fjlx, '2020', sjwybh, '')) XMJYSPF,max(decode(fjlx, '2020', blsj, '')) JYSFK, max(decode(fjlx, '2021', sjwybh, '')) HPPF, max(decode(fjlx, '2021', blsj, '')) HPFK, max(decode(fjlx, '2022', sjwybh, '')) TDYJH, max(decode(fjlx, '2022', blsj, '')) TDYJFK, max(decode(fjlx, '2023', sjwybh, '')) GDZCTZXM, max(decode(fjlx, '2023', blsj, '')) JNFK, max(decode(fjlx, '2024', sjwybh, '')) KYPF, max(decode(fjlx, '2024', blsj, '')) KYFK, (select czwt from (select czwt,sjwybh, row_number() over(partition by sjwybh order by blsj desc) as line from GC_QQSX_SXFJ  where dfl = '0' and sfyx = '1') sx2 where line = '1' and sx2.sjwybh(+)=sx1.sjwybh) as czwt from GC_QQSX_SXFJ sx1 where sfyx='1' group by sjwybh) sx, (select * from GC_JH_SJ where sfyx='1')  jhsj,(select * from Gc_Zgb_Qqsx where sfyx='1') qqsx " +
						"  where   qqsx.sjwybh(+)=jhsj.sjwybh  and sx.sjwybh(+) = jhsj.sjwybh and jhsj.bdid is null and jhsj.GC_JH_SJ_ID IN ("+condiSql+")  "+condition +"  order by  jhsj.xmbh,jhsj.pxh asc ";
				
				BaseResultSet bs = DBUtil.query(conn, querySql, page);
				bs.setFieldDic("BBLSX", "LXKYFJLX");
				bs.setFieldFileUploadWithWybh("XMJYSPF", "2020");
				bs.setFieldFileUploadWithWybh("HPPF", "2021");
				bs.setFieldFileUploadWithWybh("TDYJH", "2022");
				bs.setFieldFileUploadWithWybh("GDZCTZXM", "2023");
				bs.setFieldFileUploadWithWybh("KYPF", "2024");
				domString = bs.getJson();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String querySjglList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			
			PageManager page = RequestUtil.getPageManager(json);
			String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String tiaojian = request.getParameter("tiaojian");
			String sgjldw = request.getParameter("SGJLDW");
			
			String flagCondition="";
			String proKeyCondition="";
			String zblx="";
			if("1".equals(tiaojian)){
				flagCondition= "  is not null  ";  
			}else{
				flagCondition= "  is  null ";  
			}
			
			if("CQTVALUE".equals(proKey)){
				proKeyCondition= " cqt_sj "; 
				
			}else if("PQTVALUE".equals(proKey)){
				
				proKeyCondition= " pqt_sj ";  
				
			}else if("SGTVALUE".equals(proKey)){
				
				proKeyCondition= " sgt_sj "; 
				
			}else if("TBJVALUE".equals(proKey)){
				
				proKeyCondition= " tbj_sj ";  
				
			}else if("CSVALUE".equals(proKey)){
				
				proKeyCondition= " cs_sj "; 
			}else if("SGDWVALUE".equals(proKey)){
				
				proKeyCondition= " sgdw_sj ";  
				zblx="13";
				
			}else if("JLDWVALUE".equals(proKey)){
				
				proKeyCondition= " jldw_sj ";
				zblx="12";
			}
			
			String condiSql = Pub.getPropertiesSqlValue(propertiesFileName_lj,"sjList");

			String ndSql = Pub.empty(nd)?"":" and T.ND='"+nd+"'";
			condiSql = condiSql.replaceAll("%Condition%", ndSql);
			condiSql = condiSql.replaceAll("%proKeyCondition%", proKeyCondition);
			condiSql = condiSql.replaceAll("%flagCondition%", flagCondition);
			
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			//页面查询条件施工和监理模糊查询
			if(!Pub.empty(sgjldw)){
				condition=condition+" and (sg.dwmc like '%"+sgjldw+"%' or jl.dwmc like '%"+sgjldw+"%') ";
			}
			if("CQTVALUE".equals(proKey)||"PQTVALUE".equals(proKey)||"SGTVALUE".equals(proKey)){
				domString =querySjgl( condiSql,  condition, conn, page);
				
			}else if("TBJVALUE".equals(proKey)||"CSVALUE".equals(proKey)){
				
				domString =queryZJ( condiSql,  condition, conn, page,nd);
				
			}else if("SGDWVALUE".equals(proKey)||"JLDWVALUE".equals(proKey)){
				if("1".equals(tiaojian)){
					domString =queryZtbxq( condiSql,  condition, conn, page, zblx);
				}else{
					domString =queryTcjhList( condiSql,  condition, conn, page);
				}
			}  
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	public String querySjgl(String condiSql, String condition,Connection conn,PageManager page) {
		String sql = "select distinct jhsj.xmmc,jhsj.bdmc,jhsj.XMDZ,jhsj.xmxz,jhsj.JSMB ndmb ," +
				" jhsj.KYPF_SJ kybg,jhsj.SJdwid,jhsj.cbsjpf_sj cspf,sj.WCSJ_SGT_SSB," +
				" sj.WCSJ_SGT_ZSB,jhsj.cqt_sj,jhsj.xmbh, jhsj.XMBS, jhsj.PXH " +
				" from view_gc_jh_sj jhsj, Gc_Sj sj" +
				" where jhsj.sjwybh = sj.sjwybh(+)" +
				" and jhsj.gc_jh_sj_id in ("+condiSql+") " + condition + 
				" order by  jhsj.XMBH,jhsj.XMBS,jhsj.PXH ASC ";
		
		BaseResultSet bs = DBUtil.query(conn, sql, page);
		bs.setFieldTranslater("SJDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		bs.setFieldDic("XMXZ", "XMXZ");
		String domString = bs.getJson();
		return domString;
	}
	public String queryZJ(String condiSql, String condition,Connection conn,PageManager page,String nd) {
		String sql = "select DISTINCT gc_zjb_lbjb_id, jhsj.GC_JH_SJ_ID as jhsjid,jhsj.sjwybh, jhsj.xmbh,  jhsj.nd, jhsj.xmsx, jhsj.pxh, jhsj.xmmc, jhsj.bdmc, jhsj.xmid, jhsj.bdbh,jhsj.bdid, lbj.ISQTBMFZ,lbj.ISXYSCS,lbj.ZJBZZT,lbj.csbgbh, " +
				"lbj.txqsj, jhsj.XMBS,  lbj.zbsj, lbj.tzjjsj, lbj.zxgsj, lbj.sgfajs, lbj.zbwjjs, lbj.zxgsrq, lbj.jgbcsrq, lbj.sbcsz, lbj.czswrq, lbj.cssdz, lbj.sjz, lbj.sjbfb, lbj.zdj, lbj.bz,lbj.SBCSZRQ,lbj.CSSDZRQ,lbj.ISZH,lbj.CSSX, jhsj.iscs," +
				" jhsj.xmxz,jhsj.ISBT,jhsj.xmglgs ,jhsj.istbj,jhsj.cs_sj, jhsj.tbj_sj, nvl(cbsjpf.gs, cbsjpf.sse) as gys,  (select max(dyqk.twrq) from GC_ZJB_DYQK dyqk where dyqk.SJWYBH(+)=jhsj.SJWYBH and dyqk.sfyx='1') as YWRQ ,(select max(dyqk.dyrq) from GC_ZJB_DYQK dyqk where" +
				" dyqk.SJWYBH(+)=jhsj.SJWYBH and dyqk.sfyx='1') as HFRQ, decode(lbj.ZXGS,'', f.dsfjgid,lbj.ZXGS)  ZXGS, c.lrsj,   (case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ " +
				" from GC_ZJB_LBJB lbj,VIEW_GC_JH_SJ jhsj,(select a.gc_ztb_xq_id,b.SJWYBH,a.lrsj from gc_ztb_xq a, gc_ztb_jhsj b where b.xqid = a.GC_ZTB_XQ_ID  and a.ZBLX = '14' and a.sfyx='1' and b.sfyx='1') c,(select * from  gc_ztb_xqsj_ys where sfyx='1') e,(select * from  gc_ztb_sj where sfyx='1') f,(select * from Gc_Sj_Cbsjpf where sfyx = '1') cbsjpf  " +
				" where 1=1  and jhsj.nd= "+nd+" and lbj.SJWYBH(+)=jhsj.SJWYBH  and jhsj.SJWYBH = cbsjpf.SJWYBH(+) and c.gc_ztb_xq_id=e.ztbxqid(+) and e.ztbsjid=f.gc_ztb_sj_id(+) and jhsj.SJWYBH=c.SJWYBH(+)  " +
				"   "+condition+"  " +
				"and jhsj.sjwybh in (select t.sjwybh from view_gc_jh_sj t where t.gc_jh_sj_id in ("+condiSql+") and t.sjwybh is not null ) " +
						" order by  jhsj.XMBH,jhsj.XMBS,jhsj.PXH ASC ";  
		
		BaseResultSet bs = DBUtil.query(conn, sql, page);
		bs.setFieldOrgDept("XMGLGS"); // 项目管理公司
		bs.setFieldTranslater("ZXGS", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		bs.setFieldDic("XMXZ", "XMXZ");
		bs.setFieldDic("ISBT", "SF");
		bs.setFieldThousand("GYS");
		bs.setFieldThousand("SBCSZ");
		bs.setFieldThousand("CSSDZ");
		String domString = bs.getJson();
		return domString;
	}
	public String queryZtbxq(String condiSql, String condition,Connection conn,PageManager page,String zblx) {
		String sql = "select distinct X.gc_ztb_xq_id, X.gzmc, X.gznr, X.zzyjyq, X.sxyq, X.jsyq, X.cgmbyq, X.pbryyq, X.pbsbyq, X.tbjfs, X.zbfs, X.qtyq, X.zblx, X.yse, X.xqdwjbr, X.xqdwjbrsj, X.xqdwfzr, X.xqdwfzrsj, X.zbbjbr, X.zbbfzr, X.xqzt, X.ywlx, X.sjbh, X.bz, X.lrr, X.lrsj, X.lrbm, X.lrbmmc, X.gxr, X.gxsj, X.gxbm, X.gxbmmc, X.sjmj, X.sfyx,X.QFBZ,X.TGBZ,X.nd " +
					" from gc_ztb_xq X,(select * from  GC_ZTB_JHSJ where SFYX='1') a,(select * from  gc_jh_sj where SFYX='1') T " +
					" where X.sfyx = '1'  and x.gc_ztb_xq_id=a.xqid(+) and a.jhsjid=T.gc_jh_sj_id(+) and x.zblx='"+zblx+"' " +
					" AND a.jhsjid IN ("+condiSql+") " +
					" " + condition +   " " +
					" order by  LRSJ desc ";
		BaseResultSet bs = DBUtil.query(conn, sql, page);
		bs.setFieldDic("ZBLX", "ZBLX");//招标类型
		bs.setFieldDic("ZBFS", "ZBFS");//招标方式
		bs.setFieldDic("XQZT", "SF");//招标方式
		bs.setFieldDic("XQZT", "XQZT");//垫资方式
		bs.setFieldDic("TBJFS", "TBBJFS");//投标报价方式
		bs.setFieldThousand("YSE");//预算额
		bs.setFieldSjbh("sjbh");//事件编号
		bs.setFieldTranslater("JLDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC"); // 监理单位
		bs.setFieldTranslater("LRBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");	// 录入部门
		String domString = bs.getJson();
		return domString;
	}
	public String queryTcjhList(String condiSql, String condition,Connection conn,PageManager page) throws UnsupportedEncodingException {
		String sql = Pub.getPropertiesSqlValue("com.ccthanking.properties.business.bzjklj.lj_list","TCJh_DETAIL");
		
		sql = sql.replaceAll("%queryCondition%", condition);
		sql = sql.replaceAll("%condition_sql%", condiSql);
		
		BaseResultSet bs = DBUtil.query(conn, sql, page);
		bs.setFieldThousand("JH_XJ");	//计划总投资-小计
		bs.setFieldThousand("JH_GC");	//计划总投资-工程
		bs.setFieldThousand("JH_ZQ");	//计划总投资-征地排迁
		bs.setFieldThousand("JH_QT");	//计划总投资-其他
		bs.setFieldThousand("ND_GC");	//年度计划投资-工程
		bs.setFieldThousand("ND_ZQ");	//年度计划投资-工程
		bs.setFieldThousand("ND_QT");	//年度计划投资-工程
		bs.setFieldThousand("ND_XJ");	//年度计划投资-工程
		bs.setFieldThousand("ND_XJ");	//年度计划投资-工程
		bs.setFieldDic("ZRBM", "ZRBM");
		bs.setFieldDic("XMFR", "XMFR");
		bs.setFieldDic("XMJSJD", "XMJSJD");
		bs.setFieldDic("XMXZ", "XMXZ");
		bs.setFieldDic("NDMB", "NDMB");//年度目标
		bs.setFieldDic("XMLX", "XMLX");
		bs.setFieldDic("SSXZ", "XMSX");
		bs.setFieldDic("CJXMSX", "CJXMSX");
		bs.setFieldDic("ISXD", "SF");
		bs.setFieldDic("QY", "QY");
		bs.setFieldDic("ISBT", "SF");
		bs.setFieldDic("XJXJ", "XMXZ");
		bs.setFieldDic("BGLB", "BGLB2");
		bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
		bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		String domString = bs.getJson();
		return domString;
	}
}
