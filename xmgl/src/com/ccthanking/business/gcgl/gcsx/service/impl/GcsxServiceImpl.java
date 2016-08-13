package com.ccthanking.business.gcgl.gcsx.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.gcgl.gcqs.vo.GcqsVO;
import com.ccthanking.business.gcgl.gcsx.service.GcsxService;
import com.ccthanking.business.gcgl.gcsx.vo.GcsxVO;
import com.ccthanking.business.pqgl.vo.PqJzfkVO;
import com.ccthanking.business.pqgl.vo.PqZxmVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class GcsxServiceImpl implements GcsxService {
	
	private String ywlx = YwlxManager.GC_GCB_GCSX;

	@Override
	public String query(String json, HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		PageManager page = RequestUtil.getPageManager(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		String orderFilter = RequestUtil.getOrderFilter(json);
		condition += " AND t.jhsjid = t1.gc_jh_sj_id ";
		condition += " AND t.sjbh = t2.sjbh ";
		condition += BusinessUtil.getSJYXCondition("t");
        condition += BusinessUtil.getCommonCondition(user,"t");
        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);

			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"t1.gc_jh_sj_id,t1.nd,t1.xmbh,t1.xmmc,t1.bdbh,t1.bdmc,t2.sjzt,"
					+ "(case t1.xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) sgdw,"
					+ "(case t1.xmbs when '0' then (select SGDWXMJL from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select SGDWXMJL from GC_XMBD where GC_XMBD_ID = t1.bdid) end) SGDWXMJL,"
					+ "(case t1.xmbs when '0' then (select SGDWXMJLLXDH from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select SGDWXMJLLXDH from GC_XMBD where GC_XMBD_ID = t1.bdid) end) SGDWXMJLLXDH,"
					
					+ "(case t1.xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select jldw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) JLDW,"
					+ "(case t1.xmbs when '0' then (select JLDWZJ from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select JLDWZJ from GC_XMBD where GC_XMBD_ID = t1.bdid) end) JLDWZJ,"
					+ "(case t1.xmbs when '0' then (select JLDWZJLXDH from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select JLDWZJLXDH from GC_XMBD where GC_XMBD_ID = t1.bdid) end) JLDWZJLXDH,"
					+ "t.gc_gcgl_gcsx_id, t.bdid, t.jhsjid, t.xmid, t.sfsjpq, t.fqlx, t.xmglgs, t.xmglgsyzdb, t.xmglgsyzdblxfs, t.gcbyzdb, t.gcbyzdblxfs, t.gckgrq, t.sxsqrq, t.sxsqnr, t.ywlx, t.sjbh, t.bz, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx,t.sxmc, "+
					" (case t1.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = t1.nd and GC_TCJH_XMXDK_ID = t1.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = t1.bdid and rownum = 1) end ) as XMBDDZ "+
					" FROM " +
					"gc_jh_sj t1,gc_gcgl_gcsx t,FS_EVENT t2";
					
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//计划表
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			
			//字典
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			//bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDBH");
			bs.setFieldDic("SFSJPQ","SF");//是否下发
			bs.setFieldDic("SJZT","SJZT");//审批状态
			//字典
			bs.setFieldTranslater("GCBYZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
			
			//日期
			bs.setFieldDateFormat("GCKGRQ", "yyyy-MM-dd");
			bs.setFieldDateFormat("SXSQRQ", "yyyy-MM-dd");
			bs.setFieldSjbh("sjbh");
			
			// 录入人
			bs.setFieldUserID("LRR");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<工程洽商>", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	@Override
	public String insert(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		EventVO eventVO = null;
		GcsxVO vo = new GcsxVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			String ywid = request.getParameter("ywid");
			if(Pub.empty(vo.getGc_gcgl_gcsx_id())){
				if(Pub.empty(ywid)){
					vo.setGc_gcgl_gcsx_id(new RandomGUID().toString()); 
				}else{
					vo.setGc_gcgl_gcsx_id(ywid); 
					
				}
				
				vo.setYwlx(ywlx);
			    eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			    vo.setSjbh(eventVO.getSjbh());
			    FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
		        fvo.setGlid3(vo.getXmid()); //存入项目ID
		        fvo.setGlid4(vo.getBdid()); //存入标段ID
		        fvo.setYwlx(ywlx);
		        fvo.setSjbh(eventVO.getSjbh());
		        fvo.setFjzt("1");
		        FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_gcgl_gcsx_id(),user);
			    BusinessUtil.setInsertCommonFields(vo,user);
			    //add by cbl 审批用
			    vo.setConnection(conn);
			    //add by cbl
				BaseDAO.insert(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), ywlx,
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "新增<工程甩项>成功", user,"","");
			}else{
				//add by cbl 审批用
			    vo.setConnection(conn);
			    FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setGlid2("");//存入计划数据ID
		        fvo.setGlid3(vo.getGc_gcgl_gcsx_id()); //存入项目ID
		        fvo.setGlid4(""); //存入标段ID
		        FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_gcgl_gcsx_id());
			    //add by cbl
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "更新<工程甩项>成功", user,"","");
			}
		
			resultVO = vo.getRowJson();
			conn.commit();
			String jsona = Pub.makeQueryConditionByID(vo.getGc_gcgl_gcsx_id(), "t.GC_GCGL_GCSX_ID");
            return query(jsona, request);
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	//项目名称自动查询
		/* 
		 * 根据输入值从计划数据表自动模糊匹配项目名称
		 * @see com.ccthanking.business.tcjh.jhgl.service.TcjhService#xmmcAutoComplete(com.ccthanking.framework.model.autocomplete, com.ccthanking.framework.common.User)
		 */
		@Override
		public List<autocomplete> xmmcAutoCompleteByYw(autocomplete json,User user) throws Exception {
			List<autocomplete> autoResult = new ArrayList<autocomplete>(); 
			autocomplete ac = new autocomplete();
			String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
			condition += BusinessUtil.getSJYXCondition(null);
			condition += BusinessUtil.getCommonCondition(user,null);
			String [][] result = DBUtil.query("select distinct xmmc from "+json.getTableName()+" " + json.getTablePrefix() + " where " + condition);
	        if(null != result){
	        	for(int i =0;i<result.length;i++){
	        	  ac = new autocomplete();
	              ac.setRegionName(result[i][0]);
	              autoResult.add(ac);
	        	}
	        }
			return autoResult;
		}
	
		@Override
		public String delete(HttpServletRequest request, String json) throws Exception {
			Connection conn = null;
			String domresult = "";
			GcsxVO vo = new GcsxVO();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			try {
				conn = DBUtil.getConnection();
				conn.setAutoCommit(false);
				JSONArray list = vo.doInitJson(json);
				vo.setValueFromJson((JSONObject)list.get(0));
				//删业务数据
				GcsxVO delVO = new GcsxVO();
				delVO.setGc_gcgl_gcsx_id(vo.getGc_gcgl_gcsx_id());
				delVO.setSfyx("0");
				BaseDAO.update(conn, delVO);
				//删附件
				FileUploadVO fvo = new FileUploadVO();
				fvo.setYwid(vo.getGc_gcgl_gcsx_id());
				FileUploadService.deleteByConditionVO(conn, fvo);
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "工程甩项删除成功", user,"","");
				conn.commit();
			} catch (Exception e) {
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "工程甩项删除失败", user,"","");
				e.printStackTrace(System.out);
				DBUtil.rollbackConnetion(conn);
				throw e;
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
		
}
