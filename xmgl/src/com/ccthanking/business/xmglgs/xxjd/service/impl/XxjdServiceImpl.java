package com.ccthanking.business.xmglgs.xxjd.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.tcjh.jhgl.GenerateGtt;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.business.xmglgs.xxjd.service.XxjdService;
import com.ccthanking.business.xmglgs.xxjd.vo.JhbzVO;
import com.ccthanking.business.xmglgs.xxjd.vo.JhfkVO;
import com.ccthanking.business.xmglgs.xxjd.vo.XxjdVO;
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
public class XxjdServiceImpl implements XxjdService {
	
	private String ywlx = YwlxManager.GC_XMGLGS_XXJD;//项目管理公司-形象进度管理

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
		/*	condition += " AND (/*t.XMGLGS = '"+xmglgs+"' or t.gc_jh_sj_id in (select tt.gc_jh_sj_id from gc_jh_sj tt where /*tt.xmbs = '0' and tt.xmid in (select xmid from gc_jh_sj j /*where j.xmglgs = '"+xmglgs+"'))) ";
		*/}
		condition += " AND T.XMID = T1.GC_TCJH_XMXDK_ID ";
		condition += BusinessUtil.getSJYXCondition("t");
	    condition += BusinessUtil.getCommonCondition(user,"t");
	    condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			
			
			String sql = "SELECT (select GC_JH_TJGL_ID from GC_JH_TJGL tjgl where tjgl.jhsjid=t.gc_jh_sj_id) GC_JH_TJGL_ID,(select TBJJG from GC_JH_TJGL tjgl where tjgl.jhsjid=t.gc_jh_sj_id) TBJJG,t.GC_JH_SJ_ID jhsjid," +
					"t.GC_JH_SJ_ID,t.XMBH,t.ND,t.XMID,t.BDID,t.BDBH,t.XMMC,t.BDMC,t.XMBS,t.KGSJ,t.WGSJ, " +
					"(case xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid  AND SFYX='1') when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = t.bdid  AND SFYX='1') end) SGDW,"+
					"(case xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid  AND SFYX='1') when '1' then (select jldw from GC_XMBD where GC_XMBD_ID = t.bdid  AND SFYX='1') end) JLDW," +
					"T1.GC_TCJH_XMXDK_ID,t1.YZDB,t1.JSMB,T1.XMLX,"+
					"(SELECT GC_XMGLGS_XXJD_ID FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS GC_XMGLGS_XXJD_ID,"+
					"(SELECT JZFK FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS JZFK,"+
					"(SELECT FKRQ FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS FKRQ,"+
					"(SELECT SJKGSJ FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS SJKGSJ,"+
					"(SELECT SJWGSJ FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS SJWGSJ,"+
					"(SELECT BZ FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS BZ,"+
					"(SELECT ZT FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS ZT,"+
					"(SELECT FXMS FROM GC_XMGLGS_XXJD WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS FXMS,"+
					"(SELECT GC_XMGLGS_XXJD_JHBZ_ID FROM GC_XMGLGS_XXJD_JHBZ WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS GC_XMGLGS_XXJD_JHBZ_ID,"+
					"(SELECT GC_XMGLGS_XXJD_JHFK_ID FROM GC_XMGLGS_XXJD_JHFK WHERE T.GC_JH_SJ_ID = JHSJID  AND SFYX='1')AS GC_XMGLGS_XXJD_JHFK_ID,"+
					"(SELECT XXJDID FROM GC_XMGLGS_XXJD_JHBZ WHERE T.GC_JH_SJ_ID = JHSJID AND SFYX='1')AS XXJDID,"+
					"(SELECT xmlx FROM GC_XMGLGS_XXJD_JHBZ WHERE T.GC_JH_SJ_ID = JHSJID AND SFYX='1')AS BZ_XMLX,"+
					"(SELECT ZT_FK FROM GC_XMGLGS_XXJD_JHFK WHERE T.GC_JH_SJ_ID = JHSJID AND SFYX='1')AS ZT_FK,"+
					"(SELECT ZT_BZ FROM GC_XMGLGS_XXJD_JHBZ WHERE T.GC_JH_SJ_ID = JHSJID AND SFYX='1')AS ZT_BZ,"+
					"(case t.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1) end ) as XMBDDZ ";
					sql += " FROM GC_JH_SJ t,GC_TCJH_XMXDK T1 ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//字典翻译
			//bs.setFieldDic("JLYF", "JLYF");
			bs.setFieldDic("ZT", "XXJDZT");
			bs.setFieldDic("TBJJG", "SF");
			bs.setFieldDic("ZT_FK", "XXJDZT");
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("ZT_BZ", "XXJDZT");
			//表选翻译
			//bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
			
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");

			//bs.setFieldDateFormat("KGSJ", "YYYY-MM-DD");
			//bs.setFieldDateFormat("WGSJ", "YYYY-MM-DD");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<工程计量信息>", user,"","");
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
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String resultVO = null;
		EventVO eventVO = null;
		XxjdVO vo = new XxjdVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			if(Pub.empty(vo.getGc_xmglgs_xxjd_id())){
				vo.setGc_xmglgs_xxjd_id(new RandomGUID().toString()); 
				vo.setYwlx(ywlx);
			    eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			    vo.setSjbh(eventVO.getSjbh());
				BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), ywlx,
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "新增<形象进度>成功", user,"","");
			}else{
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), ywlx,
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "修改<形象进度>成功", user,"","");
			}
			//如果状态为提交，反馈统筹计划数据时间节点
			if(vo.getZt().equals("1")){
				TcjhVO tvo = new TcjhVO();
				tvo.setGc_jh_sj_id(vo.getJhsjid());
				tvo = (TcjhVO)BaseDAO.getVOByPrimaryKey(conn,tvo);
				//回写实际时间
				tvo.setKgsj_sj(vo.getSjkgsj());
				tvo.setWgsj_sj(vo.getSjwgsj());
				BusinessUtil.setUpdateCommonFields(tvo,user);
				BaseDAO.update(conn, tvo);
			}
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
	public String queryByJhid(String json, HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		PageManager page = null;
		try {
			if (page == null)
				page = new PageManager();
			conn.setAutoCommit(false);
			String jhsjid = request.getParameter("jhsjid");
			if(!Pub.empty(jhsjid)){
				String sql = "SELECT " +
						"T.gc_xmglgs_xxjd_id, T.nd, T.xmid, T.bdid, T.jhsjid, T.xmmc, T.xmbh, T.sjdw, T.jldw, T.sgdw, T.jzfk, T.fkrq, T.fxms, T.sjkgsj, T.sjwgsj, T.bz, T.zt, T.ywlx, T.sjbh, T.lrr, T.lrsj, T.lrbm, T.lrbmmc, T.gxr, T.gxsj, T.gxbm, T.gxbmmc, T.sjmj, T.sfyx  " +
						",t1.KGSJ,t1.WGSJ,t2.YZDB,t2.JSMB"+
						" FROM GC_XMGLGS_XXJD T,GC_JH_SJ t1,GC_TCJH_XMXDK T2" +
						" WHERE t.jhsjid = '"+jhsjid+"' AND T.SFYX = '1' AND T1.GC_JH_SJ_ID = T.jhsjid AND T2.GC_TCJH_XMXDK_ID = T.XMID";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
				bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");
				bs.setFieldTranslater("sjdw", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				domresult = bs.getJson();
				conn.commit();
			}else{
				domresult = "";
			}
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	@Override
	public String queryJhbzByJhid(String json,HttpServletRequest request) throws Exception {
			Connection conn = DBUtil.getConnection();
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String domresult = "";
			PageManager page = null;
			try {
				if (page == null)
					page = new PageManager();
				conn.setAutoCommit(false);
				String jhbzid = request.getParameter("jhbzid");
				if(!Pub.empty(jhbzid)){
					String sql = "SELECT " +
							"t.gc_xmglgs_xxjd_jhbz_id, t.xmlx,t.nd, t.xmid, t.bdid, t.jhsjid, t.xmmc, t.xmbh, t.sjdw, t.jldw, t.sgdw, t.zj, t.zj_sj, t.zj_wt, t.ct, t.ct_sj, t.ct_wt, t.dz, t.dz_sj, t.dz_wt, t.zxl, t.zxl_sj, t.zxl_wt, t.zd, t.zd_sj, t.zd_wt, t.qtfs, t.qtfs_sj, t.qtfs_wt, t.tfht, t.tfht_sj, t.tfht_wt, t.dnssaz, t.dnssaz_sj, t.dnssaz_wt, t.dnlqpzc, t.dnlqpzc_sj, t.dnlqpzc_wt, t.dnfsssjzx, t.dnfsssjzx_sj, t.dnfsssjzx_wt, t.fdjc, t.fdjc_sj, t.fdjc_wt, t.fdmc, t.fdmc_sj, t.fdmc_wt, t.fzddlfs, t.fzddlfs_sj, t.fzddlfs_wt, t.whzj, t.whzj_sj, t.whzj_wt, t.zswm, t.zswm_sj, t.zswm_wt, t.gljhntzc, t.gljhntzc_sj, t.gljhntzc_wt, t.tfkw, t.tfkw_sj, t.tfkw_wt, t.ztdb, t.ztdb_sj, t.ztdb_wt, t.ztbqjcq, t.ztbqjcq_sj, t.ztbqjcq_wt, t.ztdcb, t.ztdcb_sj, t.ztdcb_wt, t.zdyjdsm, t.czzywt, t.xxjdid, t.zt_bz, t.zt_fk, t.fkrq, t.fkrid, t.fkrxm, t.ywlx, t.sjbh, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx, t.jhkfid, t.jd1, t.jd1_sj, t.jd1_wt, t.jd2, t.jd2_sj, t.jd2_wt, t.jd3, t.jd3_sj, t.jd3_wt, t.jd4, t.jd4_sj, t.jd4_wt, t.jd5, t.jd5_sj, t.jd5_wt, t.jd6, t.jd6_sj, t.jd6_wt   " +
							",t1.KGSJ,t1.WGSJ,t2.YZDB,t2.JSMB, tjjswcsj, tjsjwcsj, tjywyy, jcjswcsj, jcsjwcsj, jcywyy, mcjswcsj, mcsjwcsj, mcywyy, fzjswcsj, fzsjwcsj, fzywyy, zjcjswcsj, zjcsjwcsj, zjcywyy, dzjswcsj, dzsjwcsj, dzywyy, qlsbjswcsj, qlsbsjwcsj, qlsbywyy, qlfsjswcsj, qlfssjwcsj, qlfsywyy, tfjswcsj, tfsjwcsj, tfywyy, jgjswcsj, jgsjwcsj, jgywyy, fsjswcsj, fssjwcsj, fsywyy, "+
							" dljhkgsj,dlsjkgsj,dlkgywyy,dljhwgsj,dlsjwgsj,dlwgywyy,qljhkgsj,qlsjkgsj,qlkgywyy,qljhwgsj,qlsjwgsj,qlwgywyy," +
							" psjhkgsj,pssjkgsj,pskgywyy,psjhwgsj,pssjwgsj,pswgywyy,kgjhkgsj,kgsjkgsj,kgkgywyy,kgjhwgsj,kgsjwgsj,kgwgywyy " +
							", tjjhkgsj, tjsjkgsj, tjkgywyy, jcjhkgsj, jcsjkgsj, jckgywyy, mcjhkgsj, mcsjkgsj, mckgywyy, fzjhkgsj, fzsjkgsj, fzkgywyy, zjjhkgsj, zjsjkgsj, zjkgywyy, ctjjzjhkgsj, ctjjzsjkgsj, ctjjzkgywyy, qlsbjgjhkgsj, qlsbjgsjkgsj, qlsbjgkgywyy, qlfsjhkgsj, qlfssjkgsj, qlfskgywyy, tfjhkgsj, tfsjkgsj, tfkgywyy, jgjhkgsj, jgsjkgsj, jgkgywyy, fsjhkgsj, fssjkgsj, fskgywyy " +
							" FROM GC_XMGLGS_XXJD_JHBZ T,GC_JH_SJ t1,GC_TCJH_XMXDK T2" +
							" WHERE t.gc_xmglgs_xxjd_jhbz_id = '"+jhbzid+"' AND T.SFYX = '1' AND T1.GC_JH_SJ_ID = T.jhsjid AND T2.GC_TCJH_XMXDK_ID = T.XMID";
					BaseResultSet bs = DBUtil.query(conn, sql, page);
					//字典翻译
					bs.setFieldDic("zt_bz", "XXJDZT");
					bs.setFieldDic("zt_fk", "XXJDZT");
					bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
					bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");
					bs.setFieldTranslater("sjdw", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					domresult = bs.getJson();
				}
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String insertJhbz(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String xmlx=request.getParameter("xmlx");
		String resultVO = null;
		EventVO eventVO = null;
		JhbzVO vo = new JhbzVO();
		//ZdyjdVO zvo = null;
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			if(Pub.empty(vo.getGc_xmglgs_xxjd_jhbz_id())){
				vo.setGc_xmglgs_xxjd_jhbz_id(new RandomGUID().toString()); 
				vo.setXmlx(xmlx);
				vo.setYwlx(YwlxManager.GC_XMGLGS_XXJD_JHBZ);
			    eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			    vo.setSjbh(eventVO.getSjbh());
				BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), YwlxManager.GC_XMGLGS_XXJD_JHBZ,
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "新增<形象进度-计划编制>成功", user,"","");
				
			}else{
				BusinessUtil.setUpdateCommonFields(vo,user);
				vo.setXmlx(xmlx);
				BaseDAO.update(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), YwlxManager.GC_XMGLGS_XXJD_JHBZ,
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "修改<形象进度-计划编制>成功", user,"","");
			}
		/*	//如果状态为提交，添加反馈表数据，与编制表相同
			if(vo.getZt_bz().equals("1")){
				JhfkVO fvo = new JhfkVO();
				JSONArray bvolist = fvo.doInitJson(vo.getRowJson());
				fvo.setValueFromJson((JSONObject)bvolist.get(0));
				if(Pub.empty(vo.getJhkfid())){
					fvo.setGc_xmglgs_xxjd_jhfk_id(new RandomGUID().toString());
					fvo.setYwlx(YwlxManager.GC_XMGLGS_XXJD_JHFK);
					fvo.setJhbzid(vo.getGc_xmglgs_xxjd_jhbz_id());
				    eventVO = EventManager.createEvent(conn, fvo.getYwlx(), user);//生成事件
				    fvo.setSjbh(eventVO.getSjbh());
					BusinessUtil.setInsertCommonFields(fvo,user);
					BaseDAO.insert(conn, fvo);
					LogManager.writeUserLog(vo.getSjbh(), YwlxManager.GC_XMGLGS_XXJD_JHFK,
							Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
							user.getOrgDept().getDeptName() + " " + user.getName()
									+ "修改<形象进度-计划反馈>成功", user,"","");
					vo.setJhkfid(fvo.getGc_xmglgs_xxjd_jhfk_id());
					BaseDAO.update(conn, vo);
				}else{
					fvo.setGc_xmglgs_xxjd_jhfk_id(vo.getJhkfid());
					fvo = (JhfkVO)BaseDAO.getVOByPrimaryKey(conn,fvo);
					fvo.setZj(vo.getZj());
					fvo.setCt(vo.getCt());
					fvo.setDz(vo.getDz());
					fvo.setZxl(vo.getZxl());
					fvo.setZd(vo.getZd());
					fvo.setQtfs(vo.getQtfs());
					fvo.setTfht(vo.getTfht());
					fvo.setDnssaz(vo.getDnssaz());
					fvo.setDnlqpzc(vo.getDnlqpzc());
					fvo.setDnfsssjzx(vo.getDnfsssjzx());
					fvo.setFdjc(vo.getFdjc());
					fvo.setFdmc(vo.getFdmc());
					fvo.setFzddlfs(vo.getFzddlfs());
					fvo.setWhzj(vo.getWhzj());
					fvo.setZswm(vo.getZswm());
					fvo.setGljhntzc(vo.getGljhntzc());
					fvo.setTfkw(vo.getTfkw());
					fvo.setZtdb(vo.getZtdb());
					fvo.setZtbqjcq(vo.getZtbqjcq());
					fvo.setZtdcb(vo.getZtdcb());
					fvo.setJd1(vo.getJd1());
					fvo.setJd2(vo.getJd2());
					fvo.setJd3(vo.getJd3());
					fvo.setJd4(vo.getJd4());
					fvo.setJd5(vo.getJd5());
					fvo.setJd6(vo.getJd6());
					BusinessUtil.setUpdateCommonFields(fvo,user);
					BaseDAO.update(conn, fvo);
				}
				
			}*/
			resultVO = vo.getRowJson();
			conn.commit();
			String jsona = Pub.makeQueryConditionByID(vo.getJhsjid(), "t.GC_JH_SJ_ID");
            return query(jsona, request);
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	@Override
	public String queryJhbzByJhfkid(String json,HttpServletRequest request) throws Exception {
			Connection conn = DBUtil.getConnection();
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String domresult = "";
			PageManager page = null;
			try {
				if (page == null)
					page = new PageManager();
				conn.setAutoCommit(false);
				String jhfkId = request.getParameter("jhfkId");
				if(!Pub.empty(jhfkId)){
					String sql = "SELECT " +
							"t.gc_xmglgs_xxjd_jhfk_id, t.xmid, t.bdid, t.jhsjid, t.xmmc, t.xmbh, t.zj, t.zj_sj, t.zj_wt, t.ct, t.ct_sj, t.ct_wt, t.dz, t.dz_sj, t.dz_wt, t.zxl, t.zxl_sj, t.zxl_wt, t.zd, t.zd_sj, t.zd_wt, t.qtfs, t.qtfs_sj, t.qtfs_wt, t.tfht, t.tfht_sj, t.tfht_wt, t.dnssaz, t.dnssaz_sj, t.dnssaz_wt, t.dnlqpzc, t.dnlqpzc_sj, t.dnlqpzc_wt, t.dnfsssjzx, t.dnfsssjzx_sj, t.dnfsssjzx_wt, t.fdjc, t.fdjc_sj, t.fdjc_wt, t.fdmc, t.fdmc_sj, t.fdmc_wt, t.fzddlfs, t.fzddlfs_sj, t.fzddlfs_wt, t.whzj, t.whzj_sj, t.whzj_wt, t.zswm, t.zswm_sj, t.zswm_wt, t.gljhntzc, t.gljhntzc_sj, t.gljhntzc_wt, t.tfkw, t.tfkw_sj, t.tfkw_wt, t.ztdb, t.ztdb_sj, t.ztdb_wt, t.ztbqjcq, t.ztbqjcq_sj, t.ztbqjcq_wt, t.ztdcb, t.ztdcb_sj, t.ztdcb_wt, t.zdyjdsm, t.czzywt, t.xxjdid, t.zt_bz, t.zt_fk, t.fkrq, t.fkrid, t.fkrxm, t.ywlx, t.sjbh, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx, t.jhbzid, t.jd1, t.jd1_sj, t.jd1_wt, t.jd2, t.jd2_sj, t.jd2_wt, t.jd3, t.jd3_sj, t.jd3_wt, t.jd4, t.jd4_sj, t.jd4_wt, t.jd5, t.jd5_sj, t.jd5_wt, t.jd6, t.jd6_sj, t.jd6_wt " +
							" FROM GC_XMGLGS_XXJD_JHFK t" +
							" WHERE t.GC_XMGLGS_XXJD_JHFK_ID = '"+jhfkId+"' AND T.SFYX = '1'";
					BaseResultSet bs = DBUtil.query(conn, sql, page);
					//字典翻译
					bs.setFieldDic("zt_bz", "XXJDZT");
					bs.setFieldDic("zt_fk", "XXJDZT");
					bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");
					domresult = bs.getJson();
				}
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String insertJhfk(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String resultVO = null;
		JhfkVO vo = new JhfkVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo,user);
			BaseDAO.update(conn, vo);
			LogManager.writeUserLog(vo.getSjbh(), YwlxManager.GC_XMGLGS_XXJD_JHFK,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改<形象进度-计划反馈>成功", user,"","");
		
			//如果状态为提交，添加反馈表数据，与编制表相同
			if(vo.getZt_fk().equals("1")){
				/*JhbzVO bvo = new JhbzVO();
				bvo.setGc_xmglgs_xxjd_jhbz_id(vo.getJhbzid());
				
				bvo = (JhbzVO)BaseDAO.getVOByPrimaryKey(conn,vo);
				JSONArray bvolist = bvo.doInitJson(bvo.getRowJson());
				bvo.setValueFromJson((JSONObject)bvolist.get(0));
				BusinessUtil.setUpdateCommonFields(bvo,user);
				BaseDAO.update(conn, bvo);*/
				
			}
			resultVO = vo.getRowJson();
			conn.commit();
			String jsona = Pub.makeQueryConditionByID(vo.getJhsjid(), "t.GC_JH_SJ_ID");
            return query(jsona, request);
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	@Override
	public String queryZdyjdByJhbzid(String json,HttpServletRequest request) throws Exception {
			Connection conn = DBUtil.getConnection();
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String domresult = "";
			PageManager page = null;
			try {
				if (page == null)
					page = new PageManager();
				conn.setAutoCommit(false);
				String jhbzid = request.getParameter("jhbzid");
				if(!Pub.empty(jhbzid)){
					String sql = "SELECT " +
							"gc_xmglgs_xxjd_zdyjd_id, jdmc, jhsj, sjsj, czwt, ywlx, sjbh, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, jhbzid " +
							" FROM GC_XMGLGS_XXJD_ZDYJD " +
							" WHERE JHBZID = '"+jhbzid+"' AND SFYX = '1'";
					BaseResultSet bs = DBUtil.query(conn, sql, page);
					//字典翻译
					//bs.setFieldDic("zt_bz", "XXJDZT");
					//bs.setFieldDic("zt_fk", "XXJDZT");
					//bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");
					domresult = bs.getJson();
				}
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String delete(HttpServletRequest request, String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		JhbzVO vo = new JhbzVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			//删业务数据
			JhbzVO delVO = new JhbzVO();
			delVO.setGc_xmglgs_xxjd_jhbz_id(vo.getGc_xmglgs_xxjd_jhbz_id());
			delVO.setSfyx("0");
			BaseDAO.update(conn, delVO);
			/*//删附件
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getGc_gcgl_gcqs_id());
			FileUploadService.deleteByConditionVO(conn, fvo);*/
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "形象进度编制删除成功", user,"","");
			conn.commit();
			String jsona = Pub.makeQueryConditionByID(vo.getJhsjid(), "t.GC_JH_SJ_ID");
            return query(jsona, request);
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String ggtById(String id,User user) {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		String domresult = "";
		PageManager page = null;
		JhbzVO vo = new JhbzVO();
		try {
			if (page == null)
				page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "select bz.TJJSWCSJ, bz.TJSJWCSJ,bz.JCJSWCSJ,bz.JCSJWCSJ,bz.MCJSWCSJ,bz.MCSJWCSJ,bz.FZJSWCSJ,bz.FZSJWCSJ, " +
					"bz.ZJCJSWCSJ,bz.ZJCSJWCSJ,bz.DZJSWCSJ,bz.DZSJWCSJ,bz.QLSBJSWCSJ,bz.QLSBSJWCSJ,bz.QLFSJSWCSJ,bz.QLFSSJWCSJ, " +
					"bz.TFJSWCSJ,bz.TFSJWCSJ,bz.JGJSWCSJ,bz.JGSJWCSJ,bz.FSJSWCSJ,bz.FSSJWCSJ, " + 
					"t.kgsj,t.kgsj_sj,t.wgsj,t.wgsj_sj,t.xmlx " + 
					"from gc_xmglgs_xxjd_jhbz bz, view_gc_jh_sj t where bz.jhsjid=t.gc_jh_sj_id and  bz.jhsjid='"+id+"'";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			domresult = bs.getJson();
			JSONArray list = vo.doInitJson(domresult);
			JSONObject json = (JSONObject)list.get(0);
			
			String str = GenerateGtt.createXxjdGtt(json, json.get("XMLX").toString());

			resultVO = str;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
}
