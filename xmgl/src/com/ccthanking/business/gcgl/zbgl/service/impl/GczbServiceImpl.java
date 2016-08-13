package com.ccthanking.business.gcgl.zbgl.service.impl;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.gcgl.zbgl.service.GczbService;
import com.ccthanking.business.gcgl.zbgl.vo.GcZbbVO;
import com.ccthanking.business.gcgl.zbgl.vo.GsSjbVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class GczbServiceImpl implements GczbService {
	
	private String ywlx = YwlxManager.GC_XMGLGS_ZB;
	
	 
	 //首页查询，查询出最新周报
	@Override
	public String query(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				String orderFilter = RequestUtil.getOrderFilter(json);
				String xmid = request.getParameter("xmid");
				String bdid  = request.getParameter("bdid");
				String xmglgs = request.getParameter("xmglgs");
				if(!Pub.empty(xmid))
				{
					condition+=" and zb.xdkid='"+xmid+"'";
				}
				if(!Pub.empty(bdid))
				{
					condition+=" and zb.bdid='"+bdid+"' ";
				}		
				//condition +=" and zb.sfyx='1' ";
				if(!Pub.empty(xmglgs)){
					condition += " AND (/*tcjh.XMGLGS = '"+xmglgs+"' or*/ tcjh.gc_jh_sj_id in (select tt.gc_jh_sj_id from gc_jh_sj tt where /*tt.xmbs = '0' and*/ tt.xmid in (select xmid from gc_jh_sj j/* where j.xmglgs = '"+xmglgs+"'*/))) ";
				}
				
				condition += BusinessUtil.getSJYXCondition("tcjh");
				condition += BusinessUtil.getCommonCondition(user,null);
			    condition += orderFilter;
				if (page == null)
					page = new PageManager();
					page.setFilter(condition);
		
					conn.setAutoCommit(false);
					String sql =" select  tcjh.xmbh,tcjh.xmglgs,ISNOBDXM,tcjh.xmmc,xmbs,pxh,tcjh.bdmc,tcjh.bdbh,gc_xmglgs_zbb_id as XCZP ,gc_xmglgs_zbb_id as QTWJ, xdkid, bzjh, bzwc, bnwc,zb.bdid as zbdid, ljwc, xzjh, pqwcsj, pabzjz, cqwmc, cqwcsj, cqbzjz, zjlbz, zjlnd, "+
					         	" zjlljwc, qqwt,htzjwt, sjwt, zcwt, pqwt, fjid, note, zb.lrrq,gxmc, zb.ywlx, zb.sjbh, zb.bz, zb.sfyx as zsfyx,zb.jhsjid,"+
					         	" xmid, tcjh.bdid, kssj, jssj, gc_tcjh_xmxdk_id,  gc_jh_sj_id, xdk.sgdw,xdk.jldw,xdk.yzdb,xdk.JSRW,"+
					         	"(SELECT HTID FROM GC_HTGL_HTSJ WHERE htid in(select ID from GC_HTGL_HT where htlx = 'SG' and sfyx = '1' and rownum=1) and sfyx = '1' and jhsjid = tcjh.gc_jh_sj_id)  AS HTID,"+
					         	"(case tcjh.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = tcjh.nd and GC_TCJH_XMXDK_ID = tcjh.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = tcjh.bdid and rownum = 1) end ) as XMBDDZ " + 
					         	" from  GC_JH_SJ tcjh  left join"+
					         	" (select * from(select gc_xmglgs_zbb_id,xmmc,kssj,jssj, htid, xdkid, bdid, bzjh, bzwc, bnwc, ljwc, xzjh, gxmc,"+
					         	" pqwcsj, pabzjz, cqwmc, cqwcsj, cqbzjz, zjlbz, zjlnd, zjlljwc, qqwt, htzjwt, sjwt, zcwt, pqwt, fjid, note, "+
					         	"lrrq, ywlx, sjbh, bz,lrsj, sfyx, xmglgs, jhsjid, row_number() over ( partition by jhsjid order by kssj desc)as "+
					         	"row_flg from  gc_xmglgs_zbb where sfyx='1') where row_flg='1'   ) zb  on tcjh.gc_jh_sj_id = zb.jhsjid  left join "+
					         	" (select gc_tcjh_xmxdk_id, xmmc, xmglgs, sgdw, jldw, yzdb, JSRW  from gc_tcjh_xmxdk) xdk on xdk.gc_tcjh_xmxdk_id = tcjh.xmid" ;
					
					BaseResultSet bs = DBUtil.query(conn, sql, page);
					
					//金额
					bs.setFieldThousand("ZJLBZ");
					bs.setFieldThousand("ZJLND");
					bs.setFieldThousand("ZJLLJWC");
					bs.setFieldClob("LJWC");
					bs.setFieldClob("BZJH");
					bs.setFieldClob("BZWC");
					bs.setFieldClob("BNWC");
					bs.setFieldClob("XZJH");
					bs.setFieldClob("PABZJZ");
					bs.setFieldClob("CQBZJZ");
					bs.setFieldDic("XMLX", "XMLX");
					bs.setFieldFileUpload("XCZP", "0311");
					bs.setFieldFileUpload("QTWJ", "0310");
					bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
		            bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");// 施工单位
		            bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");// 监理单位
					bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//项目管理公司
					/*bs.setFieldDic("XMGLGS", "XMGLGS");*/
					//bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");
					
					
					domresult = bs.getJson();
					LogManager.writeUserLog(null, ywlx,
							Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
							user.getOrgDept().getDeptName() + " " + user.getName()
									+ "查询<工程周报信息>", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	/*	
	 * 确定是否存在周报
	 * 
	 */
	@Override
	public String query_zb(String json,User user,String xmid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				conn.setAutoCommit(false);
				String sql = "select GC_XMGLGS_ZBB_ID from GC_XMGLGS_ZBB where sfyx='1' and xdkid='"+xmid+"'";
				String result[][] = DBUtil.query(conn, sql);
				if(null!=result&&result.length>0&&!Pub.empty(result[0][0]))
				{					
					domresult = "1";
				}
				else
				{
					domresult = "0";
				}	
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//周报统计查询，弹出列表查询
	@Override
	public String query_tj(String json,User user,String xmid,String bdid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				String orderFilter = RequestUtil.getOrderFilter(json);
				if(!Pub.empty(xmid))
				{
					condition+=" and zb.xdkid='"+xmid+"'";
				}
				if(!Pub.empty(bdid))
				{
					condition+=" and zb.bdid='"+bdid+"' ";
				}		
				condition +=" and zb.sfyx='1' ";
				condition += BusinessUtil.getSJYXCondition("tcjh");
				condition += BusinessUtil.getCommonCondition(user,null);
			    condition += orderFilter;
				if (page == null)
					page = new PageManager();
					page.setFilter(condition);
		
					conn.setAutoCommit(false);
					String sql =" select gc_xmglgs_zbb_id as XCZP ,gc_xmglgs_zbb_id as QTWJ, xdkid, bzjh, bzwc, bnwc,zb.bdid as zbdid, ljwc, xzjh, pqwcsj, pabzjz, cqwmc, cqwcsj, cqbzjz, zjlbz, zjlnd, zjlljwc, qqwt,"+
					         	" htzjwt, sjwt, zcwt, pqwt, fjid, note, zb.lrrq,gxmc, zb.ywlx, zb.sjbh, zb.bz, zb.sfyx as zsfyx,zb.jhsjid,zb.htid,"+
					         	" xmid, tcjh.bdid, tcjh.bdmc, kfgrq, kssj, jssj, gc_tcjh_xmxdk_id,tcjh.xmbh, tcjh.bdbh, gc_jh_sj_id, "+
					         	" xdk.xmmc, xdk.xmdz, xdk.sgdw, xdk.jldw, xdk.yzdb,xdk.xmlx,xdk.jsrw, xdk.xmglgs "+
					         	" from  gc_xmglgs_zbb zb  left join GC_JH_SJ tcjh on tcjh.gc_jh_sj_id = zb.jhsjid  left join "+
					         	" (select gc_tcjh_xmxdk_id,xmlx, xmmc, xmdz, sgdw, jldw, yzdb,jsrw, xmglgs from gc_tcjh_xmxdk) xdk on xdk.gc_tcjh_xmxdk_id = tcjh.xmid";
					BaseResultSet bs = DBUtil.query(conn, sql, page);
					
					//金额
					bs.setFieldThousand("ZJLBZ");
					bs.setFieldThousand("ZJLND");
					bs.setFieldThousand("ZJLLJWC");
					bs.setFieldDic("XMLX", "XMLX");
					bs.setFieldClob("LJWC");
					bs.setFieldClob("BZJH");
					bs.setFieldClob("BZWC");
					bs.setFieldClob("BNWC");
					bs.setFieldClob("XZJH");
					bs.setFieldClob("PABZJZ");
					bs.setFieldClob("CQBZJZ");
					bs.setFieldFileUpload("XCZP", "0311");
					bs.setFieldFileUpload("QTWJ", "0310");
					bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
					/*bs.setFieldDic("XMGLGS", "XMGLGS");*/
					
					domresult = bs.getJson();
					LogManager.writeUserLog(null, ywlx,
							Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
							user.getOrgDept().getDeptName() + " " + user.getName()
									+ "查询<工程周报信息>", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	
	//项目管理公司概况列表
	@Override
	public String query_gk(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				String orderFilter = RequestUtil.getOrderFilter(json);
				String xmglgs = request.getParameter("xmglgs");
				if(!Pub.empty(xmglgs)){
					condition+=" and t1.xmglgs = '"+xmglgs+"' ";
				}
				String date = request.getParameter("time");
				String month ="";
				String year ="";
				if(Pub.empty(date)){
					date = Pub.getDate("yyyy-MM-dd", new Date());
					month = Pub.getDate("yyyy-MM", new Date());
					 year = Pub.getDate("yyyy", new Date());
				}else{
					month = date.substring(0,7);
					year =  date.substring(0, 4);
				}
				condition +=" and t1.gc_tcjh_xmxdk_id = t.xmid and t.nd = '"+year+"'";
				condition += BusinessUtil.getSJYXCondition("t");
				condition += BusinessUtil.getCommonCondition(user,"t");
			    condition += orderFilter;
				if (page == null)
					page = new PageManager();
					page.setFilter(condition);
		
					conn.setAutoCommit(false);
					String sql ="select t.xmbh,t.xmmc,t.bdmc, t.bdbh,t.bdid,"+
					"(select GC_XMGLGS_ZBB_ID from GC_XMGLGS_ZBB where " +
					"sfyx = '1' " +
					"and KSSJ < to_date('"+date+"', 'YYYY-MM-DD') "+
					"and JSSJ > to_date('"+date+"', 'YYYY-MM-DD') "+
					"and JHSJID = t.gc_jh_sj_id"+
					")as XCZP,"+
					"(select GC_XMGLGS_ZBB_ID from GC_XMGLGS_ZBB where "+
					"sfyx = '1' "+
					"and KSSJ < to_date('"+date+"', 'YYYY-MM-DD') "+
					"and JSSJ > to_date('"+date+"', 'YYYY-MM-DD') "+
					"and JHSJID = t.gc_jh_sj_id"+
					")as qtwj,"+
					"(select kssj from GC_XMGLGS_ZBB where sfyx = '1' "+
					"and KSSJ < to_date('"+date+"', 'YYYY-MM-DD') "+
					"and JSSJ > to_date('"+date+"', 'YYYY-MM-DD') "+
					"and JHSJID = t.gc_jh_sj_id"+
					")as kssj,"+
					"(select jssj from GC_XMGLGS_ZBB where sfyx = '1' "+
					"and KSSJ < to_date('"+date+"', 'YYYY-MM-DD') "+
					"and JSSJ > to_date('"+date+"', 'YYYY-MM-DD') "+
					"and JHSJID = t.gc_jh_sj_id"+
					")as jssj,"+
					"(select ZJLBZ from GC_XMGLGS_ZBB where sfyx = '1' "+
					"and KSSJ < to_date('"+date+"', 'YYYY-MM-DD') "+
					"and JSSJ > to_date('"+date+"', 'YYYY-MM-DD') "+
					"and JHSJID = t.gc_jh_sj_id"+
					")as ZJLBZ,"+
					"(select ZJLLJWC from GC_XMGLGS_ZBB where sfyx = '1' "+
					"and KSSJ < to_date('"+date+"', 'YYYY-MM-DD') "+
					"and JSSJ > to_date('"+date+"', 'YYYY-MM-DD') "+
					"and JHSJID = t.gc_jh_sj_id"+
					")as ZJLLJWC,"+
					"(select JLYF from GC_XMGLGS_JLB where sfyx = '1' "+
					"and TCJH_SJ_ID = t.gc_jh_sj_id "+
					"and jlyf = to_date('"+month+"','YYYY-MM') "+
					")as jlyf,"+
					"(select DYJLSDZ from GC_XMGLGS_JLB where sfyx = '1' "+
					"and TCJH_SJ_ID = t.gc_jh_sj_id "+
					"and jlyf = to_date('"+month+"','YYYY-MM') "+
					")as DYJLSDZ "+
					"from "+
					"gc_jh_sj t,gc_tcjh_xmxdk t1 ";
					BaseResultSet bs = DBUtil.query(conn, sql, page);
					//金额
					bs.setFieldThousand("ZJLBZ");
					bs.setFieldThousand("ZJLLJWC");
					bs.setFieldThousand("DYJLSDZ");
					bs.setFieldClob("LJWC");
					bs.setFieldClob("BZJH");
					bs.setFieldClob("BZWC");
					bs.setFieldClob("BNWC");
					bs.setFieldClob("XZJH");
					bs.setFieldClob("PABZJZ");
					bs.setFieldClob("CQBZJZ");
					bs.setFieldDateFormat("JLYF", "yyyy-MM");
					//bs.setFieldDic("XMLX", "XMLX");
					bs.setFieldFileUpload("XCZP", "0311");
					bs.setFieldFileUpload("QTWJ", "0310");
					//bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
					/*bs.setFieldDic("XMGLGS", "XMGLGS");*/
					//bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");
					
					
					domresult = bs.getJson();
					LogManager.writeUserLog(null, ywlx,
							Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
							user.getOrgDept().getDeptName() + " " + user.getName()
									+ "查询<工程周报和工程计量信息>", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	//项目管理公司概况列表(月度)
		@Override
		public String query_yd(String json,HttpServletRequest request) throws Exception {
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			Connection conn = DBUtil.getConnection();
			String domresult = "";
			try {
					PageManager page = RequestUtil.getPageManager(json);
					String condition = RequestUtil.getConditionList(json).getConditionWhere();
					String xmglgs = request.getParameter("xmglgs");
					String year =request.getParameter("year");
					if(!Pub.empty(year))
					{
						condition+=" and QKNF = '"+year+"' ";				
					}	
					if(!Pub.empty(xmglgs)){
						condition += " and SQDW = '"+xmglgs+"'";
					}
					condition += BusinessUtil.getSJYXCondition("t");
					condition += BusinessUtil.getCommonCondition(user,"t");
					if (page == null)
						page = new PageManager();
						page.setFilter(condition);
			
						conn.setAutoCommit(false);
						String sql ="SELECT (SELECT COUNT(*) FROM gc_zjgl_tqkbmmx gzt WHERE t.id = gzt.tqkid) AS bmjls,"+
							       "(SELECT sum(gzt.bcsq) FROM gc_zjgl_tqkbmmx gzt WHERE t.id = gzt.tqkid) AS bmze,"+
							       "(SELECT COUNT(*) FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx "+
							       "WHERE gzt.id = mx.bmmxid AND t.id = gzt.tqkid AND gzt.bmtqkmxzt = '6') AS cwjls,"+
							       "(SELECT sum(gzt.cwshz) FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx "+
							       "WHERE gzt.id = mx.bmmxid AND t.id = gzt.tqkid AND gzt.bmtqkmxzt = '6') AS cwze,"+
							       "(SELECT COUNT(*) FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx "+
							       "WHERE gzt.id = mx.bmmxid AND t.id = gzt.tqkid AND gzt.bmtqkmxzt = '7') AS jcjls, "+
							       "(SELECT sum(gzt.jchdz) FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx "+
							       "WHERE gzt.id = mx.bmmxid AND t.id = gzt.tqkid AND gzt.bmtqkmxzt = '7') AS jcze,"+
							       "t.* FROM gc_zjgl_tqkbm t";
						BaseResultSet bs = DBUtil.query(conn, sql, page);
						//金额
						bs.setFieldThousand("BMZE");
						bs.setFieldThousand("CWZE");
						bs.setFieldThousand("JCZE");
						bs.setFieldClob("LJWC");
						bs.setFieldClob("BZJH");
						bs.setFieldClob("BZWC");
						bs.setFieldClob("BNWC");
						bs.setFieldClob("XZJH");
						bs.setFieldClob("PABZJZ");
						bs.setFieldClob("CQBZJZ");
						bs.setFieldDic("TQKZT", "TQKZT");
						domresult = bs.getJson();
			}  catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
	
	//周报本周完成
	@Override
	public String query_bz(String json,User user,String year,String xmglgs) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				//String year = Pub.getDate("yyyy", new Date());
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				if(!Pub.empty(xmglgs)){
					condition+=" and zb.xmglgs = '"+xmglgs+"' ";
				}
				condition+=" and xdk.ND = '"+year+"'and zb.sfyx='1' and xdk.sfyx='1'";
				conn.setAutoCommit(false);
				String sql = "select sum(ZJLBZ) from GC_XMGLGS_ZBB zb left join GC_TCJH_XMXDK xdk on xdk.gc_tcjh_xmxdk_id=zb.xdkid where "+condition;			
				String result[][] = DBUtil.query(conn, sql);
				if(null!=result&&result.length>0&&!Pub.empty(result[0][0]))
				{		
					domresult =Pub.MoneyFormat(result[0][0]);
				}	
				else
				{
					domresult =Pub.MoneyFormat("0");
				}	

				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询项目管理公司概况", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	
	//本月累计完成合计
	@Override
	public String query_bylj(String json,User user,String time,String deptId) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				if(Pub.empty(time))
				{
					time=Pub.getDate("yyyy-MM-dd", new Date());
				}
				String xmglgs="";
				if(!Pub.empty(deptId)){
					xmglgs=" and xmglgs = '"+deptId+"' ";
				}
				String year=time.substring(0,4);		
				String month=time.substring(5, 7);
				String fristday=time.substring(0,8)+"01";
				String sql="";
				sql="select sum(dyjlsdz) sum, count(dyjlsdz) num from GC_XMGLGS_JLB t where jlyf=to_date('"+fristday+"','yyyy-mm-dd') and sfyx=1 "+xmglgs;
				String result[][] = DBUtil.query(conn, sql);
				if(Integer.parseInt(result[0][1])==0)
				{				
					//计算出指定月份最后一天
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();  
					cal.set(Calendar.YEAR, Integer.parseInt(year));     
					cal.set(Calendar.MONTH, Integer.parseInt(month)-1);     
					cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));  
					String lastday= df.format(cal.getTime()); 
					if(!Pub.empty(deptId)){
						xmglgs=" and zb.xmglgs = '"+deptId+"' ";
					}
					String condition ="zb.sfyx='1' "+xmglgs+"  and  xdk.ND = '"+year+"'  and xdk.sfyx='1'" +
							"  and KSSJ >= to_date('"+fristday+"', 'YYYY-MM-DD')  and KSSJ <= to_date('"+lastday+"', 'YYYY-MM-DD')";
					conn.setAutoCommit(false);
					sql = "select sum(ZJLBZ*10000) from GC_XMGLGS_ZBB zb left join GC_TCJH_XMXDK xdk on xdk.gc_tcjh_xmxdk_id=zb.xdkid where "+condition;			
					 result = DBUtil.query(conn, sql);
				}	
				if(null!=result&&result.length>0&&!Pub.empty(result[0][0]))
				{		
					domresult=Pub.MoneyFormat(result[0][0]);
					//domresult =Pub.NumberToThousand(result[0][0]);
				}	
				else
				{
					domresult =Pub.MoneyFormat("0");;
				}	

				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询项目管理公司概况", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	//质量安全部概况检查整改信息监控
	@Override
	public String fzr_ztxx(User user, String nd,String deptId) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String time=Pub.getDate("yyyy-MM-dd", new Date());
		String json="{querycondition: {conditions: [{} ]}}";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			OrgDept dept = user.getOrgDept();
			if(null == deptId){
				deptId = dept.getDeptID();
			}
			String sqlnd="",sqlnd_xdk="",sqlnd_tcjh="",sqlnd_bd="",sqlnd_t="",sqlnd_qknf="";
			if(!Pub.empty(nd))
			{
				sqlnd="and nd='"+nd+"' ";
				sqlnd_xdk="and xdk.nd='"+nd+"' ";
				sqlnd_tcjh="and tcjh.nd='"+nd+"' ";
				sqlnd_bd="and bd.nd='"+nd+"' ";
				sqlnd_t="and t.nd='"+nd+"' ";
				sqlnd_qknf=" and gzt.qknf='"+nd+"' ";
			}
			String sql="select "+
					" ( select  wmsys.wm_concat( to_char(name)) from FS_ORG_PERSON where account in(select fzr from VIEW_YW_ORG_DEPT where row_id='"+deptId+"')) as XMGS_FZR, "+
					" ( select  wmsys.wm_concat( to_char( name))from FS_ORG_PERSON where account in(select fzr_glgs  from GC_TCJH_XMXDK where  sfyx='1' and isnatc='1' "+sqlnd+"  and xmglgs='"+deptId+"')) as XM_FZR, "+
					" ( select nvl(count (distinct xmid),0) xm_sum from GC_JH_SJ tcjh left join GC_TCJH_XMXDK xdk on tcjh.xmid=xdk.gc_tcjh_xmxdk_id where xdk.sfyx='1' and tcjh.sfyx='1'  "+sqlnd_tcjh+"  and xdk.xmglgs='"+deptId+"' ) as XM_SUM, "+
					" ( select nvl(count (tcjh.gc_jh_sj_id),0) bd_sum from GC_JH_SJ tcjh  left join GC_TCJH_XMXDK xdk on tcjh.xmid = xdk.gc_tcjh_xmxdk_id where xdk.sfyx = '1' and tcjh.sfyx = '1' "+sqlnd_tcjh+"  and xdk.xmglgs = '"+deptId+"' and tcjh.gc_jh_sj_id not in (select gc_jh_sj_id from GC_JH_SJ where   xmbs = '0' and ISNOBDXM = '0' )) AS BD_SUM,"+  
					" ( select nvl(trunc(sum(JHZTZE)/ 100000000,2), '0') ztz_sum from GC_JH_SJ tcjh left join GC_TCJH_XMXDK xdk on tcjh.xmid=xdk.gc_tcjh_xmxdk_id where xdk.sfyx='1' and tcjh.sfyx='1'  "+sqlnd_tcjh+" and xdk.xmglgs='"+deptId+"' ) AS ZTZ_SUM, "+  				
					" ( select nvl(count(tcjh.gc_jh_sj_id), 0) as kgxm_sum from GC_JH_SJ tcjh left join GC_TCJH_XMXDK xdk on tcjh.xmid = xdk.gc_tcjh_xmxdk_id where tcjh.sfyx = '1' and xdk.sfyx='1' and bdid is null  AND KGSJ_SJ IS NOT NULL   "+sqlnd_tcjh+" and xdk.xmglgs = '"+deptId+"') as kgxm_sum, "+
					" ( select nvl(count(GC_JH_SJ_ID), 0) as kgbd_sum from GC_JH_SJ  tcjh left join GC_TCJH_XMXDK xdk on tcjh.xmid = xdk.gc_tcjh_xmxdk_id where gc_jh_sj_id not in (select gc_jh_sj_id from gc_jh_sj where xmbs = '0' and ISNOBDXM = '0') and tcjh.sfyx = '1' and xdk.sfyx='1'  AND KGSJ_SJ IS NOT NULL  "+sqlnd_tcjh+"   and tcjh.xmglgs = '"+deptId+"') as kgbd_sum, "+   				
					" ( select nvl(count(distinct GC_JH_SJ_ID), 0) as wgxm_sum  from GC_JH_SJ tcjh left join GC_TCJH_XMXDK xdk on tcjh.xmid = xdk.gc_tcjh_xmxdk_id  where tcjh.sfyx = '1'and xdk.sfyx='1' and bdid is null and WGSJ_SJ is not null  "+sqlnd_tcjh+"  and xdk.xmglgs = '"+deptId+"') as wgxm_sum ,"+ 
					" ( select nvl(count(GC_JH_SJ_ID), 0) wgbd_sum  from GC_JH_SJ  tcjh left join GC_TCJH_XMXDK xdk on tcjh.xmid = xdk.gc_tcjh_xmxdk_id  where gc_jh_sj_id not in (select gc_jh_sj_id from gc_jh_sj  where xmbs = '0' and ISNOBDXM = '0'and sfyx = '1') and tcjh.sfyx = '1'and xdk.sfyx='1' and WGSJ_SJ is not null  "+sqlnd_tcjh+" and tcjh.xmglgs = '"+deptId+"') as wgbd_sum ,"+  
					" ( select nvl(count(xmid), 0) wfkxm_sum from GC_JH_SJ  tcjh left join GC_TCJH_XMXDK xdk on tcjh.xmid = xdk.gc_tcjh_xmxdk_id   where tcjh.sfyx = '1' and xdk.sfyx='1'   and bdid is null  and WGSJ is not null  and WGSJ_SJ is null   "+sqlnd_tcjh+"  and xdk.xmglgs = '"+deptId+"' ) as wfkxm_sum ,"+
					" ( select nvl(count(GC_JH_SJ_ID), 0) wfkbd_sum from GC_JH_SJ tcjh left join GC_TCJH_XMXDK xdk on tcjh.xmid = xdk.gc_tcjh_xmxdk_id  where tcjh.sfyx = '1' and  xdk.sfyx='1' and gc_jh_sj_id not in (select gc_jh_sj_id from gc_jh_sj  where xmbs = '0'  and ISNOBDXM = '0' and sfyx = '1') and WGSJ is not null and WGSJ_SJ is null  "+sqlnd_tcjh+" and tcjh.xmglgs = '"+deptId+"') as wfkbd_sum, " +  				
					
					
					" ( select nvl(COUNT( distinct(GC_JH_SJ_ID)), 0) AS yzyqxm from ( SELECT GC_JH_SJ_ID,kgsj,kgsj_sj,wgsj,wgsj_sj,trunc(sysdate) as now  FROM GC_JH_SJ tcjh LEFT JOIN GC_TCJH_XMXDK xdk ON tcjh.xmid = xdk.gc_tcjh_xmxdk_id  WHERE tcjh.KGSJ IS NOT NULL AND tcjh.wgsj IS NOT NULL AND bdid IS NULL  "+sqlnd_tcjh+"  AND tcjh.sfyx = '1' AND xdk.sfyx = '1' AND tcjh.xmglgs = '"+deptId+"' ) tcjh "+
					"  where tcjh.kgsj_sj IS  NULL AND tcjh.wgsj_sj IS NULL AND (trunc(SYSDATE)>tcjh.KGSJ  OR trunc(SYSDATE)>tcjh.wgsj) or ( tcjh.kgsj_sj IS not NULL AND tcjh.wgsj_sj IS NULL AND (tcjh.kgsj_sj>tcjh.KGSJ  OR trunc(SYSDATE)>tcjh.wgsj) ) or( tcjh.kgsj_sj IS  NULL AND tcjh.wgsj_sj IS not NULL AND (trunc(SYSDATE)>tcjh.KGSJ  OR tcjh.wgsj_sj>tcjh.wgsj)) or( tcjh.kgsj_sj IS not NULL AND tcjh.wgsj_sj IS not NULL AND (tcjh.kgsj_sj>tcjh.KGSJ  OR tcjh.wgsj_sj>tcjh.wgsj)) ) as yzyqxm ,"+

					" ( select nvl(COUNT( distinct(GC_JH_SJ_ID)), 0) yzyqbd  from( SELECT GC_JH_SJ_ID,kgsj,kgsj_sj,wgsj,wgsj_sj,trunc(sysdate) as now FROM GC_JH_SJ tcjh LEFT JOIN GC_TCJH_XMXDK xdk  ON tcjh.xmid = xdk.gc_tcjh_xmxdk_id WHERE tcjh.sfyx = '1' "+sqlnd_tcjh+" and xdk.sfyx = '1' AND tcjh.xmglgs = '"+deptId+"' AND tcjh.KGSJ IS NOT NULL AND tcjh.wgsj IS NOT NULL AND gc_jh_sj_id NOT IN (SELECT gc_jh_sj_id FROM gc_jh_sj WHERE xmbs = '0'  AND ISNOBDXM = '0' AND sfyx = '1') ) tcjh "+
					"  where tcjh.kgsj_sj IS  NULL AND tcjh.wgsj_sj IS NULL AND (trunc(SYSDATE)>tcjh.KGSJ  OR trunc(SYSDATE)>tcjh.wgsj)or( tcjh.kgsj_sj IS not NULL AND tcjh.wgsj_sj IS NULL AND (tcjh.kgsj_sj>tcjh.KGSJ  OR trunc(SYSDATE)>tcjh.wgsj) )or( tcjh.kgsj_sj IS  NULL AND tcjh.wgsj_sj IS not NULL AND (trunc(SYSDATE)>tcjh.KGSJ  OR tcjh.wgsj_sj>tcjh.wgsj)) or( tcjh.kgsj_sj IS not NULL AND tcjh.wgsj_sj IS not NULL AND (tcjh.kgsj_sj>tcjh.KGSJ  OR tcjh.wgsj_sj>tcjh.wgsj)) ) as yzyqbd ,"+  
				
					"( SELECT nvl(SUM(jl.dyjlsdz), '0') AS bn_sum	FROM GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id = tcjh.gc_jh_sj_id WHERE jl.sfyx = '1'and tcjh.sfyx=1 "+sqlnd_tcjh+"  AND jl.xmglgs = '"+deptId+"')  as bn_sum ,"+
					" ( select nvl(sum(ljjlsdz),'0') as LJWC_SUM from GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id=tcjh.gc_jh_sj_id where jl.sfyx=1 and tcjh.sfyx=1 and jlyf=(select max(jlyf) from GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id=tcjh.gc_jh_sj_id where tcjh.nd=(select max(tcjh.nd) from GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id=tcjh.gc_jh_sj_id)) and tcjh.xmglgs = '"+deptId+"' ) as LJWC_SUM ,"+  
					" ( select count(tcjh.gc_jh_sj_id) kgl_sum  from GC_GCB_KFG kfg  left join GC_JH_SJ tcjh  on kfg.tcjhsjid = tcjh.gc_jh_sj_id  left join GC_TCJH_XMXDK xdk on kfg.xdkid=xdk.gc_tcjh_xmxdk_id  where xdk.sfyx='1' and kfg.sfyx='1' and tcjh.sfyx='1'and kgfg='0'  "+sqlnd_tcjh+" and tcjh.xmglgs='"+deptId+"' ) as kgl_sum, "+  
					" ( select count(tcjh.gc_jh_sj_id) kgl_sum  from GC_GCB_KFG kfg  left join GC_JH_SJ tcjh  on kfg.tcjhsjid = tcjh.gc_jh_sj_id  left join GC_TCJH_XMXDK xdk on kfg.xdkid=xdk.gc_tcjh_xmxdk_id left join FS_EVENT  event on kfg.sjbh=event.sjbh  where xdk.sfyx='1' and sjzt='2' and kfg.sfyx='1' and tcjh.sfyx='1'and kgfg='0'   "+sqlnd_tcjh+" and tcjh.xmglgs='"+deptId+"' ) as kglsp_sum, "+  
					" ( select count(tcjh.gc_jh_sj_id) kgl_sum  from GC_GCB_KFG kfg  left join GC_JH_SJ tcjh  on kfg.tcjhsjid = tcjh.gc_jh_sj_id  left join GC_TCJH_XMXDK xdk on kfg.xdkid=xdk.gc_tcjh_xmxdk_id  where xdk.sfyx='1' and kfg.sfyx='1' and tcjh.sfyx='1'and kgfg='1'  "+sqlnd_tcjh+" and tcjh.xmglgs='"+deptId+"' ) as fgl_sum, "+    
					" ( select count(tcjh.gc_jh_sj_id) kgl_sum  from GC_GCB_KFG kfg  left join GC_JH_SJ tcjh  on kfg.tcjhsjid = tcjh.gc_jh_sj_id  left join GC_TCJH_XMXDK xdk on kfg.xdkid=xdk.gc_tcjh_xmxdk_id  where xdk.sfyx='1' and kfg.sfyx='1' and tcjh.sfyx='1'and kgfg='2'  "+sqlnd_tcjh+" and tcjh.xmglgs='"+deptId+"') as tgl_sum ,"+
										
					" ( select to_char(max(kssj),'YYYY-MM-DD') max_kssj  from GC_XMGLGS_ZBB zb left join GC_TCJH_XMXDK xdk on xdk.gc_tcjh_xmxdk_id=zb.xdkid where rownum <= '1000' and zb.xmglgs='"+deptId+"'  and zb.sfyx='1' and xdk.sfyx='1'  "+sqlnd_xdk+" ) as max_kssj, "+
					" ( select to_char(max(jlyf),'YYYY-MM') jlyf from GC_XMGLGS_JLB where sfyx='1' and xmglgs='"+deptId+"'   "+sqlnd+" ) as jlyf, "+
					" ( select COUNT(*) ytqk_sum FROM gc_zjgl_tqkbm gzt WHERE gzt.sqdw='"+deptId+"'"+ sqlnd_qknf+"  and gzt.sfyx = '1'  ) as ytqk_sum, "+
					" ( select  COUNT(distinct gzt2.ID) as spwc_sum  FROM gc_zjgl_tqk gzt2, gc_zjgl_tqkmx gzt3, gc_zjgl_tqkbmmx gzt1, gc_zjgl_tqkbm gzt WHERE gzt2.id = gzt3.tqkid AND gzt3.bmmxid = gzt1.id AND gzt1.tqkid=gzt.id  AND gzt2.tqkzt = 6   AND gzt.sqdw='"+deptId+"'   "+sqlnd_qknf+" and gzt.sfyx = '1'  ) as spwc_sum, "+
					" ( select  COUNT(distinct gzt2.ID) as ybf_sum FROM gc_zjgl_tqk gzt2, gc_zjgl_tqkmx gzt3, gc_zjgl_tqkbmmx gzt1, gc_zjgl_tqkbm gzt WHERE gzt2.id = gzt3.tqkid AND gzt3.bmmxid = gzt1.id AND gzt1.tqkid=gzt.id AND gzt2.tqkzt = 7  AND gzt.sqdw='"+deptId+"'   "+sqlnd_qknf+" and gzt.sfyx = '1'  ) as ybf_sum, "+
					" ( select nvl(sum(gzt2.bcsq) ,'0') as tqkze FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2 WHERE gzt.id=gzt2.tqkid AND  gzt.sqdw='"+deptId+"'  "+sqlnd_qknf+" and gzt.sfyx = '1'  ) as tqkze, "+
					" ( select nvl(sum(gzt2.bcsq) ,'0') as spwcje FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2 WHERE gzt.id=gzt2.tqkid AND gzt2.bmtqkmxzt='6' and gzt.sqdw='"+deptId+"'   "+sqlnd_qknf+" and gzt.sfyx = '1'  ) as spwcje, "+
					" ( select nvl(sum(gzt2.cwshz) ,'0') as cwspje FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2 WHERE gzt.id=gzt2.tqkid AND gzt2.bmtqkmxzt='6' and gzt.sqdw='"+deptId+"'   "+sqlnd_qknf+" and gzt.sfyx = '1'  ) as cwspje, "+
					" ( select nvl(sum(gzt2.csz) ,'0') as sjsde FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2 WHERE gzt.id=gzt2.tqkid AND gzt2.bmtqkmxzt='6' and gzt.sqdw='"+deptId+"'   "+sqlnd_qknf+" and gzt.sfyx = '1'  ) as sjsde, "+
					" ( select nvl(sum(gzt2.jchdz) ,'0') as bfze FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2 WHERE gzt.id=gzt2.tqkid AND gzt2.bmtqkmxzt='7' and gzt.sqdw='"+deptId+"'   "+sqlnd_qknf+" and gzt.sfyx = '1'  ) as bfze, "+
					" ( select count(GC_GCGL_GCQS_ID) ytj_sum from GC_GCGL_GCQS gcqs  where sfyx='1'  "+sqlnd+" and xmglgs='"+deptId+"'   ) as ytj_sum, "+
					//" ( select count(GC_GCGL_GCQS_ID) ysp_sum from GC_GCGL_GCQS gcqs left join fs_event event  on gcqs.sjbh=event.sjbh  where sfyx='1' and sjzt='7'  "+sqlnd+" and xmglgs='"+deptId+"'   ) as ysp_sum, "+
					" ( select count(GC_GCGL_GCQS_ID) bytj_sum from GC_GCGL_GCQS gcqs  where sfyx='1' and to_char(gcqs.qstcrq,'yyyy-mm')='"+time.substring(0, 7)+"'"+sqlnd+" and xmglgs='"+deptId+"'   ) as bytj_sum, "+
					//" ( select count(GC_GCGL_GCQS_ID) byysp_sum from GC_GCGL_GCQS gcqs left join fs_event event  on gcqs.sjbh=event.sjbh  where sfyx='1' and sjzt='7' and to_char(gcqs.qstcrq,'yyyy-mm')='2013-11'  "+sqlnd+" and xmglgs='"+deptId+"'   ) as byysp_sum, "+
					" ( select COUNT(GC_ZLAQ_JCB_ID)AS zgtz_num FROM GC_ZLAQ_JCB WHERE ZT >1  and xmglgs='"+deptId+"'  "+sqlnd+"  AND SFYX = '1'  ) as zgtz_num, "+
					" ( select COUNT(GC_ZLAQ_JCB_ID)AS zghf_num FROM GC_ZLAQ_JCB WHERE ZT>3 and xmglgs='"+deptId+"' "+sqlnd+"  AND SFYX = '1'  ) as zghf_num, "+
					" ( select COUNT(GC_ZLAQ_JCB_ID)AS zgfc_num FROM GC_ZLAQ_JCB jcb left join GC_ZLAQ_ZGB zgb  on jcb.gc_zlaq_jcb_id=zgb.jcbid WHERE ZT=5 and zgb.sfyx='1' and  fcjl='1'  and xmglgs='"+deptId+"' "+sqlnd+"  AND jcb.SFYX = '1'  ) as zgfc_num, "+
					" ( select COUNT(gc_zlaq_zgb_id) as zgcq_num from (select gc_zlaq_zgb_id, jcbid, xgrq, sfyx  from  (select gc_zlaq_zgb_id,jcbid, xgrq,sfyx, row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t where sfyx = '1') temp where temp.row_flg = '1') zgb left join GC_ZLAQ_JCB jcb on gc_zlaq_jcb_id = jcbid where  xgrq < (trunc(SYSDATE)) and zt in(2,3) and jcb.sfyx = '1' and zt>1  and xmglgs='"+deptId+"'  and zgb.sfyx = '1'  "+sqlnd+"   ) as zgcq_num  "+
					" from dual ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);	
			bs.setFieldThousand("ZTZ_SUM");
			bs.setFieldThousand("BN_SUM");
			bs.setFieldThousand("LJWC_SUM");
			bs.setFieldThousand("TQKZE");
			bs.setFieldThousand("SPWCJE");
			bs.setFieldThousand("CWSPJE");
			bs.setFieldThousand("SJSDE");
			bs.setFieldThousand("BFZE");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	//整改最多的项目(TOP5)
	@Override
	public String zgxm_xm_top(User user, String nd,String deptId) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			conn.setAutoCommit(false);
			OrgDept dept = user.getOrgDept();
			if(null == deptId){
				deptId = dept.getDeptID();
			}	
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd="and nd='"+nd+"' ";
			}
			String sql="select  rownum||'、'||xmmc as xmmc , '[' || sum_xm || ']' || '次' num_xm from (select distinct (tj.GC_JH_SJ_ID), xmmc, sum_xm "+
					   " from (select count(tcjh.GC_JH_SJ_ID) sum_xm, tcjh.GC_JH_SJ_ID from GC_ZLAQ_JCB jcb left join gc_jh_sj tcjh on jcb.gc_jh_sj_id = tcjh.gc_jh_sj_id  where tcjh.bdid is null  group by tcjh.GC_JH_SJ_ID) tj "+
					   " left join GC_ZLAQ_JCB jcb on tj.GC_JH_SJ_ID = jcb.GC_JH_SJ_ID  where sfyx = '1'  and xmglgs = '"+deptId+"'  "+sqlnd+" order by sum_xm desc) where rownum<=5";
			/*String sql="select * from ( select rownum||'、'||xmmc||nvl2(bdmc,'/'||bdmc,'') mc_js,'['||sum||']'|| '次' num_js,  mod(rownum, 2) rnum from(select distinct (tj.GC_JH_SJ_ID), xmmc, bdmc, sum from (select count(GC_JH_SJ_ID) sum, GC_JH_SJ_ID from GC_ZLAQ_JCB group by GC_JH_SJ_ID) tj " +
					"left join GC_ZLAQ_JCB jcb on tj.GC_JH_SJ_ID = jcb.GC_JH_SJ_ID where sfyx = '1'  and xmglgs='"+deptId+"'  "+sqlnd+" order by sum desc) where rownum <=10"+
					"  )where rnum = 1";*/
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	//整改最多的标段(TOP5)
	@Override
	public String zgxm_bd_top(User user, String nd,String deptId) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
		//设置每页最多可以显示多少条数据
		//String json="{querycondition: {conditions: [{} ]},pages:{recordsperpage:100, currentpagenum:1, totalpage:0, countrows:0}}";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			conn.setAutoCommit(false);
			OrgDept dept = user.getOrgDept();
			if(null == deptId){
				deptId = dept.getDeptID();
			}	
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd="and nd='"+nd+"' ";
			}
			String sql ="select  rownum||'、'||nvl2(bdmc, nvl2(bdbh, bdmc || '—' || bdbh, bdmc), xmmc) bdmc,  '[' || sum_bd || ']' || '次' num_bd from (select distinct (tj.GC_JH_SJ_ID), xmmc, bdmc, bdbh,  sum_bd "+
						" from (select count(jcb.GC_JH_SJ_ID) sum_bd, jcb.GC_JH_SJ_ID  from GC_ZLAQ_JCB jcb  where jcb.gc_jh_sj_id not in   (select gc_jh_sj_id  from gc_jh_sj where xmbs = '0'and ISNOBDXM = '0'  and sfyx = 1)  and jcb.sfyx = 1 group by jcb.GC_JH_SJ_ID) tj"+
						" left join GC_ZLAQ_JCB jcb on tj.GC_JH_SJ_ID = jcb.GC_JH_SJ_ID left join gc_jh_sj tcjh  on jcb.gc_jh_sj_id = tcjh.gc_jh_sj_id "+
						" where jcb.sfyx = '1'  and tcjh.sfyx = 1 and xmglgs = '"+deptId+"'"+sqlnd+" order by sum_bd desc ) where rownum<=5 ";
			/*String sql="select * from ( select rownum||'、'||xmmc||nvl2(bdmc,'/'||bdmc,'') mc_os,'['||sum||']'|| '次' num_os,  mod(rownum, 2) rnum from(select distinct (tj.GC_JH_SJ_ID), xmmc, bdmc, sum from (select count(GC_JH_SJ_ID) sum, GC_JH_SJ_ID from GC_ZLAQ_JCB group by GC_JH_SJ_ID) tj " +
					"left join GC_ZLAQ_JCB jcb on tj.GC_JH_SJ_ID = jcb.GC_JH_SJ_ID where sfyx = '1'  and xmglgs='"+deptId+"'  "+sqlnd+" order by sum desc) where rownum <=10"+
					"  )where rnum = 0";*/
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	//招标需求表格
	@Override
	public String zbxq_bg(User user, String nd,String deptId) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			OrgDept dept = user.getOrgDept();
			if(null == deptId){
				deptId = dept.getDeptID();
			}
			StringBuffer sbSql = new StringBuffer();
			String sqlnd="",depStr="";
			if(!Pub.empty(nd))
			{
				sqlnd=" and to_char(gxsj, 'yyyy')='"+nd+"' ";
			}
            if(!Pub.empty(deptId)){
				depStr = "  and gxbm='"+deptId+"'  ";
			}else{
				depStr = "";
			}
      		sbSql.append("SELECT DISTINCT '施工招标' as xmglgs,(select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ where zblx = '13'");
      		sbSql.append(depStr+sqlnd+"  and xqzt = '2' and sfyx = '1') tq,");
      		sbSql.append("(select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ where zblx = '13' ");
      		sbSql.append(depStr+sqlnd+"  and xqzt = '3' and sfyx = '1') ysp,");
      		sbSql.append("(select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ where zblx = '13' ");
      		sbSql.append(depStr+sqlnd+"  and xqzt = '6' and sfyx = '1') ywc ");
      		sbSql.append("from dual ");
      		sbSql.append("UNION ALL ");
      		sbSql.append("SELECT DISTINCT '监理招标' as xmglgs, ");
      		sbSql.append("(select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ where zblx = '12' ");
      		sbSql.append(depStr+sqlnd+"  and xqzt = '2' and sfyx = '1') tq,");
      		sbSql.append("(select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ  where zblx = '12' ");
      		sbSql.append(depStr+sqlnd+"  and xqzt = '3' and sfyx = '1') ysp,");
      		sbSql.append("(select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ where zblx = '12' ");
      		sbSql.append(depStr+sqlnd+"  and xqzt = '6' and sfyx = '1') ywc ");
      		sbSql.append("from dual ");
      		sbSql.append("UNION ALL ");
      		sbSql.append("SELECT DISTINCT '其他招标' as xmglgs, ");
      		sbSql.append("(select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ where zblx = '18' ");
      		sbSql.append(depStr+sqlnd+"  and xqzt = '2' and sfyx = '1') tq,");
      		sbSql.append("(select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ  where zblx = '18' ");
      		sbSql.append(depStr+sqlnd+"  and xqzt = '3' and sfyx = '1') ysp,");
      		sbSql.append("(select count(GC_ZTB_XQ_ID) from GC_ZTB_XQ where zblx = '18' ");
      		sbSql.append(depStr+sqlnd+"  and xqzt = '6' and sfyx = '1') ywc ");
      		sbSql.append("from dual ");
      		String sql = sbSql.toString();
      		sql=sql+"  UNION ALL select  DISTINCT '总计' as zj , sum (tq) ,sum(ysp),sum(ywc) from( "+sql+")";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	//项目合同表格
	@Override
	public String xmht_bg(User user, String nd,String deptId) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			OrgDept dept = user.getOrgDept();
			if(null == deptId){
				deptId = dept.getDeptID();
			}
			StringBuffer sbSql = new StringBuffer();
			String sqlnd="",depStr="";
			if(!Pub.empty(nd))
			{
				sqlnd=" and nd='"+nd+"' ";
			}
            if(!Pub.empty(deptId)){
				depStr = "  and xmglgs='"+deptId+"'  ";
			}else{
				depStr = "";
			}
      		sbSql.append("SELECT '施工合同'as xmglgs,nvl(COUNT(ID),'0') as hts,nvl(SUM(HTQDJ),'0') as htqdj,nvl(SUM(ZXHTJ),'0') as zxhtj, ");
      		sbSql.append("nvl(SUM(HTZF),'0') as htzf,nvl(SUM(WCZF),'0') as wczf FROM GC_HTGL_HTSJ ");
      		sbSql.append("WHERE HTID IN(SELECT ID FROM GC_HTGL_HT WHERE HTLX = 'SG' AND SFYX = '1') ");
      		sbSql.append("AND JHSJID IN(SELECT GC_JH_SJ_ID FROM GC_JH_SJ WHERE SFYX = '1'  "+sqlnd);
      		sbSql.append("AND XMID IN(SELECT GC_TCJH_XMXDK_ID FROM GC_TCJH_XMXDK WHERE SFYX = '1'"+depStr+"))");
      		sbSql.append("AND SFYX = '1' ");
      		sbSql.append("UNION ALL ");
      		sbSql.append("SELECT '监理合同'as xmglgs,nvl(COUNT(ID),'0')as hts,nvl(SUM(HTQDJ),'0') as htqdj,nvl(SUM(ZXHTJ),'0') as zxhtj, ");
      		sbSql.append("nvl(SUM(HTZF),'0') as htzf,nvl(SUM(WCZF),'0') as wczf FROM GC_HTGL_HTSJ ");
      		sbSql.append("WHERE HTID IN(SELECT ID FROM GC_HTGL_HT WHERE HTLX = 'JL' AND SFYX = '1') ");
      		sbSql.append("AND JHSJID IN(SELECT GC_JH_SJ_ID FROM GC_JH_SJ WHERE SFYX = '1'  "+sqlnd);
      		sbSql.append("AND XMID IN(SELECT GC_TCJH_XMXDK_ID FROM GC_TCJH_XMXDK WHERE SFYX = '1'"+depStr+"))");
      		sbSql.append("AND SFYX = '1' ");
      		sbSql.append("UNION ALL ");
      		sbSql.append("SELECT '其他合同'as xmglgs,nvl(COUNT(ID),'0' ) as hts,nvl(SUM(HTQDJ),'0' ) as htqdj,nvl(SUM(ZXHTJ),'0' ) as zxhtj, ");
      		sbSql.append("nvl(SUM(HTZF),'0' ) as htzf,nvl(SUM(WCZF),'0' ) as wczf FROM GC_HTGL_HTSJ ");
      		sbSql.append("WHERE HTID IN(SELECT ID FROM GC_HTGL_HT WHERE HTLX = 'QT' AND SFYX = '1') ");
      		sbSql.append("AND JHSJID IN(SELECT GC_JH_SJ_ID FROM GC_JH_SJ WHERE   SFYX = '1'  "+sqlnd);
      		sbSql.append("AND XMID IN(SELECT GC_TCJH_XMXDK_ID FROM GC_TCJH_XMXDK WHERE SFYX = '1'"+depStr+"))");
      		sbSql.append("AND SFYX = '1' ");
      		String sql = sbSql.toString();
      		sql=sql+"  UNION ALL select  DISTINCT '总计' as zj , sum(hts) ,sum(htqdj), sum(zxhtj),sum(htzf),sum(wczf) from( "+sql+")";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			bs.setFieldThousand("HTQDJ");
			bs.setFieldThousand("WCZF");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	//工程部总体概况
	@Override
	public String gcb_ztgk(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
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
				"(  select nvl(count (distinct xmid),0) as sum_ndxm from GC_JH_SJ where sfyx='1' "+sqlnd+" ) as  sum_ndxm  , "+
				"(  select nvl(count (gc_jh_sj_id),0) as sum_bd from gc_jh_sj where gc_jh_sj_id not in (select gc_jh_sj_id from gc_jh_sj where xmbs = '0' and ISNOBDXM = '0') and sfyx='1' "+sqlnd+" ) as   sum_bd , "+ 
				"(  select nvl(trunc(sum(JHZTZE)/ 100000000,2), '0') ztz_sum from GC_JH_SJ tcjh left join GC_TCJH_XMXDK xdk on tcjh.xmid=xdk.gc_tcjh_xmxdk_id where xdk.sfyx='1' and tcjh.sfyx='1'  "+sqlnd_tcjh+" ) as  ztz_sum  , "+
				"(  select nvl(count (distinct xmid),0) as sum_ykgxm from GC_JH_SJ where sfyx='1'   and bdid is null and KGSJ_SJ is not null "+sqlnd+" ) as   sum_ykgxm , "+
				"(  select nvl(count (GC_JH_SJ_ID),0) as sum_ykgbd from GC_JH_SJ where gc_jh_sj_id not in (select gc_jh_sj_id from gc_jh_sj where xmbs = '0' and ISNOBDXM = '0' ) and sfyx='1' and KGSJ_SJ is not null "+sqlnd+" ) as   sum_ykgbd , "+
				"(  select nvl(count (distinct GC_JH_SJ_ID),0) as sum_wgxm from GC_JH_SJ where sfyx='1' and bdid is null and WGSJ_SJ is not null "+sqlnd+" ) as  sum_wgxm  , "+
				"(  select nvl(count (GC_JH_SJ_ID),0) sum_wgbd from GC_JH_SJ where gc_jh_sj_id not in (select gc_jh_sj_id from gc_jh_sj where xmbs = '0' and ISNOBDXM = '0' and sfyx='1') and sfyx='1' and WGSJ_SJ is not null "+sqlnd+" ) as  sum_wgbd  , "+
				"(  select nvl(count (xmid),0) sum_wfkxm from GC_JH_SJ where sfyx='1' and bdid is null and WGSJ is not null and WGSJ_SJ is null "+sqlnd+" ) as   sum_wfkxm , "+
				"(  select nvl(count (GC_JH_SJ_ID),0) sum_wfkbd from GC_JH_SJ where sfyx='1' and gc_jh_sj_id not in (select gc_jh_sj_id from gc_jh_sj where xmbs = '0' and ISNOBDXM = '0' and sfyx='1') and WGSJ is not null and WGSJ_SJ is null "+sqlnd+" ) as  sum_wfkbd  , "+
			
				" ( select nvl(COUNT( distinct(GC_JH_SJ_ID)), 0) AS yzyqxm from ( SELECT GC_JH_SJ_ID,kgsj,kgsj_sj,wgsj,wgsj_sj,trunc(sysdate) as now  FROM GC_JH_SJ tcjh  WHERE tcjh.KGSJ IS NOT NULL AND tcjh.wgsj IS NOT NULL AND bdid IS NULL  "+sqlnd_tcjh+"  AND tcjh.sfyx = '1') tcjh "+
				"  where tcjh.kgsj_sj IS  NULL AND tcjh.wgsj_sj IS NULL AND (trunc(SYSDATE)>tcjh.KGSJ  OR trunc(SYSDATE)>tcjh.wgsj) or ( tcjh.kgsj_sj IS not NULL AND tcjh.wgsj_sj IS NULL AND (tcjh.kgsj_sj>tcjh.KGSJ  OR trunc(SYSDATE)>tcjh.wgsj) ) or( tcjh.kgsj_sj IS  NULL AND tcjh.wgsj_sj IS not NULL AND (trunc(SYSDATE)>tcjh.KGSJ  OR tcjh.wgsj_sj>tcjh.wgsj)) or( tcjh.kgsj_sj IS not NULL AND tcjh.wgsj_sj IS not NULL AND (tcjh.kgsj_sj>tcjh.KGSJ  OR tcjh.wgsj_sj>tcjh.wgsj)) ) as yzyqxm ,"+
				
				

				" ( select nvl(COUNT( distinct(GC_JH_SJ_ID)), 0) yzyqbd  from( SELECT GC_JH_SJ_ID,kgsj,kgsj_sj,wgsj,wgsj_sj,trunc(sysdate) as now FROM GC_JH_SJ tcjh  WHERE tcjh.sfyx = '1'  "+sqlnd_tcjh+"  AND tcjh.KGSJ IS NOT NULL AND tcjh.wgsj IS NOT NULL AND gc_jh_sj_id NOT IN (SELECT gc_jh_sj_id FROM gc_jh_sj WHERE xmbs = '0'  AND ISNOBDXM = '0' AND sfyx = '1') ) tcjh "+
				"  where tcjh.kgsj_sj IS  NULL AND tcjh.wgsj_sj IS NULL AND (trunc(SYSDATE)>tcjh.KGSJ  OR trunc(SYSDATE)>tcjh.wgsj)or( tcjh.kgsj_sj IS not NULL AND tcjh.wgsj_sj IS NULL AND (tcjh.kgsj_sj>tcjh.KGSJ  OR trunc(SYSDATE)>tcjh.wgsj) )or( tcjh.kgsj_sj IS  NULL AND tcjh.wgsj_sj IS not NULL AND (trunc(SYSDATE)>tcjh.KGSJ  OR tcjh.wgsj_sj>tcjh.wgsj)) or( tcjh.kgsj_sj IS not NULL AND tcjh.wgsj_sj IS not NULL AND (tcjh.kgsj_sj>tcjh.KGSJ  OR tcjh.wgsj_sj>tcjh.wgsj)) ) as yzyqbd ,"+
				
					  				
				//"( select nvl(sum(ZJLBZ),'0') bzhj_sum from GC_XMGLGS_ZBB zb left join GC_TCJH_XMXDK xdk on xdk.gc_tcjh_xmxdk_id=zb.xdkid where   KSSJ >= to_date('"+sj+"','YYYY-MM-DD') and JSSJ < to_date('"+sj+"','YYYY-MM-DD')  "+sqlnd_xdk+"  and zb.sfyx='1' and xdk.sfyx='1' ) as  bzhj_sum  ,"+				
				//"(  select nvl(sum(ZJLBZ),'0') BYLJ_SUM from GC_XMGLGS_ZBB zb left join GC_TCJH_XMXDK xdk on xdk.gc_tcjh_xmxdk_id=zb.xdkid where  zb.sfyx='1' and xdk.sfyx='1' "+sqlnd_xdk+"  and  KSSJ >= to_date('"+fristday+"', 'YYYY-MM-DD')  and KSSJ <= to_date('"+lastday+"', 'YYYY-MM-DD')  ) as BYLJ_SUM ,"+
				//"( select decode(num_jl , 0, sum_by,sum_jl) BYLJ_SUM from (select sum(dyjlsdz) sum_jl, count(dyjlsdz) num_jl from GC_XMGLGS_JLB t where jlyf=to_date('"+fristday+"','yyyy-mm-dd') and sfyx=1 ), "+
				//" (select sum(ZJLBZ*10000) sum_by from GC_XMGLGS_ZBB zb left join GC_TCJH_XMXDK xdk on xdk.gc_tcjh_xmxdk_id=zb.xdkid where zb.sfyx='1' and xdk.sfyx='1' "+sqlnd_xdk+" and KSSJ >= to_date('"+fristday+"', 'YYYY-MM-DD')  and KSSJ <= to_date('"+lastday+"', 'YYYY-MM-DD'))) as BYLJ_SUM, "+

				"( SELECT nvl(SUM(jl.dyjlsdz), '0') AS bn_sum	FROM GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id = tcjh.gc_jh_sj_id WHERE jl.sfyx = '1'and tcjh.sfyx=1 "+sqlnd_tcjh+" )  as bn_sum ,"+
				" ( select nvl(sum(ljjlsdz),'0') as LJWC_SUM from GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id=tcjh.gc_jh_sj_id where jl.sfyx=1 and tcjh.sfyx=1 and jlyf=(select max(jlyf) from GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id=tcjh.gc_jh_sj_id where tcjh.nd=(select max(tcjh.nd) from GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id=tcjh.gc_jh_sj_id)) ) as ljwchj_sum ,"+  

				//" ( select nvl(sum(ZJLND),'0') as  bn_sum from GC_XMGLGS_ZBB zb left join GC_TCJH_XMXDK xdk on xdk.gc_tcjh_xmxdk_id=zb.xdkid where zb.sfyx='1' and xdk.sfyx='1' "+sqlnd_xdk+" ) as bn_sum, "+  				
				//" ( select nvl(sum(ZJLLJWC),'0') as ljwchj_sum from GC_XMGLGS_ZBB zb left join GC_TCJH_XMXDK xdk on xdk.gc_tcjh_xmxdk_id=zb.xdkid where zb.sfyx='1' and xdk.sfyx='1' ) as ljwchj_sum, "+  
				"(  select count(tcjh.gc_jh_sj_id) kgl_sum  from GC_GCB_KFG kfg left join GC_JH_SJ tcjh on kfg.tcjhsjid = tcjh.gc_jh_sj_id where kfg.sfyx='1' and tcjh.sfyx='1'and kgfg='0'  "+sqlnd_tcjh+" ) as   kgl_sum , "+
				"(  select count(tcjh.gc_jh_sj_id) kgl_sum from GC_GCB_KFG kfg  left join GC_JH_SJ tcjh on kfg.tcjhsjid = tcjh.gc_jh_sj_id left join FS_EVENT  event on kfg.sjbh=event.sjbh  where sjzt='2' and kfg.sfyx='1' and tcjh.sfyx='1'and kgfg='0'   "+sqlnd_tcjh+" ) as  kglsp_sum  , "+
				"(  select count(tcjh.gc_jh_sj_id) kgl_sum from GC_GCB_KFG kfg  left join GC_JH_SJ tcjh on kfg.tcjhsjid = tcjh.gc_jh_sj_id  where kfg.sfyx='1' and tcjh.sfyx='1' and kgfg='1'  "+sqlnd_tcjh+" ) as   fgl_sum , "+
				//"(  select count(distinct(kfg.bdid)) fglsp_sum from GC_GCB_KFG kfg left join GC_JH_SJ tcjh on kfg.bdid=tcjh.bdid left join GC_TCJH_XMXDK xdk on kfg.xdkid=xdk.gc_tcjh_xmxdk_id left join FS_EVENT  event on kfg.sjbh=event.sjbh  where xdk.sfyx='1' and sjzt='2' and kfg.sfyx='1' and tcjh.sfyx='1'and kgfg='0'   "+sqlnd_tcjh+" ) as   fglsp_sum , "+
				"(  select count(tcjh.gc_jh_sj_id) kgl_sum from GC_GCB_KFG kfg  left join GC_JH_SJ tcjh on kfg.tcjhsjid = tcjh.gc_jh_sj_id   where  kfg.sfyx='1' and tcjh.sfyx='1' and kgfg='2'  "+sqlnd_tcjh+" ) as  tgl_sum  , "+
				//SJL 新增未办理项目数
				"(  SELECT COUNT(gc_jh_sj_id) FROM GC_JH_SJ WHERE GC_JH_SJ_ID NOT IN(SELECT DISTINCT TCJHSJID FROM GC_GCB_KFG WHERE SFYX = '1')"+sqlnd+")as WBL_SUM  , "+
				
				"(  select nvl(COUNT(*),0) as yjnxm FROM gc_zjgl_lybzj gzl where gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1'" +sqlnd+ " and bdid is null)  ) as   yjnxm , "+
				"(  select nvl(COUNT(*),0) as yjnbd FROM gc_zjgl_lybzj gzl where gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1'" +sqlnd+ " and gc_jh_sj_id not in (select gc_jh_sj_id from gc_jh_sj where xmbs = '0' and ISNOBDXM = '0' and sfyx='1'))  ) as   yjnbd , "+
				"(  select nvl(sum(gzl.je),0) as yjnze FROM gc_zjgl_lybzj gzl where gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1'" +sqlnd+ ")  ) as  yjnze  , "+
				"(  select count(*) as ysp FROM gc_zjgl_lybzj gzl WHERE gzl.fhqk='2' and gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1'" +sqlnd+ ")  ) as   ysp , "+
				"(  select count(*) as yfh FROM gc_zjgl_lybzj gzl WHERE gzl.fhqk='6' and gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1'" +sqlnd+ ")  ) as  yfh  , "+
				"(  select nvl(sum(gzl.je),0) as yfhze FROM gc_zjgl_lybzj gzl WHERE gzl.fhqk='6' and gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1'" +sqlnd+ ")  ) as  yfhze  , "+
				"(  select count(*)  bhsxxm FROM gc_zjgl_lybzj gzl WHERE gzl.jnfs='BH' and  gzl.sfyx=1 AND gzl.bhyxqz <SYSDATE and gzl.jhsjid in(select GC_JH_SJ_ID from GC_JH_SJ where sfyx ='1' " +sqlnd+ ")  ) as  bhsxxm  , "+
				"(  select count(GC_GCGL_GCQS_ID) ytj_sum from GC_GCGL_GCQS gcqs  where sfyx='1'  "+sqlnd+" ) as  ytj_sum  , "+
				//"(  select count(GC_GCGL_GCQS_ID) ysp_sum from GC_GCGL_GCQS gcqs left join fs_event event  on gcqs.sjbh=event.sjbh  where sfyx='1' and sjzt='7' "+sqlnd+" ) as  ysp_sum  , "+
				"(  select count(GC_GCGL_GCQS_ID) bytj_sum from GC_GCGL_GCQS gcqs  where sfyx='1' and to_char(gcqs.qstcrq,'yyyy-mm')='"+time.substring(0, 7)+"' "+sqlnd+" ) as  bytj_sum  , "+
				//"(  select count(GC_GCGL_GCQS_ID) byysp_sum from GC_GCGL_GCQS gcqs left join fs_event event  on gcqs.sjbh=event.sjbh  where sfyx='1' and sjzt='7' and to_char(gcqs.qstcrq,'yyyy-mm')='2013-11' "+sqlnd+" ) as  byysp_sum  , "+
				"(  select to_char(max(kssj),'YYYY-MM-DD') max_kssj  from GC_XMGLGS_ZBB zb left join GC_TCJH_XMXDK xdk on xdk.gc_tcjh_xmxdk_id=zb.xdkid  where zb.sfyx='1' and xdk.sfyx='1'  "+sqlnd_xdk+" ) as   max_kssj , "+
				"(  select to_char(max(jlyf),'YYYY-MM') jlyf_new from GC_XMGLGS_JLB where sfyx='1' "+sqlnd+"  ) as  jlyf_new "+
				" from dual ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
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
	
	
	//洽商最多的项目数(TOP5)
	@Override
	public String qsxm_xm_top(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			OrgDept dept = user.getOrgDept();
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd="and T.nd='"+nd+"' ";
			}
			String sql="select rownum || '、' || xmmc as xmmc,  '[' || sum_xm || ']' || '次' num_xm  from (select distinct (tj.jhsjid), xmmc, sum_xm  from (select count(jhsjid) sum_xm, jhsjid "+
					   " from GC_GCGL_GCQS where bdid is null  group by jhsjid) tj left join GC_GCGL_GCQS gcqs  on tj.jhsjid = gcqs.jhsjid left join GC_JH_SJ T  on TJ.jhsjid = T.GC_JH_SJ_ID  where gcqs.sfyx = '1' "+sqlnd+" order by sum_xm desc)  where rownum <= 5 ";
			/*String sql=" select * from(" +
					   " select rownum || '、' || xmmc || nvl2(bdmc, '/' || bdmc, '') mc_js,  '[' || sum || ']' || '次' num_js ,mod(rownum,2) rnum "+
					   " from (select distinct (tj.jhsjid), xmmc, bdmc, sum from (select count(jhsjid) sum, jhsjid from GC_GCGL_GCQS  group by jhsjid) tj "+
					   " left join GC_GCGL_GCQS gcqs on tj.jhsjid = gcqs.jhsjid  where sfyx = '1'"+sqlnd+" order by sum desc) where rownum <= 10 "+
					   " )where rnum=1";*/
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	//洽商最多的标段数(TOP5)
	@Override
	public String qsxm_bd_top(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			OrgDept dept = user.getOrgDept();
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd="and nd='"+nd+"' ";
			}
			String sql=" select rownum || '、' || nvl2(bdmc, nvl2(bdbh, bdmc || '—' || bdbh, bdmc), xmmc) bdmc,  '[' || sum_bd || ']' || '次' num_bd  from (select distinct (tj.jhsjid), xmmc, bdmc, bdbh, sum_bd "+
						" from (select count(jhsjid) sum_bd, jhsjid  from GC_GCGL_GCQS gcqs where gcqs.jhsjid not in (select gc_jh_sj_id  from gc_jh_sj where xmbs = '0'  and ISNOBDXM = '0'and sfyx = 1) and gcqs.sfyx = 1  group by jhsjid) tj " +
 						" left join GC_GCGL_GCQS gcqs on tj.jhsjid = gcqs.jhsjid  left join gc_jh_sj tcjh on gcqs.jhsjid = tcjh.gc_jh_sj_id where sfyx = '1' and tcjh.sfyx = 1 "+sqlnd+" order by sum_bd desc) where rownum <= 5";
			/*String sql=" select * from(" +
					   " select rownum || '、' || xmmc || nvl2(bdmc, '/' || bdmc, '') mc_os,  '[' || sum || ']' || '次' num_os ,mod(rownum,2) rnum "+
					   " from (select distinct (tj.jhsjid), xmmc, bdmc, sum from (select count(jhsjid) sum, jhsjid from GC_GCGL_GCQS  group by jhsjid) tj "+
					   " left join GC_GCGL_GCQS gcqs on tj.jhsjid = gcqs.jhsjid  where sfyx = '1'"+sqlnd+" order by sum desc) where rownum <= 10 "+
					   " )where rnum=0";*/
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	

	
	//校验开始时间和结束时间是否重复————————————————新增时
	@Override
	public String query_ksjs_sj(String json,User user,String kssj,String jssj,String jhsjid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				String sql_kssj = "select GC_XMGLGS_ZBB_ID from GC_XMGLGS_ZBB zb  where sfyx='1' and kssj<=to_date('"+kssj+"','yyyy-mm-dd') and jssj> =to_date('"+kssj+"','yyyy-mm-dd') and jhsjid='"+jhsjid+"'";			
				String result_kssj[][] = DBUtil.query(conn, sql_kssj);

				String sql_jssj = "select GC_XMGLGS_ZBB_ID from GC_XMGLGS_ZBB zb  where sfyx='1' and kssj<=to_date('"+jssj+"','yyyy-mm-dd') and jssj> =to_date('"+jssj+"','yyyy-mm-dd') and jhsjid='"+jhsjid+"'";			
				String result_jssj[][] = DBUtil.query(conn, sql_jssj);
				
				String sql_ksjs="select GC_XMGLGS_ZBB_ID from GC_XMGLGS_ZBB zb  where sfyx='1' and kssj>=to_date('"+kssj+"','yyyy-mm-dd') and jssj< =to_date('"+jssj+"','yyyy-mm-dd') and jhsjid='"+jhsjid+"'";
				String result_ksjs[][] = DBUtil.query(conn, sql_ksjs);
				if(null!=result_kssj&&result_kssj.length>0&&!Pub.empty(result_kssj[0][0]))
				{					
					domresult ="flag1";
				}
				else
				{
					if(null!=result_jssj&&result_jssj.length>0&&!Pub.empty(result_jssj[0][0]))
					{					
						domresult ="flag2";
					}
					else
					{
						if(null!=result_ksjs&&result_ksjs.length>0&&!Pub.empty(result_ksjs[0][0]))
						{
							domresult ="flag3";
						}	
					}	
				}
			}  catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
	}

	
	//校验开始时间和结束时间是否重复————————————————————————修改时
	@Override
	public String query_sj(String json,User user,String ksORjs,String kssj,String jssj,String jhsjid,String zbid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				if(ksORjs.equals("1"))
				{
					String sql_kssj = "select GC_XMGLGS_ZBB_ID from GC_XMGLGS_ZBB zb  where sfyx='1' and kssj<=to_date('"+kssj+"','yyyy-mm-dd') and  jssj> =to_date('"+kssj+"','yyyy-mm-dd') and jhsjid='"+jhsjid+"' and GC_XMGLGS_ZBB_ID<>'"+zbid+"'";					 																		//kssj<=to_date('"+kssj+"','yyyy-mm-dd') and jssj> =to_date('"+kssj+"','yyyy-mm-dd')					
					String result_kssj[][] = DBUtil.query(conn, sql_kssj);					

					String sql_ksjs="select GC_XMGLGS_ZBB_ID from GC_XMGLGS_ZBB zb  where sfyx='1' and kssj>=to_date('"+kssj+"','yyyy-mm-dd') and jssj< =to_date('"+jssj+"','yyyy-mm-dd') and jhsjid='"+jhsjid+"' and GC_XMGLGS_ZBB_ID<>'"+zbid+"'";
					String result_ksjs[][] = DBUtil.query(conn, sql_ksjs);

					if(null!=result_kssj&&result_kssj.length>0&&!Pub.empty(result_kssj[0][0]))
					{					
						domresult ="flag1";
					}
					else
					{
						if(null!=result_ksjs&&result_ksjs.length>0&&!Pub.empty(result_ksjs[0][0]))
						{
							domresult ="flag3";
						}							
					}	
				}	
				else
				{
					String sql_jssj = "select GC_XMGLGS_ZBB_ID from GC_XMGLGS_ZBB zb  where sfyx='1' and kssj<=to_date('"+jssj+"','yyyy-mm-dd') and  jssj> =to_date('"+jssj+"','yyyy-mm-dd') and jhsjid='"+jhsjid+"' and GC_XMGLGS_ZBB_ID<>'"+zbid+"'";			
					String result_jssj[][] = DBUtil.query(conn, sql_jssj);					

					String sql_ksjs="select GC_XMGLGS_ZBB_ID from GC_XMGLGS_ZBB zb  where sfyx='1' and kssj>=to_date('"+kssj+"','yyyy-mm-dd') and jssj< =to_date('"+jssj+"','yyyy-mm-dd') and jhsjid='"+jhsjid+"' and GC_XMGLGS_ZBB_ID<>'"+zbid+"'";
					String result_ksjs[][] = DBUtil.query(conn, sql_ksjs);
					if(null!=result_jssj&&result_jssj.length>0&&!Pub.empty(result_jssj[0][0]))
					{					
						domresult ="flag2";
					}
					else
					{
						if(null!=result_ksjs&&result_ksjs.length>0&&!Pub.empty(result_ksjs[0][0]))
						{
							domresult ="flag3";
						}							
					}	
				}	
			}  catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
	}
	
	
	//新增周报
	@Override
	public String insert(String json,User user,String ywid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		EventVO eventVO = null;
		GcZbbVO vo = new GcZbbVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list_zb = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list_zb.get(0));
		    eventVO = EventManager.createEvent(conn,ywlx, user);//生成事件
		    vo.setSjbh(eventVO.getSjbh());
			if(!Pub.empty(ywid))
			{
				vo.setGc_xmglgs_zbb_id(ywid);
		        // FileUploadService.updateFjztByYwid(conn, ywid);
		        FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
		        fvo.setGlid3(vo.getXdkid()); //存入项目ID
		        fvo.setGlid4(vo.getBdid()); //存入标段ID
		        fvo.setSjbh(vo.getSjbh());//事件编号
		        fvo.setYwlx(ywlx);
		        FileUploadService.updateVOByYwid(conn, fvo, ywid);
			}
			else
			{				
				ywid=new RandomGUID().toString();
			}	
			vo.setGc_xmglgs_zbb_id(ywid); 
			//vo.setHtid(htvo.getId());
			vo.setYwlx(ywlx);
			BusinessUtil.setInsertCommonFields(vo,user);
			FileUploadService.updateFjztByYwid(conn,ywid);
			BaseDAO.insert(conn, vo);
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "新增<工程周报>成功", user,"","");
			resultVO = vo.getRowJson();
			conn.commit();
			
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultVO;
	}
	

	//修改周报
	@Override
	public String update_zb(String json,User user,String ywid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		EventVO eventVO = null;
		GcZbbVO vo = new GcZbbVO();
		GcZbbVO vo_old = new GcZbbVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list_zb = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list_zb.get(0));
			vo_old.setGc_xmglgs_zbb_id(vo.getGc_xmglgs_zbb_id());
			vo_old = (GcZbbVO)BaseDAO.getVOByPrimaryKey(conn,vo_old);
			vo.setYwlx(ywlx);
			BusinessUtil.setInsertCommonFields(vo,user);
	        FileUploadVO fvo = new FileUploadVO();
	        fvo.setFjzt("1");//更新附件状态
	        fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
	        fvo.setGlid3(vo.getXdkid()); //存入项目ID
	        fvo.setGlid4(vo.getBdid()); //存入标段ID
	        fvo.setSjbh(vo_old.getSjbh());//事件编号
	        fvo.setYwlx(ywlx);
	        FileUploadService.updateVOByYwid(conn, fvo, ywid);
			BaseDAO.update(conn, vo);
			LogManager.writeUserLog(vo_old.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改<工程周报>成功", user,"","");
			resultVO = vo.getRowJson();
			conn.commit();
			
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultVO;
	}

	
	//删除检查信息
	@Override
	public String delete(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZbbVO xmvo = new GcZbbVO(); 
		try {
		conn.setAutoCommit(false);
		JSONArray list_jc = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list_jc.get(0));
		xmvo = (GcZbbVO)BaseDAO.getVOByPrimaryKey(conn,xmvo);
		xmvo.setSfyx("0");
		BaseDAO.update(conn,xmvo);
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "删除周报信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除周报信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	
	@Override
	public String queryZbsj(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		String orderFilter = RequestUtil.getOrderFilter(json);
		condition += BusinessUtil.getSJYXCondition(null);
	    condition += BusinessUtil.getCommonCondition(user,null);
	    condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);

			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"t.gc_xmglgs_sjb_id, t.kssj, t.jssj, t.ywlx, t.sjbh, t.bz, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx " +
					"FROM " +
					"GC_XMGLGS_SJB t";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("KSSJ", "yyyy-MM-dd");
			bs.setFieldDateFormat("JSSJ", "yyyy-MM-dd");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, YwlxManager.GC_XMGLGS_ZBSJ,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<周报时间信息>", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String insertZbsj(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		EventVO eventVO = null;
		GsSjbVO vo = new GsSjbVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			if(vo.getGc_xmglgs_sjb_id().equals("") || vo.getGc_xmglgs_sjb_id().equals(null)){
				vo.setGc_xmglgs_sjb_id(new RandomGUID().toString()); 
				vo.setYwlx(YwlxManager.GC_XMGLGS_ZBSJ);
			    eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			    vo.setSjbh(eventVO.getSjbh());
				BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), YwlxManager.GC_XMGLGS_ZBSJ,
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "新增<周报时间>成功", user,"","");
			}else{
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), YwlxManager.GC_XMGLGS_ZBSJ,
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "更新<周报时间>成功", user,"","");
			}
		
			resultVO = vo.getRowJson();
			conn.commit();
			
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultVO;
	}
	
	
	/* 
	 * 工程周报管理自动模糊匹配项目名称
	 * @see com.ccthanking.business.tcjh.jhgl.service.TcjhService#xmmcAutoComplete(com.ccthanking.framework.model.autocomplete, com.ccthanking.framework.common.User)
	 */
	@Override
	public List<autocomplete> xmmcAutoComplete(autocomplete json,User user) throws Exception {
		List<autocomplete> autoResult = new ArrayList<autocomplete>(); 
		autocomplete ac = new autocomplete();
		String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
		condition += BusinessUtil.getSJYXCondition("tcjh");
		condition += BusinessUtil.getCommonCondition(user,null);
		String a=user.getDepartment();
		String [][] result = DBUtil.query("select distinct tcjh.xmmc from GC_JH_SJ " + json.getTablePrefix() + "  left join  gc_tcjh_xmxdk xdk on xdk.gc_tcjh_xmxdk_id=tcjh.xmid where " + condition);
        if(null != result&&result.length>0){
        	for(int i =0;i<result.length;i++){
        	  ac = new autocomplete();
              ac.setRegionName(result[i][0]);
              autoResult.add(ac);
        	}
        }
		return autoResult;
	}	

	
	
	
	/* 
	 * 周报统计自动模糊匹配项目名称
	 * @see com.ccthanking.business.tcjh.jhgl.service.TcjhService#xmmcAutoComplete(com.ccthanking.framework.model.autocomplete, com.ccthanking.framework.common.User)
	 */
	@Override
	public List<autocomplete> xmmcAutoComplete_tj(autocomplete json,User user) throws Exception {
		List<autocomplete> autoResult = new ArrayList<autocomplete>(); 
		autocomplete ac = new autocomplete();
		String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
		condition += BusinessUtil.getSJYXCondition(null);
		condition += BusinessUtil.getCommonCondition(user,null);
		String [][] result = DBUtil.query("select distinct xmmc from GC_XMGLGS_ZBB " + json.getTablePrefix() + " where " + condition);
        if(null != result&&result.length>0){
        	for(int i =0;i<result.length;i++){
        	  ac = new autocomplete();
              ac.setRegionName(result[i][0]);
              autoResult.add(ac);
        	}
        }
		return autoResult;
	}


	
	
}
