

package com.ccthanking.framework.demo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.UserService;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class DemoServiceImpl implements DemoService {


	//@Autowired
	//private UserManager userMapper;


	@Override
	public String insertdemo(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		XmxxVO xmvo = new XmxxVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		//设置主键
		xmvo.setId(new RandomGUID().toString()); // 主键
		//
		xmvo.setTbr(user.getAccount()); //更新人
		xmvo.setTbsj(Pub.getCurrentDate()); //更新时间
		xmvo.setTbdw(user.getDepartment());//录入人单位
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
	public String updatedemo(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		XmxxVO xmvo = new XmxxVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));


		xmvo.setGxr(user.getAccount()); //更新人
		xmvo.setGxsj(Pub.getCurrentDate()); //更新时间
		xmvo.setGxdw(user.getDepartment());//录入人单位


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
	public String deletedemo(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		XmxxVO xmvo = new XmxxVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		//删除
		BaseDAO.delete(conn, xmvo);
		resultVO = xmvo.getRowJson();//返回值可以自定义
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "操作结果描述请写在这里", user,"","");

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE,
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
	public String updatebatchdemo(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		
		String resultVO = "";

		try {
			conn.setAutoCommit(false);
			XmxxVO xmvou = new XmxxVO();
		JSONArray list = xmvou.doInitJson(json);
		
		List<String> listrow= new ArrayList<String>(); 
		
		
		for(int i=0;i<list.size();i++){
			
			XmxxVO xmvo = new XmxxVO();
			
			
			xmvo.setValueFromJson((JSONObject)list.get(i));


			xmvo.setGxr(user.getAccount()); //更新人
			xmvo.setGxsj(Pub.getCurrentDate()); //更新时间
			xmvo.setGxdw(user.getDepartment());//录入人单位


			//返回的记录存入listrow
			listrow.add(xmvo.getRowJsonSingle());
			//更新操作
			BaseDAO.update(conn, xmvo);
			conn.commit();
			
		}
		//
		resultVO = BaseDAO.comprisesResponseData(conn, listrow);
		/*LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "更新样例页面成功", user,"","");*/
		
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

	@Override
	public String queryConditiondemo(String json,User user) throws Exception {
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
			String sql = "select * from XMXX";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			  bs.setFieldFileUpload("XMLY","");
			  bs.setFieldDecimals("GS");
			  bs.setFieldSjbh("SJBH");
//			  bs.setFieldFileUpload("BZ");

			//bs.setFieldDateFormat("XMQZD", "yyyy-MM-dd");

			// bs.setFieldDateFormat("IN_DATE", "yyyy-MM-dd");
			// bs.setFieldDic("CUST_TYPE", "KHLX");
			 bs.setFieldDic("XMNF", "XMNF");
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
	public User findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<User> find() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<User> find(List<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int remove(int id) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int remove(List<Integer> ids) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int update(User bean) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int insert(User bean) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public User getUserByUsernameAndPassword(String username, String password)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String queryAttchdemo(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		QueryConditionList list = RequestUtil.getConditionList(json);
		PageManager page =  RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = list == null ? "" : list.getConditionWhere();
		condition += "	and regexp_like(filename,'(.doc)$|(.docx)$|(.xls)$|(.xlsx)$|(.ppt)$|(.pptx)$')";
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select fileid, filename, url, zhux, bz, lrr, lrbm, lrsj, gxr, gxbm, gxsj, sjbh, ywlx, fjlx, filesize, ywid, fjzt, glid1, glid2, glid3, glid4, fjlb from fs_fileupload";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			  bs.setFieldSjbh("SJBH");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			if (conn != null)
				conn.close();
		}
		return domresult;
	}
}
