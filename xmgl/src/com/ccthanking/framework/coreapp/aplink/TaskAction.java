package com.ccthanking.framework.coreapp.aplink;

import java.util.*;
import javax.servlet.http.*;

import net.sf.json.JSONObject;

import org.apache.struts.action.*;

import java.sql.*;

import org.dom4j.*;
import org.dom4j.io.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import com.ccthanking.framework.base.*;
import com.ccthanking.framework.common.*;
import com.ccthanking.framework.*;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.*;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;

/**
 * @author leo leochou  oss@tom.com
 * @version 1.0
 */

@Controller
@RequestMapping("/TaskAction")
public class TaskAction
    
{

    public TaskAction()
    {
    }
    @RequestMapping(params = "getUserTask")
    @ResponseBody
    public requestJson getUserTask(final HttpServletRequest request,requestJson json)
	throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json.getMsg());
		String orderFilter = RequestUtil.getOrderFilter(json.getMsg());
		String condition = RequestUtil.getConditionList(json.getMsg()).getConditionWhere();
        //condition += BusinessUtil.getSJYXCondition("t");
		condition += BusinessUtil.getCommonCondition(user,"t");
        condition += " and RWZT='"+ TaskVO.TASK_STATUS_VALID + "' ";
        //condition += " and (DBDWDM='"+user.getDepartment()+"')";
        condition += " and (DBRYID='" + user.getAccount() + "' or DBRYID is null) and t.spbh = a.processoid ";
      //  condition += " and (DBROLE in (" + (Pub.empty(user.getRoleListString())?"''":user.getRoleListString());
    //    condition += ") or DBROLE is null)";
