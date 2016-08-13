package com.ccthanking.business.gcgl.gcjl.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.gcgl.gcjl.service.GcjlService;
import com.ccthanking.business.gcgl.gcjl.vo.JlbVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class GcjlServiceImpl implements GcjlService {
	
	private String ywlx = YwlxManager.GC_XMGLGS_JL;//业务类型-工程计量

	@Override
	public String query(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		try {

		PageManager page = RequestUtil.getPageManager(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		String orderFilter = RequestUtil.getOrderFilter(json);
		String yf = request.getParameter("yf");
		String nd = request.getParameter("nd");
		String xmglgs = request.getParameter("xmglgs");
		condition += " AND T.XMID = T1.GC_TCJH_XMXDK_ID ";
		condition += " AND (/*t.XMGLGS = '"+xmglgs+"' or*/ t.gc_jh_sj_id in (select tt.gc_jh_sj_id from gc_jh_sj tt where /*tt.xmbs = '0' and*/ tt.xmid in (select xmid from gc_jh_sj j /*where j.xmglgs = '"+xmglgs+"'*/))) ";
		if(!Pub.empty(nd)){
			condition += " AND T.ND = '"+nd+"' ";
		}
		condition += BusinessUtil.getSJYXCondition("t");
	    condition += BusinessUtil.getCommonCondition(user,"t");
	    condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String YNcondition ="";
			if(!Pub.empty(yf)){
				YNcondition += " AND JLYF = TO_DATE('"+yf+"','YYYY-MM') ";
			}
			if(!Pub.empty(nd)){
				YNcondition += " AND ND = '"+nd+"' ";
			}
			String sql = "SELECT " +
					"t.GC_JH_SJ_ID,t.XMBH,t.ND,t.XMID,t.BDID,t.XMMC,t.BDMC,t.XMBS,t.XMSX,t.XMXZ,t.XMGLGS,T.ISNOBDXM, " +
					"(case xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = t.bdid) end) SGDW,"+
					"(case xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select jldw from GC_XMBD where GC_XMBD_ID = t.bdid) end) JLDW," +
					"(case xmbs when '0' then (select sjdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select sjdw from GC_XMBD where GC_XMBD_ID = t.bdid) end) SJDW,"+
					"T1.GC_TCJH_XMXDK_ID,"+
					"(SELECT HTBM FROM GC_HTGL_HT WHERE htlx = 'SG' AND sfyx = '1' AND ID IN(SELECT HTID FROM GC_HTGL_HTSJ WHERE jhsjid = t.gc_jh_sj_id) and rownum=1)  AS HTBM,"+
					"(SELECT HTQDJ FROM GC_HTGL_HTSJ WHERE htid in(select ID from GC_HTGL_HT where htlx = 'SG' and sfyx = '1') and sfyx = '1' and jhsjid = t.gc_jh_sj_id and rownum=1) AS HTQDJ,"+
					"nvl((case xmbs when '0' then(SELECT SUM(DYJLSDZ) FROM GC_XMGLGS_JLB WHERE t.Xmid = XMID AND SFYX ='1' "+YNcondition +
					")when '1' then(SELECT DYJLSDZ FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID AND SFYX ='1' "+YNcondition+
					")END),'0')AS DYJLSDZ,"+
					//累计质保金
					"nvl((case xmbs when '0' then(SELECT SUM(LJZBJ) FROM GC_XMGLGS_JLB WHERE t.Xmid = XMID AND SFYX ='1' "+YNcondition +
					")when '1' then(SELECT LJZBJ FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID AND SFYX ='1' "+YNcondition+
					")END),'0')AS LJZBJ,"+
					//甲供材款
					"nvl((case xmbs when '0' then(SELECT SUM(GCK_LJ) FROM GC_XMGLGS_JLB WHERE t.Xmid = XMID AND SFYX ='1' "+YNcondition +
					")when '1' then(SELECT GCK_LJ FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID AND SFYX ='1' "+YNcondition+
					")END),'0')AS GCK_LJ,"+
					//工程款
					"nvl((case xmbs when '0' then(SELECT SUM(GCK1_LJ) FROM GC_XMGLGS_JLB WHERE t.Xmid = XMID AND SFYX ='1' "+YNcondition +
					")when '1' then(SELECT GCK1_LJ FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID AND SFYX ='1' "+YNcondition+
					")END),'0')AS GCK1_LJ,"+
					//累计当月应付款
					"nvl((case xmbs when '0' then(SELECT SUM(LJDYYFK) FROM GC_XMGLGS_JLB WHERE t.Xmid = XMID AND SFYX ='1' "+YNcondition +
					")when '1' then(SELECT LJDYYFK FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID AND SFYX ='1' "+YNcondition+
					")END),'0')AS LJDYYFK,"+
					//以前年度结转监理审定值
					"nvl((case xmbs when '0' then(select sum(JZJLZ) from GC_XMGLGS_JLB J where J.XMID=t.XMID and J.SFYX='1' "+YNcondition+") " +
					"when '1' then (select JZJLZ from GC_XMGLGS_JLB J where J.TCJH_SJ_ID=t.GC_JH_SJ_ID and J.SFYX='1' "+YNcondition+")" +
					"END),'0') as JZJLZ,"+
					//当前年度结转监理审定值
					"nvl((case xmbs when '0' then(select sum(LJJLZ) from GC_XMGLGS_JLB J where J.XMID=t.XMID and J.SFYX='1' "+YNcondition+")" +
					"when '1' then(select LJJLZ from GC_XMGLGS_JLB J where J.TCJH_SJ_ID=t.GC_JH_SJ_ID and J.SFYX='1' "+YNcondition+")" +
					"END),'0') as LJJLZ,"+
					//完工百分比
					"(select WGBFB from GC_XMGLGS_JLB J where J.TCJH_SJ_ID=t.GC_JH_SJ_ID  and J.SFYX='1'"+YNcondition+") as WGBFB,"+
					//累计监理审定值
					"nvl((case xmbs when '0' then (SELECT sum(LJJLSDZ) FROM GC_XMGLGS_JLB WHERE XMID = T.XMID AND SFYX = '1' "+YNcondition+")" +
					"when '1' then(SELECT LJJLSDZ FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID AND SFYX = '1' "+YNcondition+")" +
					"END),'0') AS LJJLSDZ,"+
					//计量月份
					"(SELECT JLYF FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID AND SFYX = '1' "+YNcondition+")AS JLYF," +
					"(case t.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1) end ) as XMBDDZ ";
					sql += " FROM " +
					"GC_JH_SJ t,GC_TCJH_XMXDK T1";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//字典翻译
			//bs.setFieldDic("JLYF", "JLYF");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("XMXZ", "XMXZ");
			//表选翻译
			bs.setFieldTranslater("XMID", "GC_TCJH_XMXDK", "GC_TCJH_XMXDK_ID", "XMMC");
			bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDBH");
			//bs.setFieldTranslater("HTID", "GC_HTGL_HT", "ID", "HTBM");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");

			bs.setFieldDateFormat("JLYF", "yyyy-MM");
			//金额
			bs.setFieldThousand("DYJLSDZ");
			bs.setFieldThousand("DYZBJ");
			bs.setFieldThousand("DYYFK");
			bs.setFieldThousand("LJDYYFK");
			bs.setFieldThousand("LJJLSDZ");
			bs.setFieldThousand("LJZBJ");
			bs.setFieldThousand("JZJLZ");
			bs.setFieldThousand("LJJLZ");
			bs.setFieldThousand("GCK_DY");
			bs.setFieldThousand("GCK1_DY");
			bs.setFieldThousand("XJ_DY");
			bs.setFieldThousand("GCK_LJ");
			bs.setFieldThousand("GCK1_LJ");
			bs.setFieldThousand("XJ_LJ");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<工程计量信息>", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	@Override
	public String insert(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String resultVO = null;
		EventVO eventVO = null;
		JlbVO vo = new JlbVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			if(Pub.empty(vo.getGc_xmglgs_jlb_id())){
				vo.setGc_xmglgs_jlb_id(new RandomGUID().toString()); 
				vo.setDyjlsdz(vo.getXj_dy());//当月审定值=当月小计
				vo.setLjjlsdz(vo.getXj_lj());//累计审定值=累计小计
				//vo.setNd(Pub.getDate("yyyy", vo.getJlyf()));
				vo.setYwlx(ywlx);
			    eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			    vo.setSjbh(eventVO.getSjbh());
				BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), ywlx,
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "新增<工程计量>成功", user,"","");
			}else{
				vo.setDyjlsdz(vo.getXj_dy());//当月审定值=当月小计
				vo.setLjjlsdz(vo.getXj_lj());//累计审定值=累计小计
				vo.setNd(Pub.getDate("yyyy", vo.getJlyf()));
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), ywlx,
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "修改<工程计量>成功", user,"","");
			}
		
			resultVO = vo.getRowJson();
			conn.commit();
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	@Override
	public String queryByTcjhId(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += BusinessUtil.getSJYXCondition("t");
		    condition += BusinessUtil.getCommonCondition(user,"t");
		    condition += orderFilter;
			if (page == null)
				page = new PageManager();
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql = "SELECT " +
						"t.gc_xmglgs_jlb_id, t.xmid,t.bdid, t.jlyf, t.dyjlsdz, t.dyzbj, t.dyyfk, t.ljdyyfk, t.ljjlsdz, t.ljzbj, t.jzjlz, t.ljjlz, t.wgbfb, t.ywlx, t.sjbh, t.bz, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx, t.xmglgs, t.nd, t.sjdw, t.sgdw, t.jldw, t.gck_dy, t.gck1_dy, t.xj_dy, t.gck_lj, t.gck1_lj, t.xj_lj, t.xmmc, t.bdmc, t.tcjh_sj_id, t.xmsx, t.xmxz, "+
						"(SELECT HTBM FROM GC_HTGL_HT WHERE htlx = 'SG' AND sfyx = '1' AND ID IN(SELECT HTID FROM GC_HTGL_HTSJ WHERE jhsjid = t.TCJH_SJ_ID))  AS HTBM,"+
						"(SELECT HTQDJ FROM GC_HTGL_HTSJ WHERE htid in(select ID from GC_HTGL_HT where htlx = 'SG' and sfyx = '1') and sfyx = '1' and jhsjid = t.TCJH_SJ_ID) AS HTQDJ"+
						" FROM " +
						"GC_XMGLGS_JLB t";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				//字典翻译
				//bs.setFieldDic("JLYF", "JLYF");
				bs.setFieldDic("XMSX", "XMSX");
				bs.setFieldDic("XMXZ", "XMXZ");
				//表选翻译
				bs.setFieldTranslater("XMID", "GC_TCJH_XMXDK", "GC_TCJH_XMXDK_ID", "XMMC");
				bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDBH");
				//bs.setFieldTranslater("HTID", "GC_HTGL_HT", "ID", "HTBM");
				bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
				
				bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				
				bs.setFieldDateFormat("JLYF", "yyyy-MM");

				//金额
				bs.setFieldThousand("DYJLSDZ");
				bs.setFieldThousand("DYZBJ");
				bs.setFieldThousand("DYYFK");
				bs.setFieldThousand("LJDYYFK");
				bs.setFieldThousand("LJJLSDZ");
				bs.setFieldThousand("LJZBJ");
				bs.setFieldThousand("JZJLZ");
				bs.setFieldThousand("LJJLZ");
				bs.setFieldThousand("GCK_DY");
				bs.setFieldThousand("GCK1_DY");
				bs.setFieldThousand("XJ_DY");
				bs.setFieldThousand("GCK_LJ");
				bs.setFieldThousand("GCK1_LJ");
				bs.setFieldThousand("XJ_LJ");
				domresult = bs.getJson();
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
			
		}
	
	
	@Override
	public String queryTjxx(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String nd = request.getParameter("nd");
			String xmnd = request.getParameter("xmnd");
			String xmglgs = request.getParameter("xmglgs");
			condition += " AND T.XMID = T1.GC_TCJH_XMXDK_ID ";
			if(!Pub.empty(xmglgs)){
				condition += " AND (/*t.XMGLGS = '"+xmglgs+"' or*/ t.gc_jh_sj_id in (select tt.gc_jh_sj_id from gc_jh_sj tt where /*tt.xmbs = '0' and*/ tt.xmid in (select xmid from gc_jh_sj j /*where j.xmglgs = '"+xmglgs+"'*/))) ";
			}
			if(!Pub.empty(xmnd)){
				condition += " AND T.ND = '"+xmnd+"' ";
			}
			condition += BusinessUtil.getSJYXCondition("t");
		    condition += BusinessUtil.getCommonCondition(user,"t");
		    condition += orderFilter;
		    String yf1 = request.getParameter("yf1");
			
			String Jlcondition = "";
			String JlconditionJL = "";
			if(!Pub.empty(yf1)){
				Jlcondition += " AND JLYF = TO_DATE('"+yf1+"','YYYY-MM') ";
				JlconditionJL += " AND j.JLYF = TO_DATE('"+yf1+"','YYYY-MM') ";
			}
			if(!Pub.empty(nd)){
				Jlcondition += " AND ND = '"+nd+"'";
				JlconditionJL += " AND j.ND = '"+nd+"'";
			}
			if (page == null)
				
				page = new PageManager();
				page.setFilter(condition);
				conn.setAutoCommit(false);
				
				String sql = "SELECT " +
						"t.GC_JH_SJ_ID,t.ND,t.XMBH,t.XMID,t.BDID,t.XMMC,t.BDMC,t.XMBS,t.XMSX,t.XMXZ, " +
						"T1.GC_TCJH_XMXDK_ID,T1.SGDW,T1.JLDW,T1.SJDW,"+
						"(SELECT HTBM FROM GC_HTGL_HT WHERE htlx = 'SG' AND sfyx = '1' AND ID IN(SELECT HTID FROM GC_HTGL_HTSJ WHERE jhsjid = t.gc_jh_sj_id) and rownum=1)  AS HTBM,"+
						"(SELECT HTQDJ FROM GC_HTGL_HTSJ WHERE htid in(select ID from GC_HTGL_HT where htlx = 'SG' and sfyx = '1') and sfyx = '1' and jhsjid = t.gc_jh_sj_id and rownum=1) AS HTQDJ,"+

						"(case xmbs when '0' then (SELECT SUM(GCK1_LJ) FROM GC_XMGLGS_JLB WHERE  t.Xmid = XMID and sfyx='1' "+Jlcondition+" )"+
						"when '1' then (SELECT SUM(GCK1_LJ) FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID and sfyx='1' "+Jlcondition+") END)AS GCK1_LJ,"+
						"(case xmbs when '0' then (SELECT SUM(GCK_LJ) FROM GC_XMGLGS_JLB WHERE  t.Xmid = XMID and sfyx='1' "+Jlcondition+" )"+
						"when '1' then (SELECT SUM(GCK_LJ) FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID and sfyx='1' "+Jlcondition+") END)AS GCK_LJ,"+
						
						"(case xmbs when '0' then (select sum(ZZ) from (select max(LJJLSDZ) ZZ,J.BDID,J.XMID from Gc_Xmglgs_Jlb J,GC_JH_SJ  S where J.Tcjh_Sj_Id=S.GC_JH_SJ_ID "+
						" and (S.XMBS = '1' or isnobdxm = '1') and J.SFYX='1'"+JlconditionJL+" group by J.BDID,J.XMID) where XMID=T.XMID group by XMID ) "+
						"when '1' then (SELECT MAX(LJJLSDZ) FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID and sfyx='1' "+Jlcondition+") END)AS LJJLSDZ,"+
						"(case xmbs when '0' then (SELECT SUM(LJDYYFK) FROM GC_XMGLGS_JLB WHERE  t.Xmid = XMID and sfyx='1' "+Jlcondition+" )"+
						"when '1' then (SELECT SUM(LJDYYFK) FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID and sfyx='1' "+Jlcondition+") END)AS LJDYYFK,"+
						"(case xmbs when '0' then (SELECT SUM(LJZBJ) FROM GC_XMGLGS_JLB WHERE  t.Xmid = XMID and sfyx='1' "+Jlcondition+" )"+
						"when '1' then (SELECT SUM(LJZBJ) FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID and sfyx='1' "+Jlcondition+") END)AS LJZBJ,"+
						"(case xmbs when '0' then (SELECT SUM(JZJLZ) FROM GC_XMGLGS_JLB WHERE  t.Xmid = XMID and sfyx='1' "+Jlcondition+" )"+
						"when '1' then (SELECT SUM(JZJLZ) FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID and sfyx='1' "+Jlcondition+") END)AS JZJLZ,"+
						"(case xmbs when '0' then (SELECT SUM(LJJLZ) FROM GC_XMGLGS_JLB WHERE  t.Xmid = XMID and sfyx='1' "+Jlcondition+" )"+
						"when '1' then (SELECT SUM(LJJLZ) FROM GC_XMGLGS_JLB WHERE TCJH_SJ_ID = T.GC_JH_SJ_ID and sfyx='1' "+Jlcondition+") END)AS LJJLZ,"+
						"(case t.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1) end ) as XMBDDZ ";
						sql += " FROM " +
						"GC_JH_SJ t,GC_TCJH_XMXDK T1";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				//字典翻译
				//bs.setFieldDic("JLYF", "JLYF");
				bs.setFieldDic("XMSX", "XMSX");
				bs.setFieldDic("XMXZ", "XMXZ");
				//表选翻译
				bs.setFieldTranslater("XMID", "GC_TCJH_XMXDK", "GC_TCJH_XMXDK_ID", "XMMC");
				bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDBH");
				bs.setFieldTranslater("HTID", "GC_HTGL_HT", "ID", "HTBM");
				bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
				
				bs.setFieldDateFormat("JLYF", "yyyy-MM");
				//金额
				bs.setFieldThousand("DYJLSDZ");
				bs.setFieldThousand("DYZBJ");
				bs.setFieldThousand("DYYFK");
				bs.setFieldThousand("LJDYYFK");
				bs.setFieldThousand("LJJLSDZ");
				bs.setFieldThousand("LJZBJ");
				bs.setFieldThousand("JZJLZ");
				bs.setFieldThousand("LJJLZ");
				bs.setFieldThousand("GCK_DY");
				bs.setFieldThousand("GCK1_DY");
				bs.setFieldThousand("XJ_DY");
				bs.setFieldThousand("GCK_LJ");
				bs.setFieldThousand("GCK1_LJ");
				bs.setFieldThousand("XJ_LJ");
				domresult = bs.getJson();
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
			
		}
	
	@Override
	public String queryMaxMonth(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				conn.setAutoCommit(false);
				String nd = request.getParameter("nd");
				String xmglgs = request.getParameter("xmglgs");
				if(Pub.empty(nd)){
					nd = "";
				}
				if(Pub.empty(xmglgs)){
					xmglgs = "";
				}
				String sql = "SELECT to_char(MAX(JLYF),'yyyy-mm') FROM GC_XMGLGS_JLB WHERE SFYX = '1' AND ND = '"+nd+"' AND XMGLGS = '"+xmglgs+"'";
				String[][] yf = DBUtil.query(conn, sql);
				if(yf.length>0 && null !=yf && !Pub.empty(yf[0][0])){
					domresult = yf[0][0].toString();
				}else{
					domresult = "";
				}
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
			
		}
	
	
	@Override
	public String queryDate(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				conn.setAutoCommit(false);
				String jhsjid = request.getParameter("jhsjid");
				String dateValue = request.getParameter("dateValue");
				if(Pub.empty(jhsjid)){
					jhsjid = "";
				}
				if(Pub.empty(dateValue)){
					dateValue = "";
				}
				String sql = "SELECT count(JLYF) FROM GC_XMGLGS_JLB WHERE SFYX = '1' AND JLYF = to_date('"+dateValue+"','yyyy-mm') and TCJH_SJ_ID = '"+jhsjid+"'";
				String[][] yf = DBUtil.query(conn, sql);
				if(yf.length>0 && null !=yf && !Pub.empty(yf[0][0])){
					if(Integer.parseInt(yf[0][0].toString()) > 0){
						domresult = "0";
					}else{
						domresult = "1";
					}
				}else{
					domresult = "1";
				}
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
			
		}
	
	@Override
	public String delete(HttpServletRequest request, String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		JlbVO vo = new JlbVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			//删业务数据
			JlbVO delVO = new JlbVO();
			delVO.setGc_xmglgs_jlb_id(vo.getGc_xmglgs_jlb_id());
			delVO.setSfyx("0");
			BaseDAO.update(conn, delVO);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "工程计量删除成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "工程计量删除失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
}
