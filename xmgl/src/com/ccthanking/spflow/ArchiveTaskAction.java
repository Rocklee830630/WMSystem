package com.ccthanking.spflow;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.aplink.Process;
import com.ccthanking.framework.coreapp.aplink.ProcessMgr;
import com.ccthanking.framework.coreapp.aplink.SPAgainOP;
import com.ccthanking.framework.coreapp.aplink.Step;
import com.ccthanking.framework.coreapp.aplink.TaskBO;
import com.ccthanking.framework.coreapp.aplink.TaskMgrBean;
import com.ccthanking.framework.coreapp.aplink.TaskVO;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.WorkdayUtils;
import com.ccthanking.framework.wsdy.PubWS;


/**
 *
 * @author
 * @des 审批归档通用类,误删
 */
@Controller
@RequestMapping("/TaskBackAction")
public class ArchiveTaskAction
{

	public ActionForward customStampAction(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
            throws Exception
        {
            String sjbh = request.getParameter("sjbh");
            String ywlx = request.getParameter("ywlx");
            String templateid = request.getParameter("templateid");
            String org_azt = request.getParameter("org_azt");
            String cor_azt = request.getParameter("cor_azt");
            String org_type = request.getParameter("org_type");
            String yzfieldname = request.getParameter("yzfieldname");
            User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
            OrgDept dept = user.getOrgDept();
            
            String deptlevel = "320";
            if (org_type != null){
            	if(org_type.equals("3"))//目前jsp中传递的参数都是3  modified by zhangj@2009-07-22
            		org_type="320";
                deptlevel = org_type;
              }
            Connection conn = DBUtil.getConnection();
            String errMsg = "";
            try
            {
                if (sjbh == null)
                {
                    throw new Exception("事件编号:null" + "\r\n");
                }
                if (ywlx == null)
                {
                    throw new Exception("业务类型:null" + "\r\n");
                }
            }
            catch (Exception ce)
            {
                errMsg += ce.getMessage();
            }
            try
            {
                conn.setAutoCommit(false);
                // add by guanchb 2008-04-14 start
                // 查询文书是否为审批文书
                String selIsSp = "select IS_SP_FLAG from ws_template where WS_TEMPLATE_ID='"+templateid+"'";
                String[][] resIsSp = DBUtil.querySql(conn,selIsSp);
                String isSp = resIsSp[0][0];
                // add by guanchb 2008-04-14 end;
                PubWS ws = new PubWS();
                //处理印章级别
               
                String aztdept = null;
                aztdept = dept.getDeptID();//正常盖本单位章 addby zhangj 090723
          
                if (deptlevel .equals("320"))//支队盖分局章或者本支队章 addby zhangj 090723
                {
 
                   if(dept.getDeptType().equals("330")){ //民警盖大队章，或者分局章 addby zhangj 090723
                	   OrgDept parent = dept;
                	   parent = parent.getParent();
                	   aztdept= parent.getDeptID();
                   }
                    //modified by jiawh 20080917 修改内容说明：修改刑事的文书中部分都盖分局章的问题 start
                    if (ywlx.startsWith("0404"))
                    {
                        //除《(刑事)移送案件接收回执》（188）、《(刑事)案件移送通知书》（153）《五级审批呈请报告》（390）外都加盖分局印章
                          //如果是分局负责人审批的也加盖分局印章
                      // modified by guanchb@2009-04-22 start:文书379为五级呈请报告，盖章方式同文书390.
                      // modified by zhangt@2009-05-18 start:分局内移送通知书盖支队章、移送外分局通知书加盖分局印章
                      if (!(templateid.equals("188") || templateid.equals("390") || templateid.equals("391") || templateid.equals("171") || templateid.equals("379") || (templateid.equals("153") && ywlx.equals("040402"))))
                      // modified by guanchb@2009-05-09 end: 分局内移送通知书盖支队章、移送外分局通知书加盖分局印章
                      // modified by zhangt@2009-05-18 end
                      {
                       aztdept = dept.getDeptID().substring(0, 6) + "000000";
                      }

                    }
                    //modified by jiawh 20080917 修改内容说明：修改刑事的文书中部分都盖分局章的问题 end
                    else
                    {
                    // add by guanchb 2008-04-14 start
                    // 如果此部门是和分局同一部门级别，并且
                    // 如果该文书不是审批文书，那么取浦东分局机构代码，否则取该部门机构代码
                    	// modified by guanchb@2009-03-24 start
                    	// 解决问题：刑事民警办理行政案件时支队长审批通过应该加盖分局印章，或者非审批文书加盖分局印章                  	
                    	if("35".equals(dept.getCus_dept_level())||"35".equals(dept.getParent().getCus_dept_level())){
                    		aztdept = dept.getDeptID().substring(0, 6) + "000000";
                    	}
                        // modified by guanchb@2009-03-24 end                    	
                    if (aztdept.equals(dept.getDeptID()))
                    {
                        if(isSp.equals("0")){
                            aztdept = dept.getDeptID().substring(0, 6) + "000000";
                        }
                    }
                    // add by guanchb 2008-04-14 end;
                    }
                }
           //     if (deptlevel .equals("2") || deptlevel .equals("1"))
           //     {
           //         throw new Exception("市局,分局用户不能在这里盖章!");
           //     }
                // modified by guanchb@2009-05-20 start
                // 审批人有多个审批角色，只要有符合当前审批角色的就可盖章。
                // 解决同一个审批人被赋予两个审批角色不能加盖电子公章的问题。
                String role = user.getRoleListString();
                //String role = user.getRoles()[0].getName();
                // modified by guanchb@2009-05-20 end.
                String level = deptlevel;
                QuerySet qs = DBUtil.executeQuery(
                    "select * from WS_TEMPLATE_SQL where WS_TEMPLATE_ID = '" +
                    templateid + "' and WSWH_FLAG ='6' and APPROVELEVEL='"+deptlevel+"'", null, conn);


                if (qs.getRowCount() > 0)
                {
                    for (int i=1;i<=qs.getRowCount();i++)
                    {
                        if (role.indexOf(qs.getString(i, "APPROVEROLE"))>=0)
                        {
                            role = qs.getString(i, "APPROVEROLE");
                            level = qs.getString(i, "APPROVELEVEL");
                          
                            break;
                        }
                    }
                }
                // 注释 by guanchb@2009-05-20 start
                // 已通过WS_TEMPLATE_SQL表的配置解决了同一个处罚决定书即加盖分局印章又加盖当前部门公章的问题
                // 因此，不需要在代码中控制了，将此段代码注释！
                /*
                else{
                    // add by guanchb 2008-03-27 start
                    // 在WS_TEMPLATE_SQL表中无法获得角色和部门级别时，角色取角色列表中的第一个角色
                    // 部门等级取前台传过来的部门等级,为的是解决同一个处罚决定书即盖分局公章又盖
                    // 派出所公章
                    role = role.replaceAll("\'","");
                    String[] roles = role.split(",");
                    role = roles[0];
                    // add by guanchb 2008-03-27 end
                }
                */
                // 注释 by guanchb@2009-05-20 end
                if ("1".equals(org_azt))
                {
                    //盖机构章
                    java.util.HashMap newAzt = new java.util.HashMap();
                    newAzt.put("WS_TEMPLATE_ID", templateid);
                    newAzt.put("SJBH", sjbh);
                    newAzt.put("YWLX", ywlx);
                    newAzt.put("APPROVEROLE", role);
                    newAzt.put("APPROVELEVEL", level);
                    newAzt.put("YZLX", "2");
                    ws.copyOrgPrintAzt(conn, aztdept, "2", newAzt);
                }
                if ("1".equals(cor_azt))
                {
                    //盖法人章
                    java.util.HashMap newAztFr = new java.util.HashMap();
                    newAztFr.put("WS_TEMPLATE_ID", templateid);
                    newAztFr.put("SJBH", sjbh);
                    newAztFr.put("YWLX", ywlx);
                    newAztFr.put("APPROVEROLE", role);
                    newAztFr.put("APPROVELEVEL", level);
                    newAztFr.put("YZLX", "1");
                    ws.copyOrgPrintAzt(conn, aztdept, "1", newAztFr);
                }
                conn.commit();

            }
            catch (Exception e)
            {
                conn.rollback();
                errMsg += e.getMessage();
                System.out.println("加盖印章错误:" + errMsg);
                Pub.writeXmlErrorMessage(response, "意外错误！！" + errMsg);
            }
            finally
            {
                if (conn != null)
                    conn.close();
            }
            return null;
        }

        /**
         * 处理审批结束后 自动加盖印章action
         * @param mapping
         * @param form
         * @param request
         * @param response
         * @return
         * @throws java.lang.Exception
         */
        public ActionForward stampAction(ActionMapping mapping,
                                         ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
            throws Exception
        {
            String sjbh = request.getParameter("sjbh");
            String ywlx = request.getParameter("ywlx");
            String templateid = request.getParameter("templateid");
            String org_azt = request.getParameter("org_azt");
            String cor_azt = request.getParameter("cor_azt");
            User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
            Connection conn = DBUtil.getConnection();
            String errMsg = "";
            try
            {
                if (sjbh == null)
                {
                    throw new Exception("事件编号:null" + "\r\n");
                }
                if (ywlx == null)
                {
                    throw new Exception("业务类型:null" + "\r\n");
                }
            }
            catch (Exception ce)
            {
                errMsg += ce.getMessage();
            }
            try
            {
                conn.setAutoCommit(false);

                PubWS ws = new PubWS();
                Process process = ProcessMgr.getProcessByEvent(conn, sjbh);
                Step step = process.getLastStep();
                if ("1".equals(org_azt))
                {
                    //盖机构章
                    java.util.HashMap newAzt = new java.util.HashMap();
                    newAzt.put("WS_TEMPLATE_ID", templateid);
                    newAzt.put("SJBH", sjbh);
                    newAzt.put("YWLX", ywlx);
                    newAzt.put("APPROVEROLE", step.getRole());
                    newAzt.put("APPROVELEVEL", String.valueOf(step.getDeptLevel()));
                    newAzt.put("YZLX", "2");
                    ws.copyOrgPrintAzt(conn, step.getDeptID(), "2", newAzt);
                }
                if ("1".equals(cor_azt))
                {
                    //盖法人章
                    java.util.HashMap newAztFr = new java.util.HashMap();
                    newAztFr.put("WS_TEMPLATE_ID", templateid);
                    newAztFr.put("SJBH", sjbh);
                    newAztFr.put("YWLX", ywlx);
                    newAztFr.put("APPROVEROLE", step.getRole());
                    newAztFr.put("APPROVELEVEL", String.valueOf(step.getDeptLevel()));
                    newAztFr.put("YZLX", "1");
                    ws.copyOrgPrintAzt(conn, step.getDeptID(), "1", newAztFr);
                }
                conn.commit();

            }
            catch (Exception e)
            {
                conn.rollback();
                errMsg += e.getMessage();
                System.out.println("加盖印章错误:" + errMsg);
                Pub.writeXmlErrorMessage(response, "意外错误！！" + errMsg);
            }
            finally
            {
                if (conn != null)
                    conn.close();
            }
            return null;
        }

    @RequestMapping(params = "customOperationAction", method = RequestMethod.POST)
    public void customOperationAction(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String eventid = request.getParameter("eventid");
		String ywlx = request.getParameter("ywlx");
		String spbh = request.getParameter("spbh");
		String sjzt = request.getParameter("sjzt");
		String processtype = request.getParameter("processtype"); // 是否是特送（收文和工作联系单）
		String opFlag  = request.getParameter("opFlag"); //是否有自定义业务操作
		if(sjzt == null || "".equals(sjzt))
			sjzt = "2";
		if(opFlag == null || "".equals(opFlag))
			opFlag = "0";

		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String errMsg = "";
		try
		{
			if (eventid == null)
			{
				throw new CustomException("事件编号:null" + "\r\n");
			}
			if (ywlx == null)
			{
				throw new CustomException("业务类型:null" + "\r\n");
			}
		} catch (CustomException ce)
		{
			errMsg += ce.getMessage();
		}
		try
		{
			conn.setAutoCommit(false);
			ArchiveTaskBO atBO = new ArchiveTaskBO();
			switch(Integer.parseInt(sjzt))
			{
			  case 2://审批同意
				  // add by guanchb@2009-02-10 start
				  // 解决问题：如果待归档的任务是由管理员恢复的，那么不执行归档方法。
				  String sql = "SELECT ISHF FROM AP_TASK_SCHEDULE WHERE SJBH='"+eventid+"' AND YWLX='"+ywlx+"' ORDER BY CJSJ DESC ";
				  String[][] res = DBUtil.query(conn, sql);
				  if(res[0][0].equals("0")){
					  atBO.operationTaskBack(eventid, ywlx, user, conn, 2);
				  }
				  // add by guanchb@2009-02-10 end
				  //更新pub_blob的spzt为2
				  String update = " update pub_blob set spzt='2' where sjbh='"+eventid+"' and ywlx='"+ywlx+"' and spzt='1' and zfbs='0' ";
				  DBUtil.executeUpdate(conn, update, null);
		//		  ArchiveTaskBO.publicToDo(eventid, ywlx, user, conn);
				  
				  String jsrYj = request.getParameter("jsrYj") == null ? "" : request.getParameter("jsrYj");
				  if(!"4".equals(processtype)) {
					  ArchiveTaskBO.publicToDo(eventid, ywlx, user, conn, jsrYj);
				  } else {
					  // processtype=4 表示特送流程，这里处理事件编号的状态
					  EventVO event = EventManager.getEventByID(conn, eventid);
					  event.setGdsj(Pub.getCurrentDate());
					  EventManager.archiveEvent(conn, event, user);
					  EventManager.setEventState(conn, eventid, "2");
				  }
				  
				  // 此case是特送结束流程处理，372行【ArchiveTaskBO.publicToDo(eventid, ywlx, user, conn);】是更新AP_TASK_SCHEDULE表的相应状态
				  // 还需要把ap_processinfo表的状态更改一下
				  String updateApProcessInfoSql = "update AP_PROCESSINFO t set t.closetime=sysdate,t.result='1' " 
						  + "where t.eventid='" + eventid + "'";
				  DBUtil.execSql(conn, updateApProcessInfoSql);
				  break;
			  case 3://审批不同意
				  if("1".equals(opFlag))
				   atBO.operationTaskBack(eventid, ywlx, user, conn, 3);
				  TaskMgrBean bo = new TaskMgrBean();
				  EventVO event = EventManager.getEventByID(conn, eventid);
				  bo.doAffirmTask(conn, eventid, ywlx, user);
				  event.setGdsj(Pub.getCurrentDate());
				  EventManager.archiveEvent(conn, event, user);
				  EventManager.setEventState(conn, eventid, "3");
				  break;
			}
			conn.commit();
			JSONObject jo = new JSONObject();
			jo.put("Msg", "审批归档成功！");
			jo.put("status", "sucess");
			Pub.writeMessage(response, jo.toString());

		} catch (Exception e)
		{
			conn.rollback();
			errMsg += e.getMessage();
			JSONObject jo = new JSONObject();
			jo.put("Msg", "审批归档错误:" + errMsg);
			jo.put("status", "error");
			Pub.writeMessage(response, jo.toString());
		}finally
		{
			if(conn != null)
				conn.close();
		}
		//return null;
	}

    /**
     * 收文特送结束流程是，归档需要在AP_PROCESS_WS表中提交意见
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(params = "insertApProcessWsWhenEnd", method = RequestMethod.POST)
	@ResponseBody
    public void insertApProcessWsWhenEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		String jsrYj = request.getParameter("jsrYj");
		String sjbh = request.getParameter("sjbh");
		String ywlx = request.getParameter("ywlx");
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE," 
					+ "DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
          		  	" values('"+new RandomGUID().toString()+"','"+PubWS.FIELDEXTEN_MIND+"','381','','"
					+ jsrYj+"','','YJRYJ','','','1','','"
          		  	+ sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"') ";
            DBUtil.execSql(conn, insertSql);
            conn.commit();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
    }

    @RequestMapping(params = "doRollbackTask", method = RequestMethod.POST)
	public void doRollbackTask(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TaskMgrBean bo = new TaskMgrBean();
		String sjbh = request.getParameter("sjbh");
		String ywlx = request.getParameter("ywlx");
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn.setAutoCommit(false);
			bo.doRollBackTask(conn, sjbh, ywlx, user);
			EventManager.deleteEvent(conn, sjbh, user);
			EventManager.setEventState(conn, sjbh, "3");
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			JSONObject jo = new JSONObject();
			jo.put("Msg", "发生异常！\n" + e.getMessage());
			jo.put("status", "sucess");
			Pub.writeMessage(response, jo.toString());
			//Pub.writeMessage(response,"发生异常！\n" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (conn != null)
				conn.close();
		}
		JSONObject jo = new JSONObject();
		jo.put("Msg", "审批归档成功！");
		jo.put("status", "sucess");
		Pub.writeMessage(response, jo.toString());
		//Pub.writeXmlMessage(response, "操作完成！", "MESSAGE");
		//return null;
	}
   @RequestMapping(params = "doSpAgainAction", method = RequestMethod.POST)
    public void doSpAgainAction(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	
        TaskMgrBean bo = new TaskMgrBean();
        String sjbh = request.getParameter("sjbh");
        String ywlx = request.getParameter("ywlx");
        String id = request.getParameter("id");
        String seq = request.getParameter("seq");
        String spbh = request.getParameter("spbh");
        String mind = request.getParameter("mind");
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
             //add by cbl start 重新生成设置ap_task_shedule表的memo信息
          	   String tempStrMemo = "请办理。";
          	   String name = Pub.getDictValueByCode("YWLX", ywlx);
               String adscrMemo = "";
//               String memo = list[0][0];
//               if (!Pub.empty(memo))
//               {
//                   if (memo.lastIndexOf("，") > 0 && memo.lastIndexOf("，") > memo.lastIndexOf("]"))
//                   {
//                  	 adscrMemo = memo.substring(0, memo.lastIndexOf("，")) + "，" + tempStrMemo;
//                   }
//                   else
//                   {
//                  	 adscrMemo = memo.substring(0, memo.length() - 2)+ "，" + tempStrMemo;
//                   }
//               }
               adscrMemo = Pub.getProdesc(conn, ywlx, sjbh);
               if(Pub.empty(adscrMemo)){
            	   adscrMemo = name;
               }
               adscrMemo = "【" + adscrMemo + "】的业务，请办理。";
             
             //add by cbl end 重新生成设置ap_task_shedule表的memo信息
               
               TaskVO vo = bo.createTask(TaskVO.TASK_TYPE_APPROVE, sjbh, ywlx, adscrMemo,user,conn);
               vo.setRWLX(TaskVO.TASK_TYPE_APPROVE);
               vo.setRWZT(TaskVO.TASK_STATUS_VALID);
               vo.setDBRYID(dbr);

               String SHEDULE_DAYS = step.getShedule_days();//获取超期天数
            	if(!Pub.empty(SHEDULE_DAYS)){
            		//计算超期日期，在当前工作日期后增加特定天数,取时间为当前日期
       			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       			WorkdayUtils workdayUtils = new WorkdayUtils();
       			Date date = workdayUtils.getWorkday(new Date(), Integer.parseInt(SHEDULE_DAYS));//获取计算后的时间
       			//转换为字符串更新到ap_processdetail表中
       			String updatesql = "update AP_PROCESSDETAIL a  set a.SHEDULE_TIME = to_date('"+format.format(date)+" 23:59:59','yyyy-MM-dd hh24:mi:ss') where PROCESSOID = '"+step.getProcessOID()+"' and STEPSEQUENCE='"+step.getStepSequence()+"'";
       			DBUtil.exec(conn, updatesql);
       			//设置待办表规定完成时间
       			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       			 vo.setShedule_time(sdf.parse(format.format(date)+" 23:59:59"));
            	}
               vo.setSPBH(spbh);
               vo.SetStepsequence(String.valueOf(step.getStepSequence()));
               //bo.saveTask(conn, vo);
               process.rollbackTo(step.getStepSequence());
              /* if(step!=null && "1".equals(step.getIsMS()))
               {*/
               	String actors = step.getActor();
               	if(actors!=null &&actors.length() > 0)
               	{
               		String s[] = actors.split(",");
               		for(int k=0;k<s.length;k++)
               		{
               			
               			vo.setDBRYID(s[k]);
               			//vo.setDBDWDM("");
               			//vo.setDbRole("");
               			vo.setFbs("1");
               			if(k==0)
               			{
               				bo.saveTask(conn, vo);
               			}
               			else
               			{
               				bo.copyTask(conn,vo);
               			}
               		}
               	}
               	
               /*}*/
               if(step!=null && "1".equals(step.getIsCC()))
               {
               	 actors = step.getCcActor();
               	if(actors!=null &&actors.length() > 0)
               	{
               		String s[] = actors.split(",");
               		for(int k=0;k<s.length;k++)
               		{
               			//TaskVO vo1 = vo;
               			vo.setDBRYID(s[k]);
               			//vo.setDBDWDM(step.getDeptID());
               			//vo.setDbRole(step.getRole());
               			vo.setFbs("0");
               			if(k==0 && (null==step.getActor()||"".equals(step.getActor())))
               			{
               				bo.saveTask(conn, vo);
               			}
               			else
               			{
               				bo.copyTask(conn,vo);
               			}
               		}
               	}
               }
                //vo.setFbs("1");
                if(step!=null&&"0".equals(step.getIsCC())&&"0".equals(step.getIsMS()))
                {
                	vo.setDBDWDM("");
                	vo.setFbs("1");
                	vo.setDbRole("");
                	bo.saveTask(conn, vo);
                }
               	
               
               process.close();
//               bo.doTask(conn,id,seq,sjbh,ywlx,TaskVO.TASK_TYPE_ROLLBACK,"4","再次提请",user);
               bo.doTask(conn,id,seq,sjbh,ywlx,TaskVO.TASK_TYPE_ROLLBACK,"4",mind,user);
               //-- add by guanchb -- 2008-03-28 -- start --
               //-- 再次提请审批时，需要有业务操作--
               SPAgainOP spop = new SPAgainOP();
               spop.ywOp(conn,sjbh,ywlx,id,seq,spbh);
               //-- add by guanchb -- 2008-03-28 -- end --
               // add by guanchb@2009-03-25 start:再次提请审批时，事件状态由“审批退回”变更为“审批中”
               String upSjzt = "UPDATE FS_EVENT SET SJZT='1' WHERE SJBH='"+sjbh+"'";
               DBUtil.execSql(conn, upSjzt);
               // add by guanchb@2009-03-25 end.
               //added by andy 再次提请审批时，将审批状态置为1
          
               // 发起人再次发起流程时，把意见添加到文书中。 add by xhb 2014-03-07
               TaskBO.insertIntoWs(ywlx, sjbh, mind, user, conn);
               conn.commit();
    		JSONObject jo = new JSONObject();
    		jo.put("Msg", "再次提起审批成功！");
    		jo.put("status", "sucess");
    		Pub.writeMessage(response, jo.toString());

        } catch (Exception e)
        {
            conn.rollback();
            String errMsg = e.getMessage();
    		JSONObject jo = new JSONObject();
    		jo.put("Msg", "再次提请审批错误:"+errMsg);
    		jo.put("status", "sucess");
    		Pub.writeMessage(response, jo.toString());
        }finally
        {
            if(conn != null)
                conn.close();
        }
       // return null;
    }
    public ActionForward IsSignedAction(ActionMapping mapping,
                                                   ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response)
                throws Exception
           {
                String sjbh = request.getParameter("sjbh");
                String ywlx = request.getParameter("ywlx");
                String templateid = request.getParameter("templateid");

                User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
                String level=user.getLevelName();
                Connection conn = DBUtil.getConnection();
                String rlt = "";
                try
                {
                    QuerySet qs = DBUtil.executeQuery(
                    "select * from ws_electron_print where WS_TEMPLATE_ID = '" +
                    templateid + "' and APPROVELEVEL='"+level+"' and sjbh='"+sjbh+"' and ywlx='"+ywlx+"' and yzlx='1'", null, conn);
                    if (qs.getRowCount() > 0)
                        rlt="1";
                    else
                        rlt="0";

                    Pub.writeMessage(response,rlt);
                }
                catch (Exception e)
                {
                    Pub.writeXmlErrorMessage(response, "意外错误！！");
                }
                finally
                {
                    if (conn != null)
                        conn.close();
                }
                return null;
            }

