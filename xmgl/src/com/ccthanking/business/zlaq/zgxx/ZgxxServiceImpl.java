package com.ccthanking.business.zlaq.zgxx;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.zlaq.vo.JcxxVO;
import com.ccthanking.business.zlaq.vo.ZgxxVO;
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
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class ZgxxServiceImpl  implements ZgxxService{

	
	private String ywlx=YwlxManager.GC_ZLAQ_ZGB;
	/*	
	 * 普通查询
	 * 
	 */
	@Override
	public String query_zg(String json,User user,String flag) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				if("0".equals(flag))
				{
					condition+=" and zt>'1'";					
				}	
				condition += BusinessUtil.getSJYXCondition("jcb") + BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql = "select gc_zlaq_jcb_id, GC_JH_SJ_ID,xdkid, xmmc, bdid as bdbh,bdid, bdmc, jclx, jcgm, jcnr, czwt, jcb.jcbm,jcrq, jcdw, kcsjdw, jcb.zt, jcb.ywlx, jcb.sjbh, jcb.bz, jcb.lrr, jcb.lrsj, jcb.lrbm, jcb.lrbmmc, jcb.gxr, jcb.gxsj, jcb.gxbm, jcb.gxbmmc, jcb.sjmj, jcb.sfyx, jcb.jcr, xmbh, isczwt, nd, xmglgs, sgdw, fzr_sgdw, jldw, fzr_jldw, yzdb, jcb.sjdw, fzr_sjdw, fzr_glgs, lxfs_glgs, lxfs_sgdw, lxfs_jldw, lxfs_sjdw, jcbh,gc_zlaq_zgb_id, jcbid, tzrq, hfrq, fcrq, zgzt, cljy, xgrq,  fcr, hfnr, fcjl,  sjr, hfr, xgqx, zgbh,hfbh,fcbh,fcyj,zjbs,ajbs from GC_ZLAQ_JCB jcb left join  ( select gc_zlaq_zgb_id, jcbid, tzrq, hfrq, fcrq, zgzt, cljy, xgrq,  fcr, hfnr, fcjl,  sjr, hfr, xgqx, zgbh,hfbh,fcbh,lxbs,fcyj  from (select gc_zlaq_zgb_id, jcbid, tzrq, hfrq, fcrq, zgzt, cljy, xgrq,  fcr, hfnr, fcjl,  sjr, hfr, xgqx, zgbh ,hfbh,fcbh,lxbs,fcyj, row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t where sfyx = '1' ) temp where temp.row_flg='1')zgb on gc_zlaq_jcb_id= jcbid ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("ZT", "ZGZT");
				bs.setFieldDic("ZGZT", "ZGZT");
				bs.setFieldDic("JCGM", "JCGM");
				bs.setFieldDic("JCLX", "JCLX");
				bs.setFieldDic("XMLX", "XMLX");
				bs.setFieldDic("ND","XMNF");
				bs.setFieldDic("XMSX", "XMSX");
				bs.setFieldDic("ISXD","XMZT");
				bs.setFieldDic("ISBT", "SF");
				bs.setFieldDic("SFYX", "SF");
				bs.setFieldDic("FCJL", "FCJL");
				bs.setFieldDic("ISCZWT", "SF");
				bs.setFieldDic("JCBM", "ZZDW");
				bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JCBM", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				//bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				/*bs.setFieldDic("XMGLGS", "XMGLGS");*/
				bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
				bs.setFieldTranslater("FZR_GLGS", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("FCR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("FZR_JLDW", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("FZR_SGDW", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("LXFS_JLDW", "FS_ORG_PERSON", "ACCOUNT", "SJHM");
				bs.setFieldTranslater("LXFS_SGDW", "FS_ORG_PERSON", "ACCOUNT", "SJHM");
				bs.setFieldTranslater("BDBH", "GC_XMBD", "GC_XMBD_ID", "BDBH");
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询质量整改信息", user,"","");

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
	 * 新增整改通知
	 * 
	 */
	@Override
	public String insert_zg(String json,User user,String ywid) throws Exception {
		Connection conn = DBUtil.getConnection();
		ZgxxVO xmvo = new ZgxxVO();
		JcxxVO jcxmvo = new JcxxVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			jcxmvo.setValueFromJson((JSONObject)list.get(0));
			jcxmvo.setZt("2");
			jcxmvo.setIsczwt("1");
			xmvo.setValueFromJson((JSONObject)list.get(0));
			xmvo.setJcbid(jcxmvo.getGc_zlaq_jcb_id());
			xmvo.setGc_zlaq_zgb_id(new RandomGUID().toString()); //设置主键
			BusinessUtil.setInsertCommonFields(xmvo, user);//公共字段插入
			xmvo.setYwlx(ywlx);
			xmvo.setLxbs("1");
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO.getSjbh());
			
			
			FileUploadVO vo = new FileUploadVO();
			vo.setYwid(jcxmvo.getGc_zlaq_jcb_id());
			vo.setFjzt("1");
	        vo.setGlid2(jcxmvo.getGc_jh_sj_id());//存入计划数据ID
	        vo.setGlid3(jcxmvo.getXdkid()); //存入项目ID
	        vo.setGlid4(jcxmvo.getBdid()); //存入标段ID
	        vo.setSjbh(xmvo.getSjbh());//事件编号
	        vo.setYwlx(YwlxManager.GC_ZLAQ_JC);
			FileUploadService.updateVOByYwid(conn, vo, ywid,user);
			
			BaseDAO.update(conn, jcxmvo);
			BaseDAO.insert(conn, xmvo);//插入
			
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "发送整改通知成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "发送整改通知失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zlaq_zgb_id()+"\",\"fieldname\":\"Gc_zlaq_zgb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_zg(jsona,user,"1");
		return resultXinXi;
	}
	
	/*	
	 * 修改整改通知
	 * 
	 */
	@Override
	public String update_zg(String json,User user,String ywid) throws Exception {
		Connection conn = DBUtil.getConnection();
		ZgxxVO xmvo = new ZgxxVO();
		JcxxVO jcxmvo = new JcxxVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list_jc = jcxmvo.doInitJson(json);
			jcxmvo.setValueFromJson((JSONObject)list_jc.get(0));
			jcxmvo.setZt("2");	
			BusinessUtil.setUpdateCommonFields(jcxmvo, user);//公共字段更新
			JSONArray list_zg = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list_zg.get(0));
			xmvo.setLxbs("1");
			BusinessUtil.setUpdateCommonFields(xmvo, user);//公共字段更新
			BaseDAO.update(conn, jcxmvo);
			BaseDAO.update(conn, xmvo);
			FileUploadVO vo = new FileUploadVO();
			vo.setYwid(jcxmvo.getGc_zlaq_jcb_id());
			vo.setFjzt("1");
	        vo.setGlid2(jcxmvo.getGc_jh_sj_id());//存入计划数据ID
	        vo.setGlid3(jcxmvo.getXdkid()); //存入项目ID
	        vo.setGlid4(jcxmvo.getBdid()); //存入标段ID
	        vo.setSjbh(xmvo.getSjbh());//事件编号
	        vo.setYwlx(YwlxManager.GC_ZLAQ_JC);

			FileUploadService.updateVOByYwid(conn, vo, ywid,user);

			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改整改通知成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改整改通知失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zlaq_zgb_id()+"\",\"fieldname\":\"Gc_zlaq_zgb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_zg(jsona,user,"1");
		return resultXinXi;
	}


	
	
	/*	
	 * 新增回复
	 * 
	 */
	@Override
	public String insert_hf(String json,User user,String ywid,String flag) throws Exception {
		Connection conn = DBUtil.getConnection();
		ZgxxVO xmvo = new ZgxxVO();
		JcxxVO jcxmvo = new JcxxVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list_jc = jcxmvo.doInitJson(json);
			jcxmvo.setValueFromJson((JSONObject)list_jc.get(0));
			if("1".equals(flag))
			{
				jcxmvo.setZt("4");//已提交
			}
			else
			{
				jcxmvo.setZt("3");//未提交
			}
			JSONArray list = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list.get(0));
			xmvo.setJcbid(jcxmvo.getGc_zlaq_jcb_id());
			xmvo.setGc_zlaq_zgb_id(new RandomGUID().toString()); //设置主键
			BusinessUtil.setInsertCommonFields(xmvo, user);//公共字段插入
			xmvo.setYwlx(ywlx);
			xmvo.setLxbs("2");
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO.getSjbh());
			
			FileUploadVO vo = new FileUploadVO();
			vo.setYwid(jcxmvo.getGc_zlaq_jcb_id());
			vo.setFjzt("1");
	        vo.setGlid2(jcxmvo.getGc_jh_sj_id());//存入计划数据ID
	        vo.setGlid3(jcxmvo.getXdkid()); //存入项目ID
	        vo.setGlid4(jcxmvo.getBdid()); //存入标段ID
	        vo.setSjbh(xmvo.getSjbh());//事件编号
	        vo.setYwlx(YwlxManager.GC_ZLAQ_JC);
			FileUploadService.updateVOByYwid(conn, vo, ywid,user);

			BaseDAO.update(conn, jcxmvo);
			BaseDAO.insert(conn, xmvo);//插入
			
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "发送整改通知成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "发送整改通知失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zlaq_zgb_id()+"\",\"fieldname\":\"Gc_zlaq_zgb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_zg(jsona,user,"0");
		return resultXinXi;
	}
	
	/*	
	 * 回复修改
	 * 
	 */
	@Override
	public String update_hf(String json,User user,String ywid,String flag) throws Exception {
		Connection conn = DBUtil.getConnection();
		ZgxxVO xmvo = new ZgxxVO();
		JcxxVO jcxmvo = new JcxxVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list_jc = jcxmvo.doInitJson(json);
			jcxmvo.setValueFromJson((JSONObject)list_jc.get(0));
			if("1".equals(flag))
			{
				jcxmvo.setZt("4");//已提交
			}
			else
			{
				jcxmvo.setZt("3");//未提交
			}
			JSONArray list = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(xmvo, user);//公共字段更新
			
			FileUploadVO vo = new FileUploadVO();
			vo.setYwid(jcxmvo.getGc_zlaq_jcb_id());
			vo.setFjzt("1");
	        vo.setGlid2(jcxmvo.getGc_jh_sj_id());//存入计划数据ID
	        vo.setGlid3(jcxmvo.getXdkid()); //存入项目ID
	        vo.setGlid4(jcxmvo.getBdid()); //存入标段ID
	        vo.setSjbh(xmvo.getSjbh());//事件编号
	        vo.setYwlx(YwlxManager.GC_ZLAQ_JC);
			FileUploadService.updateVOByYwid(conn, vo, ywid,user);
			
			
			BaseDAO.update(conn, jcxmvo);
			BaseDAO.update(conn, xmvo);
			FileUploadService.updateFjztByYwid(conn, jcxmvo.getGc_zlaq_jcb_id());
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "操作结果描述请写在这里", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "操作结果描述请写在这里", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zlaq_zgb_id()+"\",\"fieldname\":\"Gc_zlaq_zgb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_zg(jsona,user,"0");
		return resultXinXi;
	}

	
	/*	
	 * 新增复查
	 * 
	 */
	@Override
	public String insert_fc(String json,User user,String ywid) throws Exception {
		Connection conn = DBUtil.getConnection();
		ZgxxVO xmvo = new ZgxxVO();
		JcxxVO jcxmvo = new JcxxVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			jcxmvo.setValueFromJson((JSONObject)list.get(0));
			jcxmvo.setZt("5");
			xmvo.setValueFromJson((JSONObject)list.get(0));
			xmvo.setJcbid(jcxmvo.getGc_zlaq_jcb_id());
			xmvo.setGc_zlaq_zgb_id(new RandomGUID().toString()); //设置主键
			BusinessUtil.setInsertCommonFields(xmvo, user);//公共字段插入
			xmvo.setYwlx(ywlx);
			xmvo.setLxbs("3");
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO.getSjbh());
			
			
			FileUploadVO vo = new FileUploadVO();
			vo.setYwid(jcxmvo.getGc_zlaq_jcb_id());
			vo.setFjzt("1");
	        vo.setGlid2(jcxmvo.getGc_jh_sj_id());//存入计划数据ID
	        vo.setGlid3(jcxmvo.getXdkid()); //存入项目ID
	        vo.setGlid4(jcxmvo.getBdid()); //存入标段ID
	        vo.setSjbh(xmvo.getSjbh());//事件编号
	        vo.setYwlx(YwlxManager.GC_ZLAQ_JC);
			FileUploadService.updateVOByYwid(conn, vo, ywid,user);
			
			BaseDAO.update(conn, jcxmvo);
			BaseDAO.insert(conn, xmvo);//插入
			
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "发送整改通知成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "发送整改通知失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zlaq_zgb_id()+"\",\"fieldname\":\"Gc_zlaq_zgb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_zg(jsona,user,"1");
		return resultXinXi;
	}
	
	/*	
	 * 修改复查
	 * 
	 */
	@Override
	public String update_fc(String json,User user,String ywid) throws Exception {
		Connection conn = DBUtil.getConnection();
		ZgxxVO xmvo = new ZgxxVO();
		JcxxVO jcxmvo = new JcxxVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list.get(0));
			JSONArray list_jc = jcxmvo.doInitJson(json);
			jcxmvo.setValueFromJson((JSONObject)list_jc.get(0));
			BusinessUtil.setUpdateCommonFields(xmvo, user);//公共字段更新			
			FileUploadVO vo = new FileUploadVO();
			vo.setYwid(jcxmvo.getGc_zlaq_jcb_id());
			vo.setFjzt("1");
	        vo.setGlid2(jcxmvo.getGc_jh_sj_id());//存入计划数据ID
	        vo.setGlid3(jcxmvo.getXdkid()); //存入项目ID
	        vo.setGlid4(jcxmvo.getBdid()); //存入标段ID
	        vo.setSjbh(jcxmvo.getSjbh());//事件编号
	        vo.setYwlx(YwlxManager.GC_ZLAQ_JC);
			FileUploadService.updateVOByYwid(conn, vo, ywid,user);
			BaseDAO.update(conn, xmvo);
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "操作结果描述请写在这里", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "操作结果描述请写在这里", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zlaq_zgb_id()+"\",\"fieldname\":\"Gc_zlaq_zgb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_zg(jsona,user,"1");
		return resultXinXi;
	}
	
	
	/* 
	 * 根据输入值从储备库数据表自动模糊匹配项目名称
	 * @see com.ccthanking.business.tcjh.jhgl.service.TcjhService#xmmcAutoComplete(com.ccthanking.framework.model.autocomplete, com.ccthanking.framework.common.User)
	 */
	@Override
	public List<autocomplete> xmmcAutoComplete(autocomplete json,User user) throws Exception {
		List<autocomplete> autoResult = new ArrayList<autocomplete>(); 
		autocomplete ac = new autocomplete();
		String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
		condition += BusinessUtil.getSJYXCondition("zgb") + BusinessUtil.getCommonCondition(user,null);
		String [][] result = DBUtil.query("select distinct xmmc from GC_ZLAQ_ZGB zgb,GC_ZLAQ_JCB jcb  " + json.getTablePrefix() + " where GC_ZLAQ_JCB_ID=JCBID  and " + condition);
        if(null != result&&result.length>0){
        	for(int i =0;i<result.length;i++){
        	  ac = new autocomplete();
              ac.setRegionName(result[i][0]);
              autoResult.add(ac);
        	}
        }
		return autoResult;
	}	
}