//        condition += " order by (SHEDULE_TIME - sysdate) asc";
        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			
			conn.setAutoCommit(false);
			String sql = "";
			sql = "select t.*,a.cjrid as sqr,a.createtime  from ap_task_schedule t,ap_processinfo a  ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ywlx", "YWLX");
			bs.setFieldUserID("CJRID");
			bs.setFieldUserID("SQR");

			bs.setFieldDateFormat("CREATETIME", "yyyy-MM-dd HH:mm:ss");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, "",
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<流程待办>成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		j.setMsg(domresult);
		return 	j;

	}
    @RequestMapping(params = "getUserTaskNew")
    @ResponseBody
	public void getUserTaskNew(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection conn = null;
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		String name = doc.selectSingleNode("//ZAGLDOC/NAME").getStringValue();
		String unitid = doc.selectSingleNode("//ZAGLDOC/UNITNAME")
				.getStringValue();
		String date = doc.selectSingleNode("//ZAGLDOC/DATE").getStringValue();
		String userid = doc.selectSingleNode("//ZAGLDOC/USERID")
				.getStringValue();
		String eventtypeid = doc.selectSingleNode("//ZAGLDOC/EVENTTYPEID")
				.getStringValue();
		String tasktypeid = doc.selectSingleNode("//ZAGLDOC/TASKTYPEID")
				.getStringValue();
		String currentPage = doc.selectSingleNode("//ZAGLDOC/CURRENTTASKNUM")
				.getStringValue();
		String perPagenum = doc.selectSingleNode("//ZAGLDOC/PAGENUM").getStringValue();
		String stageselected = doc.selectSingleNode("//ZAGLDOC/STAGESELECTED")
				.getStringValue();
	    //add by  zhangj 2009年9月3日 首页审批代办修改
		String sflag=request.getParameter("sflag");
		if(sflag==null){sflag="";}
		//add by  zhangj 2009年9月3日  end
		String pageSql = "";
		int pageNum = Integer.parseInt(currentPage);
		/*
		 * String sql = "select id,seq,memo,ywlx,sjbh," + "
		 * ywlx,spbh,rwlx,DBDWDM," + " dbrole,dbryid,cjrxm,cjdwdm," + "
		 * linkurl,cjsj,SLBH,AJBH,AJMC,SARQ,AjBLJD from ap_task_schedule t where
		 * RWZT='" + TaskVO.TASK_STATUS_VALID + "' ";
		 */
		String sql = "select * from ap_task_schedule t where RWZT='"
				+ TaskVO.TASK_STATUS_VALID + "' ";
		if (!Pub.empty(name))
			sql += " and memo like '%" + name + "%'";
		if (!Pub.empty(eventtypeid))
			sql += " and ywlx='" + eventtypeid + "'";
		if (!Pub.empty(tasktypeid))
		{
			if (tasktypeid.equals("3"))
			{
			  sql += " and rwlx='" + tasktypeid + "'";
			}
			else
			{
				sql += " and rwlx in(1,2,3,6)";//modified by hongf 2009-10-29 解决退回任务上一级查询不到问题 增加了rwlx为6的任务查询 1归档任务 2审批任务 3待办任务 6退回任务
			}
		}
		if (!Pub.empty(date)) {
			if (date.equals("now"))
				sql += " and cjsj>sysdate-1";
			else
				sql += " and cjsj>=to_date('" + date
						+ " 00:00:00','yyyy-mm-dd hh24:mi:ss')"
						+ " and cjsj<=to_date('" + date
						+ " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		if (!Pub.empty(unitid))
			sql += " and cjdwdm in (select row_id from fs_org_dept where bmjc like '%"
					+ unitid + "%')";
		String dbdw = null;
		OrgDept dept = user.getOrgDept();
		switch (dept.getDeptLevel()) {
		case 1:
			dbdw = dept.getDeptID().substring(0, 2) + "0000000000";
			break;
		case 2:
			dbdw = dept.getDeptID().substring(0, 4) + "00000000";
			break;
		case 3:
			dbdw = dept.getDeptID().substring(0, 6) + "000000";
			break;
		default:
		case 4:
			dbdw = dept.getDeptID();
			break;
		}
        sql += " and (DBDWDM='" + dbdw + "' or DBDWDM='"+dept.getDeptID()+"')";
        sql += " and (DBRYID='" + user.getAccount() + "' or DBRYID is null)";
        sql += " and (DBROLE in (" + (Pub.empty(user.getRoleListString())?"''":user.getRoleListString()) +
            ") or DBROLE is null)";
		sql += " order by cjsj desc";
		Statement stmt = null;
		try {
			conn = DBUtil.getConnection();
			stmt = conn.createStatement();
            //声明记录总数
            int sum=0;
			String sumsql="select count(*) from (select * from ("+sql+"))";
			ResultSet rs = stmt.executeQuery(sumsql);
			if(rs.next()){
				sum=rs.getInt(1);
			}
			//声明每页显示记录数
			int tasksnum=4;
			int num=Integer.parseInt(tasktypeid);
			//根据任务类型区别每页显示记录数
			if(perPagenum!=null && perPagenum.length()>0){
				tasksnum=Integer.parseInt(perPagenum);
			}
			//得到案件办理阶段，并组串
			//分页sql
			pageSql = "select * from (select s.*,rownum idnum from (" + sql
					+ " ) s  where  rownum <=" + String.valueOf(pageNum * tasksnum)
					+ " ) q where  idnum> "
					+ String.valueOf((pageNum - 1) * tasksnum);
		    rs = stmt.executeQuery(pageSql);
			BaseResultSet bs = new BaseResultSet(rs, stmt);
			bs.setFieldDateFormat("cjsj", "yyyy-MM-dd HH:mm:ss");
			bs.setFieldOrgDept("cjdwdm");
			bs.setFieldDic("rwlx", "RWLX");
			bs.setFieldDic("ywlx", "YWLX");
			//bs.setFieldDic("AjBLJD", "AjBLJD");
			bs.setFieldSjbh("SJBH");
			StringBuffer retbuf = new StringBuffer();
			//String path=request.getContextPath();
//			add by  zhangj 2009年9月3日 首页审批代办修改
			Document domresult = null;
			domresult=bs.getDocument();
	//		domresult.addElement("<RESPONSE><SUM>"+sum+"</SUM>");
			if(sflag.equals("1")){
				Element ROElement = domresult.getRootElement();
				Element sumElement = ROElement.addElement("SUM");
				sumElement.setText(Integer.toString(sum));
				 Pub.writeXmlDocument(response,domresult , "UTF-8");
			}
			else{
            retbuf = getTaskContent_sp(bs.getDocument(),sum,tasksnum);
			//response.getWriter().write(retbuf.toString());
			Pub.writeMessage(response,retbuf.toString(),"UTF-8");
			}
//			add by  zhangj 2009年9月3日  end
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null)
				stmt.close();
			stmt = null;
			if (conn != null)
				conn.close();
			conn = null;
		}
		//return null;
	}

	@RequestMapping(params = "getApproveTask")
    @ResponseBody
    public ActionForward getApproveTask( HttpServletRequest request,
                                        HttpServletResponse response)
        throws Exception
    {
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList cond = RequestUtil.getConditionList(doc);
        PageManager pm = RequestUtil.getPageManager(doc);
        TaskBO bo = new TaskBO(null);
        try
        {
            Document domresult = bo.getApproveTaskList(null, cond, pm);
            //
            JSONObject jo = new JSONObject();
	          jo.put("message", domresult);
            Pub.writeMessage(response, jo.toString(),"UTF-8");
            
            //
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            JSONObject jo = new JSONObject();
	          jo.put("message", "意外错误！"+ e.toString());
	          Pub.writeMessage(response, jo.toString(),"UTF-8");

        }
        return null;
    }

    public ActionForward getDisApproveTask(ActionMapping mapping,
                                           ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response)
        throws Exception
    {
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList cond = RequestUtil.getConditionList(doc);
        PageManager pm = RequestUtil.getPageManager(doc);
        TaskBO bo = new TaskBO(null);
        try
        {
            Document domresult = bo.getDisProveTaskList(null, cond, pm);
            Pub.writeXmlDocument(response, domresult);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            Pub.writeXmlMessage(response, "意外错误！！" + e.toString(), "ERRMESSAGE");
        }
        return null;
    }
    
    @RequestMapping(params = "AdminReturnFqr")
    @ResponseBody
    public void AdminReturnFqr(HttpServletRequest request,HttpServletResponse response)
        throws Exception
    {
        //Document doc = RequestUtil.getDocument(request);
        TaskBO bo = new TaskBO(null);
        JSONObject jo = new JSONObject();

        String sjbh = (String)request.getParameter("sjbh");
        jo.put("SJBH", sjbh==null?"":sjbh);
        String ywlx = (String)request.getParameter("ywlx");
        jo.put("YWLX", ywlx==null?"":ywlx);
        String  spbh= (String)request.getParameter("spbh");
        jo.put("SPBH", spbh==null?"":spbh);
        
        String id = (String)request.getParameter("id");
        String seq = (String)request.getParameter("seq");
        
        String ap_task_schedule_sql = "select id,seq from ap_task_schedule where sjbh='"+sjbh+"' and rwzt='01'";
		QuerySet qs_ = DBUtil.executeQuery(ap_task_schedule_sql, null);
		if(qs_ != null&&qs_.getRowCount()>0){
			id = qs_.getString(1, 1);
			seq = qs_.getString(1, 2);
		}
        jo.put("ID", id==null?"":id);
        jo.put("SEQ", seq==null?"":seq);
        
        String  processtype = (String)request.getParameter("processtype");
        jo.put("processtype", processtype==null?"":processtype);
        
        String  spjg = (String)request.getParameter("spjg");
        jo.put("SPJG", spjg==null?"":spjg);
        String  result = (String)request.getParameter("result");
        jo.put("RESULT", result==null?"":result);
        String  resultDscr = (String)request.getParameter("resultDscr");
        jo.put("resultDscr", resultDscr==null?"":resultDscr);
        
        String  fjbh = (String)request.getParameter("fjbh");
        jo.put("FJBH", fjbh==null?"":fjbh);
        String  sfth = (String)request.getParameter("sfth");
        jo.put("SFTH", sfth==null?"":sfth);
        String  spUser = (String)request.getParameter("spUser");
        String fqr_sql = "select CJRID from ap_processinfo t where PROCESSOID='"+spbh+"'";
		 qs_ = DBUtil.executeQuery(fqr_sql, null);
		if(qs_ != null&&qs_.getRowCount()>0){
			spUser = qs_.getString(1, 1);
		}
        
        jo.put("SPUSER", spUser==null?"":spUser);        
        String  tsdept = (String)request.getParameter("tsdept");
        jo.put("TSDEPT", tsdept==null?"":tsdept);    
        String  tsperson = (String)request.getParameter("tsperson");
        jo.put("TSPERSON", tsperson==null?"":tsperson);
        String  tsstep = (String)request.getParameter("tsstep");
        jo.put("TSSTEP", tsstep==null?"":tsstep);
        //特送下一级
        String isSpecialNextStep = (String)request.getParameter("isSpecialNextStep");
        jo.put("isSpecialNextStep", isSpecialNextStep==null?"":isSpecialNextStep);
        
        String  spFieldname = (String)request.getParameter("spFieldname");
        jo.put("spFieldname", spFieldname==null?"":spFieldname);
        
        String isRead = (String)request.getParameter("isRead");
        jo.put("isRead", isRead==null?"":isRead);
        
        String ccFlag = (String)request.getParameter("ccFlag");
        jo.put("ccFlag", ccFlag==null?"":ccFlag);
        String autodoApprove = (String)request.getParameter("autodoApprove");
        jo.put("autodoApprove", autodoApprove==null?"":autodoApprove);
        //
        String res = bo.doApprove(jo,(User) request.getSession().getAttribute(Globals.USER_KEY));
        JSONObject jo2 = new JSONObject();
       
        if (Pub.empty(res))
        {
        	jo2.put("status", "sucess");
        	jo2.put("Msg", "审批完成！");
            Pub.writeMessage(response,jo2.toString(),"UTF-8");
        }
        else
        {
        	jo2.put("status", "false");
        	jo2.put("Msg", "发生异常！\n" + res);
            Pub.writeMessage(response, jo2.toString(),"UTF-8");
        }
        //return null;
    }
    

    @RequestMapping(params = "doApprove")
    @ResponseBody
    public void doApprove(HttpServletRequest request,HttpServletResponse response,requestJson js)
        throws Exception
    {
        TaskBO bo = new TaskBO(null);
        JSONObject jo = new JSONObject();
    	String domFlag = request.getParameter("domFlag");
    	if(domFlag=="true" || "true".equals(domFlag)){
        	JSONObject rq = JSONObject.fromObject(js.getMsg());
        	RequestUtil.setJSONValueByJSON(jo,"ID",rq,"id");
        	RequestUtil.setJSONValueByJSON(jo,"SEQ",rq,"seq");
        	RequestUtil.setJSONValueByJSON(jo,"SJBH",rq,"sjbh");
        	RequestUtil.setJSONValueByJSON(jo,"YWLX",rq,"ywlx");
        	RequestUtil.setJSONValueByJSON(jo,"SPBH",rq,"spbh");
        	RequestUtil.setJSONValueByJSON(jo,"processtype",rq,"processtype");
        	RequestUtil.setJSONValueByJSON(jo,"SPJG",rq,"spjg");
        	RequestUtil.setJSONValueByJSON(jo,"RESULT",rq,"result");
        	RequestUtil.setJSONValueByJSON(jo,"resultDscr",rq,"resultDscr");
        	RequestUtil.setJSONValueByJSON(jo,"FJBH",rq,"fjbh");
        	RequestUtil.setJSONValueByJSON(jo,"SFTH",rq,"sfth");
        	RequestUtil.setJSONValueByJSON(jo,"SPUSER",rq,"spUser");
        	RequestUtil.setJSONValueByJSON(jo,"TSDEPT",rq,"tsdept");
        	RequestUtil.setJSONValueByJSON(jo,"TSPERSON",rq,"tsperson");
        	RequestUtil.setJSONValueByJSON(jo,"TSSTEP",rq,"tsstep");
        	RequestUtil.setJSONValueByJSON(jo,"isSpecialNextStep",rq,"isSpecialNextStep");
        	RequestUtil.setJSONValueByJSON(jo,"spFieldname",rq,"spFieldname");
        	RequestUtil.setJSONValueByJSON(jo,"isRead",rq,"isRead");
        	RequestUtil.setJSONValueByJSON(jo,"ccFlag",rq,"ccFlag");
        	RequestUtil.setJSONValueByJSON(jo,"autodoApprove",rq,"autodoApprove");
        	RequestUtil.setJSONValueByJSON(jo,"stepsequence",rq,"stepsequence");
//        	jo.put("ID", rq.getString("resultDscr")==null?"":rq.getString("resultDscr"));
//        	jo.put("SEQ", rq.getString("seq")==null?"":rq.getString("seq"));
//        	jo.put("SJBH", rq.getString("sjbh")==null?"":rq.getString("sjbh"));
//        	jo.put("YWLX", rq.getString("ywlx")==null?"":rq.getString("ywlx"));
//        	jo.put("SPBH", rq.getString("spbh")==null?"":rq.getString("spbh"));
//        	jo.put("processtype", rq.containsKey("processtype")&&rq.getString("processtype")==null?"":rq.getString("processtype"));
//        	jo.put("SPJG", rq.getString("spjg")==null?"":rq.getString("spjg"));
//        	jo.put("RESULT", rq.getString("result")==null?"":rq.getString("result"));
//        	jo.put("resultDscr", rq.getString("resultDscr")==null?"":rq.getString("resultDscr"));
//        	jo.put("FJBH", rq.getString("fjbh")==null?"":rq.getString("fjbh"));
//        	jo.put("SFTH", rq.getString("sfth")==null?"":rq.getString("sfth"));
//        	jo.put("SPUSER", rq.getString("spUser")==null?"":rq.getString("spUser"));
//        	jo.put("TSDEPT", rq.getString("tsdept")==null?"":rq.getString("tsdept"));
//        	jo.put("TSPERSON", rq.getString("tsperson")==null?"":rq.getString("tsperson"));
//        	jo.put("TSSTEP", rq.getString("tsstep")==null?"":rq.getString("tsstep"));
//        	jo.put("isSpecialNextStep", rq.getString("isSpecialNextStep")==null?"":rq.getString("isSpecialNextStep"));
//        	jo.put("spFieldname", rq.getString("spFieldname")==null?"":rq.getString("spFieldname"));
//        	jo.put("isRead", rq.getString("isRead")==null?"":rq.getString("isRead"));
//        	jo.put("ccFlag", rq.getString("ccFlag")==null?"":rq.getString("ccFlag"));
//        	jo.put("autodoApprove", rq.getString("autodoApprove")==null?"":rq.getString("autodoApprove"));
//        	jo.put("stepsequence", rq.getString("stepsequence")==null?"":rq.getString("stepsequence"));
    	}else{
	        //Document doc = RequestUtil.getDocument(request);
	        String id = (String)request.getParameter("id");
	        jo.put("ID", id==null?"":id);
	        String seq = (String)request.getParameter("seq");
	        jo.put("SEQ", seq==null?"":seq);
	        String sjbh = (String)request.getParameter("sjbh");
	        jo.put("SJBH", sjbh==null?"":sjbh);
	        String ywlx = (String)request.getParameter("ywlx");
	        jo.put("YWLX", ywlx==null?"":ywlx);
	        String  spbh= (String)request.getParameter("spbh");
	        jo.put("SPBH", spbh==null?"":spbh);
	        String  processtype = (String)request.getParameter("processtype");
	        jo.put("processtype", processtype==null?"":processtype);
	        
	        String  spjg = (String)request.getParameter("spjg");
	        jo.put("SPJG", spjg==null?"":spjg);
	        String  result = (String)request.getParameter("result");
	        jo.put("RESULT", result==null?"":result);
	        String  resultDscr = (String)request.getParameter("resultDscr");
	        jo.put("resultDscr", resultDscr==null?"":resultDscr);
	        String  fjbh = (String)request.getParameter("fjbh");
	        jo.put("FJBH", fjbh==null?"":fjbh);
	        String  sfth = (String)request.getParameter("sfth");
	        jo.put("SFTH", sfth==null?"":sfth);
	        String  spUser = (String)request.getParameter("spUser");
	        jo.put("SPUSER", spUser==null?"":spUser);        
	        String  tsdept = (String)request.getParameter("tsdept");
	        jo.put("TSDEPT", tsdept==null?"":tsdept);    
	        String  tsperson = (String)request.getParameter("tsperson");
	        jo.put("TSPERSON", tsperson==null?"":tsperson);
	        String  tsstep = (String)request.getParameter("tsstep");
	        jo.put("TSSTEP", tsstep==null?"":tsstep);
	        //特送下一级
	        String isSpecialNextStep = (String)request.getParameter("isSpecialNextStep");
	        jo.put("isSpecialNextStep", isSpecialNextStep==null?"":isSpecialNextStep);
	        
	        String  spFieldname = (String)request.getParameter("spFieldname");
	        jo.put("spFieldname", spFieldname==null?"":spFieldname);
	        
	        String isRead = (String)request.getParameter("isRead");
	        jo.put("isRead", isRead==null?"":isRead);
	        
	        String ccFlag = (String)request.getParameter("ccFlag");
	        jo.put("ccFlag", ccFlag==null?"":ccFlag);
	        String autodoApprove = (String)request.getParameter("autodoApprove");
	        jo.put("autodoApprove", autodoApprove==null?"":autodoApprove);
	        
	        // 步骤
	        String stepsequence = (String)request.getParameter("stepsequence");
	        jo.put("stepsequence", stepsequence==null?"":stepsequence);
    	}
        //
        String res = bo.doApprove(jo,(User) request.getSession().getAttribute(Globals.USER_KEY));
        JSONObject jo2 = new JSONObject();
       
        if (Pub.empty(res))
        {
        	jo2.put("success", "1");
        	jo2.put("message", "审批完成！");
            Pub.writeMessage(response,jo2.toString(),"UTF-8");
        }
        else
        {
        	jo2.put("success", "0");
        	jo2.put("message", "发生异常！\n" + res);
            Pub.writeMessage(response, jo2.toString(),"UTF-8");
        }
        //return null;
    }
    public ActionForward doRollbackTask(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception
    {
        TaskMgrBean bo = new TaskMgrBean();
        String sjbh = request.getParameter("sjbh");
        String ywlx = request.getParameter("ywlx");
        Connection conn = DBUtil.getConnection();
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        try
           {
               conn.setAutoCommit(false);
               bo.doAffirmTask(conn,sjbh,ywlx,user);
               EventManager.deleteEvent(conn,sjbh,user);
               EventManager.setEventState(conn,sjbh,"3");
               conn.commit();
           }
           catch (Exception e)
           {
               conn.rollback();
               Pub.writeXmlMessage(response, "发生异常！\n" + e.getMessage(), "ERRMESSAGE");
               e.printStackTrace(System.out);
           }
           finally
           {
               if (conn != null)
                   conn.close();
           }
            Pub.writeXmlMessage(response, "操作完成！", "MESSAGE");
        return null;
    }
    public ActionForward doBackTask(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception
    {
        TaskMgrBean bo = new TaskMgrBean();
        String sjbh = request.getParameter("sjbh");
        String ywlx = request.getParameter("ywlx");
        Connection conn = DBUtil.getConnection();
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        try
           {
               conn.setAutoCommit(false);
               EventManager.setEventState(conn,sjbh,Globals.EVENT_STATE_ROLLBACK);//审批退回
               conn.commit();
           }
           catch (Exception e)
           {
               conn.rollback();
               Pub.writeXmlMessage(response, "发生异常！\n" + e.getMessage(), "ERRMESSAGE");
               e.printStackTrace(System.out);
           }
           finally
           {
               if (conn != null)
                   conn.close();
           }
            Pub.writeXmlMessage(response, "操作完成！", "MESSAGE");
        return null;
    }
    public ActionForward doSpAgainAction(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception
    {
        TaskMgrBean bo = new TaskMgrBean();
        String sjbh = request.getParameter("sjbh");
        String ywlx = request.getParameter("ywlx");
        String id = request.getParameter("id");
        String seq = request.getParameter("seq");
        String spbh = request.getParameter("spbh");
        Connection conn = DBUtil.getConnection();
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        try
           {
               conn.setAutoCommit(false);
               Process process = ProcessMgr.getProcessByEvent(conn, sjbh); //通过事件获得当前流程实例
               Step step = process.getFirstStep();
               String dbdw = step.getDeptID();
               String dbr = step.getActor();
               String dbrole = step.getRole();
               String sql =
                   "select memo from ap_task_schedule "
                   + "where id='" + id + "' and seq='" + seq + "'";
               String[][] list = DBUtil.querySql(conn, sql);
//               TaskVO vo = bo.createNextApproveTask(conn, id, seq  , dbdw, dbrole,dbr, user);
//               vo.setSpr(user.getName());
               TaskVO vo = bo.createTask(TaskVO.TASK_TYPE_APPROVE, sjbh, ywlx, list[0][0]+"重新提起审批，请确认！",user,conn);
               vo.setRWLX(TaskVO.TASK_TYPE_APPROVE);
               vo.setRWZT(TaskVO.TASK_STATUS_VALID);
               if (!Pub.empty(dbdw))
               {
                   vo.setDBDWDM(dbdw);
               }
               if (!Pub.empty(dbrole))
               {
                   vo.setDbRole(dbrole);
               }
               //del by andy 20080505 再次提请审批需发给角色，而不指定原退回人
//               if (!Pub.empty(dbr))
//               {
//                   vo.setDBRYID(dbr);
//               }
               //end
               vo.setSPBH(spbh);
               bo.saveTask(conn, vo);
               process.rollbackTo(step.getStepSequence());
               process.close();
               bo.doTask(conn,id,seq,sjbh,ywlx,TaskVO.TASK_TYPE_ROLLBACK,"","",user);
               //-- add by guanchb -- 2008-03-28 -- start --
               //-- 再次提请审批时，需要有业务操作--
               SPAgainOP spop = new SPAgainOP();
               spop.ywOp(conn,sjbh,ywlx,id,seq,spbh);
               //-- add by guanchb -- 2008-03-28 -- end --
               // add by guanchb@2009-03-25 start:再次提请审批时，事件状态由“审批退回”变更为“审批中”
               String upSjzt = "UPDATE EVENT SET SJZT='1' WHERE SJBH='"+sjbh+"'";
               DBUtil.execSql(conn, upSjzt);
               // add by guanchb@2009-03-25 end.
               //added by andy 再次提请审批时，将审批状态置为1
               String update = " update pub_blob set spzt='1' where sjbh='"+sjbh+"' and ywlx='"+ywlx+"' and zfbs='0' and (spzt='7' or spzt='9') ";
               String update_glws = " update pub_blob set spzt='1' where fjbh in (select t1.fjbh from za_zfba_jcxx_ws_spckws t1,za_zfba_jcxx_ws_tycqbg t2 where t1.sjbh='"+sjbh+"' and t1.sjbh=t2.sjbh and t1.cqbgxh=t2.cqxh and t2.ywlx='"+ywlx+"' and t1.ws_template_id is not null and t1.zhux='0')";
               DBUtil.executeUpdate(conn, update, null);
               DBUtil.executeUpdate(conn, update_glws, null);
               conn.commit();
           }
           catch (Exception e)
           {
               conn.rollback();
               Pub.writeXmlMessage(response, "发生异常！\n" + e.getMessage(), "ERRMESSAGE");
               e.printStackTrace(System.out);
           }
           finally
           {
               if (conn != null)
                   conn.close();
           }
            Pub.writeXmlMessage(response, "操作完成！", "MESSAGE");
        return null;
    }
    public ActionForward doArchiveTask(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception
    {
        TaskMgrBean bo = new TaskMgrBean();
        String sjbh = request.getParameter("sjbh");
        String ywlx = request.getParameter("ywlx");
        Connection conn = DBUtil.getConnection();
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        try
           {
               conn.setAutoCommit(false);
               bo.doPrintTask(conn,sjbh,ywlx,user);
               EventManager.archiveEvent(conn,sjbh,user);
               EventManager.setEventState(conn,sjbh,"2");
               conn.commit();
           }
           catch (Exception e)
           {
               conn.rollback();
               Pub.writeXmlMessage(response, "发生异常！\n" + e.getMessage(), "ERRMESSAGE");
               e.printStackTrace(System.out);
           }
           finally
           {
               if (conn != null)
                   conn.close();
           }
            Pub.writeXmlMessage(response, "操作完成！", "MESSAGE");
        return null;
    }
    public ActionForward finishTaskSchedule(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception
    {
        String taskseq = request.getParameter("taskseq");
        Connection conn = DBUtil.getConnection();
        try
           {
               String sql = "update ap_task_schedule set rwzt='04' where seq ='"+taskseq+"'";
               conn.setAutoCommit(false);
               DBUtil.executeUpdate(conn,sql,null);
               conn.commit();
           }
           catch (Exception e)
           {
               conn.rollback();
               e.printStackTrace(System.out);
           }
           finally
           {
               if (conn != null)
                   conn.close();
           }
        return null;
    }

    public ActionForward getSpPerson(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response)
        throws Exception
    {
    PageManager page = new PageManager();
    String spRole = request.getParameter("spRole");
    String condition = "";
    if (Pub.empty(condition))
        condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
    condition +=" and ROLE_NAME='"+spRole+"'";
     //此处可以设置自定义的过滤条件
    // 如 condition+=" AND SHZT='0' "
    page.setFilter(condition);
    Document domresult = null;
    Connection conn = DBUtil.getConnection();
    try {
        conn.setAutoCommit(false);
        String sql = "select PERSON_ACCOUNT from org_role_psn_map ";
        BaseResultSet bs = DBUtil.query(conn, sql,page);
        bs.setFieldUserID("PERSON_ACCOUNT");
        domresult = bs.getDocument();
        Pub.writeXmlDocument(response, domresult, "UTF-8");
    }
    catch (Exception e) {
        conn.rollback();
        e.printStackTrace(System.out);
        Pub.writeXmlErrorMessage(response, e.toString());
    }
    finally {
        if (conn != null)
            conn.close();
    }
        return null;

    }

    //@author leo <oss@tom.com>处理流程信息查询,王立群加的，用于查询所有正在办理的流程后查看 办理到哪个环节了

	public ActionForward QueryList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String orderFilter = RequestUtil.getOrderFilter(doc);
        String condition = list == null ? "" : list.getConditionWhere();
        if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
        condition += orderFilter;
         //此处可以设置自定义的过滤条件
        // 如 condition+=" AND SHZT='0' "
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        Document domresult = null;
        Connection conn = DBUtil.getConnection();
        try {
            conn.setAutoCommit(false);
            String sql = "select PROCESSOID , PROCESSTYPEOID , CREATETIME , CLOSETIME , EVENTID , STATE , OPERATIONOID , MEMO , TASKOID , PROCESSEVENT , RESULT , RESULTDSCR from AP_PROCESSINFO";
            BaseResultSet bs = DBUtil.query(conn, sql,page);
             		//设置字典翻译定义

  		  		  		  							  		//设置时间的显示格式
			     				bs.setFieldDateFormat("CREATETIME","yyyy-MM-dd");
			     				bs.setFieldDateFormat("CLOSETIME","yyyy-MM-dd");
            domresult = bs.getDocument();
            Pub.writeXmlDocument(response, domresult, "UTF-8");
        }
        catch (Exception e) {
            conn.rollback();
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, e.toString());
        }
        finally {
            if (conn != null)
                conn.close();
        }
        return null;
	}
    //@author jinyb

    public ActionForward QuerySpList(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response)
        throws Exception
    {

        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String orderFilter = RequestUtil.getOrderFilter(doc);
        String condition = list == null ? "" : list.getConditionWhere();
        if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
        condition += orderFilter;
        //此处可以设置自定义的过滤条件
        condition += " AND t.processtypeoid = s.processtypeoid ";
        condition += " AND r.operationoid=s.OPERATIONOID ";

        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        Document domresult = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);
            String sql = "select t.processtypeoid,s.name,t.name as SNAME,t.rolename,s.createtime,s.state,r.eventtype from ap_processstep t ,ap_processtype s,ap_tasks r ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            //设置字典翻译定义
            bs.setFieldDateFormat("CREATETIME", "yyyy-MM-dd");
            bs.setFieldDic("EVENTTYPE","YWLX");
            domresult = bs.getDocument();
            Pub.writeXmlDocument(response, domresult, "UTF-8");
        }
        catch (Exception e)
        {
            conn.rollback();
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, e.toString());
        }
        finally
        {
            if (conn != null)
                conn.close();
        }
        return null;
    }
