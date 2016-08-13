package com.ccthanking.business.xtbg.ndxxgx;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
public class NdxxgxServiceImpl implements NdxxgxService {

	private String ywlx=YwlxManager.XTBG_XXZX_NDXXGX;
	@Override
	public String queryNdxxgx(String json,User user) throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += " and LRBM='"+user.getDepartment()+"' ";
		    condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = " SELECT XTBG_XXZX_NDXXGX_ID, NDXXBT, NR, FBR, FBSJ, ISFB, YWLX, SJBH, BZ, LRR, LRSJ, LRBM, LRBMMC, GXR, GXSJ, GXBM, GXBMMC, SJMJ, SFYX FROM XTBG_XXZX_NDXXGX ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ISFB", "SF");
			bs.setFieldClob("NR");
			bs.setFieldTranslater("FBR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryMoreNdxxgx(String json,HttpServletRequest request) throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += " and A.XTBG_XXZX_NDXXGX_ID=B.GGID ";
		    condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = " SELECT XTBG_XXZX_NDXXGX_ID, NDXXBT,  FBR, FBSJ, ISFB, YWLX, SJBH, BZ, LRR, LRSJ, LRBM, LRBMMC, GXR, GXSJ, GXBM, GXBMMC, SJMJ, SFYX,B.SFYD FROM XTBG_XXZX_NDXXGX A " +
					",(select GGID,SFYD from XTBG_XXZX_GGTZ_FB where JSR_ACCOUNT='"+user.getAccount()+"' and SFYX='1' and SHZT='1') B";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ISFB", "SF");
			bs.setFieldTranslater("FBR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertNdxxgx(String json, User user,String ywid) throws Exception {
		Connection conn = null;
		String resultVO = null;
		XtbgXxzxNdxxgxVO xmvo = new XtbgXxzxNdxxgxVO();
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			
			xmvo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setInsertCommonFields(xmvo, user);
			xmvo.setYwlx(ywlx);//业务类型
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO.getSjbh());
			String ggid = "";
			//设置主键
			if(!Pub.empty(ywid)){
				ggid = ywid;
				xmvo.setXtbg_xxzx_ndxxgx_id(ggid);
			    FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setYwlx(ywlx);
		        fvo.setSjbh(eventVO.getSjbh());
		        FileUploadService.updateVOByYwid(conn, fvo, xmvo.getXtbg_xxzx_ndxxgx_id(),user);
			}else{
				ggid = new RandomGUID().toString();
				xmvo.setXtbg_xxzx_ndxxgx_id(ggid); // 主键
			}
			xmvo.setFbr(user.getAccount());
			//插入
			BaseDAO.insert(conn, xmvo);
			resultVO = xmvo.getRowJson();
			insertFb(ywid,conn);
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "年度信息共享添加成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return resultVO;
	}

	
	@Override
	public String updateNdxxgx(String json, User user) throws Exception {
		Connection conn = null;
		String resultVO = null;
		XtbgXxzxNdxxgxVO xmvo = new XtbgXxzxNdxxgxVO();

		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
		    JSONArray list = xmvo.doInitJson(json);
		    xmvo.setValueFromJson((JSONObject)list.get(0));
			//设置公共字段
			BusinessUtil.setUpdateCommonFields(xmvo, user);
	     	xmvo.setYwlx(ywlx);//业务类型
	     	//根据ID查YWID和SJBH
	     	XtbgXxzxNdxxgxVO vo1=(XtbgXxzxNdxxgxVO) BaseDAO.getVOByPrimaryKey(conn, xmvo);
	     	FileUploadVO fvo = new FileUploadVO();
	     	fvo.setFjzt("1");
	     	fvo.setYwlx(ywlx);
	     	fvo.setSjbh(vo1.getSjbh());
	     	FileUploadService.updateVOByYwid(conn, fvo, xmvo.getXtbg_xxzx_ndxxgx_id(),user);
			//修改 
			BaseDAO.update(conn, xmvo);
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "年度信息共享修改成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return resultVO;
	}
	
	@Override
	public String deleteNdxxgx(String json, User user) {
		Connection conn = null;
		String resultVO = null;
		XtbgXxzxNdxxgxVO vo = new XtbgXxzxNdxxgxVO();

		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			vo.setYwlx(ywlx);
			//置成失效
	        vo.setSfyx("0");
			//插入
			BaseDAO.update(conn, vo);
			//附件删除
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getXtbg_xxzx_ndxxgx_id());
			FileUploadService.deleteByConditionVO(conn, fvo);
			
			resultVO = vo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "年度信息共享删除成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return resultVO;
	}
	private void insertFb(String ggid,Connection conn) throws Exception{
		String sql = "insert into XTBG_XXZX_GGTZ_FB(FBID,GGID,GGBT,GGLB,FBR,FBBM,FBBMMC,FBSJ,NF,JSR_ACCOUNT,JSR,JSBM,JSBMMC,SFYD,SFYX,YDSJ,FBFWMC,SHZT,LRSJ) " +
				"select sys_GUID(),C.XTBG_XXZX_NDXXGX_ID,C.NDXXBT,'ND',C.FBR,C.LRBM,C.LRBMMC,C.FBSJ,to_char(C.FBSJ,'yyyy')," +
				"A.ACCOUNT,A.NAME,B.ROW_ID,B.DEPT_NAME,'0','1',null,'全部','1',sysdate " +
				"from VIEW_YW_ORG_PERSON A," +
				"FS_ORG_DEPT B," +
				"(select * from XTBG_XXZX_NDXXGX WHERE XTBG_XXZX_NDXXGX_ID = '"+ggid+"') C where A.DEPARTMENT=B.ROW_ID";
		DBUtil.execSql(conn, sql);
	}
}


