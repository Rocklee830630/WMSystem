package com.ccthanking.business.ztb.bmjk.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.ztb.bmjk.service.ZtbBmjkService;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

@Service

public class ZtbBmjkServiceImpl implements ZtbBmjkService{
	private static String propertyFileName = "com.ccthanking.properties.business.bmjk.bmjk_ztb";
	@Override
	public String queryZtbFormInfo(HttpServletRequest request)	throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			//拼接年度查询条件
			if(!Pub.empty(nd)){
				nd = " and to_char(lrsj,'YYYY')='"+nd+"'";
			}else{
				nd = "";
			}
			StringBuffer sb = new StringBuffer();
			sb.append("select ");
			//累计招标需求数
			sb.append(" (select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ where sfyx='1' and xqzt in ('3','5','6')) as LJZBXQ ");
			//累计已完成招标数（需求数）
			sb.append(", (select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ where xqzt='6' and sfyx='1') as LJYWZB");
			//共计（总中标价求和）
			sb.append(", (select sum(nvl(zzbj,0)) from GC_ZTB_SJ where xqzt='2' and sfyx='1') as GJ");
			//累计已完成百分比（计算已完成招标的需求）
			sb.append(", (select to_char(round((YWC/ZS)*100,2),'fm99999999999990.00') from (select sum(decode(xqzt,'3',1,decode(xqzt,'5',1,decode(xqzt,'6',1,0)))) ZS,sum(decode(xqzt,'6',1,0)) YWC from GC_ZTB_XQ where sfyx='1')) LJWCL");
			//年度招标需求数
			sb.append(", (select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ where sfyx='1' and xqzt in ('3','5','6') "+nd+") as NDZBXQ");
			//年度已完成招标数（需求数）
			sb.append(", (select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ where xqzt='6' and sfyx='1' "+nd+") as NDYWZB");
			//合计（年度总中标价求和）
			sb.append(", (select nvl(sum(nvl(zzbj,0)),0) from GC_ZTB_SJ where xqzt='2' and sfyx='1' "+nd+") as HJ");
			//年度已完成百分比（计算已完成招标需求）
			sb.append(", (select nvl(to_char(round((YWC/ZS)*100,2),'fm99999999999990.00'),0) from (select sum(decode(xqzt,'3',1,decode(xqzt,'5',1,decode(xqzt,'6',1,0)))) ZS,sum(decode(xqzt,'6',1,0)) YWC from GC_ZTB_XQ where sfyx='1' "+nd+")) NDWCL ");
			sb.append(" from dual ");
			String sql = sb.toString();
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("GJ");	//共计
			bs.setFieldThousand("HJ");	//合计
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
	public String queryZtbTableInfo(HttpServletRequest request) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(1000);
			//拼接年度查询条件
			if(!Pub.empty(nd)){
				nd = " and to_char(X.LRSJ,'YYYY')='"+nd+"'";
			}else{
				nd = "";
			}
			StringBuffer sb = new StringBuffer();
			sb.append("select ");
			sb.append(" dept.bmjc dept_name,nvl(XQLJTQ,0) XQLJTQ,nvl(XQLJYW,0) XQLJYW,nvl(XQNDTQ,0) XQNDTQ,nvl(XQNDYW,0) XQNDYW,");
			sb.append("nvl(SGLJTQ,0) SGLJTQ,nvl(SGLJWC,0) SGLJWC,nvl(SGLJZZ,0) SGLJZZ,nvl(SGNDTQ,0) SGNDTQ,nvl(SGNDWC,0) SGNDWC,nvl(SGNDZZ,0) SGNDZZ,");
			sb.append("nvl(JLLJTQ,0) JLLJTQ,nvl(JLLJWC,0) JLLJWC,nvl(JLLJZZ,0) JLLJZZ,nvl(JLNDTQ,0) JLNDTQ,nvl(JLNDWC,0) JLNDWC,nvl(JLNDZZ,0) JLNDZZ,");
			sb.append("nvl(SJLJTQ,0) SJLJTQ,nvl(SJLJWC,0) SJLJWC,nvl(SJLJZZ,0) SJLJZZ,nvl(SJNDTQ,0) SJNDTQ,nvl(SJNDWC,0) SJNDWC,nvl(SJNDZZ,0) SJNDZZ,");
			sb.append("nvl(QTLJTQ,0) QTLJTQ,nvl(QTLJWC,0) QTLJWC,nvl(QTLJZZ,0) QTLJZZ,nvl(QTNDTQ,0) QTNDTQ,nvl(QTNDWC,0) QTNDWC,nvl(QTNDZZ,0) QTNDZZ");
			sb.append(" from FS_ORG_DEPT dept ");
			//总累计
			sb.append(",(select count(GC_ZTB_XQ_ID) XQLJTQ,LRBM from GC_ZTB_XQ where sfyx='1' and xqzt in ('3','5','6') group by LRBM) a1");
			sb.append(",(select count(GC_ZTB_XQ_ID) XQLJYW,LRBM from GC_ZTB_XQ where sfyx='1' and xqzt ='6' group by LRBM) a2");
			//总年度
			sb.append(",(select count(GC_ZTB_XQ_ID) XQNDTQ,LRBM from GC_ZTB_XQ X where sfyx='1' and xqzt in ('3','5','6') "+nd+" group by LRBM) a3");
			sb.append(",(select count(GC_ZTB_XQ_ID) XQNDYW,LRBM from GC_ZTB_XQ X where sfyx='1' and xqzt ='6' "+nd+" group by LRBM) a4");
			//施工累计
			sb.append(",(select count(GC_ZTB_XQ_ID) SGLJTQ,LRBM from GC_ZTB_XQ where sfyx='1' and xqzt in ('3','5','6') and ZBLX='13' group by LRBM) b1");
			sb.append(",(select count(GC_ZTB_XQ_ID) SGLJWC,LRBM from GC_ZTB_XQ where sfyx='1' and xqzt ='6' and ZBLX='13' group by LRBM) b2");
//			sb.append(",(select decode(count(J.JHSJID),0,sum(S.ZZBJ),sum(J.ZZBJ)) SGLJZZ,X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='13' group by X.LRBM) b3");
			//上面这句是区分总中标价和子中标价的，下面这句是只查询总中标价的
			sb.append(",(select sum(nvl(S.ZZBJ,0)) SGLJZZ,X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='13' group by X.LRBM) b3");
			//施工年度
			sb.append(",(select count(GC_ZTB_XQ_ID) SGNDTQ,LRBM from GC_ZTB_XQ X where sfyx='1' and xqzt in ('3','5','6') "+nd+" and ZBLX='13' group by LRBM) b4");
			sb.append(",(select count(GC_ZTB_XQ_ID) SGNDWC,LRBM from GC_ZTB_XQ X where sfyx='1' and xqzt ='6' "+nd+" and ZBLX='13' group by LRBM) b5");
//			sb.append(",(select decode(count(J.JHSJID),0,sum(S.ZZBJ),sum(J.ZZBJ)) SGNDZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='13' "+nd+" group by X.LRBM) b6");
			sb.append(",(select sum(nvl(S.ZZBJ,0)) SGNDZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='13' "+nd+" group by X.LRBM) b6");
			
