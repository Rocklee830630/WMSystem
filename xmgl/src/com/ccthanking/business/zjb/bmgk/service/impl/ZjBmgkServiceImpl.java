package com.ccthanking.business.zjb.bmgk.service.impl;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.zjb.bmgk.service.ZjBmgkService;
import com.ccthanking.common.BzjkCommon;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class ZjBmgkServiceImpl implements ZjBmgkService{
	private String propertyFileName = "com.ccthanking.properties.business.bmjk.bmjk_zjb";
	private static String propertyFileNameXX = "com.ccthanking.properties.business.bzjklj.zjb_xx";
	@Override
	public String queryJsljList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		String tiaojian = request.getParameter("tiaojian");
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
			//页面查询条件施工和监理模糊查询
			if(!Pub.empty(sgjddw)){
				condition=condition+" and (sg.dwmc like '%"+sgjddw+"%' or jl.dwmc like '%"+sgjddw+"%') ";
			}
			PageManager page = RequestUtil.getPageManager(json);
			String ndSql2 = Pub.empty(nd) ? "" : " and a.nd='"+nd+"'";
			String sjndSql = Pub.empty(nd)?"":" and c.SJND='"+nd+"' ";
			
			String arr[] = proKey.split("\\*\\*");
			//子查询properties的路径
			//页面sql--arr[0]是链接到哪个页面，例如统筹计划、储备库等调用不同的sql的名称
			String querySql = Pub.getPropertiesSqlValue(propertyFileNameXX, arr[0]);
			String condiSql = Pub.getPropertiesSqlValue(propertyFileNameXX,arr[2]);
			//子查询sqlarr[1]是子查询sql的名称同bzfieldname
			if(!Pub.empty(tiaojian)&&!"null".equals(tiaojian)){
				condiSql=condiSql.replaceAll("%queryTiaojian%", tiaojian);
			}
			//替换查询条件
//			querySql = querySql.replaceAll("%queryCondition%", condition);
			querySql = querySql.replaceAll("%condition_sql%", condiSql);
			querySql = querySql.replaceAll("%aCondition%", ndSql2);
			querySql = querySql.replaceAll("%sjndCondition%", sjndSql);
			querySql += condition;
			BaseResultSet bs = DBUtil.query(conn, querySql, page);

			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("JLZBFS", "ZBFS");
			bs.setFieldDic("SGZBFS", "ZBFS");
			bs.setFieldThousand("HTQDJ");
			bs.setFieldThousand("TBJE");
			bs.setFieldThousand("YZSDJE");
			bs.setFieldThousand("CSSDJE");
			bs.setFieldThousand("SJSDJE");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("SGDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("WTZXGS", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	//造价编制统计概况
	@Override
	public String queryZjbzTjgk(HttpServletRequest request,String json)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "ZJBZ_TJGK_SQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd);
			//page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//造价分析管理统计概况
	@Override
	public String queryFxglTjgk(HttpServletRequest request,String json)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "FXGL_TJGK_SQL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and A.ND='"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd);
			//page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	} 
	//分析管理超概分布饼图
	@Override
	public String queryFxglCgfbChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");
			String ndtj="";
			if(!Pub.empty(nd))
			{
				ndtj=" and jhsj.nd='"+nd+"' ";
			}	
			String sql = Pub.getPropertiesString(propertyFileName, "FXGL_CGFB_CHART_SQL");
			sql=sql.replaceAll("%Condition%", ndtj);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = domresult.replaceAll("&lt;", "<");
			HashMap chartMap = new HashMap();
			chartMap.put("showLegend", "1");
			chartMap.put("showLabels", "0");
			chartMap.put("WARNING", "true");
			chartMap.put("chartLeftMargin", "0");
			chartMap.put("chartRightMargin", "0");
			chartMap.put("canvasLeftMargin", "0");
			chartMap.put("canvasRightMargin", "0");
			domresult = ChartUtil.makePieChartJsonString(domresult,chartMap, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryFxglCgfbTable(HttpServletRequest request, String json) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		String nd=request.getParameter("nd");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			if(!Pub.empty(nd))
			{
				roleCond=" and jhsj.nd='"+nd+"' ";
			}	
			//发起人查询
			String sql = Pub.getPropertiesString(propertyFileName, "FXGL_CGBL_TABLE_SQL");
			roleCond += " and jhsj.sjwybh=sj.sjwybh(+)  and jhsj.sjwybh=lbj.sjwybh(+)  and jhsj.sjwybh=js.sjwybh(+)  and jhsj.sjwybh=ztb.sjwybh(+)  AND BDBS='1' ";
			condition +=roleCond;
			condition +=orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("GSTZ");
			bs.setFieldThousand("YSTZ");
			bs.setFieldThousand("LBJ");
			bs.setFieldThousand("ZBJ");
			bs.setFieldThousand("JSZ");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	public String queryGcjsTjgk(HttpServletRequest request,String json)throws Exception{
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "GCJS_TJGK_SQL");
			//拼接年度查询条件
			String ndSql = Pub.empty(nd)?"":" and a.ND='"+nd+"'";
			sql=sql.replaceAll("%Condition%", ndSql);
			String ltNdSql = Pub.empty(nd)?"":" and ND<='"+nd+"'";
			sql=sql.replaceAll("%ltCondition%", ltNdSql);
			//page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	public String queryGcjsTxfx(HttpServletRequest request,String json)throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "GCJS_TXFX_SQL");
			//拼接年度查询条件
			String ndSql = Pub.empty(nd)?"":" and a.ND='"+nd+"' ";
			String sjndSql = Pub.empty(nd)?"":" and c.SJND='"+nd+"' ";
			sql=sql.replaceAll("%Condition%", ndSql);
			sql = sql.replaceAll("%sjndCondition%", sjndSql);
			//page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
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
	public String queryGcjsList(HttpServletRequest request, String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = null;
			//发起人查询
			String sql = Pub.getPropertiesString(propertyFileName, "GCJSLIST");
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
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
	public String queryList_zjbz(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			
			PageManager page = RequestUtil.getPageManager(json);
			String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String propertyFileName1=BzjkCommon.luJing("ZJB");

			String jhsjidSql = Pub.getPropertiesSqlValue(propertyFileName1, proKey);
			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			String ndSql = Pub.empty(nd) ? "" : " and jhsj.nd='"+nd+"'";
			jhsjidSql = jhsjidSql.replaceAll("%ndCondition2%", ndSql2);
			
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}

			String sql = "select DISTINCT gc_zjb_lbjb_id, jhsj.GC_JH_SJ_ID as jhsjid,jhsj.sjwybh, jhsj.xmbh,  jhsj.nd, jhsj.xmsx, jhsj.pxh, jhsj.xmmc, jhsj.bdmc, jhsj.xmid, jhsj.bdbh,jhsj.bdid, lbj.ISQTBMFZ,lbj.ISXYSCS,lbj.ZJBZZT,lbj.csbgbh, " +
						"lbj.txqsj, jhsj.XMBS,  lbj.zbsj, lbj.tzjjsj, lbj.zxgsj, lbj.sgfajs, lbj.zbwjjs, lbj.zxgsrq, lbj.jgbcsrq, lbj.sbcsz, lbj.czswrq, lbj.cssdz, lbj.sjz, lbj.sjbfb, lbj.zdj, lbj.bz,lbj.SBCSZRQ,lbj.CSSDZRQ,lbj.ISZH,lbj.CSSX, jhsj.iscs," +
						" jhsj.xmxz,jhsj.ISBT,jhsj.xmglgs ,jhsj.istbj,jhsj.cs_sj, jhsj.tbj_sj, nvl(cbsjpf.gs, cbsjpf.sse) as gys,  (select max(dyqk.twrq) from GC_ZJB_DYQK dyqk where dyqk.SJWYBH(+)=jhsj.SJWYBH and dyqk.sfyx='1') as YWRQ ,(select max(dyqk.dyrq) from GC_ZJB_DYQK dyqk where" +
						" dyqk.SJWYBH(+)=jhsj.SJWYBH and dyqk.sfyx='1') as HFRQ, decode(lbj.ZXGS,'', f.dsfjgid,lbj.ZXGS)  ZXGS, c.lrsj,   (case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ " +
						" from GC_ZJB_LBJB lbj,VIEW_GC_JH_SJ jhsj,(select a.gc_ztb_xq_id,b.SJWYBH,a.lrsj from gc_ztb_xq a, gc_ztb_jhsj b where b.xqid = a.GC_ZTB_XQ_ID  and a.ZBLX = '14' and a.sfyx='1' and b.sfyx='1') c,(select * from  gc_ztb_xqsj_ys where sfyx='1') e,(select * from  gc_ztb_sj where sfyx='1') f,(select * from Gc_Sj_Cbsjpf where sfyx = '1') cbsjpf  " +
						" where 1=1  "+ndSql+" and lbj.SJWYBH(+)=jhsj.SJWYBH  and jhsj.SJWYBH = cbsjpf.SJWYBH(+) and c.gc_ztb_xq_id=e.ztbxqid(+) and e.ztbsjid=f.gc_ztb_sj_id(+) and jhsj.SJWYBH=c.SJWYBH(+)  " +
						"   "+condition+"  " +
						"and jhsj.sjwybh in (select t.sjwybh from view_gc_jh_sj t where t.gc_jh_sj_id in ("+jhsjidSql+") and t.sjwybh is not null ) " +
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
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String queryJslbljList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		//页面查询条件施工和监理模糊查询
		String sgjddw= request.getParameter("SGJLDW");
		String lieCondition=" ";
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = RequestUtil.getPageManager(json);
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			String orderCondition = " order by t.XMBH, t.XMBS, t.PXH";
			//列条件判断
			if("BDZS".equals(proKey)){
				lieCondition="   ";
			}
			if("KGSJ_SJ".equals(proKey)){
				lieCondition=" and t.KGSJ_SJ is not null ";
			}
			if("JG_SJ".equals(proKey)){
				lieCondition=" and t.JG_SJ is not null ";
			}
			if("TBJS".equals(proKey)){
				lieCondition=" and A.GC_XMGLGS_XXJD_TBZJ_ID is not null ";
			}
			if("YZSDRQ".equals(proKey)){
				lieCondition=" and B.YZSDRQ is not null ";
			}
			if("CSSDRQ".equals(proKey)){
				lieCondition=" and B.CSSDRQ is not null ";
			}
			if("SJSDRQ".equals(proKey)){
				lieCondition=" and B.SJSDRQ is not null ";
			}
			
			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			String querySql = Pub.getPropertiesSqlValue(propertyFileNameXX, "GCJS_DETAIL");
			//子查询
			String condiSql = " SELECT T.GC_JH_SJ_ID FROM VIEW_GC_JH_SJ T, " +
					"(select * from GC_XMGLGS_XXJD_TBZJ where SFYX='1') A, " +
					"(select * from GC_ZJB_JSB where SFYX='1') B " +
					" WHERE T.GC_JH_SJ_ID = A.JHSJID(+) AND T.GC_JH_SJ_ID = B.JHSJID(+) AND T.BDBS = '1' %lieCondition%  %tCondition% ";
			
			//替换查询条件
			condiSql = condiSql.replaceAll("%lieCondition%", lieCondition);
			querySql = querySql.replaceAll("%condition_sql%", condiSql);
			querySql = querySql.replaceAll("%tCondition%", ndSql2);
			querySql += condition;
			querySql += orderCondition;
			BaseResultSet bs = DBUtil.query(conn, querySql, page);

			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("JLZBFS", "ZBFS");
			bs.setFieldDic("SGZBFS", "ZBFS");
			bs.setFieldThousand("HTQDJ");
			bs.setFieldThousand("TBJE");
			bs.setFieldThousand("YZSDJE");
			bs.setFieldThousand("CSSDJE");
			bs.setFieldThousand("SJSDJE");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("SGDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("WTZXGS", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
}
