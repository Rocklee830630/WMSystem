package com.ccthanking.business.zjb.lbj;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.business.clzx.service.ClzxManagerService;
import com.ccthanking.business.clzx.service.impl.ClzxManagerServiceImpl;
import com.ccthanking.business.tcjh.jhgl.vo.FkqkVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.business.zjb.lbj.LbjService;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.business.zjb.lbj.GcZjbLbjbVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;


@Service
public  class LbjServiceImpl implements LbjService {
	private String ywlx=YwlxManager.GC_ZJB_LBJ;
	// 提报价反馈
	private String ywlxtbfk=YwlxManager.GC_ZJB_TBJFK ;
    // 财审反馈
	private String ywlxcsfk=YwlxManager.GC_ZJB_CSFK ;
	@Override
	public String queryConditionLbj(String json,User user,String IsNull) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		if("1".equals(IsNull))
		{
			condition += "and lbj.SJWYBH=jhsj.SJWYBH and jhsj.SJWYBH = cbsjpf.SJWYBH(+)  and c.gc_ztb_xq_id=e.ztbxqid(+) and e.ztbsjid=f.gc_ztb_sj_id(+) and jhsj.SJWYBH=c.SJWYBH(+) ";
		}	
		else{
		condition += "and lbj.SJWYBH(+)=jhsj.SJWYBH  and jhsj.SJWYBH = cbsjpf.SJWYBH(+) and c.gc_ztb_xq_id=e.ztbxqid(+) and e.ztbsjid=f.gc_ztb_sj_id(+) and jhsj.SJWYBH=c.SJWYBH(+) ";
		}
		condition += BusinessUtil.getSJYXCondition("jhsj") + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select gc_zjb_lbjb_id, jhsj.GC_JH_SJ_ID as jhsjid,jhsj.sjwybh, jhsj.xmbh, jhsj.jhid, jhsj.nd, jhsj.xmsx, jhsj.pxh, jhsj.xmmc, jhsj.bdmc, jhsj.xmid, jhsj.bdbh,jhsj.bdid," +
						 " lbj.ISQTBMFZ,lbj.ISXYSCS,lbj.ZJBZZT,lbj.csbgbh, lbj.txqsj, lbj.zbsj, lbj.tzjjsj, lbj.zxgsj, lbj.sgfajs, lbj.zbwjjs, lbj.zxgsrq, lbj.jgbcsrq, lbj.sbcsz, lbj.czswrq, lbj.cssdz, lbj.sjz, lbj.sjbfb, lbj.zdj, lbj.bz,lbj.SBCSZRQ,lbj.CSSDZRQ,lbj.ISZH,lbj.CSSX, jhsj.iscs, jhsj.istbj," +
						 "jhsj.cs_sj, jhsj.tbj_sj, nvl(cbsjpf.gs, cbsjpf.sse) as gys,  " +
						 "(select max(dyqk.twrq) from GC_ZJB_DYQK dyqk where dyqk.SJWYBH(+)=jhsj.SJWYBH and dyqk.sfyx='1') as YWRQ ," +
						 "(select max(dyqk.dyrq) from GC_ZJB_DYQK dyqk where dyqk.SJWYBH(+)=jhsj.SJWYBH and dyqk.sfyx='1') as HFRQ, decode(lbj.ZXGS,'', f.dsfjgid,lbj.ZXGS)  ZXGS,f.kbrq, c.lrsj,  " +
						 " (case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ " +
						 " from GC_ZJB_LBJB lbj,GC_JH_SJ jhsj,(select a.gc_ztb_xq_id,b.SJWYBH,a.lrsj" +
						 " from gc_ztb_xq a, gc_ztb_jhsj b where b.xqid = a.GC_ZTB_XQ_ID  and a.ZBLX = '14' and a.sfyx='1' and b.sfyx='1') c" +
						 ",(select * from  gc_ztb_xqsj_ys where sfyx='1') e,(select * from  gc_ztb_sj where sfyx='1') f," +
						 "(select * from Gc_Sj_Cbsjpf where sfyx = '1') cbsjpf  ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("SBCSZ"); 
			bs.setFieldThousand("CSSDZ"); 
			bs.setFieldThousand("SJZ"); 
			bs.setFieldThousand("ZDJ");
			bs.setFieldThousand("GYS");
			bs.setFieldDic("ZJBZZT", "ZJTJ");
			bs.setFieldDic("CSSX", "CSSX");
			 bs.setFieldTranslater("ZXGS", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询拦标价信息成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertLbj(HttpServletRequest request,String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjbLbjbVO xmvo = new GcZjbLbjbVO();
		TcjhVO jh= new TcjhVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list.get(0));
			jh.setValueFromJson((JSONObject)list.get(0));
			//设置主键
			xmvo.setGc_zjb_lbjb_id(new RandomGUID().toString()); // 主键
			BusinessUtil.setInsertCommonFields(xmvo, user);
			xmvo.setYwlx(ywlx);
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO.getSjbh());
	      /*  //判断是否是暂缓属性，如果是同步zjbzt为暂缓状态
	        if("1".equals(xmvo.getIszh())){
	        	xmvo.setZjbzzt("1");
	        }else{
	        	if("1".equals(xmvo.getIsxyscs())){
	        		xmvo.setZjbzzt("2");
	        	}else{
	        		xmvo.setZjbzzt("3");
	        	}
	        }*/
			//插入
			BaseDAO.insert(conn, xmvo);
			//-------------加入处理中心任务生成代码-------------BEGIN---------
			if(xmvo.getJgbcsrq()!=null){
				//如果建管报财审日期存在，那么生成上报值处理任务
				ClzxManagerService cms = new ClzxManagerServiceImpl();
				cms.createTask("2001001", xmvo.getJhsjid(), user,conn);
			}

