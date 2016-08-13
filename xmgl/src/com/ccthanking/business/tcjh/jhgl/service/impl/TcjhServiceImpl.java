package com.ccthanking.business.tcjh.jhgl.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.tcjh.jhgl.GenerateGtt;
import com.ccthanking.business.tcjh.jhgl.service.TcjhService;
import com.ccthanking.business.tcjh.jhgl.vo.BgbbVO;
import com.ccthanking.business.tcjh.jhgl.vo.BgxmVO;
import com.ccthanking.business.tcjh.jhgl.vo.GcJhZtXmVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.business.xdxmk.vo.ZtVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class TcjhServiceImpl implements TcjhService {
	
	private String ywlx = YwlxManager.GC_JH_SJ;

	@Override
	public String insertBgbb(String json,User user,String xmbh) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		PageManager page = null;
		EventVO eventVO = null;
		String bgbbId="";
		BgbbVO vo = new BgbbVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			if(Pub.empty(vo.getGc_jh_bgbb_id()) ){
				String sql = "select max(BGBBH) from GC_JH_BGBB t where t.jhid ='"+vo.getJhid()+"'";
				String[][] str = DBUtil.query(conn, sql);
				int res = 1;
				if(str[0][0]!=""){
					res = Integer.parseInt(str[0][0])+1;
				}
				vo.setGc_jh_bgbb_id(new RandomGUID().toString());
				vo.setBgbbh(String.valueOf(res));
				vo.setBgrq(new Date());
				vo.setYwlx(YwlxManager.GC_JH_BGBB);
			    eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			    vo.setSjbh(eventVO.getSjbh());
			    BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
				bgbbId = vo.getGc_jh_bgbb_id();
			}else{
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
			}
				BgxmVO bvo = new BgxmVO();
				bvo.setGc_jh_bgxm_id(new RandomGUID().toString());
				bvo.setXmbh(xmbh);
				bvo.setBgid(bgbbId);
				bvo.setYwlx(YwlxManager.GC_JH_BGXM);
			    eventVO = EventManager.createEvent(conn, bvo.getYwlx(), user);//生成事件
			    bvo.setSjbh(eventVO.getSjbh());
			    BusinessUtil.setInsertCommonFields(bvo,user);
				BaseDAO.insert(conn, bvo);
			
			
			resultVO = vo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "添加<版本项目>成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String query(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition += " and t.xmid = t1.gc_tcjh_xmxdk_id ";
        condition += BusinessUtil.getSJYXCondition("t");
        condition += BusinessUtil.getCommonCondition(user,"t");
        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			
			conn.setAutoCommit(false);
			String sql = "";
			sql = "SELECT "
					+" decode(  t.xmbs,'1',(select BDDD from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1),(select XMDZ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1)) as XMDZ," +
					"  decode (t.xmbs,'1',(select JSGM from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1),(select JSNR from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1)) as JSNR," +
					"  (select JSMB from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum=1) as JSMB," +
					//"  (select XMGLGS from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum=1) as XMGLGS, " +
					"  (select XJXJ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum=1) as XMXZ, " +
					" t1.wdd,"+
					"t.gc_jh_sj_id, t.jhid, t.nd, t.xmid, t.bdid, t.xmbh, t.bdbh,t.xmmc,t.xmglgs," +
					" t.bdmc, t.pxh, t.kgsj, t.kgsj_sj, t.kgsj_bz, t.wgsj, t.wgsj_sj, t.wgsj_bz, t.kypf, t.kypf_sj, t.kypf_bz, t.hpjds, t.hpjds_sj, t.hpjds_bz, t.gcxkz, t.gcxkz_sj, t.gcxkz_bz, t.sgxk, t.sgxk_sj, t.sgxk_bz, t.cbsjpf, t.cbsjpf_sj, t.cbsjpf_bz, t.cqt, t.cqt_sj, t.cqt_bz, t.pqt, t.pqt_sj, t.pqt_bz, t.sgt, t.sgt_sj, t.sgt_bz, t.tbj, t.tbj_sj, t.tbj_bz, t.cs, t.cs_sj, t.cs_bz, t.jldw, t.jldw_sj, t.jldw_bz, t.sgdw, t.sgdw_sj, t.sgdw_bz, t.zc, t.zc_sj, t.zc_bz, t.pq, t.pq_sj, t.pq_bz, t.jg, t.jg_sj, t.jg_bz, t.xmsx, t.ywlx, t.sjbh, t.bz, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx, t.xflx, t.isxf, t.iskypf, t.ishpjds, t.isgcxkz, t.issgxk, t.iscbsjpf, t.iscqt, t.ispqt, t.issgt, t.istbj, t.iscs, t.isjldw, t.issgdw, t.iszc, t.ispq, t.isjg, t.xmbs,t.iskgsj,t.iswgsj,t.isnobdxm "+
					" FROM " +
					" GC_JH_SJ t,GC_TCJH_XMXDK t1 " ;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");//标段
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldDic("ISKGSJ", "SF");
			bs.setFieldDic("ISWGSJ", "SF");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<统筹计划数据>成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String xdtcjhByPc(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		ZtVO zvo = null;
		TcjhVO tvo = null;
		try {
			conn.setAutoCommit(false);
			zvo = new ZtVO();
			zvo.setGc_jh_zt_id(json);
			zvo = (ZtVO)BaseDAO.getVOByPrimaryKey(conn,zvo);
			//判断该批次是否有下发版本
			if(Pub.empty(zvo.getXfbb())){
				zvo.setIsxf("1");//下发标识
				//没有下发过，更改下发标识
				zvo.setScxfbb(zvo.getMqbb());//首次下发版本 -等于当前版本
				zvo.setScxfrq(new Date());//首次下发日期
			}
			//已经下发过，更改下发版本
			zvo.setXfbb(zvo.getMqbb());//下发版本-等于当前版本
			// start 发完本次以后，目前版本次数+1 	add by xiahongbo by 2014-10-18 
			String mqbb = String.valueOf(Integer.parseInt(zvo.getMqbb())+1);
			zvo.setMqbb(mqbb);
			// end
			zvo.setXfrq(new Date());//下发日期
			BusinessUtil.setUpdateCommonFields(zvo,user);
			BaseDAO.update(conn, zvo);
			//当统筹计划已下发时，发送给流程发起人提示信息。
			PushMessage.push(conn, request, PushMessage.TCJHXF,"统筹计划<"+zvo.getJhpch()+">已下发。",null,zvo.getSjbh(),zvo.getYwlx(),zvo.getGc_jh_zt_id());
			String sql = "SELECT GC_JH_SJ_ID FROM GC_JH_SJ WHERE JHID='"+json+"'";
			BaseResultSet bs = DBUtil.query(conn, sql, null);
			ResultSet rs = bs.getResultSet();
			while(rs.next()){
				String xmid = rs.getString("GC_JH_SJ_ID");
				if(!Pub.empty(xmid)){
					tvo = new TcjhVO();
					tvo.setGc_jh_sj_id(xmid);
					tvo = (TcjhVO)BaseDAO.getVOByPrimaryKey(conn,tvo);
					tvo.setIsxf("1");//是否下发
					BusinessUtil.setUpdateCommonFields(tvo,user);
					BaseDAO.update(conn, tvo);
				}
			}
			conn.commit();
			resultVO = zvo.getRowJson();
			LogManager.writeUserLog(zvo.getSjbh(), zvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "<按计划下达统筹>成功", user,"","");
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String xdtcjhByXm(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String thisYwlx = YwlxManager.GC_JH_ZT_XM;
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		TcjhVO tvo = null;
		GcJhZtXmVO jhztvo = null;
		try {
			conn.setAutoCommit(false);
			
			int nd = DateTimeUtil.getCurrentYear();
			Date date = DateTimeUtil.getNow();
			String xfid = new RandomGUID().toString();
			String[] idArray = json.split(",");
			for (int i = 0; i < idArray.length; i++) {
				String jhsjid = idArray[i];
				
				if(!Pub.empty(jhsjid)) {
					tvo = new TcjhVO();
					tvo.setGc_jh_sj_id(jhsjid);
					tvo = (TcjhVO)BaseDAO.getVOByPrimaryKey(conn,tvo);
					tvo.setIsxf("1");//是否下发
					BusinessUtil.setUpdateCommonFields(tvo,user);
					BaseDAO.update(conn, tvo);
					
					jhztvo = new GcJhZtXmVO();
					jhztvo.setGc_jh_zt_xm_id(new RandomGUID().toString());
					jhztvo.setXfid(xfid);
					jhztvo.setJhsjid(tvo.getGc_jh_sj_id());
					jhztvo.setNd(String.valueOf(nd));
					jhztvo.setXfrq(date);
					EventVO eventVO = EventManager.createEvent(conn, thisYwlx, user);//生成事件
					jhztvo.setSjbh(eventVO.getSjbh());
					jhztvo.setYwlx(thisYwlx);
				    BusinessUtil.setInsertCommonFields(jhztvo, user);
				    BaseDAO.insert(conn, jhztvo);
				}
			}
			conn.commit();
			resultVO = jhztvo.getRowJson();
			LogManager.writeUserLog(jhztvo.getSjbh(), jhztvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "<按项目下达统筹>成功", user,"","");
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String queryMoreXiangMu(String json, HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			int nd = DateTimeUtil.getCurrentYear();
			PageManager page = RequestUtil.getPageManager(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += BusinessUtil.getSJYXCondition("T") + BusinessUtil.getCommonCondition(user,null);
			condition +="  AND t.xmid = t2.GC_TCJH_XMXDK_ID and t.bdid=t3.gc_xmbd_id(+) and t.xmbs='0' ";
			condition += " order by t.xmbh, t.xmbs, t.pxh asc ";
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select t.GC_JH_SJ_ID, t.JHID, t.xmbs, t.ND, t.XMID, t.BDID, t.XMBH, t.BDBH, t.XMMC, t.BDMC, t.XMXZ, t.PXH, t.KGSJ, t.KGSJ_SJ, t.KGSJ_BZ, t.WGSJ, t.WGSJ_SJ, t.WGSJ_BZ, t.KYPF, t.KYPF_SJ, t.KYPF_BZ, t.HPJDS, t.HPJDS_SJ, t.HPJDS_BZ, t.GCXKZ, t.GCXKZ_SJ, t.GCXKZ_BZ, t.SGXK, t.SGXK_SJ, t.SGXK_BZ, t.CBSJPF, t.CBSJPF_SJ, t.CBSJPF_BZ, t.CQT, t.CQT_SJ, t.CQT_BZ, t.PQT, t.PQT_SJ, t.PQT_BZ, t.SGT, t.SGT_SJ, t.SGT_BZ, t.TBJ, t.TBJ_SJ, t.TBJ_BZ, t.CS, t.CS_SJ, t.CS_BZ, t.JLDW jh_jldw, t.JLDW_SJ, t.JLDW_BZ, t.SGDW jh_sgdw, t.SGDW_SJ, t.SGDW_BZ, t.ZC, t.ZC_SJ, t.ZC_BZ, t.PQ, t.PQ_SJ, t.PQ_BZ, t.JG, t.JG_SJ, t.JG_BZ, t.XMSX, t.YWLX, t.SJBH, t.XFLX, t.ISXF,t2.GC_TCJH_XMXDK_ID,t2.XMGLGS,t2.FZR_GLGS, decode(t.bdid,'',t2.xmdz,t3.bddd) xmdz,t2.xmlx,t2.YZDB,t2.SGDW,t2.FZR_SGDW,t2.JLDW,t2.FZR_JLDW,t2.SJDW,t2.FZR_SJDW,t2.JSNR,t2.WGRQ,t2.LXFS_GLGS,t2.LXFS_SGDW,t2.LXFS_JLDW,t2.LXFS_SJDW,t2.LXFS_YZDB ,t2.Isbt from  GC_JH_SJ t,GC_TCJH_XMXDK t2,gc_xmbd t3 ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ISXF", "SF");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("XMGLGS", "XMGLGS");
			//日期
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			domresult = bs.getJson();
		/*	LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "结算管理查询", user,"","");*/
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String queryById(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		//String resultVO = null;
		String domresult = "";
		PageManager page = RequestUtil.getPageManager(json);
		try {
			if (page == null)
				page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "select * from GC_TCJH_XMXDK where GC_TCJH_XMXDK_ID = '"+json+"'";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			bs.setFieldDic("XMLX", "XMLX");
			//bs.setFieldDic("ND","XMNF");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("ISNATC","XMZT");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("SFYX", "SF");
			domresult = bs.getJson();
			conn.commit();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "根据ID查询<统筹计划数据>", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String queryXfjh(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
	        condition += BusinessUtil.getSJYXCondition("t");
	        condition += BusinessUtil.getCommonCondition(user,"t");
	        condition += " AND t.JHID = t1.GC_JH_ZT_ID";
	        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			
			String sql = "";
			sql = "SELECT "+
					" decode(  t.xmbs,'1',(select BDDD from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1),(select XMDZ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1)) as XMDZ," +
					"  decode (t.xmbs,'1',(select JSGM from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1),(select JSNR from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1)) as JSNR," +
					"(select JSMB from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum=1) as JSMB, " +
					"  (select XJXJ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum=1) as XMXZ, " +
					"t.gc_jh_sj_id, t.jhid, t.nd, t.xmid, t.bdid, t.xmbh, t.bdbh,t.xmmc,t.xmglgs, " +
					"t.bdmc,  t.pxh, t.kgsj, t.kgsj_sj, t.kgsj_bz, t.wgsj, t.wgsj_sj, t.wgsj_bz, t.kypf, t.kypf_sj, t.kypf_bz, t.hpjds, t.hpjds_sj, t.hpjds_bz, t.gcxkz, t.gcxkz_sj, t.gcxkz_bz, t.sgxk, t.sgxk_sj, t.sgxk_bz, t.cbsjpf, t.cbsjpf_sj, t.cbsjpf_bz, t.cqt, t.cqt_sj, t.cqt_bz, t.pqt, t.pqt_sj, t.pqt_bz, t.sgt, t.sgt_sj, t.sgt_bz, t.tbj, t.tbj_sj, t.tbj_bz, t.cs, t.cs_sj, t.cs_bz, t.jldw, t.jldw_sj, t.jldw_bz, t.sgdw, t.sgdw_sj, t.sgdw_bz, t.zc, t.zc_sj, t.zc_bz, t.pq, t.pq_sj, t.pq_bz, t.jg, t.jg_sj, t.jg_bz, t.xmsx, t.ywlx, t.sjbh, t.bz, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx, t.xflx, t.isxf, t.iskypf, t.ishpjds, t.isgcxkz, t.issgxk, t.iscbsjpf, t.iscqt, t.ispqt, t.issgt, t.istbj, t.iscs, t.isjldw, t.issgdw,t.iskgsj,t.iswgsj, t.iszc, t.ispq, t.isjg, t.xmbs, " +
					"t1.GC_JH_ZT_ID "+
					"FROM " +
					"GC_JH_SJ t,GC_JH_ZT t1";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<已下发统筹计划数据>成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	//未来得改，暂不细化调整
	@Override
	public String ggtById(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		String domresult = "";
		PageManager page = null;
		TcjhVO vo = new TcjhVO();
		try {
			if (page == null)
				page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "select * from GC_JH_SJ where GC_JH_SJ_ID = '"+json+"'";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			domresult = bs.getJson();
			JSONArray list = vo.doInitJson(domresult);
			vo.setValueFromJson((JSONObject)list.get(0));
			String str = "";
			str = GenerateGtt.createGtt(vo);

			resultVO = str;
			conn.commit();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}


	//形象进度甘特图
	@Override
	public String ggtById_xxjd(String id,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String json = null;
		PageManager page = null;
		try {
			if (page == null)
				page = new PageManager();
			conn.setAutoCommit(false);
			String sql_xmlx = "select xmlx from GC_XMGLGS_XXJD_JHBZ where jhsjid = '"+id+"'";
			String xmlxs[][]=DBUtil.query(sql_xmlx);
			if(null!=xmlxs&&xmlxs.length>0&&null!=xmlxs[0][0])
			{
				String xmlx[]=xmlxs[0][0].split(",");
				String sj="";
				String name="";
				for(int i=0;i<xmlx.length;i++)
				{
					int lx=Integer.parseInt(xmlx[i]);
					switch(lx)
					{
						case 1:sj+="DLJHKGSJ,DLJHWGSJ,DLSJKGSJ,DLSJWGSJ,TJJHKGSJ,TJJSWCSJ,TJSJKGSJ,TJSJWCSJ,JCJHKGSJ,JCJSWCSJ,JCSJKGSJ,JCSJWCSJ,MCJHKGSJ,MCJSWCSJ,MCSJKGSJ,MCSJWCSJ,FZJHKGSJ,FZJSWCSJ,FZSJKGSJ,FZSJWCSJ,";
								name+="道路开工完工,土基,基层,面层,方砖,";
						break;
						case 2:sj+="PSJHKGSJ,PSJHWGSJ,PSSJKGSJ,PSSJWGSJ,";
								name+="排水开工完工,";
						break;
						case 3:sj+="QLJHKGSJ,QLJHWGSJ,QLSJKGSJ,QLSJWGSJ,ZJJHKGSJ,ZJCJSWCSJ,ZJSJKGSJ,ZJCSJWCSJ,CTJJZJHKGSJ,DZJSWCSJ,CTJJZSJKGSJ,DZSJWCSJ,QLSBJGJHKGSJ,QLSBJSWCSJ,QLSBJGSJKGSJ,QLSBSJWCSJ,QLFSJHKGSJ,QLFSJSWCSJ,QLFSSJKGSJ,QLFSSJWCSJ,";
								name+="桥梁开工完工,桩基,承台及墩柱,桥梁上部结构,桥梁附属,";
						break;
						case 4:sj+="KGJHKGSJ,KGJHWGSJ,KGSJKGSJ,KGSJWGSJ,TFJHKGSJ,TFJSWCSJ,TFSJKGSJ,TFSJWCSJ,JGJHKGSJ,JGJSWCSJ,JGSJKGSJ,JGSJWCSJ,FSJHKGSJ,FSJSWCSJ,FSSJKGSJ,FSSJWCSJ,";
								name+="框构开工完工,土方,结构,附属,";
						break;
					}
				}
				sj=sj.substring(0, sj.length()-1);
				name=name.substring(0, name.length());
				String names[]=name.split(",");
				String sql_sj = "select "+sj+"   from GC_XMGLGS_XXJD_JHBZ where jhsjid = '"+id+"'";
				String sjdata[][]=DBUtil.query(sql_sj);
				//List max_min = new ArrayList();
				List<String> max_min = new ArrayList<String>();
				if(null!=sjdata&&sjdata.length>0&&null!=sjdata[0][0])
				{
					for(int i=0;i<sjdata[0].length;i++)
					{
						if(null!=sjdata[0][i]&&!sjdata[0][i].equals(""))
						{
							max_min.add(sjdata[0][i]);													
						}	
					}
					if(max_min.size()>0)
					{
						Collections.sort(max_min);
						String min=max_min.get(0);
						String max=max_min.get(max_min.size()-1);
						///HashMap maxORmin= new HashMap();
						//maxORmin.put("min", min);
						//maxORmin.put("max", max);
						//Date maxDate = new SimpleDateFormat("yyyy-MM-dd").parse(max.substring(0, 10)); 
						//Date minDate = new SimpleDateFormat("yyyy-MM-dd").parse(min.substring(0, 10));
						//int mouthnum = (maxDate.getYear()-minDate.getYear())*12+maxDate.getMonth()-minDate.getMonth();
						json=ChartUtil.makeGanttEChartJsonString(sjdata, names,max,min);						
					}
					else
					{
						json=null;
					}	
				}	
			}	
			//BaseResultSet bs = DBUtil.query(conn, null, page);
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return json;
	}

	@Override
	public String queryBgxx(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
	        condition += BusinessUtil.getSJYXCondition(null);
	        condition += BusinessUtil.getCommonCondition(user,null);
	        condition += orderFilter;
	        if (page == null)
				page = new PageManager();
				page.setFilter(condition);
				conn.setAutoCommit(false);
			String sql = "select " +
					"gc_jh_bgbb_id, jhid, bgbbh, bgrq, bgsm, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx " +
					",gc_jh_bgbb_id FJ "+
					"from " +
					"GC_JH_BGBB";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldFileUpload("FJ","0060");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, YwlxManager.GC_JH_BGBB,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<版本信息>", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryBgxm(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			//String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			//condition += orderFilter;
			BusinessUtil.getSJYXCondition("t1");
			if (page == null)
				page = new PageManager();
			String sql = "SELECT " +
					" (select XMDZ from GC_TCJH_XMXDK where GC_TCJH_XMXDK_ID = t1.XMID and rownum=1) as XMDZ," +
					" (select JSNR from GC_TCJH_XMXDK where GC_TCJH_XMXDK_ID = t1.XMID and rownum=1) as JSNR," +
					" (select JSMB from GC_TCJH_XMXDK where GC_TCJH_XMXDK_ID = t1.XMID and rownum=1) as JSMB," +
					" (select XMGLGS from GC_TCJH_XMXDK where GC_TCJH_XMXDK_ID = t1.XMID and rownum=1) as XMGLGS, " +
					" t1.gc_jh_bgxm_id, t1.bgid, t1.jhid, t1.xmid, t1.bdid, t1.xmbh, t1.bdbh, t1.xmmc, t1.bdmc, t1.xmxz, t1.pxh, t1.kgsj, t1.kgsj_sj, t1.kgsj_bz, t1.wgsj, t1.wgsj_sj, t1.wgsj_bz, t1.kypf, t1.kypf_sj, t1.kypf_bz, t1.hpjds, t1.hpjds_sj, t1.hpjds_bz, t1.gcxkz, t1.gcxkz_sj, t1.gcxkz_bz, t1.sgxk, t1.sgxk_sj, t1.sgxk_bz, t1.cbsjpf, t1.cbsjpf_sj, t1.cbsjpf_bz, t1.cqt, t1.cqt_sj, t1.cqt_bz, t1.pqt, t1.pqt_sj, t1.pqt_bz, t1.sgt, t1.sgt_sj, t1.sgt_bz, t1.tbj, t1.tbj_sj, t1.tbj_bz, t1.cs, t1.cs_sj, t1.cs_bz, t1.jldw, t1.jldw_sj, t1.jldw_bz, t1.sgdw, t1.sgdw_sj, t1.sgdw_bz, t1.zc, t1.zc_sj, t1.zc_bz, t1.pq, t1.pq_sj, t1.pq_bz, t1.jg, t1.jg_sj, t1.jg_bz, t1.xmsx, t1.ywlx, t1.sjbh, t1.bz, t1.lrr, t1.lrsj, t1.lrbm, t1.lrbmmc, t1.gxr, t1.gxsj, t1.gxbm, t1.gxbmmc, t1.sjmj, t1.sfyx,t1.sfxg " +
					"FROM GC_JH_BGXM t1 " +
					"WHERE "+condition+" ORDER BY T1.PXH,t1.XMBH,T1.XMBS,T1.BDBH ASC";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldDic("SFXG", "SF");
			bs.setFieldDic("XMXZ", "XMXZ");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, YwlxManager.GC_JH_BGXM,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<版本项目>", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String updatebatchdata(String json, User user,String ywid,String jhid, String bgsm,String tids)
			throws Exception {
		Connection conn = DBUtil.getConnection();

		String resultVO = "";
		String bgbbId = "";
		EventVO eventVO = null;
		String xmbh = "";
		ZtVO zvo = null;
		try {
			conn.setAutoCommit(false);
			BgbbVO vo = new BgbbVO();
			//取得最大变更号
			String sql = "select max(BGBBH) from GC_JH_BGBB t where t.jhid ='"
					+ jhid + "'";
			String[][] str = DBUtil.query(conn, sql);
			int res = 0;
			if (str[0][0] != "" && str[0][0].length()>0) {
				res = Integer.parseInt(str[0][0]) + 1;
			}
			//================新增变更版本=================
			if(Pub.empty(ywid)){
				vo.setGc_jh_bgbb_id(new RandomGUID().toString());//变更版本ID
			}else{
				vo.setGc_jh_bgbb_id(ywid);
				FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setGlid2("");//存入计划数据ID
		        fvo.setGlid3(""); //存入项目ID
		        fvo.setGlid4(""); //存入标段ID
		        FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_jh_bgbb_id());
			}
			
			vo.setBgbbh(String.valueOf(res));//变更号
			vo.setBgrq(new Date());//变更日期
			vo.setBgsm(bgsm);//变更说明
			vo.setJhid(jhid);//计划ID
			vo.setYwlx(YwlxManager.GC_JH_BGBB);//业务类型
			eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);// 生成事件
			vo.setSjbh(eventVO.getSjbh());//事件编号
			BusinessUtil.setInsertCommonFields(vo,user);//公共方法
			BaseDAO.insert(conn, vo);
			bgbbId = vo.getGc_jh_bgbb_id();//获取变更版本ID
			//===========修改计划表============
			zvo = new ZtVO();
			zvo.setGc_jh_zt_id(jhid);
			zvo = (ZtVO)BaseDAO.getVOByPrimaryKey(conn,zvo);
			zvo.setMqbb(vo.getBgbbh());//目前版本 -等于变更版本号
			BaseDAO.update(conn, zvo);
			
			// ===========更新项目计划管理信息===========
			
			TcjhVO tcjhvo = new TcjhVO();
			JSONArray list = tcjhvo.doInitJson(json);
			List<String> listrow = new ArrayList<String>();
			TcjhVO tcjhVO = null;
			String tcID = "";
			for (int i = 0; i < list.size(); i++) {
				tcjhVO = new TcjhVO();
				tcjhVO.setValueFromJson((JSONObject) list.get(i));
				String xmmc = (String) tcjhVO.getXmmc();//获取项目名称
				String xmid = (String) tcjhVO.getXmid();//获取项目ID
				String xmbs = (String) tcjhVO.getXmbs();//获取项目标识
				if(tcjhVO.getIskgsj().equals("0")){
					tcjhVO.setKgsj(null);
					tcjhVO.setKgsj_sj(null);
				
				}else if(tcjhVO.getIswgsj().equals("0")){
					tcjhVO.setWgsj(null);
					tcjhVO.setWgsj_sj(null);
					
				}else if(tcjhVO.getIskypf().equals("0")){
					tcjhVO.setKypf(null);
					tcjhVO.setKypf_sj(null);
					
				}else if(tcjhVO.getIshpjds().equals("0")){
					tcjhVO.setHpjds(null);
					tcjhVO.setHpjds_sj(null);
					
				}else if(tcjhVO.getIsgcxkz().equals("0")){
					tcjhVO.setGcxkz(null);
					tcjhVO.setGcxkz_sj(null);
					
				}else if(tcjhVO.getIssgxk().equals("0")){
					tcjhVO.setSgxk(null);
					tcjhVO.setSgxk_sj(null);
					
				}else if(tcjhVO.getIscbsjpf().equals("0")){
					tcjhVO.setCbsjpf(null);
					tcjhVO.setCbsjpf_sj(null);
					
				}else if(tcjhVO.getIscqt().equals("0")){
					tcjhVO.setCqt(null);
					tcjhVO.setCqt_sj(null);
					
				}else if(tcjhVO.getIspqt().equals("0")){
					tcjhVO.setPqt(null);
					tcjhVO.setPqt_sj(null);
					
				}else if(tcjhVO.getIssgt().equals("0")){
					tcjhVO.setSgt(null);
					tcjhVO.setSgt_sj(null);
					
				}else if(tcjhVO.getIstbj().equals("0")){
					tcjhVO.setTbj(null);
					tcjhVO.setTbj_sj(null);
					
				}else if(tcjhVO.getIscs().equals("0")){
					tcjhVO.setCs(null);
					tcjhVO.setCs_sj(null);
					
				}else if(tcjhVO.getIsjldw().equals("0")){
					tcjhVO.setJldw(null);
					tcjhVO.setJldw_sj(null);
					
				}else if(tcjhVO.getIssgdw().equals("0")){
					tcjhVO.setSgdw(null);
					tcjhVO.setSgdw_sj(null);
					
				}else if(tcjhVO.getIszc().equals("0")){
					tcjhVO.setZc(null);
					tcjhVO.setZc_sj(null);
					
				}else if(tcjhVO.getIspq().equals("0")){
					tcjhVO.setPq(null);
					tcjhVO.setPq_sj(null);
					
				}else if(tcjhVO.getIsjg().equals("0")){
					tcjhVO.setJg(null);
					tcjhVO.setJg_sj(null);
				}
			
			String[] ss = xmmc.split("\\|\\|");
			if (ss.length > 1)
				tcjhVO.setXmmc(ss[1]);
			
			if(xmbs.equals("1")){
				BaseVO[] bv = null;
				TcjhVO condVO = new TcjhVO();
				condVO.setXmid(xmid);
				condVO.setXmbs("0");
				if(condVO!=null&&!condVO.isEmpty()){
					bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
					if(bv!=null){
						for(int j=0;j<bv.length;j++){
							TcjhVO newvo = (TcjhVO)bv[j];
							if(newvo.getIskgsj().equals("0")){
								tcjhVO.setIskgsj("0");
								tcjhVO.setKgsj(null);
								tcjhVO.setKgsj_sj(null);
							
							}else if(newvo.getIswgsj().equals("0")){
								tcjhVO.setIswgsj("0");
								tcjhVO.setWgsj(null);
								tcjhVO.setWgsj_sj(null);
								
							}else if(newvo.getIskypf().equals("0")){
								tcjhVO.setIskypf("0");
								tcjhVO.setKypf(null);
								tcjhVO.setKypf_sj(null);
								
							}else if(newvo.getIshpjds().equals("0")){
								tcjhVO.setIshpjds("0");
								tcjhVO.setHpjds(null);
								tcjhVO.setHpjds_sj(null);
								
							}else if(newvo.getIsgcxkz().equals("0")){
								tcjhVO.setIsgcxkz("0");
								tcjhVO.setGcxkz(null);
								tcjhVO.setGcxkz_sj(null);
								
							}else if(newvo.getIssgxk().equals("0")){
								tcjhVO.setIssgxk("0");
								tcjhVO.setSgxk(null);
								tcjhVO.setSgxk_sj(null);
								
							}else if(newvo.getIscbsjpf().equals("0")){
								tcjhVO.setIscbsjpf("0");
								tcjhVO.setCbsjpf(null);
								tcjhVO.setCbsjpf_sj(null);
								
							}else if(newvo.getIscqt().equals("0")){
								tcjhVO.setIscqt("0");
								tcjhVO.setCqt(null);
								tcjhVO.setCqt_sj(null);
								
							}else if(newvo.getIspqt().equals("0")){
								tcjhVO.setIspqt("0");
								tcjhVO.setPqt(null);
								tcjhVO.setPqt_sj(null);
								
							}else if(newvo.getIssgt().equals("0")){
								tcjhVO.setIssgt("0");
								tcjhVO.setSgt(null);
								tcjhVO.setSgt_sj(null);
								
							}else if(newvo.getIstbj().equals("0")){
								tcjhVO.setIstbj("0");
								tcjhVO.setTbj(null);
								tcjhVO.setTbj_sj(null);
								
							}else if(newvo.getIscs().equals("0")){
								tcjhVO.setIscs("0");
								tcjhVO.setCs(null);
								tcjhVO.setCs_sj(null);
								
							}else if(newvo.getIsjldw().equals("0")){
								tcjhVO.setIsjldw("0");
								tcjhVO.setJldw(null);
								tcjhVO.setJldw_sj(null);
								
							}else if(newvo.getIssgdw().equals("0")){
								tcjhVO.setIssgdw("0");
								tcjhVO.setSgdw(null);
								tcjhVO.setSgdw_sj(null);
								
							}else if(newvo.getIszc().equals("0")){
								tcjhVO.setIszc("0");
								tcjhVO.setZc(null);
								tcjhVO.setZc_sj(null);
								
							}else if(newvo.getIspq().equals("0")){
								tcjhVO.setIspq("0");
								tcjhVO.setPq(null);
								tcjhVO.setPq_sj(null);
								
							}else if(newvo.getIsjg().equals("0")){
								tcjhVO.setIsjg("0");
								tcjhVO.setJg(null);
								tcjhVO.setJg_sj(null);
							}
						}
					}
				}
			}
			
			
			BusinessUtil.setUpdateCommonFields(tcjhVO,user);
			BaseDAO.update(conn, tcjhVO);
				//================
				tcID += "'"+tcjhVO.getGc_jh_sj_id()+"',";
				String s[] =tids.split(","); 
				if ((xmbh != null && !xmbh.equals(tcjhVO.getXmbh()))||null!=tcjhVO.getBdbh()) {
					BgxmVO bvo = new BgxmVO();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
					//======================================
					JSONArray bvolist = bvo.doInitJson(tcjhVO.getRowJson());
					bvo.setValueFromJson((JSONObject)bvolist.get(0));
					bvo.setGc_jh_bgxm_id(new RandomGUID().toString());//变更项目ID
					for(int j = 0;j<s.length;j++){
						if(tcjhVO.getGc_jh_sj_id().equals(s[j])){
							bvo.setSfxg("1");//是否修改 1是，0否
							continue;
						}
					}
					//bvo.setSfxg("1");//是否修改 1是，0否
					bvo.setBgid(bgbbId);//变更版本ID
					bvo.setYwlx(YwlxManager.GC_JH_BGXM);//也类型
					eventVO = EventManager.createEvent(conn, bvo.getYwlx(),user);// 生成事件
					bvo.setSjbh(eventVO.getSjbh());//事件编号
					BusinessUtil.setInsertCommonFields(bvo,user);//公共方法
					BaseDAO.insert(conn, bvo);
					xmbh = (String) bvo.getXmbh();
				}

				tcjhVO.setXmmc(xmmc);
				listrow.add(tcjhVO.getRowJsonSingle());
			}
			/*tcID = tcID.substring(0, tcID.length()-1);
			String tcjhByPcsql = "SELECT GC_JH_SJ_ID from gc_jh_sj where jhid='"+jhid+"' and XFLX ='1' and GC_JH_SJ_ID not in("+tcID+")";
			BaseResultSet bs = DBUtil.query(conn, tcjhByPcsql,null);
			ResultSet rs = bs.getResultSet();
			while(rs.next()){
				String tcjhId = rs.getString("GC_JH_SJ_ID");
				tcjhVO = new TcjhVO();
				tcjhVO.setGc_jh_sj_id(tcjhId);
				tcjhVO = (TcjhVO)BaseDAO.getVOByPrimaryKey(conn,tcjhVO);
				BgxmVO bvo = new BgxmVO();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
				//======================================
				JSONArray bvolist = bvo.doInitJson(tcjhVO.getRowJson());
				bvo.setValueFromJson((JSONObject)bvolist.get(0));
				bvo.setGc_jh_bgxm_id(new RandomGUID().toString());//变更项目ID
				//bvo.setSfxg("1");//是否修改 1是，0否
				bvo.setBgid(bgbbId);//变更版本ID
				bvo.setYwlx(YwlxManager.GC_JH_BGXM);//也类型
				eventVO = EventManager.createEvent(conn, bvo.getYwlx(),user);// 生成事件
				bvo.setSjbh(eventVO.getSjbh());//事件编号
				BusinessUtil.setInsertCommonFields(bvo,user);//公共方法
				BaseDAO.insert(conn, bvo);
			}*/
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "添加<版本项目>成功", user, "", "");

			//
			resultVO = BaseDAO.comprisesResponseData(conn, listrow);

		}catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	@Override
	public String updatebatchdataNobg(String json, User user,String jhid)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String flag = "";
		String resultVO = "";
		EventVO eventVO = null;
		try {
			conn.setAutoCommit(false);
			// ===========更新项目计划管理信息===========
			TcjhVO tcjhvo = new TcjhVO();
			JSONArray list = tcjhvo.doInitJson(json);

			List<String> listrow = new ArrayList<String>();

			TcjhVO tcjhVO = null;
			for (int i = 0; i < list.size(); i++) {
				tcjhVO = new TcjhVO();
				tcjhVO.setValueFromJson((JSONObject) list.get(i));
				String xmmc = (String) tcjhVO.getXmmc();//获取项目名称
				String xmid = (String) tcjhVO.getXmid();//获取项目ID
				String xmbs = (String) tcjhVO.getXmbs();//获取项目标识
					if(tcjhVO.getIskgsj().equals("0")){
						tcjhVO.setKgsj(null);
						tcjhVO.setKgsj_sj(null);
					
					}else if(tcjhVO.getIswgsj().equals("0")){
						tcjhVO.setWgsj(null);
						tcjhVO.setWgsj_sj(null);
						
					}else if(tcjhVO.getIskypf().equals("0")){
						tcjhVO.setKypf(null);
						tcjhVO.setKypf_sj(null);
						
					}else if(tcjhVO.getIshpjds().equals("0")){
						tcjhVO.setHpjds(null);
						tcjhVO.setHpjds_sj(null);
						
					}else if(tcjhVO.getIsgcxkz().equals("0")){
						tcjhVO.setGcxkz(null);
						tcjhVO.setGcxkz_sj(null);
						
					}else if(tcjhVO.getIssgxk().equals("0")){
						tcjhVO.setSgxk(null);
						tcjhVO.setSgxk_sj(null);
						
					}else if(tcjhVO.getIscbsjpf().equals("0")){
						tcjhVO.setCbsjpf(null);
						tcjhVO.setCbsjpf_sj(null);
						
					}else if(tcjhVO.getIscqt().equals("0")){
						tcjhVO.setCqt(null);
						tcjhVO.setCqt_sj(null);
						
					}else if(tcjhVO.getIspqt().equals("0")){
						tcjhVO.setPqt(null);
						tcjhVO.setPqt_sj(null);
						
					}else if(tcjhVO.getIssgt().equals("0")){
						tcjhVO.setSgt(null);
						tcjhVO.setSgt_sj(null);
						
					}else if(tcjhVO.getIstbj().equals("0")){
						tcjhVO.setTbj(null);
						tcjhVO.setTbj_sj(null);
						
					}else if(tcjhVO.getIscs().equals("0")){
						tcjhVO.setCs(null);
						tcjhVO.setCs_sj(null);
						
					}else if(tcjhVO.getIsjldw().equals("0")){
						tcjhVO.setJldw(null);
						tcjhVO.setJldw_sj(null);
						
					}else if(tcjhVO.getIssgdw().equals("0")){
						tcjhVO.setSgdw(null);
						tcjhVO.setSgdw_sj(null);
						
					}else if(tcjhVO.getIszc().equals("0")){
						tcjhVO.setZc(null);
						tcjhVO.setZc_sj(null);
						
					}else if(tcjhVO.getIspq().equals("0")){
						tcjhVO.setPq(null);
						tcjhVO.setPq_sj(null);
						
					}else if(tcjhVO.getIsjg().equals("0")){
						tcjhVO.setJg(null);
						tcjhVO.setJg_sj(null);
					}
				
				String[] ss = xmmc.split("\\|\\|");
				if (ss.length > 1)
					tcjhVO.setXmmc(ss[1]);
				
				if(xmbs.equals("1")){
					BaseVO[] bv = null;
					TcjhVO condVO = new TcjhVO();
					condVO.setXmid(xmid);
					condVO.setXmbs("0");
					if(condVO!=null&&!condVO.isEmpty()){
						bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
						if(bv!=null){
							for(int j=0;j<bv.length;j++){
								TcjhVO newvo = (TcjhVO)bv[j];
								if(newvo.getIskgsj().equals("0")){
									tcjhVO.setIskgsj("0");
									tcjhVO.setKgsj(null);
									tcjhVO.setKgsj_sj(null);
								
								}else if(newvo.getIswgsj().equals("0")){
									tcjhVO.setIswgsj("0");
									tcjhVO.setWgsj(null);
									tcjhVO.setWgsj_sj(null);
									
								}else if(newvo.getIskypf().equals("0")){
									tcjhVO.setIskypf("0");
									tcjhVO.setKypf(null);
									tcjhVO.setKypf_sj(null);
									
								}else if(newvo.getIshpjds().equals("0")){
									tcjhVO.setIshpjds("0");
									tcjhVO.setHpjds(null);
									tcjhVO.setHpjds_sj(null);
									
								}else if(newvo.getIsgcxkz().equals("0")){
									tcjhVO.setIsgcxkz("0");
									tcjhVO.setGcxkz(null);
									tcjhVO.setGcxkz_sj(null);
									
								}else if(newvo.getIssgxk().equals("0")){
									tcjhVO.setIssgxk("0");
									tcjhVO.setSgxk(null);
									tcjhVO.setSgxk_sj(null);
									
								}else if(newvo.getIscbsjpf().equals("0")){
									tcjhVO.setIscbsjpf("0");
									tcjhVO.setCbsjpf(null);
									tcjhVO.setCbsjpf_sj(null);
									
								}else if(newvo.getIscqt().equals("0")){
									tcjhVO.setIscqt("0");
									tcjhVO.setCqt(null);
									tcjhVO.setCqt_sj(null);
									
								}else if(newvo.getIspqt().equals("0")){
									tcjhVO.setIspqt("0");
									tcjhVO.setPqt(null);
									tcjhVO.setPqt_sj(null);
									
								}else if(newvo.getIssgt().equals("0")){
									tcjhVO.setIssgt("0");
									tcjhVO.setSgt(null);
									tcjhVO.setSgt_sj(null);
									
								}else if(newvo.getIstbj().equals("0")){
									tcjhVO.setIstbj("0");
									tcjhVO.setTbj(null);
									tcjhVO.setTbj_sj(null);
									
								}else if(newvo.getIscs().equals("0")){
									tcjhVO.setIscs("0");
									tcjhVO.setCs(null);
									tcjhVO.setCs_sj(null);
									
								}else if(newvo.getIsjldw().equals("0")){
									tcjhVO.setIsjldw("0");
									tcjhVO.setJldw(null);
									tcjhVO.setJldw_sj(null);
									
								}else if(newvo.getIssgdw().equals("0")){
									tcjhVO.setIssgdw("0");
									tcjhVO.setSgdw(null);
									tcjhVO.setSgdw_sj(null);
									
								}else if(newvo.getIszc().equals("0")){
									tcjhVO.setIszc("0");
									tcjhVO.setZc(null);
									tcjhVO.setZc_sj(null);
									
								}else if(newvo.getIspq().equals("0")){
									tcjhVO.setIspq("0");
									tcjhVO.setPq(null);
									tcjhVO.setPq_sj(null);
									
								}else if(newvo.getIsjg().equals("0")){
									tcjhVO.setIsjg("0");
									tcjhVO.setJg(null);
									tcjhVO.setJg_sj(null);
								}
							}
						}
					}
				}
				
				
				BusinessUtil.setUpdateCommonFields(tcjhVO,user);
				BaseDAO.update(conn, tcjhVO);
				listrow.add(tcjhVO.getRowJsonSingle());
			}
			conn.commit();
			LogManager.writeUserLog(tcjhVO.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "更新<统筹计划数据>成功", user, "", "");

			//
			resultVO = BaseDAO.comprisesResponseData(conn, listrow);
		}catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	/* 
	 * 根据输入值从计划数据表自动模糊匹配项目名称
	 * @see com.ccthanking.business.tcjh.jhgl.service.TcjhService#xmmcAutoComplete(com.ccthanking.framework.model.autocomplete, com.ccthanking.framework.common.User)
	 */
	@Override
	public List<autocomplete> xmmcAutoComplete(autocomplete json,User user) throws Exception {
		List<autocomplete> autoResult = new ArrayList<autocomplete>(); 
		autocomplete ac = new autocomplete();
		String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
		condition += BusinessUtil.getSJYXCondition(json.getTablePrefix());
		condition += BusinessUtil.getCommonCondition(user,json.getTablePrefix());
		String [][] result = DBUtil.query("select distinct "+json.getTablePrefix()+".xmmc from GC_TCJH_XMXDK T2,GC_JH_SJ " + json.getTablePrefix() + " where XMID = T2.GC_TCJH_XMXDK_ID AND " + condition);
        if(null != result){
        	for(int i =0;i<result.length;i++){
        	  ac = new autocomplete();
              ac.setRegionName(result[i][0]);
              autoResult.add(ac);
        	}
        }
		return autoResult;
	}
	
	/* 
	 * 根据输入值从计划数据表自动模糊匹配项目名称(项目管理公司)
	 * @see com.ccthanking.business.tcjh.jhgl.service.TcjhService#xmmcAutoComplete(com.ccthanking.framework.model.autocomplete, com.ccthanking.framework.common.User)
	 */
	@Override
	public List<autocomplete> xmmcAutoQueryByXmglgs(autocomplete json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> autoResult = new ArrayList<autocomplete>(); 
		autocomplete ac = new autocomplete();
		String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
		String xmglgs = request.getParameter("xmglgs");
		if(!Pub.empty(xmglgs)){
			condition += " AND T9.XMGLGS = '"+xmglgs+"' ";
		}
		condition += BusinessUtil.getSJYXCondition("T");
		condition += BusinessUtil.getCommonCondition(user,"T");
		
		String [][] result = DBUtil.query("select distinct T.xmmc from GC_TCJH_XMXDK T9,GC_JH_SJ " + json.getTablePrefix() + " where XMID = T9.GC_TCJH_XMXDK_ID AND " + condition);
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
	public String queryJh(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		PageManager page = null;
		try {
			if (page == null)
				page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"gc_jh_zt_id, jhpch, nd, jhmc, isxf, xfbb, xfrq, scxfbb, scxfrq, mqbb, mbid, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx " +
					"FROM GC_JH_ZT " +
					"WHERE GC_JH_ZT_ID = '"+json+"'";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			conn.commit();
			LogManager.writeUserLog(null, YwlxManager.GC_JH_ZT,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "根据ID查询<统筹计划主体>", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	/**
	 * 计算项目无标段项目标识
	 * @param xmid 项目下达库主键
	 * @param conn 数据连接
	 */
	public static void calculateXmHasBd(String xmid,Connection conn) throws Exception{
		//查询项目标段数
		String sql = "select count(*) from GC_JH_SJ where XMID = ? and XMBS = '1' and SFYX = '1'";
		Object para[] = new Object[1];
		para[0] = xmid;
		String result [][] = DBUtil.querySql(conn, sql, para);
		String bds = result[0][0];
		//计算无标段项目标识
		int bdsl = Integer.parseInt(bds);//标段数
		String ISNOBDXM = bdsl > 0?"0":"1";//无标段项目标识
		//更新标识
		String updateSql = "update GC_JH_SJ set ISNOBDXM = ? where xmid = ? and SFYX = '1' ";
		para = new Object[2];
		para[0] = ISNOBDXM;
		para[1] = xmid;
		DBUtil.executeUpdate(conn, updateSql, para);
	}

	@Override
	public String deleteJh(HttpServletRequest request, User user) {
		Connection conn = DBUtil.getConnection();
		
		try {
			conn.setAutoCommit(false);
			
			String gc_jh_sj_id = request.getParameter("gc_jh_sj_id");
			
			String insertSql = "INSERT INTO GC_JH_SJ_DEL SELECT * FROM GC_JH_SJ T WHERE T.GC_JH_SJ_ID='"+gc_jh_sj_id+"'";
			String deleteSql = "DELETE FROM GC_JH_SJ WHERE GC_JH_SJ_ID='"+gc_jh_sj_id+"'";
//			String updateSql = "UPDATE GC_TCJH_XMXDK set ISNATC='0' where ";
			
			DBUtil.execUpdateSql(conn, insertSql);
			DBUtil.execUpdateSql(conn, deleteSql);
			
			conn.commit();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return null;
	}
}
