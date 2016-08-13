package com.ccthanking.business.gcgl.gcgk.service.impl;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.gcgl.gcgk.service.GcgkService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class GcgkServiceImpl implements GcgkService {
	private static String propertiesFileName = "com.ccthanking.properties.business.bmgk.gcb";
	
	//工程部总体概况
	@Override
	public String gcb_ztgk(String json,HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String nd = request.getParameter("nd");
		String jsonT="{querycondition: {conditions: [{} ]}}";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);			
			String time=Pub.getDate("yyyy-MM-dd", new Date());//当前日期
			String month=time.substring(5, 7);		
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    Calendar cal = Calendar.getInstance();  
		    String sqlnd="",sqlnd_xdk="",sqlnd_tcjh="",sqlnd_t="",sj="",lastday="",fristday="";
		    if(!Pub.empty(nd))
		    {
		    	sj=nd+time.substring(4,10);
		    	fristday=nd+"-"+month+"-"+"01";
		    	//计算出指定月份最后一天
		    	cal.set(Calendar.YEAR, Integer.parseInt(nd));  
		    	cal.set(Calendar.MONTH, Integer.parseInt(month)-1);     
		    	cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));
		    	lastday= df.format(cal.getTime());	
		    	sqlnd="and nd='"+nd+"' ";
		    	sqlnd_xdk="and xdk.nd='"+nd+"' ";
		    	sqlnd_tcjh="and tcjh.nd='"+nd+"' ";
		    	sqlnd_t="and t.nd='"+nd+"' ";
		    }
		    else
		    {
		    	sj=time;
		    	fristday=time.substring(0,4)+"-"+month+"-"+"01";
		    	//计算出指定月份最后一天
		    	cal.set(Calendar.YEAR, Integer.parseInt(time.substring(0,4)));  
		    	cal.set(Calendar.MONTH, Integer.parseInt(month)-1);     
		    	cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE)); 
		    	lastday= df.format(cal.getTime());		
		    }	
		    
			String sql="select "+
					//年度计划项目数
				"(select nvl(count(gc_jh_sj_id),0) from GC_JH_SJ where sfyx='1' and nd ='"+nd+"' and xmbs = '0') as sum_ndxm,"+
					//标段数
				"(select nvl(count(gc_jh_sj_id),0) from gc_jh_sj where sfyx='1' and nd ='"+nd+"' and (xmbs = '1' or ISNOBDXM = '1')) as sum_bd,"+ 
					//工程总投资
				"(select nvl(trunc(sum(JHZTZE)/ 100000000,2), '0') from GC_JH_SJ tcjh left join GC_TCJH_XMXDK xdk on tcjh.xmid=xdk.gc_tcjh_xmxdk_id where xdk.sfyx='1' and tcjh.sfyx='1' and tcjh.nd='"+nd+"') as ztz_sum,"+
					//已开工项目
				"(select nvl(count(gc_jh_sj_id),0) from GC_JH_SJ where sfyx='1' and xmbs = '0' and ((iskgsj = '1' and KGSJ_SJ is not null) or (iskgsj ='0')) and nd='"+nd+"') as sum_ykgxm,"+
					//已开工标段数
				"(select nvl(count(GC_JH_SJ_ID),0) from GC_JH_SJ where (xmbs = '1' or ISNOBDXM = '1') and sfyx='1' and ((iskgsj = '1' and KGSJ_SJ is not null) or (iskgsj ='0')) and nd ='"+nd+"') as sum_ykgbd,"+
					//已完工项目
				"(select nvl(count(gc_jh_sj_id),0) from GC_JH_SJ where sfyx='1' and xmbs = '0' and ((iswgsj = '1' and WGSJ_SJ is not null) or (iswgsj ='0')) and nd='"+nd+"') as sum_wgxm,"+
					//已完工标段数
				"(select nvl(count(GC_JH_SJ_ID),0) from GC_JH_SJ where (xmbs = '1' or ISNOBDXM = '1') and sfyx='1' and ((iswgsj = '1' and WGSJ_SJ is not null) or (iswgsj ='0')) and nd ='"+nd+"') as sum_wgbd,"+
					//未反馈项目
				"(select nvl(count(GC_JH_SJ_ID),0) from GC_JH_SJ where sfyx='1' and xmbs = '0' and iswgsj ='1' and WGSJ is not null and WGSJ_SJ is null and nd ='"+nd+"') as sum_wfkxm,"+
					//未反馈标段数
				"(select nvl(count(GC_JH_SJ_ID),0) from GC_JH_SJ where sfyx='1' and (xmbs = '1' or ISNOBDXM = '1') and iswgsj ='1' and WGSJ is not null and WGSJ_SJ is null and nd='"+nd+"') as sum_wfkbd,"+
					//进度拖延项目
				"(select nvl(count(GC_JH_SJ_ID),0) from gc_jh_sj t where nd='"+nd+"' and sfyx = '1' and xmbs = '0' and ((t.iskgsj = '1' and t.kgsj is not null and t.kgsj_sj is not null and (trunc(kgsj_sj) > trunc(kgsj))) or (t.iswgsj = '1' and t.wgsj is not null and t.wgsj_sj is not null and (trunc(wgsj_sj) > trunc(wgsj))))) as yzyqxm,"+
					//进度拖延标段数
				"(select nvl(count(GC_JH_SJ_ID),0) from gc_jh_sj t where nd='"+nd+"' and sfyx = '1' and (xmbs = '1' or ISNOBDXM = '1') and ((t.iskgsj = '1' and t.kgsj is not null and t.kgsj_sj is not null and (trunc(kgsj_sj) > trunc(kgsj))) or (t.iswgsj = '1' and t.wgsj is not null and t.wgsj_sj is not null and (trunc(wgsj_sj) > trunc(wgsj))))) as yzyqbd,"+
					//本年度完成合计
				"(SELECT nvl(SUM(jl.dyjlsdz),'0') FROM GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id = tcjh.gc_jh_sj_id WHERE jl.sfyx = '1' and tcjh.sfyx=1 and tcjh.nd = '"+nd+"' ) as bn_sum,"+
					//累计完成合计
				"(select nvl(sum(ljjlsdz),'0') from GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id=tcjh.gc_jh_sj_id where jl.sfyx=1 and tcjh.sfyx=1 and jlyf=(select max(jlyf) from GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id=tcjh.gc_jh_sj_id where tcjh.nd=(select max(tcjh.nd) from GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id=tcjh.gc_jh_sj_id)) ) as ljwchj_sum ,"+  
					//开工令办理项目数
				"(select nvl(count(gc_jh_sj_id),0) from GC_GCB_KFG,gc_jh_sj t1 where t1.nd = '"+nd+"' and t1.sfyx = '1' and kgfg = '0' and gc_jh_sj_id = TCJHSJID) as kgl_sum,"+
					//审批完成数
				"(select nvl(count(gc_jh_sj_id),0) from gc_gcb_kfg t2,gc_jh_sj t1,fs_event e where t1.gc_jh_sj_id = t2.tcjhsjid and t2.sfyx = '1' and t1.nd = '"+nd+"' and kgfg = '0' and e.sjbh = t2.sjbh and e.sjzt = '2') as kglsp_sum,"+
					//复工令办理项目数
				"(select nvl(count(gc_jh_sj_id),0) from GC_GCB_KFG,gc_jh_sj t1 where t1.nd = '"+nd+"' and t1.sfyx = '1' and kgfg = '1' and gc_jh_sj_id = TCJHSJID) as fgl_sum,"+
					//停工令办理项目数
				"(select nvl(count(gc_jh_sj_id),0) from GC_GCB_KFG,gc_jh_sj t1 where t1.nd = '"+nd+"' and t1.sfyx = '1' and kgfg = '2' and gc_jh_sj_id = TCJHSJID) as tgl_sum,"+
					//未办理项目数
				"(SELECT COUNT(gc_jh_sj_id) FROM GC_JH_SJ WHERE GC_JH_SJ_ID NOT IN(SELECT DISTINCT TCJHSJID FROM GC_GCB_KFG WHERE SFYX = '1') and nd='"+nd+"')as WBL_SUM,"+
					//已缴纳项目
				"(select nvl(COUNT(*),0) FROM gc_zjgl_lybzj gzl where gzl.sfyx ='1' and gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' and nd='" +nd+ "' and bdid is null)  ) as   yjnxm , "+
					//已缴纳标段数
				"(select nvl(COUNT(*),0) FROM gc_zjgl_lybzj gzl where gzl.sfyx ='1' and gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' and nd='" +nd+ "' and (xmbs = '1' or ISNOBDXM ='1') and sfyx='1')) as   yjnbd , "+
					//总额
				"(select nvl(sum(gzl.je),0) as yjnze FROM gc_zjgl_lybzj gzl where gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' and nd='" +nd+ "')) as yjnze,"+
					//已审批数
				"(select count(*) as ysp FROM gc_zjgl_lybzj gzl WHERE gzl.fhqk='2' and gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' and nd='" +nd+ "')) as ysp,"+
					//已返还
				"(select count(*) as yfh FROM gc_zjgl_lybzj gzl WHERE gzl.fhqk='6' and gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' and nd='" +nd+ "')) as yfh,"+
					//总额
				"(select nvl(sum(gzl.je),0) FROM gc_zjgl_lybzj gzl WHERE gzl.fhqk='6' and gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' and nd='" +nd+ "')) as yfhze,"+
					//保函失效项目数
				"(select count(*)  bhsxxm FROM gc_zjgl_lybzj gzl WHERE gzl.jnfs='BH' and  gzl.sfyx=1 AND gzl.bhyxqz <SYSDATE and gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' and nd='" +nd+ "')  ) as  bhsxxm  , "+
					//工程洽商总数
				"(select count(GC_GCGL_GCQS_ID) from GC_GCGL_GCQS qs,gc_jh_sj jh where qs.sfyx='1' and jh.nd='"+nd+"' and jh.gc_jh_sj_id = qs.jhsjid ) as  ytj_sum  , "+
					//本月工程洽商数
				"(select count(GC_GCGL_GCQS_ID) from GC_GCGL_GCQS gcqs,gc_jh_sj jh  where gcqs.sfyx='1' and to_char(gcqs.qstcrq,'yyyy-mm')='"+nd+"-"+month+"' and jh.nd='"+nd+"' and jh.gc_jh_sj_id = gcqs.jhsjid ) as  bytj_sum  , "+
				"(select to_char(max(kssj),'YYYY-MM-DD') max_kssj  from GC_XMGLGS_ZBB zb left join GC_TCJH_XMXDK xdk on xdk.gc_tcjh_xmxdk_id=zb.xdkid  where zb.sfyx='1' and xdk.sfyx='1' and xdk.nd=' "+nd+"' ) as   max_kssj , "+
				"(select to_char(max(jlyf),'YYYY-MM') jlyf_new from GC_XMGLGS_JLB where sfyx='1' and nd='"+nd+"'  ) as  jlyf_new "+
				" from dual ";
			BaseResultSet bs = DBUtil.query(conn, sql, null);
			bs.setFieldThousand("ZTZ_SUM");
			bs.setFieldThousand("BZHJ_SUM");
			bs.setFieldThousand("BYLJ_SUM");
			bs.setFieldThousand("BN_SUM");
			bs.setFieldThousand("LJWCHJ_SUM");
			bs.setFieldThousand("YJNZE");
			/*bs.setFieldThousand("YFH");*/
			bs.setFieldThousand("YFHZE");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	public String gcJdglLb(String json, HttpServletRequest request) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = RequestUtil.getPageManager(json);
			
			String nd = request.getParameter("nd");
			String xmlx = request.getParameter("xmlx");
			xmlx = Pub.empty(xmlx) ? "" : " and xdk.xmlx in ("+xmlx+") ";
			
			String sql = Pub.getPropertiesString(propertiesFileName, "GC_JDGL_LB");
			
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			sql = sql.replaceAll("%XMLX%", xmlx);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	
	public String gcGcjlzfTcgk(String json, HttpServletRequest request) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertiesFileName, "GC_GCJLZF_TCGK");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			sql = sql.replaceAll("%_nd%", nd);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	
	public String gcGcjlzfZtqk(String json, HttpServletRequest request) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			
			String sql = Pub.getPropertiesString(propertiesFileName, "GC_GCJLZF_ZTQK");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			sql = sql.replaceAll("%_nd%", nd);
			BaseResultSet bs = DBUtil.query(conn, sql, page);

			domString = bs.getJson();
			HashMap rowMap = new HashMap();
			rowMap.put("BREAKNUM", "2");
			rowMap.put("TOTALNUM", "4");
			List list = new ArrayList();
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor6);
			list.add(ChartUtil.chartColor3);
			list.add(ChartUtil.chartColor6);
			rowMap.put("COLOR", list);
			HashMap chartMap = new HashMap();
			chartMap.put("subCaption", "单位（亿元）");
			domString = ChartUtil.makeColumn2DChartJsonString(domString, chartMap, rowMap);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	
	public String gcGcglTcgk(String json, HttpServletRequest request) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertiesFileName, "GC_GCGL_TCGK1");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			List<String> list = new ArrayList<String>();
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			list.add(bs.getJson());
			sql = Pub.getPropertiesString(propertiesFileName, "GC_GCGL_TCGK2");
			sql = sql.replaceAll("%Condition%", ndSql);
			BaseResultSet bs2 = DBUtil.query(conn, sql, page);
			list.add(bs2.getJson());
			sql = Pub.getPropertiesString(propertiesFileName, "GC_GCGL_TCGK3");
			sql = sql.replaceAll("%Condition%", ndSql);
			BaseResultSet bs3 = DBUtil.query(conn, sql, page);
			list.add(bs3.getJson());
			sql = Pub.getPropertiesString(propertiesFileName, "GC_GCGL_TCGK4");
			sql = sql.replaceAll("%Condition%", ndSql);
			BaseResultSet bs4 = DBUtil.query(conn, sql, page);
			list.add(bs4.getJson());
			
			sql = Pub.getPropertiesString(propertiesFileName, "GC_GCGL_TCGK5");
			sql = sql.replaceAll("%Condition%", ndSql);
			BaseResultSet bs5 = DBUtil.query(conn, sql, page);
			list.add(bs5.getJson());

			sql = Pub.getPropertiesString(propertiesFileName, "GC_GCGL_TCGK6");
			sql = sql.replaceAll("%Condition%", ndSql);
			BaseResultSet bs6 = DBUtil.query(conn, sql, page);
			list.add(bs6.getJson());

			sql = Pub.getPropertiesString(propertiesFileName, "GC_GCGL_TCGK7");
			sql = sql.replaceAll("%Condition%", ndSql);
			BaseResultSet bs7 = DBUtil.query(conn, sql, page);
			list.add(bs7.getJson());
			
			domString = Pub.toBaseResultSetJsonString(list);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString.toString();
	}
	
	public String gcLybzjTcgk(String json, HttpServletRequest request) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertiesFileName, "GC_LYBZJ_TCGK");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and to_char(QS.JNRQ, 'YYYY')='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	
	public String gcGcsxTcgk(String json, HttpServletRequest request) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertiesFileName, "GC_GCSX_TCGK");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and to_char(qs.SXSQRQ,'yyyy')='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	
	public String gcGcqsTxfx(String json, HttpServletRequest request) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertiesFileName, "GC_GCQS_TXFX");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	public String gcGcqsTcgk(String json,HttpServletRequest request) throws Exception {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertiesFileName, "GC_GCQS_TCGK");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and to_char(qs.blrq,'yyyy')='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("GC_GCQS_TCGK_QZJE");
			bs.setFieldThousand("GC_GCQS_TCGK_SPTG_QZJE");
			
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}


	public String queryJdgl1(HttpServletRequest request) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertiesFileName, "GC_JDGL_PIE1");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			
			//AND (A.XMLX LIKE '%2%' OR A.XMLX LIKE '%4%' )
			String xmlx = request.getParameter("xmlx");
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
			sql = sql.replaceAll("%conditionXmlx%", xmlxSql);
			System.out.println(xmlxSql);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
			HashMap chartMap = new HashMap();
			chartMap.put("pieRadius", "70");
			chartMap.put("showLegend", "1");
			chartMap.put("showLabels", "0");
			HashMap rowMap = new HashMap();
			List list = new ArrayList();
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor1);
			rowMap.put("COLOR", list);
			domString = ChartUtil.makePieEChartJsonString(domString, chartMap, rowMap);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	public String queryJdgl2(HttpServletRequest request) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertiesFileName, "GC_JDGL_PIE2");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);

			String xmlx = request.getParameter("xmlx");
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
			sql = sql.replaceAll("%conditionXmlx%", xmlxSql);
			System.out.println(xmlxSql);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
			HashMap chartMap = new HashMap();
			chartMap.put("pieRadius", "70");
			chartMap.put("showLegend", "1");
			chartMap.put("showLabels", "0");
			HashMap rowMap = new HashMap();
			List list = new ArrayList();
			list.add(ChartUtil.chartColor2);
			list.add(ChartUtil.chartColor1);
			rowMap.put("COLOR", list);
			domString = ChartUtil.makePieEChartJsonString(domString, chartMap, rowMap);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	
	public String gcJdglTcgk(String json,HttpServletRequest request) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertiesFileName, "GC_JDGL_TCGK");
			
			String nd = request.getParameter("nd");
			String ndSql = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			sql = sql.replaceAll("%Condition%", ndSql);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	// 履约保证金列表
	public String gcLybzjLb(String json,HttpServletRequest request) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String sql = "select t.nd, count(t.gc_jh_sj_id) jhxms, "
					+ "(select count(sj.gc_jh_sj_id) from view_gc_jh_sj sj where sj.nd=t.nd and sj.js_sj is not null ) jvgs, "
					+ "(select count(l.id) from gc_zjgl_lybzj l where to_char(l.JNRQ, 'YYYY')=t.nd) sqs, "
					+ "(select count(l.id) from gc_zjgl_lybzj l where to_char(l.JNRQ, 'YYYY')=t.nd and l.fhqk='1') spz, "
					+ "(select count(l.id) from gc_zjgl_lybzj l where to_char(l.JNRQ, 'YYYY')=t.nd and (l.fhqk='2' or l.fhqk='6')) spw "
					+ "from view_gc_jh_sj t  group by t.nd order by t.nd";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
}
