package com.ccthanking.business.sjgl.bgsf;

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
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.message.messagemgr.sendMessage;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.business.sjgl.bgsf.GcSjBgsfJsService;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.business.sjgl.bgsf.GcSjBgsfJsVO;
import com.ccthanking.business.sjgl.xgzllq.GcSjXgzlLqVO;
import com.ccthanking.business.sjgl.sj.GcSjVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;


@Service
public  class GcSjBgsfJsServiceImpl implements GcSjBgsfJsService {
	private String ywlx=YwlxManager.GC_SJ_BGSF_JS;
	private String yw=YwlxManager.GC_SJ_XGZL_LQ;
	@Override
	public String querySf(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition += " and zj.SJWYBH(+)=jhsj.SJWYBH";
		condition += BusinessUtil.getSJYXCondition("jhsj") + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
		    page.setFilter(condition); 
			conn.setAutoCommit(false);
			String sql="select distinct jhsj.xmbs,jhsj.SJWYBH,jhsj.pxh,jhsj.xmbh,jhsj.xmmc, jhsj.bdmc, jhsj.bdbh, jhsj.jhid, jhsj.lrsj, jhsj.gc_jh_sj_id as jhsjid, jhsj.nd, jhsj.xmid, jhsj.bdid," +
					"zj.zjjc,zj.zjjcrq,zj.hftsjc,zj.hftsjcrq,zj.djzsyjc,zj.djzsyjcrq," + 
					"(case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ " + 
							"from (select SJWYBH, " +
							"max(decode(bglb, '0300', SJWYBH, '')) zjjc," +
							"max(decode(bglb, '0300', jsrq, '')) zjjcrq, " +
							"max(decode(bglb, '0301', SJWYBH, '')) as hftsjc," +
							"max(decode(bglb, '0301', jsrq, '')) hftsjcrq, " +
							"max(decode(bglb, '0302', SJWYBH, '')) djzsyjc," +
							"max(decode(bglb, '0302', jsrq, '')) djzsyjcrq " +
							"from GC_SJ_BGSF_JS where sfyx='1'" +
							"  group by SJWYBH) zj, GC_JH_SJ jhsj " ;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldFileUploadWithWybh("ZJJC", "0300");
			bs.setFieldFileUploadWithWybh("HFTSJC", "0301");
			bs.setFieldFileUploadWithWybh("DJZSYJC", "0302");
			bs.setFieldDic("BGLB", "BGLB");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询检测检测（接收）信息成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String queryJs(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
		    page.setFilter(condition); 
			conn.setAutoCommit(false);
			String sql = "select bgsf.jhsjid, bgsf.bglb,bgsf.gc_sj_bgsf_js_id,bgsf.jsrq,bgsf.jsr,bgsf.fs,bgsf.bz,bgsf.sjbh from gc_sj_bgsf_js bgsf";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("BGLB", "BGLB");
			bs.setFieldTranslater("JSR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询检测检测（接收）信息成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String insertSf(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String ywid=request.getParameter("ywid");
		String xmmc=request.getParameter("xmmc");
		String bdmc=request.getParameter("bdmc");
		String sj_id="";
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjBgsfJsVO xmvo = new GcSjBgsfJsVO();
		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		if(Pub.empty(ywid)){
		//设置主键
			ywid=new RandomGUID().toString();
		}
		EventVO eventVO = EventManager.createEvent(conn, ywlx, user);//生成事件
		BusinessUtil.setInsertCommonFields(xmvo, user);
		xmvo.setGc_sj_bgsf_js_id(ywid); // 主键
		int fs=Integer.parseInt(xmvo.getFs());
		//int fs=Integer.valueOf(xmvo.getFs()).intValue();
		xmvo.setFs(String.valueOf(fs));
		xmvo.setYwlx(ywlx);
		xmvo.setSjbh(eventVO.getSjbh());
		BaseDAO.insert(conn, xmvo);
		resultVO = xmvo.getRowJson();
		
		//附件信息
		FileUploadVO vo = new FileUploadVO();
		vo.setYwid(xmvo.getJhsjid());
		vo.setGlid1(xmvo.getGc_sj_bgsf_js_id());
		vo.setGlid2(xmvo.getJhsjid());
		vo.setGlid3(xmvo.getXmid());
		vo.setGlid4(xmvo.getBdid());
		vo.setSjbh(eventVO.getSjbh());
		vo.setFjlb(xmvo.getBglb());
		vo.setYwlx(ywlx);
		vo.setFjzt("1");
		FileUploadService.updateVOByYwid(conn,vo,xmvo.getGc_sj_bgsf_js_id(),user);
		
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "插入报告收发（接收）信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "插入报告收发（接收）信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String updateSf(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjBgsfJsVO vo = new GcSjBgsfJsVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		BusinessUtil.setUpdateCommonFields(vo, user);
		//插入
		BaseDAO.update(conn, vo);
		resultVO = vo.getRowJson();
		
		//附件操作
		FileUploadVO fvo = new FileUploadVO();
		fvo.setFjzt("1");
		fvo.setYwid(vo.getJhsjid());
		fvo.setGlid1(vo.getGc_sj_bgsf_js_id());
		fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
		fvo.setGlid3(vo.getXmid()); //存入项目ID
		fvo.setGlid4(vo.getBdid());			//存入标段ID
		fvo.setFjlb(vo.getBglb());
		FileUploadService.updateVOByGlid1(conn, fvo, vo.getGc_sj_bgsf_js_id(),user);
		
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), ywlx,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "修改报告收发（接收）信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改报告收发（接收）信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	@Override
	public String querySfxx(HttpServletRequest request,String json) throws SQLException {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String ZLLJSD=request.getParameter("ZLLJSD");
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition +=" and ZLLB='03' and ZLLJSD='"+ZLLJSD+"'";
		condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		    page.setFilter(condition);
 
			conn.setAutoCommit(false);
			String sql = "select gc_sj_xgzl_lq_id, zlljsd, zllb, jhid, jhsjid, nd, bdid, xmid, fs, lqbm, lqrq, lqr, blr, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx from gc_sj_xgzl_lq";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldTranslater("LQBM", "FS_ORG_DEPT", "ROW_ID", "DEPT_NAME");
			bs.setFieldTranslater("LQR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("BLR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldDic("BGLB", "BGLB");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, yw,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询报告收发（领取）信息成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertSfxx(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjXgzlLqVO xmvo = new GcSjXgzlLqVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		//设置主键
		xmvo.setGc_sj_xgzl_lq_id(new RandomGUID().toString()); // 主键
		BusinessUtil.setInsertCommonFields(xmvo, user);
		int fs=Integer.parseInt(xmvo.getFs());
		//int fs=Integer.valueOf(xmvo.getFs()).intValue();
		xmvo.setFs(String.valueOf(fs));
		xmvo.setYwlx(yw);
		xmvo.setZllb("03");
		EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
		xmvo.setSjbh(eventVO.getSjbh());

		//插入
		BaseDAO.insert(conn, xmvo);
		resultVO = xmvo.getRowJson();
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "添加报告收发（领取）信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "添加报告收发（领取）信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String updateSfxx(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjXgzlLqVO vo = new GcSjXgzlLqVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		BusinessUtil.setUpdateCommonFields(vo, user);

		//插入
		BaseDAO.update(conn, vo);
		resultVO = vo.getRowJson();
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), yw,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "修改报告收发（领取）信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), yw,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改报告收发（领取）信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String deleteJS(HttpServletRequest request, String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjBgsfJsVO vo = new GcSjBgsfJsVO();

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
			//删除附件
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getGc_sj_bgsf_js_id());
			FileUploadService.deleteByConditionVO(conn, fvo);
			
			// 删除领取信息
			GcSjXgzlLqVO volq = new GcSjXgzlLqVO();
			volq.setZlljsd(vo.getGc_sj_bgsf_js_id());
			BaseVO[] lqs=(BaseVO[]) BaseDAO.getVOByCondition(conn, volq);
			if(null!=lqs)
			{
				for(int i=0;i<lqs.length;i++)
				{
					GcSjXgzlLqVO lqvo=(GcSjXgzlLqVO) lqs[i];
					lqvo.setSfyx("0");
					BaseDAO.update(conn, lqvo);
				}
			}
			
			//删除手续，附件删除
			FileUploadVO condVO=new FileUploadVO();
			String del_id=vo.getGc_sj_bgsf_js_id();
			condVO.setGlid1(del_id);
			condVO.setFjlb(vo.getBglb());
			FileUploadService.deleteByConditionVO(conn,condVO);
			
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), yw,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除监测检测（接收）信息成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		return resultVO;
	}

	@Override
	public String deleteLQ(HttpServletRequest request, String json) {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjXgzlLqVO vo = new GcSjXgzlLqVO();

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
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), yw,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除监测检测（领取）信息成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		return resultVO;
	}

}
