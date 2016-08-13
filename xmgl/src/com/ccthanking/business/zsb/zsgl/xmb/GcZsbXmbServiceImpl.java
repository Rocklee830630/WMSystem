package com.ccthanking.business.zsb.zsgl.xmb;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.tcjh.jhgl.vo.FkqkVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public  class GcZsbXmbServiceImpl implements GcZsbXmbService {
	private String ywlx=YwlxManager.GC_ZS_XM;
//初始进入的查询方法
	@Override
	public String queryConditionXmb(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition=condition+" and xmb.jhsjid(+)=jhsj.gc_jh_sj_id and jhsj.bdid is null and qqsx.jhsjid(+)=jhsj.gc_jh_sj_id";
		condition += BusinessUtil.getSJYXCondition("jhsj") + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			//添加伪装数据sjmj作为前期手续的内容
			String sql = "select xmb.GC_ZSB_XMB_ID,xmb.wtjfx,xmb.sjbh,xmb.cdsjsj,xmb.lrsj,xmb.isfk,jhsj.sjmj,jhsj.pxh,jhsj.gc_jh_sj_id as jhsjid," +
					"jhsj.nd,jhsj.jhid,jhsj.xmid,jhsj.xmmc,jhsj.xmbh,jhsj.ISZC as SFCQ_JH,xmb.SFCQ as SFCQ_SJ ,jhsj.CQT_SJ as CQTQDSJ,jhsj.ZC as JHWCSJ,jhsj.ZC_SJ as JHWCRQ,xmb.SJWCRQ, " +
					" qqsx.ghxkz,qqsx.tdsyz,qqsx.sfgh,qqsx.sftd,qqsx.jhsjid as mark, " +
					"(case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ " +
					"from GC_JH_SJ jhsj, GC_ZSB_XMB xmb," +
					"(select distinct sx.ghspbjsj as ghxkz,sx.tdspbjsj as tdsyz,decode(sx.ghspbblsx,'0008',sx.jhsjid,'') as sfgh," +
					"decode(sx.tdspbblsx,'0014',sx.jhsjid,'') as sftd,sx.jhsjid from gc_zgb_qqsx sx where sx.sfyx='1') qqsx";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("SFCQ_JH","SF");
			bs.setFieldDic("SFCQ_SJ","SF");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询征收项目信息成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertXmb(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZsbXmbVO xmvo = new GcZsbXmbVO();
		TcjhVO jhVO = new TcjhVO();
		
		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		//设置主键
		xmvo.setGc_zsb_xmb_id(new RandomGUID().toString()); // 主键
		BusinessUtil.setInsertCommonFields(xmvo, user);
		xmvo.setYwlx(ywlx);
		xmvo.setIsfk("0");
		EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
		xmvo.setSjbh(eventVO.getSjbh());
		//插入
		BaseDAO.insert(conn, xmvo);
		resultVO = xmvo.getRowJson();
		
		String upsql="update gc_zsb_xxb xxb set xxb.zsxmid='"+xmvo.getGc_zsb_xmb_id()+"' where  xxb.jhsjid='"+xmvo.getJhsjid()+"' and xxb.sfyx='1' ";
		DBUtil.execUpdateSql(conn, upsql);
		
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "插入征收项目信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "插入征收项目信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		/*return resultVO;*/
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zsb_xmb_id()+"\",\"fieldname\":\"gc_zsb_xmb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String insert_result=this.queryConditionXmb(request,jsona);
		return insert_result;
	}

	@Override
	public String updateXmb(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String result = null;
		GcZsbXmbVO vo = new GcZsbXmbVO();
		TcjhVO jhVO = new TcjhVO();
		try {
			conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		BusinessUtil.setUpdateCommonFields(vo, user);
		vo.setIsfk("0");
		BaseDAO.update(conn, vo);
		result = vo.getRowJson();
		
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), ywlx,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "修改征收项目信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改征收项目信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
//		return result;
		String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_zsb_xmb_id()+"\",\"fieldname\":\"gc_zsb_xmb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String update_result =this.queryConditionXmb(request,jsona);
		return update_result;
	}
	//综合信息页面，征拆进度查询
	@Override
	public String queryJdxx(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		//condition=condition+" and  xmb.jhsjid=xxb.jhsjid";
		condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select xxb.cqrwmc, xxb.qy,  xxb.lrsj,  xxb.zrdw,  xxb.jmhs, xxb.qyjs, xxb.zmj, xxb.ljjmzl,xxb.fzr, " +
					"xxb.ljqyzl, xxb.ljzdmj, xxb.wtjfx,xxb.cdyjrq,xxb.tdfwyjrq from Gc_Zsb_Xxb xxb";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("QY", "QY");
			bs.setFieldTranslater("FZR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询征收项目信息成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//弹出页查询，查询征拆项目信息
	@Override
	public String queryXmb(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition=condition+" and jhsj.ISZC='1' and GC_TCJH_XMXDK_ID=jhsj.xmid and jhsj.bdid is null and xmb.jhsjid(+)=jhsj.gc_jh_sj_id";
		condition += BusinessUtil.getSJYXCondition("xmxdk") +BusinessUtil.getSJYXCondition("jhsj") + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			//添加伪装数据sjmj作为前期手续的内容
			String sql = "select jhsj.pxh,jhsj.jhid,jhsj.gc_jh_sj_id as jhsjid,jhsj.nd,jhsj.xmid,jhsj.xmmc,jhsj.zc as cqjhwcsj,xmxdk.qy,xmb.gc_zsb_xmb_id as zsxmid,xmb.isfk,jhsj.xmbh, xmxdk.XMDZ, xmxdk.JSNR from GC_JH_SJ jhsj, GC_TCJH_XMXDK xmxdk,GC_ZSB_XMB xmb";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("SFCQ","SF");
			
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询征收项目信息成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//反馈信息添加
	@Override
	public String doJhsjfk(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		FkqkVO vo = new FkqkVO();
		GcZsbXmbVO xmbVO = new GcZsbXmbVO();
		TcjhVO jhVO = new TcjhVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			xmbVO.setValueFromJson((JSONObject)list.get(0));
			
			if("".equals(xmbVO.getGc_zsb_xmb_id())){
				EventVO event = EventManager.createEvent(conn, ywlx, user);//生成事件
				xmbVO.setGc_zsb_xmb_id(new RandomGUID().toString()); // 主键
				xmbVO.setYwlx(ywlx);
				xmbVO.setSjbh(event.getSjbh());
				BusinessUtil.setInsertCommonFields(xmbVO,user);
				BaseDAO.insert(conn, xmbVO);
				String upsql="update gc_zsb_xxb xxb set xxb.zsxmid='"+xmbVO.getGc_zsb_xmb_id()+"' and isfk='1' where  xxb.jhsjid='"+xmbVO.getJhsjid()+"' and xxb.sfyx='1' ";
				DBUtil.execUpdateSql(conn, upsql);
				
			}else{
				xmbVO.setIsfk("1");
				BusinessUtil.setUpdateCommonFields(xmbVO,user);
				BaseDAO.update(conn, xmbVO);
			}
			//--操作反馈情况表
			vo.setGc_jh_fkqk_id(new RandomGUID().toString()); // 主键
			vo.setJhid(xmbVO.getJhid());
			vo.setJhsjid(xmbVO.getJhsjid());
			vo.setSjbh(xmbVO.getSjbh());
			vo.setFkid(xmbVO.getGc_zsb_xmb_id());
			vo.setFkrq(Pub.getCurrentDate());
			vo.setBz(xmbVO.getWtjfx());
			BusinessUtil.setInsertCommonFields(vo,user);
			BaseDAO.insert(conn, vo);
			//--操作统筹计划表
			jhVO.setGc_jh_sj_id(xmbVO.getJhsjid());
			jhVO.setZc_sj(xmbVO.getSjwcrq());
			jhVO.setZc_bz(xmbVO.getWtjfx());
			jhVO.setIszc(xmbVO.getSfcq());
			BusinessUtil.setUpdateCommonFields(jhVO,user);
			BaseDAO.update(conn, jhVO);
			//----------------------------------------------------------------
			LogManager.writeUserLog(xmbVO.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "统筹计划征收情况反馈成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(xmbVO.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "统筹计划征收情况反馈失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmbVO.getGc_zsb_xmb_id()+"\",\"fieldname\":\"gc_zsb_xmb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String update_result =this.queryConditionXmb(request,jsona);
		return update_result;
	}
	@Override
	public String getZt(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String jhsjid = request.getParameter("jhsjid");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sql = "select jhsj.ZC_SJ from GC_JH_SJ jhsj where GC_JH_SJ_ID='"+jhsjid+"' and sfyx='1'";
			String[][] resArr = DBUtil.query(conn, sql);
			if(resArr!=null){
				domresult = resArr[0][0];
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
