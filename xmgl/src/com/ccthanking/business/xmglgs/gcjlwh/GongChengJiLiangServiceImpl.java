package com.ccthanking.business.xmglgs.gcjlwh;

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
public class GongChengJiLiangServiceImpl implements GongChengJiLiangService {



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
			condition +=" and sj.xmbh(+)=xmxdk.xmbh and JLB.xdkid=xmxdk.id(+) and xmbd.xmbh(+)=xmxdk.xmbh and jlb.sjid='"+shijianid+"' ";
			
		}else{
			condition="  sj.xmbh(+)=xmxdk.xmbh and JLB.xdkid=xmxdk.id(+) and xmbd.xmbh(+)=xmxdk.xmbh  and jlb.sjid='"+shijianid+"' ";
		}
		
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);

			conn.setAutoCommit(false);
			String sql = "select JLB.id,JLB.sjid,xmxdk.xmmc,xmxdk.id XDKID,xmxdk.jldw,xmxdk.sgdw,xmbd.id BDID,xmbd.bdmc,JLB.dyjlsdz,JLB.ljjlsdz,JLB.dyzbj,JLB.ljdyyfk,JLB.jzjlz,JLB.ljjlz,JLB.LJZBJ,JLB.DYYFK,JLB.wgbfb,JLB.sfys,JLB.jlrq,JLB.jgzt,JLB.jgztt,JLB.xjxj  from gc_jh_sj sj,gc_tcjh_xmxdk xmxdk,GC_XMGLGS_JLB JLB,gc_xmbd xmbd "; 
				
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("SFYS", "SFYS");
			bs.setFieldDic("XJXJ", "XMXZ");
			bs.setFieldDic("JGZT", "SF");
			bs.setFieldDic("JGZTT", "SF");

			
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
		GcXmglgsJlbVO xmvo = new GcXmglgsJlbVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
	
		xmvo.setValueFromJson((JSONObject)list.get(0));
		//设置主键
		if(Pub.empty(xmvo.getSfys()))
		{
			xmvo.setSfys("1");
		}
		if(Pub.empty(xmvo.getJgzt()))
		{
			xmvo.setJgzt("0");
		}
		if(Pub.empty(xmvo.getJgztt()))
		{
			xmvo.setJgztt("0");
		}
		if(Pub.empty(xmvo.getXjxj()))
		{
			xmvo.setXjxj("2");
		}
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
		GcXmglgsJlbVO xmvo = new GcXmglgsJlbVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		if(Pub.empty(xmvo.getSfys()))
		{
			xmvo.setSfys("1");
		}
		if(Pub.empty(xmvo.getJgzt()))
		{
			xmvo.setJgzt("0");
		}
		if(Pub.empty(xmvo.getJgztt()))
		{
			xmvo.setJgztt("0");
		}
		if(Pub.empty(xmvo.getXjxj()))
		{
			xmvo.setXjxj("2");
		}
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
			 /*	QueryConditionList list = RequestUtil.getConditionList(json);
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
		GcXmglgsJlyfbVO xmvo = new GcXmglgsJlyfbVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		//设置主键
//		String aa=xmvo.getSfwt();
	
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
		PageManager page = null;
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
			String sql = "select * from  GC_XMGLGS_JLYFB ";
				
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
		GcXmglgsJlyfbVO xmvo = new GcXmglgsJlyfbVO();

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