			// start 拦标价已完成,可以进行招标，发送相关配置人员信息提醒。    add by xiahongbo by 2014-10-14
			// 正常編制造价
			if("2".equals(xmvo.getZjbzzt())) {
				// 需要送财审
				if("1".equals(xmvo.getIsxyscs())) {
					if(xmvo.getCssdz()!=null) {
						if(Pub.empty(jh.getBdmc())) {
							//信息推送
			    			PushMessage.push(conn, request, PushMessage.ZJB_LBJ,"项目："+jh.getXmmc()+" 。拦标价已完成,可以进行招标。【此项目正常編制造价、需要送财审】",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_zjb_lbjb_id(),"消息提示");
			        	} else{
			        		//信息推送
			        		PushMessage.push(conn, request, PushMessage.ZJB_LBJ,"项目："+jh.getXmmc()+"  标段："+jh.getBdmc()+"。拦标价已完成,可以进行招标。【此项目正常編制造价、需要送财审】",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_zjb_lbjb_id(),"消息提示");
			        	}
						
						//如果财政审完日期存在，那么生成审定值处理任务
						ClzxManagerService cms = new ClzxManagerServiceImpl();
						cms.createTask("2001101", xmvo.getJhsjid(), user,conn);
					}
				// 不需要送财审
				} else if("0".equals(xmvo.getIsxyscs())) {
					if(xmvo.getZxgsrq()!=null) {
						if(Pub.empty(jh.getBdmc())) {
							//信息推送
			    			PushMessage.push(conn, request, PushMessage.ZJB_LBJ,"项目："+jh.getXmmc()+" 。拦标价已完成,可以进行招标。【此项目正常編制造价、不需要送财审】",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_zjb_lbjb_id(),"消息提示");
			        	} else{
			        		//信息推送
			        		PushMessage.push(conn, request, PushMessage.ZJB_LBJ,"项目："+jh.getXmmc()+"  标段："+jh.getBdmc()+"。拦标价已完成,可以进行招标。【此项目正常編制造价、不需要送财审】",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_zjb_lbjb_id(),"消息提示");
			        	}
					}
					
					//如果财政审完日期存在，那么生成审定值处理任务
					ClzxManagerService cms = new ClzxManagerServiceImpl();
					cms.createTask("2001101", xmvo.getJhsjid(), user,conn);
				}
			// 不需要編制造价
			} else if("3".equals(xmvo.getZjbzzt())) {
				if(Pub.empty(jh.getBdmc())) {
					//信息推送
	    			PushMessage.push(conn, request, PushMessage.ZJB_LBJ,"项目："+jh.getXmmc()+" 。拦标价已完成,可以进行招标。【此项目不需要編制造价】",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_zjb_lbjb_id(),"消息提示");
	        	} else{
	        		//信息推送
	        		PushMessage.push(conn, request, PushMessage.ZJB_LBJ,"项目："+jh.getXmmc()+"  标段："+jh.getBdmc()+"。拦标价已完成,可以进行招标。【此项目不需要編制造价】",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_zjb_lbjb_id(),"消息提示");
	        	}
				
				//如果财政审完日期存在，那么生成审定值处理任务
				ClzxManagerService cms = new ClzxManagerServiceImpl();
				cms.createTask("2001101", xmvo.getJhsjid(), user,conn);
			}
			// end
			//-------------加入处理中心任务生成代码-------------END---------
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "插入拦标价信息成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zjb_lbjb_id()+"\",\"fieldname\":\"gc_zjb_lbjb_id\",\"operation\":\"=\",\"logic\":\"and\"} ,{\"value\":\""+xmvo.getNd()+"\",\"fieldname\":\"jhsj.nd\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String result=this.queryConditionLbj(jsona,user,"0");
		return result;
	}

	@Override
	public String updateLbj(HttpServletRequest request,String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjbLbjbVO vo = new GcZjbLbjbVO();
		TcjhVO jh= new TcjhVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			jh.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			vo.setYwlx(ywlx);

	      //根据ID查YWID和SJBH
			GcZjbLbjbVO vo1=(GcZjbLbjbVO) BaseDAO.getVOByPrimaryKey(conn, vo);
		/*	  //判断是否是暂缓属性，如果是同步zjbzt为暂缓状态
	        if("1".equals(vo.getIszh())){
	        	vo.setZjbzzt("1");
	        }else{
	        	if("1".equals(vo.getIsxyscs())){
	        		vo.setZjbzzt("2");
	        	}else{
	        		vo.setZjbzzt("3");
	        	}
	        }*/
	      //插入
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();

			// start 拦标价已完成,可以进行招标，发送相关配置人员信息提醒。    add by xiahongbo by 2014-10-14
			// 正常編制造价
			if("2".equals(vo1.getZjbzzt())) {
				// 需要送财审
				if("1".equals(vo1.getIsxyscs())) {
					if(vo1.getCssdz()!=null) {
						if(Pub.empty(jh.getBdmc())) {
							//信息推送
			    			PushMessage.push(conn, request, PushMessage.ZJB_LBJ,"项目："+jh.getXmmc()+" 。拦标价已完成,可以进行招标。【此项目正常編制造价、需要送财审】",null,vo1.getSjbh(),vo1.getYwlx(),vo1.getGc_zjb_lbjb_id(),"消息提示");
			        	} else{
			        		//信息推送
			        		PushMessage.push(conn, request, PushMessage.ZJB_LBJ,"项目："+jh.getXmmc()+"  标段："+jh.getBdmc()+"。拦标价已完成,可以进行招标。【此项目正常編制造价、需要送财审】",null,vo1.getSjbh(),vo1.getYwlx(),vo1.getGc_zjb_lbjb_id(),"消息提示");
			        	}
					}
				// 不需要送财审
				} else if("0".equals(vo1.getIsxyscs())) {
					if(vo1.getZxgsrq()!=null) {
						if(Pub.empty(jh.getBdmc())) {
							//信息推送
			    			PushMessage.push(conn, request, PushMessage.ZJB_LBJ,"项目："+jh.getXmmc()+" 。拦标价已完成,可以进行招标。【此项目正常編制造价、不需要送财审】",null,vo1.getSjbh(),vo1.getYwlx(),vo1.getGc_zjb_lbjb_id(),"消息提示");
			        	} else{
			        		//信息推送
			        		PushMessage.push(conn, request, PushMessage.ZJB_LBJ,"项目："+jh.getXmmc()+"  标段："+jh.getBdmc()+"。拦标价已完成,可以进行招标。【此项目正常編制造价、不需要送财审】",null,vo1.getSjbh(),vo1.getYwlx(),vo1.getGc_zjb_lbjb_id(),"消息提示");
			        	}
					}
				}
			// 不需要編制造价
			} else if("3".equals(vo1.getZjbzzt())) {
				if(Pub.empty(jh.getBdmc())) {
					//信息推送
	    			PushMessage.push(conn, request, PushMessage.ZJB_LBJ,"项目："+jh.getXmmc()+" 。拦标价已完成,可以进行招标。【此项目不需要編制造价】",null,vo1.getSjbh(),vo1.getYwlx(),vo1.getGc_zjb_lbjb_id(),"消息提示");
	        	} else{
	        		//信息推送
	        		PushMessage.push(conn, request, PushMessage.ZJB_LBJ,"项目："+jh.getXmmc()+"  标段："+jh.getBdmc()+"。拦标价已完成,可以进行招标。【此项目不需要編制造价】",null,vo1.getSjbh(),vo1.getYwlx(),vo1.getGc_zjb_lbjb_id(),"消息提示");
	        	}
			}
			// end
			//-------------加入处理中心任务生成代码-------------BEGIN---------
			String sql = "select to_char(TBJ_SJ,'yyyy-MM-dd'),to_char(CS_SJ,'yyyy-MM-dd') from GC_JH_SJ where GC_JH_SJ_ID='"+vo.getJhsjid()+"'";
			String arr[][] = DBUtil.query(conn, sql);
			if(vo.getJgbcsrq()!=null && arr!=null && arr.length!=0){
				String jsrq = DateTimeUtil.getDateTime(vo.getJgbcsrq(),"yyyy-MM-dd");
				//如果建管报财审日期存在，那么生成上报值处理任务
				if(arr[0][0]!=null && !jsrq.equals(arr[0][0])){
					ClzxManagerService cms = new ClzxManagerServiceImpl();
					cms.createTask("2001001", vo.getJhsjid(), user,conn);
				}
			}
			if(vo.getCzswrq()!=null && arr!=null && arr.length!=0){
				String jsrq = DateTimeUtil.getDateTime(vo.getCzswrq(),"yyyy-MM-dd");
				//如果财政审完日期存在，那么生成审定值处理任务
				if(arr[0][1]!=null && !jsrq.equals(arr[0][1])){
					ClzxManagerService cms = new ClzxManagerServiceImpl();
					cms.createTask("2001101", vo.getJhsjid(), user,conn);
				}
			}
			//-------------加入处理中心任务生成代码-------------END---------
			conn.commit();
			LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改拦标价信息成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			/*return resultVO;*/
		String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_zjb_lbjb_id()+"\",\"fieldname\":\"gc_zjb_lbjb_id\",\"operation\":\"=\",\"logic\":\"and\"} ,{\"value\":\""+vo.getNd()+"\",\"fieldname\":\"jhsj.nd\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String result=this.queryConditionLbj(jsona,user,"0");
			return result;
	}
	@Override
	public String updateZt(HttpServletRequest request, String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjbLbjbVO vo = new GcZjbLbjbVO();
		TcjhVO jh= new TcjhVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			vo.setYwlx(ywlx);
			
			if(Pub.empty(vo.getGc_zjb_lbjb_id())){
				vo.setGc_zjb_lbjb_id(new RandomGUID().toString()); // 主键
				BusinessUtil.setInsertCommonFields(vo, user);
				vo.setYwlx(ywlx);
				EventVO eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
				vo.setSjbh(eventVO.getSjbh());
				BaseDAO.insert(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "插入拦标价信息成功", user,"","");
			}else{
				 //根据ID查YWID和SJBH
				GcZjbLbjbVO vo1=(GcZjbLbjbVO) BaseDAO.getVOByPrimaryKey(conn, vo);
				//插入
				BaseDAO.update(conn, vo);	
				LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "修改拦标价信息成功", user,"","");
			}
			resultVO = vo.getRowJson();
			conn.commit();
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			/*return resultVO;*/
			String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_zjb_lbjb_id()+"\",\"fieldname\":\"gc_zjb_lbjb_id\",\"operation\":\"=\",\"logic\":\"and\"} ,{\"value\":\""+vo.getNd()+"\",\"fieldname\":\"jhsj.nd\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String result=this.queryConditionLbj(jsona,user,"0");
			return result;
	}

}