    @RequestMapping(params = "deleteProcess", method = RequestMethod.POST)
    public void deleteProcess(HttpServletRequest request,HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    	String spbh = Pub.get(request, "spbh");
    	String sjbh = Pub.get(request, "sjbh");
    	String ywlx = Pub.get(request, "ywlx");
    	String glyDelete = request.getParameter("isgly");
    	if(Pub.empty(glyDelete)){
    		glyDelete = "";
    	}
    	
    	String ap_processdetail = "delete from ap_processdetail where PROCESSOID='"+spbh+"'";
    	String ap_processinfo  = "delete from ap_processinfo  where PROCESSOID='"+spbh+"'";
    	String ap_process_ws   = "delete from ap_process_ws   where sjbh='"+sjbh+"'";
    	String pub_blob   = "delete from pub_blob where sjbh='"+sjbh+"'";
    	String ap_task_schedule    = "delete from ap_task_schedule  where SPBH='"+spbh+"'";
    	String ap_processconfig = "delete from ap_processconfig  where sjbh='"+sjbh+"'";
        String fs_event  = "update fs_event set sjzt = '0' where sjbh = '"+sjbh+"'";
    	
    	Connection conn = null;
    	try {
    		conn = DBUtil.getConnection();
        	conn.setAutoCommit(false);
        	boolean delete =true;
        	
        	if(!"1".equals(glyDelete)){
        		String[][] s = DBUtil.query(conn,"select processoid,CLOSETIME from ap_processdetail  where PROCESSOID='"+spbh+"' and STEPSEQUENCE=1 ");
        		if(s!=null&&s.length>0){
        			if(s[0][1]!=null&&!"".equals(s[0][1])){
        				delete = false;
        			}
        		}        		
        	}
        	
        	if(delete==false){
        		JSONObject jo = new JSONObject();
    			jo.put("Msg", "流程已审批，不准许删除！");
    			jo.put("status", "false");
    			Pub.writeMessage(response, jo.toString());
        	}else{
        		DBUtil.execSql(conn, ap_processdetail);
            	DBUtil.execSql(conn, ap_processinfo);
            	DBUtil.execSql(conn, ap_process_ws);
            	DBUtil.execSql(conn, pub_blob);
            	DBUtil.execSql(conn, ap_task_schedule);
            	DBUtil.execSql(conn, ap_processconfig);
            	DBUtil.exec(conn, fs_event);
            	if("300101".equals(ywlx)){//招标需求业务删除后特殊处理
            		DBUtil.exec(conn,"update gc_ztb_xq set xqzt='1' where sjbh = '"+sjbh+"' and ywlx = '"+ywlx+"'");
            	}else if("700101".equals(ywlx)){
//            	    HTRXZT 合同履行类型
//            	    -3  未审批 新建
//            	    -2  审批中 呈请审批
//            	    -1  已审批 审批信息   审批通过   查看审批信息
//            	    0   审核中 部门：部门提交审批    合同管理部： 签订审核
//            	    1   履行中 合同管理部：签订履行
//            	    2   已结算
//            	    3   已结束
//            	    4   已中止
            	    //删除审批 合同状态小于审核中的 删除流程数据的同时修改状态为新建状态
            		DBUtil.exec(conn,"update GC_HTGL_HT set HTZT='-3' where sjbh = '"+sjbh+"' and ywlx = '"+ywlx+"' and htzt<0 ");
            	}else if(YwlxManager.GC_ZJGL_TQK.equals(ywlx)){
            	    //提请款删除 修改提请款状态
//            	    1  未提交     新建
//            	    4   待处理     部门领导通过
//            	    5   审批中     呈请审批
//            	    6   已审批     审批通过
//            	    7   已拔付
            	    //只有审批中的在删除审核流程时，才把改为待处理状态
            	    DBUtil.exec(conn, "update gc_zjgl_tqk set tqkzt='4'  where sjbh = '"+sjbh+"' and ywlx='"+ywlx+"' and tqkzt='5' ");
            	}

            	conn.commit();
            	
            	JSONObject jo = new JSONObject();
    			jo.put("Msg", "操作成功！");
    			jo.put("status", "sucess");
    			Pub.writeMessage(response, jo.toString());
        	}
        	
        	
        	
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
			String errMsg = e.getMessage();
			JSONObject jo = new JSONObject();
			jo.put("Msg", "删除流程错误:" + errMsg);
			jo.put("status", "error");
			Pub.writeMessage(response, jo.toString());
		} finally {
			DBUtil.closeConnetion(conn);
		}
    	
    }
    @RequestMapping(params = "changBlr", method = RequestMethod.POST)
    public void changBlr(HttpServletRequest request,HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    	String id = Pub.get(request, "id");
    	String seq = Pub.get(request, "seq");
    	String blr = Pub.get(request, "blr");
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
            String sql = "update ap_task_schedule set dbryid='"+blr+"' where id ='"+id+"' and seq = '"+seq+"' ";
            DBUtil.exec(conn, sql);
			conn.commit();
            request.setAttribute("issuccess", "1");
			JSONObject jo = new JSONObject();
			jo.put("Msg", "操作成功！");
			jo.put("status", "sucess");
			Pub.writeMessage(response, jo.toString());

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
			String errMsg = e.getMessage();
			JSONObject jo = new JSONObject();
			jo.put("Msg", "删除流程错误:" + errMsg);
			jo.put("status", "error");
			Pub.writeMessage(response, jo.toString());
		} finally {
			DBUtil.closeConnetion(conn);
		}

	}
    @RequestMapping(params = "doRegainSPAction", method = RequestMethod.POST)
    public void doRegainSPAction(HttpServletRequest request,HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String ywlx = request.getParameter("ywlx");
		String spbh = request.getParameter("spbh");
        String sjbh = request.getParameter("sjbh");
        
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String updateApTaskSchedule = "update AP_TASK_SCHEDULE t set " 
					+ "t.rwzt='" + TaskVO.TASK_STATUS_VALID + "'," 
					+ "CJSJ=sysdate,WCSJ=null, " 
					+ "RESULT=null,RESULTDSCR=null "
					+ "where SJBH = '"+sjbh+"' and rwzt='"+TaskVO.TASK_STATUS_BREAK+"'" ;
	    	String updateApProcessInfo = "update AP_PROCESSINFO set RESULT='0',RESULTDSCR='' where PROCESSOID = '"+spbh+"'";	
	    	String updateEventInfo = "update fs_event  set SJZT='7' where SJBH = '"+sjbh+"'";	
	    	
            DBUtil.exec(conn, updateApTaskSchedule);
            DBUtil.exec(conn, updateApProcessInfo);
            DBUtil.exec(conn, updateEventInfo);

			conn.commit();
            request.setAttribute("issuccess", "1");
			JSONObject jo = new JSONObject();
			jo.put("Msg", "操作成功！");
			jo.put("status", "sucess");
			Pub.writeMessage(response, jo.toString());

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
			String errMsg = e.getMessage();
			JSONObject jo = new JSONObject();
			jo.put("Msg", "恢复中断流程错误:" + errMsg);
			jo.put("status", "error");
			Pub.writeMessage(response, jo.toString());
		} finally {
			DBUtil.closeConnetion(conn);
		}

	}
    
    @RequestMapping(params = "doBreakSPAction", method = RequestMethod.POST)
    public void doBreakSPAction(HttpServletRequest request,HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String ywlx = request.getParameter("ywlx");
		String spbh = request.getParameter("spbh");
        String sjbh = request.getParameter("sjbh");
        
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String updateApTaskSchedule = "update AP_TASK_SCHEDULE t set " 
					+ "t.rwzt='" + TaskVO.TASK_STATUS_BREAK + "'," 
					+ "RESULT='"+TaskVO.TASK_TYPE_BREAK+"',RESULTDSCR='已中断', " 
					+ "WCSJ=sysdate where SJBH = '"+sjbh+"' and rwzt='"+TaskVO.TASK_STATUS_VALID+"'" ;
	    	String updateApProcessInfo = "update AP_PROCESSINFO set RESULT='3',RESULTDSCR='已中断' where PROCESSOID = '"+spbh+"'";	
	    	String updateEventInfo = "update fs_event  set SJZT='8' where SJBH = '"+sjbh+"'";	
	    	
            DBUtil.exec(conn, updateApTaskSchedule);
            DBUtil.exec(conn, updateApProcessInfo);
            DBUtil.exec(conn, updateEventInfo);

			conn.commit();
            request.setAttribute("issuccess", "1");
			JSONObject jo = new JSONObject();
			jo.put("Msg", "操作成功！");
			jo.put("status", "sucess");
			Pub.writeMessage(response, jo.toString());

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
			String errMsg = e.getMessage();
			JSONObject jo = new JSONObject();
			jo.put("Msg", "中断流程错误:" + errMsg);
			jo.put("status", "error");
			Pub.writeMessage(response, jo.toString());
		} finally {
			DBUtil.closeConnetion(conn);
		}

	}
    
    @RequestMapping(params = "finishThis", method = RequestMethod.POST)
    public void finishThis(HttpServletRequest request,HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    	String taskid = Pub.get(request, "taskid");
    	String taskseq = Pub.get(request, "taskseq");
    	String sjbh = Pub.get(request, "sjbh");
    	String ywlx = Pub.get(request, "ywlx");
    	
    	String updateApTaskSchedule = "update AP_TASK_SCHEDULE t set t.rwzt='" + TaskVO.TASK_STATUS_EXECUTED + "', " 
    			+ "t.wcsj=sysdate,t.wcrid='" + user.getAccount() + "',t.result='1',t.rwlx='2'," 
    			+ "t.wcrxm='" + user.getName() + "',t.wcdwdm='" + user.getDepartment() + "' where "
    			+ "id='" + taskid + "' and seq='" + taskseq + "' and sjbh='" + sjbh + "' and ywlx='" + ywlx + "'";
    	
    	Connection conn = null;
    	try {
    		conn = DBUtil.getConnection();
        	conn.setAutoCommit(false);
        	DBUtil.execSql(conn, updateApTaskSchedule);
        	conn.commit();
        	
        	JSONObject jo = new JSONObject();
			jo.put("Msg", "此待办审批结束");
			jo.put("status", "sucess");
			Pub.writeMessage(response, jo.toString());
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
			String errMsg = e.getMessage();
			JSONObject jo = new JSONObject();
			jo.put("Msg", "此待办归档错误:" + errMsg);
			jo.put("status", "error");
			Pub.writeMessage(response, jo.toString());
		} finally {
			DBUtil.closeConnetion(conn);
		}
    }
    
    @RequestMapping(params = "deleteBlr", method = RequestMethod.POST)
    public void deleteBlr(HttpServletRequest request,HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    	String taskseq = Pub.get(request, "seq");
    	String sjbh = Pub.get(request, "sjbh");
    	
/*    	String updateApTaskSchedule = "update AP_TASK_SCHEDULE t set t.rwzt='" + TaskVO.TASK_STATUS_EXECUTED + "', " 
    			+ "t.wcsj=sysdate,t.wcrid='" + user.getAccount() + "',t.result='1',t.rwlx='2'," 
    			+ "t.wcrxm='" + user.getName() + "',t.wcdwdm='" + user.getDepartment() + "' where "
    			+ "id='" + taskid + "' and seq='" + taskseq + "' and sjbh='" + sjbh + "' and ywlx='" + ywlx + "'";*/
    	
    	
    	
    	Connection conn = null;
    	try {
    		conn = DBUtil.getConnection();
        	conn.setAutoCommit(false);

        	String querySql = "select count(1) cnt from ap_task_schedule where sjbh='" + sjbh + "' and RWZT='01'";
        	String[][] rs = DBUtil.query(conn, querySql);
        	
        	
        	if(rs != null && rs.length != 0) {
    			// 修改待办信息为已办理
				String updateSql = "update ap_task_schedule set " 
						+ "rwzt='" + TaskVO.TASK_STATUS_EXECUTED + "'," 
						+ "wcsj=sysdate," 
						+ "wcrid='"+user.getAccount()+"',yxbs='1'," 
						+ "result='1',resultdscr='发起人发错，管理员处理'," 
						+ "wcrxm='"+user.getName()+"',wcdwdm='"+user.getDepartment()+"' " 
						+ "where seq='" + taskseq + "' and sjbh='" + sjbh + "'";
	        	DBUtil.execSql(conn, updateSql);
        		if("1".equals(rs[0][0])) {// 当剩一条待办信息的时候，返回给发起人。
        	        String newseq = DBUtil.getSequenceValue("AP_TASK_S", conn);
        	        
                	String queryInfoSql = "select CJRID,CJDWDM from AP_PROCESSINFO where EVENTID='" + sjbh + "'";
                	String[][] cjxx = DBUtil.query(conn, queryInfoSql);
        			// 添加待办信息
        			String insertSql = "insert into ap_task_schedule " 
        				+ " cols  (id,sjbh,ywlx,spbh,spjg,rwzt,yxbs," 
        				+ "rwlx,dbdwdm,dbryid,dbrrys,cjsj," 
        				+ "cjrid,rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl," 
        				+ "seq,dbrole,cjrxm,cjdwdm) " 
        	            + " select id,sjbh,ywlx,spbh,'','" + TaskVO.TASK_STATUS_VALID + "','1'," 
        				+ "rwlx,'"+cjxx[0][1]+"','"+cjxx[0][0]+"','',"
        	            + " sysdate "+"," 
        	            + "'"+user.getAccount()+"',rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl,'" 
        	            + newseq + "','','" + user.getName() + "','" + user.getOrgDept().getDeptParentID() + "' "
        	            + "from ap_task_schedule where seq='"+taskseq+"'";
    	        	DBUtil.execSql(conn, insertSql);
        		}
        	}
        	conn.commit();
        	
        	JSONObject jo = new JSONObject();
			jo.put("Msg", "此待办审批结束");
			jo.put("status", "sucess");
			Pub.writeMessage(response, jo.toString());
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
			String errMsg = e.getMessage();
			JSONObject jo = new JSONObject();
			jo.put("Msg", "此待办归档错误:" + errMsg);
			jo.put("status", "error");
			Pub.writeMessage(response, jo.toString());
		} finally {
			DBUtil.closeConnetion(conn);
		}
    }
}
