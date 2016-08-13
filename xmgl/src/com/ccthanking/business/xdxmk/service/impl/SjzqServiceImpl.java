package com.ccthanking.business.xdxmk.service.impl;

import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xdxmk.service.SjzqService;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class SjzqServiceImpl implements SjzqService {
	
	@Override
	public String query_pcxx(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
	        condition += BusinessUtil.getSJYXCondition(null);
	        condition += BusinessUtil.getCommonCondition(user,null);
	        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT * FROM GC_JH_ZT"; 
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//字典翻译
			bs.setFieldDic("ISXF", "SF");//是否下发
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
	public String query_tcjh(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String nd = request.getParameter("nd");
			String index = request.getParameter("index");
			String xmglgs = request.getParameter("xmglgs");
			String where = "";
			String wc = " AND ((T.ISKGSJ ='1' AND  T.KGSJ IS NOT NULL) OR (T.ISKGSJ = '0'))"+
					" AND ((T.ISWGSJ = '1' AND T.WGSJ IS NOT NULL) OR (T.ISWGSJ = '0'))"+
					" AND ((T.ISKYPF = '1' AND T.KYPF IS NOT NULL) OR (T.ISKYPF = '0'))"+
					" AND ((T.ISHPJDS = '1' AND T.HPJDS IS NOT NULL) OR (T.ISHPJDS = '0'))"+
					" AND ((T.ISGCXKZ = '1' AND T.GCXKZ IS NOT NULL) OR (T.ISGCXKZ = '0'))"+
					" AND ((T.ISSGXK = '1' AND T.SGXK IS NOT NULL) OR (T.ISSGXK = '0'))"+
					" AND ((T.ISCBSJPF = '1' AND T.CBSJPF IS NOT NULL) OR (T.ISCBSJPF = '0'))"+
					" AND ((T.ISCQT = '1' AND T.CQT IS NOT NULL) OR (T.ISCQT = '0'))"+
					" AND ((T.ISPQT = '1' AND T.PQT IS NOT NULL) OR (T.ISPQT = '0'))"+
					" AND ((T.ISSGT = '1' AND T.SGT IS NOT NULL) OR (T.ISSGT = '0'))"+
					" AND ((T.ISTBJ = '1' AND T.TBJ IS NOT NULL) OR (T.ISTBJ = '0'))"+
					" AND ((T.ISCS = '1' AND T.CS IS NOT NULL) OR (T.ISCS = '0'))"+
					" AND ((T.ISJLDW = '1' AND T.JLDW IS NOT NULL) OR (T.ISJLDW = '0'))"+
					" AND ((T.ISSGDW = '1' AND T.SGDW IS NOT NULL) OR (T.ISSGDW = '0'))"+
					" AND ((T.ISZC = '1' AND T.ZC IS NOT NULL) OR (T.ISZC = '0'))"+
					" AND ((T.ISPQ = '1' AND T.PQ IS NOT NULL) OR (T.ISPQ = '0'))"+
					" AND ((T.ISJG = '1' AND T.JG IS NOT NULL) OR (T.ISJG = '0'))";;
			String wbz = " AND T.ISKGSJ ='1' AND T.KGSJ IS NULL "+
					" AND T.ISWGSJ = '1' AND T.WGSJ IS NULL "+
					" AND T.ISKYPF = '1' AND T.KYPF IS NULL"+
					" AND T.ISHPJDS = '1' AND T.HPJDS IS NULL"+
					" AND T.ISGCXKZ = '1' AND T.GCXKZ IS NULL"+
					" AND T.ISSGXK = '1' AND T.SGXK IS NULL"+
					" AND T.ISCBSJPF = '1' AND T.CBSJPF IS NULL"+
					" AND T.ISCQT = '1' AND T.CQT IS NULL"+
					" AND T.ISPQT = '1' AND T.PQT IS NULL"+
					" AND T.ISSGT = '1' AND T.SGT IS NULL"+
					" AND T.ISTBJ = '1' AND T.TBJ IS NULL"+
					" AND T.ISCS = '1' AND T.CS IS NULL"+
					" AND T.ISJLDW = '1' AND T.JLDW IS NULL"+
					" AND T.ISSGDW = '1' AND T.SGDW IS NULL"+
					" AND T.ISZC = '1' AND T.ZC IS NULL"+
					" AND T.ISPQ = '1' AND T.PQ IS NULL"+
					" AND T.ISJG = '1' AND T.JG IS NULL";;
			String bfwc = " and t.gc_jh_sj_id not in(SELECT gc_jh_sj_id   FROM  GC_JH_SJ t where t.XMBS = '0' AND T.ND ='"+nd+"'";
			String ndjhxms = "and t.sfyx='1' and t.nd ='"+nd+"' and t.xmbs = '0'";
			String ndyjbds = "and t.sfyx='1' and t.nd ='"+nd+"' and (t.xmbs = '1' or t.ISNOBDXM = '1')";
			if(!Pub.empty(nd)){
				condition +=" AND T.ND ='"+nd+"'";
			}
			if(!Pub.empty(xmglgs)){
				condition +=" AND T.XMGLGS ='"+xmglgs+"'";
			}
			if(!Pub.empty(index)){
				if(index.equals("4")){
					where = wc;
				}else if(index.equals("5")){
					where = bfwc+wc+")"+bfwc+wbz+")";
				}else if(index.equals("6")){
					where = wbz;
				}else if(index.equals("7")){
					where = ndjhxms;
				}else if(index.equals("8")){
					where = ndyjbds;
				}
				
			}
			
			condition += where;
			condition += " and t.xmid = t1.gc_tcjh_xmxdk_id ";
	        condition += BusinessUtil.getSJYXCondition("t");
	        condition += BusinessUtil.getCommonCondition(user,"t");
	        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT "
					+" (select XMDZ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1) as XMDZ," +
					"  (select JSNR from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1) as JSNR," +
					"  (select JSMB from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum=1) as JSMB," +
					"  (select XJXJ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum=1) as XMXZ, " +
					" t1.wdd,"+
					"t.xmglgs,t.gc_jh_sj_id, t.jhid, t.nd, t.xmid, t.bdid, t.xmbh, t.bdbh,t.xmmc," +
					" t.bdmc, t.pxh, t.kgsj, t.kgsj_sj, t.kgsj_bz, t.wgsj, t.wgsj_sj, t.wgsj_bz, t.kypf, t.kypf_sj, t.kypf_bz, t.hpjds, t.hpjds_sj, t.hpjds_bz, t.gcxkz, t.gcxkz_sj, t.gcxkz_bz, t.sgxk, t.sgxk_sj, t.sgxk_bz, t.cbsjpf, t.cbsjpf_sj, t.cbsjpf_bz, t.cqt, t.cqt_sj, t.cqt_bz, t.pqt, t.pqt_sj, t.pqt_bz, t.sgt, t.sgt_sj, t.sgt_bz, t.tbj, t.tbj_sj, t.tbj_bz, t.cs, t.cs_sj, t.cs_bz, t.jldw, t.jldw_sj, t.jldw_bz, t.sgdw, t.sgdw_sj, t.sgdw_bz, t.zc, t.zc_sj, t.zc_bz, t.pq, t.pq_sj, t.pq_bz, t.jg, t.jg_sj, t.jg_bz, t.xmsx, t.ywlx, t.sjbh, t.bz, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx, t.xflx, t.isxf, t.iskypf, t.ishpjds, t.isgcxkz, t.issgxk, t.iscbsjpf, t.iscqt, t.ispqt, t.issgt, t.istbj, t.iscs, t.isjldw, t.issgdw, t.iszc, t.ispq, t.isjg, t.xmbs"+
					" FROM " +
					" GC_JH_SJ t,GC_TCJH_XMXDK t1 " ;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//字典翻译
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("JHID", "GC_JH_ZT", "GC_JH_ZT_ID", "JHPCH");//批次号
			bs.setFieldDic("XMXZ", "XMXZ");
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
	public String query_kfgl(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String nd = request.getParameter("nd");
			String index = request.getParameter("index");
			String xmglgs = request.getParameter("xmglgs");;
			String where = "";
			String glb = "";
			String kglblxms = " and t1.nd = '"+nd+"' and t1.sfyx = '1' and t2.kgfg = '0' and gc_jh_sj_id = TCJHSJID ";
			String spwcs = " and t1.gc_jh_sj_id = t2.tcjhsjid and t2.sfyx = '1' and t1.nd = '"+nd+"' and kgfg = '0' and e.sjzt = '2' ";
			String fglblxms = " and t1.nd = '"+nd+"' and t1.sfyx = '1' and t2.kgfg = '1' and gc_jh_sj_id = TCJHSJID ";
			String tglblxms = " and t1.nd = '"+nd+"' and t1.sfyx = '1' and t2.kgfg = '2' and gc_jh_sj_id = TCJHSJID ";
			String wblxm = " and t1.GC_JH_SJ_ID NOT IN(SELECT DISTINCT TCJHSJID FROM GC_GCB_KFG WHERE SFYX = '1') and t1.nd = '"+nd+"' ";
			if(!Pub.empty(nd)){
				condition +=" AND T1.ND ='"+nd+"'";
			}
			if(!Pub.empty(xmglgs)){
				condition +=" AND T1.XMGLGS ='"+xmglgs+"'";
			}
			if(!Pub.empty(index)){
				if(index.equals("17")){
					where = kglblxms;
					glb = ",GC_GCB_KFG t2,fs_event e";
					condition += "and e.sjbh = t2.sjbh ";
				}else if(index.equals("18")){
					where = spwcs;
					glb = ",GC_GCB_KFG t2,fs_event e";
					condition += "and e.sjbh = t2.sjbh ";
				}else if(index.equals("19")){
					where = fglblxms;
					glb = ",GC_GCB_KFG t2,fs_event e";
					condition += "and e.sjbh = t2.sjbh ";
				}else if(index.equals("20")){
					where = tglblxms;
					glb = ",GC_GCB_KFG t2,fs_event e";
					condition += "and e.sjbh = t2.sjbh ";
				}else if(index.equals("21")){
					where = wblxm;
				}
				
			}
			condition += where;
			condition += "AND t.GC_TCJH_XMXDK_ID = t1.XMID ";
	        condition += BusinessUtil.getSJYXCondition("t");
	        condition += BusinessUtil.getCommonCondition(user,"t");
	        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"t.GC_TCJH_XMXDK_ID,t.XMLX,t.XMDZ,t.QGRQ, " +
					"(case xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) sgdw, "+
					"(case xmbs when '0' then (select fzr_sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select sgdwfzr from GC_XMBD where GC_XMBD_ID = t1.bdid) end) FZR_SGDW, "+
					"(case xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select jldw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) JLDW, "+
					"(case xmbs when '0' then (select fzr_jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select jldwfzr from GC_XMBD where GC_XMBD_ID = t1.bdid) end) FZR_JLDW, "+
					"(case xmbs when '0' then (select sjdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select sjdw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) SJDW,"+
					"t1.GC_JH_SJ_ID,t1.BDID,t1.ND,t1.XMBH,t1.XMMC,t1.BDBH,t1.BDMC,t1.KGSJ,t1.KGSJ_SJ,t1.JHID,t1.XMSX,t1.XMBS, "+
					"(SELECT BDDD FROM GC_XMBD WHERE GC_XMBD_ID = T1.BDID)AS BDDD, "+
					"(SELECT HTID FROM GC_HTGL_HTSJ WHERE htid in(select ID from GC_HTGL_HT where htlx = 'SG' and sfyx = '1') and sfyx = '1' and jhsjid = t1.gc_jh_sj_id) as HTID, "+
					"(select blsj from (select TCJHSJID, blsj,KGFG from GC_GCB_KFG WHERE SFYX='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID AND KGFG = '0' and rownum = 1) as M_KGSJ,"+
					"(select blsj from (select TCJHSJID, blsj,KGFG from GC_GCB_KFG WHERE SFYX='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID AND KGFG = '1' and rownum = 1) as M_FGSJ, "+
					"(select blsj from (select TCJHSJID, blsj,KGFG from GC_GCB_KFG WHERE SFYX='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID AND KGFG = '2' and rownum = 1) as M_TGSJ,"+
					"(select GC_GCB_KFG_ID from (select GC_GCB_KFG_ID, TCJHSJID, blsj,KGFG from GC_GCB_KFG  where sfyx='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID AND KGFG = '0' and rownum = 1) as M_KGFJ,"+
					"(select GC_GCB_KFG_ID from (select GC_GCB_KFG_ID, TCJHSJID, blsj,KGFG from GC_GCB_KFG  where sfyx='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID AND KGFG = '1' and rownum = 1) as M_FGFJ, "+
					"(select GC_GCB_KFG_ID from (select GC_GCB_KFG_ID, TCJHSJID, blsj,KGFG from GC_GCB_KFG  where sfyx='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID AND KGFG = '2' and rownum = 1) as M_TGFJ, "+
					"(select sjbh from (select sjbh,GC_GCB_KFG_ID, TCJHSJID, blsj,KGFG from GC_GCB_KFG  where sfyx='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID and rownum = 1) as sjbh, "+
					"(select ywlx from (select ywlx,GC_GCB_KFG_ID, TCJHSJID, blsj,KGFG from GC_GCB_KFG  where sfyx='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID and rownum = 1) as ywlx "+
					" FROM " +
					"GC_TCJH_XMXDK t,GC_JH_SJ t1 "+glb;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");//标段
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			//表选
			bs.setFieldTranslater("FS_ORG_PERSON", "FZRXM", "ACCOUNT", "NAME");
			bs.setFieldTranslater("HTID", "GC_HTGL_HTSJ", "HTID", "HTQDJ");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			//字典
			bs.setFieldDic("GCZL", "XMLX");
			bs.setFieldFileUpload("M_KGFJ","0040");
			bs.setFieldFileUpload("M_FGFJ","0040");
			bs.setFieldFileUpload("M_TGFJ","0040");
			bs.setFieldDic("SFFJ", "FJSCZT");
			bs.setFieldDic("KGFG", "KFTGZT");
			bs.setFieldDic("XMLX", "XMLX");
			//add by cbl 绑定sjbh
			bs.setFieldSjbh("sjbh");
			//字典翻译
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
	public String query_xxjd(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String nd = request.getParameter("nd");
			String index = request.getParameter("index");
			String xmglgs= request.getParameter("xmglgs");
			String where = "";
			String ykgxm = " and t.sfyx='1' and t.xmbs = '0' and ((t.iskgsj = '1' and t.KGSJ_SJ is not null) or (t.iskgsj ='0')) and t.nd='"+nd+"'";
			String ykgbd = " and (t.xmbs = '1' or t.ISNOBDXM = '1') and t.sfyx='1' and ((t.iskgsj = '1' and t.KGSJ_SJ is not null) or (t.iskgsj ='0')) and t.nd ='"+nd+"'";	
			String ywgxm = " and t.sfyx='1' and t.xmbs = '0' and ((t.iswgsj = '1' and t.WGSJ_SJ is not null) or (t.iswgsj ='0')) and t.nd='"+nd+"'";
			String ywgbd = " and (t.xmbs = '1' or t.ISNOBDXM = '1') and t.sfyx='1' and ((t.iswgsj = '1' and t.WGSJ_SJ is not null) or (t.iswgsj ='0')) and t.nd ='"+nd+"'";
			String wfkxm = " and t.sfyx='1' and t.xmbs = '0' and t.iswgsj ='1' and t.WGSJ is not null and t.WGSJ_SJ is null and t.nd ='"+nd+"'";
			String wfkbd = " and t.sfyx='1' and (t.xmbs = '1' or t.ISNOBDXM = '1') and t.iswgsj ='1' and t.WGSJ is not null and t.WGSJ_SJ is null and t.nd='"+nd+"'";
			String jztyxm = " and t.nd='"+nd+"' and t.sfyx = '1' and t.xmbs = '0' and ((t.iskgsj = '1' and t.kgsj is not null and t.kgsj_sj is not null and (trunc(t.kgsj_sj) > trunc(t.kgsj))) or (t.iswgsj = '1' and t.wgsj is not null and t.wgsj_sj is not null and (trunc(t.wgsj_sj) > trunc(t.wgsj))))";
			String jztybd = " and t.nd='"+nd+"' and t.sfyx = '1' and (t.xmbs = '1' or t.ISNOBDXM = '1') and ((t.iskgsj = '1' and t.kgsj is not null and t.kgsj_sj is not null and (trunc(t.kgsj_sj) > trunc(t.kgsj))) or (t.iswgsj = '1' and t.wgsj is not null and t.wgsj_sj is not null and (trunc(t.wgsj_sj) > trunc(t.wgsj))))";
			if(!Pub.empty(nd)){
				condition +=" AND T.ND ='"+nd+"'";
			}
			if(!Pub.empty(xmglgs)){
				condition +=" AND T.XMGLGS ='"+xmglgs+"'";
			}
			if(!Pub.empty(index)){
				if(index.equals("9")){
					where = ykgxm;
				}else if(index.equals("10")){
					where = ykgbd;
				}else if(index.equals("11")){
					where = ywgxm;
				}else if(index.equals("12")){
					where = ywgbd;
				}else if(index.equals("13")){
					where = wfkxm;
				}else if(index.equals("14")){
					where = wfkbd;
				}else if(index.equals("15")){
					where = jztyxm;
				}else if(index.equals("16")){
					where = jztybd;
				}
				
			}
			
			condition += where;
			condition += " and t.xmid = t1.gc_tcjh_xmxdk_id ";
	        condition += BusinessUtil.getSJYXCondition("t");
	        condition += BusinessUtil.getCommonCondition(user,"t");
	        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"t.GC_JH_SJ_ID,t.XMBH,t.ND,t.XMID,t.BDID,t.BDBH,t.XMMC,t.BDMC,t.XMBS,t.KGSJ,t.WGSJ, " +
					"(case xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid  AND SFYX='1') when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = t.bdid  AND SFYX='1') end) SGDW,"+
					"(case xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid  AND SFYX='1') when '1' then (select jldw from GC_XMBD where GC_XMBD_ID = t.bdid  AND SFYX='1') end) JLDW," +
					"T1.GC_TCJH_XMXDK_ID,t1.YZDB,t1.JSMB,"+
					"(SELECT GC_XMGLGS_XXJD_ID FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS GC_XMGLGS_XXJD_ID,"+
					"(SELECT JZFK FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS JZFK,"+
					"(SELECT FKRQ FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS FKRQ,"+
					"(SELECT SJKGSJ FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS SJKGSJ,"+
					"(SELECT SJWGSJ FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS SJWGSJ,"+
					"(SELECT BZ FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS BZ,"+
					"(SELECT ZT FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS ZT,"+
					"(SELECT FXMS FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS FXMS,"+
					"(SELECT GC_XMGLGS_XXJD_JHBZ_ID FROM GC_XMGLGS_XXJD_JHBZ WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS GC_XMGLGS_XXJD_JHBZ_ID,"+
					"(SELECT GC_XMGLGS_XXJD_JHFK_ID FROM GC_XMGLGS_XXJD_JHFK WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS GC_XMGLGS_XXJD_JHFK_ID,"+
					"(SELECT XXJDID FROM GC_XMGLGS_XXJD_JHBZ WHERE T.GC_JH_SJ_ID = JHSJID AND SFYX='1')AS XXJDID,"+
					"(SELECT ZT_FK FROM GC_XMGLGS_XXJD_JHFK WHERE T.GC_JH_SJ_ID = JHSJID AND SFYX='1')AS ZT_FK,"+
					"(SELECT ZT_BZ FROM GC_XMGLGS_XXJD_JHBZ WHERE T.GC_JH_SJ_ID = JHSJID AND SFYX='1')AS ZT_BZ";
					sql += " FROM " +
					"GC_JH_SJ t,GC_TCJH_XMXDK T1 ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ZT", "XXJDZT");
			bs.setFieldDic("ZT_FK", "XXJDZT");
			bs.setFieldDic("ZT_BZ", "XXJDZT");
			//表选翻译
			//bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
			
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
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
	public String query_gcqs(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String nd = request.getParameter("nd");
			String index = request.getParameter("index");
			String xmglgs= request.getParameter("xmglgs"); 
			String time=Pub.getDate("yyyy-MM-dd", new Date());//当前日期
			String month=time.substring(5, 7);		
			String where = "";
			String ndgcqss = " and t.sfyx='1' and t1.nd='"+nd+"'";
			String bygcqss = " and t.sfyx='1' and to_char(t.qstcrq,'yyyy-mm')='"+nd+"-"+month+"' and t1.nd='"+nd+"' ";
			if(!Pub.empty(nd)){
				condition +=" AND T1.ND ='"+nd+"'";
			}
			if(!Pub.empty(xmglgs)){
				condition +=" AND T1.xmglgs ='"+xmglgs+"'";
			}
			if(!Pub.empty(index)){
				if(index.equals("26")){
					where = ndgcqss;
				}else if(index.equals("27")){
					where = bygcqss;
				}
				
			}
			condition += " AND t.jhsjid = t1.gc_jh_sj_id ";
			condition += where;
	        condition += BusinessUtil.getSJYXCondition("t");
	        condition += BusinessUtil.getCommonCondition(user,"t");
	        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"t1.gc_jh_sj_id,t1.nd,t1.xmbh,t1.xmmc,t1.bdid,t1.bdbh,t1.bdmc,t1.xmglgs,"
					+ "(case t1.xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) sgdw,"
					+ "(case t1.xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select jldw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) JLDW,"
					+ "(case t1.xmbs when '0' then (select sjdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select sjdw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) sjdw,"+
					"t.gc_gcgl_gcqs_id,t.xmid,t.sjbh,t.sfsjpq,t.ywlx,t.qsbt,t.QSTCRQ,t.QSYY,t.QSNR,t.BLRXM,t.BLRID,t.BLRQ,t.GSZJ,t.jhsjid,t.sfyx,t.JLDWXMJL,t.JLDWXMJLLXFS,t.SJDWFZR,t.SJDWFZRLXFS,t.SGDWXMZJ,t.SGDWXMZJLXFS "+
					" FROM " +
					"gc_jh_sj t1,gc_gcgl_gcqs t";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//计划表
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			
			//字典
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			//bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDBH");
			bs.setFieldDic("SFSJPQ","SF");//是否下发
			
			//金额格式化
			bs.setFieldThousand("GSZJ");//估算造价
			//日期
			bs.setFieldDateFormat("QSTCRQ", "yyyy-MM-dd");
			bs.setFieldDateFormat("BLRQ", "yyyy-MM-dd");
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
	public String query_lybzj(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String nd = request.getParameter("nd");
			String index = request.getParameter("index");
			String where = "";
			String yjnxm = " and t.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' and nd='" +nd+ "' and bdid is null)";
			String yjnbd = " and t.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' and nd='" +nd+ "' and (xmbs = '1' or ISNOBDXM = '1')) ";
			String ysps = " and t.fhqk='2' and t.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' and nd='" +nd+ "') ";
			String yfh = " and t.fhqk='6' and t.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' and nd='" +nd+ "') ";
			String bhsxs = " and t.jnfs='BH' and  t.sfyx=1 AND t.bhyxqz <SYSDATE and t.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' and nd='" +nd+ "')   ";
			if(!Pub.empty(nd)){
				condition +=" AND gjs2.ND ='"+nd+"'";
			}
			if(!Pub.empty(index)){
				if(index.equals("28")){
					where = yjnxm;
				}else if(index.equals("29")){
					where = yjnbd;
				}else if(index.equals("30")){
					where = ysps;
				}else if(index.equals("31")){
					where = yfh;
				}else if(index.equals("32")){
					where = bhsxs;
				}
				
			}
			
			condition += where;
	        condition += BusinessUtil.getSJYXCondition("t");
	        condition += BusinessUtil.getCommonCondition(user,"t");
	        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT gjs2.xmmc, gjs2.bdmc, gjs.htmc, gjs.htbm, t.*  FROM gc_zjgl_lybzj t LEFT JOIN gc_jh_sj gjs2 ON t.jhsjid=gjs2.gc_jh_sj_id  LEFT JOIN view_htgl_ht_htsj_xm gjs    ON t.jhsjid = gjs.jhsjid";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			 // 设置字典
            bs.setFieldDic("JNFS", "JNFS");// 交纳方式:现金、保函（则需要对方银行和始终日期）
            bs.setFieldDic("FHQK", "SHZT");// 返还情况
            bs.setFieldDic("BHLX", "BHLX");// 保函性质
            
            bs.setFieldUserID("BLR");

            // 设置千分位
            bs.setFieldThousand("JE");// 金额

            bs.setFieldTranslater("JNDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
            bs.setFieldTranslater("DFYH", "FS_COMMON_DICT", "DICT_ID", "DICT_NAME");

            bs.setFieldSjbh("SJBH");

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
	public String query_tqk(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String nd = request.getParameter("nd");
			String index = request.getParameter("index");
			String xmglgs= request.getParameter("xmglgs"); 
			String glb = "";
			String where = "";
			String ytqkcs = " and t.sqdw='"+xmglgs+"' and t.qknf='"+nd+"'  and t.sfyx = '1' ";
			String wcspcs = " and gzt2.id = gzt3.tqkid AND gzt3.bmmxid = gzt1.id AND gzt1.tqkid=t.id AND gzt2.tqkzt = 6  AND t.sqdw='"+xmglgs+"' and t.qknf='"+nd+"' and t.sfyx = '1' ";
			String ybfs   = " and gzt2.id = gzt3.tqkid AND gzt3.bmmxid = gzt1.id AND gzt1.tqkid=t.id AND gzt2.tqkzt = 7  AND t.sqdw='"+xmglgs+"' and t.qknf='"+nd+"' and t.sfyx = '1'";
			if(!Pub.empty(nd)){
				condition +=" AND T.QKNF ='"+nd+"'";
			}
			if(!Pub.empty(xmglgs)){
				condition +=" AND T.SQDW ='"+xmglgs+"'";
			}
			if(!Pub.empty(index)){
				if(index.equals("1")){
					where = ytqkcs;
				}else if(index.equals("2")){
					glb = ",gc_zjgl_tqk gzt2,gc_zjgl_tqkmx gzt3,gc_zjgl_tqkbmmx gzt1 ";
					where = wcspcs;
				}else if(index.equals("3")){
					glb = ",gc_zjgl_tqk gzt2,gc_zjgl_tqkmx gzt3,gc_zjgl_tqkbmmx gzt1 ";
					where = ybfs;
				}
				
			}
			
			condition += where;
	        condition += BusinessUtil.getSJYXCondition("t");
	        condition += BusinessUtil.getCommonCondition(user,"t");
	        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT (SELECT COUNT(*) FROM gc_zjgl_tqkbmmx gzt WHERE t.id = gzt.tqkid) AS bmjls,"+
						"(SELECT sum(gzt.bcsq) FROM gc_zjgl_tqkbmmx gzt WHERE t.id = gzt.tqkid) AS bmze,"+
					      " (SELECT COUNT(*)"+
					         " FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx"+
					         " WHERE gzt.id = mx.bmmxid"+
					         " AND t.id = gzt.tqkid"+
					          " AND (gzt.bmtqkmxzt = '6' or gzt.bmtqkmxzt = '7')) AS cwjls,"+
					       "(SELECT sum(gzt.cwshz)"+
					          "FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx"+
					        " WHERE gzt.id = mx.bmmxid"+
					          " AND t.id = gzt.tqkid"+
					          " AND (gzt.bmtqkmxzt = '6' or gzt.bmtqkmxzt = '7')) AS cwze,"+
					       "(SELECT COUNT(*)"+
					         " FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx"+
					         " WHERE gzt.id = mx.bmmxid"+
					           " AND t.id = gzt.tqkid"+
					           " AND gzt.bmtqkmxzt = '7') AS jcjls,"+
					       "(SELECT sum(gzt.jchdz)"+
					          "FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx"+
					        " WHERE gzt.id = mx.bmmxid"+
					          " AND t.id = gzt.tqkid"+
					         "  AND gzt.bmtqkmxzt = '7') AS jcze,"+
					       "t.*"+
					 " FROM gc_zjgl_tqkbm t "+glb;

            BaseResultSet bs = DBUtil.query(conn, sql, page);

            bs.setFieldThousand("ZXHTJ");// 最新合同价
            bs.setFieldThousand("YBF");// 已拔付
            bs.setFieldThousand("BCSQ");// 本次申请拔款
            bs.setFieldThousand("LJBF");// 累计拔付
            bs.setFieldThousand("CSZ");// 财审值
            bs.setFieldThousand("JZQR");// 监理确认计量款
            bs.setFieldThousand("CWSHZ");// 财务审核值
            bs.setFieldThousand("JCHDZ");// 计财核定值
            bs.setFieldThousand("ZWCZF");// 完成投资
            bs.setFieldThousand("ZHTZF");// 合同支付
            bs.setFieldThousand("JGC");// 甲供材
            bs.setFieldThousand("KQJGC");// 扣除甲供材

            bs.setFieldUserID("BMBLR");
            bs.setFieldUserID("BMSPR");
            bs.setFieldUserID("CWBLR");
            bs.setFieldUserID("JCBLR");

            bs.setFieldDic("TQKZT", "TQKZT");// 状态
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