//---------------------------------------zl add 2007.11.13  增加待办锁定处理------------------------------------------------------




    public ActionForward getOperFlag(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response)
        throws Exception
    {
        String taskid=request.getParameter("taskID");
        String taskseq = request.getParameter("taskSeq");
        String operater=request.getParameter("user");
        Connection conn = DBUtil.getConnection();
        try
           {
               conn.setAutoCommit(false);
               String sql = "select nvl(OPFALG,''),nvl(OPUSER,'') from ap_task_schedule where id='"+taskid+"' and seq ='"+taskseq+"'";
               String[][] r = DBUtil.query(conn, sql);
               String rlt="";
               if ((r[0][0].equals("1") && r[0][1].equals(operater)) || (r[0][0].equals("") && r[0][1].equals(""))) //如果审批已被打开过未复位，并且是同一个人再次打开，或者该文书从未打开过允许进入
               {
                   String updateSql="update ap_task_schedule set OPFALG='1',OPUSER='"+operater+"' where id='"+taskid+"' and seq ='"+taskseq+"'";
                   DBUtil.executeUpdate(conn,updateSql,null);
                   rlt="0";
               }
               else
               {
                   String sqlp = "select name from org_person where account ='"+r[0][1]+"'";
                   String[][] rp = DBUtil.query(conn, sqlp);

                   rlt=rp[0][0]+"正在办理此业务";
               }
               conn.commit();
               Pub.writeMessage(response,rlt);
           }
           catch (Exception e)
           {
               conn.rollback();
               e.printStackTrace(System.out);
           }
           finally
           {
               if (conn != null)
                   conn.close();
           }
        return null;

    }
    public ActionForward setOperInit(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response)
        throws Exception
    {
        String taskid=request.getParameter("taskID");
        String taskseq = request.getParameter("taskSeq");
        Connection conn = DBUtil.getConnection();
        try
           {
               conn.setAutoCommit(false);

               String updateSql="update ap_task_schedule set OPFALG='',OPUSER='' where id='"+taskid+"' and seq ='"+taskseq+"'";
               DBUtil.executeUpdate(conn,updateSql,null);

               conn.commit();
               Pub.writeMessage(response,"操作成功");
           }
           catch (Exception e)
           {
               conn.rollback();
               e.printStackTrace(System.out);
           }
           finally
           {
               if (conn != null)
                   conn.close();
           }
        return null;

    }

	  /**
     * 得到审批内容
     * add by lhh20090207
     * @param doc
     * @return
     */
    public StringBuffer getTaskContent_sp(Document doc,int sum,int tasksnum){
    	String table_begin = "<table style=\"height:100%;width:100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"1\" bgcolor=\"CECECE\">";
    	String table_end = "</table>";
    	String tr_begin = "<tr width=\"100%\">";
    	String tr_end = "<div style=\"display:none;\" id=\"task_spsum\" >@"+sum+"$</div></tr>";
    	String radio = "<td width=\"4%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\"><input type=\"radio\" name=\"radiobutton\" value=\"radiobutton\"></td>";
    	StringBuffer strbuf = new StringBuffer("");
    	strbuf.append(table_begin);
    	//String colorStr = "ffffee";
    	List list = doc.selectNodes("/RESPONSE/RESULT/ROW");
    	if(list.size() > 0){
    	  for(int i=0;i<(list.size()<tasksnum?tasksnum:list.size());i++){
    		if(i<list.size()){
    		Element e = (Element)list.get(i);
    		String id = e.elementText("ID");
    		String seq = e.elementText("SEQ");
    		String slbh = e.elementText("SLBH");//受理编号
    		String ajbh = e.elementText("AJBH");//案件编号
    		String ajmc = e.elementText("MEMO");//案件名称
    		//String sarq = e.elementText("SARQ");//受案日期
    		String sjbh = e.elementText("SJBH");//事件编号
    		String ywlxcode = e.elementText("YWLX");//业务类型
    		String ywlxvalue = e.element("YWLX").attributeValue("sv");//业务类型
    		//String ajbljd = e.element("AJBLJD").attributeValue("sv");//.elementText("AJBLJD");//案件办理阶段
			//String bljd = e.elementText("AJBLJD");
    		String cjsj = e.element("CJSJ").attributeValue("sv");//e.elementText("CJSJ");//创建时间
    		String cbr = e.elementText("CJRXM");//创建人
    		String cjdwdm = e.elementText("CJDWDM");//
    		String cjdwmc = e.element("CJDWDM").attributeValue("sv");
    		String rwlx  = e.element("RWLX").attributeValue("sv");
    		String linkurl =e.elementText("LINKURL").trim();//页面链接
    		String spbh =e.elementText("SPBH").trim();//审批编号

    		strbuf.append(tr_begin);
    		//strbuf.append(radio);
    		strbuf.append("<td width=\"4%\" align=\"center\" bgcolor=\"#"+((i%2==0)?"FFFFEE":"EFFFFF")+"\" class=\"FontSize9Black\">"+(i+1)+"</td>");
    		strbuf.append("<td width=\"13%\" align=\"center\" bgcolor=\"#"+((i%2==0)?"FFFFEE":"EFFFFF")+"\" class=\"FontSize9Black\">"+ywlxvalue+"</td>");
    		strbuf.append("<td width=\"46%\" style=\"text-align:left\" bgcolor=\"#"+((i%2==0)?"FFFFEE":"EFFFFF")+"\" class=\"FontSize9Black\"><a href=\"../../../"+linkurl+"?taskid="+id+"&taskseq="+seq+"&slbh="+slbh+"&sjbh="+sjbh+"&ywlx="+ywlxcode+"&spbh="+spbh+"\">"+ajmc+"</a></td>");
    	    strbuf.append("<td width=\"9%\" align=\"center\" bgcolor=\"#"+((i%2==0)?"FFFFEE":"EFFFFF")+"\" class=\"FontSize9Black\">"+cjsj+"</td>");
    	    strbuf.append("<td width=\"7%\" align=\"center\" bgcolor=\"#"+((i%2==0)?"FFFFEE":"EFFFFF")+"\" class=\"FontSize9Black\">"+cbr+"</td>");
    	    strbuf.append("<td width=\"14%\" align=\"center\" bgcolor=\"#"+((i%2==0)?"FFFFEE":"EFFFFF")+"\" class=\"FontSize9Black\">"+cjdwmc+"</td>");
      	    strbuf.append("<td width=\"7%\" align=\"center\" bgcolor=\"#"+((i%2==0)?"FFFFEE":"EFFFFF")+"\" class=\"FontSize9Black\">"+rwlx+"</td>");
    	    strbuf.append(tr_end);
    	  }else{
    		strbuf.append(tr_begin);
    		//strbuf.append(radio);
    		strbuf.append("<td width=\"4%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\"></td>");
    		strbuf.append("<td width=\"13%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    		strbuf.append("<td width=\"46%\" align=\"left\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    	    strbuf.append("<td width=\"9%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    	    strbuf.append("<td width=\"7%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    	    strbuf.append("<td width=\"14%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    	    strbuf.append("<td width=\"7%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    	    strbuf.append(tr_end);
    	  }
    	}
    	}else{
      		 strbuf.append(tr_begin);
  		     strbuf.append("<td width=\"4%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
		     strbuf.append("<td width=\"13%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
		     strbuf.append("<td width=\"46%\" align=\"left\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\"><span style=\"font-size:9pt;color:red;font-weight:bold;\">没有待办事项！</span></td>");
	         strbuf.append("<td width=\"9%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
	         strbuf.append("<td width=\"7%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
	         strbuf.append("<td width=\"14%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
	         strbuf.append("<td width=\"7%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    	     strbuf.append(tr_end);
      	    for(int i=0;i<tasksnum-1;i++){
      		  strbuf.append(tr_begin);
    		//strbuf.append(radio);
    		  strbuf.append("<td width=\"4%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\"></td>");
    		  strbuf.append("<td width=\"13%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    		  strbuf.append("<td width=\"46%\" align=\"left\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    	      strbuf.append("<td width=\"9%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    	      strbuf.append("<td width=\"7%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    	      strbuf.append("<td width=\"14%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    	      strbuf.append("<td width=\"7%\" align=\"center\" bgcolor=\"#FFFFFF\" class=\"FontSize9Black\">&nbsp;</td>");
    	      strbuf.append(tr_end);
      	    }
    	}
    	strbuf.append(table_end);
    	return strbuf;
    }
    //author by liujs
    //发文，退回的流程再次呈请审批操作，需要的查询语句
	@RequestMapping(params = "getFwLc")
	@ResponseBody
	public requestJson getFwLc(HttpServletRequest request,requestJson json)
			throws Exception
			{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String sjbh=request.getParameter("sjbh");
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "";
			sql = "select * from ap_task_schedule t where sjbh='"+sjbh+"' and RWZT='01'";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ywlx", "YWLX");
			bs.setFieldUserID("CJRID");
			bs.setFieldDateFormat("CJSJ", "yyyy-MM-dd HH:mm:ss");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, "",
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
					+ "查询<流程待办>成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		j.setMsg(domresult);
		return 	j;
		
			}

    
//----------------------------------------------------------------------------------------------------

	/**
	 * 获取当前数据是否进行“呈请审批”操作标志
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getCqspFlag")
	@ResponseBody
	public requestJson getCqspFlag(HttpServletRequest request,requestJson json) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		Connection conn = null;
		String sjbh = request.getParameter("sjbh");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sql = "select count(SJBH) from ap_task_schedule where SJBH='"+sjbh+"'";
			String count = DBUtil.query(conn, sql)[0][0];
			if(count=="0"||"0".equals(count)){
				domresult = "false";
			}else{
				domresult = "true";
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		j.setMsg(domresult);
		return j;
	}

}