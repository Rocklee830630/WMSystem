

package com.ccthanking.business.qqsx.sxfj;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.ccthanking.business.qqsx.sxfj.GcQqsxSxfjService;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.business.qqsx.sxfj.GcQqsxSxfjVO;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.EventVO;


@Service
public  class GcQqsxSxfjServiceImpl implements GcQqsxSxfjService {

	
	@Override
	public String queryConditionSxfj(String json) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		QueryConditionList list = RequestUtil.getConditionList(json);
		PageManager page = null;
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = list == null ? "" : list.getConditionWhere();
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql="select * from GC_QQSX_SXFJ";
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
	public String insertSxfj(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcQqsxSxfjVO xmvo = new GcQqsxSxfjVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		//设置主键
		xmvo.setId(new RandomGUID().toString()); // 主键
		//
		xmvo.setGxr(user.getAccount()); //更新人
		xmvo.setGxsj(Pub.getCurrentDate()); //更新时间
		xmvo.setLrbm(user.getDepartment());//录入人单位
		xmvo.setYwlx("ywlx");
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
	public String updateSxfj(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcQqsxSxfjVO vo = new GcQqsxSxfjVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		//设置主键
		/*vo.setId(new RandomGUID().toString()); // 主键
*/		/*vo.setId("20FC7EF9-696D-6398-15C8-A77F2C4DFC02");*/
		//
		vo.setGxr(user.getAccount()); //更新人
		vo.setGxsj(Pub.getCurrentDate()); //更新时间
		vo.setLrbm(user.getDepartment());//录入人单位
		vo.setYwlx("ywlx");
		EventVO eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
		vo.setSjbh(eventVO.getSjbh());

		//插入
		BaseDAO.update(conn, vo);
		resultVO = vo.getRowJson();
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "操作结果描述请写在这里", user,"","");

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
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

/*	@Override
	public String querySxfj(String json) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}*/
}
