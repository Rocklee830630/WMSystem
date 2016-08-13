package com.ccthanking.business.xmglgs.xmxxgl.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.bdhf.vo.BdwhVO;
import com.ccthanking.business.xdxmk.vo.XmxdkVO;
import com.ccthanking.business.xmglgs.xmxxgl.service.XmxxService;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class XmxxServiceImpl implements XmxxService {
	
	private String ywlx = YwlxManager.GC_XMXDK_XMXX;//项目管理公司-项目信息管理

	@Override
	public String query(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		try {

		PageManager page = RequestUtil.getPageManager(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		String orderFilter = RequestUtil.getOrderFilter(json);
		String xmglgs = request.getParameter("xmglgs");
		if(!Pub.empty(xmglgs)){
			//condition += " AND T1.XMGLGS = '"+xmglgs +"' ";
			condition += " AND (/*t.XMGLGS = '"+xmglgs+"' or*/ t.gc_jh_sj_id in (select tt.gc_jh_sj_id from gc_jh_sj tt where /*tt.xmbs = '0' and*/ tt.xmid in (select xmid from gc_jh_sj j /*where j.xmglgs = '"+xmglgs+"'*/))) ";
		}
		condition += " AND t.xmid = t1.GC_TCJH_XMXDK_ID ";
		
		condition += BusinessUtil.getSJYXCondition("t");
	    condition += BusinessUtil.getCommonCondition(user,"t");
	    condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT "
					+ "T.GC_JH_SJ_ID,T.XMBH,T.XMMC,T.XMBS,T.BDID,T.XMGLGS,T.BDBH,T.BDMC,T.PXH,T.XMID, "
					+"T1.GC_TCJH_XMXDK_ID,T1.XMLX,T1.YZDB,T1.FZR_GLGS,T1.LXFS_GLGS, "
					+ "(case xmbs when '0' then (select sjdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select sjdw from GC_XMBD where GC_XMBD_ID = t.bdid) end) SJDW, "
					+ "(case xmbs when '0' then (select FZR_SJDW from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select SJDWFZR from GC_XMBD where GC_XMBD_ID = t.bdid) end) SJDWFZR, "
					+ "(case xmbs when '0' then (select LXFS_SJDW from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select SJDWFZRLXFS from GC_XMBD where GC_XMBD_ID = t.bdid) end) SJDWFZRLXFS, "
					+ "(case xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select SGDW from GC_XMBD where GC_XMBD_ID = t.bdid) end) SGDW, "
					+ "(case xmbs when '0' then (select SGDWXMJL from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select SGDWXMJL from GC_XMBD where GC_XMBD_ID = t.bdid) end) SGDWXMJL, "
					+ "(case xmbs when '0' then (select SGDWXMJLLXDH from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select SGDWXMJLLXDH from GC_XMBD where GC_XMBD_ID = t.bdid) end) SGDWXMJLLXDH, "
					+ "(case xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select JLDW from GC_XMBD where GC_XMBD_ID = t.bdid) end) JLDW, "
					+ "(case xmbs when '0' then (select JLDWZJ from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select JLDWZJ from GC_XMBD where GC_XMBD_ID = t.bdid) end) JLDWZJ, "
					+ "(case xmbs when '0' then (select JLDWZJLXDH from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select JLDWZJLXDH from GC_XMBD where GC_XMBD_ID = t.bdid) end) JLDWZJLXDH, "
					+ "(SELECT SJHM FROM FS_ORG_PERSON WHERE ACCOUNT =t1.yzdb)AS LXFS_YZDB, "
					+ "(case t.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1) end ) as XMBDDZ "
					+ "FROM GC_JH_SJ T,GC_TCJH_XMXDK T1";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//表选翻译
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("FZR_GLGS", "FS_ORG_PERSON", "ACCOUNT", "NAME");//项目管理公司负责人
			//字典翻译
			bs.setFieldDic("XMLX", "XMLX");//项目类型
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<项目信息>", user,"","");
		}  catch (Exception e) {
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
		String ywid=request.getParameter("ywid");
		String resultVO = null;
		XmxdkVO vo = new XmxdkVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			
			
			//FileUploadVO vo = new FileUploadVO();
			/*vo.setYwid(pcbvo.getGc_cbk_pcb_id());
			vo.setFjzt("1");
			vo.setSjbh(pcbvo.getSjbh());
			vo.setYwlx(ywlx);
			FileUploadService.updateVOByYwid(conn, vo, ywid,user);*/

			
			
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getGc_tcjh_xmxdk_id());
	        fvo.setFjzt("1");//更新附件状态
	        //fvo.setSjbh(vo.getSjbh());
	        fvo.setYwlx(YwlxManager.GC_XM);
	        fvo.setGlid2("");//存入计划数据ID
	        fvo.setGlid3(vo.getGc_tcjh_xmxdk_id()); //存入项目ID
	        fvo.setGlid4(""); //存入标段ID
	        FileUploadService.updateVOByYwid(conn, fvo, ywid,user);
			BusinessUtil.setUpdateCommonFields(vo,user);
			BaseDAO.update(conn, vo);
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "更新<项目信息>成功", user,"","");
			
		resultVO = vo.getRowJson();
		conn.commit();
		
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	
	@Override
	public String queryBdById(String json,HttpServletRequest request) throws Exception {
			Connection conn = DBUtil.getConnection();
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String domresult = "";
			String id = request.getParameter("id");
			PageManager page = null;
			try {
				if (page == null)
					page = new PageManager();
				conn.setAutoCommit(false);
				String sql = "SELECT " +
						"gc_xmbd_id, jhid, jhsjid, nd, xmid, bdbh, bdmc, qdzh, zdzh, cd, kd, gj, wgrq, qgrq, jgrq, gcztfy, sgdw, sgdwfzr, sgdwfzrlxfs, jldw, jldwfzr, jldwfzrlxfs, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, bddd, mj, sghtid, jlhtid, jsgm, sjdw, sjdwfzr, sjdwfzrlxfs, sgdwxmjl, sgdwxmjllxdh, sgdwjsfzr, sgdwjsfzrlxdh, jldwzj, jldwzjlxdh, jldwzjdb, jldwzjdblxdh, jldwaqjl, jldwaqjllxdh " +
						"FROM " +
						"GC_XMBD WHERE gc_xmbd_id = '"+id+"'";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				domresult = bs.getJson();
				conn.commit();
				LogManager.writeUserLog(null, YwlxManager.GC_XM_BD,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询<标段信息>", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String insertBdxx(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		BdwhVO vo = new BdwhVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo,user);
			BaseDAO.update(conn, vo);
			LogManager.writeUserLog(vo.getSjbh(), YwlxManager.GC_XM_BD,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "更新<标段信息>成功", user,"","");
		
		resultVO = vo.getRowJson();
		conn.commit();
		
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	@Override
	public String queryXdkById(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		String id = request.getParameter("id");
		PageManager page = null;
		try {
			if (page == null)
				page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"t.gc_tcjh_xmxdk_id, t.xmbh, t.xmmc, t.qy, t.nd, t.pch, t.xmlx, t.xmdz, t.jsnr, t.jsyy, t.jsrw, t.jsbyx, t.jhztze, t.gc, t.zc, t.qt, t.xmsx, t.isbt, t.isnatc, t.jszt, t.xmfr, t.xmglgs, t.fzr_glgs, t.lxfs_glgs, t.sgdw, t.fzr_sgdw, t.lxfs_sgdw, t.jldw, t.fzr_jldw, t.lxfs_jldw, t.yzdb, t.sjdw, t.fzr_sjdw, t.lxfs_sjdw, t.wgrq, t.qgrq, t.jgrq, t.ywlx, t.sjbh, t.bz, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx, t.xmcbk_id, t.xjxj, t.pxh, t.xj_xmid, t.wdd, t.jsmb, t.sghtid, t.jlhtid, t.sjhtid, t.sgdwxmjl, t.sgdwxmjllxdh, t.sgdwjsfzr, t.sgdwjsfzrlxdh, t.jldwzj, t.jldwzjlxdh, t.jldwzjdb, t.jldwzjdblxdh, t.jldwaqjl, t.jldwaqjllxdh, " +
					"(SELECT SJHM FROM FS_ORG_PERSON WHERE ACCOUNT =t.yzdb)AS LXFS_YZDB, "+
					"(SELECT GC_XMBD_ID FROM GC_XMBD WHERE XMID = '"+id+"' AND ROWNUM = 1)AS BDID"+
					" FROM GC_TCJH_XMXDK T" +
					" WHERE T.GC_TCJH_XMXDK_ID = '"+id+"'";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("XJ_XMID", "GC_TCJH_XMXDK", "GC_TCJH_XMXDK_ID", "XMMC");//续建关联下达库
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
			bs.setFieldTranslater("FZR_GLGS", "FS_ORG_PERSON", "ACCOUNT", "NAME");//项目管理公司负责人
			domresult = bs.getJson();
			conn.commit();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<项目信息>", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
