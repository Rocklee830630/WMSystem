package com.ccthanking.business.sjgl.sjxxk;

import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public  class SJXXKServiceImpl implements SJXXKService {
	private String ywlx=YwlxManager.GC_SJ;

	@Override
	public String xiangMuXinXi(User user, HttpServletRequest request) 
	{
		Connection conn = DBUtil.getConnection();
		String sjwybh = request.getParameter("sjwybh");
		String nd = request.getParameter("nd");
		String domresult = "";
		try {
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "select f.kbrq,jhsj.gc_jh_sj_id,xdk.gc_tcjh_xmxdk_id,xdk.xmmc,xdk.sjdw,xdk.fzr_sjdw,xdk.lxfs_sjdw" +
					" from gc_tcjh_xmxdk xdk, GC_JH_SJ jhsj, (select *  from gc_ztb_jhsj b  where b.gc_ztb_jhsj_id in " +
					" (select max(b.gc_ztb_jhsj_id)  from gc_ztb_xq a, gc_ztb_jhsj b where b.xqid = a.GC_ZTB_XQ_ID and a.ZBLX = '14'" +
					"  group by b.jhsjid)) c,  gc_ztb_xq d,  gc_ztb_xqsj_ys e, gc_ztb_sj f where rownum <= '1000'  " +
					"  and xdk.gc_tcjh_xmxdk_id=jhsj.xmid and c.xqid = d.gc_ztb_xq_id(+) and d.gc_ztb_xq_id = e.ztbxqid(+)" +
					" and e.ztbsjid = f.gc_ztb_sj_id(+) and jhsj.gc_jh_sj_id = c.jhsjid(+) and jhsj.sfyx = '1' and jhsj.sjwybh='"+sjwybh+"' and jhsj.nd="+nd;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("FZR_SJDW", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			domresult = bs.getJson();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryTongCJH(String json, User user,HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String sjwybh = request.getParameter("sjwybh");
		String nd = request.getParameter("nd");
		String domresult = "";
		try {
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sql = " select '初设批复' mc, a.cbsjpf wcsj,a.cbsjpf_sj sjsj,'拆迁图' mc1,a.cqt wcsj1,a.cqt_sj sjsj1  from  gc_jh_sj a where a.sjwybh='"+sjwybh+"' and a.nd="+nd+" " +
					"union all select '排迁图' mc,a.pqt wcsj,a.pqt_sj sjsj ,'施工图' mc1,a.sgt wcsj1,a.sgt_sj sjsj1 " +
					"from  gc_jh_sj a where  a.sjwybh='"+sjwybh+"' and a.nd="+nd+" ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryJiHuaWanCheng(String json, User user,HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String sjwybh = request.getParameter("sjwybh");
		String nd = request.getParameter("nd");
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			condition += "and gcsj.sjwybh(+)=jhsj.sjwybh and cbsj.sjwybh(+)=jhsj.sjwybh and " +
						"sjbg.sjwybh(+)=jhsj.sjwybh and sjwj.sjwybh(+)=jhsj.sjwybh " +
						"and gsjh.xmid(+)=jhsj.xmid and bd.jhsjid(+) = jhsj.gc_jh_sj_id and xm.jhsjid(+) = jhsj.gc_jh_sj_id  and jhsj.sjwybh='"+sjwybh+"' and jhsj.nd="+nd+"";
			condition += BusinessUtil.getSJYXCondition("jhsj")+BusinessUtil.getCommonCondition(user,null);
			condition += orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = " select jhsj.xmbs,jhsj.pxh,jhsj.xmbh,jhsj.xmmc,jhsj.bdmc,jhsj.xmid,jhsj.bdid,jhsj.jhid,jhsj.gc_jh_sj_id as jhsjid,sjbg.bg," +
						 "sjwj.jbgh,jhsj.cbsjpf_sj,jhsj.cqt_sj,jhsj.sgt_sj,jhsj.pqt_sj," +
						 "jhsj.cbsjpf_bz,jhsj.cqt_bz,jhsj.pqt_bz,jhsj.sgt_bz," +
						 "nvl(jhsj.wgsj_sj,jhsj.jg_sj) as jjgsj," +
						 //计划信息
						 "gsjh.JHGS,gsjh.sjgs,jhsj.CQT as JHCQT,jhsj.PQT as JHPQT,jhsj.SGT as JHSGT," +
						 "gcsj.gc_sj_id,gcsj.wcsj_kcbg,gcsj.wcsj_cqt,gcsj.wcsj_pqt,gcsj.wcsj_sgt_ssb,gcsj.wcsj_sgt_zsb," +
						 "cbsj.wcsj_ys,cbsj.ssjs,cbsj.sse,cbsj.bz as bz_gs,cbsj.cbsjpf,cbsj.pfnr,cbsj.gs,cbsj.gc_sj_cbsjpf_id " +				//初步設計批覆修改爲概算，此處的兩條信息针对批复内容和概算
						 ",nvl2(jhsj.bdid, bd.sjdw, xm.sjdw) as sjdw,nvl2(jhsj.bdid, bd.sgdw, xm.sgdw) as sgdw,nvl2(jhsj.bdid, bd.jldw, xm.jldw) as jldw "+
						 "from Gc_Jh_Sj jhsj,(select * from Gc_Sj where sfyx='1') gcsj," +
						 "(select max(decode(tzlb,'0',JSRQ,'')) as jbgh,sjwybh from GC_SJ_ZLSF_JS group by sjwybh) sjwj," +
						 "(select sj.sjwybh,sj.wcsj_ys,cbsjpf.bz,cbsjpf.gc_sj_cbsjpf_id,cbsjpf.cbsjpf,cbsjpf.pfnr,cbsjpf.sse,cbsjpf.ssjs,cbsjpf.gs,cbsjpf.xmid from Gc_Sj_Cbsjpf cbsjpf,(select * from Gc_Sj where sfyx='1') sj where sj.sjwybh=cbsjpf.sjwybh and cbsjpf.sfyx='1') cbsj," +
						 "(select count(*) as bg,sjwybh from gc_sj_sjbg_js2 where sfyx='1' and bgzt='1' group by sjwybh) sjbg," +
						 //添加初步设计计划时间显示
						 "(select jhsj.gc_jh_sj_id as gsjhid,jhsj.xmid, jhsj.cbsjpf as JHGS,jhsj.cbsjpf_sj as sjgs from  GC_JH_SJ jhsj where jhsj.bdid is null and jhsj.sfyx='1') gsjh," +
						 //施工监理单位查询
						 "(select xmbd.sjdw,xmbd.sgdw,xmbd.jldw, jhsj.gc_jh_sj_id as jhsjid from gc_xmbd xmbd, gc_jh_sj jhsj where xmbd.gc_xmbd_id = jhsj.bdid and xmbd.sfyx = '1') bd, " +
						 "(select xdk.sjdw,xdk.sgdw,xdk.jldw, jhsj.gc_jh_sj_id as jhsjid from gc_tcjh_xmxdk xdk, gc_jh_sj jhsj where xdk.gc_tcjh_xmxdk_id = jhsj.xmid and jhsj.bdid is null and xdk.sfyx = '1') xm ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryJianCeJianCe(String json, User user, HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String sjwybh = request.getParameter("sjwybh");
		String nd = request.getParameter("nd");
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			condition += " and zj.sjwybh(+)=jhsj.sjwybh and jhsj.sjwybh='"+sjwybh+"' and jhsj.nd="+nd;
			condition += BusinessUtil.getSJYXCondition("jhsj") + BusinessUtil.getCommonCondition(user,null);
			condition += orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql="select distinct jhsj.xmbs,jhsj.pxh,jhsj.xmbh,jhsj.xmmc, jhsj.bdmc, jhsj.bdbh, jhsj.jhid, jhsj.lrsj, jhsj.gc_jh_sj_id as jhsjid, jhsj.nd, jhsj.xmid, jhsj.bdid," +
			"zj.zjjc,zj.zjjcrq,zj.hftsjc,zj.hftsjcrq,zj.djzsyjc,zj.djzsyjcrq from (select sjwybh, " +
					"max(decode(bglb, '0300', jhsjid, '')) zjjc," +
					"max(decode(bglb, '0300', jsrq, '')) zjjcrq, " +
					"max(decode(bglb, '0301', jhsjid, '')) as hftsjc," +
					"max(decode(bglb, '0301', jsrq, '')) hftsjcrq, " +
					"max(decode(bglb, '0302', jhsjid, '')) djzsyjc," +
					"max(decode(bglb, '0302', jsrq, '')) djzsyjcrq " +
					"from GC_SJ_BGSF_JS where sfyx='1'" +
					"  group by sjwybh) zj, GC_JH_SJ jhsj ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldFileUpload("ZJJC", "0300");
			bs.setFieldFileUpload("HFTSJC", "0301");
			bs.setFieldFileUpload("DJZSYJC", "0302");
			bs.setFieldDic("BGLB", "BGLB");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryJiaoJunGong(String json, User user, HttpServletRequest request) {
		
		Connection conn = DBUtil.getConnection();
		String sjwybh = request.getParameter("sjwybh");
		String nd = request.getParameter("nd");
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			condition += "  and jhsj.xmid = xdk.gc_tcjh_xmxdk_id and jjg.jhsjid(+)=jhsj.gc_jh_sj_id  and jhsj.bdid =bd.gc_xmbd_id(+)  and jhsj.sjwybh='"+sjwybh+"' and jhsj.nd="+nd+"";
		
			condition += BusinessUtil.getSJYXCondition("jhsj")+BusinessUtil.getCommonCondition(user,null);
			condition += BusinessUtil.getSJYXCondition("jjg")+BusinessUtil.getCommonCondition(user,null);
			condition += orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = " select jjg.gc_sjgl_jjg_id jiaogongfj,jjg.gc_sjgl_jjg_id jungongfj,jjg.jiaogjsr,jjg.jungjsdw,jjg.JUNGJSR," +
					"jjg.gc_sjgl_jjg_id,jhsj.bdbh as bdbh,jjg.wjgys,jjg.wjgysy,jjg.jiaogjsdw,jjg.jgysrq, jjg.jgyssj,jhsj.xmid,jjg.ywlx," +
					"jhsj.bdid,jhsj.xmbs, jhsj.bdmc,jjg.lrsj,xdk.gc_tcjh_xmxdk_id,jhsj.wgsj_sj wgrq,xdk.xmmc," +
					"xdk.jsnr,xdk.sjdw,xdk.isnatc,xdk.fzr_sjdw,jhsj.xmglgs,xdk.yzdb,xdk.nd,xdk.xmbh as xmbh," +
					"xdk.xmlx,xdk.sfyx as xsfyx ,jhsj.gc_jh_sj_id  JHSJID from GC_SJGL_JJG jjg,GC_TCJH_XMXDK xdk ,gc_jh_sj jhsj, gc_xmbd bd ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryHeTongXinXi(String json, User user,HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String sjwybh = request.getParameter("sjwybh");
		String nd = request.getParameter("nd");
		String department = request.getParameter("department");
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(sjwybh))
			{
			condition += "   and jhsj.sjwybh='"+sjwybh+"' and jhsj.nd="+nd+" ";
			}
			if(!Pub.empty(department))
			{
			condition += "  and  ghh.ZBBMID  ='"+department+"' ";
			}
			condition += BusinessUtil.getSJYXCondition("jhsj")+BusinessUtil.getCommonCondition(user,null);
			condition += orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = " SELECT ghh.ID AS htid, ghh.qdnf, ghh.htmc, ghh.htbm, ghh.htlx, ghh.zhtqdj, ghh2.id as htsjid," +
					" jhsj.gc_jh_sj_id AS jhsjid, jhsj.xmid,jhsj.xmbh,jhsj.xmmc, jhsj.xmxz, jhsj.nd, jhsj.bdid,jhsj.bdmc,ghh.yfdw, " +
					"  ghh.htjqdrq ,'' AS xmlx, '' AS sgdwid  ,'' AS sgdwmc, '' AS jldwid, '' AS jldwmc, ghh.htzt, " +
					" nvl(ghh.ZHTQDJ, 0) htqdj,  nvl(ghh.ZHTZF, '0') zfje,  decode(ghh.ZHTQDJ,'',0,0, 0, round((nvl(ghh.ZHTZF, 0) / ghh.ZHTQDJ) * 100, 2)) zfl, " +
					"     nvl(ghh.ZHTQDJ, 0) - nvl(ghh.ZHTZF, 0) wzf, jhsj.gc_jh_sj_id FROM  gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2" +
					" ON ghh.ID=ghh2.htid LEFT JOIN gc_jh_sj jhsj ON jhsj.gc_jh_sj_id=ghh2.jhsjid " +
					"left join  GC_HTGL_HT_HTZF thzf on ghh2.id=thzf.htsjid ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("HTLX", "HTLX");
	            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
	            bs.setFieldDic("FBFS", "FBFS");// 发包方式
	            bs.setFieldDic("QDNF", "XMNF");// 项目年份
	            bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同
	            // 日期
	            bs.setFieldDateFormat("HTJQDRQ", "yyyy-MM-dd");
	            bs.setFieldThousand("ZHTQDJ");// 总合同签订价(元)
	            bs.setFieldThousand("HTQDJ");// 合同签订价(元)
	            bs.setFieldThousand("ZFJE");// 合同签订价(元)
	            bs.setFieldThousand("WZF");// 合同签订价(元)
	            bs.setFieldTranslater("YFDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;	
	}
	
	

	
}
