package com.ccthanking.business.sjgl.sjbggl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccthanking.business.sjgl.xgzllq.GcSjXgzlLqVO;
import com.ccthanking.business.sjgl.zlsfgl.GcSjZlsfJsVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.business.xdxmk.vo.XmxdkVO;
import com.ccthanking.business.zjb.jsgl.GcZjbJsbVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.common.vo.XmxxVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.log.log;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.UserService;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class SheJiBianGengServiceImpl implements SheJiBianGengService {


	private String ywlx=YwlxManager.GC_SJ_SJBG_JS;
	@Override
	public String queryConditionSheJiBianGeng(String json,String  sjwybh,String nd,User user) throws SQLException {
		
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				if(!Pub.empty(condition)){
					condition +=" and a.sjwybh=b.sjwybh  and b.bdid=c.gc_xmbd_id(+) and d.gc_tcjh_xmxdk_id(+)=b.xmid  and  b.sjwybh='"+sjwybh+"' and  b.nd='"+nd+"'";
					
				}else{
					condition +="  a.sjwybh=b.sjwybh  and b.bdid=c.gc_xmbd_id(+) and d.gc_tcjh_xmxdk_id(+)=b.xmid and  b.sjwybh='"+sjwybh+"' and  b.nd='"+nd+"'";
				}
				    condition += BusinessUtil.getSJYXCondition("B") + BusinessUtil.getCommonCondition(user,null);
				    condition += BusinessUtil.getSJYXCondition("A") + BusinessUtil.getCommonCondition(user,null);
				    condition += orderFilter;
				    page.setFilter(condition);
					conn.setAutoCommit(false);
					String sql = "select a.gc_sj_sjbg_js2_id,b.sjwybh, b.jhid, b.nd,b.xmbh, b.xmmc, b.bdbh, b.gc_jh_sj_id jhsjid, a.bgbh, a.bglb, " +
							"a.bglldjsrq, a.sjy, a.fs, a.jbgrq, a.jsrq, a.jsr, a.bgnr, a.ywlx, a.sjbh, a.bz, a.lrr, a.lrsj," +
							" a.lrbm, a.lrbmmc, a.gxr, a.gxsj, a.gxbm, a.gxbmmc, a.sjmj, a.sfyx,a.bgfy , a.lldffrq, a.bgzt ," +
							" c.bdmc, decode(b.bdid,'',d.sgdw,c.sgdw) sgdw , decode(b.bdid,'',d.jldw,c.jldw) jldw," +
							" decode(b.bdid,'',d.sjdw,c.sjdw) sjdw,decode(b.bdid, '', d.xmdz, c.BDDD) xmdz  from gc_sj_sjbg_js2 a,gc_jh_sj b,gc_xmbd c,gc_tcjh_xmxdk d ";
						
					BaseResultSet bs = DBUtil.query(conn, sql, page);
					bs.setFieldDic("BGLB", "BGLB2");
					bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("SJY", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("JSR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
					domresult = bs.getJson();
					LogManager.writeUserLog(null, ywlx,
							Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
							user.getOrgDept().getDeptName() + " " + user.getName()
									+ "设计变更查询成功", user,"","");
				} catch (Exception e) {
					e.printStackTrace(System.out);
				
				} finally {
					DBUtil.closeConnetion(conn);
				}
				return domresult;
	}

	@Override
	public String insert(HttpServletRequest request,String json, User user,String ywid) throws SQLException, Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		String xiaoxi;
		GcSjSjbgJs2VO xmvo = new GcSjSjbgJs2VO();
		TcjhVO jh= new TcjhVO();
		try {
				conn.setAutoCommit(false);
				JSONArray list = xmvo.doInitJson(json);
				xmvo.setValueFromJson((JSONObject)list.get(0));
				jh.setValueFromJson((JSONObject)list.get(0));
				xmvo.setYwlx(ywlx);//业务类型
				EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
				xmvo.setSjbh(eventVO.getSjbh());
				//设置主键
				if(!Pub.empty(ywid)){
					xmvo.setGc_sj_sjbg_js2_id(ywid);
				   // FileUploadService.updateFjztByYwid(conn, ywid);
			        FileUploadVO fvo = new FileUploadVO();
			        fvo.setFjzt("1");//更新附件状态
			        fvo.setGlid2(xmvo.getJhsjid());//存入计划数据ID
			        fvo.setGlid3(xmvo.getXmid()); //存入项目ID
			        fvo.setGlid4(xmvo.getBdid()); //存入标段ID
			        fvo.setYwlx(xmvo.getYwlx()); 
			        fvo.setSjbh(xmvo.getSjbh()); 
			        FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_sj_sjbg_js2_id(),user);
				}else{
					xmvo.setGc_sj_sjbg_js2_id(new RandomGUID().toString()); // 主键
				}
				//推送消息判断
				if(Pub.empty(jh.getBdid()))
	        	{
	    		    xiaoxi="项目："+jh.getXmmc()+"";
	        	}
	        	else
	        	{
	        		xiaoxi="项目："+jh.getXmmc()+"  标段："+jh.getBdmc()+"";
	        	}
				BusinessUtil.setInsertCommonFields(xmvo, user);
				//插入
				BaseDAO.insert(conn, xmvo);
				 //信息推送
				 PushMessage.push(conn, request, PushMessage.SJBG,""+xiaoxi+"  设计发生了变更。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_sjbg_js2_id(),"消息提示");
				
				resultVO = xmvo.getRowJson();
				conn.commit();
				LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "设计变更添加成功", user,"","");
		
				} catch (Exception e) {
					DBUtil.rollbackConnetion(conn);
					e.printStackTrace(System.out);
				} finally {
					DBUtil.closeConnetion(conn);
				}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_sj_sjbg_js2_id()+"\",\"fieldname\":\"gc_sj_sjbg_js2_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryConditionSheJiBianGeng(jsona,xmvo.getSjwybh(),xmvo.getNd(),user);
		return resultXinXi;
	}
	@Override
	public String updatedemo(String json, User user) throws SQLException,Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjSjbgJs2VO xmvo = new GcSjSjbgJs2VO();

		try {
				conn.setAutoCommit(false);
				JSONArray list = xmvo.doInitJson(json);
				xmvo.setValueFromJson((JSONObject)list.get(0));
				BusinessUtil.setUpdateCommonFields(xmvo, user);//公共字段更新
				
				GcSjSjbgJs2VO xmvo1=(GcSjSjbgJs2VO) BaseDAO.getVOByPrimaryKey(conn,xmvo);
				
				FileUploadVO fvo = new FileUploadVO();
				fvo.setFjzt("1");
				fvo.setGlid2(xmvo.getJhsjid());//存入计划数据ID
				fvo.setGlid3(xmvo.getXmid()); //存入项目ID
				fvo.setGlid4(xmvo.getBdid()); //存入标段ID
				fvo.setYwlx(xmvo1.getYwlx()); 
		        fvo.setSjbh(xmvo1.getSjbh()); 
				FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_sj_sjbg_js2_id(),user);
				//插入
				BaseDAO.update(conn, xmvo);
				resultVO = xmvo.getRowJson();
				conn.commit();
				LogManager.writeUserLog(xmvo1.getSjbh(), xmvo1.getYwlx(),
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "设计变更修改成功", user,"","");
		
				} catch (Exception e) {
					DBUtil.rollbackConnetion(conn);
					e.printStackTrace(System.out);
				
				} finally {
					DBUtil.closeConnetion(conn);
				}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_sj_sjbg_js2_id()+"\",\"fieldname\":\"gc_sj_sjbg_js2_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryConditionSheJiBianGeng(jsona,xmvo.getSjwybh(),xmvo.getNd(),user);
		return resultXinXi;
	}

	@Override
	public String insertLingQu(String json, User user) throws SQLException,Exception 
	{
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjXgzlLqVO xmvo = new GcSjXgzlLqVO();

		try {
				conn.setAutoCommit(false);
				JSONArray list = xmvo.doInitJson(json);
				xmvo.setValueFromJson((JSONObject)list.get(0));
				xmvo.setGc_sj_xgzl_lq_id(new RandomGUID().toString()); // 主键
				BusinessUtil.setInsertCommonFields(xmvo, user);//公共字段插入
				xmvo.setYwlx(ywlx);//业务类型
				xmvo.setZllb("02");
				EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
				xmvo.setSjbh(eventVO.getSjbh());
		        
				//插入
				BaseDAO.insert(conn, xmvo);
				resultVO = xmvo.getRowJson();
				conn.commit();
				GcSjXgzlLqVO xmvo1=(GcSjXgzlLqVO) BaseDAO.getVOByPrimaryKey(conn,xmvo);
				resultVO=xmvo1.getRowJson();
				LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "设计变更新增成功", user,"","");
		
				} catch (Exception e) {
					DBUtil.rollbackConnetion(conn);
					e.printStackTrace(System.out);
				} finally {
					DBUtil.closeConnetion(conn);
				}
				return resultVO;
	}

	@Override
	public String updateLingQu(String json, User user) throws SQLException,Exception 
	{
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjXgzlLqVO xmvo = new GcSjXgzlLqVO();

		try {
				conn.setAutoCommit(false);
				JSONArray list = xmvo.doInitJson(json);
				xmvo.setValueFromJson((JSONObject)list.get(0));
				BusinessUtil.setUpdateCommonFields(xmvo, user);//公共字段更新
				xmvo.setYwlx(ywlx);//业务类型
				//EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
				//xmvo.setSjbh(eventVO.getSjbh());
				//插入
				BaseDAO.update(conn, xmvo);
				resultVO = xmvo.getRowJson();
				conn.commit();
				
				GcSjXgzlLqVO xmvo1=(GcSjXgzlLqVO) BaseDAO.getVOByPrimaryKey(conn,xmvo);
				resultVO=xmvo1.getRowJson();
				
				LogManager.writeUserLog(xmvo1.getSjbh(), xmvo1.getYwlx(),
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "设计变更领取成功", user,"","");
		
				} catch (Exception e) {
					DBUtil.rollbackConnetion(conn);
					e.printStackTrace(System.out);
					
				} finally {
					DBUtil.closeConnetion(conn);
				}
				return resultVO;
	}

	@Override
	public String queryConditionShouQu(String json, String SJBGID,User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				if(!Pub.empty(condition)){
					condition +=" and ZLLB='02' and ZLLJSD='"+SJBGID+"'";
					
				}else{
					condition=" ZLLB='02' and ZLLJSD='"+SJBGID+"'";
				}
				condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql = "select gc_sj_xgzl_lq_id, zlljsd, zllb,  fs, lqbm, lqrq, lqr, blr, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx from GC_SJ_XGZL_LQ ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				 bs.setFieldTranslater("LQR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				 bs.setFieldTranslater("LQBM", "FS_ORG_DEPT", "ROW_ID", "DEPT_NAME");
			     bs.setFieldTranslater("BLR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				//bs.setFieldDic("TZLB", "TZLB");
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "设计变更领取查询成功", user,"","");
			} catch (Exception e) {
				e.printStackTrace(System.out);
			
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}

	@Override
	public String deleteJS(HttpServletRequest request, String json) {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjSjbgJs2VO vo = new GcSjSjbgJs2VO();

		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			//置成失效
	        vo.setSfyx("0");
			//插入
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();
			
			//删除附件
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getGc_sj_sjbg_js2_id());
			FileUploadService.deleteByConditionVO(conn, fvo);
			
			// 删除领取信息
			GcSjXgzlLqVO volq = new GcSjXgzlLqVO();
			volq.setZlljsd(vo.getGc_sj_sjbg_js2_id());
			BaseVO[] lqs=(BaseVO[]) BaseDAO.getVOByCondition(conn, volq);
			if(null!=lqs)
			{
				for(int i=0;i<lqs.length;i++)
				{
					GcSjXgzlLqVO lqvo=(GcSjXgzlLqVO) lqs[i];
					lqvo.setSfyx("0");
					BaseDAO.update(conn, lqvo);
				}
			}
			
			
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除变更管理（接收）信息成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		return resultVO;
	}

	@Override
	public String deleteLQ(HttpServletRequest request, String json) {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjXgzlLqVO vo = new GcSjXgzlLqVO();

		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			//置成失效
	        vo.setSfyx("0");
			//插入
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除变更管理（领取）信息成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		return resultVO;
	}
	}


