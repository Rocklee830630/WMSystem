package com.ccthanking.business.wttb.service.impl;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ccthanking.business.bgh.GcBghWtVO;
import com.ccthanking.business.wttb.service.WttbService;
import com.ccthanking.business.wttb.vo.WttbEventVO;
import com.ccthanking.business.wttb.vo.WttbHflsVO;
import com.ccthanking.business.wttb.vo.WttbInfoVO;
import com.ccthanking.business.wttb.vo.WttbLzlsVO;
import com.ccthanking.business.wttb.vo.WttbPfqkVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.common.vo.OrgPersonVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.params.ParaManager;
import com.ccthanking.framework.params.AppPara.AppParaVO;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
@Service
public class WttbServiceImpl implements WttbService{
	AppParaVO[] syspara = (AppParaVO[]) ParaManager.getInstance().getAppParameter("",YwlxManager.GC_WTTB_XX,"WTTBYJCOLOR");
	
	@Override
	public String insertInfo(String json,User user)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		WttbInfoVO vo = new WttbInfoVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			vo.setYwlx(YwlxManager.GC_WTTB_XX);
			if(Pub.empty(vo.getWttb_info_id())){
				//设置主键
				vo.setWttb_info_id(new RandomGUID().toString()); // 主键
				vo.setSjzt("0");//事件状态 存草稿
				EventVO eventVO = EventManager.createEvent(conn, YwlxManager.GC_WTTB_XX, user);//生成事件
				vo.setSjbh(eventVO.getSjbh());
				BusinessUtil.setInsertCommonFields(vo,user);
				//插入
				BaseDAO.insert(conn, vo);
			}else{
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
			}
			resultVO = vo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "问题提报保存成功", user,"","");

		} catch (Exception e) {
			conn.rollback();
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "问题提报保存操作失败", user,"","");
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultVO;
	}


	@Override
	public String queryPsnList(String json, User user, PageManager page)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			QueryConditionList list = RequestUtil.getConditionList(json);
			String condition = list == null ? "" : list.getConditionWhere();
			if(StringUtils.isBlank(condition)){
				condition += "1=1";
			}
			page = RequestUtil.getPageManager(json);
			if (page == null){
				page = new PageManager();
			}
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select * from FS_ORG_PERSON";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("DEPARTMENT");
			bs.setFieldUserID("LRR");
			bs.setFieldDic("SEX", "XB");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null)
				conn.close();
		}
		return domresult;
	}
	@Override
	public String sendReport(HttpServletRequest request,String json)
			throws Exception {
		// TODO Auto-generated method stub
		String domresult = "";
		Connection conn = DBUtil.getConnection();
		WttbInfoVO vo = new WttbInfoVO();
		WttbLzlsVO lzVO = new WttbLzlsVO();
		WttbPfqkVO pfVO = new WttbPfqkVO();
		Date sysdate = Pub.getCurrentDate();
		String ywid = request.getParameter("ywid");
		String sjzt = request.getParameter("sjzt")==null?"1":"2";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try{
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			lzVO.setValueFromJson((JSONObject)list.get(0));
			pfVO.setValueFromJson((JSONObject)list.get(0));
			//补全信息主表
			vo.setSjzt(sjzt);
			vo.setYwlx(YwlxManager.GC_WTTB_XX);
//			vo.setYjsj(lzVO.getXwwcsj());
			EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			//如果没有主键，那么表示这是一条新记录
			if(Pub.empty(vo.getWttb_info_id())){
				vo.setWttb_info_id(new RandomGUID().toString()); // 主键
				vo.setSjbh(event.getSjbh());
				//插入
				BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
			}else{
				//更新信息主表
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
			}
			//-----------------------------------录入流转历史------------------------------//
			String[] jsrArr = lzVO.getJsr().split(":");
			String[] zbrArr = jsrArr[0].split(",");
			String[] sbrArr = jsrArr[1].split(",");
			lzVO.setWtid(vo.getWttb_info_id());
			lzVO.setFsr(user.getAccount());
			lzVO.setFsbm(user.getDepartment());
			lzVO.setFssj(sysdate);
			lzVO.setYwlx(YwlxManager.GC_WTTB_LZ);
			lzVO.setSjbh(event.getSjbh());
			BusinessUtil.setInsertCommonFields(lzVO,user);
//			String sqlLZ = "update WTTB_LZLS set SFYX='0' where WTID='"+vo.getWttb_info_id()+"'";
			String sqlLZ = "delete from WTTB_LZLS where WTID='"+vo.getWttb_info_id()+"'";
			DBUtil.execSql(conn, sqlLZ);
//			String sqlPF = "update WTTB_PFQK set SFYX='0' where WTID='"+vo.getWttb_info_id()+"'";
			String sqlPF = "delete from WTTB_PFQK where WTID='"+vo.getWttb_info_id()+"'";
			DBUtil.execSql(conn, sqlPF);
			//-----------------------------------发起人-----------------------------------//
			String zfid = new RandomGUID().toString();
			lzVO.setWttb_lzls_id(zfid);
			lzVO.setJsr(user.getAccount());
			lzVO.setJsbm(user.getDepartment());
			lzVO.setJssj(sysdate);
			lzVO.setBlrjs("0");//发起人
			lzVO.setSfdqblr("0");	//发起人不是当前办理人
			lzVO.setZfid(zfid);
			BaseDAO.insert(conn, lzVO);
			for(int i=0;i<zbrArr.length;i++){
				lzVO.setWttb_lzls_id(new RandomGUID().toString());
				User jsrUser = UserManager.getInstance().getUserByLoginName(zbrArr[i]);
				lzVO.setJsr(jsrUser.getAccount());
				lzVO.setJsbm(jsrUser.getDepartment());
				lzVO.setJssj(sysdate);
				lzVO.setBlrjs("1");//主办人
				lzVO.setSfdqblr("1");	//主办人是当前办理人
				BaseDAO.insert(conn, lzVO);
//				sendMessage.sendMessageToPerson(conn, "问题提报",
//						user.getName()+" 完成[问题提报]业务，请查看", 
//						user.getAccount(), lzVO.getJsr(), false, false, false, "", "", lzVO.getSjbh(), vo.getYwlx());
//				ServletContext application = request.getSession().getServletContext();
//				DwrService.remindToPerson(application,new String[]{user.getName()+" 有问题需要您解决，请查看[问题提报]",zbrArr[i]});
//				PushMessage.push(request,"",user.getName()+" 完成[问题提报]业务，请查看","","","","");
			}
			for(int i=0;i<sbrArr.length;i++){
				if(!Pub.empty(sbrArr[i])){
					lzVO.setWttb_lzls_id(new RandomGUID().toString());
					User jsrUser = UserManager.getInstance().getUserByLoginName(sbrArr[i]);
					lzVO.setJsr(jsrUser.getAccount());
					lzVO.setJsbm(jsrUser.getDepartment());
					lzVO.setJssj(sysdate);
					lzVO.setBlrjs("4");//从办人
					lzVO.setSfdqblr("0");	//从办人不是当前办理人
					BaseDAO.insert(conn, lzVO);
//					sendMessage.sendMessageToPerson(conn, "问题提报", 
//							user.getName()+" 完成[问题提报]业务，请查看", 
//							user.getAccount(), lzVO.getJsr(), false, false, false, "", "", lzVO.getSjbh(), vo.getYwlx());
//					ServletContext application = request.getSession().getServletContext();
//					DwrService.remindToPerson(application,new String[]{user.getName()+" 有问题需要您解决，请查看[问题提报]",sbrArr[i]});
//					PushMessage.push(request,"",user.getName()+" 完成[问题提报]业务，请查看","","","","");
				}
			}
			if("2".equals(sjzt)||sjzt=="2"){
				//如果问题是直接发送给本部门领导的，那么也不需要审核了
			}else{
				if(jsrArr.length>2){
					String[] blrArr = jsrArr[2].split(",");
					for(int i=0;i<blrArr.length;i++){
						if(!Pub.empty(blrArr[i])){
							lzVO.setWttb_lzls_id(new RandomGUID().toString());
							User jsrUser = UserManager.getInstance().getUserByLoginName(blrArr[i]);
							lzVO.setJsr(jsrUser.getAccount());
							lzVO.setJsbm(jsrUser.getDepartment());
							lzVO.setJssj(sysdate);
							lzVO.setBlrjs("3");		//提请接收人
							lzVO.setSfdqblr("0");	//审核人不是当前办理人
							BaseDAO.insert(conn, lzVO);
	//						sendMessage.sendMessageToPerson(conn, "问题提报",
	//								user.getName()+" 完成[问题提报]业务，请审核", 
	//								user.getAccount(), lzVO.getJsr(), false, false, false, "", "", lzVO.getSjbh(), vo.getYwlx());
//							ServletContext application = request.getSession().getServletContext();
							//DwrService.remindToPerson(application,new String[]{user.getName()+" 有问题需要您解决，请查看[问题提报]",zbrArr[i]});
	//						PushMessage.push(request,"",user.getName()+" 完成[问题提报]业务，请查看","","","","");
						}
					}
				}
			}
			//-----------------------------------录入批复情况------------------------------//
//			pfVO.setWttb_pfqk_id(new RandomGUID().toString());
			if(!Pub.empty(ywid)){
				pfVO.setWttb_pfqk_id(ywid);
				FileUploadVO fvo = new FileUploadVO();
				fvo.setFjzt("1");
				fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
				fvo.setGlid3(vo.getXmid());	//存入项目ID
				fvo.setGlid4(vo.getBdid());	//存入标段ID
				fvo.setSjbh(vo.getSjbh());	//存入时间编号
				fvo.setYwlx(vo.getYwlx());	//存入时间编号
				FileUploadService.updateVOByYwid(conn, fvo, ywid,user);
//				FileUploadService.updateFjztByYwid(conn, ywid);
			}else{
				pfVO.setWttb_pfqk_id(new RandomGUID().toString());
			}
			pfVO.setWtid(vo.getWttb_info_id());
			pfVO.setPfr(user.getAccount());
			pfVO.setPfdw(user.getDepartment());
			pfVO.setPfsj(sysdate);
			pfVO.setSjbh(event.getSjbh());
			pfVO.setYwlx(YwlxManager.GC_WTTB_GT);
			pfVO.setNrlx("0");
			pfVO.setLzid(zfid);
			BusinessUtil.setInsertCommonFields(pfVO,user);
			BaseDAO.insert(conn, pfVO);
			conn.commit();
			domresult = Pub.makeQueryConditionByID(vo.getWttb_info_id(), "I.WTTB_INFO_ID");
			domresult = this.queryInfoFqr(domresult, request);
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String doSendBack(HttpServletRequest request,String json)
			throws Exception {
		// TODO Auto-generated method stub
		String domresult = "";
		Connection conn = DBUtil.getConnection();
		WttbInfoVO vo = new WttbInfoVO();
		WttbPfqkVO pfVO = new WttbPfqkVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String ywid = request.getParameter("ywid");
		try{
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			pfVO.setValueFromJson((JSONObject)list.get(0));
			//插入批复数据
			if(!Pub.empty(ywid)){
				pfVO.setWttb_pfqk_id(ywid);
				WttbInfoVO condWtVO = (WttbInfoVO) BaseDAO.getVOByPrimaryKey(conn,
						vo);
				FileUploadVO fvo = new FileUploadVO();
				fvo.setFjzt("1");
				fvo.setGlid2(condWtVO.getJhsjid());//存入计划数据ID
				fvo.setGlid3(condWtVO.getXmid());	//存入项目ID
				fvo.setGlid4(condWtVO.getBdid());	//存入标段ID
				fvo.setSjbh(condWtVO.getSjbh());	//存入时间编号
				fvo.setYwlx(condWtVO.getYwlx());	//存入时间编号
				FileUploadService.updateVOByYwid(conn, fvo, ywid,user);
//				FileUploadService.updateFjztByYwid(conn, ywid);
			}else{
				pfVO.setWttb_pfqk_id(new RandomGUID().toString());
			}
			pfVO.setYwlx(YwlxManager.GC_WTTB_GT);
			EventVO event = EventManager.createEvent(conn, pfVO.getYwlx(), user);//生成事件
			pfVO.setSjbh(event.getSjbh());
			pfVO.setWtid(vo.getWttb_info_id());
			pfVO.setPfr(user.getAccount());
			pfVO.setPfsj(Pub.getCurrentDate());
			pfVO.setPfdw(user.getDepartment());
			pfVO.setNrlx("1");
			String sql = "select WTTB_LZLS_ID from WTTB_LZLS where wtid='"+pfVO.getWtid()+"' and JSR='"+user.getAccount()+"' and BLRJS='3'";
			String[][] arr = DBUtil.query(conn, sql);
			if(arr!=null){
				pfVO.setLzid(arr[0][0]);
			}
			BusinessUtil.setInsertCommonFields(pfVO,user);
			BaseDAO.insert(conn, pfVO);
			//更新主表的状态为“退回”
			WttbInfoVO resVO = new WttbInfoVO();
			resVO.setWttb_info_id(vo.getWttb_info_id());
			resVO.setSjzt("5");
			BusinessUtil.setUpdateCommonFields(resVO,user);
			BaseDAO.update(conn, resVO);
			//通知录入人
			resVO = (WttbInfoVO)BaseDAO.getVOByPrimaryKey(conn, vo);
			String[] members = {resVO.getLrr()};
//			PushMessage.push(request,"","问题提报被退回，请查看","",pfVO.getSjbh(),pfVO.getYwlx(),"",members);
			ServletContext application = request.getSession().getServletContext();
			//DwrService.remindToPerson(application,new String[]{user.getName()+" 问题提报被退回，请查看",resVO.getLrr()});
//			result = this.queryInfoByWtid(conn, vo.getWttb_info_id());
			conn.commit();
			domresult = Pub.makeQueryConditionByID(vo.getWttb_info_id(), "I.WTTB_INFO_ID");
			domresult = this.queryInfoJsr(domresult, request);
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String doRevoke(String json, User user) throws Exception {
		// TODO Auto-generated method stub
		String result = "";
		Connection conn = DBUtil.getConnection();
		WttbInfoVO vo = new WttbInfoVO();
		WttbEventVO eventVO = new WttbEventVO();
		try{
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			eventVO.setValueFromJson((JSONObject)list.get(0));
			//更新事件表
			String sql = "update wttb_event set sfwc='0' ,pfnr='问题发起人撤销问题，系统自动批复',pfsj=sysdate  where sfwc='1' and wtid='"+vo.getWttb_info_id()+"'";
			DBUtil.execSql(conn, sql);
			//更新信息主表
			String updateSql = "update wttb_info set sjzt='2' where id='"+vo.getWttb_info_id()+"' and sjzt='1'";
			DBUtil.execSql(conn, updateSql);
			//查询出所有接收人，用于发送消息
			String voArr[][] = DBUtil.query(conn, "select jsr from wttb_event where wtid='"+vo.getWttb_info_id()+"'");
			for(int i=0;i<voArr.length;i++){
//				sendMessage.sendMessageToPerson(conn, "问题提报", 
//						user.getName()+" 发起人已撤销业务", 
//						user.getAccount(), voArr[i][0], false, false, false, "", "", eventVO.getSjbh(), vo.getYwlx());
			}
			result = this.queryInfoByWtid(conn, vo.getWttb_info_id());
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "问题撤销成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "问题撤销失败", user,"","");
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return result;
	}
	@Override
	public String doSugg(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		String domresult = "";
		Connection conn = DBUtil.getConnection();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		WttbInfoVO vo = new WttbInfoVO();
		WttbPfqkVO pfVO = new WttbPfqkVO();
		String ywid = request.getParameter("ywid");
		String nrlx = request.getParameter("nrlx");
		try{
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			pfVO.setValueFromJson((JSONObject)list.get(0));
			nrlx = Pub.empty(nrlx)?"1":nrlx;
			WttbInfoVO condWtVO = (WttbInfoVO) BaseDAO.getVOByPrimaryKey(conn,
					vo);
			if(!Pub.empty(ywid)){
				pfVO.setWttb_pfqk_id(ywid);
				FileUploadVO fvo = new FileUploadVO();
				fvo.setFjzt("1");
				fvo.setGlid2(condWtVO.getJhsjid());//存入计划数据ID
				fvo.setGlid3(condWtVO.getXmid());	//存入项目ID
				fvo.setGlid4(condWtVO.getBdid());	//存入标段ID
				fvo.setSjbh(condWtVO.getSjbh());	//存入时间编号
				fvo.setYwlx(condWtVO.getYwlx());	//存入时间编号
				FileUploadService.updateVOByYwid(conn, fvo, ywid,user);
//				FileUploadService.updateFjztByYwid(conn, ywid);
			}else{
				pfVO.setWttb_pfqk_id(new RandomGUID().toString());
			}
			String wtid = vo.getWttb_info_id();
			String lssjSql = "select LSSJ,WTBT,SJZT from WTTB_INFO where WTTB_INFO_ID='"+wtid+"'";
			String lssjArr[][] = DBUtil.query(conn, lssjSql);
			String lssj = lssjArr[0][0];
			String wtbt = lssjArr[0][1];
			String sql = "";
			sql = "select WTTB_LZLS_ID,BLRJS,SFDQBLR " +
			"from WTTB_LZLS " +
			"where jsr='"+user.getAccount()+"' " +
			"and WTID='"+vo.getWttb_info_id()+"' " +
			"order by SFDQBLR DESC";
			String[][] arr = DBUtil.query(conn, sql);
			WttbInfoVO wtVO = new WttbInfoVO();
			wtVO.setWttb_info_id(vo.getWttb_info_id());
			pfVO.setYwlx(YwlxManager.GC_WTTB_GT);
			EventVO event = EventManager.createEvent(conn, pfVO.getYwlx(), user);//生成事件
			pfVO.setSjbh(event.getSjbh());
			if(arr==null){
				//Do Nothing
			}else{
				pfVO.setLzid(arr[0][0]);
				WttbLzlsVO lzvo = new WttbLzlsVO();
				lzvo.setWttb_lzls_id(arr[0][0]);
				lzvo.setHfqk("1");
				BusinessUtil.setUpdateCommonFields(lzvo,user);
				BaseDAO.update(conn, lzvo);//更新回复情况
				if("1".equals(lssj)){//历史数据走以前的回复方式
					if("1".equals(arr[0][1])){
						wtVO.setSjzt("4");
						BusinessUtil.setUpdateCommonFields(wtVO,user);
						BaseDAO.update(conn, wtVO);
					}
				}else{//新数据走逐级回复
					if("1".equals(arr[0][2])){
						String dqblrSql = "select WTTB_LZLS_ID,JSR from WTTB_LZLS where WTID='"+wtid+"' and SFDQBLR='1'";
						//如果主办人正好是当前办理人，那么回复并且将当前办理人传给上一个人，并记录回传历史
						//如果主办人不是当前办理人，那么只回复
						String[][] dqblrArr = DBUtil.query(conn, dqblrSql) ;
						if(dqblrArr!=null && dqblrArr.length>0){
							String jsrAccount = dqblrArr[0][1];
							if(jsrAccount.equals(user.getAccount())){
								if(!"7".equals(lssjArr[0][2])){//对于已上会的问题，不进行逐级回复
									//取消所有当前办理人
									String updateDqblrSql = "update WTTB_LZLS set SFDQBLR='0' where WTID='"+wtid+"'";
									DBUtil.execSql(conn, updateDqblrSql);
									//将上一个转发人的记录设置为当前办理人
									String currentLzid = getNextZfid(dqblrArr[0][0], wtid, conn);
	//								String queryFqrLzidSql = "select WTTB_LZLS_ID from WTTB_LZLS where BLRJS='0' and WTID='"+wtid+"' and SFYX='1'";
	//								String fqrLzid = DBUtil.query(conn, queryFqrLzidSql)[0][0];
									if("0".equals(currentLzid)){//果到达了发起人，那么不用记录了，发起人选择回复和解决就可以了
										wtVO.setSjzt("4");
										BusinessUtil.setUpdateCommonFields(wtVO,user);
										BaseDAO.update(conn, wtVO);
									}else{//如果还在回复过程中，那么逐级向前找节点
										updateDqblrSql = "update WTTB_LZLS set SFDQBLR='1' where WTID='"+wtid+"' and WTTB_LZLS_ID='"+currentLzid+"'";
										DBUtil.execSql(conn, updateDqblrSql);
										String getDqblrSql = "select JSR from WTTB_LZLS where WTTB_LZLS_ID='"+currentLzid+"'";
										String nextJsrAccount = DBUtil.query(conn, getDqblrSql)[0][0];
										//将转发记录保存到回复历史表
										WttbHflsVO hfvo = new WttbHflsVO();
										hfvo.setWttb_hfls_id(new RandomGUID().toString());
										hfvo.setWtid(wtid);
										hfvo.setJsr(nextJsrAccount);
										hfvo.setJsid(currentLzid);
										hfvo.setHfid(dqblrArr[0][0]);
										BusinessUtil.setInsertCommonFields(hfvo,user);
										BaseDAO.insert(conn, hfvo);
										//消息推送 delete by zhangbr@ccthanking.com 2015-07-07 又不推送了
	//										PushMessage.push(conn, request,
	//												PushMessage.WTTB_BLRHF, user.getName()+"已经回复了问题【"
	//														+ wtbt + "】，请办理",
	//												"/jsp/business/wttb/clWt.jsp?openType=1&infoID="
	//														+ vo.getWttb_info_id(), vo
	//														.getSjbh(), vo.getYwlx(),
	//												vo.getWttb_info_id(), new String[]{nextJsrAccount},
	//												"问题当前办理人已批复");
									}
								}
							}
						}
					}
				}
			}
			pfVO.setWtid(vo.getWttb_info_id());
			pfVO.setPfr(user.getAccount());
			pfVO.setPfsj(Pub.getCurrentDate());
			pfVO.setPfdw(user.getDepartment());
			pfVO.setNrlx(nrlx);
			BusinessUtil.setInsertCommonFields(pfVO,user);
			BaseDAO.insert(conn, pfVO);
			//查询问题发起人，用于发送消息
//			String voArr[][] = DBUtil.query(conn, "select lrr from wttb_info where wttb_info_id='"+vo.getWttb_info_id()+"'");
//			sendMessage.sendMessageToPerson(conn, "问题提报", 
//					user.getName()+" 已批复，请查看", 
//					user.getAccount(), voArr[0][0], false, false, false, "", "", pfVO.getSjbh(), pfVO.getYwlx());

			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "问题批复成功", user,"","");
			conn.commit();
			if("0".equals(nrlx) || "2".equals(nrlx)){
				//发起人的问题描述，就不需要发桌面提醒了
			}else{
//				ServletContext application = request.getSession().getServletContext();
				//DwrService.remindToPerson(application,new String[]{"您的[问题提报]业务，"+user.getName()+"已批复，请查看[问题提报]",voArr[0][0]});
			}
			domresult = Pub.makeQueryConditionByID(vo.getWttb_info_id(), "I.WTTB_INFO_ID");
			domresult = this.queryInfoJsr(domresult, request);
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "问题批复失败", user,"","");
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	public String getNextZfid(String currentID,String wtid,Connection conn) throws Exception{
		String res = "";
		String queryJsrSql = "select FSR,BLRJS,ZFID from WTTB_LZLS " +
				"where ZFID=(select ZFID from WTTB_LZLS where WTID='"+wtid+"' and WTTB_LZLS_ID='"+currentID+"') order by BLRJS asc";
		String jsrArr[][] = DBUtil.query(conn, queryJsrSql);
		String queryLrrSql = "select LRR from WTTB_INFO where WTTB_INFO_ID='"+wtid+"'";
		String lrrArr[][] = DBUtil.query(conn, queryLrrSql);
		if("0".equals(jsrArr[0][1])){
			//如果已经退回到发起人了，，那么不再向前找了，回复过程到达终点
//			res = jsrArr[0][2];
			res = "0";
		}else if(lrrArr[0][0].equals(jsrArr[0][0])){
			//或者退回到审核人并且审核人就是发送人
			String queryNextJsrSql = "select FSR,BLRJS,ZFID,JSR from WTTB_LZLS where  WTTB_LZLS_ID='"+jsrArr[0][2]+"'";
			String nextJsrArr[][] = DBUtil.query(conn, queryNextJsrSql);
			if("3".equals(nextJsrArr[0][1]) && nextJsrArr[0][0].equals(nextJsrArr[0][3])){
				res = "0";
			}
		}
		if(!"0".equals(res)){
			String queryHflsSql = "select count(*) from WTTB_HFLS where JSR='"+jsrArr[0][0]+"' and WTID='"+wtid+"' and SFYX='1'";
			String hflsCount = DBUtil.query(conn, queryHflsSql)[0][0];
			if("0".equals(hflsCount)){
				//如果下一个接收人在逐级回复历史中不存在，那么返回这条记录ID
				res = jsrArr[0][2];
			}else{
				//如果下一个接收人在逐级回复历史中已存在，那么继续向前找
				res = getNextZfid(jsrArr[0][2], wtid, conn);
			}
		}
		return res;
	}
	@Override
	public String doConfirm(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		String domresult = "";
		Connection conn = DBUtil.getConnection();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		WttbInfoVO vo = new WttbInfoVO();
		String blrjs = request.getParameter("blrjs");
		WttbLzlsVO lzvo = new WttbLzlsVO();
		try{
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			lzvo.setValueFromJson((JSONObject)list.get(0));
			//更新流转表，对当前主办人进行评分
			String sql = "update WTTB_LZLS set MYCD=decode(BLRJS,'1','"+lzvo.getMycd()+"','') where SFYX='1' and WTID='"+vo.getWttb_info_id()+"'";
			DBUtil.execSql(conn, sql);
			//更新信息主表,批复完成，待填报人确认
			BusinessUtil.setUpdateCommonFields(vo,user);
			BaseDAO.update(conn, vo);

			//查询问题接收人，用于发送消息，不包括当前登录人
			String wtzt = Pub.getDictValueByCode("WTZT", vo.getSjzt());
			String lzArr[][] = DBUtil.query(conn, "select jsr from wttb_lzls where wtid='"+vo.getWttb_info_id()+"' and jsr!='"+user.getAccount()+"'");
			//
			if(lzArr!=null && lzArr.length>0){
				List<String> jsrList = new ArrayList<String>();
				for(int lz=0;lz<lzArr.length;lz++){
					jsrList.add(lzArr[lz][0]);
				}
				String[] jsrArr = jsrList.toArray(new String[jsrList.size()]);
				//消息推送
				PushMessage.push(conn, request, PushMessage.WTTB_FQRJS,"问题【"+vo.getWtbt()+"】状态变更为“"+wtzt+"”。","/jsp/business/wttb/wtCard.jsp?infoID="+vo.getWttb_info_id(),vo.getSjbh(),vo.getYwlx(),vo.getWttb_info_id(),jsrArr,"问题处理结束");
			}
			
//			result = this.queryInfoByWtid(conn, vo.getWttb_info_id());
			conn.commit();
			if("fqr".equals(blrjs)){
				domresult = Pub.makeQueryConditionByID(vo.getWttb_info_id(), "I.WTTB_INFO_ID");
				domresult = this.queryInfoFqr(domresult, request);
			}else{
				domresult = Pub.makeQueryConditionByID(vo.getWttb_info_id(), "I.WTTB_INFO_ID");
				domresult = this.queryInfoJsr(domresult, request);
			}
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	public String queryInfoByWtid(Connection conn,String id) throws Exception {
		String domresult = "";
		WttbInfoVO vo = new WttbInfoVO();
		vo.setWttb_info_id(id);
		vo = (WttbInfoVO)BaseDAO.getVOByPrimaryKey(conn, vo);
		domresult = vo.getRowJson();
		return domresult;
	}
	@Override
	public String queryInfoByWtid(HttpServletRequest request,String id) throws Exception {
		String domresult = "";
		Connection conn = null;
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try{
			conn = DBUtil.getConnection();
//			WttbInfoVO vo = new WttbInfoVO();
//			vo.setWttb_info_id(id);
//			vo = (WttbInfoVO)BaseDAO.getVOByPrimaryKey(conn, vo);
			String condition = "";
			String cond = "X.wttb_info_id=P.WTID(+) and X.WTTB_INFO_ID=L.WTID(+) and P.NRLX='0' and P.wtid='"+id+"'";
			condition += cond;
			condition += BusinessUtil.getSJYXCondition("P") + BusinessUtil.getCommonCondition(user,null);
			String sql = "select wttb_info_id, xmmc, bdmc,XMID,BDID, wtlx, ywtxz, wtbt, yjsj, sjsj, sjzt, wtxz, cqbz, X.lrr, X.lrsj, X.lrbm, X.lrbmmc, X.gxr, X.gxsj, X.gxbm, X.gxbmmc, X.ywlx, X.sjbh, X.sjmj, X.sfyx, X.jhsjid,X.HYID " +
					",P.PFNR,P.JYJJFA,P.WTTB_PFQK_ID,X.TBLX,L.JSR as ZBR," +
					"(select PFNR from(select B.PFNR from WTTB_LZLS A,WTTB_PFQK B where A.WTTB_LZLS_ID=B.LZID and A.BLRJS='1' and B.WTID='"+id+"' order by B.LRSJ desc) where rownum<2) ZBRYJ " +
					"from wttb_info X,WTTB_PFQK P,(select JSR,WTID from WTTB_LZLS where BLRJS='1') L ";
			PageManager page = new PageManager();
			page.setFilter(condition);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldDateFormat("YJSJ", "yyyy-MM-dd");
			bs.setFieldUserID("LRR");
			bs.setFieldUserID("ZBR");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldDic("WTLX", "WTLX");
			bs.setFieldDic("WTXZ", "WTXZ");
			bs.setFieldDic("SJZT", "WTZT");
			domresult = bs.getJson();
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String querySomeData(String id, User user, PageManager page)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			String condition = " I.ID=E.WTID(+) and I.XMID=X.ID(+) and X.XMBH=B.XMBH(+) and I.ID='"+id+"'";
			if (page == null){
				page = new PageManager();
			}
//			condition += " order by pfsj asc ";
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select E.JSR JSR,E.JSDW JSDW ,X.ID XMID,X.XMMC XMMC, B.BDBH BDBH,B.BDMC BDMC,E.PFID PFID,E.SFWC SFWC " +
					"from WTTB_INFO I,WTTB_EVENT E,GC_TCJH_XMXDK X,GC_XMBD B ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("WTLX", "WTLX");
			bs.setFieldDic("SJZT", "WTZT");
			bs.setFieldOrgDept("JSDW");
			bs.setFieldUserID("JSR");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String doSupplySugg(String json, User user) throws Exception {
		// TODO Auto-generated method stub
		String result = "";
		Connection conn = DBUtil.getConnection();
		WttbInfoVO vo = new WttbInfoVO();
		WttbEventVO eventVO = new WttbEventVO();
		try{
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			eventVO.setValueFromJson((JSONObject)list.get(0));
			EventVO event = EventManager.createEvent(conn, YwlxManager.GC_WTTB_GT, user);//生成事件
			//更新事件表
			eventVO.setPfid(new RandomGUID().toString());
			eventVO.setSfwc("0");
			eventVO.setPfsj(Pub.getCurrentDate());
			eventVO.setJsr(user.getAccount());
			eventVO.setJsdw(user.getDepartment());
			eventVO.setJssj(eventVO.getPfsj());
			eventVO.setSjbh(event.getSjbh());
			BaseDAO.insert(conn, eventVO);
//			String sql = "update wttb_event set sfwc='0' ,pfnr='"+eventVO.getPfnr()+"' pf  where sfwc='1' and wtid='"+vo.getId()+"'";
//			DBUtil.execSql(conn, sql);
			//更新信息主表,批复完成，待填报人确认
			String updateSql = "update wttb_info set sjzt='4' where id='"+vo.getWttb_info_id()+"' and sjzt='1'";
			DBUtil.execSql(conn, updateSql);
			//查询问题发起人，用于发送消息
			String voArr[][] = DBUtil.query(conn, "select lrr from wttb_info where id='"+vo.getWttb_info_id()+"'");
//			sendMessage.sendMessageToPerson(conn, "问题提报", 
//					user.getName()+" 已批复，请查看", 
//					user.getAccount(), voArr[0][0], false, false, false, "", "", eventVO.getSjbh(), vo.getYwlx());
			result = this.queryInfoByWtid(conn, vo.getWttb_info_id());
			LogManager.writeUserLog(eventVO.getSjbh(), eventVO.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "问题提报补充意见成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(eventVO.getSjbh(), eventVO.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "问题提报补充意见失败", user,"","");
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return result;
	}

	@Override
	public String queryInfoFqr(String json, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		String sjzt = request.getParameter("sjzt");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += BusinessUtil.getSJYXCondition("I") + BusinessUtil.getCommonCondition(user,null);
			String roleCond = "";
			String sql = "";
			//发起人查询
			sql = "select distinct I.wttb_info_id, I.jhsjid, I.wtlx, I.wtbt, I.yjsj, I.sjsj, I.sjzt, I.wtxz, I.cqbz, I.lrr, I.lrsj, I.lrbm, I.lrbmmc, I.gxr, I.gxsj, I.gxbm, I.gxbmmc, I.ywlx, I.sjbh, I.sjmj, I.sfyx,I.xmmc,I.bdmc,I.HYID " +
					",'' BLQK,L.JSR,XWWCSJ,CBCS,HFQK," +
					"(select JSR from WTTB_LZLS B where B.BLRJS='1' and B.WTID=I.WTTB_INFO_ID) ZBR " +
					" from WTTB_INFO I,(select WTID,JSR,XWWCSJ,HFQK " +
						"from WTTB_LZLS L,WTTB_INFO I " +
						"where L.WTID(+)=I.WTTB_INFO_ID " +
						"and L.BLRJS = '1' " +
						"and L.lrsj in (select max(lrsj) from WTTB_LZLS group by WTID)) L";
			roleCond = " and I.WTTB_INFO_ID=L.WTID and I.LRR='"+user.getAccount()+"' ";
			if(!Pub.empty(sjzt)){
				roleCond +=" and sjzt in ("+sjzt+") ";
			}
			condition +=roleCond;
			condition +=orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			bs.setFieldDic("WTLX", "WTLX");
			bs.setFieldDic("CQBZ", "CQBZ");
			bs.setFieldDic("SJZT", "WTZT");
			bs.setFieldDic("HFQK", "HFQK");
			bs.setFieldUserID("JSR");
			bs.setFieldUserID("ZBR");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//我发起的问题
	@Override
	public String queryInfoJsr(String json, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String bllx = request.getParameter("bllx");
		String hfqk = request.getParameter("hfqk");
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = " order by LRSJ desc ";//RequestUtil.getOrderFilter(json);//用户要求，这里按照提出时间排序
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += BusinessUtil.getSJYXCondition("I") + BusinessUtil.getCommonCondition(user,null);
			String roleCond = "";
			String sql = "";
			//批复人查询
//			sql = "select distinct I.wttb_info_id, I.xmbh, I.bdbh, I.wtlx, I.wtbt, I.yjsj, I.sjsj, I.sjzt, I.wtxz, I.cqbz, I.lrr, I.lrsj, I.lrbm, I.lrbmmc, I.gxr, I.gxsj, I.gxbm, I.gxbmmc, I.ywlx, I.sjbh, I.sjmj, I.sfyx,I.xmmc,I.bdmc " +
//					",JSBM,JSR ,'' BLQK " +
//					"from WTTB_INFO I,WTTB_LZLS L  ";
			sql = "select distinct I.wttb_info_id, I.jhsjid, I.wtlx, I.wtbt, I.yjsj, I.sjsj, I.sjzt, I.wtxz, I.cqbz, I.lrr, I.lrsj, I.lrbm, I.lrbmmc, I.gxr, I.gxsj, I.gxbm, I.gxbmmc, I.ywlx, I.sjbh, I.sjmj, I.sfyx,I.xmmc,I.bdmc,I.HYID " +
					",'' BLQK,L.JSR,L.XWWCSJ ,decode(I.LRR,'"+user.getAccount()+"','1','2') LRRFLAG,CBCS,L.HFQK," +
					"(select JSR from WTTB_LZLS B where B.BLRJS='1' and B.WTID=I.WTTB_INFO_ID) ZBR " +
					" from WTTB_INFO I,(select WTID,JSR,XWWCSJ,HFQK " +
					"from WTTB_LZLS L,WTTB_INFO I " +
					"where L.WTID(+)=I.WTTB_INFO_ID " +
					"and L.BLRJS = '1' ) L,WTTB_LZLS Z";
			roleCond = " and I.WTTB_INFO_ID=L.WTID(+) and I.WTTB_INFO_ID = Z.WTID(+) " +
//					"and I.SJZT!='5' " +
					"and I.LRBM='"+user.getDepartment()+"'";
			if(Pub.empty(bllx)){
				roleCond += "and ((Z.JSR='"+user.getAccount()+"' and Z.BLRJS='3' and I.SJZT!='5') or (Z.LRR='"+user.getAccount()+"') or (Z.JSR='"+user.getAccount()+"' and Z.LRR!='"+user.getAccount()+"'  and Z.BLRJS='1' and I.SJZT != '5' and Z.JSR in (select FZR from FS_ORG_DEPT))) ";
			}else{
				String cond1 = "1=0";
				String cond2 = "1=0";
				if(bllx.indexOf("1")!=-1){
					cond1 = "(Z.JSR='"+user.getAccount()+"' and Z.BLRJS='3' and Z.LRR!='"+user.getAccount()+"') or (Z.JSR='"+user.getAccount()+"' and Z.LRR!='"+user.getAccount()+"'  and Z.BLRJS='1' and I.SJZT != '5' and Z.JSR in (select FZR from FS_ORG_DEPT))";
				}
				if(bllx.indexOf("2")!=-1){
					cond2 = "(Z.LRR='"+user.getAccount()+"')";
				}
				roleCond += "and ("+cond1+" or "+cond2+") ";
			}
			condition +=roleCond;
			condition +=orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			bs.setFieldDic("WTLX", "WTLX");
			bs.setFieldDic("CQBZ", "CQBZ");
			bs.setFieldDic("SJZT", "WTZT");
			bs.setFieldDic("HFQK", "HFQK");
			bs.setFieldUserID("JSR");
			bs.setFieldUserID("ZBR");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//待处理问题
	@Override
	public String queryInfoBlr(String json, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		String blrjs = request.getParameter("blrjs");
		String hfqk = request.getParameter("hfqk");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += BusinessUtil.getSJYXCondition("I") + BusinessUtil.getCommonCondition(user,null);
			String roleCond = "";
			String sql = "";
			//发起人查询
			sql = "select distinct I.wttb_info_id, I.jhsjid, I.wtlx, I.wtbt, I.yjsj, I.sjsj, I.sjzt, I.wtxz, I.cqbz, I.lrr, I.lrsj, I.lrbm, I.lrbmmc, I.gxr, I.gxsj, I.gxbm, I.gxbmmc, I.ywlx, I.sjbh, I.sjmj, I.sfyx,I.xmmc,I.bdmc,I.HYID " +
					",'' BLQK,L.JSR,L.XWWCSJ,L.BLRJS,decode(I.LRR,'"+user.getAccount()+"','1','2') LRRFLAG,CBCS,case when BLRJS='0' or BLRJS='3' then ZBRHFQK else HFQK end HFQK," +
					"(select JSR from WTTB_LZLS B where B.BLRJS='1' and B.WTID=I.WTTB_INFO_ID) ZBR " +
					" from WTTB_INFO I,(select WTID,JSR,XWWCSJ,BLRJS,HFQK,ZBRHFQK " +
						"from (select A.WTTB_LZLS_ID,A.WTID, A.JSR, A.XWWCSJ,A.BLRJS,A.HFQK,A.LRR from (select * from WTTB_LZLS  where JSR='"+user.getAccount()+"' and SFYX='1') A,(select max(LRSJ) ZXLRSJ,WTID from WTTB_LZLS where SFYX='1' and JSR='"+user.getAccount()+"' group by WTID,JSR) B where A.WTID=B.WTID and A.LRSJ=B.ZXLRSJ) L," +
						"WTTB_INFO I,(select WTID ZBRWTID,HFQK ZBRHFQK from WTTB_LZLS where BLRJS='1') K " +
						"where L.WTID(+)=I.WTTB_INFO_ID " +
						"and K.ZBRWTID(+) = I.WTTB_INFO_ID " +
						"and ((JSR='"+user.getAccount()+"' and I.SJZT='2' and (BLRJS = '1' or BLRJS='2' or BLRJS='4') and (select JSR from WTTB_LZLS where SFYX='1' and SFDQBLR='1' and WTID=I.WTTB_INFO_ID)='"+user.getAccount()+"') " +
						"or (JSR='"+user.getAccount()+"' and I.SJZT='2' and BLRJS='4' and HFQK='0') " +
						"or (L.LRR!='"+user.getAccount()+"' and JSR='"+user.getAccount()+"' and I.SJZT in ('1','2','4') and BLRJS='3') " +
						"or (I.LRR='"+user.getAccount()+"' and (I.SJZT='5' or (ZBRHFQK='1' and I.SJZT='4'))  and BLRJS='0'))) L";
			roleCond = " and I.WTTB_INFO_ID=L.WTID " ;
//					" and WTTB_INFO_ID not in (select WTID from WTTB_PFQK where WTID=L.WTID and L.BLRJS in('2','4') and LRR='"+user.getAccount()+"' group by WTID having count(WTID)>0) " ;
//					"and L.JSR='"+user.getAccount()+"' ";
			//添加办理人角色条件
			if(!Pub.empty(blrjs)){
				roleCond += " and L.BLRJS in ("+blrjs+")";
			}else{
				//roleCond += " and (L.BLRJS='1' or L.BLRJS='2')";
				roleCond += " and (L.BLRJS='0' or L.BLRJS='1' or L.BLRJS='2' or L.BLRJS='3' or L.BLRJS='4')";
			}
			//添加回复情况条件
			if(!Pub.empty(hfqk)){
				roleCond += " and I.SJZT in ("+hfqk+")";
			}else{
				//roleCond += " and I.SJZT in ('2','3','4','6')";
				roleCond += " and I.SJZT in ('1','2','4','5')";
			}
			condition +=roleCond;
			condition +=orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			bs.setFieldDic("WTLX", "WTLX");
			bs.setFieldDic("CQBZ", "CQBZ");
			bs.setFieldDic("WTXZ", "WTXZ");
			bs.setFieldDic("SJZT", "WTZT");
			bs.setFieldDic("HFQK", "HFQK");
			bs.setFieldDic("BLRJS", "BLRJS");
			bs.setFieldUserID("JSR");//当前接收人
			bs.setFieldUserID("ZBR");//主办人
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryInfoYcl(String json, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		String blrjs = request.getParameter("blrjs");
		String hfqk = request.getParameter("hfqk");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += BusinessUtil.getSJYXCondition("I") + BusinessUtil.getCommonCondition(user,null);
			String roleCond = "";
			String sql = "";
			//发起人查询
			sql = "select distinct I.wttb_info_id, I.jhsjid, I.wtlx, I.wtbt, I.yjsj, I.sjsj, I.sjzt, I.wtxz, I.cqbz, I.lrr, I.lrsj, I.lrbm, I.lrbmmc, I.gxr, I.gxsj, I.gxbm, I.gxbmmc, I.ywlx, I.sjbh, I.sjmj, I.sfyx,I.xmmc,I.bdmc,I.HYID " +
					",'' BLQK,L.JSR,decode(I.LRR,'"+user.getAccount()+"','1','2') LRRFLAG,CBCS ,HFQK," +
					"(select JSR from WTTB_LZLS B where B.BLRJS='1' and B.WTID=I.WTTB_INFO_ID) ZBR " +
					" from WTTB_INFO I,(select WTID,JSR,XWWCSJ,BLRJS,HFQK " +
						"from WTTB_LZLS L,WTTB_INFO I " +
						"where L.WTID(+)=I.WTTB_INFO_ID and ((JSR='"+user.getAccount()+"' and (I.SJZT in('3','4','6','7')) " +
						" and (BLRJS='1' or BLRJS='2' or BLRJS='4')" +
						" ) " +
						"or (I.LRR='"+user.getAccount()+"' and I.SJZT in('3','4','6')) " +
						"or (JSR='"+user.getAccount()+"' " +
								"and (BLRJS='1' or BLRJS='2' or BLRJS='4') " +
								"and I.SJZT='2' " +
								"and (select JSR from WTTB_LZLS where WTID=I.WTTB_INFO_ID and SFYX='1' and SFDQBLR='1')!='"+user.getAccount()+"' " +
								"and (select count(WTTB_PFQK_ID) from WTTB_PFQK where WTID=I.WTTB_INFO_ID and SFYX='1' and PFR='"+user.getAccount()+"')>0))) L";
			roleCond = " and I.WTTB_INFO_ID=L.WTID and L.JSR='"+user.getAccount()+"' ";
			//添加办理人角色条件
			if(!Pub.empty(blrjs)){
				roleCond += " and L.BLRJS in ("+blrjs+")";
			}else{
				roleCond += " and (L.BLRJS='1' or L.BLRJS='2' or L.BLRJS='4')";
				//roleCond += " and (L.BLRJS='1' or L.BLRJS='2' or L.BLRJS='3')";
			}
			//添加回复情况条件
			if(!Pub.empty(hfqk)){
				roleCond += " and I.SJZT in ("+hfqk+") ";
			}else{
				//roleCond += " and I.SJZT in ('2','3','4','6')";
				roleCond += " and I.SJZT in ('1','2','4','5')";
			}
			condition +=roleCond;
			condition +=orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			bs.setFieldDic("WTLX", "WTLX");
			bs.setFieldDic("WTXZ", "WTXZ");
			bs.setFieldDic("CQBZ", "CQBZ");
			bs.setFieldDic("SJZT", "WTZT");
			bs.setFieldDic("HFQK", "HFQK");
			bs.setFieldUserID("JSR");
			bs.setFieldUserID("ZBR");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryLzls(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		String id = request.getParameter("id");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = Pub.empty(json)?new PageManager():RequestUtil.getPageManager(json);
			String condition = "";
			String roleCond = "";
			roleCond = " WTID='"+id+"'";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("X") + BusinessUtil.getCommonCondition(user,null);
			page.setFilter(condition);
			condition += " order by lrsj asc ";
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select wttb_lzls_id, wtid, fsr, fsbm, fssj, jsr, jsbm, jssj, blrjs, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, ywlx, sjbh, sjmj, sfyx, xwwcsj,wtid fj from wttb_lzls X";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("JSSJ", "yyyy-MM-dd");
			bs.setFieldFileUpload("FJ","0045");
			bs.setFieldOrgDept("JSBM");
			bs.setFieldUserID("JSR");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryPfqk(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		String id = request.getParameter("id");
		String flag = request.getParameter("flag");
		PageManager page = Pub.empty(json)?new PageManager():RequestUtil.getPageManager(json);
		if(page==null){
			page.setPageRows(1000);
		}
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String condition = "";
			String roleCond = "";
			roleCond = " 1=1 ";
			condition +=roleCond;
			//用户又不想显示发起人的信息了
			//2014-05-12 用户又想显示发起人的信息了
			//排序改为：主办人-经办人和抄送人按回复时间倒序-部门负责人-发起人
			if("2".equals(flag)){
				condition += " order by XH asc,NRLX asc,PFSJ desc ";
			}else{
				condition += " order by XH asc,NRLX asc,PFSJ desc ";
			}
			page.setFilter(condition);
			String sql = "select wttb_lzls_id,wtid,fsr,fsbm,fssj,JSR,jsbm,jssj,blrjs,lrr,lrsj,lrbm,lrbmmc,gxr,gxsj,gxbm,gxbmmc,ywlx,sjbh,sjmj,sfyx,xwwcsj,FJ,PFSJ,PFNR, JYJJFA,NRLX,XH from("+
					//发起人
					"select wttb_lzls_id,L.wtid,L.fsr,L.fsbm,L.fssj,L.JSR,L.jsbm,L.jssj,L.blrjs,L.lrr,L.lrsj,L.lrbm,L.lrbmmc,L.gxr,L.gxsj,L.gxbm,L.gxbmmc,L.ywlx,L.sjbh,L.sjmj,L.sfyx,L.xwwcsj,P.WTTB_PFQK_ID fj  " +
					",P.PFSJ,  P.PFNR, P.JYJJFA,P.NRLX,4 as XH " +
					"from wttb_lzls L,WTTB_PFQK P where  L.WTTB_LZLS_ID=P.LZID(+) and L.WTID='"+id+"' and BLRJS='0' " +
					//审核人
					" union all "+
					"select wttb_lzls_id,L.wtid,L.fsr,L.fsbm,L.fssj,L.JSR,L.jsbm,L.jssj,L.blrjs,L.lrr,L.lrsj,L.lrbm,L.lrbmmc,L.gxr,L.gxsj,L.gxbm,L.gxbmmc,L.ywlx,L.sjbh,L.sjmj,L.sfyx,L.xwwcsj,P.WTTB_PFQK_ID fj  " +
					",decode(W.SJZT,'1',to_date('','yyyy'),L.GXSJ) PFSJ,P.PFNR, P.JYJJFA,P.NRLX,3 as XH " +
					"from wttb_lzls L,WTTB_PFQK P,(select SJZT,WTTB_INFO_ID from WTTB_INFO) W where  L.WTTB_LZLS_ID=P.LZID(+) and L.WTID=W.WTTB_INFO_ID and L.WTID='"+id+"' and BLRJS='3' " +
					//主办人
					" union all "+
					"select wttb_lzls_id,L.wtid,L.fsr,L.fsbm,L.fssj,L.JSR,L.jsbm,L.jssj,L.blrjs,L.lrr,L.lrsj,L.lrbm,L.lrbmmc,L.gxr,L.gxsj,L.gxbm,L.gxbmmc,L.ywlx,L.sjbh,L.sjmj,L.sfyx,L.xwwcsj," +
					"case when P.XXLY='1' then (select HYID from WTTB_INFO where WTTB_INFO_ID='"+id+"') else P.WTTB_PFQK_ID end fj  " +
					",P.PFSJ,  P.PFNR, P.JYJJFA,P.NRLX,1 as XH " +
					"from wttb_lzls L,WTTB_PFQK P where  L.WTTB_LZLS_ID=P.LZID(+) and L.WTID='"+id+"' and BLRJS='1' " +
					//送办人
					" union all "+
					"select wttb_lzls_id,L.wtid,L.fsr,L.fsbm,L.fssj,L.JSR,L.jsbm,L.jssj,L.blrjs,L.lrr,L.lrsj,L.lrbm,L.lrbmmc,L.gxr,L.gxsj,L.gxbm,L.gxbmmc,L.ywlx,L.sjbh,L.sjmj,L.sfyx,L.xwwcsj," +
					"case when P.XXLY='1' then (select HYID from WTTB_INFO where WTTB_INFO_ID='"+id+"') else P.WTTB_PFQK_ID end fj  " +
					",P.PFSJ,  P.PFNR, P.JYJJFA,P.NRLX,2 as XH " +
					"from wttb_lzls L,WTTB_PFQK P where  L.WTTB_LZLS_ID=P.LZID(+) and L.WTID='"+id+"' and BLRJS='2' "+
					//抄送人
					" union all "+
					"select wttb_lzls_id,L.wtid,L.fsr,L.fsbm,L.fssj,L.JSR,L.jsbm,L.jssj,L.blrjs,L.lrr,L.lrsj,L.lrbm,L.lrbmmc,L.gxr,L.gxsj,L.gxbm,L.gxbmmc,L.ywlx,L.sjbh,L.sjmj,L.sfyx,L.xwwcsj," +
					"case when P.XXLY='1' then (select HYID from WTTB_INFO where WTTB_INFO_ID='"+id+"') else P.WTTB_PFQK_ID end fj  " +
					",P.PFSJ,  P.PFNR, P.JYJJFA,P.NRLX,2 as XH " +
					"from wttb_lzls L,WTTB_PFQK P where  L.WTTB_LZLS_ID=P.LZID(+) and L.WTID='"+id+"' and BLRJS='4' "+
					") ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("PFSJ", "yyyy-MM-dd HH:mm");
			bs.setFieldDateFormat("XWWCSJ", "yyyy-MM-dd");
			bs.setFieldDic("BLRJS", "BLRJS");
			bs.setFieldUserID("PFR");
			bs.setFieldUserID("JSR");
			bs.setFieldFileUpload("FJ","0045,0071");//附件有可能是从会议中心上传的
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String getColor(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		String id = request.getParameter("id");
		WttbInfoVO vo = new WttbInfoVO();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject) list.get(0));
			String lzSql = "select to_char(YJSJ,'yyyy-mm-dd hh:mi:ss') " +
					"from wttb_info I " +
					"where sjzt='2' and WTTB_INFO_ID='"+vo.getWttb_info_id()+"'";
			String lzArr[][] = DBUtil.query(conn, lzSql);
			if(lzArr!=null){
				String jssj = lzArr[0][0];
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d1 = sdf.parse(jssj);
				Date d2 = sdf.parse(sdf.format(Pub.getCurrentDate()));
				if(d1.after(d2)){
					int blts = daysBetween(d1, d2);
					int min = 0;
					for (AppParaVO sysVO : syspara) {
						int max = Integer.parseInt(sysVO.getAppParaParavalue2());
						if (vo.getWtxz().equals(sysVO.getAppParaParavalue1())) {
							if (blts >= max) {
								if (max <= min) {
									min = max;
									List lists = new ArrayList();
									HashMap map = new HashMap();
									map.put("CLASS", sysVO.getAppParaParavalue3());
									map.put("TITLE", sysVO.getAppParaParavalue4().replace("::", String.valueOf(Math.abs(blts))));
									lists.add(map);
									domresult = this.makeSimpleResult(lists);
								}
							}
						}
					}
				}else{
					for (AppParaVO sysVO : syspara) {
						int blts = daysBetween(d1, d2);
						int min = 0;
						int max = Integer.parseInt(sysVO.getAppParaParavalue2());
						if (vo.getWtxz().equals(sysVO.getAppParaParavalue1())) {
							if (blts >= max) {
								if (max >= min) {
									min = max;
									List lists = new ArrayList();
									HashMap map = new HashMap();
									map.put("CLASS", sysVO.getAppParaParavalue3());
									map.put("TITLE", sysVO.getAppParaParavalue4());
									lists.add(map);
									domresult = this.makeSimpleResult(lists);
								}
							}
						}
					}
				}
				domresult = domresult==""?"none":domresult;
			}else{
				domresult="none";
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	/** 
	 * 计算两个日期之间相差的天数 
	 * @param smdate 较小的时间
	 * @param bdate  较大的时间
	 * @return 相差天数
	 * @throws ParseException 
	 */
	public int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 *字符串的日期格式的计算
	 */
	public int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
	/**
	 * 根据map的key和value生成简单的json串形式的返回值
	 * @param map
	 * @return
	 */
	public String makeSimpleResult(List lists){
		JSONObject jso = new JSONObject();
		JSONObject json = new JSONObject();
		//内层查询条件		开始
        JSONArray jsa = new JSONArray();
//        for(HashMap map:(HashMap)lists){}
        for(Iterator li = lists.iterator();li.hasNext();){
        	HashMap map = (HashMap)li.next();
        	JSONObject jsono = new JSONObject();
            Iterator it = map.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry e = (Map.Entry)it.next();
                jsono.put(e.getKey(),e.getValue());
            }
            jsa.add(jsono);
        }
        //内层查询条件		结束
        json.put("data", jsa);
        jso.put("response", json);
        return jso.toString();
	}


	@Override
	public String doConfirmSend(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		String domresult = "";
		Connection conn = DBUtil.getConnection();
		WttbInfoVO vo = new WttbInfoVO();
		WttbLzlsVO lzVO = new WttbLzlsVO();
		WttbPfqkVO pfVO = new WttbPfqkVO();
		Date sysdate = Pub.getCurrentDate();
		String ywid = request.getParameter("ywid");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		int queryFlag = 0;//查询方式判断变量，用于区分部长和普通人员
		String bzLzid = "";
		try{
			String fqrName = user.getName();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			lzVO.setValueFromJson((JSONObject)list.get(0));
			pfVO.setValueFromJson((JSONObject)list.get(0));
			//补全信息主表
			vo.setYwlx(YwlxManager.GC_WTTB_XX);
			vo.setYwtxz(vo.getWtxz());
//			vo.setYjsj(lzVO.getXwwcsj());
			EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			//如果没有主键，那么表示这是一条新记录
			String lzlsSql = "select lrr,WTTB_LZLS_ID,to_char(lrsj,'yyyy-mm-dd hh24:mi:ss') lrsj,lrbm,lrbmmc,zfid from WTTB_LZLS where WTID='"+vo.getWttb_info_id()+"' and BLRJS='0'";
			String[][] lzArr = DBUtil.query(conn,lzlsSql);
			String shrid = new RandomGUID().toString();
			if(Pub.empty(vo.getWttb_info_id())){
				vo.setWttb_info_id(new RandomGUID().toString()); // 主键
				vo.setSjzt("2");//事件状态 存处理中
				vo.setSjbh(event.getSjbh());
				//插入
				BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
			}else{
				//更新信息主表
				vo.setSjzt("2");//事件状态 存处理中
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
				String sql = "delete from WTTB_LZLS where WTID='"+vo.getWttb_info_id()+"'";
				DBUtil.execSql(conn, sql);
			}
			//-----------------------------------录入流转历史------------------------------//
			String[] jsrArr = lzVO.getJsr().split(":");
			String[] zbrArr = jsrArr[0].split(",");
			lzVO.setWtid(vo.getWttb_info_id());
			lzVO.setFsr(user.getAccount());
			lzVO.setFsbm(user.getDepartment());
			lzVO.setFssj(sysdate);
			lzVO.setYwlx(YwlxManager.GC_WTTB_LZ);
			lzVO.setSjbh(event.getSjbh());
			lzVO.setSfdqblr("0");
			BusinessUtil.setInsertCommonFields(lzVO,user);
			String zfid = "";
			if(lzArr!=null){
				zfid = lzArr[0][5];
				lzVO.setZfid(zfid);
				fqrName = Pub.getUserNameByLoginId(lzArr[0][0]);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				lzVO.setLrr(lzArr[0][0]);
				lzVO.setFsr(lzArr[0][0]);
				lzVO.setLrsj(sdf.parse(lzArr[0][2]));
				lzVO.setWttb_lzls_id(lzArr[0][1]);
				lzVO.setJsr(lzArr[0][0]);
				lzVO.setJsbm(user.getDepartment());
				lzVO.setJssj(sysdate);
				lzVO.setBlrjs("0");//发起人
				BaseDAO.insert(conn, lzVO);
				queryFlag = 0;
			}else{
				zfid = new RandomGUID().toString();
				lzVO.setZfid(zfid);
				fqrName = user.getName();
				lzVO.setLrr(user.getAccount());
				lzVO.setFsr(user.getAccount());
				lzVO.setWttb_lzls_id(zfid);
				lzVO.setJsr(user.getAccount());
				lzVO.setJsbm(user.getDepartment());
				lzVO.setJssj(sysdate);
				lzVO.setBlrjs("0");//发起人
				//lzVO.setZfid(new RandomGUID().toString());
				BaseDAO.insert(conn, lzVO);
				queryFlag = 1;
				bzLzid = lzVO.getWttb_lzls_id();
			}
			lzVO.setZfid(shrid);//主办人和送办人的转发ID使用审核人角色的主键
			for(int i=0;i<zbrArr.length;i++){
				lzVO.setWttb_lzls_id(new RandomGUID().toString());
				User jsrUser = UserManager.getInstance().getUserByLoginName(zbrArr[i]);
				lzVO.setJsr(jsrUser.getAccount());
				lzVO.setJsbm(jsrUser.getDepartment());
				lzVO.setJssj(sysdate);
				lzVO.setBlrjs("1");//主办人
				lzVO.setSfdqblr("1");
				BaseDAO.insert(conn, lzVO);
//				sendMessage.sendMessageToPerson(conn, "问题提报",
//						fqrName+" 完成[问题提报]业务，请查看", 
//						user.getAccount(), lzVO.getJsr(), false, false, false, "", "", lzVO.getSjbh(), vo.getYwlx());
				ServletContext application = request.getSession().getServletContext();
				//DwrService.remindToPerson(application,new String[]{fqrName+" 您发送的问题已通过审核，请查看[问题提报]",zbrArr[i]});
//				PushMessage.push(request,"",fqrName+" 完成[问题提报]业务，请查看", "","","","");
			}
			if(jsrArr.length>1){
				String[] sbrArr = jsrArr[1].split(",");
				for(int i=0;i<sbrArr.length;i++){
					if(!Pub.empty(sbrArr[i])){
						lzVO.setWttb_lzls_id(new RandomGUID().toString());
						User jsrUser = UserManager.getInstance().getUserByLoginName(sbrArr[i]);
						lzVO.setJsr(jsrUser.getAccount());
						lzVO.setJsbm(jsrUser.getDepartment());
						lzVO.setJssj(sysdate);
						lzVO.setBlrjs("4");//抄送人
						lzVO.setSfdqblr("0");
						BaseDAO.insert(conn, lzVO);
//						sendMessage.sendMessageToPerson(conn, "问题提报", 
//								fqrName+" 完成[问题提报]业务，请查看", 
//								user.getAccount(), lzVO.getJsr(), false, false, false, "", "", lzVO.getSjbh(), vo.getYwlx());
						ServletContext application = request.getSession().getServletContext();
						//DwrService.remindToPerson(application,new String[]{fqrName+" 有问题需要您解决，请查看[问题提报]",sbrArr[i]});
					}
				}
			}
			lzVO.setWttb_lzls_id(shrid);
			lzVO.setZfid(zfid);
			lzVO.setJsr(user.getAccount());
			lzVO.setJsbm(user.getDepartment());
			lzVO.setJssj(sysdate);
			lzVO.setBlrjs("3");//审核人
			lzVO.setSfdqblr("0");
			BaseDAO.insert(conn, lzVO);
			//-----------------------------------录入批复情况------------------------------//
//			pfVO.setWttb_pfqk_id(new RandomGUID().toString());
			pfVO.setWtid(vo.getWttb_info_id());
			pfVO.setPfdw(user.getDepartment());
			pfVO.setPfsj(sysdate);
			pfVO.setSjbh(event.getSjbh());
			pfVO.setYwlx(YwlxManager.GC_WTTB_GT);
			String pfls = "select count(decode(NRLX,1,1,0)) from WTTB_PFQK where wtid='"+vo.getWttb_info_id()+"'";
			String arr[][] = DBUtil.query(conn, pfls);
			if(arr!=null && (arr[0][0]!="0" || !"0".equals(arr[0][0]))){
//				pfVO.setPfr(user.getAccount());
			}else{
				pfVO.setPfr(user.getAccount());
			}
			pfVO.setNrlx("0");//部长也是发起人
			if(Pub.empty(pfVO.getWttb_pfqk_id())){
				if(!Pub.empty(ywid)){
					pfVO.setWttb_pfqk_id(ywid);
					FileUploadService.updateFjztByYwid(conn, ywid);
				}else{
					pfVO.setWttb_pfqk_id(new RandomGUID().toString());
				}
				pfVO.setLzid(bzLzid);
				BusinessUtil.setInsertCommonFields(pfVO,user);
				BaseDAO.insert(conn, pfVO);
			}else{
				BusinessUtil.setUpdateCommonFields(pfVO,user);
				BaseDAO.update(conn, pfVO);
			}
			conn.commit();
			domresult = Pub.makeQueryConditionByID(vo.getWttb_info_id(), "I.WTTB_INFO_ID");
			if(queryFlag==0){
				domresult = this.queryInfoBlr(domresult, request);
			}else{
				domresult = this.queryInfoJsr(domresult, request);
			}
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryLeaderFlag(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
//			String sql = "select count(ROW_ID) " +
//					"from FS_ORG_DEPT " +
//					"where " +
//					"FZR='"+user.getAccount()+"'" +
//					" OR FGZR='"+user.getAccount()+"'" +
//					" OR YBZR='"+user.getAccount()+"'";
			String sql = "select count(ACCOUNT) from (select FZR ACCOUNT from FS_ORG_DEPT where FZR='"+user.getAccount()+"' "+
					"union all "+
					"select ACCOUNT from VIEW_ZR where ACCOUNT='"+user.getAccount()+"' "+
					")";
			String arr[][] = DBUtil.query(conn, sql);
			if(arr!=null){
				if(Integer.parseInt(arr[0][0])>0){
					domresult = "true";
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	} 
	@Override
	public String queryLeader(HttpServletRequest request, String json)
	throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		OrgPersonVO vo = new OrgPersonVO();
		try {
			conn = DBUtil.getConnection();
			vo = this.getFzrUserJson(conn, user);
			domresult = vo.getRowJson();
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	} 
	public OrgPersonVO getFzrUserJson(Connection conn,Object obj) throws Exception{
		User user = (User) obj;
		conn.setAutoCommit(false);
		OrgPersonVO vo = new OrgPersonVO();
		String isZrSql = "select count(*) from FS_ORG_DEPT where FGZR='"+user.getAccount()+"' or YBZR='"+user.getAccount()+"'";
		String zrArr[][] = DBUtil.query(conn, isZrSql);
		if(zrArr!=null && !"0".equals(zrArr[0][0])){
			//主任
			vo.setAccount(user.getAccount());
//			String querySql = "select ACCOUNT,NAME from FS_ORG_PERSON where ACCOUNT='"+user.getAccount()+"'";
			vo = (OrgPersonVO) BaseDAO.getVOByPrimaryKey(conn,vo);
			vo.setFlag("ZR");
		}else{
			//部门人员
			String sql = "select FZR from FS_ORG_DEPT where ROW_ID='"+user.getDepartment()+"'";
			String[][] rs = DBUtil.query(conn, sql);
			if(rs[0][0]!=null && !"".equals(rs[0][0]) && rs[0][0]!=""){
				vo.setAccount(rs[0][0]);
				vo = (OrgPersonVO) BaseDAO.getVOByPrimaryKey(conn,vo);
			}
		}
		return vo;
	}
	@Override
	public String doTransfer(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		String domresult = "";
		Connection conn = DBUtil.getConnection();
		WttbInfoVO vo = new WttbInfoVO();
		WttbLzlsVO lzVO = new WttbLzlsVO();
		WttbPfqkVO pfVO = new WttbPfqkVO();
		Date sysdate = Pub.getCurrentDate();
		String ywid = request.getParameter("ywid");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try{
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			lzVO.setValueFromJson((JSONObject)list.get(0));
			pfVO.setValueFromJson((JSONObject)list.get(0));
			//补全信息主表
			EventVO event = EventManager.createEvent(conn, YwlxManager.GC_WTTB_LZ, user);//生成事件
			//-----------------------------------录入流转历史------------------------------//
			String[] jsrArr = lzVO.getJsr().split(":");
			String[] zbrArr = jsrArr[0].split(",");
			lzVO.setWtid(vo.getWttb_info_id());
			lzVO.setFsr(user.getAccount());
			lzVO.setFsbm(user.getDepartment());
			lzVO.setFssj(sysdate);
			lzVO.setYwlx(YwlxManager.GC_WTTB_LZ);
			lzVO.setSjbh(event.getSjbh());
			BusinessUtil.setInsertCommonFields(lzVO,user);
			String zbrName = "";
			for(int i=0;i<zbrArr.length;i++){
				String getSfZbrSql = "select count(*) from WTTB_LZLS where JSR='"+user.getAccount()+"' and wtid='"+vo.getWttb_info_id()+"' and BLRJS='1'";
				String sfZbr = DBUtil.query(conn, getSfZbrSql)[0][0];
				//获取当前主办人记录的主键，记录到表中，用于后期退回业务
				String getZbrsql = "select WTTB_LZLS_ID from WTTB_LZLS where JSR='"+user.getAccount()+"' and wtid='"+vo.getWttb_info_id()+"' and SFDQBLR='1'";
				String zbrsqlArr[][] = DBUtil.query(conn, getZbrsql);
				lzVO.setZfid(zbrsqlArr[0][0]);
				if("0".equals(sfZbr)){
					//如果是当前办理人转发，但是不是主办人，那么记录转发ID，并取消当前办理人权限
					String sql = "update WTTB_LZLS set SFDQBLR='0' where JSR='"+user.getAccount()+"' and wtid='"+vo.getWttb_info_id()+"' and SFDQBLR='1'";
					DBUtil.execSql(conn,sql);
					String qxZbrSql = "update WTTB_LZLS set BLRJS='2' where  wtid='"+vo.getWttb_info_id()+"' and BLRJS='1'";
					DBUtil.execSql(conn,qxZbrSql);
				}else{
					//如果是当前办理人转发，并且是主办人，那么要将当前办理人设置为经办人，并记录转发ID，并取消当前办理人权限
					//取消当前人员的主办人权限
					String sql = "update WTTB_LZLS set BLRJS='2',SFDQBLR='0' where JSR='"+user.getAccount()+"' and wtid='"+vo.getWttb_info_id()+"' and BLRJS='1'";
					DBUtil.execSql(conn,sql);
				}
				lzVO.setBlrjs("1");//主办人
				lzVO.setWttb_lzls_id(new RandomGUID().toString());
				User jsrUser = UserManager.getInstance().getUserByLoginName(zbrArr[i]);
				lzVO.setJsr(jsrUser.getAccount());
				lzVO.setJsbm(jsrUser.getDepartment());
				lzVO.setJssj(sysdate);
				lzVO.setSfdqblr("1");//主办人自动成为获取当前办理人
				BaseDAO.insert(conn, lzVO);
				zbrName += jsrUser.getName()+",";
//				sendMessage.sendMessageToPerson(conn, "问题提报",
//						user.getName()+" 完成[问题提报]业务，请查看", 
//						user.getAccount(), lzVO.getJsr(), false, false, false, "", "", lzVO.getSjbh(), vo.getYwlx());
//				ServletContext application = request.getSession().getServletContext();
				//DwrService.remindToPerson(application,new String[]{user.getName()+" 有问题需要您解决，请查看[问题提报]",zbrArr[i]});
//				PushMessage.push(request,"",user.getName()+" 完成[问题提报]业务，请查看", "","","","");
			}
			if(zbrName.length()>0){
				zbrName = zbrName.substring(0,zbrName.length()-1);
			}
			if(jsrArr.length>1){
				String[] sbrArr = jsrArr[1].split(",");
				for(int i=0;i<sbrArr.length;i++){
					if(!Pub.empty(sbrArr[i])){
						lzVO.setWttb_lzls_id(new RandomGUID().toString());
						User jsrUser = UserManager.getInstance().getUserByLoginName(sbrArr[i]);
						lzVO.setJsr(jsrUser.getAccount());
						lzVO.setJsbm(jsrUser.getDepartment());
						lzVO.setJssj(sysdate);
						lzVO.setBlrjs("4");//从办人
						lzVO.setSfdqblr("0");//从办人都不是当前办理人
						BaseDAO.insert(conn, lzVO);
//						sendMessage.sendMessageToPerson(conn, "问题提报", 
//								user.getName()+" 完成[问题提报]业务，请查看", 
//								user.getAccount(), lzVO.getJsr(), false, false, false, "", "", lzVO.getSjbh(), vo.getYwlx());
//						ServletContext application = request.getSession().getServletContext();
						//DwrService.remindToPerson(application,new String[]{user.getName()+" 有问题需要您解决，请查看[问题提报]",sbrArr[i]});
					}
				}
			}
			//-----------------------------------录入批复情况------------------------------//
			String sql = "select WTTB_LZLS_ID,BLRJS from WTTB_LZLS where jsr='"+user.getAccount()+"' and WTID='"+vo.getWttb_info_id()+"'";
			String[][] arr = DBUtil.query(conn, sql);
			if(arr==null){
				//Do Nothing
			}else{
				pfVO.setLzid(arr[0][0]);
			}
			WttbInfoVO condWtVO = (WttbInfoVO) BaseDAO.getVOByPrimaryKey(conn,
					vo);
			if(!Pub.empty(ywid)){
				pfVO.setWttb_pfqk_id(ywid);
				FileUploadVO fvo = new FileUploadVO();
				fvo.setFjzt("1");
				fvo.setGlid2(condWtVO.getJhsjid());//存入计划数据ID
				fvo.setGlid3(condWtVO.getXmid());	//存入项目ID
				fvo.setGlid4(condWtVO.getBdid());	//存入标段ID
				fvo.setSjbh(condWtVO.getSjbh());	//存入时间编号
				fvo.setYwlx(condWtVO.getYwlx());	//存入时间编号
				FileUploadService.updateVOByYwid(conn, fvo, ywid,user);
//				FileUploadService.updateFjztByYwid(conn, ywid);
			}else{
				pfVO.setWttb_pfqk_id(new RandomGUID().toString());
			}
			pfVO.setWtid(vo.getWttb_info_id());
			pfVO.setPfr(user.getAccount());
			pfVO.setPfdw(user.getDepartment());
			pfVO.setPfsj(sysdate);
			pfVO.setSjbh(event.getSjbh());
			pfVO.setYwlx(YwlxManager.GC_WTTB_GT);
			pfVO.setNrlx("1");
			BusinessUtil.setInsertCommonFields(pfVO,user);
			BaseDAO.insert(conn, pfVO);
			//----------------------------------------修改问题提报主表的提报情况
			String getSjztSql = "select SJZT from WTTB_INFO where WTTB_INFO_ID='"+vo.getWttb_info_id()+"'";
			String sjzt = DBUtil.query(conn, getSjztSql)[0][0];
			WttbInfoVO newVO = new WttbInfoVO();
			newVO.setWttb_info_id(vo.getWttb_info_id());
			newVO.setTblx(vo.getTblx());
			if(sjzt=="4" || "4".equals(sjzt)){
				//如果填报人还未确认，那么把事件状态更新为处理中
				newVO.setSjzt("2");
			}
			BaseDAO.update(conn, newVO);
			//----------------------------------------修改问题提报主表的提报情况
			String updateHflsSql = "update WTTB_HFLS set SFYX='0' where WTID='"+vo.getWttb_info_id()+"'";
			DBUtil.execSql(conn, updateHflsSql);
			//消息推送 add by zhangbr@ccthanking.com 2015-07-14
			PushMessage.push(conn, request, PushMessage.WTTB_BLRHF, user
					.getName()
					+ "已经将问题【" + condWtVO.getWtbt() + "】转发给 "+zbrName,
					"", condWtVO.getSjbh(), condWtVO.getYwlx(),
					vo.getWttb_info_id(), new String[] { condWtVO.getLrr() },
					"问题转发");
			conn.commit();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryBlrjs(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String id = request.getParameter("wtid");
		PageManager page = new PageManager();
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String lssjSql = "select LSSJ from WTTB_INFO where WTTB_INFO_ID='"+id+"'";
			String lssj = DBUtil.query(conn, lssjSql)[0][0];
			String sql = "";
			if("1".equals(lssj)){
				sql = "select min(BLRJS) as BLRJS from WTTB_LZLS X";
			}else{
				sql = "select max(SFDQBLR) as BLRJS from WTTB_LZLS X";
			}
			String condition = "";
			String roleCond = "";
			roleCond = "  JSR='"+user.getAccount()+"' and WTID='"+id+"'";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("X") + BusinessUtil.getCommonCondition(user,null);
			page.setFilter(condition);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}


	@Override
	public String getWtCounts(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		PageManager page = new PageManager();
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String condition = "";
			String roleCond = "";
//			roleCond = "  I.WTTB_INFO_ID=X.WTID and K.ZBRWTID=I.WTTB_INFO_ID and " +
//					"((JSR='"+user.getAccount()+"' and I.SJZT='2' and (BLRJS = '1' or BLRJS='2' or BLRJS='4') and (select MAX(HFQK) from WTTB_LZLS C where C.WTID=X.WTID)='0') " +
//							"or (X.LRR!='"+user.getAccount()+"' and JSR='"+user.getAccount()+"' and I.SJZT in ('1','2','4') and X.BLRJS='3') " +
//							"or (I.LRR='"+user.getAccount()+"' and (I.SJZT='5' or (ZBRHFQK='1' and I.SJZT='4'))  and BLRJS='0')) " +
//							"and WTTB_INFO_ID not in (select WTID from WTTB_PFQK where WTID=X.WTID and BLRJS in('2','4') and LRR='"+user.getAccount()+"' group by WTID having count(WTID)>0) ";
//			condition +=roleCond;
//			condition += BusinessUtil.getSJYXCondition("I") + BusinessUtil.getCommonCondition(user,null);
//			page.setFilter(condition);
//			String sql = "select count(*) COUNTS from WTTB_LZLS X,WTTB_INFO I,(select WTID ZBRWTID,HFQK ZBRHFQK from WTTB_LZLS where BLRJS='1') K ";
			roleCond = "  I.WTTB_INFO_ID=X.WTID " +
					"and ((JSR='"+user.getAccount()+"' and I.SJZT='2' and (BLRJS = '1' or BLRJS='2' or BLRJS='4') and (select JSR from WTTB_LZLS where SFYX='1' and SFDQBLR='1' and WTID=I.WTTB_INFO_ID)='"+user.getAccount()+"') " +
					"or  (JSR = '"+user.getAccount()+"' and I.SJZT = '2' and BLRJS = '4' and  (select count(WTTB_LZLS_ID) from WTTB_LZLS L where L.WTID = I.WTTB_INFO_ID and L.JSR = '"+user.getAccount()+"' and HFQK='0')>0) " +
					"or (JSR='"+user.getAccount()+"' and I.SJZT in ('1','2','4') and X.BLRJS='3' and I.LRR!='"+user.getAccount()+"') " +
					"or (I.LRR='"+user.getAccount()+"' and (I.SJZT='5' or I.SJZT='4')  and BLRJS='0'))";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("X") + BusinessUtil.getCommonCondition(user,null);
			page.setFilter(condition);
			String sql = "select count(distinct I.WTTB_INFO_ID) COUNTS from WTTB_LZLS X,WTTB_INFO I ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String getWtList(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		PageManager page = new PageManager();
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String condition = "";
			String roleCond = "";
			page.setPageRows(10);
			roleCond = "  I.WTTB_INFO_ID=X.WTID " +
					"and ((JSR='"+user.getAccount()+"' and I.SJZT='2' and (BLRJS = '1' or BLRJS='2' or BLRJS='4') and (select JSR from WTTB_LZLS where SFYX='1' and SFDQBLR='1' and WTID=I.WTTB_INFO_ID)='"+user.getAccount()+"') " +
					"or  (JSR = '"+user.getAccount()+"' and I.SJZT = '2' and BLRJS = '4' and  (select count(WTTB_LZLS_ID) from WTTB_LZLS L where L.WTID = I.WTTB_INFO_ID and L.JSR = '"+user.getAccount()+"' and HFQK='0')>0) " +
					"or (JSR='"+user.getAccount()+"' and I.SJZT in ('1','2','4') and X.BLRJS='3' and I.LRR!='"+user.getAccount()+"') " +
					"or (I.LRR='"+user.getAccount()+"' and (I.SJZT='5' or I.SJZT='4')  and BLRJS='0'))";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("X") + BusinessUtil.getCommonCondition(user,null);
			condition += " order by I.LRSJ desc";
			page.setFilter(condition);
			String sql = "select distinct I.WTBT,I.WTTB_INFO_ID,I.LRSJ from WTTB_LZLS X,WTTB_INFO I ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}


	@Override
	public String doUrge(HttpServletRequest request, String json)
			throws Exception {
		String domresult = "";
		Connection conn = DBUtil.getConnection();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		WttbInfoVO vo = new WttbInfoVO();
		WttbLzlsVO lzvo = new WttbLzlsVO();
		String blrjs = request.getParameter("blrjs");
		try{
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			lzvo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo,user);
			BaseDAO.update(conn, vo);
			String sql = "";
			if("1".equals(vo.getSjzt())){
				//审核中的问题，查询审核人
				sql = "select JSR from WTTB_LZLS where BLRJS='3' and WTID='"+vo.getWttb_info_id()+"'";
			}else{
				//处理中的问题，查询主办人
				sql = "select JSR from WTTB_LZLS where BLRJS='1' and WTID='"+vo.getWttb_info_id()+"'";
			}
			String arr[][] = DBUtil.query(conn, sql);
			//对问题的当前主送人还要发一个消息
			PushMessage.push(conn, request, "", user.getName()+" 希望您能尽快对问题["+vo.getWtbt()+"]进行处理！", 
					"", vo.getSjbh(), vo.getYwlx(), "",
					arr[0], "问题提报");
			conn.commit();
			if("fqr".equals(blrjs)){
				domresult = Pub.makeQueryConditionByID(vo.getWttb_info_id(), "I.WTTB_INFO_ID");
				domresult = this.queryInfoFqr(domresult, request);
			}else{
				domresult = Pub.makeQueryConditionByID(vo.getWttb_info_id(), "I.WTTB_INFO_ID");
				domresult = this.queryInfoJsr(domresult, request);
			}
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}


	@Override
	public String doResend(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		String domresult = "";
		Connection conn = DBUtil.getConnection();
		WttbInfoVO vo = new WttbInfoVO();
		WttbLzlsVO lzVO = new WttbLzlsVO();
		WttbPfqkVO pfVO = new WttbPfqkVO();
		Date sysdate = Pub.getCurrentDate();
		String ywid = request.getParameter("ywid");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try{
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			lzVO.setValueFromJson((JSONObject)list.get(0));
			pfVO.setValueFromJson((JSONObject)list.get(0));
			//补全信息主表
			EventVO event = EventManager.createEvent(conn, YwlxManager.GC_WTTB_LZ, user);//生成事件
			//-----------------------------------录入流转历史------------------------------//
			String[] jsrArr = lzVO.getJsr().split(":");
			String[] zbrArr = jsrArr[0].split(",");
			lzVO.setWtid(vo.getWttb_info_id());
			lzVO.setFsr(user.getAccount());
			lzVO.setFsbm(user.getDepartment());
			lzVO.setFssj(sysdate);
			lzVO.setYwlx(YwlxManager.GC_WTTB_LZ);
			lzVO.setSjbh(event.getSjbh());
			BusinessUtil.setInsertCommonFields(lzVO,user);
			//取主办人信息
			String zbrSql = "select JSR from  WTTB_LZLS  where WTID='"+vo.getWttb_info_id()+"' and BLRJS='1'";
			String zbr = DBUtil.query(conn, zbrSql)[0][0];
			//取发起人信息
			String fqrSql = "select WTTB_LZLS_ID from WTTB_LZLS where BLRJS='0' and WTID='"+vo.getWttb_info_id()+"' and SFYX='1' order by LRSJ asc";
			String[][] fqr = DBUtil.query(conn, fqrSql);
			for(int i=0;i<zbrArr.length;i++){
				//取消当前人员的主办人权限
				String sql = "update WTTB_LZLS set BLRJS='2' where JSR='"+zbr+"' and wtid='"+vo.getWttb_info_id()+"' and BLRJS='1'";
				DBUtil.execSql(conn,sql);
				//取消当前人员的主办人权限
				String dablrSql = "update WTTB_LZLS set SFDQBLR='0' where wtid='"+vo.getWttb_info_id()+"'";
				DBUtil.execSql(conn,dablrSql);
				lzVO.setWttb_lzls_id(new RandomGUID().toString());
				User jsrUser = UserManager.getInstance().getUserByLoginName(zbrArr[i]);
				lzVO.setJsr(jsrUser.getAccount());
				lzVO.setJsbm(jsrUser.getDepartment());
				lzVO.setJssj(sysdate);
				lzVO.setSfdqblr("1");
				lzVO.setZfid(fqr[0][0]);
				lzVO.setBlrjs("1");//主办人
				BaseDAO.insert(conn, lzVO);
//				sendMessage.sendMessageToPerson(conn, "问题提报",
//						user.getName()+" 完成[问题提报]业务，请查看", 
//						user.getAccount(), lzVO.getJsr(), false, false, false, "", "", lzVO.getSjbh(), vo.getYwlx());
//				ServletContext application = request.getSession().getServletContext();
				//DwrService.remindToPerson(application,new String[]{user.getName()+" 有问题需要您解决，请查看[问题提报]",zbrArr[i]});
//				PushMessage.push(request,"",user.getName()+" 完成[问题提报]业务，请查看", "","","","");
			}
			if(jsrArr.length>1){
				String[] sbrArr = jsrArr[1].split(",");
				for(int i=0;i<sbrArr.length;i++){
					if(!Pub.empty(sbrArr[i])){
						lzVO.setWttb_lzls_id(new RandomGUID().toString());
						User jsrUser = UserManager.getInstance().getUserByLoginName(sbrArr[i]);
						lzVO.setJsr(jsrUser.getAccount());
						lzVO.setJsbm(jsrUser.getDepartment());
						lzVO.setJssj(sysdate);
						lzVO.setSfdqblr("0");
						lzVO.setZfid(fqr[0][0]);
						lzVO.setBlrjs("4");//从办人
						BaseDAO.insert(conn, lzVO);
//						sendMessage.sendMessageToPerson(conn, "问题提报", 
//								user.getName()+" 完成[问题提报]业务，请查看", 
//								user.getAccount(), lzVO.getJsr(), false, false, false, "", "", lzVO.getSjbh(), vo.getYwlx());
//						ServletContext application = request.getSession().getServletContext();
						//DwrService.remindToPerson(application,new String[]{user.getName()+" 有问题需要您解决，请查看[问题提报]",sbrArr[i]});
					}
				}
			}
			//-----------------------------------录入批复情况------------------------------//
			String sql = "select WTTB_LZLS_ID,BLRJS from WTTB_LZLS where jsr='"+zbr+"' and WTID='"+vo.getWttb_info_id()+"'";
			String[][] arr = DBUtil.query(conn, sql);
			if(arr==null){
				//Do Nothing
			}else{
				pfVO.setLzid(arr[0][0]);
			}
			WttbInfoVO condWtVO = (WttbInfoVO) BaseDAO.getVOByPrimaryKey(conn,
					vo);
			FileUploadVO fvo = new FileUploadVO();
			fvo.setFjzt("1");
			fvo.setGlid2(condWtVO.getJhsjid());//存入计划数据ID
			fvo.setGlid3(condWtVO.getXmid());	//存入项目ID
			fvo.setGlid4(condWtVO.getBdid());	//存入标段ID
			fvo.setSjbh(condWtVO.getSjbh());	//存入时间编号
			fvo.setYwlx(condWtVO.getYwlx());	//存入时间编号
			FileUploadService.updateVOByYwid(conn, fvo, ywid,user);
//			pfVO.setWttb_pfqk_id(new RandomGUID().toString());
//			pfVO.setWtid(vo.getWttb_info_id());
//			pfVO.setPfr(user.getAccount());
//			pfVO.setPfdw(user.getDepartment());
//			pfVO.setPfsj(sysdate);
//			pfVO.setSjbh(event.getSjbh());
//			pfVO.setYwlx(YwlxManager.GC_WTTB_GT);
//			pfVO.setNrlx("3");
//			pfVO.setPfnr("发起人转发问题");
//			BusinessUtil.setInsertCommonFields(pfVO,user);
//			BaseDAO.insert(conn, pfVO);
			//----------------------------------------修改问题提报主表的提报情况
			//String getSjztSql = "select SJZT from WTTB_INFO where WTTB_INFO_ID='"+vo.getWttb_info_id()+"'";
			//String sjzt = DBUtil.query(conn, getSjztSql)[0][0];
			WttbInfoVO newVO = new WttbInfoVO();
			newVO.setWttb_info_id(vo.getWttb_info_id());
			newVO.setTblx(vo.getTblx());
			newVO.setSjzt("2");
			BaseDAO.update(conn, newVO);
			conn.commit();
			domresult = Pub.makeQueryConditionByID(vo.getWttb_info_id(), "I.WTTB_INFO_ID");
			domresult = this.queryInfoFqr(domresult, request);
//			if(queryFlag==0){
//				domresult = this.queryInfoBlr(domresult, request);
//			}else{
//				domresult = this.queryInfoJsr(domresult, request);
//			}
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String deleteWttb(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		WttbInfoVO vo = new WttbInfoVO();
		BaseVO[] bv = null;
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			//删业务数据
			WttbInfoVO delVO = new WttbInfoVO();
			delVO.setWttb_info_id(vo.getWttb_info_id());
			delVO.setSfyx("0");
			BaseDAO.update(conn, delVO);
			String lzlsSql = "update WTTB_LZLS set SFYX='0' where WTID='"+vo.getWttb_info_id()+"'";
			DBUtil.execSql(conn, lzlsSql);
			String pfqkSql = "update WTTB_PFQK set SFYX='0' where WTID='"+vo.getWttb_info_id()+"'";
			DBUtil.execSql(conn, pfqkSql);
			//删附件
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getWttb_info_id());
			FileUploadService.deleteByConditionVO(conn, fvo);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "问题提报删除成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "问题提报删除失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String doSendToHyzx(String json, HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		String res = "true";
		WttbInfoVO wtvo = new WttbInfoVO();
		GcBghWtVO xmvo = new GcBghWtVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String wtid =request.getParameter("wtid");
		try {
			conn.setAutoCommit(false);
			WttbInfoVO condWtVO = new WttbInfoVO();
			condWtVO.setWttb_info_id(wtid);
			wtvo = (WttbInfoVO) BaseDAO.getVOByPrimaryKey(conn,
					condWtVO);
			BusinessUtil.setInsertCommonFields(xmvo, user);
			xmvo.setYwlx(YwlxManager.GC_BGH_WT);//业务类型
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			String pfnrSql = "select PFNR from WTTB_PFQK where WTID='"+wtvo.getWttb_info_id()+"' and NRLX='0'";
			String[][] pfnrArr = DBUtil.query(conn, pfnrSql);
			xmvo.setSjbh(eventVO.getSjbh());
			xmvo.setWtbt(wtvo.getWtbt());	//标题
			if(pfnrArr!=null&&pfnrArr.length>0){
				xmvo.setWtms(pfnrArr[0][0]);
			}
			xmvo.setXmid(wtvo.getXmid());
			xmvo.setBdid(wtvo.getBdid());
			xmvo.setJhsjid(wtvo.getJhsjid());
			xmvo.setFqr(user.getAccount());
			xmvo.setFqbm(user.getDepartment());
			xmvo.setFqsj(xmvo.getLrsj());
			xmvo.setWtlx("2");
			xmvo.setGc_bgh_wt_id(new RandomGUID().toString());
			xmvo.setSfgk("1");
			xmvo.setXwjjsj(wtvo.getYjsj());
			xmvo.setZt("1");
			xmvo.setWtid(wtvo.getWttb_info_id());
			xmvo.setWthxcs("0");
			//插入
			BaseDAO.insert(conn, xmvo);
			resultVO = xmvo.getRowJson();
			wtvo.setSjzt("7");
			wtvo.setHyid(xmvo.getGc_bgh_wt_id());
			BusinessUtil.setUpdateCommonFields(wtvo, user);
			BaseDAO.update(conn, wtvo);
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "办公会问题添加成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			res = "false";
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return res;
	}
	@Override
	public String getAccepter(String json, HttpServletRequest request) throws Exception{
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String wtid = request.getParameter("wtid");
		WttbInfoVO condVO = new WttbInfoVO();
		try{
			condVO.setWttb_info_id(wtid);
			conn = DBUtil.getConnection();
			WttbInfoVO vo = (WttbInfoVO) BaseDAO.getVOByPrimaryKey(conn,condVO);
			String sql = "";
			sql = "select WTTB_LZLS_ID,BLRJS,SFDQBLR " +
			"from WTTB_LZLS " +
			"where jsr='"+user.getAccount()+"' " +
			"and WTID='"+vo.getWttb_info_id()+"' " +
			"order by SFDQBLR DESC";
			String[][] arr = DBUtil.query(conn, sql);
			String jsr = "";
			if("0".equals(arr[0][2])){
				jsr = vo.getLrr();
			}else if("1".equals(arr[0][2])){
				if("7".equals(vo.getSjzt())){
					jsr = vo.getLrr();
				}else{
					String currentLzid = getNextZfid(arr[0][0], vo.getWttb_info_id(), conn);
					if("0".equals(currentLzid)){
						jsr = vo.getLrr();
					}else{
						String getDqblrSql = "select JSR from WTTB_LZLS where WTTB_LZLS_ID='"+currentLzid+"'";
						jsr = DBUtil.query(conn, getDqblrSql)[0][0];
					}
				}
			}
			JSONObject obj = new JSONObject();
			obj.put("jsr", jsr);
			obj.put("jsrname", Pub.getUserNameByLoginId(jsr));
			domresult = obj.toString();
		}catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
