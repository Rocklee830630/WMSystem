package com.ccthanking.business.pqgl.service.impl;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.htgl.service.impl.GcHtglHtServiceImpl;
import com.ccthanking.business.jhfk.service.impl.JhfkCommon;
import com.ccthanking.business.pqgl.service.PqglService;
import com.ccthanking.business.pqgl.vo.PqInfoVO;
import com.ccthanking.business.pqgl.vo.PqJzVO;
import com.ccthanking.business.pqgl.vo.PqJzfkVO;
import com.ccthanking.business.pqgl.vo.PqWtVO;
import com.ccthanking.business.pqgl.vo.PqZxmVO;
import com.ccthanking.business.tcjh.jhgl.vo.FkqkVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.FjlbManager;
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
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class PqglServiceImpl implements PqglService{
	@Override
	public String queryProjectInfo(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			roleCond = " and X.GC_JH_SJ_ID=P.JHSJID(+) and X.BDID=B.GC_XMBD_ID(+) ";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("X") + BusinessUtil.getCommonCondition(user,null);
			condition +=orderFilter;
			page.setFilter(condition);
			String sql = "select distinct P.GC_PQ_ID,P.JHSJID,X.XMID,X.BDID, X.ND, P.sjsj_pq, P.cdyj_pq, P.ywlx, P.sjbh, P.bz, P.lrr, P.lrsj, P.lrbm, P.gxr, P.gxsj, P.gxbm, P.sjmj, P.sfyx,decode(P.ispq,null,X.ISPQ,P.ISPQ) ISPQ," +
					"X.XMMC,X.XMBH,X.BDMC,X.BDBH ,X.JHID,X.PQT_SJ PQT,X.PQ ,X.GC_JH_SJ_ID,X.PQ_SJ,X.Xmbs,X.pxh " +
					",X.WGSJ,B.BDDD,(case X.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = X.nd and GC_TCJH_XMXDK_ID = X.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = X.bdid and rownum = 1) end ) as XMBDDZ " +
					"from GC_JH_SJ X,GC_PQ P,GC_XMBD B";
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldDateFormat("PQT", "yyyy-MM-dd");
			bs.setFieldDateFormat("WGSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			bs.setFieldDic("SJZT", "WTZT");
			bs.setFieldUserID("JSR");
			domresult = bs.getJson();
			LogManager.writeUserLog("", YwlxManager.GC_PQ,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁计划查询成功", user,"","");
		} catch (Exception e) {
			LogManager.writeUserLog("", YwlxManager.GC_PQ,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁计划查询失败", user,"","");
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	/**
	 * 查询排迁信息
	 */
	@Override
	public String queryPqInfo(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			roleCond = " and  Z.JHSJID=S.GC_JH_SJ_ID(+) and S.BDID=B.GC_XMBD_ID(+) and Z.HTID=HT.ID(+) and HTSJ.HTID(+)=HT.ID";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("Z") + BusinessUtil.getCommonCondition(user,null);
			condition +=orderFilter;
			String sql = "";
			sql = "select distinct S.XMMC,S.BDMC,BDDD,S.XMBH,S.BDBH,S.WGSJ," +
					"Z.XMID,Z.BDID,Z.JHSJID," +
					" (case S.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = S.nd and GC_TCJH_XMXDK_ID = S.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = S.bdid and rownum = 1) end ) as XMBDDZ," + 
					"Z.GC_PQ_ZXM_ID,Z.JHID,Z.GC_PQ_ZXM_ID as ZXMID ,Z.KGSJ_JH,Z.WCSJ_JH,Z.QHRQ,Z.SFWTH,Z.GXR" +
					",Z.SSR,Z.SSDW,Z.SSRQ,Z.SSDBH,Z.LLDBH,Z.PSBGBH,Z.WTHBH,Z.SSZ,Z.LXR,Z.LXDH,Z.SDRQ,Z.SDZ,Z.SJZ,Z.HTID,Z.ND,Z.SJBH,Z.YWLX" +
					",Z.GXLB,Z.ZXMMC,Z.PQDD,Z.PXH,Z.PQFA,Z.JZQK,Z.SYGZL,Z.ISJHWC ISJHWC,Z.CZWT,Z.JJFA,Z.KGSJ,Z.WCSJ,Z.CDYJ_PQ,Z.XMFZR " +
					",GC_PQ_ZXM_ID ZYGXPQT,GC_PQ_ZXM_ID PQLLD,GC_PQ_ZXM_ID YSSSD  ,GC_PQ_ZXM_ID GCYSSDB  ,GC_PQ_ZXM_ID WTH  ,GC_PQ_ZXM_ID HT ,Z.BZ,Z.LRSJ " +
					",HT.HTBM,HT.HTJQDRQ ,HT.HTZT,HTSJ.HTQDJ,HTSJ.HTZF ,decode(SDZ,'',HTQDJ-nvl(HTZF,0),SDZ-nvl(HTZF,0)) as WFHTK,Z.HTSX " +
					"from GC_PQ_ZXM Z ,GC_JH_SJ S,GC_XMBD B,GC_HTGL_HT HT,GC_HTGL_HTSJ HTSJ";
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldDateFormat("WGSJ", "yyyy-MM-dd");
			bs.setFieldDateFormat("KGSJ_JH", "yyyy-MM-dd");
			bs.setFieldDateFormat("WCSJ_JH", "yyyy-MM-dd");
			bs.setFieldDateFormat("QHRQ", "yyyy-MM-dd");
			bs.setFieldDateFormat("HTJQDRQ", "yyyy-MM-dd");
			bs.setFieldDateFormat("DQRQ", "yyyy-MM-dd");
			
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			bs.setFieldUserID("XMFZR");
			bs.setFieldDic("ISJHWC", "SF");
			bs.setFieldDic("HTZT", "HTRXZT");//合同状态
			bs.setFieldDic("GXLB", "GXLB");	//管线类别
			bs.setFieldDic("HTSX", "HTSX");	//合同属性
			bs.setFieldThousand("SSZ");	//合同签订价
			bs.setFieldThousand("HTQDJ");	//合同签订价
			bs.setFieldThousand("SDZ");		//合同结算价
			bs.setFieldThousand("HTZF");	//已支付合同款
			bs.setFieldThousand("WFHTK");	//未支付合同款
			bs.setFieldFileUpload("ZYGXPQT","0001");
			bs.setFieldFileUpload("PQLLD","0002");
			bs.setFieldFileUpload("YSSSD","0003");
			bs.setFieldFileUpload("GCYSSDB","0004");
			bs.setFieldFileUpload("WTH","0005");
			bs.setFieldFileUpload("HT","0006");
			domresult = bs.getJson();
			LogManager.writeUserLog("", YwlxManager.GC_PQ_ZXM,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁子项目查询成功", user,"","");
		} catch (Exception e) {
			LogManager.writeUserLog("", YwlxManager.GC_PQ_ZXM,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁子项目查询失败", user,"","");
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	/**
	 *计划反馈
	 */
	@Override
	public String doJhfk(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqInfoVO vo = new PqInfoVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			String sql = "select count(*) from GC_PQ where jhsjid='"+vo.getJhsjid()+"'";
			String s = DBUtil.query(conn, sql)[0][0];
			if(s=="0"||"0".equals(s)){
				vo.setYwlx(YwlxManager.GC_PQ);
				EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
				vo.setSjbh(event.getSjbh());
				BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
			}else{
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
			}
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁计划反馈成功", user,"","");
			conn.commit();
			domresult = Pub.makeQueryConditionByID(vo.getJhsjid(), "P.JHSJID");
			domresult = this.queryProjectInfo(request, domresult);
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁计划反馈失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String insertPqzxm(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqZxmVO vo = new PqZxmVO();
		String ywid = request.getParameter("ywid");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			vo.setYwlx(YwlxManager.GC_PQ_ZXM);
			EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			vo.setSjbh(event.getSjbh());
			if(!Pub.empty(ywid)){
				vo.setGc_pq_zxm_id(ywid);
//				FileUploadService.updateFjztByYwid(conn, ywid);
				FileUploadVO fvo = new FileUploadVO();
				fvo.setFjzt("1");
				fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
				fvo.setGlid3(vo.getXmid());	//存入项目ID
				fvo.setGlid4(vo.getBdid());	//存入标段ID
				fvo.setSjbh(vo.getSjbh());	//存入时间编号
				fvo.setYwlx(vo.getYwlx());	//存入时间编号
				FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_pq_zxm_id(),user);
			}else{
				vo.setGc_pq_zxm_id(new RandomGUID().toString()); // 主键
			}
			vo.setXmfzr(user.getAccount());
			BusinessUtil.setInsertCommonFields(vo,user);
			BaseDAO.insert(conn, vo);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁子项目添加成功", user,"","");
			conn.commit();
			domresult = Pub.makeQueryConditionByID(vo.getGc_pq_zxm_id(), "Z.GC_PQ_ZXM_ID");
			domresult = this.queryPqInfo(request, domresult);
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁子项目添加失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String insertJzsy(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqJzVO vo = new PqJzVO();
		PqZxmVO zxmVO = new PqZxmVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			vo.setGc_pq_jz_id(new RandomGUID().toString()); // 主键
			vo.setYwlx(YwlxManager.GC_PQ_JZ);
			EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			vo.setSjbh(event.getSjbh());
			BusinessUtil.setInsertCommonFields(vo,user);
			BaseDAO.insert(conn, vo);
			zxmVO.setGc_pq_zxm_id(vo.getZxmid());
			zxmVO.setJzqk(vo.getJzqk());
			zxmVO.setSygzl(vo.getSygzl());
			BusinessUtil.setUpdateCommonFields(zxmVO,user);
			BaseDAO.update(conn, zxmVO);
			domresult = this.queryJzInfoByPk(conn, vo.getGc_pq_jz_id());
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁进展剩余添加成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁进展剩余添加失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String queryJzsy(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition(null);
			condition += BusinessUtil.getCommonCondition(user,null);
			condition +=orderFilter;
			String sql = "select gc_pq_jz_id, jhid, jhsjid, xmid, bdid, zxmid, jzrq, jzqk, sygzl, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx from  GC_PQ_JZ X ";
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("JZRQ", "yyyy-MM-dd");
			domresult = bs.getJson();
			LogManager.writeUserLog("", YwlxManager.GC_PQ_JZ,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁进展剩余查询成功", user,"","");
		} catch (Exception e) {
			LogManager.writeUserLog("", YwlxManager.GC_PQ_JZ,
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "排迁进展剩余查询失败", user,"","");
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String updateJzsy(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqJzVO vo = new PqJzVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo,user);
			BaseDAO.update(conn, vo);
			String sql = "select GC_PQ_JZ_ID from(select GC_PQ_JZ_ID from GC_PQ_JZ where zxmid='"+vo.getZxmid()+"' order by lrsj desc) where rownum=1";
			String lastID = DBUtil.query(conn, sql)[0][0];
			if(vo.getGc_pq_jz_id().equals(lastID)){
				PqZxmVO zxmVO = new PqZxmVO();
				zxmVO.setGc_pq_zxm_id(vo.getZxmid());
				zxmVO.setJzqk(vo.getJzqk());
				zxmVO.setSygzl(vo.getSygzl());
				BusinessUtil.setUpdateCommonFields(zxmVO,user);
				BaseDAO.update(conn, zxmVO);
			}
			domresult = this.queryJzInfoByPk(conn, vo.getGc_pq_jz_id());
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁进展剩余修改成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁进展剩余修改失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	/**
	 * 根据主键查询 进展剩余 数据
	 * @param conn
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String queryJzInfoByPk(Connection conn,String id) throws Exception {
		String domresult = "";
		PqJzVO vo = new PqJzVO();
		vo.setGc_pq_jz_id(id);
		vo = (PqJzVO)BaseDAO.getVOByPrimaryKey(conn, vo);
		domresult = vo.getRowJson();
		return domresult;
	}
	/**
	 * 根据主键查询 问题剩余 数据
	 * @param conn
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String queryWtInfoByPk(Connection conn,String id) throws Exception {
		String domresult = "";
		PqWtVO vo = new PqWtVO();
		vo.setGc_pq_wt_id(id);
		vo = (PqWtVO)BaseDAO.getVOByPrimaryKey(conn, vo);
		domresult = vo.getRowJson();
		return domresult;
	}
	/**
	 * 根据主键查询 子项目 数据
	 * @param conn
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String queryZxmByPk(Connection conn,String id) throws Exception {
		String domresult = "";
		PqZxmVO vo = new PqZxmVO();
		vo.setGc_pq_zxm_id(id);
		vo = (PqZxmVO)BaseDAO.getVOByPrimaryKey(conn, vo);
		domresult = vo.getRowJson();
		return domresult;
	}
	@Override
	public String insertWtjj(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqWtVO vo = new PqWtVO();
		PqZxmVO zxmVO = new PqZxmVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			vo.setGc_pq_wt_id(new RandomGUID().toString()); // 主键
			vo.setYwlx(YwlxManager.GC_PQ_WT);
//			vo.setXmfzr(user.getAccount());
			EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			vo.setSjbh(event.getSjbh());

			BusinessUtil.setInsertCommonFields(vo,user);
			BaseDAO.insert(conn, vo);
//			eventVO.setValueFromJson((JSONObject)list.get(0));
			zxmVO.setGc_pq_zxm_id(vo.getZxmid());
			zxmVO.setCzwt(vo.getCzwt());
			zxmVO.setJjfa(vo.getJjfa());
			BusinessUtil.setUpdateCommonFields(zxmVO,user);
			BaseDAO.update(conn, zxmVO);
			domresult = this.queryWtInfoByPk(conn, vo.getGc_pq_wt_id());
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁问题解决添加成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁问题解决添加失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryWtjj(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition(null);
			condition += BusinessUtil.getCommonCondition(user,null);
			condition +=orderFilter;
			String sql = "select gc_pq_wt_id, jhid, jhsjid, xmid, bdid, zxmid, jzrq, czwt, jjfa, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx from GC_PQ_WT X ";
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("JZRQ", "yyyy-MM-dd");
			domresult = bs.getJson();
			LogManager.writeUserLog("", YwlxManager.GC_PQ_WT,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁问题解决查询成功", user,"","");
		} catch (Exception e) {
			LogManager.writeUserLog("", YwlxManager.GC_PQ_WT,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁问题解决查询失败", user,"","");
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String updateWtjj(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqWtVO vo = new PqWtVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo,user);
			BaseDAO.update(conn, vo);
			domresult = this.queryWtInfoByPk(conn, vo.getGc_pq_wt_id());
			String sql = "select GC_PQ_WT_ID from(select GC_PQ_WT_ID from GC_PQ_WT where zxmid='"+vo.getZxmid()+"' order by lrsj desc) where rownum=1";
			String lastID = DBUtil.query(conn, sql)[0][0];
			if(vo.getGc_pq_wt_id().equals(lastID)){
				PqZxmVO zxmVO = new PqZxmVO();
				zxmVO.setGc_pq_zxm_id(vo.getZxmid());
				zxmVO.setCzwt(vo.getCzwt());
				zxmVO.setJjfa(vo.getJjfa());
				BusinessUtil.setUpdateCommonFields(zxmVO,user);
				BaseDAO.update(conn, zxmVO);
			}
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁问题解决修改成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁问题解决修改失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String doYnqk(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqZxmVO vo = new PqZxmVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			//记录排迁数据
			BusinessUtil.setUpdateCommonFields(vo,user);
			BaseDAO.update(conn, vo);
			//回写合同数据
			if(!Pub.empty(vo.getHtid())){
				HashMap map = new HashMap();
				map.put("htjsj", vo.getSdz());
				GcHtglHtServiceImpl htService = new GcHtglHtServiceImpl();
				htService.updateHtHtqjjs(user,vo.getHtid(),"",map);
			}
			//--记录附件新增数据项
			FileUploadVO fvo = new FileUploadVO();
			fvo.setFjzt("1");
			fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
			fvo.setGlid3(vo.getXmid());	//存入项目ID
			fvo.setGlid4(vo.getBdid());	//存入标段ID
			FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_pq_zxm_id(),user);
			//--附件数据项更新完毕
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁业内情况操作成功", user,"","");
			conn.commit();
			domresult = Pub.makeQueryConditionByID(vo.getGc_pq_zxm_id(), "Z.GC_PQ_ZXM_ID");
			domresult = this.queryPqInfo(request, domresult);
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁业内情况操作失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String updatePqzxm(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqZxmVO vo = new PqZxmVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo,user);
			BaseDAO.update(conn, vo);
			FileUploadVO fvo = new FileUploadVO();
			fvo.setFjzt("1");
			fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
			fvo.setGlid3(vo.getXmid());	//存入项目ID
			fvo.setGlid4(vo.getBdid());	//存入标段ID
			fvo.setSjbh(vo.getSjbh());	//存入时间编号
			fvo.setYwlx(vo.getYwlx());	//存入时间编号
			FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_pq_zxm_id(),user);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁子项目修改成功", user,"","");
			conn.commit();
			//由于更新和查询使用的是两个不同的connection，所以查询方法必须在更新方法提交后才能执行
			domresult = Pub.makeQueryConditionByID(vo.getGc_pq_zxm_id(), "Z.GC_PQ_ZXM_ID");
			domresult = this.queryPqInfo(request, domresult);
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁子项目修改失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryPqzxmByPk(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqJzfkVO vo = new PqJzfkVO();
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			domresult = this.queryZxmByPk(conn, vo.getZxmid());
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryJzfk(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition(null);
			condition += BusinessUtil.getCommonCondition(user,null);
			condition +=orderFilter;
			String sql = "select gc_pq_jzfk_id, jhid, jhsjid, xmid, bdid, zxmid, fkrq, jzqk, sygzl, czwt, jjfa, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx from gc_pq_jzfk J ";
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("FKRQ", "yyyy-MM-dd");
			domresult = bs.getJson();
			LogManager.writeUserLog("", YwlxManager.GC_PQ_JZ,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁进展剩余查询成功", user,"","");
		} catch (Exception e) {
			LogManager.writeUserLog("", YwlxManager.GC_PQ_JZ,
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "排迁进展剩余查询失败", user,"","");
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertJzfk(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqJzfkVO vo = new PqJzfkVO();
		PqZxmVO zxmVO = new PqZxmVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String ywid = request.getParameter("ywid");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			vo.setYwlx(YwlxManager.GC_PQ_JZ);
			EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			vo.setSjbh(event.getSjbh());
			if(!Pub.empty(ywid)){
				vo.setGc_pq_jzfk_id(ywid); // 主键
				FileUploadVO fvo = new FileUploadVO();
				fvo.setFjzt("1");
				fvo.setFjlb(FjlbManager.GC_PQ_JZFKXG);
				fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
				fvo.setGlid3(vo.getXmid());	//存入项目ID
				fvo.setGlid4(vo.getBdid());	//存入标段ID
				fvo.setSjbh(vo.getSjbh());
				fvo.setYwlx(vo.getYwlx());
				FileUploadService.updateVOByYwid(conn, fvo,ywid,user);
			}else{
				vo.setGc_pq_jzfk_id(new RandomGUID().toString()); // 主键
			}
			BusinessUtil.setInsertCommonFields(vo,user);
			BaseDAO.insert(conn, vo);

			zxmVO.setValueFromJson((JSONObject)list.get(0));
			zxmVO.setGc_pq_zxm_id(vo.getZxmid());
			zxmVO.setJzfkid(vo.getGc_pq_jzfk_id());
			BusinessUtil.setUpdateCommonFields(zxmVO,user);
			BaseDAO.update(conn, zxmVO);
			domresult = this.queryJzfkInfoByPk(conn, vo.getGc_pq_jzfk_id());
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁进展剩余添加成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁进展剩余添加失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String updateJzfk(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqJzfkVO vo = new PqJzfkVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo,user);
			BaseDAO.update(conn, vo);
			FileUploadVO fvo = new FileUploadVO();
			fvo.setFjzt("1");
			fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
			fvo.setGlid3(vo.getXmid());	//存入项目ID
			fvo.setGlid4(vo.getBdid());	//存入标段ID
			FileUploadService.updateVOByYwid(conn, fvo,vo.getGc_pq_jzfk_id(),user);
			String sql = "select GC_PQ_JZFK_ID from(select GC_PQ_JZFK_ID from GC_PQ_JZFK where zxmid='"+vo.getZxmid()+"' order by lrsj desc) where rownum=1";
			String lastID = DBUtil.query(conn, sql)[0][0];
			PqZxmVO tempVO = new PqZxmVO();
			if(vo.getGc_pq_jzfk_id().equals(lastID)){
				tempVO.setValueFromJson((JSONObject)list.get(0));
				PqZxmVO zxmVO = new PqZxmVO();
				zxmVO.setGc_pq_zxm_id(vo.getZxmid());
				zxmVO.setJzqk(vo.getJzqk());
				zxmVO.setSygzl(vo.getSygzl());
				zxmVO.setCzwt(vo.getCzwt());
				zxmVO.setJjfa(vo.getJjfa());
				zxmVO.setKgsj(tempVO.getKgsj());
				zxmVO.setWcsj(tempVO.getWcsj());
				zxmVO.setIsjhwc(tempVO.getIsjhwc());
				BusinessUtil.setUpdateCommonFields(zxmVO,user);
				BaseDAO.update(conn, zxmVO);
			}
			domresult = this.queryJzfkInfoByPk(conn, vo.getGc_pq_jzfk_id());
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁进展反馈修改成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁进展剩余修改失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	/**
	 * 根据主键查询 进展剩余 数据
	 * @param conn
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String queryJzfkInfoByPk(Connection conn,String id) throws Exception {
		String domresult = "";
		PqJzfkVO vo = new PqJzfkVO();
		vo.setGc_pq_jzfk_id(id);
		vo = (PqJzfkVO)BaseDAO.getVOByPrimaryKey(conn, vo);
		domresult = vo.getRowJson();
		return domresult;
	}
	@Override
	public String doJhsjfk(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String ywlx = YwlxManager.GC_PQ;
		FkqkVO vo = new FkqkVO();
		PqInfoVO infoVO = new PqInfoVO();
		TcjhVO jhVO = new TcjhVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			infoVO.setValueFromJson((JSONObject)list.get(0));
			EventVO event = EventManager.createEvent(conn, ywlx, user);//生成事件
			//--操作排迁信息表
			if("".equals(infoVO.getGc_pq_id())){
				infoVO.setGc_pq_id(new RandomGUID().toString()); // 主键
				infoVO.setYwlx(ywlx);
				infoVO.setSjbh(event.getSjbh());
				BusinessUtil.setInsertCommonFields(infoVO,user);
				BaseDAO.insert(conn, infoVO);
			}else{
//				infoVO.setGc_pq_id(arr[0][0]);
				BusinessUtil.setUpdateCommonFields(infoVO,user);
				BaseDAO.update(conn, infoVO);
			}
			//--操作反馈情况表
			vo.setGc_jh_fkqk_id(new RandomGUID().toString()); // 主键
			vo.setJhid(infoVO.getJhid());
			vo.setJhsjid(infoVO.getJhsjid());
			vo.setSjbh(infoVO.getSjbh());
			vo.setFkid(infoVO.getGc_pq_id());
			vo.setFkrq(Pub.getCurrentDate());
			vo.setFklx("1001001");//排迁时间反馈
			vo.setYwlx(ywlx);
			vo.setBz(infoVO.getBz());
			BusinessUtil.setInsertCommonFields(vo,user);
			BaseDAO.insert(conn, vo);
			//--操作统筹计划表
			jhVO.setGc_jh_sj_id(vo.getJhsjid());
			jhVO.setPq_sj(infoVO.getSjsj_pq());
			jhVO.setPq_bz(vo.getBz());
			jhVO.setIspq(infoVO.getIspq());
//			jhVO.setYwlx(ywlx);
			BusinessUtil.setUpdateCommonFields(jhVO,user);
			BaseDAO.update(conn, jhVO);
			//--自动反馈项目
			//JhfkCommon.doAutoFeedBackXM(conn, vo.getJhsjid(), "1001001",user);
			//----------------------------------------------------------------
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "统筹计划排迁情况反馈成功", user,"","");
			conn.commit();
			domresult = Pub.makeQueryConditionByID(vo.getJhsjid(), "P.JHSJID");
			domresult = this.queryProjectInfo(request, domresult);
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "统筹计划排迁情况反馈失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String deleteJzfk(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqJzfkVO vo = new PqJzfkVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			String sql = "select GC_PQ_JZFK_ID,JZQK,SYGZL,CZWT,JJFA from GC_PQ_JZFK where zxmid='"+vo.getZxmid()+"' order by lrsj desc";
			String[][] lastRow = DBUtil.query(conn, sql);
			if(lastRow!=null && vo.getGc_pq_jzfk_id().equals(lastRow[0][0])){
				PqZxmVO zxmVO = new PqZxmVO();
				zxmVO.setGc_pq_zxm_id(vo.getZxmid());
				if(lastRow.length>1){
					zxmVO.setJzfkid(lastRow[1][0]);
					zxmVO.setJzqk(lastRow[1][1]);
					zxmVO.setSygzl(lastRow[1][2]);
					zxmVO.setCzwt(lastRow[1][3]);
					zxmVO.setJjfa(lastRow[1][4]);
				}else{
					zxmVO.setJzfkid("");
					zxmVO.setJzqk("");
					zxmVO.setSygzl("");
					zxmVO.setCzwt("");
					zxmVO.setJjfa("");
				}
				BusinessUtil.setUpdateCommonFields(zxmVO,user);
				BaseDAO.update(conn, zxmVO);
			}
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getGc_pq_jzfk_id());
			FileUploadService.deleteByConditionVO(conn, fvo);
			BaseDAO.delete(conn, vo);
			//domresult = this.queryJzfkInfoByPk(conn, vo.getGc_pq_jzfk_id());
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁进展反馈删除成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁进展剩余删除失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String getJhfkCounts(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String jhsjid = request.getParameter("jhsjid");
		String ywlx = request.getParameter("ywlx");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sql = "select count(GC_JH_FKQK_ID) from GC_JH_FKQK where jhsjid='"+jhsjid+"' and ywlx='"+ywlx+"'";
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
	@Override
	public String getPqzxmCounts(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String jhsjid = request.getParameter("jhsjid");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sql = "select count(GC_PQ_ZXM_ID) from GC_PQ_ZXM where jhsjid='"+jhsjid+"' and SFYX='1'";
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
	@Override
	public String deletePqzxm(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		PqZxmVO vo = new PqZxmVO();
		BaseVO[] bv = null;
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			//删业务数据
			PqZxmVO delVO = new PqZxmVO();
			delVO.setGc_pq_zxm_id(vo.getGc_pq_zxm_id());
			delVO.setSfyx("0");
			BaseDAO.update(conn, delVO);
			//删除关联的进展反馈数据
			PqJzfkVO condVO = new PqJzfkVO();
			condVO.setZxmid(vo.getGc_pq_zxm_id());
			condVO.setSfyx("1");
			if(condVO!=null&&!condVO.isEmpty()){
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
			}
			if(bv!=null){
				for(int i=0;i<bv.length;i++){
					PqJzfkVO jzvo = (PqJzfkVO)bv[i];
					jzvo.setSfyx("0");
					BaseDAO.update(conn, jzvo);
					FileUploadVO fvo = new FileUploadVO();
					fvo.setYwid(jzvo.getGc_pq_jzfk_id());
					FileUploadService.deleteByConditionVO(conn, fvo);
				}
			}
			//删附件
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getGc_pq_zxm_id());
			FileUploadService.deleteByConditionVO(conn, fvo);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁子项目删除成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁子项目删除失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	/**
	 * 查询排迁导出信息
	 */
	@Override
	public String doCustomExportExcel(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			roleCond = " and  Z.JHSJID=S.GC_JH_SJ_ID(+) and S.BDID=B.GC_XMBD_ID(+) and Z.HTID=HT.ID(+) and HTSJ.HTID(+)=HT.ID";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("Z") + BusinessUtil.getCommonCondition(user,null);
			condition +=orderFilter;
			String sql = "";
			sql = "select distinct S.XMMC,S.BDMC,BDDD,S.XMBH,S.BDBH,S.WGSJ," +
					"Z.XMID,Z.BDID,Z.JHSJID," +
					"Z.GC_PQ_ZXM_ID,Z.JHID,Z.GC_PQ_ZXM_ID as ZXMID ,Z.KGSJ_JH,Z.WCSJ_JH,Z.QHRQ,Z.SFWTH,Z.GXR" +
					",Z.SSR,Z.SSDW,Z.SSRQ,Z.SSDBH,Z.LLDBH,Z.PSBGBH,Z.WTHBH,Z.SSZ,Z.LXR,Z.LXDH,Z.SDRQ,Z.SDZ,Z.SJZ,Z.HTID,Z.ND,Z.SJBH,Z.YWLX" +
					",Z.GXLB,(select DIC_VALUE from FS_DIC_TREE where DIC_NAME_CODE='GXLB' and DIC_CODE=Z.GXLB) GXLB_SV," +
					"Z.ZXMMC,Z.PQDD,Z.PXH,Z.PQFA,Z.JZQK,Z.SYGZL," +
					"Z.ISJHWC ISJHWC,(select DIC_VALUE from FS_DIC_TREE where DIC_NAME_CODE='SF' and DIC_CODE=Z.ISJHWC) ISJHWC_SV," +
					"Z.CZWT,Z.JJFA,Z.KGSJ,Z.WCSJ,Z.CDYJ_PQ," +
					"(select NAME from FS_ORG_PERSON P where P.ACCOUNT=Z.XMFZR) XMFZR " +
					",GC_PQ_ZXM_ID ZYGXPQT,GC_PQ_ZXM_ID PQLLD,GC_PQ_ZXM_ID YSSSD  ,GC_PQ_ZXM_ID GCYSSDB  ,GC_PQ_ZXM_ID WTH  ,GC_PQ_ZXM_ID HT ,Z.BZ,Z.LRSJ " +
					",HT.HTBM,HT.HTJQDRQ ," +
					"HT.HTZT,(select DIC_VALUE from FS_DIC_TREE where DIC_NAME_CODE='HTRXZT' and DIC_CODE=HT.HTZT) HTZT_SV," +
					"HTSJ.HTQDJ,HTSJ.HTZF ,decode(SDZ,'',HTQDJ-nvl(HTZF,0),SDZ-nvl(HTZF,0)) as WFHTK," +
					"Z.HTSX ,(select DIC_VALUE from FS_DIC_TREE where DIC_NAME_CODE='HTSX' and  DIC_CODE=Z.HTSX) HTSX_SV " +
					"from GC_PQ_ZXM Z ,GC_JH_SJ S,GC_XMBD B,GC_HTGL_HT HT,GC_HTGL_HTSJ HTSJ,FS_DIC_TREE T ";
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
//			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
//			bs.setFieldDateFormat("WGSJ", "yyyy-MM-dd");
//			bs.setFieldDateFormat("KGSJ_JH", "yyyy-MM-dd");
//			bs.setFieldDateFormat("WCSJ_JH", "yyyy-MM-dd");
//			bs.setFieldDateFormat("QHRQ", "yyyy-MM-dd");
//			bs.setFieldDateFormat("HTJQDRQ", "yyyy-MM-dd");
//			bs.setFieldDateFormat("DQRQ", "yyyy-MM-dd");
//			
//			bs.setFieldOrgDept("LRBM");
//			bs.setFieldUserID("LRR");
//			bs.setFieldUserID("XMFZR");
//			bs.setFieldDic("ISJHWC", "SF");
//			bs.setFieldDic("HTZT", "HTRXZT");//合同状态
//			bs.setFieldDic("GXLB", "GXLB");	//管线类别
//			bs.setFieldDic("HTSX", "HTSX");	//合同属性
			bs.setFieldThousand("SSZ");	//合同签订价
			bs.setFieldThousand("HTQDJ");	//合同签订价
			bs.setFieldThousand("SDZ");		//合同结算价
			bs.setFieldThousand("HTZF");	//已支付合同款
			bs.setFieldThousand("WFHTK");	//未支付合同款
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
