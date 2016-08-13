package com.ccthanking.business.sjgl.sjrws;

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
public  class renWuShuServiceImpl implements renWuShuService {
	private String ywlx=YwlxManager.GC_SJGL_RWSGL;
	

	@Override
	public String queryRenWuShu(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition +=" and rws.SJWYBH=jhsj.SJWYBH";
		condition += BusinessUtil.getSJYXCondition("RWS") + BusinessUtil.getCommonCondition(user,null);
		condition += BusinessUtil.getSJYXCondition("JHSJ") + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select rws.gc_sjgl_rwsgl_id,  rws.jhsjid, jhsj.SJWYBH, rws.nd,  rws.jsdw,  rws.fssj,  rws.jbr,  rws.rwslx,  rws.lb,  rws.ywlx,  rws.sjbh,  rws.bz," +
						"  rws.lrr,  rws.lrsj,  rws.lrbm,  rws.lrbmmc,  rws.gxr,  rws.gxsj,  rws.gxbm, rws.gxbmmc,  rws.sjmj,  rws.sfyx," +
						" jhsj.xmid,jhsj.bdid,jhsj.xmmc,jhsj.bdmc   from gc_sjgl_rwsgl rws,gc_jh_sj jhsj ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("RWSLX", "RWSLX");
			bs.setFieldDic("LB", "KCSJRWS");
			bs.setFieldTranslater("JBR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("JSDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询查询任务书成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertRenWuShu(String json,User user,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjglRwsglVO xmvo = new GcSjglRwsglVO();
		String ywid=request.getParameter("ywid");
		String fjbh=request.getParameter("fjbh");
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
					xmvo.setGc_sjgl_rwsgl_id(ywid);
					FileUploadVO fvo = new FileUploadVO();
			        fvo.setFjzt("1");//更新附件状态
			        fvo.setGlid2(xmvo.getJhsjid());//存入计划数据ID
			        fvo.setGlid3(xmvo.getXmid()); //存入项目ID
			        fvo.setGlid4(xmvo.getBdid()); //存入标段ID
			        fvo.setYwlx(xmvo.getYwlx()); 
			        fvo.setSjbh(xmvo.getSjbh()); 
			        fvo.setFjlb(fjbh);
			        FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_sjgl_rwsgl_id(),user);
			}else{
				xmvo.setGc_sjgl_rwsgl_id(new RandomGUID().toString()); // 主键
			}
			//插入
			BaseDAO.insert(conn, xmvo);
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "添加设计任务书成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_sjgl_rwsgl_id()+"\",\"fieldname\":\"gc_sjgl_rwsgl_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryRenWuShu(jsona,user);
		return resultXinXi;
	}

	@Override
	public String updateRenWuShu(String json,User user,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjglRwsglVO vo = new GcSjglRwsglVO();
		String fjbh=request.getParameter("fjbh");
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			vo.setYwlx(ywlx);
		
			//修改
			BaseDAO.update(conn, vo);
			
			//根据ID查YWID和SJBH
			GcSjglRwsglVO vo1=(GcSjglRwsglVO) BaseDAO.getVOByPrimaryKey(conn, vo);
			
			FileUploadVO fvo = new FileUploadVO();
			fvo.setFjzt("1");
			fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
			fvo.setGlid3(vo.getXmid()); //存入项目ID
			fvo.setGlid4(vo.getBdid()); //存入标段ID
	        fvo.setYwlx(vo1.getYwlx()); 
	        fvo.setSjbh(vo1.getSjbh()); 
	        fvo.setFjlb(fjbh);
			FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_sjgl_rwsgl_id(),user);
			
			resultVO = vo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改设计任务书成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_sjgl_rwsgl_id()+"\",\"fieldname\":\"gc_sjgl_rwsgl_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryRenWuShu(jsona,user);
		return resultXinXi;
			//return resultVO;
	}
	@Override
	public String deleteRenWuShu(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjglRwsglVO vo = new GcSjglRwsglVO();

		try {
		conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		BusinessUtil.setUpdateCommonFields(vo, user);
		vo.setYwlx(ywlx);
		//置成失效
        vo.setSfyx("0");
		//插入
		BaseDAO.update(conn, vo);
		resultVO = vo.getRowJson();
		
		
		//删除附件
		FileUploadVO fvo = new FileUploadVO();
		fvo.setYwid(vo.getGc_sjgl_rwsgl_id());
		FileUploadService.deleteByConditionVO(conn, fvo);
		
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "删除设计任务书成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_sjgl_rwsgl_id()+"\",\"fieldname\":\"gc_sjgl_rwsgl_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryRenWuShu(jsona,user);
		return resultXinXi;
	}

}
