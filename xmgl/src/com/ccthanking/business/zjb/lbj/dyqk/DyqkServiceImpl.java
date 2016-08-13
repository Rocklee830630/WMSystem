package com.ccthanking.business.zjb.lbj.dyqk;

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
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.business.zjb.lbj.GcZjbLbjbVO;
import com.ccthanking.business.zjb.lbj.dyqk.*;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;


@Service
public  class DyqkServiceImpl implements DyqkService {
	private String ywlx=YwlxManager.GC_ZJB_LBJ;
	

	@Override
	public String queryConditionDyqk(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition += "and A.SJWYBH(+)=jhsj.SJWYBH  ";
		condition += BusinessUtil.getSJYXCondition("A") + BusinessUtil.getCommonCondition(user,null);
		condition += BusinessUtil.getSJYXCondition("JHSJ") + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select gc_zjb_dyqk_id, lbjid, jhsj.jhid, jhsj.Gc_Jh_Sj_Id jhsjid, jhsj.nd, jhsj.xmid, jhsj.bdid, twrq, dyrq, a.bz, a.lrsj from gc_zjb_dyqk A,gc_jh_sj  jhsj";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询拦标价答疑成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertDyqk(String json,User user,String ywid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjbDyqkVO xmvo = new GcZjbDyqkVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		
		BusinessUtil.setInsertCommonFields(xmvo, user);
		xmvo.setYwlx(ywlx);
		EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
		xmvo.setSjbh(eventVO.getSjbh());
		
		//设置主键
		if(!Pub.empty(ywid)){
				xmvo.setGc_zjb_dyqk_id(ywid);
			
				FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setGlid2(xmvo.getJhsjid());//存入计划数据ID
		        fvo.setGlid3(xmvo.getXmid()); //存入项目ID
		        fvo.setGlid4(xmvo.getBdid()); //存入标段ID
		        fvo.setYwlx(xmvo.getYwlx()); 
		        fvo.setSjbh(xmvo.getSjbh()); 
		        
		        FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_zjb_dyqk_id(),user);

		/*	FileUploadService.updateFjztByYwid(conn, ywid);*/
		}else{
			xmvo.setGc_zjb_dyqk_id(new RandomGUID().toString()); // 主键
		}

		//插入
		BaseDAO.insert(conn, xmvo);
		resultVO = xmvo.getRowJson();
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "插入拦标价答疑成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String updateDyqk(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjbDyqkVO vo = new GcZjbDyqkVO();

		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			vo.setYwlx(ywlx);
		
			//插入
			BaseDAO.update(conn, vo);
			//根据ID查YWID和SJBH
			GcZjbDyqkVO vo1=(GcZjbDyqkVO) BaseDAO.getVOByPrimaryKey(conn, vo);
			FileUploadVO fvo = new FileUploadVO();
			fvo.setFjzt("1");
			fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
			fvo.setGlid3(vo.getXmid()); //存入项目ID
			fvo.setGlid4(vo.getBdid()); //存入标段ID
	        fvo.setYwlx(vo1.getYwlx()); 
	        fvo.setSjbh(vo1.getSjbh()); 
			FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_zjb_dyqk_id(),user);
			
			resultVO = vo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改拦标价答疑成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return resultVO;
	}
	@Override
	public String deleteDyqk(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjbDyqkVO vo = new GcZjbDyqkVO();

		try {
		conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		BusinessUtil.setUpdateCommonFields(vo, user);
		vo.setYwlx(ywlx);
		//EventVO eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
		//vo.setSjbh(eventVO.getSjbh());
		//置成失效
        vo.setSfyx("0");
		//插入
		BaseDAO.update(conn, vo);
		resultVO = vo.getRowJson();
		//删除附件
		FileUploadVO fvo = new FileUploadVO();
		fvo.setYwid(vo.getGc_zjb_dyqk_id());
		FileUploadService.deleteByConditionVO(conn, fvo);
		
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "删除拦标价答疑成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

}
