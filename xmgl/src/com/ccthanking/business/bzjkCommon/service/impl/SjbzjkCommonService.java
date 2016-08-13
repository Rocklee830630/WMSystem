package com.ccthanking.business.bzjkCommon.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.common.BzjkCommon;
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
public class SjbzjkCommonService implements
		com.ccthanking.business.bzjkCommon.service.SjbzjkCommonService {

	@Override
	public String querySjglList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			
			PageManager page = RequestUtil.getPageManager(json);
			String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String propertyFileName1=BzjkCommon.luJing("SJ");

			String jhsjidSql = Pub.getPropertiesSqlValue(propertyFileName1, proKey);

			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			String xyNdSql = Pub.empty(nd)?"":" and t.ND<='"+nd+"'";
			jhsjidSql = jhsjidSql.replaceAll("%ndCondition2%", ndSql2);
			jhsjidSql = jhsjidSql.replaceAll("%ltCondition%", xyNdSql);
			
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}

			String sql = "select distinct jhsj.xmmc,jhsj.bdmc,jhsj.XMDZ,jhsj.xmxz,jhsj.JSMB ndmb ," +
					" jhsj.KYPF_SJ kybg,jhsj.SJdwid,jhsj.cbsjpf_sj cspf,sj.WCSJ_SGT_SSB," +
					" sj.WCSJ_SGT_ZSB,jhsj.cqt_sj,jhsj.xmbh, jhsj.XMBS, jhsj.PXH,(select max(zlsf.jsrq) from gc_sj_zlsf_js zlsf where zlsf.sjwybh=jhsj.sjwybh and zlsf.tzlb='0' ) GHTJ," +
					"  (select max(zlsf.jsrq) from gc_sj_zlsf_js zlsf where zlsf.sjwybh=jhsj.sjwybh and zlsf.tzlb='6' ) SGTSCBG" +
					" from view_gc_jh_sj jhsj, Gc_Sj sj" +
					" where jhsj.sjwybh = sj.sjwybh(+)" +
					" and jhsj.gc_jh_sj_id in ("+jhsjidSql+") " + condition + 
					" order by  jhsj.XMBH,jhsj.XMBS,jhsj.PXH ASC ";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldTranslater("SJDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldDic("XMXZ", "XMXZ");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}


	@Override
	public String querySjJJGglList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			
			PageManager page = RequestUtil.getPageManager(json);
			String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String propertyFileName1=BzjkCommon.luJing("SJ");

			String jhsjidSql = Pub.getPropertiesSqlValue(propertyFileName1, proKey);

			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			jhsjidSql = jhsjidSql.replaceAll("%ndCondition2%", ndSql2);
			
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}

			String sql = "select distinct jhsj.xmmc,jhsj.bdmc,jhsj.XMDZ,jhsj.xmxz,jhsj.JSMB ndmb , " +
					" jjg.JU_SFBZJZ ,jjg.JU_JBYSTJ,jjg.JU_SFZZYS,jjg.JU_YSQK,jjg.JGYSRQ,jjg.JGYSSJ,jhsj.xmbh, jhsj.XMBS, jhsj.PXH  " +
					" from GC_SJGL_JJG jjg,view_gc_jh_sj jhsj " +
					" where jjg.JHSJID(+)=jhsj.gc_jh_sj_id " +
					" and jhsj.gc_jh_sj_id in ("+jhsjidSql+") " + condition + 
					" order by jhsj.XMBH,jhsj.XMBS,jhsj.PXH ASC ";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldTranslater("SJDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldDic("JU_SFBZJZ", "SF");
			bs.setFieldDic("JU_JBYSTJ", "SF");
			bs.setFieldDic("JU_SFZZYS", "SF");
			bs.setFieldDic("JU_YSQK", "SF");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
}
