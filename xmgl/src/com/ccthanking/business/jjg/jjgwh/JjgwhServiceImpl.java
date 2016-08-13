package com.ccthanking.business.jjg.jjgwh;

import java.sql.Connection;
import java.sql.SQLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.bdhf.bdwh.BdwhService;
import com.ccthanking.business.bdhf.vo.BdwhVO;
import com.ccthanking.business.clzx.service.ClzxManagerService;
import com.ccthanking.business.clzx.service.impl.ClzxManagerServiceImpl;
import com.ccthanking.business.jjg.vo.JjgwhVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.business.xdxmk.vo.XmxdkVO;
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
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class JjgwhServiceImpl   implements JjgwhService{
	
	private String ywlx=YwlxManager.GC_SJGL_JJG;
	/*	
	 * 普通查询
	 * 
	 */
	@Override
	public String query_jjg(String json,User user,String sfwg,String tbjjg) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				QueryConditionList list = RequestUtil.getConditionList(json);
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				String sqltbjjg="";
				if(!Pub.empty(sfwg)){
					if(("0").equals(sfwg))
					{
						condition+=" and sj.wgsj_sj is not null ";
					}
					if(("1").equals(sfwg))
					{
						condition+=" and sj.wgsj_sj is  null ";
					}
				}
				if(!Pub.empty(tbjjg)){
					if(("1").equals(tbjjg))
					{
						condition+=" and tjgl.tbjjg=1  ";
					}
				}
				//condition +=BusinessUtil.getSJYXCondition("")+BusinessUtil.getCommonCondition(user,null);
				condition+=" and sj.xmid = xdk.gc_tcjh_xmxdk_id and jjg.jhsjid(+)=sj.gc_jh_sj_id  and sj.bdid =bd.gc_xmbd_id(+)  and sj.gc_jh_sj_id = tjgl.jhsjid(+)  ";
			
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql=
						"select tbjjg,JU_KSRQ,JU_SFBZJZ,JU_JBYSTJ,JU_SFZZYS,JU_YSQK,JU_SFZLCD,JU_SFZJZBA,JA_KSRQ, jjg.gc_sjgl_jjg_id jiaogongfj,jjg.gc_sjgl_jjg_id jungongfj,jjg.jiaogjsr,jjg.jungjsdw,jjg.JUNGJSR,jjg.gc_sjgl_jjg_id,sj.bdbh as bdbh,jjg.wjgys,jjg.wjgysy,jjg.jiaogjsdw,jjg.jgysrq," +
						" jjg.jgyssj,sj.xmid,jjg.ywlx,sj.bdid,sj.xmbs, sj.bdmc,jjg.lrsj,xdk.gc_tcjh_xmxdk_id,sj.wgsj_sj wgrq,xdk.xmmc,  decode(sj.bdid, '', xdk.xmdz, bd.bddd) xmdz, " +
						" decode(sj.bdid,'',decode(xdk.jsnr_sj, '', xdk.jsnr, xdk.jsnr_sj),decode(bd.JSGM_sj, '', bd.JSGM,bd.JSGM_sj))  jsnr,xdk.sjdw,xdk.isnatc,xdk.fzr_sjdw,sj.xmglgs,xdk.yzdb,xdk.nd,xdk.xmbh as xmbh,xdk.xmlx,xdk.sfyx as xsfyx ,sj.gc_jh_sj_id  JHSJID,  "
								+ "(case xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = sj.xmid) when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = sj.bdid) end) sgdw, "
								+ "(case xmbs when '0' then (select fzr_sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = sj.xmid) when '1' then (select sgdwfzr from GC_XMBD where GC_XMBD_ID = sj.bdid) end) FZR_SGDW, "
								+ "(case xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = sj.xmid) when '1' then (select jldw from GC_XMBD where GC_XMBD_ID = sj.bdid) end) JLDW, "
								+ "(case xmbs when '0' then (select fzr_jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = sj.xmid) when '1' then (select jldwfzr from GC_XMBD where GC_XMBD_ID = sj.bdid) end) FZR_JLDW, "
								+ "(case xmbs when '0' then (select lxfs_sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = sj.xmid) when '1' then (select sgdwfzrlxfs from GC_XMBD where GC_XMBD_ID = sj.bdid) end) LXFS_SGDW, "
								+ "(case xmbs when '0' then (select LXFS_JLDW from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = sj.xmid) when '1' then (select jldwfzrlxfs from GC_XMBD where GC_XMBD_ID = sj.bdid) end) LXFS_JLDW "
				+ "from GC_SJGL_JJG jjg,GC_TCJH_XMXDK xdk ,gc_jh_sj sj, gc_xmbd bd ,GC_JH_TJGL tjgl ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldFileUpload("JIAOGONGFJ", "0028");
				bs.setFieldFileUpload("JUNGONGFJ", "0029");
				bs.setFieldDic("XMLX", "XMLX");
				bs.setFieldDic("ND","XMNF");
				bs.setFieldDic("XMSX", "XMSX");
				bs.setFieldDic("ISXD","XMZT");
				bs.setFieldDic("ISBT", "SF");
				bs.setFieldDic("SFYX", "SF");
				bs.setFieldDic("ND","XMNF");
				bs.setFieldDic("JU_YSQK","YSQK");
				bs.setFieldDic("TBJJG","SF");
				bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");
				bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询交竣工项目标段", user,"","");

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
	 * 项目信息查询
	 * 
	 */
	@Override
	public String query_xmxx(String json,User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		QueryConditionList list = RequestUtil.getConditionList(json);
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition+=" and bd.xmbh=xdk.xmbh and bd.sfyx='1'  and xdk.isnatc='1' ";
		condition +=BusinessUtil.getSJYXCondition("xdk")+ BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql=" select  gc_tcjh_xmxdk_id, xdk.xmbh, xdk.xmmc, qy, nd, pch, xmlx, xmdz, jsnr, jsyy, jsrw, jsbyx, jhztze, gc, zc, qt, xmsx, isbt, isnatc, jszt, xmfr, xmglgs, fzr_glgs, lxfs_glgs, xdk.sgdw, fzr_sgdw, lxfs_sgdw, xdk.jldw, fzr_jldw, lxfs_jldw, yzdb, sjdw, fzr_sjdw, lxfs_sjdw, lxfs_yzdb, xdk.wgrq, xdk.qgrq, xdk.jgrq,xdk.sfyx,bd.xmbh as bxmbh,bd.bdmc,bd.bdbh,bd.lrsj,bd.gc_xmbd_id as bdid,bd.sfyx as bsfyx from GC_XMBD bd,GC_TCJH_XMXDK xdk ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("XMGLGS", "XMGLGS");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询可进行交竣工的项目标段", user,"","");

			
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
	*
	*	
	* 交竣工插入
	* 
	*/ 
	@Override
	public String insert_jjg(String json,User user,String ywid) throws Exception {
		Connection conn = DBUtil.getConnection();
		PageManager page = RequestUtil.getPageManager(json);
		String resultVO = null;
		JjgwhVO xmvo = new JjgwhVO();
		XmxdkVO  xdk1=new XmxdkVO();
		try {
				conn.setAutoCommit(false);
				JSONArray list = xmvo.doInitJson(json);
				xmvo.setValueFromJson((JSONObject)list.get(0));
				
				xdk1.setValueFromJson((JSONObject)list.get(0));
				
				xmvo.setYwlx(ywlx);
				EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
				xmvo.setSjbh(eventVO.getSjbh());
				if(!Pub.empty(ywid)){
					xmvo.setGc_sjgl_jjg_id(ywid);
					FileUploadVO fvo = new FileUploadVO();
			        fvo.setFjzt("1");//更新附件状态
			        fvo.setGlid2(xmvo.getJhsjid());//存入计划数据ID
			        fvo.setGlid3(xmvo.getXmid()); //存入项目ID
			        fvo.setGlid4(xmvo.getBdid()); //存入标段ID
			        fvo.setYwlx(xmvo.getYwlx()); 
			        fvo.setSjbh(xmvo.getSjbh()); 
			        FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_sjgl_jjg_id(),user);
				}else{
					xmvo.setGc_sjgl_jjg_id(new RandomGUID().toString()); // 主键
				}
				BusinessUtil.setInsertCommonFields(xmvo, user);//公共字段插入
				//更新下达库项目规模
				if(Pub.empty(xmvo.getBdid()))
				{
					XmxdkVO  xdk=new XmxdkVO();
					xdk.setGc_tcjh_xmxdk_id(xmvo.getXmid());
					xdk.setJsnr_sj(xdk1.getJsnr());
					BusinessUtil.setUpdateCommonFields(xdk, user);//公共字段更新
					BaseDAO.update(conn, xdk);
				}
				//更新标段项目规模
				else{
					BdwhVO bdvo=new BdwhVO();
					bdvo.setGc_xmbd_id(xmvo.getBdid());
					bdvo.setJsgm_sj(xdk1.getJsnr());
					BusinessUtil.setUpdateCommonFields(bdvo, user);//公共字段更新
					BaseDAO.update(conn, bdvo);
				}

				//-------------加入处理中心任务生成代码-------------BEGIN---------
				ClzxManagerService cms = new ClzxManagerServiceImpl();
				if(xmvo.getJgysrq()!=null){
					//交工
					cms.createTask("1007009", xmvo.getJhsjid(), user,conn);
				}else if(xmvo.getJgyssj()!=null){
					//竣工
					cms.createTask("1007011", xmvo.getJhsjid(), user,conn);
				}
				//-------------加入处理中心任务生成代码-------------END---------
				BaseDAO.insert(conn, xmvo);//插入
				
				/*TcjhVO vo = new TcjhVO();
				vo.setGc_jh_sj_id(xmvo.getJhsjid());
				vo.setWgsj_sj(xmvo.getJgyssj());//竣工验收时间
				vo.setJg_sj(xmvo.getJgysrq());//交工验收时间
				BaseDAO.update(conn, vo);*/
				conn.commit();
				LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "添加交竣工成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "添加交竣工失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_sjgl_jjg_id()+"\",\"fieldname\":\"Gc_sjgl_jjg_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_jjg(jsona,user,"3","0");
		return resultXinXi;
		//return resultVO;
	}
	
	
	//交竣工修改
	@Override
	public String update_jjg(String json,User user) throws Exception {
 		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		JjgwhVO xmvo = new JjgwhVO();
		XmxdkVO  xdk1=new XmxdkVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list.get(0));
			xdk1.setValueFromJson((JSONObject)list.get(0));
			
			BusinessUtil.setUpdateCommonFields(xmvo, user);//公共字段更新
			BaseDAO.update(conn, xmvo);
			JjgwhVO xmvo1=(JjgwhVO)BaseDAO.getVOByPrimaryKey(conn, xmvo);
			
	        FileUploadVO fvo = new FileUploadVO();
	        fvo.setFjzt("1");//更新附件状态
	        fvo.setGlid2(xmvo.getJhsjid());//存入计划数据ID
	        fvo.setGlid3(xmvo.getXmid()); //存入项目ID
	        fvo.setGlid4(xmvo.getBdid()); //存入标段ID
	        fvo.setYwlx(xmvo1.getYwlx()); 
	        fvo.setSjbh(xmvo1.getSjbh()); 
	        FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_sjgl_jjg_id(),user);
	        
			/*TcjhVO vo = new TcjhVO();
			vo.setGc_jh_sj_id(xmvo.getJhsjid());
			vo.setWgsj_sj(xmvo.getJgyssj());
			vo.setJg_sj(xmvo.getJgysrq());
			BaseDAO.update(conn, vo);
			resultVO = xmvo.getRowJson();*/
			/*//更新下达库项目规模
			XmxdkVO  xdk=new XmxdkVO();
			xdk.setGc_tcjh_xmxdk_id(xmvo.getXmid());
			xdk.setJsnr_sj(xdk1.getJsnr());
			BusinessUtil.setUpdateCommonFields(xdk, user);//公共字段更新
			BaseDAO.update(conn, xdk);*/
			
			//更新下达库项目规模
			if(Pub.empty(xmvo.getBdid()))
			{
				XmxdkVO  xdk=new XmxdkVO();
				xdk.setGc_tcjh_xmxdk_id(xmvo.getXmid());
				xdk.setJsnr_sj(xdk1.getJsnr());
				BusinessUtil.setUpdateCommonFields(xdk, user);//公共字段更新
				BaseDAO.update(conn, xdk);
			}
			//更新标段项目规模
			else{
				BdwhVO bdvo=new BdwhVO();
				bdvo.setGc_xmbd_id(xmvo.getBdid());
				bdvo.setJsgm_sj(xdk1.getJsnr());
				BusinessUtil.setUpdateCommonFields(bdvo, user);//公共字段更新
				BaseDAO.update(conn, bdvo);
			}
			//-------------加入处理中心任务生成代码-------------BEGIN---------
			ClzxManagerService cms = new ClzxManagerServiceImpl();
			String sql = "select to_char(JG_SJ,'yyyy-MM-dd'),to_char(JS_SJ,'yyyy-MM-dd') from GC_JH_SJ where GC_JH_SJ_ID='"+xmvo.getJhsjid()+"'";
			String arr[][] = DBUtil.query(conn, sql);
			if(xmvo.getJgysrq()!=null){
				if(arr!=null && arr.length!=0 && xmvo.getJgysrq()!=null){
					//交工
					String jsrq = DateTimeUtil.getDateTime(xmvo.getJgysrq(),"yyyy-MM-dd");
					if(arr[0][0]!=null && !jsrq.equals(arr[0][0])){
		        		cms.createTask("1007009", xmvo.getJhsjid(), user,conn);
					}
				}
			}else if(xmvo.getJgyssj()!=null){
				//竣工
				if(arr!=null && arr.length!=0 && xmvo.getJgyssj()!=null){
					String jsrq = DateTimeUtil.getDateTime(xmvo.getJgyssj(),"yyyy-MM-dd");
					if(arr[0][0]!=null && !jsrq.equals(arr[0][1])){
		        		cms.createTask("1007011", xmvo.getJhsjid(), user,conn);
					}
				}
			}
			//-------------加入处理中心任务生成代码-------------END---------
			
			conn.commit();
			LogManager.writeUserLog(xmvo1.getSjbh(), xmvo1.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改交竣工成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改交竣工失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_sjgl_jjg_id()+"\",\"fieldname\":\"Gc_sjgl_jjg_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_jjg(jsona,user,"3","0");
		return resultXinXi;
	}
	
	/*	
	 * 统筹计划jhid
	 * 
	 */
	public String query_jhid(String nd,String xmbh) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				conn.setAutoCommit(false);
				String sql ="select jhid  from GC_JH_SJ where   xmbh= '"+xmbh+"' and nd='"+nd+"'";
				String result[][] = DBUtil.query(conn, sql);
				if(null!=result&&result.length>0)
				{
					domresult = result[0][0];
				}
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

}
