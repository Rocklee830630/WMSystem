package com.ccthanking.business.bzjkCommon.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.bzjkCommon.service.GBzjkCommonService;
import com.ccthanking.common.BzjkCommon;
import com.ccthanking.common.FjlbManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @author zhaiyl
 * @date 2014-12-15
 */
@Service
public  class GcBzjkCommonServiceImpl implements GBzjkCommonService {
	@Override
	public String queryList_Kfg(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			
			PageManager page = RequestUtil.getPageManager(json);
			String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String propertyFileName1=BzjkCommon.luJing("GCB");

			String jhsjidSql = Pub.getPropertiesSqlValue(propertyFileName1, proKey);
			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			String ndSql = Pub.empty(nd) ? "" : " and j.nd='"+nd+"'";
			jhsjidSql = jhsjidSql.replaceAll("%ndCondition2%", ndSql2);
			
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}

			String sql = "SELECT distinct (select GC_JH_TJGL_ID from GC_JH_TJGL tjgl where tjgl.jhsjid=t.gc_jh_sj_id) GC_JH_TJGL_ID,(select TBJJG from GC_JH_TJGL tjgl where tjgl.jhsjid=t.gc_jh_sj_id) TBJJG,t.GC_JH_SJ_ID jhsjid,t.GC_JH_SJ_ID,t.XMBH,t.ND,t.XMID,t.BDID,t.BDBH,t.XMMC,t.BDMC,t.XMBS,t.KGSJ,t.WGSJ, (case xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid  AND SFYX='1') when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = t.bdid  AND SFYX='1') end) SGDW,(case xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid  AND SFYX='1') when '1' then (select jldw from GC_XMBD where GC_XMBD_ID = t.bdid  AND SFYX='1') end) JLDW,T1.GC_TCJH_XMXDK_ID,t1.YZDB,t.JSMB,T1.XMLX,(SELECT GC_XMGLGS_XXJD_ID FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS GC_XMGLGS_XXJD_ID,(SELECT JZFK FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS JZFK,(SELECT FKRQ FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS FKRQ,(SELECT SJKGSJ FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS SJKGSJ,(SELECT SJWGSJ FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS SJWGSJ,(SELECT BZ FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS BZ,(SELECT ZT FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS ZT,(SELECT FXMS FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS FXMS,(SELECT GC_XMGLGS_XXJD_JHBZ_ID FROM GC_XMGLGS_XXJD_JHBZ WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS GC_XMGLGS_XXJD_JHBZ_ID,(SELECT GC_XMGLGS_XXJD_JHFK_ID FROM GC_XMGLGS_XXJD_JHFK WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS GC_XMGLGS_XXJD_JHFK_ID,(SELECT XXJDID FROM GC_XMGLGS_XXJD_JHBZ WHERE T.GC_JH_SJ_ID = JHSJID AND SFYX='1')AS XXJDID,(SELECT xmlx FROM GC_XMGLGS_XXJD_JHBZ WHERE T.GC_JH_SJ_ID = JHSJID AND SFYX='1')AS BZ_XMLX,(SELECT ZT_FK FROM GC_XMGLGS_XXJD_JHFK WHERE T.GC_JH_SJ_ID = JHSJID AND SFYX='1')AS ZT_FK,(SELECT ZT_BZ FROM GC_XMGLGS_XXJD_JHBZ WHERE T.GC_JH_SJ_ID = JHSJID AND SFYX='1')AS ZT_BZ,(case t.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1) end ) as XMBDDZ," +
					 " (CASE XMBS   WHEN '0' THEN (SELECT SUM(ZZ)  FROM (SELECT MAX(LJJLSDZ) ZZ, J.BDID, J.XMID FROM GC_XMGLGS_JLB J, GC_JH_SJ S " +
					 " WHERE J.TCJH_SJ_ID = S.GC_JH_SJ_ID  AND (S.XMBS = '1' OR ISNOBDXM = '1')  AND J.SFYX = '1' "+ndSql+" " +
					 " GROUP BY J.BDID, J.XMID)   WHERE XMID = T.XMID  GROUP BY XMID)   WHEN '1' THEN " +
					 " (SELECT MAX(LJJLSDZ)  FROM GC_XMGLGS_JLB j WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID  AND SFYX = '1' "+ndSql+" ) " +
					 " END) AS LJJLSDZ, t.xmxz,t.xmglgs,T.PXH,  sght.ZHTQDJ SGZHTQDJ,sght.ZHTZF SGZHTZF,jlht.ZHTQDJ JLZHTQDJ,jlht.ZHTZF JLZHTZF" +
					 " FROM view_GC_JH_SJ t,GC_TCJH_XMXDK T1," +
					 "(SELECT   GHH2.ID, GHH2.JHSJID  GC_JH_SJ_ID, GHH.ZHTQDJ,GHH.ZHTZF,GHH.HTLX" +
					 " FROM GC_HTGL_HT GHH,GC_HTGL_HTSJ GHH2   WHERE  GHH.ID  = GHH2.HTID AND GHH.HTLX='SG' AND GHH.SFYX='1'AND GHH2.SFYX='1') sght," +
					 " (SELECT   GHH2.ID, GHH2.JHSJID  GC_JH_SJ_ID, GHH.ZHTQDJ,GHH.ZHTZF,GHH.HTLX " +
					 " FROM GC_HTGL_HT GHH,GC_HTGL_HTSJ GHH2 WHERE  GHH.ID  = GHH2.HTID   AND GHH.HTLX='JL' AND GHH.SFYX='1'AND GHH2.SFYX='1')jlht " +
					 " where   T.XMID = T1.GC_TCJH_XMXDK_ID AND SGHT.GC_JH_SJ_ID(+)=T.GC_JH_SJ_ID AND JLHT.GC_JH_SJ_ID(+)=T.GC_JH_SJ_ID " +
					 " and t.gc_jh_sj_id in ("+jhsjidSql+") "+condition +" "  +
					 " order by  T.XMBH,T.XMBS,T.PXH ASC ";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldOrgDept("XMGLGS"); // 项目管理公司
			bs.setFieldThousand("LJJLSDZ");
			bs.setFieldThousand("SGZHTQDJ");
			bs.setFieldThousand("SGZHTZF");
			bs.setFieldThousand("JLZHTQDJ");
			bs.setFieldThousand("JLZHTZF");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
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
	public String querytcjhgtt(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			
			PageManager page = RequestUtil.getPageManager(json);
			String nd = request.getParameter("nd");
			String xmlx = request.getParameter("xmlx");
			String tiaojian = request.getParameter("tiaojian");
			String flag = request.getParameter("flag");
			String propertyFileName1=BzjkCommon.luJing("GCB");
			String proKey="";
			if("1".equals(flag)){
				if("0".equals(tiaojian)){
					proKey="GC_JDGL_PIE1_ZCKG";	
				}else{
					proKey="GC_JDGL_PIE1_YQKG";	
				}
				
			}else if("2".equals(flag)){
				if("0".equals(tiaojian)){
					proKey="GC_JDGL_PIE2_ZCWKG";	
				}else{
					proKey="GC_JDGL_PIE2_YQWKG";	
				}
			}else if("3".equals(flag)){
				if("0".equals(tiaojian)){
					proKey="GC_JDGL_PIE3_KG";	
				}else{
					proKey="GC_JDGL_PIE3_WKG";	
				}
			}else if("4".equals(flag)){
				if("0".equals(tiaojian)){
					proKey="GC_JDGL_PIE4_WG";	
				}else if("1".equals(tiaojian)){
					proKey="GC_JDGL_PIE4_ZJ";	
				}else if("2".equals(tiaojian)){
					proKey="GC_JDGL_PIE4_TG";	
				}
			}
			String jhsjidSql = Pub.getPropertiesSqlValue(propertyFileName1, proKey);
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			String xmlxSql = " AND (";
			if(!"".equals(xmlx)) {
				String[] xmlxArray = xmlx.split("-");
				for (int i = 0; i < xmlxArray.length; i++) {
					xmlxSql += "A.XMLX LIKE '%" + xmlxArray[i] + "%' OR ";
				}
				xmlxSql = xmlxSql.endsWith("OR ") ? xmlxSql.substring(0, xmlxSql.lastIndexOf("OR ")) : xmlxSql;
				xmlxSql += ")";
			} else {
				xmlxSql = "";
			}
			
			jhsjidSql = jhsjidSql.replaceAll("%Condition%", ndSql);
			jhsjidSql = jhsjidSql.replaceAll("%conditionXmlx%", xmlxSql);
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			
			String sql = Pub.getPropertiesSqlValue("com.ccthanking.properties.business.bzjklj.lj_list", "TCJhGTT_DETAIL");
			sql = sql.replaceAll("%condition_sql%", jhsjidSql);
			sql = sql.replaceAll("%queryCondition%", condition);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldOrgDept("XMGLGS"); // 项目管理公司
			bs.setFieldThousand("LJJLSDZ");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldDic("XJXJ", "XMXZ");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldThousand("GC");
			bs.setFieldThousand("ZC");
			bs.setFieldThousand("QT");
			bs.setFieldThousand("JHZTZE");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	@Override
	public String queryLybzjList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			
			PageManager page = RequestUtil.getPageManager(json);
			String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String qsNdSql = Pub.empty(nd)?"":" and t.nd ='"+nd+"'";
			String lyNdSql = Pub.empty(nd)?"":" and TO_CHAR(L.JNRQ, 'YYYY') ='"+nd+"'";
			String jhsjidSql=" ";
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			//列判断
			if("JHXMS".equals(proKey)){
				jhsjidSql=" SELECT T.GC_JH_SJ_ID FROM VIEW_GC_JH_SJ T where 1=1 %ndCondition_qs% "; 
			}
			if("JVGS".equals(proKey)){
				jhsjidSql=" SELECT t.GC_JH_SJ_ID   FROM VIEW_GC_JH_SJ t  WHERE 1=1 %ndCondition_qs% AND T.JS_SJ IS NOT NULL "; 			
				}
			if("SQS".equals(proKey)){
				jhsjidSql=" SELECT L.JHSJID FROM GC_ZJGL_LYBZJ L  WHERE 1=1 %ndCondition_ly% "; 
			}
			if("SPZ".equals(proKey)){
				jhsjidSql=" SELECT L.JHSJID FROM GC_ZJGL_LYBZJ L  WHERE 1=1 %ndCondition_ly%  AND L.FHQK = '1' "; 
			}
			if("SPW".equals(proKey)){
				jhsjidSql=" SELECT L.JHSJID FROM GC_ZJGL_LYBZJ L  WHERE 1=1 %ndCondition_ly%  AND (L.FHQK = '2' OR L.FHQK = '6') "; 
			}
			jhsjidSql = jhsjidSql.replaceAll("%ndCondition_qs%", qsNdSql);
			jhsjidSql = jhsjidSql.replaceAll("%ndCondition_ly%", lyNdSql);
			
			String sql = "select distinct l.id,l.FHQK,t.xmmc,t.bdmc,t.XMDZ,l.jnrq,l.je,l.jnfs,e.sjzt,l.fhrq,l.jndw,ht.htbm,ht.htmc,vzlj.jgyssj, vzlj2.tbrq "
					+ "from view_gc_jh_sj t,gc_zjgl_lybzj l,VIEW_HTGL_HT_HTSJ_XM ht, fs_event e, view_zjgl_lybzj_jgyssj vzlj ,view_zjgl_lybzj_jstbrq vzlj2 " 
					+ "where l.jhsjid(+) =t.gc_jh_sj_id and vzlj.jhsjid(+) = t.gc_jh_sj_id and vzlj2.jhsjid(+) = t.gc_jh_sj_id  "
					+ "and l.jhsjid=ht.jhsjid(+) "
					+ "and l.sjbh=e.sjbh(+) "+condition+" "
					+ "and t.gc_jh_sj_id in ("+jhsjidSql+")";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);

			bs.setFieldTranslater("JNDW", "GC_CJDW", "GC_CJDW_ID", "DWMC"); // 缴纳单位
			bs.setFieldThousand("JE"); 		//缴纳金额
			bs.setFieldDic("JNFS", "JNFS"); //缴纳方式
			bs.setFieldDic("FHQK", "SHZT"); //返回情况
			bs.setFieldDic("SJZT", "SJZT"); //事件状态
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	
}
