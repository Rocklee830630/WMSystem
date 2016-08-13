package com.ccthanking.business.bzjkCommon.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.bzjkCommon.service.BzjkCommonService;
import com.ccthanking.common.BzjkCommon;
import com.ccthanking.common.FjlbManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

//按所属地区
@Service
public class BzjkCommonServiceImpl implements BzjkCommonService{
	//所有链接页面sql的properties路径
	String propertyFileName="com.ccthanking.properties.business.bzjklj.lj_list";
	@Override
	public String queryList(HttpServletRequest request, String json) {
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
			String ndSql = Pub.empty(nd) ? "" : " and nd='"+nd+"'";
			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			String ndSql3 = Pub.empty(nd) ? "" : " and a.nd='"+nd+"'";
			String xyNdSql = Pub.empty(nd)?"":" and t.ND<='"+nd+"'";
			String zNdSql = Pub.empty(nd)?"":" and z.nd='"+nd+"'";
			String xNdSql = Pub.empty(nd)?"":" and x.nd='"+nd+"'";
			String qsNdSql = Pub.empty(nd)?"":" and to_char(QS.JNRQ, 'YYYY')='"+nd+"'";
			String gcqsNdSql = Pub.empty(nd)?"":" and to_char(qs.blrq, 'YYYY')='"+nd+"'";
			String gcsxNdSql = Pub.empty(nd)?"":" and to_char(qs.SXSQRQ, 'YYYY')='"+nd+"'";
			
			String arr[] = proKey.split("\\*\\*");
			//子查询properties的路径
			String propertyFileName1=BzjkCommon.luJing(arr[1]);
			//页面sql--arr[0]是链接到哪个页面，例如统筹计划、储备库等调用不同的sql的名称
			String querySql = Pub.getPropertiesSqlValue(propertyFileName, arr[0]);
			String condiSql = Pub.getPropertiesSqlValue(propertyFileName1,arr[2]);
			//子查询sqlarr[1]是子查询sql的名称同bzfieldname
			if(!Pub.empty(tiaojian)&&!"null".equals(tiaojian)){
				condiSql=condiSql.replaceAll("%queryTiaojian%", tiaojian);
			}
			//替换查询条件
			querySql = querySql.replaceAll("%queryCondition%", condition);
			
			
			querySql = querySql.replaceAll("%condition_sql%", condiSql);
			querySql = querySql.replaceAll("%ndCondition%", ndSql);
			querySql = querySql.replaceAll("%ndCondition2%", ndSql2);
			querySql = querySql.replaceAll("%ltCondition%", xyNdSql);
			querySql = querySql.replaceAll("%aCondition%", ndSql3);
			querySql = querySql.replaceAll("%zCondition%", zNdSql);
			querySql = querySql.replaceAll("%xCondition%", xNdSql);
			querySql = querySql.replaceAll("%ndCondition_qs%", qsNdSql);
			querySql = querySql.replaceAll("%gcqsNdCondition%", gcqsNdSql);
			querySql = querySql.replaceAll("%gcsxNdCondition%", gcsxNdSql);
			
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			
			
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
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String queryList_Qq(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String sxbh = request.getParameter("sxbh");
		String sxmc = request.getParameter("sxmc");
		String bllx = request.getParameter("bllx");
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
			//页面查询条件施工和监理模糊查询
			if(!Pub.empty(sgjddw)){
				condition=condition+" and (sg.dwmc like '%"+sgjddw+"%' or jl.dwmc like '%"+sgjddw+"%') ";
			}
			PageManager page = RequestUtil.getPageManager(json);
			
			
			int bl=Integer.parseInt(bllx);
			String condiSql="";
			if(!sgflag.equals("2")&&!sgflag.equals("3"))
			{
				switch(bl)
				{
					// 已办理
					case 0:
						if(Pub.empty(sgflag)||sgflag.equals("null"))
						{
							condiSql=  "select * from table(FUN_QQSX_YIBL_ID('"+nd+"','"+sxbh+"'))";							
						}
						else
						{
							condiSql=  "select * from table(FUN_QQSX_SG_BLWC_ID('"+nd+"','"+sxbh+"'))";	
						}	
						break;
					// 部分办理
					case 1:
						condiSql=  "select * from table(FUN_QQSX_SG_BFBL_ID('"+nd+"','"+sxbh+"'))";	
						break;
					// 应办理
					case 2:
						condiSql=  "select * from table(fun_qqsx_yingbl_id('"+nd+"','"+sxbh+"','"+sxmc+"'))";
						break;
					// 未办理
					case 3:
						if(Pub.empty(sgflag)||sgflag.equals("null"))
						{
							condiSql=  "select * from table(FUN_QQSX_WBL_ID('"+nd+"','"+sxbh+"'))";							
						}
						else
						{
							condiSql=  "select * from table(FUN_QQSX_SG_WBL_ID('"+nd+"','"+sxbh+"'))";	
						}	
						break;
					// 不需要办理
					case 4:
						condiSql=  "select * from table(fun_qqsx_bxbl_id('"+nd+"', '"+sxbh+"', '"+sxmc+"'))";
						break;
				}
			}
			else
			{
				if(sgflag.equals("2"))
				{
					switch(bl)
					{
					// 已办理
					case 0:
						condiSql=  "select GC_JH_SJ_ID from VIEW_GC_JH_SJ T where XMBS = '0' and (((select count(GC_JH_SJ_ID) from VIEW_GC_JH_SJ where XMID = T.XMID and XMBS = '1') > 0 and (select count(GC_JH_SJ_ID) BDZS from VIEW_GC_JH_SJ where XMID = T.XMID and (ISSGT='1' or ISSGT='2') and XMBS = '1') = (select count(GC_JH_SJ_ID) YWBD from VIEW_GC_JH_SJ where XMID = T.XMID and ((ISSGT='1' and SGT_SJ is not null) or ISSGT='2') and XMBS = '1')) or  (ISNOBDXM = '1' and ((ISSGT='1' and SGT_SJ is not null) or ISSGT='2') )) and ND = '"+nd+"'";
						break;
					// 部分办理
					case 1:
						condiSql=  "select GC_JH_SJ_ID from VIEW_GC_JH_SJ T where XMBS = '0' and ( (select count(GC_JH_SJ_ID) from VIEW_GC_JH_SJ where XMID = T.XMID and XMBS = '1') > 0 and (select count(GC_JH_SJ_ID) BDZS from VIEW_GC_JH_SJ where XMID = T.XMID and (ISSGT='1' or ISSGT='2') and XMBS = '1') >  (select count(GC_JH_SJ_ID) YWBD from VIEW_GC_JH_SJ where XMID = T.XMID and ((ISSGT='1' and SGT_SJ is not null) or ISSGT='2') and XMBS = '1') and (select count(GC_JH_SJ_ID) YWBD from VIEW_GC_JH_SJ where XMID = T.XMID and ((ISSGT='1' and SGT_SJ is not null) or ISSGT='2') and XMBS = '1') > 0) and ND = '"+nd+"'";	
						break;
					// 应办理
					case 2:
						condiSql=  "select GC_JH_SJ_ID from VIEW_GC_JH_SJ T where XMBS = '0' and (ISSGT='1' or ISSGT='2') and ND = '"+nd+"'";
						break;
					// 未办理
					case 3:
						condiSql=  "select GC_JH_SJ_ID from VIEW_GC_JH_SJ T where XMBS = '0' and (((select count(GC_JH_SJ_ID) from VIEW_GC_JH_SJ where XMID = T.XMID and XMBS = '1') > 0 and (select count(GC_JH_SJ_ID) YWBD from VIEW_GC_JH_SJ where XMID = T.XMID and ((ISSGT = '1' and SGT_SJ is not null) or ISSGT = '2') and XMBS = '1') = 0) or (ISNOBDXM = '1' and (ISSGT = '1' and SGT_SJ is null) and ISSGT != '2')) and ND = '"+nd+"'";
						break;
					// 不需要办理
					case 4:
						condiSql=  "select GC_JH_SJ_ID from VIEW_GC_JH_SJ T where XMBS = '0' and ISSGT = '0' and ND = '"+nd+"'";
						break;
					}					
				}
				else
				{
					switch(bl)
					{
					// 已办理
					case 0:
						condiSql=  "select GC_JH_SJ_ID from VIEW_GC_JH_SJ T where XMBS = '0' and (( (select count(GC_JH_SJ_ID) from VIEW_GC_JH_SJ where XMID = T.XMID and XMBS = '1') > 0 and (select count(GC_JH_SJ_ID) BDZS from VIEW_GC_JH_SJ where XMID = T.XMID and (ISSGDW='1' or ISSGDW='2') and XMBS = '1') = (select count(GC_JH_SJ_ID) YWBD from VIEW_GC_JH_SJ where XMID = T.XMID and ((ISSGDW='1' and SGDW_SJ is not null) or ISSGDW='2') and XMBS = '1')) or  (ISNOBDXM = '1' and ((ISSGDW='1' and SGDW_SJ is not null) or ISSGDW='2') )) and ND = '"+nd+"'";
						break;
					// 部分办理
					case 1:
						condiSql=  "select GC_JH_SJ_ID from VIEW_GC_JH_SJ T where XMBS = '0' and ( (select count(GC_JH_SJ_ID) from VIEW_GC_JH_SJ where XMID = T.XMID and XMBS = '1') > 0 and (select count(GC_JH_SJ_ID) BDZS from VIEW_GC_JH_SJ where XMID = T.XMID and (ISSGDW='1' or ISSGDW='2') and XMBS = '1') >  (select count(GC_JH_SJ_ID) YWBD from VIEW_GC_JH_SJ where XMID = T.XMID and ((ISSGDW='1' and SGDW_SJ is not null) or ISSGDW='2') and XMBS = '1') and (select count(GC_JH_SJ_ID) YWBD from VIEW_GC_JH_SJ where XMID = T.XMID and ((ISSGDW='1' and SGDW_SJ is not null) or ISSGDW='2') and XMBS = '1') > 0 ) and ND = '"+nd+"'";	
						break;
					// 应办理
					case 2:
						condiSql=  "SELECT GC_JH_SJ_ID  FROM   VIEW_GC_JH_SJ T WHERE  XMBS = '0'  AND ( ISSGDW = '1' OR ISSGDW = '2' )  AND ND = '"+nd+"'";
						break;
					// 未办理
					case 3:
						condiSql=  "SELECT GC_JH_SJ_ID from VIEW_GC_JH_SJ T where XMBS = '0' and (((select count(GC_JH_SJ_ID) from VIEW_GC_JH_SJ where XMID = T.XMID and XMBS = '1') > 0 and (select count(GC_JH_SJ_ID) YWBD from VIEW_GC_JH_SJ where XMID = T.XMID and ((ISSGDW = '1' and SGDW_SJ is not null) or ISSGDW = '2') and XMBS = '1') = 0) or (ISNOBDXM = '1' and (ISSGDW = '1' and SGDW_SJ is null) and ISSGDW != '2')) and ND = '"+nd+"'";
						break;
					// 不需要办理
					case 4:
						condiSql = "select GC_JH_SJ_ID from VIEW_GC_JH_SJ T where XMBS = '0' and ISSGDW = '0' and ND = '"+nd+"'";
						break;
					}
				}
			}
			String querySql ="SELECT  A.XMBH,A.XMMC,C.BDMC,A.XMLX, A.ISBT,A.XJXJ,B.XMSX,A.XMDZ,A.JSNR,A.XMGLGS,B.XMXZ,A.JSMB,A.SGDW,A.JLDW,C.kgsj_SJ,C.wgsj_sj,A.GC,A.ZC,A.QT,A.JHZTZE" +
					" FROM GC_TCJH_XMXDK A ,VIEW_GC_TCJH_LWYJ B,VIEW_GC_JH_SJ C,GC_CJDW SG,GC_CJDW JL  WHERE   A.GC_TCJH_XMXDK_ID=B.XDKID(+)  AND A.GC_TCJH_XMXDK_ID=C.XMID(+) AND SG.GC_CJDW_ID(+)=C.SGDWID AND JL.GC_CJDW_ID(+)=C.JLDWID " +
					"AND  C.GC_JH_SJ_ID IN ("+condiSql+")  "+condition +" ";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
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
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC"); // 监理单位
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC"); // 监理单位
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	
	/**
	 * 工程部-履约保证金查详细
	 */
	@Override
	public String queryList_gc_lybzj(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			
			PageManager page = RequestUtil.getPageManager(json);
			String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String qsNdSql = Pub.empty(nd)?"":" and to_char(l.JNRQ, 'YYYY')='"+nd+"'";
			String propertyFileName1=BzjkCommon.luJing("GCB");
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			String jhsjidSql = Pub.getPropertiesSqlValue(propertyFileName1,proKey);
			jhsjidSql = jhsjidSql.replaceAll("%ndCondition_qs%", qsNdSql);
			String sql = "select distinct l.id,l.FHQK,t.xmmc,t.bdmc,t.XMDZ,l.jnrq,l.je,l.jnfs,e.sjzt,l.fhrq,l.jndw,ht.htbm,ht.htmc,vzlj.jgyssj, vzlj2.tbrq "
					+ "from view_gc_jh_sj t,gc_zjgl_lybzj l,VIEW_HTGL_HT_HTSJ_XM ht, fs_event e, view_zjgl_lybzj_jgyssj vzlj ,view_zjgl_lybzj_jstbrq vzlj2 " 
					+ "where l.jhsjid=t.gc_jh_sj_id(+) and vzlj.jhsjid(+) = t.gc_jh_sj_id and vzlj2.jhsjid(+) = t.gc_jh_sj_id  "
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
	
	/**
	 * 工程部-工程洽商
	 */
	@Override
	public String queryList_gc_gcqs(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String qsNdSql = Pub.empty(nd)?"":" and to_char(qs.blrq, 'YYYY')='"+nd+"'";
			String propertyFileName1=BzjkCommon.luJing("GCB");

			String jhsjidSql = Pub.getPropertiesSqlValue(propertyFileName1, proKey);
			jhsjidSql = jhsjidSql.replaceAll("%gcqsNdCondition%", qsNdSql);
			if(!Pub.empty(condition)){
				condition +=" and qs.jhsjid=t.gc_jh_sj_id  and qs.sjbh=e.sjbh  and sg.gc_cjdw_id(+)=t.sgdwid and jl.gc_cjdw_id(+)=t.jldwid and qs.sfyx='1' " + qsNdSql
					+ "and t.gc_jh_sj_id in ("+jhsjidSql+") ";
				
			}else{
				condition="   qs.jhsjid=t.gc_jh_sj_id  and qs.sjbh=e.sjbh  and sg.gc_cjdw_id(+)=t.sgdwid and jl.gc_cjdw_id(+)=t.jldwid and qs.sfyx='1' " + qsNdSql
					+ "and t.gc_jh_sj_id in ("+jhsjidSql+") ";
			}
			condition += orderFilter;
			page.setFilter(condition);
			String sql = "select qs.gc_gcgl_gcqs_id,qs.qsbt,qs.qstcrq,qs.qsyy, qs.qsnr,t.SJdwid,t.xmmc,t.bdmc,t.XMDZ,e.sjzt,qs.gszj,qs.yzdb,t.xmglgs,t.jldwid,qs.gc_gcgl_gcqs_id gcqsfj,t.sgdwid "
					+ "from gc_gcgl_gcqs qs,view_gc_jh_sj t,fs_event e,gc_cjdw sg,gc_cjdw jl ";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldOrgDept("XMGLGS"); // 项目管理公司
			bs.setFieldTranslater("JLDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC"); // 监理单位
			bs.setFieldTranslater("SJDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC"); // 设计单位
			bs.setFieldTranslater("SGDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC"); // 施工单位
			bs.setFieldDic("SJZT", "SJZT"); //事件状态
			bs.setFieldFileUpload("GCQSFJ", FjlbManager.GC_GCGL_GCQS); //工程洽商附件类别
			bs.setFieldThousand("GSZJ"); 		//估算金额
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	/**
	 * 工程部-工程洽商
	 */
	@Override
	public String queryGcqsPass(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String qsNdSql = Pub.empty(nd)?"":" and to_char(qs.blrq, 'YYYY')='"+nd+"'";
			String propertyFileName1=BzjkCommon.luJing("GCB");

			String jhsjidSql = Pub.getPropertiesSqlValue(propertyFileName1, proKey);
			jhsjidSql = jhsjidSql.replaceAll("%gcqsNdCondition%", qsNdSql);
			if(!Pub.empty(condition)){
				condition +=" and qs.jhsjid=t.gc_jh_sj_id  and qs.sjbh=e.sjbh  and sg.gc_cjdw_id(+)=t.sgdwid and jl.gc_cjdw_id(+)=t.jldwid and qs.sfyx='1' " + qsNdSql
					+ "and t.gc_jh_sj_id in ("+jhsjidSql+") ";
				
			}else{
				condition="   qs.jhsjid=t.gc_jh_sj_id  and qs.sjbh=e.sjbh  and sg.gc_cjdw_id(+)=t.sgdwid and jl.gc_cjdw_id(+)=t.jldwid and qs.sfyx='1' " + qsNdSql
					+ "and t.gc_jh_sj_id in ("+jhsjidSql+") ";
			}
			condition += orderFilter;
			page.setFilter(condition);
			String sql = "select nvl(count(qs.gc_gcgl_gcqs_id),0) zs,nvl(sum(decode(e.sjzt,'2',1,0)),0) pass " +
					 "from gc_gcgl_gcqs qs,view_gc_jh_sj t,fs_event e,gc_cjdw sg,gc_cjdw jl ";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	/**
	 * 工程部-工程甩项
	 */
	@Override
	public String queryList_gc_gcsx(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			PageManager page = RequestUtil.getPageManager(json);
			String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String qsNdSql = Pub.empty(nd)?"":" and to_char(sx.SXSQRQ, 'YYYY')='"+nd+"'";
			String propertyFileName1=BzjkCommon.luJing("GCB");

			String jhsjidSql = Pub.getPropertiesSqlValue(propertyFileName1, proKey);
//			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			String ndSql2 = Pub.empty(nd)?"":" and to_char(qs.SXSQRQ,'yyyy')='"+nd+"'";
			jhsjidSql = jhsjidSql.replaceAll("%ndCondition2%", ndSql2);

			String sql = "select sx.sxmc,t.xmmc,t.bdmc,t.XMDZ,t.kgsj_sj,sx.sxsqrq,sx.sxsqnr,e.sjzt,t.Yzdb,t.xmglgs,sx.gc_gcgl_gcsx_id sxxgfj " +
					" , (case xmbs  when '0' then (select a.sgdwxmjl from GC_TCJH_XMXDK a  where gc_tcjh_xmxdk_id = t.xmid AND SFYX = '1') " +
					" when '1' then (select sgdwxmjl from GC_XMBD where GC_XMBD_ID = t.bdid  AND SFYX = '1') end) sgdwxmjl, " +
					" (case xmbs  when '0' then (select a.jldwzj from GC_TCJH_XMXDK a where gc_tcjh_xmxdk_id = t.xmid  AND SFYX = '1') " +
					" when '1' then (select jldwzj from GC_XMBD where GC_XMBD_ID = t.bdid  AND SFYX = '1')  end) jldwzj,t.sgdwid,t.jldwid "
					+ "from GC_GCGL_GCSX sx,view_gc_jh_sj t,fs_event e "
					+ "where sx.jhsjid=t.gc_jh_sj_id and sx.sjbh=e.sjbh and sx.sfyx='1' " //+ qsNdSql	delete by zhangbr@ccthanking.com 先不用计划年度了，只是用业务的年度
					+ "and t.gc_jh_sj_id in ("+jhsjidSql+") "+condition+"";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldOrgDept("XMGLGS"); // 项目管理公司
			bs.setFieldTranslater("JLDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC"); // 监理单位
			bs.setFieldTranslater("SGDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC"); // 施工单位  
			bs.setFieldDic("SJZT", "SJZT"); //事件状态
			bs.setFieldFileUpload("SXXGFJ", FjlbManager.GC_GCB_GCSX); //工程洽商附件类别
			bs.setFieldUserID("YZDB");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String querySJBGList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String tiaojian = request.getParameter("tiaojian");
			
			PageManager page = RequestUtil.getPageManager(json);
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			String arr[] = proKey.split("\\*\\*");
			//子查询properties的路径
			String propertyFileName1=BzjkCommon.luJing(arr[1]);
			//页面sql--arr[0]是链接到哪个页面，例如统筹计划、储备库等调用不同的sql的名称
			String querySql = Pub.getPropertiesSqlValue(propertyFileName, arr[0]);
			String condiSql = Pub.getPropertiesSqlValue(propertyFileName1,arr[2]);
			//子查询sqlarr[1]是子查询sql的名称同bzfieldname
			if(!Pub.empty(tiaojian)&&!"null".equals(tiaojian)){
				condiSql=condiSql.replaceAll("%queryTiaojian%", tiaojian);
			}
			querySql = querySql.replaceAll("%condition_sql%", condiSql);
			querySql = querySql.replaceAll("%ndCondition2%", ndSql2);
			//替换查询条件
			querySql = querySql.replaceAll("%queryCondition%", condition);
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDic("BGLB", "BGLB2");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldThousand("GS");//其他
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String queryJHBZList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		String tiaojian = request.getParameter("tiaojian");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition = " and " + condition;
			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			String xNdSql = Pub.empty(nd)?"":" and x.nd='"+nd+"'";
			String arr[] = proKey.split("\\*\\*");
			//子查询properties的路径
			String propertyFileName1=BzjkCommon.luJing(arr[1]);
			//页面sql--arr[0]是链接到哪个页面，例如统筹计划、储备库等调用不同的sql的名称
			String querySql = Pub.getPropertiesSqlValue(propertyFileName, arr[0]);
			String condiSql = Pub.getPropertiesSqlValue(propertyFileName1,arr[2]);
			//子查询sqlarr[1]是子查询sql的名称同bzfieldname
			if(!Pub.empty(tiaojian)&&!"null".equals(tiaojian)){
				condiSql=condiSql.replaceAll("%queryTiaojian%", tiaojian);
			}
			querySql = querySql.replaceAll("%condition_sql%", condiSql);
			querySql = querySql.replaceAll("%ndCondition%", ndSql2);
			querySql = querySql.replaceAll("%xCondition%", xNdSql);
			querySql += condition + orderFilter;
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
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
	public String queryJHGZList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		String tiaojian = request.getParameter("tiaojian");
		String lie = request.getParameter("lie");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			PageManager page = RequestUtil.getPageManager(json);
			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			String arr[] = proKey.split("\\*\\*");
			//子查询properties的路径
			String propertyFileName1=BzjkCommon.luJing(arr[1]);
			//页面sql--arr[0]是链接到哪个页面，例如统筹计划、储备库等调用不同的sql的名称
			String querySql = Pub.getPropertiesSqlValue(propertyFileName, arr[0]);
			String condiSql = Pub.getPropertiesSqlValue(propertyFileName1,arr[2]);
			//子查询sqlarr[1]是子查询sql的名称同bzfieldname
			if(!Pub.empty(lie)&&!"null".equals(lie)){
				condiSql=condiSql.replaceAll("%queryTiaojian%", lie);
			}
			if(!Pub.empty(tiaojian)&&!"null".equals(tiaojian)){
				condiSql=condiSql.replaceAll("%queryTiaojianXH%", tiaojian);
			}
			querySql = querySql.replaceAll("%condition_sql%", condiSql);
			querySql = querySql.replaceAll("%ndCondition%", ndSql2);
			//替换查询条件
			querySql = querySql.replaceAll("%queryCondition%", condition);
			
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
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
	public String queryXDKList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		String tiaojian = request.getParameter("tiaojian");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			
			PageManager page = RequestUtil.getPageManager(json);
			
			String ndSql = Pub.empty(nd) ? "" : " and nd='"+nd+"'";
			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			String ndSql3 = Pub.empty(nd) ? "" : " and a.nd='"+nd+"'";
			String xyNdSql = Pub.empty(nd)?"":" and t.ND<='"+nd+"'";
			String zNdSql = Pub.empty(nd)?"":" and z.nd='"+nd+"'";
			String xNdSql = Pub.empty(nd)?"":" and x.nd='"+nd+"'";
			String qsNdSql = Pub.empty(nd)?"":" and to_char(QS.JNRQ, 'YYYY')='"+nd+"'";
			String gcqsNdSql = Pub.empty(nd)?"":" and to_char(qs.blrq, 'YYYY')='"+nd+"'";
			
			
			String arr[] = proKey.split("\\*\\*");
			//子查询properties的路径
			String propertyFileName1=BzjkCommon.luJing(arr[1]);
			//页面sql--arr[0]是链接到哪个页面，例如统筹计划、储备库等调用不同的sql的名称
			String querySql = Pub.getPropertiesSqlValue(propertyFileName, arr[0]);
			String condiSql = Pub.getPropertiesSqlValue(propertyFileName1,arr[2]);
			//子查询sqlarr[1]是子查询sql的名称同bzfieldname
			
			
			if(!Pub.empty(tiaojian)&&!"null".equals(tiaojian)){
				condiSql=condiSql.replaceAll("%queryTiaojian%", tiaojian);
			}
			querySql = querySql.replaceAll("%condition_sql%", condiSql);
			querySql = querySql.replaceAll("%ndCondition%", ndSql);
			querySql = querySql.replaceAll("%ndCondition2%", ndSql2);
			querySql = querySql.replaceAll("%ltCondition%", xyNdSql);
			querySql = querySql.replaceAll("%aCondition%", ndSql3);
			querySql = querySql.replaceAll("%zCondition%", zNdSql);
			querySql = querySql.replaceAll("%xCondition%", xNdSql);
			querySql = querySql.replaceAll("%ndCondition_qs%", qsNdSql);
			querySql = querySql.replaceAll("%gcqsNdCondition%", gcqsNdSql);
			//替换查询条件
			querySql = querySql.replaceAll("%queryCondition%", condition);
			
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDic("NDMB", "NDMB");//年度目标
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("XJXJ", "XMXZ");
			bs.setFieldDic("XMFR", "XMFR");
			bs.setFieldDic("ISNATC", "SF");
			bs.setFieldDic("WDD", "WDD");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldThousand("JHZTZE");//总投资
			bs.setFieldThousand("GC");//工程
			bs.setFieldThousand("ZC");//征拆
			bs.setFieldThousand("QT");//其他
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String queryZBXQList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		String tiaojian = request.getParameter("tiaojian");
		String lie = request.getParameter("lie");
		String bmCondition ="";
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			PageManager page = RequestUtil.getPageManager(json);
			String ndSql2 = Pub.empty(nd) ? "" : " and c.nd='"+nd+"'";
			String xndSql = Pub.empty(nd) ? "" : " and x.nd='"+nd+"'";
			String arr[] = proKey.split("\\*\\*");
			//子查询properties的路径
			String propertyFileName1=BzjkCommon.luJing(arr[1]);
			//页面sql--arr[0]是链接到哪个页面，例如统筹计划、储备库等调用不同的sql的名称
			String querySql = Pub.getPropertiesSqlValue(propertyFileName, arr[0]);
			String condiSql = Pub.getPropertiesSqlValue(propertyFileName1,arr[2]);
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			if(!Pub.empty(lie)&&!"null".equals(lie)){
				//部门判断
				if(lie.equals("1")){
					bmCondition=" AND C.LRBM = '100000000016' ";
				}
				if(lie.equals("2")){
					bmCondition="  AND C.LRBM = '100000000014'  ";
				}
				if(lie.equals("3")){
					bmCondition=" AND C.LRBM = '100000000004' ";
				}
				if(lie.equals("4")){
					bmCondition=" AND C.LRBM = '100000000009' ";
				}
				if(lie.equals("5")){
					bmCondition=" AND C.LRBM = '100000000017' ";
				}
				if(lie.equals("6")){
					bmCondition=" AND C.LRBM = '100000000005'  ";
				}
				if(lie.equals("7")){
					bmCondition=" AND C.LRBM IN (select ROW_ID from fs_org_dept where EXTEND1='1') AND C.ZBLX = '13' ";
				}
				if(lie.equals("8")){
					bmCondition=" AND C.LRBM IN (select ROW_ID from fs_org_dept where EXTEND1='1') AND C.ZBLX = '12' ";
				}
				if(lie.equals("9")){
					bmCondition=" AND C.LRBM = '100000000006' ";
				}
				condiSql=condiSql.replaceAll("%bmCondition%", bmCondition);
				if(!Pub.empty(tiaojian)&&!"null".equals(tiaojian)){
					condiSql=condiSql.replaceAll("%xhCondition%", tiaojian);
				}
			}else{
				if(!Pub.empty(tiaojian)&&!"null".equals(tiaojian)){
					condiSql=condiSql.replaceAll("%queryTiaojian%", tiaojian);
				}	
			}
			
			querySql = querySql.replaceAll("%condition_sql%", condiSql);
			querySql = querySql.replaceAll("%ndCondition%", ndSql2);
			querySql = querySql.replaceAll("%xndCondition%", xndSql );
			//替换查询条件
			querySql = querySql.replaceAll("%queryCondition%", condition);
			
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDic("ZBLX", "ZBLX");//招标类型
			bs.setFieldDic("ZBFS", "ZBFS");//招标方式
			bs.setFieldDic("XQZT", "SF");//招标方式
			bs.setFieldDic("XQZT", "XQZT");//垫资方式
			bs.setFieldDic("TBJFS", "TBBJFS");//投标报价方式
			bs.setFieldThousand("YSE");//预算额
			bs.setFieldSjbh("sjbh");//事件编号
			bs.setFieldTranslater("JLDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC"); // 监理单位
			bs.setFieldTranslater("LRBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");	// 录入部门
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String queryHtList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		String tiaojian = request.getParameter("tiaojian");
		String bm = request.getParameter("bm");
		String lrbm="";
		String zblx="";
		String xqzt="";
		String htlx="";
		String htzt="";
		String zhuSQL="";
		String congSQL="";
		//需求状态判断
		if(tiaojian.equals("HTGL_BMLB_ZBXQ")){
			xqzt="3,5,6";
			zhuSQL="HT_DETAIL";
			congSQL="ZBXQ";
		}
		if(tiaojian.equals("HTGL_BMLB_LXCXZ")){
			xqzt="5";
			zhuSQL="HT_DETAIL";
			congSQL="ZBXQ";
		}
		if(tiaojian.equals("HTGL_BMLB_YWC")){
			xqzt="6";
			zhuSQL="HT_DETAIL";
			congSQL="ZBXQ";
		}
		if(tiaojian.equals("HTGL_BMLB_YINGQDHT")){
			htzt="-1,0,1,2";
			zhuSQL="HTID_DETAIL";
			congSQL="HT";
		}
		if(tiaojian.equals("HTGL_BMLB_YIQDHT")){
			htzt="1,2";
			zhuSQL="HTID_DETAIL";
			congSQL="HT";
		}
		if(tiaojian.equals("HTGL_BMLB_DQDHT")){
			htzt="-1,0";
			zhuSQL="HTID_DETAIL";
			congSQL="HT";
		}
		//部门判断
		if(bm.equals("工程部（项目管理公司）")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
		}
		if(bm.equals("监理")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
		}
		if(bm.equals("施工")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
		}
		if(bm.equals("其他")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
			htlx="'ZJWT','XY','QT','QQ','PQ','JC','DJ','CQ','BC','LDLH'";
		}
		if(bm.equals("总工办（设计）")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
			htlx="'ZJWT','XY','QT','QQ','PQ','JC','DJ','CQ','BC','LDLH'";
		}
		if(bm.equals("设计")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
			htlx="'ZJWT','XY','QT','QQ','PQ','JC','DJ','CQ','BC','LDLH'";
		}
		if(bm.equals("其他中介")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
			htlx="'ZJWT','XY','QT','QQ','PQ','JC','DJ','CQ','BC','LDLH'";
		}
		if(bm.equals("总工办（手续）")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
			htlx="'ZJWT','XY','QT','QQ','PQ','JC','DJ','CQ','BC','LDLH'";
		}
		if(bm.equals("总工办（信息）")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
			htlx="'ZJWT','XY','QT','QQ','PQ','JC','DJ','CQ','BC','LDLH'";
		}
		if(bm.equals("前期部（征收）")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
			htlx="'ZJWT','XY','QT','QQ','PQ','JC','DJ','CQ','BC','LDLH'";
		}
		if(bm.equals("排迁")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="'12','13','14','15','16','17','18','11','21'";
			htlx="'ZJWT','XY','QT','QQ','PQ','JC','DJ','CQ','BC','LDLH'";
		}
		if(bm.equals("造价部")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
			htlx="'ZJWT','XY','QT','QQ','PQ','JC','DJ','CQ','BC','LDLH'";
		}
		if(bm.equals("招标部")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
			htlx="'ZJWT','XY','QT','QQ','PQ','JC','DJ','CQ','BC','LDLH'";
		}
		if(bm.equals("质量安全")){
			lrbm=  "select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003'";
			zblx="12,13,14,15,16,17,18,11,21";
			htlx="'ZJWT','XY','QT','QQ','PQ','JC','DJ','CQ','BC'','LDLH'";
		}
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			PageManager page = RequestUtil.getPageManager(json);
			String ndSql2 = Pub.empty(nd) ? "" : " and c.nd='"+nd+"'";
			String xndSql = Pub.empty(nd) ? "" : " and  qdnf='"+nd+"'";
			String ghhndSql = Pub.empty(nd) ? "" : " and  ghh.qdnf='"+nd+"'";
			
			String arr[] = proKey.split("\\*\\*");
			//子查询properties的路径
			String propertyFileName1=BzjkCommon.luJing(arr[1]);
			//页面sql--arr[0]是链接到哪个页面，例如统筹计划、储备库等调用不同的sql的名称
			String querySql = Pub.getPropertiesSqlValue(propertyFileName, zhuSQL);
			String condiSql = Pub.getPropertiesSqlValue(propertyFileName1,congSQL);
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			//录入部门替换
			if(!Pub.empty(lrbm)&&!"null".equals(lrbm)){
				condiSql=condiSql.replaceAll("%lrbmCondition%", lrbm);
			}
			//招标类型替换
			if(!Pub.empty(zblx)&&!"null".equals(zblx)){
				condiSql=condiSql.replaceAll("%zblxCondition%", zblx);
			}
			//
			if(!Pub.empty(xqzt)&&!"null".equals(xqzt)){
				condiSql=condiSql.replaceAll("%xqztCondition%", xqzt);
			}
			if(!Pub.empty(htlx)&&!"null".equals(htlx)){
				condiSql=condiSql.replaceAll("%htlxCondition%", htlx);
			}
			if(!Pub.empty(htzt)&&!"null".equals(htzt)){
				condiSql=condiSql.replaceAll("%htztCondition%", htzt);
			}
			
			querySql = querySql.replaceAll("%condition_sql%", condiSql);
			querySql = querySql.replaceAll("%ndCondition%", ndSql2);
			querySql = querySql.replaceAll("%qdndCondition%", xndSql);
			querySql = querySql.replaceAll("%ghhqdndCondition%", ghhndSql);
			//替换查询条件
			querySql = querySql.replaceAll("%queryCondition%", condition);
			
			BaseResultSet bs = DBUtil.query(conn, querySql, page);

            bs.setFieldDic("XMLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式
            bs.setFieldDic("QDNF", "XMNF");// 项目年份
            bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同

            // 日期
            bs.setFieldDateFormat("HTJQDRQ", "yyyy-MM-dd");

            bs.setFieldThousand("ZHTQDJ");// 总合同签订价(元)
            bs.setFieldThousand("HTQDJ");// 合同签订价(元)
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
}
