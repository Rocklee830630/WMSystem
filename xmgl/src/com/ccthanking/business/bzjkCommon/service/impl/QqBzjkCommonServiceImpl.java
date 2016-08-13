package com.ccthanking.business.bzjkCommon.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.bzjkCommon.service.QqBzjkCommonService;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @author xiahongbo
 * @date 2014-12-12
 */
@Service
public class QqBzjkCommonServiceImpl implements QqBzjkCommonService {

	@Override
	public String queryListQqsg(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String sxbh = request.getParameter("sxbh");
		String sxmc = request.getParameter("sxmc");
		String bllx = request.getParameter("bllx");
		String sgflag = request.getParameter("sgflag");
		String iskg = request.getParameter("iskg");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String condiSql="";
			if(sgflag.equals("4")){
				  if(iskg.equals("YKG")){
			          condiSql=   " select gc_jh_sj_id from VIEW_GC_JH_SJ where xmid in" +
			                " (select distinct xmid from VIEW_GC_JH_SJ where kgsj_sj IS NOT NULL and bdbs='1' ) and xmbs='0' " +
			                "  and nd='"+nd+"' ";
			        }else{
			          condiSql=   " select gc_jh_sj_id from VIEW_GC_JH_SJ where xmid not  in" +
			                " (select distinct xmid from VIEW_GC_JH_SJ where kgsj_sj IS NOT NULL and bdbs='1' ) and xmbs='0' " +
			                " and nd='"+nd+"' ";
			        }
			}else{
				if("customFlag".equalsIgnoreCase(sxbh)){
					String sql = Pub.getPropertiesSqlValue("com.ccthanking.properties.business.bzjklj.qqsx_xx", sgflag);
					nd = Pub.empty(nd)?"":" and T.ND='"+nd+"'";
					condiSql = sql.replaceAll("%ndCondition%", nd);
				}else {
					int bl=Integer.parseInt(bllx);
					condiSql = this.getConditionSql(sgflag, bl, nd, sxbh, sxmc);
					if(!Pub.empty(iskg)&&!iskg.equals("null")) {//是否有开工条件
						if(iskg.equals("YKG")){
							condiSql= 	" select gc_jh_sj_id from VIEW_GC_JH_SJ where xmid in" +
										" (select distinct xmid from VIEW_GC_JH_SJ where kgsj_sj IS NOT NULL and bdbs='1' ) and xmbs='0' " +
										" and gc_jh_sj_id in("+condiSql+")  ";
						}else{
							condiSql= 	" select gc_jh_sj_id from VIEW_GC_JH_SJ where xmid not  in" +
										" (select distinct xmid from VIEW_GC_JH_SJ where kgsj_sj IS NOT NULL and bdbs='1' ) and xmbs='0' " +
										" and gc_jh_sj_id in("+condiSql+")  ";
						}
					}
				}
			}
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			String orderFilter = RequestUtil.getOrderFilter(json);
			condition += " and qqsx.SJWYBH(+)=jhsj.SJWYBH and sx.SJWYBH(+) = jhsj.SJWYBH";
			condition += " and jhsj.xmid in (select xmid from GC_JH_SJ where gc_jh_sj_id in("+condiSql+")) ";
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
	private String getConditionSql(String sgflag,int bl,String nd,String sxbh,String sxmc) throws Exception{
		String condiSql = "";
		if("1".equals(sgflag)) {
			switch(bl) {
				// 已办理
				case 0:
					if(Pub.empty(sgflag)||sgflag.equals("null")) {
						condiSql=  "select * from table(FUN_QQSX_YIBL_ID('"+nd+"','"+sxbh+"'))";							
					} else {
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
					if(Pub.empty(sgflag)||sgflag.equals("null")) {
						condiSql=  "select * from table(FUN_QQSX_WBL_ID('"+nd+"','"+sxbh+"'))";							
					} else {
						condiSql=  "select * from table(FUN_QQSX_SG_WBL_ID('"+nd+"','"+sxbh+"'))";	
					}
					break;
				// 不需要办理
				case 4:
					condiSql=  "select * from table(fun_qqsx_bxbl_id('"+nd+"', '"+sxbh+"', '"+sxmc+"'))";
					break;
			}
		} else if("2".equals(sgflag)) {
			switch(bl) {
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
		} else  {
			switch(bl) {
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
		return condiSql;
	}
	@Override
	public String queryList_lxky(HttpServletRequest request, String json) {
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
			PageManager page = RequestUtil.getPageManager(json);
			
			
			int bl=Integer.parseInt(bllx);
			String condiSql="";
				switch(bl)
				{
					// 已办理
					case 0:
						condiSql=  "select * from table(FUN_QQSX_YIBL_ID('"+nd+"','"+sxbh+"'))";	
						break;
					// 应办理
					case 2:
						condiSql=  "select * from table(fun_qqsx_yingbl_id('"+nd+"','"+sxbh+"','"+sxmc+"'))";
						break;
					// 未办理
					case 3:
						condiSql=  "select * from table(FUN_QQSX_WBL_ID('"+nd+"','"+sxbh+"'))";							
						break;
					// 不需要办理
					case 4:
						condiSql=  "select * from table(fun_qqsx_bxbl_id('"+nd+"', '"+sxbh+"', '"+sxmc+"'))";
						break;
				}
		
			String querySql="select distinct jhsj.pxh,jhsj.lrsj,jhsj.xmbh,jhsj.xmmc,qqsx.gc_zgb_qqsx_id as ywbid,qqsx.xdkid,jhsj.xmid,jhsj.gc_jh_sj_id as jhsjid,jhsj.nd,qqsx.LXKYBJSJ as bjsj, qqsx.LXKYBBLSX as bblsx,sx.XMJYSPF,sx.JYSFK,sx.HPPF,sx.HPFK,sx.TDYJH,jhsj.SJWYBH,sx.TDYJFK,sx.GDZCTZXM,sx.JNFK,sx.KYPF,sx.KYFK,sx.CZWT,(case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ from (select sjwybh,max(decode(fjlx, '2020', sjwybh, '')) XMJYSPF,max(decode(fjlx, '2020', blsj, '')) JYSFK, max(decode(fjlx, '2021', sjwybh, '')) HPPF, max(decode(fjlx, '2021', blsj, '')) HPFK, max(decode(fjlx, '2022', sjwybh, '')) TDYJH, max(decode(fjlx, '2022', blsj, '')) TDYJFK, max(decode(fjlx, '2023', sjwybh, '')) GDZCTZXM, max(decode(fjlx, '2023', blsj, '')) JNFK, max(decode(fjlx, '2024', sjwybh, '')) KYPF, max(decode(fjlx, '2024', blsj, '')) KYFK, (select czwt from (select czwt,sjwybh, row_number() over(partition by sjwybh order by blsj desc) as line from GC_QQSX_SXFJ  where dfl = '0' and sfyx = '1') sx2 where line = '1' and sx2.sjwybh(+)=sx1.sjwybh) as czwt from GC_QQSX_SXFJ sx1 where sfyx='1' group by sjwybh) sx, (select * from GC_JH_SJ where sfyx='1')  jhsj,(select * from Gc_Zgb_Qqsx where sfyx='1') qqsx " +
					"  where   qqsx.sjwybh(+)=jhsj.sjwybh  and sx.sjwybh(+) = jhsj.sjwybh and jhsj.bdid is null and jhsj.GC_JH_SJ_ID IN ("+condiSql+")  "+condition +"  order by  jhsj.xmbh,jhsj.pxh asc ";
			
			String aa ="SELECT  A.XMBH,A.XMMC,C.BDMC,A.XMLX, A.ISBT,A.XJXJ,B.XMSX,A.XMDZ,A.JSNR,A.XMGLGS,B.XMXZ,A.JSMB,A.SGDW,A.JLDW,C.kgsj_SJ,C.wgsj_sj,A.GC,A.ZC,A.QT,A.JHZTZE" +
					" FROM GC_TCJH_XMXDK A ,VIEW_GC_TCJH_LWYJ B,VIEW_GC_JH_SJ C,GC_CJDW SG,GC_CJDW JL  WHERE   A.GC_TCJH_XMXDK_ID=B.XDKID(+)  AND A.GC_TCJH_XMXDK_ID=C.XMID(+) AND SG.GC_CJDW_ID(+)=C.SGDWID AND JL.GC_CJDW_ID(+)=C.JLDWID " +
					"AND  C.GC_JH_SJ_ID IN ("+condiSql+")  "+condition +" ";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDic("BBLSX", "LXKYFJLX");
			bs.setFieldFileUploadWithWybh("XMJYSPF", "2020");
			bs.setFieldFileUploadWithWybh("HPPF", "2021");
			bs.setFieldFileUploadWithWybh("TDYJH", "2022");
			bs.setFieldFileUploadWithWybh("GDZCTZXM", "2023");
			bs.setFieldFileUploadWithWybh("KYPF", "2024");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	@Override
	public String queryList_tdsx(HttpServletRequest request, String json) {
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
			PageManager page = RequestUtil.getPageManager(json);
			
			
			int bl=Integer.parseInt(bllx);
			String condiSql="";
				switch(bl)
				{
					// 已办理
					case 0:
						condiSql=  "select * from table(FUN_QQSX_YIBL_ID('"+nd+"','"+sxbh+"'))";	
						break;
					// 应办理
					case 2:
						condiSql=  "select * from table(fun_qqsx_yingbl_id('"+nd+"','"+sxbh+"','"+sxmc+"'))";
						break;
					// 未办理
					case 3:
						condiSql=  "select * from table(FUN_QQSX_WBL_ID('"+nd+"','"+sxbh+"'))";							
						break;
					// 不需要办理
					case 4:
						condiSql=  "select * from table(fun_qqsx_bxbl_id('"+nd+"', '"+sxbh+"', '"+sxmc+"'))";
						break;
				}
			String querySql ="select distinct jhsj.pxh,jhsj.lrsj,jhsj.xmbh,jhsj.xmmc,qqsx.gc_zgb_qqsx_id as ywbid,qqsx.xdkid,jhsj.xmid,jhsj.gc_jh_sj_id as jhsjid,jhsj.nd,qqsx.TDSPBJSJ as bjsj, qqsx.TDSPBBLSX as bblsx,sx.YDYSFK,sx.ZDJDFK,sx.GDSXFK,sx.TDDJFK,sx.TDSYFK,sx.CZWT, sx.YDYS , sx.JTTDZD , sx.GDSX ,jhsj.SJWYBH, sx.TDDJ , sx.TDSYZ,(case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ from (select SJWYBH, max(decode(fjlx, '0010', jhsjid, '')) YDYS,max(decode(fjlx, '0010', blsj, '')) YDYSFK, max(decode(fjlx, '0011', jhsjid, '')) JTTDZD,max(decode(fjlx, '0011', blsj, '')) ZDJDFK, max(decode(fjlx, '0012', jhsjid, '')) GDSX,max(decode(fjlx, '0012', blsj, '')) GDSXFK, max(decode(fjlx, '0013', jhsjid, '')) TDDJ,max(decode(fjlx, '0013', blsj, '')) TDDJFK, max(decode(fjlx, '0014', jhsjid, '')) TDSYZ,max(decode(fjlx, '0014', blsj, '')) TDSYFK, (select czwt from (select czwt,SJWYBH, row_number() over(partition by SJWYBH order by blsj desc) as line from GC_QQSX_SXFJ  where dfl = '2' and sfyx = '1') sx2 where line = '1' and sx2.SJWYBH(+)=sx1.SJWYBH) as czwt from GC_QQSX_SXFJ sx1 where sfyx='1' group by SJWYBH) sx,  (select * from GC_JH_SJ where sfyx='1')  jhsj,(select * from Gc_Zgb_Qqsx where sfyx='1') qqsx " +
					" where  qqsx.SJWYBH(+)=jhsj.SJWYBH and sx.SJWYBH(+) = jhsj.SJWYBH and jhsj.bdid is null and jhsj.GC_JH_SJ_ID IN ("+condiSql+")  "+condition +"  order by  jhsj.xmbh,jhsj.pxh asc ";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDic("BBLSX", "TDSPSX");
			bs.setFieldFileUpload("YDYS", "0010");
			bs.setFieldFileUpload("JTTDZD", "0011");
			bs.setFieldFileUpload("GDSX", "0012");
			bs.setFieldFileUpload("TDDJ", "0013");
			bs.setFieldFileUpload("TDSYZ", "0014");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	@Override
	public String queryList_ghsx(HttpServletRequest request, String json) {
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
			PageManager page = RequestUtil.getPageManager(json);
			
			int bl=Integer.parseInt(bllx);
			String condiSql="";
				switch(bl)
				{
					// 已办理
					case 0:
						condiSql=  "select * from table(FUN_QQSX_YIBL_ID('"+nd+"','"+sxbh+"'))";	
						break;
					// 应办理
					case 2:
						condiSql=  "select * from table(fun_qqsx_yingbl_id('"+nd+"','"+sxbh+"','"+sxmc+"'))";
						break;
					// 未办理
					case 3:
						condiSql=  "select * from table(FUN_QQSX_WBL_ID('"+nd+"','"+sxbh+"'))";							
						break;
					// 不需要办理
					case 4:
						condiSql=  "select * from table(fun_qqsx_bxbl_id('"+nd+"', '"+sxbh+"', '"+sxmc+"'))";
						break;
				}
				String querySql="select distinct jhsj.pxh,jhsj.xmbh,jhsj.lrsj,jhsj.xmmc,qqsx.gc_zgb_qqsx_id as ywbid,qqsx.xdkid,jhsj.xmid,jhsj.gc_jh_sj_id as jhsjid,jhsj.nd,qqsx.ghspbjsj as bjsj, qqsx.ghspbblsx as bblsx,sx.XZYJS,sx.JSXZXMYJS,sx.JSYDGHXKZ,jhsj.SJWYBH,sx.YDXKZ,sx.JSGCGHXKZ,sx.GCXKZ,sx.CZWT,(case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ from (select sjwybh, max(decode(fjlx, '0007', jhsjid, '')) JSXZXMYJS,max(decode(fjlx, '0007', blsj, '')) XZYJS, max(decode(fjlx, '0008', jhsjid, '')) JSYDGHXKZ, max(decode(fjlx, '0008', blsj, '')) YDXKZ, max(decode(fjlx, '0009', jhsjid, '')) JSGCGHXKZ, max(decode(fjlx, '0009', blsj, '')) GCXKZ, (select czwt from (select czwt,sjwybh, row_number() over(partition by SJWYBH order by blsj desc) as line from GC_QQSX_SXFJ  where dfl = '1' and sfyx = '1') sx2 where line = '1' and sx2.sjwybh(+)=sx1.sjwybh) as czwt from GC_QQSX_SXFJ sx1 where sfyx='1' group by sjwybh) sx, (select * from GC_JH_SJ where sfyx='1')  jhsj,(select * from Gc_Zgb_Qqsx where sfyx='1') qqsx" +
						"  where  qqsx.sjwybh(+)=jhsj.sjwybh and sx.sjwybh(+) = jhsj.sjwybh and jhsj.bdid is null and jhsj.GC_JH_SJ_ID IN ("+condiSql+")  "+condition +" order by  jhsj.xmbh,jhsj.pxh asc ";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			
			bs.setFieldDic("BBLSX", "GHSX");
			bs.setFieldFileUpload("JSXZXMYJS", "0007");
			bs.setFieldFileUpload("JSYDGHXKZ", "0008");
			bs.setFieldFileUpload("JSGCGHXKZ", "0009");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

}
