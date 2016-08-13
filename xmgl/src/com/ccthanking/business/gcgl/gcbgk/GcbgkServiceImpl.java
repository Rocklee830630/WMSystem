package com.ccthanking.business.gcgl.gcbgk;

import java.sql.Connection;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class GcbgkServiceImpl implements GcbgkService {
	//年度洽商数
	@Override
	public String gcqs_zq_nd(String json,User user,String nd,String xmglgs) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();		
				if(!Pub.empty(xmglgs))
				{
					xmglgs=" and xmglgs='"+xmglgs+"'";
				}
				else
				{
					xmglgs="";
				}	
				condition +=" and sfyx='1' and nd="+nd+xmglgs;
				if (page == null)
					page = new PageManager();
					page.setFilter(condition);
					conn.setAutoCommit(false);
					String sql =" select gc_gcgl_gcqs_id, nd, xmmc, bdid, bdmc, sjdw, jldw, sgdw, xmjl, xmglgs, qstcrq, qsyy, qsnr, blrxm, blrq, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, bh, xmid, blrid, jhsjid from gc_gcgl_gcqs" ;
					BaseResultSet bs = DBUtil.query(conn, sql, page);					
					bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");
					bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDBH");				
					domresult = bs.getJson();
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	
	
	//本月洽商数
	public String gcqs_zq_by(String json,User user,String nd,String xmglgs) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();	
				if(!Pub.empty(xmglgs))
				{
					xmglgs=" and xmglgs='"+xmglgs+"'";
				}
				else
				{
					xmglgs="";
				}	
				String time=Pub.getDate("yyyy-MM-dd", new Date());//当前日期
				String yf=time.substring(0,7);
				condition +=" and sfyx = '1' and to_char(qstcrq, 'yyyy-mm') = '"+yf+"' and nd = '"+nd+"'"+xmglgs;
				if (page == null)
					page = new PageManager();
					page.setFilter(condition);
					conn.setAutoCommit(false);
					String sql =" select gc_gcgl_gcqs_id, nd, xmmc, bdid, bdmc, sjdw, jldw, sgdw, xmjl, xmglgs, qstcrq, qsyy, qsnr, blrxm, blrq, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, bh, xmid, blrid, jhsjid from gc_gcgl_gcqs" ;
					BaseResultSet bs = DBUtil.query(conn, sql, page);					
					bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");
					bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDBH");				
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
