package com.ccthanking.business.qqsx.bzjk;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;


@Service
public  class QqsxBzjkServiceImpl implements QqsxBzjkService {
	private String propertyFileName = "com.ccthanking.properties.business.bmjk.bmjk_qqb_new";
	
	//各手续情况
	@Override
	public String querySx(HttpServletRequest request) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "sxList");
			//拼接年度查询条件
			sql=sql.replaceAll("%Condition%", nd);
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


	//各手续情况-柱形图
	public String querySxZxtColumn2d(HttpServletRequest request, String json) {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "sxZxt");
			//拼接年度查询条件

			sql=sql.replaceAll("%Condition%", nd);
			page.setPageRows(1000);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap<String, Object> rowMap = new HashMap<String, Object>();
			HashMap stackMap = new HashMap();
			stackMap.put("应办理", "1");
			stackMap.put("已办理", "2");
			stackMap.put("部分办理", "2");
			rowMap.put("STACK", stackMap);
			
			// 覆盖默认属性，显示图例
			HashMap<String, Object> charMap = new HashMap<String, Object>();
			charMap.put("showlegend", "1");
			domresult = ChartUtil.makeBarEChartJsonString(domresult, charMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//施工查询
	@Override
	public String querySg(HttpServletRequest request) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "sgList");
			//拼接年度查询条件
			sql=sql.replaceAll("%Condition%", nd);
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


	//施工手续情况-柱形图
	public String querySgZxtColumn2d(HttpServletRequest request, String json) {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertyFileName, "sgZxt");
			sql=sql.replaceAll("%Condition%", nd);
			//拼接年度查询条件
			
			page.setPageRows(1000);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap<String, Object> rowMap = new HashMap<String, Object>();
			HashMap stackMap = new HashMap();
			stackMap.put("应办理", "1");
			stackMap.put("已办理", "2");
			stackMap.put("部分办理", "2");
			rowMap.put("STACK", stackMap);
			
			// 覆盖默认属性，显示图例
			HashMap<String, Object> charMap = new HashMap<String, Object>();
			charMap.put("showlegend", "1");
			domresult = ChartUtil.makeBarEChartJsonString(domresult, charMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	// 立项查询
		@Override
		public String queryLx(HttpServletRequest request) throws Exception {
			Connection conn = null;
			String domresult = "";
			String nd = request.getParameter("nd");
			try {
				conn = DBUtil.getConnection();
				PageManager page = new PageManager();
				String sql = Pub.getPropertiesString(propertyFileName, "lxList");
				//拼接年度查询条件
				sql=sql.replaceAll("%Condition%", nd);
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
		//立项手续情况-柱形图
		public String queryLxZxtColumn2d(HttpServletRequest request, String json) {
			Connection conn = null;
			String domresult = "";
			String nd = request.getParameter("nd");
			try {
				conn = DBUtil.getConnection();
				PageManager page = new PageManager();
				String sql = Pub.getPropertiesString(propertyFileName, "lxZxt");
				sql=sql.replaceAll("%Condition%", nd);
				//拼接年度查询条件
				
				page.setPageRows(1000);
				conn.setAutoCommit(false);
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				domresult = bs.getJson();
				HashMap<String, Object> rowMap = new HashMap<String, Object>();
				HashMap stackMap = new HashMap();
				stackMap.put("应办理", "1");
				stackMap.put("已办理", "2");
				stackMap.put("部分办理", "2");
				rowMap.put("STACK", stackMap);
				
				// 覆盖默认属性，显示图例
				HashMap<String, Object> charMap = new HashMap<String, Object>();
				charMap.put("showlegend", "1");
				domresult = ChartUtil.makeBarEChartJsonString(domresult, charMap, rowMap);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
		//土地查询
		@Override
		public String queryTd(HttpServletRequest request) throws Exception {
			Connection conn = null;
			String domresult = "";
			String nd = request.getParameter("nd");
			try {
				conn = DBUtil.getConnection();
				PageManager page = new PageManager();
				String sql = Pub.getPropertiesString(propertyFileName, "tdList");
				//拼接年度查询条件
				sql=sql.replaceAll("%Condition%", nd);
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
		//土地手续情况-柱形图
		public String queryTdZxtColumn2d(HttpServletRequest request, String json) {
			Connection conn = null;
			String domresult = "";
			String nd = request.getParameter("nd");
			try {
				conn = DBUtil.getConnection();
				PageManager page = new PageManager();
				String sql = Pub.getPropertiesString(propertyFileName, "tdZxt");
				sql=sql.replaceAll("%Condition%", nd);
				//拼接年度查询条件
				
				page.setPageRows(1000);
				conn.setAutoCommit(false);
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				domresult = bs.getJson();
				HashMap<String, Object> rowMap = new HashMap<String, Object>();
				HashMap stackMap = new HashMap();
				stackMap.put("应办理", "1");
				stackMap.put("已办理", "2");
				stackMap.put("部分办理", "2");
				rowMap.put("STACK", stackMap);
				
				// 覆盖默认属性，显示图例
				HashMap<String, Object> charMap = new HashMap<String, Object>();
				charMap.put("showlegend", "1");
				domresult = ChartUtil.makeBarEChartJsonString(domresult, charMap, rowMap);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
		
		//规划查询
		@Override
		public String queryGh(HttpServletRequest request) throws Exception {
			Connection conn = null;
			String domresult = "";
			String nd = request.getParameter("nd");
			try {
				conn = DBUtil.getConnection();
				PageManager page = new PageManager();
				String sql = Pub.getPropertiesString(propertyFileName, "ghList");
				//拼接年度查询条件
				sql=sql.replaceAll("%Condition%", nd);
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
		//规划手续情况-柱形图
		public String queryGhZxtColumn2d(HttpServletRequest request, String json) {
			Connection conn = null;
			String domresult = "";
			String nd = request.getParameter("nd");
			try {
				conn = DBUtil.getConnection();
				PageManager page = new PageManager();
				String sql = Pub.getPropertiesString(propertyFileName, "ghZxt");
				sql=sql.replaceAll("%Condition%", nd);
				//拼接年度查询条件
				
				page.setPageRows(1000);
				conn.setAutoCommit(false);
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				domresult = bs.getJson();
				HashMap<String, Object> rowMap = new HashMap<String, Object>();
				HashMap stackMap = new HashMap();
				stackMap.put("应办理", "1");
				stackMap.put("已办理", "2");
				stackMap.put("部分办理", "2");
				rowMap.put("STACK", stackMap);
				
				// 覆盖默认属性，显示图例
				HashMap<String, Object> charMap = new HashMap<String, Object>();
				charMap.put("showlegend", "1");
				domresult = ChartUtil.makeBarEChartJsonString(domresult, charMap, rowMap);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
		//查询时间
		public String queryDate(HttpServletRequest request) throws Exception {
			Connection conn = DBUtil.getConnection();
			String domresult = "";
			String nd=request.getParameter("nd");
			try {
				PageManager page=new PageManager();
				conn.setAutoCommit(false);
				String sql = "select " +
							"max(decode(LXSFBL,'1',LXKYBJSJ,'')) as lx_date," +
							"max(decode(GHSFBL,'1',GHSPBJSJ,'')) as gh_date," +
							"max(decode(TDSFBL,'1',TDSPBJSJ,'')) as td_date," +
							"max(decode(SGSFBL,'1',SGXKBJSJ,'')) as sg_date " +
							"from gc_zgb_qqsx qqsx,gc_jh_sj jhsj where qqsx.sfyx='1' and qqsx.jhsjid(+)=jhsj.gc_jh_sj_id and jhsj.nd='"+nd+"'";
				BaseResultSet bs = DBUtil.query(conn, sql,page);
				domresult = bs.getJson();
			} catch (Exception e) {
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
		//查询个数
		public String queryCount(User user,String nd) throws Exception {
			Connection conn = DBUtil.getConnection();
			String domresult = "";
			try {
				PageManager page=new PageManager();
				conn.setAutoCommit(false);
			String sql = "with jhsj as ("
					+ "select jh1.gc_jh_sj_id as jhsjid, "
					+ "jh1.nd, ISNOBDXM, xmbs, xmid, jh1.iskypf as mark,ISNOBDXM as BZ, "
					+ "jh1.iskypf || '&' || to_char(jh1.kypf_sj, 'yyyymmdd') as zt,"
					+ "'lx' as kind,kypf_sj as sj,kypf as jh, jh1.bdid,"
					+ "decode(fk.jhsjid,null,0,1) as fk, decode(cl.jhsjid,null,0,1) as cl "
					+ "from (select * from gc_jh_sj where sfyx='1') jh1,"
					+ "(select distinct jhsjid from gc_jh_fkqk where ywlx='070001' and sfyx='1') fk,"
					+ "(select * from gc_zgb_qqsx where sfyx='1' and jjsj is not null) cl "
					+ "where jh1.bdid is null and fk.jhsjid(+)=jh1.gc_jh_sj_id and cl.jhsjid(+)=jh1.gc_jh_sj_id "
					+ " union "
					+ "select jh1.gc_jh_sj_id as jhsjid, "
					+ "jh1.nd, ISNOBDXM, xmbs, xmid,jh1.isgcxkz as mark,ISNOBDXM as BZ, "
					+ "jh1.isgcxkz || '&' || to_char(jh1.gcxkz_sj, 'yyyymmdd') as zt,"
					+ "'gh' as kind,gcxkz_sj as sj,gcxkz as jh, jh1.bdid,"
					+ "decode(fk.jhsjid,null,0,1) as fk, decode(cl.jhsjid,null,0,1) as cl "
					+ "from (select * from gc_jh_sj where sfyx='1') jh1,"
					+ "(select distinct jhsjid from gc_jh_fkqk where ywlx='070001' and sfyx='1') fk,"
					+ "(select * from gc_zgb_qqsx where sfyx='1' and jjsj is not null) cl "
					+ "where jh1.bdid is null and fk.jhsjid(+)=jh1.gc_jh_sj_id and cl.jhsjid(+)=jh1.gc_jh_sj_id "
					+ " union "
					+ "select jh1.gc_jh_sj_id as jhsjid, "
					+ "jh1.nd, ISNOBDXM, xmbs, xmid,jh1.ishpjds as mark,ISNOBDXM as BZ, "
					+ "jh1.ishpjds || '&' || to_char(jh1.hpjds_sj, 'yyyymmdd') as zt,"
					+ "'td' as kind,hpjds_sj as sj,hpjds as jh, jh1.bdid,"
					+ "decode(fk.jhsjid,null,0,1) as fk,decode(cl.jhsjid,null,0,1) as cl "
					+ " from (select * from gc_jh_sj where sfyx='1') jh1,"
					+ "(select distinct jhsjid from gc_jh_fkqk where ywlx='070001' and sfyx='1') fk,"
					+ "(select * from gc_zgb_qqsx where sfyx='1' and jjsj is not null) cl "
					+ "where jh1.bdid is null and fk.jhsjid(+)=jh1.gc_jh_sj_id and cl.jhsjid(+)=jh1.gc_jh_sj_id "
					+ " union "
					+ "select jh1.gc_jh_sj_id as jhsjid, "
					+ "jh1.nd, ISNOBDXM, xmbs, xmid,jh1.issgxk as mark,nvl2(bdid,'1',ISNOBDXM) as BZ, "
					+ "jh1.issgxk || '&' || to_char(jh1.sgxk_sj, 'yyyymmdd') as zt,"
					+ "'sg' as kind,sgxk_sj as sj,sgxk as jh, jh1.bdid,"
					+ "decode(fk.jhsjid,null,0,1) as fk,decode(cl.jhsjid,null,0,1) as cl "
					+ "from (select * from gc_jh_sj where sfyx='1') jh1,"
					+ "(select distinct jhsjid from gc_jh_fkqk where ywlx='070001' and sfyx='1') fk,"
					+ "(select * from gc_zgb_qqsx where sfyx='1' and jjsj is not null) cl "
					+ "where "
					//"jh1.gc_jh_sj_id not in "
					//+ "(select gc_jh_sj_id from gc_jh_sj jh1 where jh1.xmbs = '0' and jh1.ISNOBDXM = '0') and " +
					+ "fk.jhsjid(+)=jh1.gc_jh_sj_id and cl.jhsjid(+)=jh1.gc_jh_sj_id),"
					+ " sjxx as ("
					+ "select "
					+ "count(distinct (case when jhsj.mark='1' and bdid is null then xmid end)) as xm_max, "
					+ "count(distinct (case when jhsj.mark='1' and bz='1' then jhsjid end)) as bd_max, "
					+ "count(distinct (case when jhsj.mark='1' and jhsj.zt='1&' and bdid is null then xmid end)) as xm_wbj, "
					+ "count(distinct (case when jhsj.mark='1' and jhsj.zt='1&' and bz='1' then jhsjid end)) as bd_wbj, "
					+ "count(distinct (case when jhsj.mark='1' and jh is not null and nvl(sj,sysdate)-jh>10 and bdid is null then xmid end)) as xm_tq, "
					+ "count(distinct (case when jhsj.mark='1' and jh is not null and nvl(sj,sysdate)-jh>10 and bz='1' then jhsjid end)) as bd_tq, "
					+ "count(distinct (case when mark='1' and kind='lx' then xmid end)) as lx_max,"
					+ "count(distinct (case when mark='1' and kind='lx' and zt!='1&' then xmid end)) as lx_wc, "
					+ "count(distinct (case when mark='1' and kind='gh' then xmid end)) as gh_max,"
					+ "count(distinct (case when mark='1' and kind='gh' and zt!='1&' then xmid end)) as gh_wc, "
					+ "count(distinct (case when mark='1' and kind='td' then xmid end)) as td_max,"
					+ "count(distinct (case when mark='1' and kind='td' and zt!='1&' then xmid end)) as td_wc, "
					+ "count(distinct (case when mark='1' and kind='sg' and bz='1' then jhsjid end)) as sg_max,"
					+ "count(distinct (case when mark='1' and kind='sg' and bz='1' and zt!='1&' then jhsjid end)) as sg_wc, "
					+ "count(distinct (case when bdid is null and fk='1' then xmid end)) as xm_fk, "
					+ "count(distinct (case when bz='1' and fk='1' then jhsjid end)) as bd_fk, "
					+ "count(distinct jhsj.jhsjid) as max,count(distinct decode(cl,1,jhsjid,null)) as cl "
					+ "from jhsj where jhsj.nd = '"
					+ nd
					+ "') "
					+ "select "
					+ "xm_max,bd_max, "
					+ "(xm_max-xm_wbj) as xm_bj,decode(xm_max,0,0,(round(100*(1-xm_wbj/xm_max),3))) as xm_bj_cent, "
					+ "(bd_max-bd_wbj) as bd_bj,decode(bd_max,0,0,(round(100*(1-bd_wbj/bd_max),3))) as bd_bj_cent, "
					+ "xm_fk,decode(xm_max,0,0,(round(100*(bd_fk/xm_max),3))) as xm_fk_cent, "
					+ "bd_fk,decode(bd_max,0,0,(round(100*(bd_fk/bd_max),3))) as bd_fk_cent, "
					+ "xm_tq,decode(xm_max,0,0,(round(100*(xm_tq/xm_max),3))) as xm_tq_cent, "
					+ "bd_tq,decode(bd_max,0,0,(round(100*(bd_tq/bd_max),3))) as bd_tq_cent, "
					+ "lx_wc as lx,decode(lx_max,0,0,(round(100*(lx_wc/lx_max),3))) as lx_cent, "
					+ "gh_wc as gh,decode(gh_max,0,0,(round(100*(gh_wc/gh_max),3))) as gh_cent, "
					+ "td_wc as td,decode(td_max,0,0,(round(100*(td_wc/td_max),3))) as td_cent, "
					+ "sg_wc as sg,decode(sg_max,0,0,(round(100*(sg_wc/sg_max),3))) as sg_cent, "
					+ "cl,decode(max,0,0,(round(100*(cl/max),3))) as cl_cent "
					+ "from sjxx";
							
				BaseResultSet bs = DBUtil.query(conn, sql,page);
				bs.setFieldDecimals("xm_bj_cent");
				bs.setFieldDecimals("bd_bj_cent");
				bs.setFieldDecimals("xm_fk_cent");
				bs.setFieldDecimals("bd_fk_cent");
				bs.setFieldDecimals("xm_tq_cent");
				bs.setFieldDecimals("bd_tq_cent");
				bs.setFieldDecimals("lx_cent");
				bs.setFieldDecimals("gh_cent");
				bs.setFieldDecimals("td_cent");
				bs.setFieldDecimals("sg_cent");
				bs.setFieldDecimals("cl_cent");
				domresult = bs.getJson();
			} catch (Exception e) {
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}

		@Override
		public String queryZtqk(HttpServletRequest request) {
			Connection conn = null;
			String domString = null;
			try {
				conn = DBUtil.getConnection();
				conn.setAutoCommit(false);
				PageManager page = new PageManager();
				String nd = request.getParameter("nd");
				String sql = Pub.getPropertiesString(propertyFileName, "QQ_ZTQK");
				sql=sql.replaceAll("%Condition%", nd);
				
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldThousand("ZTZ");
				bs.setFieldThousand("HTJHWCTZJE");
				domString = bs.getJson();
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domString;
		}


		@Override
		public String queryQqsx_DST1(HttpServletRequest request) throws Exception {
			Connection conn = null;
			String domresult = "";
			String nd = request.getParameter("nd");
			try {
				conn = DBUtil.getConnection();
				PageManager page = new PageManager();
				String sql = Pub.getPropertiesString(propertyFileName, "DST1");
				//拼接年度查询条件
				sql=sql.replaceAll("%Condition%", nd);
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
		public String queryQqsx_DST2(HttpServletRequest request) throws Exception {
			Connection conn = null;
			String domresult = "";
			String nd = request.getParameter("nd");
			try {
				conn = DBUtil.getConnection();
				PageManager page = new PageManager();
				String sql = Pub.getPropertiesString(propertyFileName, "DST2");
				//拼接年度查询条件
				sql=sql.replaceAll("%Condition%", nd);
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
}
