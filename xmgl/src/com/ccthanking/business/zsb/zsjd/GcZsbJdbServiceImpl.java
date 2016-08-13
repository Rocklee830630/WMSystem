package com.ccthanking.business.zsb.zsjd;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.business.pqgl.vo.PqJzfkVO;
import com.ccthanking.business.pqgl.vo.PqZxmVO;
import com.ccthanking.business.zsb.zsgl.xmb.GcZsbXmbVO;
import com.ccthanking.business.zsb.zsgl.xxb.GcZsbXxbVO;
import com.ccthanking.business.zsb.zsjd.GcZsbJdbService;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.business.zsb.zsjd.GcZsbJdbVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;


@Service
public  class GcZsbJdbServiceImpl implements GcZsbJdbService {
	private String ywlx=YwlxManager.GC_ZSB_JDB;

	@Override
	public String queryJdb(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition=condition+" and jh.gc_jh_sj_id = xxb.jhsjid and xxb.GC_ZSB_XXB_ID = jdb.ZDXXID";
		condition += BusinessUtil.getSJYXCondition("jdb")  +BusinessUtil.getSJYXCondition("xxb")  +BusinessUtil.getSJYXCondition("jh")  + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select jh.xmbh, jh.gc_jh_sj_id, jh.nd, jh.jhid, jh.bdid, jh.xmid, " +
					"xxb.gc_zsb_xxb_id, jdb.wtjfx, xxb.zsxmid, jh.xmmc, jh.CQT_SJ as CQTQDSJ, " +
					"xxb.qy, xxb.zrdw, xxb.mdwcrq, xxb.mdgzbz, xxb.jmhs, xxb.qyjs, xxb.jttdmj, xxb.gytdmj, " +
					"xxb.zmj, xxb.mdgs, xxb.sjfwcq, xxb.sjwcrq, xxb.qwtrq, xxb.qwtbz, xxb.zjdwje, xxb.zjdwrq, " +
					"xxb.zjdwbz, xxb.sjrq, xxb.cqssbz, xxb.cdyjrq, xxb.cdyjbz, xxb.tdfwyjrq, xxb.tdfwbz, xxb.cqbz, " +
					"xxb.ywlx, xxb.sjbh, jdb.bz, xxb.lrr, xxb.lrsj, xxb.lrbm, xxb.lrbmmc, xxb.gxr, xxb.gxsj, xxb.gxbm, xxb.gxbmmc, " +
					"(select sum(BCWCJMS) from GC_ZSB_JDB where sfyx='1' and ZDXXID = jdb.ZDXXID and to_date(to_char(SBRQ,'YYYYMMDD'),'YYYYMMDD') <=  to_date(to_char(jdb.SBRQ,'YYYYMMDD'),'YYYYMMDD')) ljjmzl, " +
					"(select sum(BCWCQYS) from GC_ZSB_JDB where sfyx='1' and ZDXXID = jdb.ZDXXID and to_date(to_char(SBRQ,'YYYYMMDD'),'YYYYMMDD') <=  to_date(to_char(jdb.SBRQ,'YYYYMMDD'),'YYYYMMDD')) LJQYZL, " +
					"(select sum(BCZDMJ) from GC_ZSB_JDB where sfyx='1' and ZDXXID = jdb.ZDXXID and to_date(to_char(SBRQ,'YYYYMMDD'),'YYYYMMDD') <=  to_date(to_char(jdb.SBRQ,'YYYYMMDD'),'YYYYMMDD')) LJZDMJ, " +
					"jdb.gc_zsb_jdb_id, jdb.sbrq, jdb.bcwcjms, jdb.bcwcqys, jdb.bczdmj from gc_zsb_xxb xxb, GC_JH_SJ jh,gc_zsb_jdb jdb ";

			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("qy","QY");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询征收进度信息数据成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	/**
	 * 根据主键查询数据
	 * @param conn
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String queryJd(Connection conn,String id) throws Exception {
		String domresult = "";
		GcZsbJdbVO vo = new GcZsbJdbVO();
		vo.setGc_zsb_jdb_id(id);
		vo = (GcZsbJdbVO)BaseDAO.getVOByPrimaryKey(conn, vo);
		domresult = vo.getRowJson();
		return domresult;
	}
	
	@Override
	public String insertJdb(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = null;
		GcZsbJdbVO vo = new GcZsbJdbVO();
		GcZsbXxbVO zxmVO = new GcZsbXxbVO();
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			vo.setGc_zsb_jdb_id(new RandomGUID().toString()); // 主键
			vo.setYwlx(ywlx);
			EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			vo.setSjbh(event.getSjbh());
			BusinessUtil.setInsertCommonFields(vo,user);
			BaseDAO.insert(conn, vo);

			zxmVO.setValueFromJson((JSONObject)list.get(0));
			zxmVO.setGc_zsb_xxb_id(vo.getZdxxid());
			BusinessUtil.setUpdateCommonFields(zxmVO,user);
			BaseDAO.update(conn, zxmVO);

			//更新主表累计信息
			String updatesql = "update GC_ZSB_XXB t set LJJMZL = (select sum(BCWCJMS) from GC_ZSB_JDB where ZDXXID = t.GC_ZSB_XXB_ID and sfyx='1') ,WTJFX ='"+vo.getWtjfx() +"', LJQYZL =  (select sum(BCWCQYS) from GC_ZSB_JDB where ZDXXID = t.GC_ZSB_XXB_ID and sfyx='1'),LJZDMJ = (select sum(BCZDMJ) from GC_ZSB_JDB where ZDXXID = t.GC_ZSB_XXB_ID and sfyx='1') where t.GC_ZSB_XXB_ID ='"+vo.getZdxxid()+"'";
			DBUtil.execUpdateSql(conn, updatesql);
			
			
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
					+ "插入征收进度信息数据成功", user,"","");
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "插入征收进度信息数据失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_zsb_jdb_id()+"\",\"fieldname\":\"gc_zsb_jdb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String insert_result=this.queryJdb(request,jsona);
		return insert_result;
	}
	@Override
	public String updateJdb(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		GcZsbJdbVO vo = new GcZsbJdbVO();
		try {
			conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		BusinessUtil.setUpdateCommonFields(vo, user);
		//修改
		BaseDAO.update(conn, vo);
		String sql = "select WTJFX from(select WTJFX from GC_ZSB_JDB where zdxxid='"+vo.getZdxxid()+"' and sfyx='1' order by SBRQ desc) where rownum=1";
		String lastWtjfx = DBUtil.query(conn, sql)[0][0];
		
		
		//更新主表累计信息
		String updatesql = "update GC_ZSB_XXB t set LJJMZL = (select sum(BCWCJMS) from GC_ZSB_JDB where ZDXXID = t.GC_ZSB_XXB_ID and sfyx='1') ,WTJFX='"+lastWtjfx+"', LJQYZL =  (select sum(BCWCQYS) from GC_ZSB_JDB where ZDXXID = t.GC_ZSB_XXB_ID and sfyx='1'),LJZDMJ = (select sum(BCZDMJ) from GC_ZSB_JDB where ZDXXID = t.GC_ZSB_XXB_ID and sfyx='1') where t.GC_ZSB_XXB_ID ='"+vo.getZdxxid()+"'";
		DBUtil.execUpdateSql(conn, updatesql);
		
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), ywlx,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "修改征收进度信息数据成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改征收进度信息数据失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_zsb_jdb_id()+"\",\"fieldname\":\"gc_zsb_jdb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String update_result=this.queryJdb(request,jsona);
		return update_result;
	}
	
	//查找进度表基础信息
	@Override
	public String queryJdxx(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition += BusinessUtil.getSJYXCondition(null)+ BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select gc_zsb_jdb_id, zdxxid, sbrq, ljjmzl, ljqyzl, bcwcjms, bcwcqys, bczdmj, ljzdmj, wtjfx from gc_zsb_jdb";

			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询征收进度信息数据成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//删除功能
	
	@Override
	public String deleteJdxx(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZsbJdbVO vo = new GcZsbJdbVO();

		try {
		conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		BusinessUtil.setUpdateCommonFields(vo, user);
		//置成失效
        vo.setSfyx("0");
		//插入
		BaseDAO.update(conn, vo);
		resultVO = vo.getRowJson();
		String lastWtjfx="";
		String sql = "select WTJFX from(select WTJFX from GC_ZSB_JDB where zdxxid='"+vo.getZdxxid()+"' and sfyx='1' order by SBRQ desc) where rownum=1";
		String[][] arr=DBUtil.query(conn, sql);
		if(arr==null||arr.length==0){
			lastWtjfx="";
		}else{
			lastWtjfx = DBUtil.query(conn, sql)[0][0];
		}
		//更新主表累计信息
		String updatesql = "update GC_ZSB_XXB t set LJJMZL = (select sum(BCWCJMS) from GC_ZSB_JDB where ZDXXID = t.GC_ZSB_XXB_ID and sfyx='1') ,WTJFX='"+lastWtjfx+"', LJQYZL =  (select sum(BCWCQYS) from GC_ZSB_JDB where ZDXXID = t.GC_ZSB_XXB_ID and sfyx='1'),LJZDMJ = (select sum(BCZDMJ) from GC_ZSB_JDB where ZDXXID = t.GC_ZSB_XXB_ID and sfyx='1') where t.GC_ZSB_XXB_ID ='"+vo.getZdxxid()+"'";
		DBUtil.execUpdateSql(conn, updatesql);
		
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), ywlx,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "删除征收进度信息数据成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除征收进度信息数据成功", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	//获取当前时间下的征收进度信息
	@Override
	public String getZsjd(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String sbrq = request.getParameter("sbrq");
		String zdxxid=request.getParameter("zdxxid");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sql = "select GC_ZSB_JDB_ID from GC_ZSB_JDB where to_date(to_char(SBRQ,'YYYYMMDD'),'YYYYMMDD')=to_date('"+sbrq+"','YYYY-MM-DD') and sfyx='1' and zdxxid='"+zdxxid+"'";
			String[][] resArr = DBUtil.query(conn, sql);
			if(resArr!=null){
				domresult = resArr[0][0];
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}

