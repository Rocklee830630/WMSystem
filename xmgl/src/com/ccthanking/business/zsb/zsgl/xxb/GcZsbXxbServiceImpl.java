package com.ccthanking.business.zsb.zsgl.xxb;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.zjb.jsgl.GcZjbJsbVO;
import com.ccthanking.business.zsb.zsjd.GcZsbJdbVO;
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
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.mysql.jdbc.Util;


@Service
public  class GcZsbXxbServiceImpl implements GcZsbXxbService {
	private String ywlx=YwlxManager.GC_ZS_XX;

	@Override
	public String queryXxb(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition=condition+" and xxb.jhsjid = jh.gc_jh_sj_id and qqsx.jhsjid(+)=jh.gc_jh_sj_id";
			condition += BusinessUtil.getSJYXCondition("xxb") +BusinessUtil.getSJYXCondition("jh") + BusinessUtil.getCommonCondition(user,null);

			condition += orderFilter;
			page.setFilter(condition);
				conn.setAutoCommit(false);
				/*String sql = "select xxb.*,xmb.CDSJSJ,xmb.SJWCRQ,jh.CQT_BZ as CQTBZ,jh.ZC as CQJHWCSJ,jh.CQT_SJ as CQTHDSJ  from GC_JH_SJ jh,GC_ZSB_XMB xmb,GC_ZSB_XXB xxb, GC_TCJH_XMXDK xd ";*/
				String sql = "select xxb.ljjmzl, xxb.ljqyzl, xxb.ljzdmj,jh.xmbh, " +
						"xxb.wtjfx,jh.pxh,jh.xmid,jh.xmmc,jh.nd,jh.jhid,jh.sjmj, " +
						"xxb.gc_zsb_xxb_id, xxb.zsxmid, xxb.qy, xxb.zrdw, xxb.mdwcrq," +
						" xxb.mdgzbz, xxb.jmhs, xxb.qyjs, xxb.jttdmj, xxb.gytdmj, xxb.zmj,xxb.sjbh,xxb.ywlx," +
						" xxb.mdgs, xxb.sjfwcq, xxb.sjwcrq, xxb.qwtrq, xxb.qwtbz, xxb.zjdwje, " +
						"xxb.zjdwrq, xxb.zjdwbz, xxb.sjrq, xxb.cqssbz, xxb.cdyjrq, xxb.cdyjbz," +
						" xxb.tdfwyjrq, xxb.tdfwbz, xxb.cqbz,xxb.lrsj,xxb.gc_zsb_xxb_id as mdfj," +
						" jh.gc_jh_sj_id as jhsjid, xxb.cqrwmc, xxb.fzr, xxb.xyje," +
						" qqsx.ghxkz,qqsx.tdsyz,qqsx.sfgh,qqsx.sftd,qqsx.jhsjid as mark, " +
						" jh.cqt_sj as cqthdsj,jh.zc as cqjhwcsj," + 
						" (case jh.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jh.nd and GC_TCJH_XMXDK_ID = jh.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jh.bdid and rownum = 1) end ) as XMBDDZ " +
						" from GC_ZSB_XXB xxb, GC_JH_SJ jh," +
						"(select sx.ghspbjsj as ghxkz,sx.tdspbjsj as tdsyz,decode(sx.ghspbblsx,'0008',sx.jhsjid,'') as sfgh," +
						"decode(sx.tdspbblsx,'0014',sx.jhsjid,'') as sftd,sx.jhsjid from gc_zgb_qqsx sx where sx.sfyx='1') qqsx";
				/* 拆迁计划完成时间：jh.ZC;
				 * 拆迁图获得时间：jh.CQT_SJ
				 * 拆迁图备注：jh.CQT_BZ
				 * */
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldTranslater("FZR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
//				bs.setFieldDic("SJFWCQ","SFCQ");
				bs.setFieldDic("qy","QY");
				bs.setFieldThousand("XYJE");
				bs.setFieldThousand("MDGS");//摸底估算
				bs.setFieldThousand("ZJDWJE");//资金到位金额
				bs.setFieldFileUpload("mdfj", "1111");
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询征收详细信息数据成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertXxb(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZsbXxbVO xmvo = new GcZsbXxbVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		//设置主键
		xmvo.setGc_zsb_xxb_id(new RandomGUID().toString()); // 主键
		BusinessUtil.setInsertCommonFields(xmvo, user);
		xmvo.setYwlx(ywlx);
		EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
		xmvo.setSjbh(eventVO.getSjbh());

		//插入
		BaseDAO.insert(conn, xmvo);
		//resultVO = xmvo.getRowJson();
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "插入征收详细信息数据成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "插入征收详细信息数据失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String updateXxb(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZsbXxbVO vo = new GcZsbXxbVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			//附件操作
			FileUploadVO fvo = new FileUploadVO();
			fvo.setFjzt("1");
			fvo.setYwid(vo.getGc_zsb_xxb_id());
			fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
			fvo.setGlid3(vo.getXmid()); //存入项目ID
			FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_zsb_xxb_id(),user);
			//修改
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();
		
		
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), ywlx,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "修改征收详细信息数据成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改征收详细信息数据失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
//		return resultVO;
		String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_zsb_xxb_id()+"\",\"fieldname\":\"gc_zsb_xxb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryXxb(request,jsona);
		return resultXinXi;
	}
	
	//删除功能
	
	@Override
	public String deleteZsxx(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZsbXxbVO vo = new GcZsbXxbVO();

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
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), ywlx,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "删除征收详细信息数据成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除征收详细信息数据失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	public List<autocomplete> xmmcAutoComplete(HttpServletRequest request,autocomplete json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> autoResult = new ArrayList<autocomplete>(); 
		autocomplete ac = new autocomplete();
		String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
		condition += BusinessUtil.getSJYXCondition("jhsj")+BusinessUtil.getSJYXCondition("xxb");
		condition += BusinessUtil.getCommonCondition(user,null);
		String [][] result = DBUtil.query("select distinct xmmc from GC_JH_SJ jhsj,GC_ZSB_XXB xxb" + json.getTablePrefix() + " where " + condition+" and xxb.jhsjid=jhsj.GC_JH_SJ_ID");
        if(null != result&&result.length>0){
        	for(int i =0;i<result.length;i++){
        	  ac = new autocomplete();
              ac.setRegionName(result[i][0]);
              autoResult.add(ac);
        	}
        }
		return autoResult;
	}
	@Override
	public String updateXXB(HttpServletRequest request, String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String fzr=request.getParameter("yfzr");
		String resultVO = null;
		GcZsbXxbVO vo = new GcZsbXxbVO();
		String sjbh="";
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			
			//根据ID查YWID和SJBH
			if(!Pub.empty(vo.getGc_zsb_xxb_id()))
			{
				//根据ID修改
				GcZsbXxbVO vo1=(GcZsbXxbVO) BaseDAO.getVOByPrimaryKey(conn, vo);
				sjbh=vo1.getSjbh();
				BaseDAO.update(conn, vo);
				resultVO = vo.getRowJson();
			}else{
				String sql=" update GC_ZSB_XXB set fzr='"+vo.getFzr()+"'  where fzr='"+fzr+"' ";
				DBUtil.execUpdateSql(conn, sql);
			}
			
			conn.commit();
		LogManager.writeUserLog(sjbh, ywlx,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "修改征收详细信息数据成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
//		return resultVO;
		String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_zsb_xxb_id()+"\",\"fieldname\":\"gc_zsb_xxb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryXxb(request,jsona);
		return resultXinXi;
	}
}
