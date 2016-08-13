package com.ccthanking.business.zjb.jswj;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccthanking.business.htgl.service.impl.GcHtglHtServiceImpl;
import com.ccthanking.business.htgl.vo.GcHtglHtsjVO;
import com.ccthanking.business.tcjh.jhgl.vo.FkqkVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.business.zjb.jsgl.GcZjbJsbVO;
import com.ccthanking.business.zjb.lbj.dyqk.GcZjbDyqkVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.common.vo.XmxxVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.UserService;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class ZaoJiaRenWuServiceImpl implements ZaoJiaRenWuService {
	private String ywlx=YwlxManager.GC_ZJB_JSWJ;
	@Override
	public String queryZaoJiaRenWu(String json, User user) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select gc_zjb_jswj_id, jsdate, nd, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx from gc_zjb_jswj ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldFileUpload("GC_ZJB_JSWJ_ID", "0072");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertZaoJiaRenWu(String json, User user, String ywid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjbJswjVO xmvo = new GcZjbJswjVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			
			xmvo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setInsertCommonFields(xmvo, user);
			xmvo.setYwlx(ywlx);//业务类型
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO.getSjbh());
			//设置主键
			if(!Pub.empty(ywid)){
				xmvo.setGc_zjb_jswj_id(ywid);
			    FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setYwlx(ywlx);
		        fvo.setSjbh(eventVO.getSjbh());
		        FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_zjb_jswj_id(),user);
			}else{
				xmvo.setGc_zjb_jswj_id(new RandomGUID().toString()); // 主键
			}
			//插入
			BaseDAO.insert(conn, xmvo);
			resultVO = xmvo.getRowJson();
		
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "结算文件管理添加成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zjb_jswj_id()+"\",\"fieldname\":\"gc_zjb_jswj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.queryZaoJiaRenWu(jsona,user);
			return resultXinXi;
	}
	@Override
	public String updateZaoJiaRenWu(String json, User user) {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjbJswjVO xmvo = new GcZjbJswjVO();

		try {
			conn.setAutoCommit(false);
		    JSONArray list = xmvo.doInitJson(json);
		    xmvo.setValueFromJson((JSONObject)list.get(0));
			//设置公共字段
			BusinessUtil.setUpdateCommonFields(xmvo, user);
	     	xmvo.setYwlx(ywlx);//业务类型
	     	//根据ID查YWID和SJBH
	     	GcZjbJswjVO vo1=(GcZjbJswjVO) BaseDAO.getVOByPrimaryKey(conn, xmvo);
			
	     	FileUploadVO fvo = new FileUploadVO();
	     	fvo.setFjzt("1");
	     	fvo.setYwlx(ywlx);
	     	fvo.setSjbh(vo1.getSjbh());
	     	FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_zjb_jswj_id(),user);
	     	
			//修改 
			BaseDAO.update(conn, xmvo);
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "结算文件管理修改成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zjb_jswj_id()+"\",\"fieldname\":\"gc_zjb_jswj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.queryZaoJiaRenWu(jsona,user);
			return resultXinXi;
	}
	}


