package com.ccthanking.business.zjb.jsgl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccthanking.business.htgl.service.impl.GcHtglHtServiceImpl;
import com.ccthanking.business.htgl.vo.GcHtglHtsjVO;
import com.ccthanking.business.tcjh.jhgl.vo.FkqkVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
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
public class JieSuanServiceImpl implements JieSuanService {

	private String ywlx=YwlxManager.GC_ZJB_JS;
	// 提报价反馈
	private String ywlxtbfk=YwlxManager.GC_ZJB_TBJFK ;
    // 财审反馈
	private String ywlxcsfk=YwlxManager.GC_ZJB_CSFK ;
	@Override
	public String queryConditionJieSuan(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += BusinessUtil.getSJYXCondition("D") + BusinessUtil.getCommonCondition(user,null);
			if(!Pub.empty(condition)){
				condition +=" AND D.Htid = f.ID(+) AND D.XDKID=A.GC_TCJH_XMXDK_ID  AND d.bdid = E.GC_XMBD_ID(+) and g.gc_jh_sj_id=f.jhsjid AND f.htid=c.id ";
				
			}else{
				condition="   D.Htid = f.ID(+) AND D.XDKID=A.GC_TCJH_XMXDK_ID  AND d.bdid = E.GC_XMBD_ID(+) and g.gc_jh_sj_id=f.jhsjid AND f.htid=c.id";
			}
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select '' SBCWBL ,'' HTCEBL ,A.XMBH,decode(d.bdid,'',a.sgdw,e.sgdw) SGDW,  A.GC_TCJH_XMXDK_ID xmid,decode(d.bdid,'',a.JLDW,e.JLDW) JLDW,f.ID HTID,C.Fbfs HTQDFS, f.HTQDJ htje,c.HTMC,g.ND,D.gc_zjb_jsb_id, D.xdkid, D.bdid, A.xmmc, E.bdmc,  C.HTBM htbh, D.jszt, D.tbr, D.tbrdh, D.tbrq, D.tbje, D.sfwt," +
					"D.wtzxgs, D.yzsdrq, D.yzsdje, D.cssdrq, D.cssdje, D.csbgbh, D.sjsdrq, D.sjsdje, D.sjbgbh, D.jsqk, D.jsbz, D.ywlx, D.sjbh,D.GCQK, " +
					"D.bz, d.zt ,D.lrr, D.lrsj, D.sfyx,f.jhsjid,g.bdbh, TBJE-YZSDJE TBYZ, YZSDJE-CSSDJE YZCS, CSSDJE-SJSDJE CSSJ,TBJE-SJSDJE  TBSJ,d.TBCSRQ from gc_tcjh_xmxdk A,gc_htgl_ht C,gc_zjb_jsb D ,gc_xmbd E,GC_HTGL_HTSJ f ,gc_jh_sj g ";
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
	public String insertdemo(String json, User user,String ywid) throws Exception {
		
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjbJsbVO xmvo = new GcZjbJsbVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			
			xmvo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setInsertCommonFields(xmvo, user);
			xmvo.setYwlx(ywlx);//业务类型
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO.getSjbh());
			
			//设置主键
			if(!Pub.empty(ywid)){
				xmvo.setGc_zjb_jsb_id(ywid);
				
			    FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setGlid2(xmvo.getJhsjid());//存入计划数据ID	 
		        fvo.setGlid3(xmvo.getXdkid()); //存入项目ID
		        fvo.setGlid4(xmvo.getBdid()); //存入标段ID
		        fvo.setYwlx(ywlx);
		        fvo.setSjbh(eventVO.getSjbh());
		        
		        FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_zjb_jsb_id(),user);
				
			}else{
				xmvo.setGc_zjb_jsb_id(new RandomGUID().toString()); // 主键
			}
		 
