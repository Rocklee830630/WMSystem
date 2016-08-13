package com.ccthanking.business.zjb.zjbmjk;

import java.sql.Connection;

import org.springframework.stereotype.Service;

import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class ZjbmjkServiceImpl {

	/**
	 * 累计结算合同
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public String queryLjjsht(String json, String nd, String zjhtlx) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

			PageManager page = RequestUtil.getPageManager(json);
			if (page == null)
				page = new PageManager();
			
			String sql = "";
			if("BMJK_ZJ_LJJSHT".equals(zjhtlx)) {
				sql = "SELECT HT.* FROM GC_ZJB_JSB T,GC_HTGL_HTSJ HTSJ,GC_HTGL_HT HT "
						 + "WHERE T.HTID=HTSJ.ID AND HTSJ.HTID=HT.ID AND T.JSZT = '6' "
						 + "AND T.SFYX = '1' AND HTSJ.SFYX='1'";
			} else if("BMJK_ZJ_NDJSHT".equals(zjhtlx)) {
				sql = "SELECT GHH.* "
						+ "FROM GC_HTGL_HT GHH "
						+ "LEFT JOIN GC_HTGL_HTSJ GHH2     ON GHH.ID = GHH2.HTID "
						+ "LEFT JOIN GC_JH_SJ JHSJ         ON JHSJ.GC_JH_SJ_ID = GHH2.JHSJID "
						+ "LEFT JOIN GC_ZJB_JSB JSB        ON JSB.JHSJID = JHSJ.GC_JH_SJ_ID "
						+ "WHERE GHH.SFYX = '1' "
						+ "AND JHSJ.WGSJ_SJ IS NOT NULL "
						+ "AND GHH.HTLX = 'SG' "
						+ "AND JHSJ.ND = '" + nd + "' "
						+ "OR (GHH.SFYX = '1' AND GHH.HTLX = 'SG' AND JHSJ.WGSJ_SJ IS NOT NULL AND "
						+ "JHSJ.ND = '" + (Integer.parseInt(nd)-1) + "' AND "
						+ "JHSJ.GC_JH_SJ_ID NOT IN (SELECT JSB.JHSJID FROM GC_ZJB_JSB JSB)) ";
			} else if("BMJK_ZJ_YWCJS".equals(zjhtlx)) {
				sql = "SELECT HT.* "
						+ "FROM GC_ZJB_JSB 					ZJB, " 
						+ "GC_JH_SJ 						JHSJ, "
						+ "GC_HTGL_HTSJ 					HTSJ, " 
						+ "GC_HTGL_HT 						HT "
						+ "WHERE ZJB.JHSJID = JHSJ.GC_JH_SJ_ID "
						+ "AND ZJB.SFYX = '1' "
						+ "AND JSZT = '6' "
						+ "AND JHSJ.ND = '" + nd + "' "
						+ "AND HTSJ.JHSJID=JHSJ.GC_JH_SJ_ID "
						+ "AND HTSJ.HTID=HT.ID";
			} else if ("BMJK_ZJ_ZZJSHTGS".equals(zjhtlx)) {
				sql = "SELECT HT.*  "
						+ "FROM GC_ZJB_JSB JSB, GC_JH_SJ JHSJ, "
						+ "GC_HTGL_HTSJ HTSJ, GC_HTGL_HT HT "
						+ "WHERE JSB.JHSJID = JHSJ.GC_JH_SJ_ID "
						+ "AND JSB.SFYX = '1' "
						+ "AND JSB.JSZT IN (0, 1, 2, 3, 4, 5) "
						+ "AND JHSJ.ND = '" + nd + "' "
						+ "AND HTSJ.JHSJID=JHSJ.GC_JH_SJ_ID "
						+ "AND HTSJ.HTID=HT.ID";
			}
			
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同
            bs.setFieldDic("SFZFJTZ", "SF");// 是否支付即投资
            bs.setFieldDic("SFDXMHT", "SF");// 是否单项目
			domresult = bs.getJson();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	private String getPropertiesName(String lujing) {
		String propertiesName = "";
		try {
			if ("zjbz".equals(lujing)) {  // 部门监控-造价监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_zjb";
			} else if ("070001".equals(lujing)) { // 部门监控-前期监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_qq";
			} else if ("050001".equals(lujing)) { // 部门监控-工程监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_gc";
			} else if ("090001".equals(lujing)) { // 部门监控-造价监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_zjb";
			} else if ("100001".equals(lujing)) { // 部门监控-征收监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_zs";
			} else if ("030001".equals(lujing)) { // 部门监控-排迁监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_pq";
			}else if ("060001".equals(lujing)) { // 部门监控-项管理公司监控
				propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_xmglgs";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return propertiesName;
	}

	public String queryLbj(String json, String lujing, String mingchen,String tiaojian, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		PageManager page = RequestUtil.getPageManager(json);
		if (page == null)
			page = new PageManager();
			String lj=getPropertiesName(lujing);
			String sql = Pub.getPropertiesSqlValue(lj, mingchen);
			if ("LJBZLBJ".equals(tiaojian)) { 
				
				sql=sql+" and jhsj.iscs='1' and jhsj.cs_sj is not null   ";
				
			} else if ("WTXYQDWCXMS".equals(tiaojian)) { 
				
				sql = sql+"    and lbj.ZXGS is not null and jhsj.sfyx = '1'  and jhsj.iscs = '1'  and jhsj.cs_sj is not null ";
			
			}else if ("NDLJBZLBJ".equals(tiaojian)) { 
				
				sql = sql+" and jhsj.iscs = '1'  and jhsj.cs_sj is not null and jhsj.nd="+nd;
				
			}else if ("NDWTXYQDWCXMS".equals(tiaojian)) { 
				
				sql = sql+"    and lbj.ZXGS is not null and jhsj.sfyx = '1' " +
						" and jhsj.iscs = '1'  and jhsj.cs_sj is not null  and jhsj.nd="+nd;
			} else if ("YWZBXM".equals(tiaojian)) { //已完招标项目数
				
			}else if ("NDYWZBXM".equals(tiaojian)) { //年度已完招标项目数
				sql = sql+"   and jhsj.nd="+nd;
			} 
			sql=sql+"  order by jhsj.xmbh, jhsj.xmbs, jhsj.pxh asc";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("SBCSZ"); 
			bs.setFieldThousand("CSSDZ"); 
			bs.setFieldThousand("SJZ"); 
			bs.setFieldThousand("ZDJ");
			bs.setFieldTranslater("ZXGS", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			
			bs.setFieldDic("ZBFS", "ZBFS");//招标方式
			bs.setFieldDic("GGQK", "GGQK");//公告情况
			bs.setFieldDic("DZFS", "DZFS");//垫资方式
			bs.setFieldDic("XQZT", "ZBZT");//垫资方式
			bs.setFieldThousand("ZZBJ");//总中标价
			bs.setFieldThousand("YLJ");//预留金
			bs.setFieldThousand("YJNLYBZJ");//应缴纳履约保证金
			bs.setFieldThousand("JHZZTZJ");//计划中主体造价
			bs.setFieldTranslater("DSFJGID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("ZBDL", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldSjbh("SJBH");
			domresult = bs.getJson();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	public String queryzjxx(String json, String lujing, String mingchen,String tiaojian, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		PageManager page = RequestUtil.getPageManager(json);
		if (page == null)
			page = new PageManager();
			String lj=getPropertiesName(lujing);
			String sql = Pub.getPropertiesSqlValue(lj, mingchen);
			if ("LJJSHT".equals(tiaojian)) { //累计结算合同
				
				sql=sql+" and D.sfyx = '1' and g.sfyx='1'  and d.JSZT='6'   ";
				
			} else if ("YWCJS".equals(tiaojian)) { //已完成结算
				
				sql = sql+" and D.sfyx = '1' and g.sfyx='1'    and d.JSZT='6'  and g.nd="+nd;
			
			}else if ("ZZJSHTGS".equals(tiaojian)) { //正在结算合同
				
				sql = sql+" and D.sfyx = '1' and g.sfyx='1'   and d.jszt in (0,1,2,3,4,5) and g.nd="+nd;
				
			}else if ("WTZXDW".equals(tiaojian)) { //委托咨询单位审核项目数
				
				sql = sql+" and D.sfyx = '1' and g.sfyx='1'  and  d.SFWT='1'  and g.nd="+nd;
				
			}else if ("NDJSHT".equals(tiaojian)) { //年度具备结算合同
				
				sql = sql+  " AND ((c.sfyx = '1' and g.wgsj_sj is not null " +
							" and c.htlx = 'SG' and  c.htzt in ('1', '2') and g.nd = '"+nd+"') " +
							" or (c.sfyx = '1' and c.htlx = 'SG' and g.wgsj_sj is not null " +
							" and  g.nd < ('"+nd+"') and  g.gc_jh_sj_id not in " +
							" (select jsb.jhsjid from gc_zjb_jsb jsb) and    c.htzt = '1')) ";
			}
			
			sql=sql+"   order by  g.xmbh,g.xmbs,g.pxh asc ";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("JSZT", "JSZT");
			bs.setFieldDic("SFWT", "SF");
			bs.setFieldDic("HTQDFS", "FBFS");
			
			bs.setFieldThousand("HTJE");
			bs.setFieldThousand("TBJE");
			bs.setFieldThousand("YZSDJE");
			bs.setFieldThousand("CSSDJE");
			bs.setFieldThousand("SJSDJE");
			bs.setFieldThousand("TBYZ");
			bs.setFieldThousand("YZCS");
			bs.setFieldThousand("CSSJ");
			bs.setFieldThousand("TBSJ");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("WTZXGS", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			
			domresult = bs.getJson();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
