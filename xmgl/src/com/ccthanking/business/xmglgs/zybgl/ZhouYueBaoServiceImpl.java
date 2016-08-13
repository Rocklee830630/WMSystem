package com.ccthanking.business.xmglgs.zybgl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccthanking.business.xdxmk.vo.XmxdkVO;
import com.ccthanking.business.zjb.jsgl.GcZjbJsbVO;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.common.vo.XmxxVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.log.log;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.UserService;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class ZhouYueBaoServiceImpl implements ZhouYueBaoService {



	@Override
	public String queryzhouYueBao(String json,String shijianid) throws SQLException {
		
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
       
		QueryConditionList list = RequestUtil.getConditionList(json);
		 
		PageManager page =  RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = list == null ? "" : list.getConditionWhere();
		
		if(!Pub.empty(condition)){
			condition +=" and xmxdk.id=zbb.xdkid and xmbd.id=zbb.bdid and kfg.bdid=xmbd.id and xmndb.xmbh(+)=xmxdk.xmbh and zbb.sjid='"+shijianid+"'";
			
		}else{
			condition="  xmxdk.id=zbb.xdkid and xmbd.id=zbb.bdid and and kfg.bdid=xmbd.id and xmndb.xmbh(+)=xmxdk.xmbh and zbb.sjid='"+shijianid+"'";
		}
		
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);

			conn.setAutoCommit(false);
			String sql = "select xmxdk.xmmc,xmxdk.id xmxdkid,xmbd.bdbh,xmbd.bdmc,xmbd.id xmbdid,xmbd.qdzh,xmndb.ndjsnr,kfg.kgsj,xmxdk.yzdb,xmxdk.sgdw,xmxdk.jldw," +
					"zbb.id id, zbb.sjid, zbb.xdkid, zbb.bdid, zbb.bzjh, zbb.bzwc, zbb.bnwc, zbb.ljwc, zbb.xzjh, zbb.gxmc, zbb.pqwcsj, " +
					" zbb.pabzjz, zbb.cqwmc, zbb.cqwcsj, zbb.cqbzjz,zbb.zjlbz, zbb.zjlnd, zbb.zjlljwc, zbb.qqwt, zbb.htzjwt, zbb.sjwt," +
					"zbb.zcwt, zbb.pqwt, zbb.fjid, zbb.note, zbb.lrrq, zbb.ywlx, zbb.sjbh," +
					" zbb.bz, zbb.lrr, zbb.lrsj, zbb.lrbm, zbb.gxr, zbb.gxsj, zbb.gxbm, zbb.sjmj, zbb.sfyx" +
					"  from gc_tcjh_xmxdk xmxdk ,gc_xmglgs_zbb zbb,gc_xmbd xmbd,gc_gcb_kfg kfg,GC_TCJH_XMNDB xmndb  ";
				
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ZT", "XMQK");
			//bs.setFieldDic("SFWT", "SF");
//			bs.setFieldDateFormat("IN_DATE", "yyyy-MM-dd");
//			bs.setFieldDic("CUST_TYPE", "KHLX");
//			bs.setFieldDic("CUST_STATE", "KHZT");
			domresult = bs.getJson();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			if (conn != null)
				conn.close();
		}
		return domresult;
	}

	@Override
	public String insert(String json, User user) throws SQLException, Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcXmglgsZbbVO xmvo = new GcXmglgsZbbVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		//设置主键
	
		xmvo.setId(new RandomGUID().toString()); // 主键
		//
		xmvo.setLrr(user.getAccount()); //更新人
		xmvo.setLrsj(Pub.getCurrentDate()); //更新时间
		xmvo.setLrbm(user.getDepartment());//录入人单位
		xmvo.setYwlx("ywlx");//业务类型
		EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
		xmvo.setSjbh(eventVO.getSjbh());

		//插入
		BaseDAO.insert(conn, xmvo);
		resultVO = xmvo.getRowJson();
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "操作结果描述请写在这里", user,"","");

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "操作结果描述请写在这里", user,"","");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultVO;
	}
	@Override
	public String updatedemo(String json, User user) throws SQLException,Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcXmglgsZbbVO xmvo = new GcXmglgsZbbVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));


		xmvo.setGxr(user.getAccount()); //更新人
		xmvo.setGxsj(Pub.getCurrentDate()); //更新时间
		xmvo.setGxbm(user.getDepartment());//录入人单位

		//插入
		BaseDAO.update(conn, xmvo);
		resultVO = xmvo.getRowJson();
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "操作结果描述请写在这里", user,"","");

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "操作结果描述请写在这里", user,"","");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultVO;
	}
	@Override
	public String queryConditionXiangMu(String json) throws SQLException {
		
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		QueryConditionList list = RequestUtil.getConditionList(json);
		PageManager page =  RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = list == null ? "" : list.getConditionWhere();
		
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);


	

			conn.setAutoCommit(false);
			String sql = "select * from GC_TCJH_XMXDK ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
