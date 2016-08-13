package com.ccthanking.business.zjb.sjgl;

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
import com.ccthanking.business.xdxmk.vo.XmxdkVO;
import com.ccthanking.business.xmcbk.vo.NdVO;
import com.ccthanking.business.xmcbk.vo.XmcbkSpVO;
import com.ccthanking.business.xmcbk.vo.XmcbkVO;
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
public class ShenJiGuanLiServiceImpl implements ShenJiGuanLiService {

	private String ywlx=YwlxManager.GC_ZJB_JS;
	@Override
	public String querysjList(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += BusinessUtil.getSJYXCondition("D") + BusinessUtil.getCommonCondition(user,null);
			if(!Pub.empty(condition)){
				condition +=" AND CSSDRQ IS NOT NULL AND SJND IS NOT  NULL AND D.Htid = f.ID(+) AND D.XDKID=A.GC_TCJH_XMXDK_ID  AND d.bdid = E.GC_XMBD_ID(+) and g.gc_jh_sj_id=f.jhsjid AND f.htid=c.id ";
				
			}else{
				condition="  CSSDRQ IS NOT NULL AND SJND IS  NOT NULL  AND D.Htid = f.ID(+) AND D.XDKID=A.GC_TCJH_XMXDK_ID  AND d.bdid = E.GC_XMBD_ID(+) and g.gc_jh_sj_id=f.jhsjid AND f.htid=c.id";
			}
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select '' SBCWBL ,'' HTCEBL ,A.XMBH,decode(d.bdid,'',a.sgdw,e.sgdw) SGDW,  A.GC_TCJH_XMXDK_ID xmid,decode(d.bdid,'',a.JLDW,e.JLDW) JLDW,f.ID HTID,C.Fbfs HTQDFS, f.HTQDJ htje,c.HTMC,g.ND,D.gc_zjb_jsb_id, D.xdkid, D.bdid, A.xmmc, E.bdmc,  C.HTBM htbh, D.jszt, D.tbr, D.tbrdh, D.tbrq, D.tbje, D.sfwt," +
					"D.wtzxgs, D.yzsdrq, D.yzsdje, D.cssdrq, D.cssdje, D.csbgbh, D.sjsdrq, D.sjsdje, D.sjbgbh, D.jsqk, D.jsbz, D.ywlx, D.sjbh,D.GCQK, " +
					"D.bz, d.zt ,D.lrr, D.lrsj, D.sfyx,f.jhsjid,g.bdbh, TBJE-YZSDJE TBYZ, YZSDJE-CSSDJE YZCS, CSSDJE-SJSDJE CSSJ,TBJE-SJSDJE  TBSJ,d.TBCSRQ,D.SJND,D.SJZXGS,D.SJZ from gc_tcjh_xmxdk A,gc_htgl_ht C,gc_zjb_jsb D ,gc_xmbd E,GC_HTGL_HTSJ f ,gc_jh_sj g ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("JSZT", "JSZT");
			bs.setFieldDic("SFWT", "SF");
			bs.setFieldDic("HTQDFS", "FBFS");
			bs.setFieldThousand("HTJE");
			bs.setFieldThousand("TBJE");
			bs.setFieldThousand("YZSDJE");
			bs.setFieldThousand("CSSDJE");
			bs.setFieldThousand("SJSDJE");
			bs.setFieldThousand("TBYZ");
			bs.setFieldThousand("YZCS");
			bs.setFieldThousand("CSSJ");
			bs.setFieldThousand("TBSJ");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("WTZXGS", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryJS(String json, User user,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			String ids=request.getParameter("ids");
			
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += BusinessUtil.getSJYXCondition("D") + BusinessUtil.getCommonCondition(user,null);
			if(!Pub.empty(condition)){
				condition +=" AND CSSDRQ IS NOT NULL AND SJND IS   NULL  AND D.Htid = f.ID(+) AND D.XDKID=A.GC_TCJH_XMXDK_ID  AND d.bdid = E.GC_XMBD_ID(+) and g.gc_jh_sj_id=f.jhsjid AND f.htid=c.id ";
				
			}else{
				condition="  CSSDRQ IS NOT NULL AND SJND IS   NULL AND D.Htid = f.ID(+) AND D.XDKID=A.GC_TCJH_XMXDK_ID  AND d.bdid = E.GC_XMBD_ID(+) and g.gc_jh_sj_id=f.jhsjid AND f.htid=c.id";
			}
			if(!Pub.empty(ids)&&!"\'".equals(ids))
			{
				int index=ids.lastIndexOf(",\'");
				ids=ids.substring(0, index);
				condition+="    and GC_ZJB_JSB_ID not in("+ids+")  ";
			}
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select '' SBCWBL ,'' HTCEBL ,A.XMBH,decode(d.bdid,'',a.sgdw,e.sgdw) SGDW,  A.GC_TCJH_XMXDK_ID xmid,decode(d.bdid,'',a.JLDW,e.JLDW) JLDW,f.ID HTID,C.Fbfs HTQDFS, f.HTQDJ htje,c.HTMC,g.ND,D.gc_zjb_jsb_id, D.xdkid, D.bdid, A.xmmc, E.bdmc,  C.HTBM htbh, D.jszt, D.tbr, D.tbrdh, D.tbrq, D.tbje, D.sfwt," +
					"D.wtzxgs, D.yzsdrq, D.yzsdje, D.cssdrq, D.cssdje, D.csbgbh, D.sjsdrq, D.sjsdje, D.sjbgbh, D.jsqk, D.jsbz, D.ywlx, D.sjbh,D.GCQK, " +
					"D.bz, d.zt ,D.lrr, D.lrsj, D.sfyx,f.jhsjid,g.bdbh, TBJE-YZSDJE TBYZ, YZSDJE-CSSDJE YZCS, CSSDJE-SJSDJE CSSJ,TBJE-SJSDJE  TBSJ,d.TBCSRQ,D.SJND from gc_tcjh_xmxdk A,gc_htgl_ht C,gc_zjb_jsb D ,gc_xmbd E,GC_HTGL_HTSJ f ,gc_jh_sj g ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("JSZT", "JSZT");
			bs.setFieldDic("SFWT", "SF");
			bs.setFieldDic("HTQDFS", "FBFS");
			bs.setFieldThousand("HTJE");
			bs.setFieldThousand("TBJE");
			bs.setFieldThousand("YZSDJE");
			bs.setFieldThousand("CSSDJE");
			bs.setFieldThousand("SJSDJE");
			bs.setFieldThousand("TBYZ");
			bs.setFieldThousand("YZCS");
			bs.setFieldThousand("CSSJ");
			bs.setFieldThousand("TBSJ");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("WTZXGS", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insert_sj(HttpServletRequest request, String msg ) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String ids=request.getParameter("ids");
		String year=request.getParameter("year");
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjbJsbVO xmvo ;
		try {	
				conn.setAutoCommit(false);
				if(ids.length() > 0 && ids.indexOf("\"")>-1)
				{
					ids = ids.replaceAll("\"", "");
				}
				String[] id = ids.split(",");
				for(int i = 0; i<id.length;i++)
				{
					xmvo = new GcZjbJsbVO();
					xmvo.setGc_zjb_jsb_id(id[i]);
					xmvo = (GcZjbJsbVO)BaseDAO.getVOByPrimaryKey(conn,xmvo);
					xmvo.setSjnd(year);
					BusinessUtil.setUpdateCommonFields(xmvo, user);//公共字段更新					
					BaseDAO.update(conn, xmvo);
					resultVO = xmvo.getRowJson();
					conn.commit();
					LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "项目纳入计划成功", user,"","");
				}

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		return resultVO;
	}
	@Override
	public String update_js(HttpServletRequest request, String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjbJsbVO xmvo = new GcZjbJsbVO();

		try {
			conn.setAutoCommit(false);
		    JSONArray list = xmvo.doInitJson(json);
		    xmvo.setValueFromJson((JSONObject)list.get(0));
			//设置公共字段
			BusinessUtil.setUpdateCommonFields(xmvo, user);
	     	xmvo.setYwlx(ywlx);//业务类型
	     	//根据ID查YWID和SJBH
			GcZjbJsbVO vo1=(GcZjbJsbVO) BaseDAO.getVOByPrimaryKey(conn, xmvo);
			
	     	FileUploadVO fvo = new FileUploadVO();
	     	fvo.setFjzt("1");
	     	fvo.setGlid2(xmvo.getJhsjid());//存入计划数据ID
	     	fvo.setGlid3(xmvo.getXdkid()); //存入项目ID
	     	fvo.setGlid4(xmvo.getBdid()); //存入标段ID
	     	fvo.setYwlx(ywlx);
	     	fvo.setSjbh(vo1.getSjbh());
	     	FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_zjb_jsb_id(),user);
	     	
			//修改 
			BaseDAO.update(conn, xmvo);
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "结算管理修改成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zjb_jsb_id()+"\",\"fieldname\":\"gc_zjb_jsb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.querysjList(jsona,user);
			return resultXinXi;
	}
	@Override
	public String querysjxx(HttpServletRequest request, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String sjnd=request.getParameter("sjnd");
		String domresult = "";
		try {
			PageManager page = null;
			conn.setAutoCommit(false);
			String sql = "SELECT T.SJBGBH,T.SJSDRQ,T.SJND FROM GC_ZJB_JSB T where  rownum='1' and sjnd= "+sjnd;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String update_jss(HttpServletRequest request, String json) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String resultVO = null;
		GcZjbJsbVO xmvo = new GcZjbJsbVO();
		GcZjbJsbVO xmvo2; 
		try {
			conn.setAutoCommit(false);
		    JSONArray list = xmvo.doInitJson(json);
		    xmvo.setValueFromJson((JSONObject)list.get(0));
		    GcZjbJsbVO xmvo1 = new GcZjbJsbVO();
		    xmvo1.setSjnd(xmvo.getSjnd());
		    //GcZjbJsbVO[] xmvos = (GcZjbJsbVO[]) BaseDAO.getVOByCondition(conn, xmvo1);
			String sql=" SELECT T.GC_ZJB_JSB_ID FROM GC_ZJB_JSB T where  sjnd= '"+xmvo.getSjnd()+"'";
			String xmvos[][] = DBUtil.query(conn, sql);
			if(null!=xmvos&&xmvos.length>0&&xmvos[0][0]!=null){
				for(int i=0;i<xmvos.length;i++){
					xmvo2 = new GcZjbJsbVO();
					xmvo2.setGc_zjb_jsb_id(xmvos[i][0]);
			    	xmvo2.setSjbgbh(xmvo.getSjbgbh());
			    	xmvo2.setSjsdrq(xmvo.getSjsdrq());
			    	//设置公共字段
					BusinessUtil.setUpdateCommonFields(xmvo, user);
			     	xmvo2.setYwlx(ywlx);//业务类型
			     	//根据ID查YWID和SJBH
					GcZjbJsbVO vo1=(GcZjbJsbVO) BaseDAO.getVOByPrimaryKey(conn, xmvo2);
					//修改 
					BaseDAO.update(conn, xmvo2);
					resultVO = xmvo.getRowJson();
					conn.commit();
					LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
							Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
							user.getOrgDept().getDeptName() + " " + user.getName()
									+ "审计信息修改成功", user,"","");
				}
			}
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zjb_jsb_id()+"\",\"fieldname\":\"gc_zjb_jsb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.querysjList(jsona,user);
			return resultXinXi;
	}
	@Override
	public String updateJSById(HttpServletRequest request ) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String jsid=request.getParameter("jsid");
		String resultVO = null;
		GcZjbJsbVO xmvo = new GcZjbJsbVO();

		try {
			conn.setAutoCommit(false);
			//设置公共字段
			BusinessUtil.setUpdateCommonFields(xmvo, user);
			xmvo.setGc_zjb_jsb_id(jsid);
			xmvo.setSjnd("");
	     	xmvo.setYwlx(ywlx);//业务类型
	     	//根据ID查YWID和SJBH
			GcZjbJsbVO vo1=(GcZjbJsbVO) BaseDAO.getVOByPrimaryKey(conn, xmvo);
			//修改 
			BaseDAO.update(conn, xmvo);
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "审计移除成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zjb_jsb_id()+"\",\"fieldname\":\"gc_zjb_jsb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.querysjList(jsona,user);
			return resultXinXi;
	}

}