			//监理累计
			sb.append(",(select count(GC_ZTB_XQ_ID) JLLJTQ,LRBM from GC_ZTB_XQ where sfyx='1' and xqzt in ('3','5','6') and ZBLX='12' group by LRBM) c1");
			sb.append(",(select count(GC_ZTB_XQ_ID) JLLJWC,LRBM from GC_ZTB_XQ where sfyx='1' and xqzt ='6' and ZBLX='12' group by LRBM) c2");
//			sb.append(",(select decode(count(J.JHSJID),0,sum(S.ZZBJ),sum(J.ZZBJ)) JLLJZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='12' group by X.LRBM) c3");
			sb.append(",(select sum(nvl(S.ZZBJ,0)) JLLJZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='12' group by X.LRBM) c3");
			//监理年度
			sb.append(",(select count(GC_ZTB_XQ_ID) JLNDTQ,LRBM from GC_ZTB_XQ X where sfyx='1' and xqzt in ('3','5','6') "+nd+" and ZBLX='12' group by LRBM) c4");
			sb.append(",(select count(GC_ZTB_XQ_ID) JLNDWC,LRBM from GC_ZTB_XQ X where sfyx='1' and xqzt ='6' "+nd+" and ZBLX='12' group by LRBM) c5");
//			sb.append(",(select decode(count(J.JHSJID),0,sum(S.ZZBJ),sum(J.ZZBJ)) JLNDZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='12' "+nd+" group by X.LRBM) c6");
			sb.append(",(select sum(nvl(S.ZZBJ,0)) JLNDZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='12' "+nd+" group by X.LRBM) c6");
			//设计累计
			sb.append(",(select count(GC_ZTB_XQ_ID) SJLJTQ,LRBM from GC_ZTB_XQ where sfyx='1' and xqzt in ('3','5','6') and ZBLX='11' group by LRBM) d1");
			sb.append(",(select count(GC_ZTB_XQ_ID) SJLJWC,LRBM from GC_ZTB_XQ where sfyx='1' and xqzt ='6' and ZBLX='11' group by LRBM) d2");
//			sb.append(",(select decode(count(J.JHSJID),0,sum(S.ZZBJ),sum(J.ZZBJ)) SJLJZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='11' group by X.LRBM) d3");
			sb.append(",(select sum(nvl(S.ZZBJ,0)) SJLJZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='11' group by X.LRBM) d3");
			//设计年度
			sb.append(",(select count(GC_ZTB_XQ_ID) SJNDTQ,LRBM from GC_ZTB_XQ X where sfyx='1' and xqzt in ('3','5','6') "+nd+" and ZBLX='11' group by LRBM) d4");
			sb.append(",(select count(GC_ZTB_XQ_ID) SJNDWC,LRBM from GC_ZTB_XQ X where sfyx='1' and xqzt ='6' "+nd+" and ZBLX='11' group by LRBM) d5");
//			sb.append(",(select decode(count(J.JHSJID),0,sum(S.ZZBJ),sum(J.ZZBJ)) SJNDZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='11' "+nd+" group by X.LRBM) d6");
			sb.append(",(select sum(nvl(S.ZZBJ,0)) SJNDZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='11' "+nd+" group by X.LRBM) d6");
			//其他累计
			sb.append(",(select count(GC_ZTB_XQ_ID) QTLJTQ,LRBM from GC_ZTB_XQ where sfyx='1' and xqzt in ('3','5','6') and ZBLX='18' group by LRBM) e1");
			sb.append(",(select count(GC_ZTB_XQ_ID) QTLJWC,LRBM from GC_ZTB_XQ where sfyx='1' and xqzt ='6' and ZBLX='18' group by LRBM) e2");
//			sb.append(",(select decode(count(J.JHSJID),0,sum(S.ZZBJ),sum(J.ZZBJ)) QTLJZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='18' group by X.LRBM) e3");
			sb.append(",(select sum(nvl(S.ZZBJ,0)) QTLJZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='18' group by X.LRBM) e3");
			//其他年度
			sb.append(",(select count(GC_ZTB_XQ_ID) QTNDTQ,LRBM from GC_ZTB_XQ X where sfyx='1' and xqzt in ('3','5','6') "+nd+" and ZBLX='18' group by LRBM) e4");
			sb.append(",(select count(GC_ZTB_XQ_ID) QTNDWC,LRBM from GC_ZTB_XQ X where sfyx='1' and xqzt ='6' "+nd+" and ZBLX='18' group by LRBM) e5");
//			sb.append(",(select decode(count(J.JHSJID),0,sum(S.ZZBJ),sum(J.ZZBJ)) QTNDZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='18' "+nd+" group by X.LRBM) e6");
			sb.append(",(select sum(nvl(S.ZZBJ,0)) QTNDZZ, X.LRBM from GC_ZTB_XQ X,GC_ZTB_JHSJ J,(select ZZBJ,SFYX,GC_ZTB_SJ_ID from GC_ZTB_SJ where SFYX = '1') S,GC_ZTB_XQSJ_YS Y where X.gc_ztb_xq_id=J.xqid(+) and X.GC_ZTB_XQ_ID=Y.ZTBXQID(+) and Y.ZTBSJID=S.GC_ZTB_SJ_ID(+) and x.xqzt='6' and X.SFYX='1' and X.ZBLX='18' "+nd+" group by X.LRBM) e6");
			//关联条件
			sb.append(" where ");
			sb.append("dept.row_id=a1.LRBM(+)");
			sb.append(" and dept.row_id=a2.LRBM(+)");
			sb.append(" and dept.row_id=a3.LRBM(+)");
			sb.append(" and dept.row_id=a4.LRBM(+)");
			sb.append(" and dept.row_id=b1.LRBM(+)");
			sb.append(" and dept.row_id=b2.LRBM(+)");
			sb.append(" and dept.row_id=b3.LRBM(+)");
			sb.append(" and dept.row_id=b4.LRBM(+)");
			sb.append(" and dept.row_id=b5.LRBM(+)");
			sb.append(" and dept.row_id=b6.LRBM(+)");
			sb.append(" and dept.row_id=c1.LRBM(+)");
			sb.append(" and dept.row_id=c2.LRBM(+)");
			sb.append(" and dept.row_id=c3.LRBM(+)");
			sb.append(" and dept.row_id=c4.LRBM(+)");
			sb.append(" and dept.row_id=c5.LRBM(+)");
			sb.append(" and dept.row_id=c6.LRBM(+)");
			sb.append(" and dept.row_id=d1.LRBM(+)");
			sb.append(" and dept.row_id=d2.LRBM(+)");
			sb.append(" and dept.row_id=d3.LRBM(+)");
			sb.append(" and dept.row_id=d4.LRBM(+)");
			sb.append(" and dept.row_id=d5.LRBM(+)");
			sb.append(" and dept.row_id=d6.LRBM(+)");
			sb.append(" and dept.row_id=e1.LRBM(+)");
			sb.append(" and dept.row_id=e2.LRBM(+)");
			sb.append(" and dept.row_id=e3.LRBM(+)");
			sb.append(" and dept.row_id=e4.LRBM(+)");
			sb.append(" and dept.row_id=e5.LRBM(+)");
			sb.append(" and dept.row_id=e6.LRBM(+)");
			sb.append(" and dept.row_id in (" +
					"'100000000016'," +	//设计
					"'100000000014'," +	//前期
//					"'100000000017'," +	//排迁,用户又说不要排迁了
					"'100000000005'," +	//征收
					"'100000000009'," +	//招投标
					"'100000000004'," +	//造价
					"'100000000006'," +	//质量安全
					"'100000000021'," +	//长城投
					"'100000000010'," +	//鸿安
					"'100000000011'," +	//中豪
					"'100000000012'," +	//华星
					"'100000000013'" +	//润德
					")");	//
			sb.append(" order by sort");
			String sql = sb.toString();
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("SGLJZZ");	//施工累计总值
			bs.setFieldThousand("SGNDZZ");	//施工年度总值
			bs.setFieldThousand("JLLJZZ");	//监理累计总值
			bs.setFieldThousand("JLNDZZ");	//监理年度总值
			bs.setFieldThousand("SJLJZZ");	//设计累计总值
			bs.setFieldThousand("SJNDZZ");	//设计年度总值
			bs.setFieldThousand("QTLJZZ");	//其他累计总值
			bs.setFieldThousand("QTNDZZ");	//其他年度总值
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	/**
	 * 数据钻取查询查询
	 * 排迁内业和排迁任务都用这个方法呢，修改的时候要注意
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryDrillingDataZbxqzh(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "THEADSQL_ZBXQZH");
			sql = sql.replace("%CONDSQL%",Pub.getPropertiesSqlValue(propertyFileName, proKey));
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(lrsj,'YYYY')='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ZBLX", "ZBLX");//招标类型
			bs.setFieldDic("ZBFS", "ZBFS");//招标方式
			bs.setFieldDic("XQZT", "SF");//招标方式
			bs.setFieldDic("XQZT", "XQZT");//垫资方式
			bs.setFieldDic("TBJFS", "TBBJFS");//投标报价方式
			bs.setFieldThousand("YSE");//预算额
			bs.setFieldSjbh("sjbh");//事件编号
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