//			bs.setFieldDateFormat("IN_DATE", "yyyy-MM-dd");
//			bs.setFieldDic("CUST_TYPE", "KHLX");
//			bs.setFieldDic("CUST_STATE", "KHZT");
			domresult = bs.getJson();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			if (conn != null)
				conn.close();
		}
		return domresult;
	}



	

	@Override
	public String queryBanLiXiangById(String ywid, User user) throws SQLException {
		
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
  /*     
		QueryConditionList list = RequestUtil.getConditionList(json);
		PageManager page = null;
		String orderFilter = RequestUtil.getOrderFilter(json);*/
		//String condition = list == null ? "" : list.getConditionWhere();
			PageManager page = null;
			String condition=" bz='0' and ywid='"+ywid+"'";
		
		if (page == null)
			page = new PageManager();
		    page.setFilter(condition);

            conn.setAutoCommit(false);
			String sql = "select * from gc_qqsx_bgl   ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			if (conn != null)
				conn.close();
		}
		return domresult;
	}

	@Override
	public String insertShiJian(String json, User user) throws Exception {
		
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcXmglgsSjbVO xmvo = new GcXmglgsSjbVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		//设置主键

	
		xmvo.setId(new RandomGUID().toString()); // 主键
		//
		xmvo.setLrr(user.getAccount()); //更新人
		xmvo.setLrsj(Pub.getCurrentDate()); //更新时间
		xmvo.setLrbm(user.getDepartment());//录入人单位
		xmvo.setYwlx("ywlx");//业务类型
		//EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
		//xmvo.setSjbh(eventVO.getSjbh());

		//插入
		BaseDAO.insert(conn, xmvo);
		resultVO = xmvo.getRowJson();
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "操作结果描述请写在这里", user,"","");

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "操作结果描述请写在这里", user,"","");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultVO;
	}

	@Override
	public String queryShiJian(String json) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		QueryConditionList list = RequestUtil.getConditionList(json);
		PageManager page =  RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = list == null ? "" : list.getConditionWhere();
	
		
		/*if(!Pub.empty(condition)){
			condition +=" and sj.xmbh=xmxdk.xmbh(+) and JLB.xdkid(+)=xmxdk.id and xmbd.xmbh(+)=xmxdk.xmbh  ";
			
		}else{
			condition=" sj.xmbh=xmxdk.xmbh(+) and JLB.xdkid(+)=xmxdk.id and xmbd.xmbh(+)=xmxdk.xmbh  ";
		}*/
		
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);

			conn.setAutoCommit(false);
			String sql = "select * from  GC_XMGLGS_SJB ";
				
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//bs.setFieldDic("SFWT", "SF");
//			bs.setFieldDateFormat("IN_DATE", "yyyy-MM-dd");
//			bs.setFieldDic("CUST_TYPE", "KHLX");
//			bs.setFieldDic("CUST_STATE", "KHZT");
			domresult = bs.getJson();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			if (conn != null)
				conn.close();
		}
		return domresult;
	}

	@Override
	public String updatedShiJian(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcXmglgsSjbVO xmvo = new GcXmglgsSjbVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));


		xmvo.setGxr(user.getAccount()); //更新人
		xmvo.setGxsj(Pub.getCurrentDate()); //更新时间
		xmvo.setGxbm(user.getDepartment());//录入人单位

		//插入
		BaseDAO.update(conn, xmvo);
		resultVO = xmvo.getRowJson();
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "操作结果描述请写在这里", user,"","");

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "操作结果描述请写在这里", user,"","");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultVO;
	}





	}


