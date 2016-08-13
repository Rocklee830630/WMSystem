package com.ccthanking.business.lcgl.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ccthanking.business.lcgl.LcglService;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.common.vo.XmxxVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.business.lcgl.*;

@Service
public class LcglServiceImpl implements LcglService{

	@Override
	public String queryLcInfo(HttpServletRequest request,String json) throws Exception {
		return queryLcInfo(request,json,"");
	}
	
	@Override
	public String queryLcInfo(HttpServletRequest request,String json,String defineCondition) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += defineCondition;
			condition +=orderFilter;
			String sql = "select d.*,(select flwname from TAC_FLOW where flwid = d.flaflwid) as flwname," +
					"(select DPTNAME from TAB_DEPARTMENT where DPTID = d.flaadptid) as DPTNAME," +
					"(select b.fblno from TAC_FLOW a, TAC_FLOWBILL  b where a.flwfblid = b.fblid and a.flwid = d.flaflwid) as flwno"+
					" from TAC_FLOWAPPLY d";
			//sql = "";
			page.setFilter(condition);
			conn.setAutoCommit(true);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("FLAADATE", "yyyy-MM-dd HH:mm:ss");
			bs.setFieldDateFormat("FLAFDATE", "yyyy-MM-dd HH:mm:ss");
			domresult = bs.getJson();
			LogManager.writeUserLog("", YwlxManager.OA_GWGL_GWBL,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "审批申请查询成功", user,"","");
		} catch (Exception e) {
			
			LogManager.writeUserLog("", YwlxManager.OA_GWGL_GWBL,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "审批申请查询失败", user,"","");
			e.printStackTrace(System.out);
			//DBUtil.rollbackConnetion(conn);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String queryFlowApplyByPerson(HttpServletRequest request,String json,User user) throws Exception {
		
		String defineCondition = " and FlaId in (select fleflaid from TAC_FLOWEXECUTE where FLEREMPNAME = '"+user.getName()+"')";
		return queryLcInfo(request,json,defineCondition);
	}
	
	@Override
	public String createSP(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition +=orderFilter;
			String sql = "select d.*,(select flwname from TAC_FLOW where flwid = d.flaflwid) as flwname," +
					"(select DPTNAME from TAB_DEPARTMENT where DPTID = d.flaadptid) as DPTNAME," +
					"(select b.fblno from TAC_FLOW a, TAC_FLOWBILL  b where a.flwfblid = b.fblid and a.flwid = d.flaflwid) as flwno"+
					" from TAC_FLOWAPPLY d";
			//sql = "";
			page.setFilter(condition);
			//conn.setAutoCommit(true);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("FLAADATE", "yyyy-MM-dd HH:mm:ss");
			bs.setFieldDateFormat("FLAFDATE", "yyyy-MM-dd HH:mm:ss");
			domresult = bs.getJson();
			LogManager.writeUserLog("", YwlxManager.OA_GWGL_GWBL,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "审批申请查询成功", user,"","");
		} catch (Exception e) {
			
			LogManager.writeUserLog("", YwlxManager.OA_GWGL_GWBL,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "审批申请查询失败", user,"","");
			e.printStackTrace(System.out);
			//DBUtil.rollbackConnetion(conn);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	
	@Override
	public String queryProcessType(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition +=orderFilter;
			String sql = "select processtypeoid, name, operationoid, actor, createtime, state, precondition1, precondition2, precondition3, memo, processevent, processtype from ap_processtype";
			page.setFilter(condition);
//			conn.setAutoCommit(true);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("CREATETIME", "yyyy-MM-dd HH:mm:ss");
			bs.setFieldUserID("ACTOR");
			domresult = bs.getJson();

		} catch (Exception e) {

			e.printStackTrace(System.out);
			//DBUtil.rollbackConnetion(conn);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String queryProcessStep(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition +=orderFilter;
			String sql = "select processtypeoid, stepsequence, stepoid, name, rolename, actor, state, precondition2, precondition3, precondition1, prestepoid, nextstepoid, deptlevel, application, memo, deptid, stepevent, shedule_days, stepactor, actordept, isms, iscc, ccactor from ap_processstep t ";
			page.setFilter(condition);
//			conn.setAutoCommit(true);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldUserID("ACTOR");
			bs.setFieldOrgDept("DEPTID");
			bs.setFieldUserID("CCACTOR");
			bs.setFieldOrgRole("ROLENAME");
			bs.setFieldDic("ISMS", "SPJDBSFS");
			bs.setFieldDic("ISCC", "SF");
			domresult = bs.getJson();

		} catch (Exception e) {

			e.printStackTrace(System.out);
			//DBUtil.rollbackConnetion(conn);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryProcessWs(HttpServletRequest request,String processoid) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page =new PageManager();
			//String orderFilter = RequestUtil.getOrderFilter(json);
			//String condition = RequestUtil.getConditionList(json).getConditionWhere();
			//condition +=orderFilter;
			String condition = " OPERATIONOID = '"+processoid+"'  ";
			String sql = "select a.ywlx,a.ws_template_id,a.operationoid,a.dept_level,a.deptid,a.rolename,a.CONDITION,a.ws_typz_id,PROCESSNAME from ap_ws_typz  a ";
			page.setFilter(condition);
//			conn.setAutoCommit(true);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("YWLX", "YWLX");
			bs.setFieldOrgDept("DEPTID");
			bs.setFieldOrgRole("ROLENAME");
			bs.setFieldTranslater("WS_TEMPLATE_ID", "WS_TEMPLATE", "WS_TEMPLATE_ID", "WS_TEMPLATE_NAME");
			
		//	bs.setFieldDic("ISCC", "SF");
			domresult = bs.getJson();

		} catch (Exception e) {

			e.printStackTrace(System.out);
			//DBUtil.rollbackConnetion(conn);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String saveProcess(HttpServletRequest request,String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
        String processoid = request.getParameter("processoid");
        String processname = request.getParameter("processname");
        if(Pub.empty(processname)){
        	processname = "新建流程";
        }
        boolean ifnewsave = false;
		try {
			conn.setAutoCommit(false);
		if(Pub.empty(processoid)){
			ifnewsave = true;
			String[][] t = DBUtil.query(conn,"select  max(t.processtypeoid)+1 from ap_processtype t");
			if(t==null||t.length==0||Pub.empty(t[0][0])){
				processoid = "1";
			}else{
				processoid = t[0][0];
			}
		}
		ApProcesstypeVO processtypeVO = new ApProcesstypeVO();
		if(ifnewsave == true){
		 //插入 ap_processtype
			processtypeVO.setProcesstypeoid(processoid);
			processtypeVO.setOperationoid(processoid);
			processtypeVO.setName(processname);
			processtypeVO.setActor(user.getAccount());
			processtypeVO.setCreatetime(Pub.getCurrentDate());
			processtypeVO.setState("1");
			processtypeVO.setPrecondition1("0");
			processtypeVO.setPrecondition2("0");
			processtypeVO.setPrecondition3("0");
			processtypeVO.setMemo(processname);
			processtypeVO.setProcesstype("1");
			BaseDAO.insert(conn, processtypeVO);
		}else{
			processtypeVO.setProcesstypeoid(processoid);
			processtypeVO = (ApProcesstypeVO)(Pub.getVOByCondition(conn, processtypeVO))[0];
		}
		ApProcessstepVO temp = new ApProcessstepVO();
		JSONArray list = temp.doInitJson(json);
		DBUtil.execSql(conn, "delete from ap_processstep where PROCESSTYPEOID = '"+processoid+"'");
		for(int i=0;i<list.size();i++){
			ApProcessstepVO processstepVO = new ApProcessstepVO();
			processstepVO.setValueFromJson((JSONObject)list.get(i));
			processstepVO.setProcesstypeoid(processoid);
			processstepVO.setPrecondition1("0");
			processstepVO.setPrecondition2("0");
			processstepVO.setPrecondition3("0");
			processstepVO.setState("1");
			processstepVO.setStepsequence(String.valueOf(i+1));//设置节点
			processstepVO.setStepoid(String.valueOf(i+1));//设置节点oid 
            //处理是否发送多人
//			String actor = processstepVO.getActor();
//			if(!Pub.empty(actor)){
//				String[] t = actor.split(",");
//				if(t.length>1){
//					processstepVO.setIsms("1");
//				}else{
//					processstepVO.setIsms("0");
//				}
//			}
			//处理是否抄送多人
			String ccactor = processstepVO.getCcactor();
			if(!Pub.empty(ccactor)){
				processstepVO.setIscc("1");
				
			}else{
				processstepVO.setIscc("0");
			}
			//设置节点上下级关系 
			if(list.size()>1){
				if(i<list.size()-1){
					processstepVO.setNextstepoid(String.valueOf(i+2));
				}
				
			}
			
			
			BaseDAO.insert(conn, processstepVO);

		}
		
		//插入
		resultVO = processtypeVO.getRowJson();
		conn.commit();
	
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
	@Override
	public String deleteProcess(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		ApProcesstypeVO processtypeVO = new ApProcesstypeVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = processtypeVO.doInitJson(json);
		processtypeVO.setValueFromJson((JSONObject)list.get(0));
		//删除
		BaseDAO.delete(conn, processtypeVO);
		DBUtil.execSql(conn, "delete from ap_processstep where PROCESSTYPEOID = '"+processtypeVO.getProcesstypeoid()+"'");
		DBUtil.execSql(conn,"delete from AP_WS_TYPZ where OPERATIONOID ='"+processtypeVO.getProcesstypeoid()+"'");
		resultVO = processtypeVO.getRowJson();//返回值可以自定义
		conn.commit();


		} catch (Exception e) {
			conn.rollback();

		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultVO;
	}
	@Override
	public String saveApwstypz(HttpServletRequest request,String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		ApWsTypzVO vo = new ApWsTypzVO();
        String type = request.getParameter("type");
        if(Pub.empty(type))
        	type = "add";
		try {
			conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		if("add".equals(type)){
			vo.setWs_typz_id(new RandomGUID().toString()); // 主键

			BaseDAO.insert(conn, vo);
		}else{
			BaseDAO.update(conn, vo);
		}
		
//		DBUtil.execSql(conn, "update  ap_processtype set NAME='"+vo.getProcessname()+"' where PROCESSTYPEOID = '"+vo.getOperationoid()+"'");
		resultVO = vo.getRowJson();//返回值可以自定义
		conn.commit();


		} catch (Exception e) {
			conn.rollback();

		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultVO;
	}
	@Override
	public String[] queryProcessTask(HttpServletRequest request,String processoid) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		String processresult = "";
		String processinfo = "";
		String[] r =new String[3];
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page =new PageManager();
			page.setPageRows(100);
			//String orderFilter = RequestUtil.getOrderFilter(json);
			//String condition = RequestUtil.getConditionList(json).getConditionWhere();
			//condition +=orderFilter;
			//String condition = " spbh = '"+processoid+"'  ";
			String sql = "select rwlx,ywlx,to_char(cjsj,'YYYY-MM-DD HH24:MI:SS') as cjsj,to_char(cjsj,'yyyy\"年\"fmmm\"月\"dd\"日\"HH24\"时\"') as cjsj_sv,cjdwdm,cjrxm,to_char(wcsj,'yyyy\"年\"fmmm\"月\"dd\"日\"HH24\"时\"') as wcsj,wcdwdm,wcrxm,result,DBRYID,yxbs,RESULTDSCR, SHEDULE_TIME-wcsj as cqbs from ap_task_schedule t where t.sjbh='"
					+ processoid + "' order by cjsj";
			//page.setFilter(condition);
//			conn.setAutoCommit(true);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("YWLX", "YWLX");
			bs.setFieldOrgDept("CJDWDM");
			bs.setFieldOrgDept("WCDWDM");
			bs.setFieldUserID("DBRYID");

			domresult = bs.getJson();
			
			String[][] seq = DBUtil.query(conn, "select max(STEPSEQUENCE)  from ap_task_schedule where sjbh = '"+processoid+"'");
			String maxseq = "0";
			if(seq!=null&&seq.length>0&&!"".equals(seq[0][0])){
				maxseq = seq[0][0];
			}
			String[][] spbh_ = DBUtil.query(conn, "select PROCESSOID from ap_task_schedule,ap_processinfo   where sjbh = '"+processoid+"' and spbh=PROCESSOID");
			String spbh = "";
			if(spbh_!=null&&spbh_.length>0){
				spbh = spbh_[0][0];
			}
			
			sql = "select PROCESSOID, stepsequence, stepoid, name, rolename, actor, state, precondition2, precondition3, precondition1, prestepoid, nextstepoid, deptlevel, application, memo, deptid, stepevent, shedule_days, stepactor, actordept, isms, iscc, ccactor " +
					"from ap_processdetail t where t.PROCESSOID='"+spbh+"' and t.state=1  and t.STEPSEQUENCE>"+maxseq+"";
			page =new PageManager();
			bs = DBUtil.query(conn, sql, page);
			bs.setFieldUserID("ACTOR");
			bs.setFieldUserID("CCACTOR");
			processresult = bs.getJson();
			
			sql = "select PROCESSOID,to_char(createtime,'yyyy\"年\"fmmm\"月\"dd\"日\"HH24\"时\"') as createtime,cjrid,value4 " +
					"from ap_processinfo t where t.PROCESSOID='"+spbh+"'";
			page =new PageManager();
			bs = DBUtil.query(conn, sql, page);
			bs.setFieldUserID("CJRID");
			processinfo = bs.getJson();
			r[0] = domresult;
			r[1] = processresult;
			r[2] = processinfo;
			
			

		} catch (Exception e) {

			e.printStackTrace(System.out);
			//DBUtil.rollbackConnetion(conn);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return r;
	}
	
}
