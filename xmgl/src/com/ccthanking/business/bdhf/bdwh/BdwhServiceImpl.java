package com.ccthanking.business.bdhf.bdwh;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.bdhf.vo.BdwhVO;
import com.ccthanking.business.tcjh.jhgl.service.impl.TcjhServiceImpl;
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
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class BdwhServiceImpl   implements BdwhService {
	
	private String ywlx=YwlxManager.GC_XM_BD;
	/*	
	 * 普通查询
	 * 
	 */
	@Override
	public String queryList(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				//condition += " AND T.BDID = T1.GC_XMBD_ID ";
		        condition += BusinessUtil.getSJYXCondition("T");
		        condition += BusinessUtil.getCommonCondition(user,"T");
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);

				String sql = "SELECT T.GC_JH_SJ_ID,T.ND,T.PXH,T.XMID,T.XMBH,T.XMMC,T.BDID,T.BDBH,T.BDMC,T.JHID,T.XMGLGS,T.ISNOBDXM,T.XMBS,(SELECT GCZTFY FROM GC_XMBD WHERE GC_XMBD_ID= T.BDID)AS GCZTFY,";
				sql+="decode(t.xmbs,'1',(select BDDD from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1),(select XMDZ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1)) as BDDD,";
				sql+="decode(t.xmbs,'1',(select JSGM from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1),(select JSNR from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1)) as JSGM";
				sql+=" FROM GC_JH_SJ T";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
				bs.setFieldDic("ISNOBDXM", "SF");
				bs.setFieldThousand("GCZTFY");
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询标段", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	 
		} finally {
			if (conn != null)
				DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	/*	
	 * 点击项目列表查询标段
	 * 
	 */
	@Override
	public String querydemo_bd(String json,String xmid,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				PageManager page =RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
		        condition += BusinessUtil.getSJYXCondition(null);
		        condition += BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql = "SELECT ";				
				sql+="gc_xmbd_id, jhid, jhsjid, nd, xmid, bdbh, bdmc, qdzh, zdzh, cd, kd, gj, wgrq, qgrq, jgrq, gcztfy, sgdw, sgdwfzr, sgdwfzrlxfs, jldw, jldwfzr, jldwfzrlxfs, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, bddd, mj, sghtid, jlhtid, jsgm, sjdw, sjdwfzr, sjdwfzrlxfs, sgdwxmjl, sgdwxmjllxdh, sgdwjsfzr, sgdwjsfzrlxdh, jldwzj, jldwzjlxdh, jldwzjdb, jldwzjdblxdh, jldwaqjl, jldwaqjllxdh, jsgm_sj, xmglgs,BDBM,PRE_BDBM,BD_XMBM";
				sql += " FROM GC_XMBD";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
				bs.setFieldDic("ISNOBDXM", "SF");
				bs.setFieldThousand("GCZTFY");
				bs.setFieldThousand("CD");
				bs.setFieldThousand("KD");
				bs.setFieldThousand("GJ");
				bs.setFieldThousand("MJ");
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询项目下所对应的标段", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			if (conn != null)
				DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	
	/*	
	 * 插入标段
	 * 
	 */
	@Override
	public String insertdemo(String json,User user,String flag,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String jhsjid = request.getParameter("jhsjid");
		String resultVO = null;
		String bdid=null;
		String xmmc=null;
		String xmid=null;
		String sjbh=null;
		try {
				BdwhVO bdvo1 = new BdwhVO();
				JSONArray list_bd = bdvo1.doInitJson(json);
				bdvo1.setValueFromJson((JSONObject)list_bd.get(0));
				TcjhVO tcjhvo = new TcjhVO();
				tcjhvo.setGc_jh_sj_id(jhsjid);
				tcjhvo = (TcjhVO)BaseDAO.getVOByPrimaryKey(conn,tcjhvo);
				//JSONArray list_tc = tcjhvo1.doInitJson(json);
				
				for(int i=0;i<list_bd.size();i++){
					BdwhVO bdvo = new BdwhVO();
					bdvo.setValueFromJson((JSONObject)list_bd.get(i));	
					conn.setAutoCommit(false);
					//设置主键
					bdvo.setGc_xmbd_id(new RandomGUID().toString()); // 主键
					bdvo.setJhid(tcjhvo.getJhid());
					bdvo.setJhsjid(new RandomGUID().toString());
					bdvo.setXmid(tcjhvo.getXmid());
					bdvo.setNd(tcjhvo.getNd());
					bdvo.setBdbm(bdvo.getPre_bdbm()+bdvo.getBd_xmbm());
					BusinessUtil.setInsertCommonFields(bdvo, user);//公共字段插入
					bdvo.setYwlx(ywlx);
					bdvo.setBdwybh(new RandomGUID().toString());//jiangc 标段唯一编号
					EventVO eventVO = EventManager.createEvent(conn, bdvo.getYwlx(), user);//生成事件
					bdvo.setSjbh(eventVO.getSjbh());
					bdvo.setIsnrtj(tcjhvo.getIsnrtj());
					BaseDAO.insert(conn, bdvo);
					
					
					TcjhVO newTcjhvo = new TcjhVO();
					newTcjhvo.setGc_jh_sj_id(bdvo.getJhsjid());
					newTcjhvo.setBdid(bdvo.getGc_xmbd_id());
					newTcjhvo.setBdbh(bdvo.getBdbh());
					newTcjhvo.setBdmc(bdvo.getBdmc());
					newTcjhvo.setJhid(tcjhvo.getJhid());
					newTcjhvo.setNd(tcjhvo.getNd());
					newTcjhvo.setXmid(tcjhvo.getXmid());
//					newTcjhvo.setXmbh(tcjhvo.getXmbh());
					newTcjhvo.setXmbh(bdvo.getBdbm());	//保存新的项目编号，使用新的项目编码
					newTcjhvo.setXmmc(tcjhvo.getXmmc());
					newTcjhvo.setXmxz(tcjhvo.getXmxz());
					newTcjhvo.setXmsx(tcjhvo.getXmsx());
					newTcjhvo.setYwlx(YwlxManager.GC_JH_SJ);
					BusinessUtil.setInsertCommonFields(newTcjhvo, user);//公共字段插入
					eventVO = EventManager.createEvent(conn, newTcjhvo.getYwlx(), user);//生成事件
					newTcjhvo.setSjbh(eventVO.getSjbh());
					newTcjhvo.setBz(bdvo.getBz());
					newTcjhvo.setXflx(tcjhvo.getXflx());
					//newTcjhvo.setIsxf(tcjhvo.getIsxf());
					newTcjhvo.setXmglgs(bdvo.getXmglgs());
					newTcjhvo.setXmbs("1");
					newTcjhvo.setIskgsj(tcjhvo.getIskgsj());
					newTcjhvo.setIswgsj(tcjhvo.getIswgsj());
					newTcjhvo.setIskypf(tcjhvo.getIskypf());
					newTcjhvo.setIshpjds(tcjhvo.getIshpjds());
					newTcjhvo.setIsgcxkz(tcjhvo.getIsgcxkz());
					newTcjhvo.setIssgxk(tcjhvo.getIssgxk());
					newTcjhvo.setIscbsjpf(tcjhvo.getIscbsjpf());
					newTcjhvo.setIscqt(tcjhvo.getIscqt());
					newTcjhvo.setIspqt(tcjhvo.getIspqt());
					newTcjhvo.setIssgt(tcjhvo.getIssgt());
					newTcjhvo.setIstbj(tcjhvo.getIstbj());
					newTcjhvo.setIscs(tcjhvo.getIscs());
					newTcjhvo.setIsjldw(tcjhvo.getIsjldw());
					newTcjhvo.setIssgdw(tcjhvo.getIssgdw());
					newTcjhvo.setIszc(tcjhvo.getIszc());
					newTcjhvo.setIspq(tcjhvo.getIspq());
					newTcjhvo.setIsjg(tcjhvo.getIsjg());
					newTcjhvo.setSjwybh(bdvo.getBdwybh());//查了标段唯一编号
					newTcjhvo.setIsnrtj(tcjhvo.getIsnrtj());//查了标段唯一编号
					//newTcjhvo.setIsnobdxm("1");
					xmmc=newTcjhvo.getXmmc();
					xmid=newTcjhvo.getXmid();
					sjbh=newTcjhvo.getSjbh();
					BaseDAO.insert(conn, newTcjhvo);
					bdid=newTcjhvo.getBdid();
					log(bdvo,user);
					//同步计划表的项目编码
					Pub.doSynchronousXmbm(conn, bdvo.getGc_xmbd_id(), "3", bdvo.getBdbm());
				}
				TcjhServiceImpl.calculateXmHasBd(xmid, conn);//跟新统筹计划数据项目信息
				PushMessage.push(conn, request, PushMessage.ZTB_BDHF,"项目："+xmmc+"进行了标段划分。",null,sjbh,ywlx,xmid);
				conn.commit();
				if("1".equals(flag))
				{
					String jsona="{querycondition: {conditions: [{\"value\":\""+bdid+"\",\"fieldname\":\"Gc_xmbd_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
					resultVO=this.queryList(jsona,user);						
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
	
	/*	
	 * 标段标段修改
	 * 
	 */
	@Override
	public String update_bdbd(String json,HttpServletRequest request) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		BdwhVO bdvo = new BdwhVO();
		String jhsjid = request.getParameter("jhsjid");
		if(Pub.empty(jhsjid)){
			jhsjid = "";
		}
		try {
			conn.setAutoCommit(false);
			//标段更新
			JSONArray list_bd = bdvo.doInitJson(json);
			bdvo.setValueFromJson((JSONObject)list_bd.get(0));
			bdvo.setBdbm(bdvo.getPre_bdbm()+bdvo.getBd_xmbm());
			TcjhVO tcjhvo1 = new TcjhVO();
			tcjhvo1.setGc_jh_sj_id(bdvo.getJhsjid());
			tcjhvo1 = (TcjhVO)BaseDAO.getVOByPrimaryKey(conn,tcjhvo1);
			if(Pub.empty(bdvo.getGc_xmbd_id())){
				//设置主键
				bdvo.setGc_xmbd_id(new RandomGUID().toString()); // 主键
				bdvo.setJhsjid(new RandomGUID().toString());
				bdvo.setBdwybh(new RandomGUID().toString());//jiangc 标段唯一编号
				bdvo.setBdbm(bdvo.getPre_bdbm()+bdvo.getBd_xmbm());
				bdvo.setIsnrtj(tcjhvo1.getIsnrtj());
				BusinessUtil.setInsertCommonFields(bdvo, user);//公共字段插入
				bdvo.setYwlx(ywlx);
				EventVO eventVO = EventManager.createEvent(conn, bdvo.getYwlx(), user);//生成事件
				bdvo.setSjbh(eventVO.getSjbh());
				BaseDAO.insert(conn, bdvo);
				
				//=========新增取统筹计划项目信息===========
				TcjhVO tvo = new TcjhVO();
				TcjhVO tcjhvo = new TcjhVO();
				tcjhvo.setGc_jh_sj_id(jhsjid);
				tcjhvo = (TcjhVO)BaseDAO.getVOByPrimaryKey(conn,tcjhvo);
				
				tvo.setGc_jh_sj_id(bdvo.getJhsjid());//主键
				tvo.setBdbh(bdvo.getBdbh());
				tvo.setBdmc(bdvo.getBdmc());
				tvo.setXmglgs(bdvo.getXmglgs());
				tvo.setBz(bdvo.getBz());
				tvo.setBdid(bdvo.getGc_xmbd_id());
				tvo.setJhid(tcjhvo.getJhid());
				tvo.setNd(tcjhvo.getNd());
				tvo.setXmid(tcjhvo.getXmid());
//				tvo.setXmbh(tcjhvo.getXmbh());
				tvo.setXmbh(bdvo.getPre_bdbm()+bdvo.getBd_xmbm());	//项目编号使用新的规则
				tvo.setXmmc(tcjhvo.getXmmc());
				tvo.setXmxz(tcjhvo.getXmxz());
				tvo.setXmsx(tcjhvo.getXmsx());
				tvo.setYwlx(YwlxManager.GC_JH_SJ);
				BusinessUtil.setInsertCommonFields(tvo, user);//公共字段插入
				eventVO = EventManager.createEvent(conn, tvo.getYwlx(), user);//生成事件
				tvo.setSjbh(eventVO.getSjbh());
				tvo.setXflx(tcjhvo.getXflx());
				//newTcjhvo.setIsxf(tcjhvo.getIsxf());
				tvo.setXmbs("1");
				tvo.setIskgsj(tcjhvo.getIskgsj());
				tvo.setIswgsj(tcjhvo.getIswgsj());
				tvo.setIskypf(tcjhvo.getIskypf());
				tvo.setIshpjds(tcjhvo.getIshpjds());
				tvo.setIsgcxkz(tcjhvo.getIsgcxkz());
				tvo.setIssgxk(tcjhvo.getIssgxk());
				tvo.setIscbsjpf(tcjhvo.getIscbsjpf());
				tvo.setIscqt(tcjhvo.getIscqt());
				tvo.setIspqt(tcjhvo.getIspqt());
				tvo.setIssgt(tcjhvo.getIssgt());
				tvo.setIstbj(tcjhvo.getIstbj());
				tvo.setIscs(tcjhvo.getIscs());
				tvo.setIsjldw(tcjhvo.getIsjldw());
				tvo.setIssgdw(tcjhvo.getIssgdw());
				tvo.setIszc(tcjhvo.getIszc());
				tvo.setIspq(tcjhvo.getIspq());
				tvo.setIsjg(tcjhvo.getIsjg());
				tvo.setSjwybh(bdvo.getBdwybh());//查了标段唯一编号
				tvo.setIsnrtj(tcjhvo.getIsnrtj());//查了标段唯一编号
				BaseDAO.insert(conn, tvo);
				TcjhServiceImpl.calculateXmHasBd(tvo.getXmid(), conn);//跟新统筹计划数据项目信息
				LogManager.writeUserLog(bdvo.getSjbh(), bdvo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "新增标段信息成功", user,"","");
			}else{
				BusinessUtil.setUpdateCommonFields(bdvo, user);//公共字段更新
				jhsjid = bdvo.getJhsjid(); 
				BaseDAO.update(conn, bdvo);
				//统筹计划数据更新
				if(!Pub.empty(jhsjid)){
					TcjhVO tvo = new TcjhVO();
					tvo.setGc_jh_sj_id(jhsjid);
					tvo = (TcjhVO)BaseDAO.getVOByPrimaryKey(conn,tvo);
					tvo.setBdbh(bdvo.getBdbh());
					tvo.setBdmc(bdvo.getBdmc());
					tvo.setXmglgs(bdvo.getXmglgs());
					tvo.setBz(bdvo.getBz());
					tvo.setXmbh(bdvo.getPre_bdbm()+bdvo.getBd_xmbm());	//项目编号使用新的规则
					BusinessUtil.setUpdateCommonFields(tvo, user);//公共字段更新
					BaseDAO.update(conn, tvo);
				}
				LogManager.writeUserLog(bdvo.getSjbh(), bdvo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "修改标段信息成功", user,"","");
			}
			//同步计划表的项目编码
			Pub.doSynchronousXmbm(conn, bdvo.getGc_xmbd_id(), "3", bdvo.getBdbm());
			resultVO = bdvo.getRowJson();
			conn.commit();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		
		return resultVO;
	}

	/*	
	 * 根据标段ID查询标段信息
	 * 
	 */
	@Override
	public String queryBdxxByBdid(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String id = request.getParameter("id");
		try {
			conn.setAutoCommit(false);
			if(!Pub.empty(id)){
				String sql = "SELECT ";	
				sql +="(SELECT XMBH FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID)AS XMBH,";
				sql +="(SELECT XMMC FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID)AS XMMC,";
				sql +="(SELECT XMSX FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID)AS XMSX,";
				sql +="(SELECT XMLX FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID)AS XMLX,";
				sql +="(SELECT ISBT FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID)AS ISBT,";
				sql +="(SELECT XJXJ FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID)AS XJXJ,";
				sql+="gc_xmbd_id, jhid, jhsjid, nd, xmid, bdbh, bdmc, qdzh, zdzh, cd, kd, gj, wgrq, qgrq, jgrq, gcztfy, sgdw, sgdwfzr, sgdwfzrlxfs, jldw, jldwfzr, jldwfzrlxfs, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, bddd, mj, sghtid, jlhtid, jsgm, sjdw, sjdwfzr, sjdwfzrlxfs, sgdwxmjl, sgdwxmjllxdh, sgdwjsfzr, sgdwjsfzrlxdh, jldwzj, jldwzjlxdh, jldwzjdb, jldwzjdblxdh, jldwaqjl, jldwaqjllxdh, jsgm_sj, xmglgs,BDBM,PRE_BDBM,BD_XMBM ";
				sql += " FROM GC_XMBD T WHERE gc_xmbd_id = '"+id+"'";
				BaseResultSet bs = DBUtil.query(conn, sql, null);
				bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
				bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldDic("XMLX", "XMLX");//项目类型
				bs.setFieldDic("XMSX", "XMSX");//项目属性
				bs.setFieldDic("ISBT", "SF");//是否BT项目
				bs.setFieldDic("XJXJ", "XMXZ");//新建/续建
				//bs.setFieldThousand("CD");
				//bs.setFieldThousand("KD");
				//bs.setFieldThousand("GJ");
				//bs.setFieldThousand("MJ");
				bs.setFieldThousand("GCZTFY");
				domresult = bs.getJson();
			}else{
				domresult = "";
			}
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	public static void log(BdwhVO bdvo,User user) throws Exception {

		LogManager.writeUserLog(bdvo.getSjbh(), bdvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "添加标段成功", user,"","");
		
	}
}