			//插入
			BaseDAO.insert(conn, xmvo);
			resultVO = xmvo.getRowJson();
		
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "结算管理添加成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "结算管理添加失败", user,"","");
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zjb_jsb_id()+"\",\"fieldname\":\"gc_zjb_jsb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.queryConditionJieSuan(jsona,user);
			return resultXinXi;
	}

	
	@Override
	public String updatedemo(String json, User user) throws Exception {
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
			String resultXinXi=this.queryConditionJieSuan(jsona,user);
			return resultXinXi;
	}
	@Override
	public String queryhtxx(String json, User user)  throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += BusinessUtil.getSJYXCondition("GHH") + BusinessUtil.getCommonCondition(user,null);
			condition +="  and HTZT = '1' and gjs.wgsj_sj is not null and ghh.htlx='SG' and NOT EXISTS (SELECT * FROM GC_ZJB_JSB j WHERE j.htid = ghh2.ID)  ";
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT distinct ghh.* FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2" +
					" ON ghh.ID = ghh2.htid LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id = ghh2.jhsjid  ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
		     // 设置字典
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式
            bs.setFieldDic("BXQDWLX", "BXQDW");// 保修期单位：年、季、月、日
            bs.setFieldDic("QDNF", "XMNF");// 项目年份
            bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同
			domresult = bs.getJson();
	} catch (Exception e) {
		e.printStackTrace(System.out);
	
	} finally {
		DBUtil.closeConnetion(conn);
	}
	return domresult;
	}
	@Override
	public String queryhtxxzs(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += BusinessUtil.getSJYXCondition("GHH") + BusinessUtil.getCommonCondition(user,null);
			condition +="  and HTZT = '1' and gjs.wgsj_sj is not null and  ghh.htlx='SG' and NOT EXISTS (SELECT * FROM GC_ZJB_JSB j WHERE j.htid = ghh2.ID) ";
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT count(distinct(ghh.id)) zs FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2" +
					" ON ghh.ID = ghh2.htid LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id = ghh2.jhsjid  ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
		     // 设置字典
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式
            bs.setFieldDic("BXQDWLX", "BXQDW");// 保修期单位：年、季、月、日
            bs.setFieldDic("QDNF", "XMNF");// 项目年份
            bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同
			domresult = bs.getJson();
	} catch (Exception e) {
		e.printStackTrace(System.out);
	
	} finally {
		DBUtil.closeConnetion(conn);
	}
	return domresult;
	}

	@Override
	public String queryConditionHeTong(String json,User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += BusinessUtil.getSJYXCondition("A") + BusinessUtil.getCommonCondition(user,null);
			if(!Pub.empty(condition)){
				condition +=" and  a.gc_tcjh_xmxdk_id=c.xmxdkid and C.BDID=b.gc_xmbd_id(+) ";
				
			}else{
				condition="  a.gc_tcjh_xmxdk_id=c.xmxdkid and C.BDID=b.gc_xmbd_id(+)  ";
			}
		    condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = " select a.xmbh,a.nd,a.xmmc,a.xmlx,a.gc_tcjh_xmxdk_id XDKID,A.XMDZ,decode(c.bdid,'',a.JLDW,b.JLDW) JLDW,decode(c.bdid,'',a.sgdw,b.sgdw) sgdw,b.qdzh,b.bdmc,b.bdbh,b.gc_xmbd_id BDID,C.HTBH,c.htmc,c.htnf,c.HTJE ,c.CESHI_ZJB_id HTID from gc_tcjh_xmxdk  a,gc_xmbd b,ceshi_zjb c  ";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMLX", "XMLX");
			domresult = bs.getJson();
			/*LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "合同查询", user,"","");*/
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String deleteDyqk(String json, User user) {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjbJsbVO vo = new GcZjbJsbVO();

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
			//附件删除
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getGc_zjb_jsb_id());
			FileUploadService.deleteByConditionVO(conn, fvo);
			
			resultVO = vo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除结算成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return resultVO;
	}

	@Override
	public String updateHeTongZT(String json, User user) throws Exception {
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
			
			GcHtglHtsjVO htvo= new GcHtglHtsjVO();
			htvo.setId(vo1.getHtid());
			GcHtglHtsjVO htvo1=(GcHtglHtsjVO) BaseDAO.getVOByPrimaryKey(conn, htvo);
			
			 //回写合同数据
			HashMap map = new HashMap();
			map.put("htjsj", vo1.getSjsdje());
			GcHtglHtServiceImpl htService = new GcHtglHtServiceImpl();
			htService.updateHtHtqjjs(user,htvo1.getHtid(),vo1.getHtid(),map);
			
			
	     	xmvo.setZt("1");
			//修改 
			BaseDAO.update(conn, xmvo);
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "结算金额提交成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zjb_jsb_id()+"\",\"fieldname\":\"gc_zjb_jsb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.queryConditionJieSuan(jsona,user);
			return resultXinXi;
	}



	}


