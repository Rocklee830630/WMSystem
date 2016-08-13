package com.ccthanking.framework.coreapp.aplink;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.WorkdayUtils;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.params.AppPara.AppParaVO;
import com.ccthanking.framework.params.ParaManager;
import com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.coreapp.aplink.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.wsdy.PubWS;
import com.ccthanking.framework.coreapp.aplink.impl.ApproveTask;
import com.ccthanking.common.EventManager;
import com.ccthanking.framework.common.Role;

public class TaskMgrBean
{
    public TaskVO createApproveTask(Connection conn, String sjbh, String ywlx,
                                    String dscr,
                                    User user)
        throws Exception
    {
        return createApproveTask(conn, sjbh, ywlx, dscr,
                                 "", "", "",
                                 user);
    }
    /** add by wuxp
     * 该方法为了实现同一个业务不同
     * 办案机关办理时，业务类型一样，文书模板
     * 审批创建中实现文书打印
     * @param request
     * @param response
     * @param sjbh
     * @param ywlx
     * @param wsbh  文书编号
     * @throws java.lang.Exception
     * add by wuxp
     */

    public void createApproveWS(HttpServletRequest request,
                               HttpServletResponse response, String sjbh,
                               String ywlx, Connection conn,String wsbh)
       throws Exception
   {
       User user = (User) request.getSession().getAttribute(com.ccthanking.
           framework.Globals.USER_KEY);
       String orgid = null;
       if (user != null)
           orgid = user.getDepartment(); //登陆人的组织机构编号
       PubWS pubws = new PubWS();
       pubws.getPrintXml(request, response,wsbh,sjbh, ywlx, user, conn,"","1");
   }

    /**
     * 审批创建中实现文书打印
     * @param request
     * @param response
     * @param sjbh
     * @param ywlx
     * @throws java.lang.Exception
     */
    public void createApproveWS(HttpServletRequest request,
                                HttpServletResponse response, String sjbh,
                                String ywlx, Connection conn)
        throws Exception
    {
        User user = (User) request.getSession().getAttribute(com.ccthanking.
            framework.Globals.USER_KEY);
        String orgid = null;
        if (user != null)
            orgid = user.getDepartment(); //登陆人的组织机构编号

        String sql = "select * from WS_OPERATIONTYPES where YWLX='" + ywlx +
            "' "; //and UNITID ='"+orgid+"'
        QuerySet qs = DBUtil.executeQuery(sql, null);
        if (qs.getRowCount() > 0)
        {
            PubWS pubws = new PubWS();
            for (int i = 0; i < qs.getRowCount(); i++)
            {
                if (qs.getString(i + 1, "WS_TEMPLATE_ID") != null)
                    pubws.getPrintXml(request, response,
                                      qs.getString(i + 1, "WS_TEMPLATE_ID"),
                                      sjbh, ywlx, user, conn,"","0");
            }
        }
    }

    public TaskVO createNextApproveTask(Connection conn, String taskOID,
                                         String seq, String dbdw, String dbrole,
                                         String dbr, User user)
        throws Exception
    {
        if (dbrole == null)
        {
            dbrole = "";
        }
        if (dbr == null)
        {
            dbr = "";
        }
        if (Pub.empty(dbdw))
        {
            dbdw = user.getOrgDept().getDeptParentID();
        }

        String newseq = DBUtil.getSequenceValue("AP_TASK_S", conn);
        String sql = "insert into ap_task_schedule cols(id,sjbh,ywlx,spbh,spjg,rwzt,rwlx,dbdwdm,dbryid,dbrrys,cjsj,cjrid,rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl,seq,dbrole,cjrxm,cjdwdm) "
            + " select id,sjbh,ywlx,spbh,'','" + TaskVO.TASK_STATUS_VALID +
            "',rwlx,'" + dbdw + "','" + dbr + "','',"+" to_date('"+Pub.getDate("yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS') "+",'" +
            user.getAccount() +
            "',rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl,'" + newseq +
            "','" +
            dbrole + "','" + user.getName() + "','" + user.getDepartment() +
            "' from ap_task_schedule "
            + " where id='" + taskOID + "' and seq='" + seq + "'";
        DBUtil.execSql(conn, sql);
        return getTask(conn, taskOID, newseq);
    }
    //add by wangzh for 退回
    private TaskVO createBackApproveTask(Connection conn, String taskOID,
                                         String seq,String dbms, String dbdw, String dbrole,
                                         String dbr, User user)
        throws Exception
    {
        if (dbrole == null)
        {
            dbrole = "";
        }
        if (dbr == null)
        {
            dbr = "";
        }
        if (Pub.empty(dbdw))
        {
            dbdw = user.getOrgDept().getDeptParentID();
        }

        String newseq = DBUtil.getSequenceValue("AP_TASK_S", conn);
        String sql = "insert into ap_task_schedule cols(id,sjbh,ywlx,spbh,spjg,rwzt,rwlx,dbdwdm,dbryid,dbrrys,cjsj,cjrid,rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl,seq,dbrole,cjrxm,cjdwdm) "
            + " select id,sjbh,ywlx,spbh,'','" + TaskVO.TASK_STATUS_VALID +
            "','"+TaskVO.TASK_TYPE_ROLLBACK+"','" + dbdw + "','" + dbr + "','',"+" to_date('"+Pub.getDate("yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS') "+",'" +
            user.getAccount() +
            "',rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,'"+dbms+"',linkurl,'" + newseq +
            "','" +
            dbrole + "','" + user.getName() + "','" + user.getDepartment() +
            "' from ap_task_schedule "
            + " where id='" + taskOID + "' and seq='" + seq + "'";
        DBUtil.execSql(conn, sql);
        return getTask(conn, taskOID, newseq);
    }



    public TaskVO getTask(Connection conn, String id, String seq)
        throws Exception
    {
        String sql = "select ID,SJBH,YWLX,SPBH,SPJG,"
            + "RWZT,RWLX,DBDWDM,DBRYID,DBRRYS,"
            + "to_char(CJSJ,'YYYY-MM-DD HH24:MI:SS'),to_char(WCSJ,'YYYY-MM-DD HH24:MI:SS'),to_char(ZXSJ,'YYYY-MM-DD HH24:MI:SS'),"
            + "CJRID,WCRID,"
            + "ZXRID,RYSBH,XH,HH,SSXQ,"
            +
            "PCSDM,FJDM,SJDM,MEMO,LINKURL,seq,result,resultdscr,dbrole,cjrxm,cjdwdm,"
            + "spyj,spr, wcrxm, wcdwdm, zxrxm, zxdwdm, xm, xb, sfhm, mz, to_char(csrq,'YYYY-MM-DD HH24:MI:SS') from ap_task_schedule where id='" +
            id + "' and seq='" + seq + "'";
        try
        {
            String[][] list = DBUtil.querySql(conn, sql);
            if (list != null)
            {
                int i = 0, j = -1;
                TaskVO vo = new TaskVO();
                vo.setID(list[i][++j]);
                vo.setSJBH(list[i][++j]);
                vo.setYWLX(list[i][++j]);
                vo.setSPBH(list[i][++j]);
                vo.setSPJG(list[i][++j]);
                vo.setRWZT(list[i][++j]);
                vo.setRWLX(list[i][++j]);
                vo.setDBDWDM(list[i][++j]);
                vo.setDBRYID(list[i][++j]);
                vo.setDBRRYS(list[i][++j]);
                vo.setCJSJ(Pub.toDate("yyyy-MM-dd HH:mm:ss", list[i][++j]));
                vo.setWCSJ(Pub.toDate("yyyy-MM-dd HH:mm:ss", list[i][++j]));
                vo.setZXSJ(Pub.toDate("yyyy-MM-dd HH:mm:ss", list[i][++j]));
                vo.setCJRID(list[i][++j]);
                vo.setWCRID(list[i][++j]);

                vo.setZXRID(list[i][++j]);
                vo.setRYSBH(list[i][++j]);
                vo.setXH(list[i][++j]);
                vo.setHH(list[i][++j]);
                vo.setSSXQ(list[i][++j]);
                vo.setPCSDM(list[i][++j]);
                vo.setFJDM(list[i][++j]);
                vo.setSJDM(list[i][++j]);
                vo.setMEMO(list[i][++j]);
                vo.setLINKURL(list[i][++j]);
                vo.setTaskSequence(list[i][++j]);
                vo.setResult(list[i][++j]);
                vo.setResultDscr(list[i][++j]);
                vo.setDbRole(list[i][++j]);
                vo.setCjrxm(list[i][++j]);
                vo.setCjdwdm(list[i][++j]);
                vo.setSpyj(list[i][++j]);
                vo.setSpr(list[i][++j]);
                vo.setWcrxm(list[i][++j]);
                vo.setWcdwdm(list[i][++j]);
                vo.setZxrxm(list[i][++j]);
                vo.setZxdwdm(list[i][++j]);
                vo.setXm(list[i][++j]);
                vo.setXb(list[i][++j]);
                vo.setSfhm(list[i][++j]);
                vo.setMz(list[i][++j]);
                vo.setCsrq(Pub.toDate("yyyy-MM-dd HH:mm:ss", list[i][++j]));
                return vo;
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return null;
    }
    public TaskVO createApproveTask(Connection conn, String sjbh, String ywlx,
                                    String dscr,
                                    String dbdw, String dbrole, String dbr,
                                    User user, String operationID)
        throws Exception
    {
         return createApproveTask(conn,sjbh,ywlx,dscr,dbdw,dbrole,dbr,user,operationID,"","","","");
    }
    public TaskVO createApproveTask(Connection conn, String sjbh, String ywlx,
                                    String dscr,
                                    String dbdw, String dbrole, String dbr,
                                    User user, String operationID,String value1,String value2,String value3,String value4)
        throws Exception
    {
        if (Pub.empty(operationID))
            return createApproveTask(conn, sjbh, ywlx, dscr, dbdw, dbrole, dbr,
                                     user);
        else
        {
        	
            if (Pub.empty(sjbh) || Pub.empty(ywlx) || dscr == null || user == null)
            {
                throw new Exception("不满足调用条件！");
            }
            try
            {
            	
                TaskVO vo = createTask(TaskVO.TASK_TYPE_APPROVE, sjbh, ywlx,dscr,user,conn);
                //add by wuxp
                vo.setValue1(value1);
                vo.setValue2(value2);
                vo.setValue3(value3);
                vo.setValue4(value4);
                
                //add by wuxp
                Collection procs = ProcessMgr.createProcess(conn,operationID,vo,user);
                String  Ists = "";
                ProcessType processtype =   (ProcessType) ProcessTypeMgr.getProcessByOperationOID(conn,operationID);
                Ists = processtype.getProcesstype();
                Step step = null;
                //普通呈请流程无需循环处理
                	vo.setRWLX(TaskVO.TASK_TYPE_APPROVE);
                    vo.setRWZT(TaskVO.TASK_STATUS_VALID);
//                    if (!Pub.empty(dbdw))
//                    {
//                        vo.setDBDWDM(dbdw);
//                    }
                    if (!Pub.empty(dbrole))
                    {
                    	vo.setDbRole(dbrole);
                    }
                    if (!Pub.empty(dbr))
                    {
                        vo.setDBRYID(dbr);
                    }


                    if (procs != null && procs.size() > 0)
                    {
                        Process proc = (Process) procs.iterator().next();
                        vo.setSPBH(proc.getProcessOID());
                        step = proc.getFirstStep();
                        //alter by cbl 发起审批后,获取ap_processdetail表的实例记录，更新此记录的超期日期维护到系统中
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
                      
                        //alter by cbl end
                        if(!Pub.empty(step.getDeptID()))
                        {
                            vo.setDBDWDM(step.getDeptID());
                        }
                        if (!Pub.empty(step.getRole()))
                        {
                        	vo.setDbRole(step.getRole());
                        }
                        if (!Pub.empty(step.getActor()))
                        {
                            vo.setDBRYID(step.getActor());
                        }
                        vo.SetStepsequence(String.valueOf(step.getStepSequence()));
                    }
                    else
                    {
                        throw new Exception("创建审批流程失败！");
                    }
                    setEventState(conn, sjbh, ywlx, Globals.EVENT_STATE_APPROVE);
                    
                    boolean isms = false;
                    //if(step!=null && "1".equals(step.getIsMS()))
                    {
                    	String actors =dbr;// step.getActor();
                    	if(actors!=null &&actors.length() > 0)
                    	{
                    		isms = true;
                    		String s[] = actors.split(",");
                    		for(int k=0;k<s.length;k++)
                    		{
                    			
                    			vo.setDBRYID(s[k]);
                    			//vo.setDBDWDM("");
                    			//vo.setDbRole("");
                    			vo.setFbs("1");
                    			if(k==0)
                    			{
                    				saveTask(conn, vo);
                    			}
                    			else
                    			{
                    				copyTask(conn,vo);
                    			}
                    		}
                    	}
                    	
                    }
                    if(step!=null && "1".equals(step.getIsCC()))
                    {
                    	String actors = step.getCcActor();
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
                    				saveTask(conn, vo);
                    			}
                    			else
                    			{
                    				copyTask(conn,vo);
                    			}
                    		}
                    	}
                    	
                    }
                    vo.setFbs("1");
                    if(step!=null&&"0".equals(step.getIsCC())&&!isms)
                    {
                    	vo.setDBDWDM(step.getDeptID());
                    	vo.setFbs("1");
                    	saveTask(conn, vo);
                    }
                
                return vo;
            }
            catch (Exception e)
            {
                e.printStackTrace(System.out);
                throw e;
            }
        }
    }
    
    /***
     * 特送，當發起特送流程時，選擇多個人。
     * @param conn
     * @param sjbh
     * @param ywlx
     * @param dscr
     * @param dbdw
     * @param dbrole
     * @param dbr
     * @param user
     * @param operationID
     * @throws Exception
     */
	public void createApproveMultiTaskTs(Connection conn, String sjbh,
			String ywlx, String dscr, String dbdw, String dbrole, String dbr,
			User user, String operationID) throws Exception {

		if (Pub.empty(sjbh) || Pub.empty(ywlx) || dscr == null
				|| user == null) {
			throw new Exception("不满足调用条件！");
		}
		
		try {
			Collection procs = null;
			String[] multiDbr = dbr.split(",");
			for (int i = 0; i < multiDbr.length; i++) {
				//创建待办对象
				TaskVO vo = createTask(TaskVO.TASK_TYPE_APPROVE, sjbh, ywlx, dscr, user, conn);
				// add by wuxp
				//创建流程实例通过待办对象。注：一个流程对应AP_PROCESSINFO一条记录，这里发起特送时，依然属于一个流程。
				procs = procs == null ? ProcessMgr.createProcess(conn, operationID, vo, user) : procs;
				if (procs != null && procs.size() > 0) {
					vo.setRWLX(TaskVO.TASK_TYPE_APPROVE);
					vo.setRWZT(TaskVO.TASK_STATUS_VALID);

					if (!Pub.empty(dbrole)) {
						// 特送无需查询角色
						vo.setDbRole("");
					}
					vo.setDBRYID(multiDbr[i]);


					Process proc = (Process) procs.iterator().next();
					vo.setSPBH(proc.getProcessOID());
					Step step = proc.getFirstStep();
					// alter by cbl
					// 发起审批后,获取ap_processdetail表的实例记录，更新此记录的超期日期维护到系统中
					String SHEDULE_DAYS = step.getShedule_days();// 获取超期天数
					if (!Pub.empty(SHEDULE_DAYS)) {
						// 计算超期日期，在当前工作日期后增加特定天数,取时间为当前日期
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						WorkdayUtils workdayUtils = new WorkdayUtils();
						Date date = workdayUtils.getWorkday(new Date(),
								Integer.parseInt(SHEDULE_DAYS));// 获取计算后的时间
						// 转换为字符串更新到ap_processdetail表中
						String updatesql = "update AP_PROCESSDETAIL a  set a.SHEDULE_TIME = to_date('"
								+ format.format(date)
								+ " 23:59:59','yyyy-MM-dd hh24:mi:ss') where PROCESSOID = '"
								+ step.getProcessOID() + "' and STEPSEQUENCE='" + step.getStepSequence() + "'";
						DBUtil.exec(conn, updatesql);
						// 设置待办表规定完成时间
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						vo.setShedule_time(sdf.parse(format.format(date) + " 23:59:59"));
					}
					vo.setDBDWDM("");

					if (Pub.empty(dbrole)) {
						// 特送无需查询角色
						vo.setDbRole("");
					}
					//设置待办序号发起序号为1
					vo.SetStepsequence("0");
					//
					setEventState(conn, sjbh, ywlx, Globals.EVENT_STATE_APPROVE);
					saveTask(conn, vo);
				} else {
					throw new Exception("创建审批流程失败！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}
    
   /*
    * 特送审批
    */
	public TaskVO createApproveTaskTs(Connection conn, String sjbh,
			String ywlx, String dscr, String dbdw, String dbrole, String dbr,
			User user, String operationID, String value1, String value2,
			String value3, String value4) throws Exception {
		if (Pub.empty(operationID))
			return createApproveTask(conn, sjbh, ywlx, dscr, dbdw, dbrole, dbr,
					user);
		else {

			if (Pub.empty(sjbh) || Pub.empty(ywlx) || dscr == null
					|| user == null) {
				throw new Exception("不满足调用条件！");
			}
			try {
				
				//创建待办对象
				TaskVO vo = createTask(TaskVO.TASK_TYPE_APPROVE, sjbh, ywlx,
						dscr, user, conn);
				// add by wuxp
				vo.setValue1(value1);
				vo.setValue2(value2);
				vo.setValue3(value3);
				vo.setValue4(value4);
				// add by wuxp
				//创建流程实例通过待办对象
				Collection procs = ProcessMgr.createProcess(conn, operationID, vo, user);
				
				/*String str = dbr;// 原始字符串
				String[] arrayStr = new String[] {};// 字符数组
				// List list = new ArrayList();// list
				arrayStr = str.split(",");// 字符串转字符数组
				// list = java.util.Arrays.asList(arrayStr);// 字符数组转list
				for (String s : arrayStr) {// 循环待办人
					
				}*/




			/*	String Ists = "";
				ProcessType processtype = (ProcessType) ProcessTypeMgr
						.getProcessByOperationOID(conn, operationID);
				Ists = processtype.getProcesstype();*/

				if (procs != null && procs.size() > 0) {
					if (!Pub.empty(dbr)) {// 代办人不能为空

					
	
							vo.setRWLX(TaskVO.TASK_TYPE_APPROVE);
							vo.setRWZT(TaskVO.TASK_STATUS_VALID);

							if (!Pub.empty(dbrole)) {
								// 特送无需查询角色
								vo.setDbRole("");
							}
							vo.setDBRYID(dbr);


							Process proc = (Process) procs.iterator().next();
							vo.setSPBH(proc.getProcessOID());
							Step step = proc.getFirstStep();
							// alter by cbl
							// 发起审批后,获取ap_processdetail表的实例记录，更新此记录的超期日期维护到系统中
							String SHEDULE_DAYS = step.getShedule_days();// 获取超期天数
							if (!Pub.empty(SHEDULE_DAYS)) {
								// 计算超期日期，在当前工作日期后增加特定天数,取时间为当前日期
								SimpleDateFormat format = new SimpleDateFormat(
										"yyyy-MM-dd");
								WorkdayUtils workdayUtils = new WorkdayUtils();
								Date date = workdayUtils.getWorkday(new Date(),
										Integer.parseInt(SHEDULE_DAYS));// 获取计算后的时间
								// 转换为字符串更新到ap_processdetail表中
								String updatesql = "update AP_PROCESSDETAIL a  set a.SHEDULE_TIME = to_date('"
										+ format.format(date)
										+ " 23:59:59','yyyy-MM-dd hh24:mi:ss') where PROCESSOID = '"
										+ step.getProcessOID()
										+ "' and STEPSEQUENCE='"
										+ step.getStepSequence() + "'";
								DBUtil.exec(conn, updatesql);
								// 设置待办表规定完成时间
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd hh:mm:ss");
								vo.setShedule_time(sdf.parse(format
										.format(date) + " 23:59:59"));
							}

							// alter by cbl end
							// 设置待办单位代码通过待办人
							//User user1 = UserManager.getInstance().getUserByLoginNameFromNc(dbr);
							vo.setDBDWDM("");

							if (Pub.empty(dbrole)) {
								// 特送无需查询角色
								vo.setDbRole("");

							}
							//设置待办序号发起序号为1
							vo.SetStepsequence("0");
							//
							setEventState(conn, sjbh, ywlx,
									Globals.EVENT_STATE_APPROVE);
							saveTask(conn, vo);
						
					}
				} else {
					throw new Exception("创建审批流程失败！");
				}

				return vo;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				throw e;
			}
		}
	}
			  
    public TaskVO createApproveTask(Connection conn, String sjbh, String ywlx,
                                    String dscr,
                                    String dbdw, String dbrole, String dbr,
                                    User user)
        throws Exception
    {

        return createApproveTask(conn, sjbh, ywlx, dscr,
                                 "", "", "",false,
                                 user);

    }

    public Collection getProcess(String ywlx, User user)
        throws Exception
    {
        if (Pub.empty(ywlx) || user == null)
        {
            throw new Exception("不满足调用条件！");
        }
        Connection conn = DBUtil.getConnection();
        Collection steps = null;
        try
        {
            TaskVO vo = createTask(TaskVO.TASK_TYPE_APPROVE, "", ywlx, "",user,conn);
            String operationOID = getDefaultOperation(conn, vo, user);
            Process process = ProcessTypeMgr.getProcessByOperationOID(conn,operationOID);
            steps = process.getSteps();

        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
        finally
        {
            if (conn != null)
                conn.close();
        }
        return steps;

    }
    public TaskVO createApproveTask(Connection conn, String sjbh, String ywlx,
                                    String dscr,
                                    String dbdw, String dbrole, String dbr,boolean firstbs,
                                    User user)
        throws Exception
    {
        return createApproveTask(conn,sjbh,ywlx,dscr,dbdw,dbrole,dbr,firstbs,user,"","","","");
    }
    public TaskVO createApproveTask(Connection conn, String sjbh, String ywlx,
                                    String dscr,
                                    String dbdw, String dbrole, String dbr,boolean firstbs,
                                    User user,String value1,String value2,String value3,String value4)
        throws Exception
    {
        String fBs = "0";
        if(firstbs) fBs = "1";
        /**@todo Complete this method*/
        if (Pub.empty(sjbh) || Pub.empty(ywlx) || dscr == null || user == null)
        {
            throw new Exception("不满足调用条件！");
        }
        try
        {
            TaskVO vo = createTask(TaskVO.TASK_TYPE_APPROVE, sjbh, ywlx, dscr,
                                   user,
                                   conn);
            vo.setRWLX(TaskVO.TASK_TYPE_APPROVE);
            vo.setRWZT(TaskVO.TASK_STATUS_VALID);
            vo.setFbs(fBs);//优先标示
            if (!Pub.empty(dbdw))
            {
                vo.setDBDWDM(dbdw);
            }
            if (!Pub.empty(dbrole))
            {
                vo.setDbRole(dbrole);
            }
            if (!Pub.empty(dbr))
            {
                vo.setDBRYID(dbr);
            }
            //add by wangzh
            vo.setValue1(value1);
            vo.setValue2(value2);
            vo.setValue3(value3);
            vo.setValue4(value4);
            //add by wangzh
            //////////////////////以下完成获取审批信息
            String operationOID = null;
            //取审批规则设置的操作编号
            switch (Pub.toInt(ywlx))
            {
//        case 101: //出生立户
//        case 102: //出生入户
//          operationOID = getCzrkCssbOperation(conn, vo, user);
//          break;
//        case 103: //准迁证办理
//          operationOID = getCzrkZqzOperation(conn, vo, user);
//          break;
//        case 0:
                default:
                    operationOID = getDefaultOperation(conn, vo, user);
                    break;
            }
            if (!Pub.empty(operationOID))
            {
                Collection procs = ProcessMgr.createProcess(conn, operationOID,
                    vo,
                    user);
                if (procs != null && procs.size() > 0)
                {
                    Process proc = (Process) procs.
                        iterator().next();
                    vo.setSPBH(proc.getProcessOID());
                    Step step = proc.getFirstStep();
                    if (Pub.empty(dbdw))
                    {
                        if(Pub.empty(step.getDeptID()))
                        {
                            OrgDept dept = user.getOrgDept();
                            int nextdepttype = step.getDeptLevel();
                            while(dept!=null&&dept.getDeptLevel() != nextdepttype){
                                if(dept.getParent()==null)
                                	break;
                            	dept = dept.getParent();
                                
                            }
//                            for (int j = Pub.toInt(dept.getDeptType());
//                                 j > nextdepttype; j--)
//                            {
//                                dept = dept.getParent() == null ? dept :
//                                    dept.getParent();
//                            }
                            dbdw = dept.getDeptID();
                           /* switch(dept.getDeptLevel())
                            {
                                case 1:
                                    dbdw = dept.getDeptID().substring(0,2)+"0000000000";
                                    break;
                                case 2:
                                    dbdw = dept.getDeptID().substring(0,4)+"00000000";
                                    break;
                                case 3:
                                    dbdw = dept.getDeptID().substring(0,6)+"000000";
                                    break;
                                default:
                                case 4:
                                    dbdw = dept.getDeptID();
                                    break;
                            }*/
                            vo.setDBDWDM(dbdw);
                        }
                        else
                        {
                            vo.setDBDWDM(step.getDeptID());
                        }
                    }
                    if (Pub.empty(dbrole))
                    {

                        vo.setDbRole(step.getRole());
                    }
                    if (Pub.empty(dbr))
                    {
                        vo.setDBRYID(step.getActor());
                    }
                }
                else
                {
                    throw new Exception("创建审批流程失败！");
                }
                setEventState(conn, sjbh, ywlx, Globals.EVENT_STATE_APPROVE);
            }
            else //没有设置相应的审批流程
            {
                //throw new Exception("没有设置相应的审批流程！");
                return null;
            }
            saveTask(conn, vo);
            return vo;
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            throw e;
        }
    }

    /**
     *
     * @param rwlx
     * @param sjbh
     * @param ywlx
     * @param dscr
     * @param user
     * @param conn
     * @return
     * @throws java.lang.Exception
     */
    public TaskVO createTask(String rwlx, String sjbh, String ywlx,
                              String dscr, User user, Connection conn)
        throws Exception
    {
        TaskVO vo = new TaskVO();
        String tid = DBUtil.getSequenceValue("AP_TASK_S", conn);
        //String tseq = tid;
        vo.setID(new RandomGUID().toString()); //任务编码
        vo.setTaskSequence(tid); //任务序号
        vo.setRWLX(rwlx); //任务类型
        vo.setSJBH(sjbh); //事件编号
        vo.setYWLX(ywlx); //业务类型
        vo.setMEMO(dscr); //任务描述
        vo.setCJRID(user.getAccount()); //任务创建人帐号
        //vo.setCJSJ(Pub.getCurrentDate()); //创建时间 
       java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        vo.setCJSJ(formatter.parse(Pub.getDate("yyyy-MM-dd HH:mm:ss"))); //创建时间
        vo.setWCSJ(null); //完成时间
        vo.setZXSJ(null); //注销时间
        vo.setSSXQ(user.getOrgDept().getSsxq()); //省市县区
        vo.setPCSDM(user.getDepartment()); //派出所代码
        vo.setFJDM(null); //分局代码
        vo.setSJDM(null); //市局代码
//        if (rwlx.equals(TaskVO.TASK_TYPE_APPROVE)) //审批任务
//            vo.setDBDWDM(user.getOrgDept().getDeptParentID()); //为上级单位
//        else
//            vo.setDBDWDM(user.getDepartment()); //本单位
        vo.setCjrxm(user.getName()); //人代码??
        vo.setCjdwdm(user.getDepartment()); //人单位代码??
        String url = null;
        //获得应用参数配置表
        if("200503".equals(ywlx)) {
        	String sql = "select t.operationoid from AP_PROCESSINFO t where t.eventid='"+sjbh+"'";
        	String[][] operationoid = DBUtil.querySql(conn,sql);
        	if(operationoid == null || "43489".equals(operationoid[0][0])) {
        		switch (Pub.toInt(vo.getRWLX()))
                {
                    case 2:
                        url = "jsp/framework/common/aplink/defaultApproveTaskSingle.jsp";
                        break;
                    case 1:
                        url = "jsp/framework/common/aplink/defaultArchivePage.jsp";
                        break;
                    case 6:
                        url = "jsp/framework/common/aplink/defaultBackPage.jsp";
                        break;
                    default:
                        url = "jsp/framework/common/aplink/defaultspFlowView.jsp";
                        break;
                }
                vo.setLINKURL(url);
                return vo;
        	}
        }
        String[][] urls = DBUtil.querySql(conn,
            "select url from ap_task_link where ywlx='" + ywlx +
            "' and (DEPTID='" +
            user.getDepartment() +
            "' or DEPTID='0') and rwlx='" +
            vo.getRWLX() + "' order by DEPTID desc");
        if (urls == null)
        {
            switch (Pub.toInt(vo.getRWLX()))
            {
                case 2:
                    url = "jsp/framework/common/aplink/defaultApproveTaskSingle.jsp";
                    break;
                case 1:
                    url = "jsp/framework/common/aplink/defaultArchivePage.jsp";
                    break;
                case 4:
                    url = "jsp/framework/common/aplink/defaultRollbackPage.jsp";
                    break;
                case 3:
                    url = "jsp/framework/common/aplink/defaultGeneralPage.jsp";
                    break;
                case 5:

                case 6:
                    url = "jsp/framework/common/aplink/defaultBackPage.jsp";
                    break;
                default:
                    url = "jsp/framework/common/aplink/defaultspFlowView.jsp";
                    break;
            }
        }
        else
        {
            url = urls[0][0];
        }
        vo.setLINKURL(url);
        return vo;
    }
   
    /**
    *
    * @param rwlx
    * @param sjbh
    * @param ywlx
    * @param dscr
    * @param user
    * @param conn
    * @return
    * @throws java.lang.Exception
    */
   public TaskVO createTaskTs(String rwlx, String sjbh, String ywlx,
                             String dscr, User user, Connection conn)
       throws Exception
   {
       TaskVO vo = new TaskVO();
       String tid = DBUtil.getSequenceValue("AP_TASK_S", conn);
       //String tseq = tid;
       vo.setID(new RandomGUID().toString()); //任务编码
       vo.setTaskSequence(tid); //任务序号
       vo.setRWLX(rwlx); //任务类型
       vo.setSJBH(sjbh); //事件编号
       vo.setYWLX(ywlx); //业务类型
       vo.setMEMO(dscr); //任务描述
       vo.setCJRID(user.getAccount()); //任务创建人帐号
       //vo.setCJSJ(Pub.getCurrentDate()); //创建时间
       java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        vo.setCJSJ(formatter.parse(Pub.getDate("yyyy-MM-dd HH:mm:ss"))); //创建时间
       vo.setWCSJ(null); //完成时间
       vo.setZXSJ(null); //注销时间
       vo.setSSXQ(user.getOrgDept().getSsxq()); //省市县区
       vo.setPCSDM(user.getDepartment()); //派出所代码
       vo.setFJDM(null); //分局代码
       vo.setSJDM(null); //市局代码
//       if (rwlx.equals(TaskVO.TASK_TYPE_APPROVE)) //审批任务
//           vo.setDBDWDM(user.getOrgDept().getDeptParentID()); //为上级单位
//       else
//           vo.setDBDWDM(user.getDepartment()); //本单位
       vo.setCjrxm(user.getName()); //人代码??
       vo.setCjdwdm(user.getDepartment()); //人单位代码??
       String url = null;
       //获得应用参数配置表
       String[][] urls = DBUtil.querySql(conn,
           "select url from ap_task_link where ywlx='" + ywlx +
           "' and (DEPTID='" +
           user.getDepartment() +
           "' or DEPTID='0') and rwlx='" +
           vo.getRWLX() + "' order by DEPTID desc");
       if (urls == null)
       {
           switch (Pub.toInt(vo.getRWLX()))
           {
               case 2:
                   url = "jsp/framework/common/aplink/defaultApproveTaskSingle.jsp";
                   break;
               case 1:
                   url = "jsp/framework/common/aplink/defaultArchivePage.jsp";
                   break;
               case 4:
                   url = "jsp/framework/common/aplink/defaultRollbackPage.jsp";
                   break;
               case 3:
                   url = "jsp/framework/common/aplink/defaultGeneralPage.jsp";
                   break;
               case 5:

               case 6:
                   url = "jsp/framework/common/aplink/defaultBackPage.jsp";
                   break;
               default:
                   url = "jsp/framework/common/aplink/defaultspFlowView.jsp";
                   break;
           }
       }
       else
       {
           url = urls[0][0];
       }
       vo.setLINKURL(url);
       return vo;
   }

    public TaskVO createDefaultTask(Connection conn, String sjbh, String ywlx,
                                    String dscr, String dbdw, String dbrole,
                                    String dbr, User user)
        throws Exception
    {
        /**@todo Complete this method*/
        if (Pub.empty(sjbh) || Pub.empty(ywlx) || Pub.empty(dscr) || user == null)
        {
            return null;
        }
        TaskVO vo = createTask(TaskVO.TASK_TYPE_DEFAULT, sjbh, ywlx, dscr,
                               user,
                               conn);
        vo.setRWLX(TaskVO.TASK_TYPE_DEFAULT);
        vo.setRWZT(TaskVO.TASK_STATUS_VALID);
        if (!Pub.empty(dbdw))
            vo.setDBDWDM(dbdw);
        if (!Pub.empty(dbrole))
            vo.setDbRole(dbrole);
        if (!Pub.empty(dbr))
            vo.setDBRYID(dbr);
        return vo;
    }

    public TaskVO createPrintTask(Connection conn, String sjbh, String ywlx,
                                  String dscr, String dbdw, String dbrole,
                                  String dbr, User user)
        throws Exception
    {
        /**@todo Complete this method*/
        if (Pub.empty(sjbh) || Pub.empty(ywlx) || Pub.empty(dscr) || user == null)
        {
            return null;
        }
        TaskVO vo = createTask(TaskVO.TASK_TYPE_PRINT, sjbh, ywlx, dscr,
                               user,
                               conn);
        vo.setRWLX(TaskVO.TASK_TYPE_PRINT);
        vo.setRWZT(TaskVO.TASK_STATUS_VALID);
        if (!Pub.empty(dbdw))
            vo.setDBDWDM(dbdw);
        if (!Pub.empty(dbrole))
            vo.setDbRole(dbrole);
        if (!Pub.empty(dbr))
            vo.setDBRYID(dbr);
        return vo;
    }

    private void setEventState(Connection conn, String sjbh, String ywlx,
                               String state)
        throws Exception
    {
        EventManager.setEventState(conn, sjbh, state);
    }
    public TaskVO createRollBackTask(Connection conn, String sjbh, String ywlx,
                                   String dscr, String dbdw, String dbrole,
                                   String dbr, User user)
        throws Exception
    {
        /**@todo Complete this method*/
        TaskVO vo = createTask(TaskVO.TASK_TYPE_ROLLBACK, sjbh, ywlx, dscr, user,
                               conn);
        vo.setRWLX(TaskVO.TASK_TYPE_ROLLBACK);
        vo.setRWZT(TaskVO.TASK_STATUS_VALID);
        if (!Pub.empty(dbdw))
            vo.setDBDWDM(dbdw);
        if (!Pub.empty(dbrole))
            vo.setDbRole(dbrole);
        if (!Pub.empty(dbr))
            vo.setDBRYID(dbr);
        return vo;
    }

    public TaskVO createAffirmTask(Connection conn, String sjbh, String ywlx,
                                   String dscr, String dbdw, String dbrole,
                                   String dbr, User user)
        throws Exception
    {
        /**@todo Complete this method*/
        TaskVO vo = createTask(TaskVO.TASK_TYPE_AFFIRM, sjbh, ywlx, dscr, user,
                               conn);
        vo.setRWLX(TaskVO.TASK_TYPE_AFFIRM);
        vo.setRWZT(TaskVO.TASK_STATUS_VALID);
        if (!Pub.empty(dbdw))
            vo.setDBDWDM(dbdw);
        if (!Pub.empty(dbrole))
            vo.setDbRole(dbrole);
        if (!Pub.empty(dbr))
            vo.setDBRYID(dbr);
        return vo;
    }

    public TaskVO createGeneralTask(Connection conn, String sjbh, String ywlx,
                                    String dscr,
                                    String dbdw, String dbrole, String dbr,
                                    User user)
        throws Exception
    {
        /**@todo Complete this method*/
        if (Pub.empty(sjbh) || Pub.empty(ywlx) || Pub.empty(dscr) || user == null)
        {
            return null;
        }
        TaskVO vo = createTask(TaskVO.TASK_TYPE_GENERAL, sjbh, ywlx, dscr,
                               user,
                               conn);
        vo.setRWLX(TaskVO.TASK_TYPE_GENERAL);
        vo.setRWZT(TaskVO.TASK_STATUS_VALID);
        if (!Pub.empty(dbdw))
            vo.setDBDWDM(dbdw);
        if (!Pub.empty(dbrole))
            vo.setDbRole(dbrole);
        if (!Pub.empty(dbr))
            vo.setDBRYID(dbr);
        return vo;
    }

    public void saveTask(Connection conn, TaskVO taskVO)
        throws Exception
    {
        if (taskVO == null)
        {
            throw new Exception("input parameter taskVO is null!");
        }
        try
        {
            String sql = null;
            if (Pub.empty(taskVO.getID()))
            { //insert
                String tid = DBUtil.getSequenceValue("AP_TASK_S", conn);
                String tseq = tid;
                taskVO.setID(tid);
                taskVO.setTaskSequence(tseq);
                sql = "insert into ap_task_schedule cols("
                    + "ID,SEQ,SJBH,YWLX,SPBH,SPJG,"
                    + "RWZT,RWLX,DBDWDM,DBRYID,DBRRYS,"
                    + "CJSJ,WCSJ,ZXSJ,"
                    + "CJRID,WCRID,"
                    + "ZXRID,RYSBH,XH,HH,SSXQ,"
                    + "PCSDM,FJDM,SJDM,MEMO,LINKURL,RESULT,RESULTDSCR,DBROLE,CJRXM,CJDWDM,spyj,spr,wcrxm, wcdwdm, zxrxm, zxdwdm, xm, xb, sfhm, mz, csrq,YXBS,SHEDULE_TIME,STEPSEQUENCE) "
                    + "values(?,?,?,?,?,?,"
                    + "?,?,?,?,?,"
                    + "?,?,?,?,?,"
                    + "?,?,?,?,?,"
                    + "?,?,?,?,?,?,?,?,"
                    + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }
            else
            {
                if (DBUtil.querySql(conn,
                    "select id,seq from ap_task_schedule where id='" +
                    taskVO.getID() + "' and seq='" +
                    taskVO.getTaskSequence() + "'") == null)
                { //insert
                    sql = "insert into ap_task_schedule cols("
                        + "ID,SEQ,SJBH,YWLX,SPBH,SPJG,"
                        + "RWZT,RWLX,DBDWDM,DBRYID,DBRRYS,"
                        + "CJSJ,WCSJ,ZXSJ,CJRID,WCRID,"
                        + "ZXRID,RYSBH,XH,HH,SSXQ,"
                        + "PCSDM,FJDM,SJDM,MEMO,LINKURL,RESULT,RESULTDSCR,"
                        + "DBROLE,CJRXM,CJDWDM,spyj,spr,"
                        + "wcrxm, wcdwdm, zxrxm, zxdwdm, xm,"
                        + " xb, sfhm, mz, csrq,YXBS,SHEDULE_TIME,STEPSEQUENCE) "
                        + "values(?,?,?,?,?,?,"
                        + "?,?,?,?,?,"
                        + "?,?,?,?,?,"
                        + "?,?,?,?,?,"
                        + "?,?,?,?,?,"
                        + "?,?,?,?,?,"
                        + "?,?,?,?,?,"
                        + "?,?,?,?,?,?,?,?,?)";
                }
                else
                { //update
                    sql = "update ap_task_schedule set "
                        + "ID=?,SEQ=?,SJBH=?,YWLX=?,SPBH=?,SPJG=?,"
                        + "RWZT=?,RWLX=?,DBDWDM=?,DBRYID=?,DBRRYS=?,"
                        + "CJSJ=?,WCSJ=?,ZXSJ=?,CJRID=?,WCRID=?,"
                        + "ZXRID=?,RYSBH=?,XH=?,HH=?,SSXQ=?,"
                        + "PCSDM=?,FJDM=?,SJDM=?,MEMO=?,LINKURL=?,RESULT=?,RESULTDSCR=?,"
                        + "DBROLE=?,CJRXM=?,CJDWDM=?,spyj=?,spr=?,"
                        + "wcrxm=?,wcdwdm=?,zxrxm=?,zxdwdm=?,xm=?,"
                        + "xb=?,sfhm=?,mz=?,csrq=?,YXBS=?,SHEDULE_TIME=?,STEPSEQUENCE = ? " 
                        + "where id='" + taskVO.getID() + "' and seq='" + taskVO.getTaskSequence() + "'";
                }
            }
            Object[] paralist =
                {
                taskVO.getID(), taskVO.getTaskSequence(), taskVO.getSJBH(),
                taskVO.getYWLX(), taskVO.getSPBH(),
                taskVO.getSPJG(), taskVO.getRWZT(), taskVO.getRWLX(),
                taskVO.getDBDWDM(), taskVO.getDBRYID(),
                taskVO.getDBRRYS(), taskVO.getCJSJ(), taskVO.getWCSJ(),
                taskVO.getZXSJ(), taskVO.getCJRID(),
                taskVO.getWCRID(), taskVO.getZXRID(), taskVO.getRYSBH(),
                taskVO.getXH(), taskVO.getHH(),
                taskVO.getSSXQ(), taskVO.getPCSDM(), taskVO.getFJDM(),
                taskVO.getSJDM(), taskVO.getMEMO(),
                taskVO.getLINKURL(), taskVO.getResult(), taskVO.getResultDscr(),
                taskVO.getDbRole(), taskVO.getCjrxm(),
                taskVO.getCjdwdm(), taskVO.getSpyj(), taskVO.getSpr(),
                taskVO.getWcrxm(), taskVO.getWcdwdm(),
                taskVO.getZxrxm(), taskVO.getZxdwdm(), taskVO.getXm(),
                taskVO.getXb(), taskVO.getSfhm(),
                taskVO.getMz(), taskVO.getCsrq(),taskVO.getFbs(),taskVO.getShedule_time(),taskVO.getStepsequence()
            };
            DBUtil.executeUpdate(conn, sql, paralist);
            //delete by zt@20090723:解决此语句将系统日志写爆问题，此语句完全没有任何意义。
            //DBUtil.execSql(conn,"update ap_task_schedule set spyj='"+taskVO.getSpyj()+"'");
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            throw e;
        }
    }

    public void copyTask(Connection conn, TaskVO taskVO)
            throws Exception
        {
            if (taskVO == null)
            {
                throw new Exception("input parameter taskVO is null!");
            }
            try
            {
                String sql = "select id,seq from ap_task_schedule where sjbh='" +
                        taskVO.getSJBH() + "' and ywlx='" +
                        taskVO.getYWLX() + "' and spbh='" +
                        taskVO.getSPBH() + "' and rwzt='" +
                        TaskVO.TASK_STATUS_VALID +"' and dbryid='" +
                        taskVO.getDBRYID()+"' and STEPSEQUENCE='"+
                        taskVO.getStepsequence()+"'";
                if(DBUtil.querySql(conn,sql) == null)
                { //insert
                    String tid = DBUtil.getSequenceValue("AP_TASK_S", conn);
                    String tseq = tid;
                    taskVO.setID(new RandomGUID().toString()); //任务编码);
                    taskVO.setTaskSequence(tseq);
                    sql = "insert into ap_task_schedule cols("
                        + "ID,SEQ,SJBH,YWLX,SPBH,SPJG,"
                        + "RWZT,RWLX,DBDWDM,DBRYID,DBRRYS,"
                        + "CJSJ,WCSJ,ZXSJ,"
                        + "CJRID,WCRID,"
                        + "ZXRID,RYSBH,XH,HH,SSXQ,"
                        + "PCSDM,FJDM,SJDM,MEMO,LINKURL,RESULT,RESULTDSCR,DBROLE,CJRXM,CJDWDM,spyj,spr,wcrxm, wcdwdm, zxrxm, zxdwdm, xm, xb, sfhm, mz, csrq,YXBS,SHEDULE_TIME,STEPSEQUENCE) "
                        + "values(?,?,?,?,?,?,"
                        + "?,?,?,?,?,"
                        + "?,?,?,?,?,"
                        + "?,?,?,?,?,"
                        + "?,?,?,?,?,?,?,?,"
                        + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                }else
                {
                	return ;
                }

                Object[] paralist =
                {
                    taskVO.getID(), taskVO.getTaskSequence(), taskVO.getSJBH(),
                    taskVO.getYWLX(), taskVO.getSPBH(),
                    taskVO.getSPJG(), taskVO.getRWZT(), taskVO.getRWLX(),
                    taskVO.getDBDWDM(), taskVO.getDBRYID(),
                    taskVO.getDBRRYS(), taskVO.getCJSJ(), taskVO.getWCSJ(),
                    taskVO.getZXSJ(), taskVO.getCJRID(),
                    taskVO.getWCRID(), taskVO.getZXRID(), taskVO.getRYSBH(),
                    taskVO.getXH(), taskVO.getHH(),
                    taskVO.getSSXQ(), taskVO.getPCSDM(), taskVO.getFJDM(),
                    taskVO.getSJDM(), taskVO.getMEMO(),
                    taskVO.getLINKURL(), taskVO.getResult(), taskVO.getResultDscr(),
                    taskVO.getDbRole(), taskVO.getCjrxm(),
                    taskVO.getCjdwdm(), taskVO.getSpyj(), taskVO.getSpr(),
                    taskVO.getWcrxm(), taskVO.getWcdwdm(),
                    taskVO.getZxrxm(), taskVO.getZxdwdm(), taskVO.getXm(),
                    taskVO.getXb(), taskVO.getSfhm(),
                    taskVO.getMz(), taskVO.getCsrq(),taskVO.getFbs(),taskVO.getShedule_time(),taskVO.getStepsequence()
                };
                DBUtil.executeUpdate(conn, sql, paralist);
                //delete by zt@20090723:解决此语句将系统日志写爆问题，此语句完全没有任何意义。
                //DBUtil.execSql(conn,"update ap_task_schedule set spyj='"+taskVO.getSpyj()+"'");
            }
            catch (Exception e)
            {
                e.printStackTrace(System.out);
                throw e;
            }
        }
    
    public TaskVO createAffirmTask(Connection conn)
        throws Exception
    {
        /**@todo Complete this method*/
        TaskVO vo = new TaskVO();
        vo.setID(DBUtil.getSequenceValue("AP_TASK_S", conn));
        vo.setTaskSequence(vo.getID());
        vo.setRWLX(TaskVO.TASK_TYPE_AFFIRM);
        vo.setRWZT(TaskVO.TASK_STATUS_VALID);
        return vo;
    }

    public TaskVO createGeneralTask(Connection conn)
        throws Exception
    {
        /**@todo Complete this method*/
        TaskVO vo = new TaskVO();
        vo.setID(DBUtil.getSequenceValue("AP_TASK_S", conn));
        vo.setTaskSequence(vo.getID());
        vo.setRWLX(TaskVO.TASK_TYPE_GENERAL);
        vo.setRWZT(TaskVO.TASK_STATUS_VALID);
        return vo;
    }

    public void deleteTask(Connection conn, String id, String seq, User user)
        throws Exception
    {
        deleteTasks(conn, id, seq, null, null, null, user);
    }

    public void deleteTasks(Connection conn, String id, User user)
        throws Exception
    {
        deleteTasks(conn, id, null, null, null, null, user);
    }

    public void deleteApproveTask(Connection conn, String sjbh, String ywlx,
                                  User user)
        throws Exception
    {
        deleteTasks(conn, null, null, sjbh, ywlx, TaskVO.TASK_TYPE_APPROVE,
                    user);
    }

    public void deletePrintTask(Connection conn, String sjbh, String ywlx,
                                User user)
        throws Exception
    {
        deleteTasks(conn, null, null, sjbh, ywlx, TaskVO.TASK_TYPE_PRINT,
                    user);
    }
    public void deleteDefaultTask(Connection conn, String sjbh, String ywlx,
                                User user)
        throws Exception
    {
        deleteTasks(conn, null, null, sjbh, ywlx, TaskVO.TASK_TYPE_DEFAULT,
                    user);
    }

    public void deleteAffirmTask(Connection conn, String sjbh, String ywlx,
                                 User user)
        throws Exception
    {
        deleteTasks(conn, null, null, sjbh, ywlx, TaskVO.TASK_TYPE_AFFIRM, user);
    }

    public void deleteGeneralTask(Connection conn, String sjbh, String ywlx,
                                  User user)
        throws Exception
    {
        deleteTasks(conn, null, null, sjbh, ywlx, TaskVO.TASK_TYPE_GENERAL,
                    user);
    }

    public void deleteAllTasks(Connection conn, String sjbh, String ywlx,
                               User user)
        throws Exception
    {
        deleteTasks(conn, null, null, sjbh, ywlx, null, user);
    }

    private void deleteTasks(Connection conn, String id, String seq,
                             String sjbh, String ywlx,
                             String rwlx, User user)
        throws Exception
    {
        if (Pub.empty(id) && Pub.empty(sjbh))
            throw new Exception("指定的任务不明确！");
        String sql = "update ap_task_schedule set RWZT='" +
            TaskVO.TASK_STATUS_INVALID + "',"
            + "ZXSJ=sysdate,ZXRID='" + user.getAccount() + "',ZXRXM='" +
            user.getName() + "',"
            + "ZXDWDM='" + user.getDepartment() + "' where 1=1 ";
        if (!Pub.empty(id))
            sql += " and ID='" + id + "'";
        if (!Pub.empty(seq))
            sql += " and SEQ='" + seq + "'";
        if (!Pub.empty(sjbh))
            sql += " and sjbh='" + sjbh + "'";
        if (!Pub.empty(ywlx))
            sql += " and ywlx='" + ywlx + "'";
        if (!Pub.empty(rwlx))
            sql += " and rwlx='" + rwlx + "'";
        DBUtil.execSql(conn, sql);
    }

    public void doPrintTask(Connection conn, String sjbh, String ywlx,
                            User user)
        throws Exception
    {
        doTask(conn, null, null, sjbh, ywlx, TaskVO.TASK_TYPE_PRINT, null, null,
               user);
    }
    
    /*
     * 结束特送
     */
    public void doPrintTask(Connection conn, String sjbh, 
    		String ywlx, String jsrYj, User user) throws Exception {
    	doTask(conn, null, null, sjbh, ywlx, TaskVO.TASK_TYPE_PRINT, null, jsrYj, user);
	}

    public void doPrintTask(Connection conn, String id, String seq,
                            String result, String dscr,
                            User user)
        throws Exception
    {
        doTask(conn, id, seq, null, null, TaskVO.TASK_TYPE_PRINT, result, dscr,
               user);
    }

    public void doGeneralTask(Connection conn, String sjbh, String ywlx,
                              User user)
        throws Exception
    {
        doTask(conn, null, null, sjbh, ywlx, TaskVO.TASK_TYPE_GENERAL, null, null,
               user);
    }

    public void doGeneralTask(Connection conn, String id, String seq,
                              String result, String dscr,
                              User user)
        throws Exception
    {
        doTask(conn, id, seq, null, null, TaskVO.TASK_TYPE_GENERAL, result,
               dscr,
               user);
    }

    public void doAffirmTask(Connection conn, String sjbh, String ywlx,
                             User user)
        throws Exception
    {
        doTask(conn, null, null, sjbh, ywlx, TaskVO.TASK_TYPE_AFFIRM, null, null,
               user);
    }

    public void doAffirmTask(Connection conn, String id, String seq,
                             String result, String dscr,
                             User user)
        throws Exception
    {
        doTask(conn, id, seq, null, null, TaskVO.TASK_TYPE_AFFIRM, result, dscr,
               user);
    }


    public void doRollBackTask(Connection conn, String sjbh, String ywlx,
                                 User user)
            throws Exception
        {
            doTask(conn, null, null, sjbh, ywlx, TaskVO.TASK_TYPE_ROLLBACK, null, null,
                   user);
        }

    public void doTask(Connection conn, String id, String seq, String sjbh,
                       String ywlx,
                       String rwlx, String result, String dscr, User user)
        throws Exception
    {
        if (Pub.empty(id) && Pub.empty(sjbh))
            throw new Exception("指定的任务不明确！");
        if (result == null)
            result = "";
        if (dscr == null)
            dscr = "";
        //add by cbl 判断是否为特送审批归档,特送审批归档任务类型为2
        String tssql = "select a.processtype from AP_PROCESSINFO t, AP_PROCESSTYPE a  where t.processtypeoid = a.processtypeoid and t.eventid = '"+sjbh+"'";
		String [][] fqr = DBUtil.querySql(conn, tssql);
		String processtype = "";

        if (fqr != null)
        {
        	processtype =  fqr[0][0];

        }
        String rwlx_update = "";
        if(("4").equals(processtype)){
        	rwlx_update = " , rwlx='1' ";
        //	result = "1";
        }
        //add by cbl end
        String objs[] = {dscr};
        String sql = "update ap_task_schedule set RWZT='" +
            TaskVO.TASK_STATUS_EXECUTED + "',"
            + "WCSJ=to_date('"+Pub.getDate("yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS'),WCRID='" + user.getAccount() + "',WCRXM='" +
            user.getName() + "',"
            + "WCDWDM='" + user.getDepartment() + "',result='" + result +
            "',resultdscr=? "+rwlx_update+" where 1=1 ";
        if (!Pub.empty(id))
            sql += " and ID='" + id + "'";
        if (!Pub.empty(seq))
            sql += " and SEQ='" + seq + "'";
        if (!Pub.empty(sjbh))
            sql += " and sjbh='" + sjbh + "'";
        if (!Pub.empty(ywlx))
            sql += " and ywlx='" + ywlx + "'";
        //add by cbl
        if(processtype.equals("4")){
                sql += " and rwlx='" + TaskVO.TASK_TYPE_APPROVE + "' and rwzt='"+TaskVO.TASK_STATUS_VALID+"'";
        }else{
        	if (!Pub.empty(rwlx))
                sql += " and rwlx='" + rwlx + "'";
        }
        
        DBUtil.executeUpdate(conn, sql, objs);
    }

    public Collection getTasks(Connection conn, String id)
        throws Exception
    {
        try
        {
            String sql = "select ID,SJBH,YWLX,SPBH,SPJG,"
                + "RWZT,RWLX,DBDWDM,DBRYID,DBRRYS,"
                + "to_char(CJSJ,'YYYY-MM-DD HH24:MI:SS'),to_char(WCSJ,'YYYY-MM-DD HH24:MI:SS'),to_char(ZXSJ,'YYYY-MM-DD HH24:MI:SS'),"
                + "CJRID,WCRID,"
                + "ZXRID,RYSBH,XH,HH,SSXQ,"
                + "PCSDM,FJDM,SJDM,MEMO,LINKURL,"
                + "seq,result,resultdscr,dbrole,cjrxm,"
                + "cjdwdm,spyj,spr,wcrxm, wcdwdm, "
                + "zxrxm, zxdwdm, xm, xb, sfhm, "
                +
                "mz, to_char(csrq,'YYYY-MM-DD HH24:MI:SS') from ap_task_schedule where id='" +
                id + "' and rwzt!='" + TaskVO.TASK_STATUS_INVALID + "'";
            sql += " order by to_number(seq) desc";
            String[][] list = DBUtil.querySql(conn, sql);
            if (list != null)
            {
                ArrayList res = new ArrayList(list.length);
                for (int i = 0, j = -1; i < list.length; i++, j = -1)
                {
                    TaskVO vo = new TaskVO();
                    vo.setID(list[i][++j]);
                    vo.setSJBH(list[i][++j]);
                    vo.setYWLX(list[i][++j]);
                    vo.setSPBH(list[i][++j]);
                    vo.setSPJG(list[i][++j]);
                    vo.setRWZT(list[i][++j]);
                    vo.setRWLX(list[i][++j]);
                    vo.setDBDWDM(list[i][++j]);
                    vo.setDBRYID(list[i][++j]);
                    vo.setDBRRYS(list[i][++j]);
                    vo.setCJSJ(Pub.toDate("yyyy-MM-dd HH:mm:ss", list[i][++j]));
                    vo.setWCSJ(Pub.toDate("yyyy-MM-dd HH:mm:ss", list[i][++j]));
                    vo.setZXSJ(Pub.toDate("yyyy-MM-dd HH:mm:ss", list[i][++j]));
                    vo.setCJRID(list[i][++j]);
                    vo.setWCRID(list[i][++j]);
                    vo.setZXRID(list[i][++j]);
                    vo.setRYSBH(list[i][++j]);
                    vo.setXH(list[i][++j]);
                    vo.setHH(list[i][++j]);
                    vo.setSSXQ(list[i][++j]);
                    vo.setPCSDM(list[i][++j]);
                    vo.setFJDM(list[i][++j]);
                    vo.setSJDM(list[i][++j]);
                    vo.setMEMO(list[i][++j]);
                    vo.setLINKURL(list[i][++j]);
                    vo.setTaskSequence(list[i][++j]);
                    vo.setResult(list[i][++j]);
                    vo.setResultDscr(list[i][++j]);
                    vo.setDbRole(list[i][++j]);
                    vo.setCjrxm(list[i][++j]);
                    vo.setCjdwdm(list[i][++j]);
                    vo.setSpyj(list[i][++j]);
                    vo.setSpr(list[i][++j]);
                    vo.setWcrxm(list[i][++j]);
                    vo.setWcdwdm(list[i][++j]);
                    vo.setZxrxm(list[i][++j]);
                    vo.setZxdwdm(list[i][++j]);
                    vo.setXm(list[i][++j]);
                    vo.setXb(list[i][++j]);
                    vo.setSfhm(list[i][++j]);
                    vo.setMz(list[i][++j]);
                    vo.setCsrq(Pub.toDate("yyyy-MM-dd HH:mm:ss", list[i][++j]));
                    res.add(vo);
                }
                return res;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Collection getTasks(String sjbh, String rwzt, String rwlx,Connection conn)
        throws Exception
    {
            String sql = "select ID,SJBH,YWLX,SPBH,SPJG,"
                + "RWZT,RWLX,DBDWDM,DBRYID,DBRRYS,"
                + "to_char(CJSJ,'YYYY-MM-DD HH24:MI:SS'),to_char(WCSJ,'YYYY-MM-DD HH24:MI:SS'),to_char(ZXSJ,'YYYY-MM-DD HH24:MI:SS'),"
                + "CJRID,WCRID,"
                + "ZXRID,RYSBH,XH,HH,SSXQ,"
                + "PCSDM,FJDM,SJDM,MEMO,LINKURL,"
                + "seq,result,resultdscr,dbrole,cjrxm,"
                + "cjdwdm,spyj,spr,wcrxm, wcdwdm, "
                + "zxrxm, zxdwdm, xm, xb, sfhm, "
                +
                "mz, to_char(csrq,'YYYY-MM-DD HH24:MI:SS') from ap_task_schedule where sjbh='" +
                sjbh + "' ";
            if (!Pub.empty(rwzt))
            {
                sql += " and rwzt='" + rwzt + "'";
            }
            if (!Pub.empty(rwlx))
            {
                sql += " and rwlx='" + rwlx + "'";
            }
            sql += " order by to_number(seq) desc";
            String[][] list = DBUtil.querySql(conn, sql);
            if (list != null)
            {
                ArrayList res = new ArrayList(list.length);
                for (int i = 0, j = -1; i < list.length; i++, j = -1)
                {
                    TaskVO vo = new TaskVO();
                    vo.setID(list[i][++j]);
                    vo.setSJBH(list[i][++j]);
                    vo.setYWLX(list[i][++j]);
                    vo.setSPBH(list[i][++j]);
                    vo.setSPJG(list[i][++j]);
                    vo.setRWZT(list[i][++j]);
                    vo.setRWLX(list[i][++j]);
                    vo.setDBDWDM(list[i][++j]);
                    vo.setDBRYID(list[i][++j]);
                    vo.setDBRRYS(list[i][++j]);
                    vo.setCJSJ(Pub.toDate("yyyy-MM-dd HH:mm:ss", list[i][++j]));
                    vo.setWCSJ(Pub.toDate("yyyy-MM-dd HH:mm:ss", list[i][++j]));
                    vo.setZXSJ(Pub.toDate("yyyy-MM-dd HH:mm:ss", list[i][++j]));
                    vo.setCJRID(list[i][++j]);
                    vo.setWCRID(list[i][++j]);
                    vo.setZXRID(list[i][++j]);
                    vo.setRYSBH(list[i][++j]);
                    vo.setXH(list[i][++j]);
                    vo.setHH(list[i][++j]);
                    vo.setSSXQ(list[i][++j]);
                    vo.setPCSDM(list[i][++j]);
                    vo.setFJDM(list[i][++j]);
                    vo.setSJDM(list[i][++j]);
                    vo.setMEMO(list[i][++j]);
                    vo.setLINKURL(list[i][++j]);
                    vo.setTaskSequence(list[i][++j]);
                    vo.setResult(list[i][++j]);
                    vo.setResultDscr(list[i][++j]);
                    vo.setDbRole(list[i][++j]);
                    vo.setCjrxm(list[i][++j]);
                    vo.setCjdwdm(list[i][++j]);
                    vo.setSpyj(list[i][++j]);
                    vo.setSpr(list[i][++j]);
                    vo.setWcrxm(list[i][++j]);
                    vo.setWcdwdm(list[i][++j]);
                    vo.setZxrxm(list[i][++j]);
                    vo.setZxdwdm(list[i][++j]);
                    vo.setXm(list[i][++j]);
                    vo.setXb(list[i][++j]);
                    vo.setSfhm(list[i][++j]);
                    vo.setMz(list[i][++j]);
                    vo.setCsrq(Pub.toDate("yyyy-MM-dd HH:mm:ss", list[i][++j]));
                    res.add(vo);
                }
                return res;
            }
            else
            {
                return null;
            }
    }

    public Collection getTasks(String sjbh, String rwzt, String rwlx)
        throws Exception
    {
        Connection conn = null;
        try
        {
            conn = DBUtil.getConnection();
            return this.getTasks(sjbh,rwzt,rwlx,conn);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
    }

    public TaskVO getApproveTask(String sjbh, String rwzt)
        throws Exception
    {
        /**@todo Complete this method*/
        Collection c = this.getTasks(sjbh, rwzt, TaskVO.TASK_TYPE_APPROVE);
        if (c != null)
        {
            return (TaskVO) c.toArray()[0];
        }
        return null;
    }

    public TaskVO getAffirmTask(String sjbh, String rwzt)
        throws Exception
    {
        /**@todo Complete this method*/
        Collection c = this.getTasks(sjbh, rwzt, TaskVO.TASK_TYPE_AFFIRM);
        if (c != null)
        {
            return (TaskVO) c.toArray()[0];
        }
        return null;
    }

    public TaskVO getPrintTask(String sjbh, String rwzt)
        throws Exception
    {
        /**@todo Complete this method*/
        Collection c = this.getTasks(sjbh, rwzt, TaskVO.TASK_TYPE_PRINT);
        if (c != null)
        {
            return (TaskVO) c.toArray()[0];
        }
        return null;
    }

    public Collection getGeneralTasks(String sjbh, String rwzt)
        throws Exception
    {
        /**@todo Complete this method*/
        return getTasks(sjbh, rwzt, TaskVO.TASK_TYPE_GENERAL);
    }



    public ApproveRule[] getApproveRule(Connection conn, TaskVO vo, User user)
        throws Exception
    {
        OrgDept dept = user.getOrgDept();
        String fjdm = "", sjdm = "";
        int level = Pub.toInt(dept.getDeptType());
        OrgDept parent = dept;
        String stdm = "";
        for (int i = level; i > 0; i--)
        {
        // modified by xukx 增加当parent为空时的判断
        	if(parent == null ) break;
        // end
            parent = parent.getParent();
            if (parent != null)
            {
                switch (Pub.toInt(parent.getDeptType()))
                {
                    case 1:
                        stdm = parent.getDeptID();
                        break;
                    case 2:
                        sjdm = parent.getDeptID();
                        break;
                    case 3:
                        fjdm = parent.getDeptID();
                        break;
                    default:
                        break;
                }
            }
        }
        String sql =
            "select * from (select UNITID,YWLX,PCSDM,FJDM,SJDM,SSXQ,VALUE1,VALUE2," +
            "VALUE3,OPERATION_OID,RULE_NAME,MEMO,RULE_OID,4 num from ap_approve_rule t " +
            " where t.ywlx='" + vo.getYWLX() + "' and t.unitid='" +
            user.getDepartment() + "'"
            + (Pub.empty(fjdm)?"":
            " union all select UNITID,YWLX,PCSDM,FJDM,SJDM,SSXQ,VALUE1,VALUE2," +
            "VALUE3,OPERATION_OID,RULE_NAME,MEMO,RULE_OID,3 num from ap_approve_rule t " +
            " where t.ywlx='" + vo.getYWLX() + "' and t.unitid='" + fjdm + "'")
            + (Pub.empty(sjdm)?"":
            " union all select UNITID,YWLX,PCSDM,FJDM,SJDM,SSXQ,VALUE1,VALUE2," +
            "VALUE3,OPERATION_OID,RULE_NAME,MEMO,RULE_OID,2 num from ap_approve_rule t " +
            " where t.ywlx='" + vo.getYWLX() + "' and t.unitid='" + stdm + "'")
            + (Pub.empty(stdm)?"":
               " union all select UNITID,YWLX,PCSDM,FJDM,SJDM,SSXQ,VALUE1,VALUE2," +
               "VALUE3,OPERATION_OID,RULE_NAME,MEMO,RULE_OID,1 num from ap_approve_rule t " +
               " where t.ywlx='" + vo.getYWLX() + "' and t.unitid='" + stdm + "'")
            +  ") order by num desc,unitid desc";
        String[][] list = DBUtil.query(conn, sql);
        if (list != null)
        {
            sql =
                "select * from (select OPERATIONOID,4 num,dwdm from ap_tasks where EVENTTYPE='" +
                vo.getYWLX() + "' and dwdm='" + dept.getDeptID() + "'"
                + (Pub.empty(fjdm)?"": " union all "
                +
                "select OPERATIONOID,3 num,dwdm from ap_tasks where EVENTTYPE='" +
                vo.getYWLX() + "' and dwdm='" + fjdm + "'")
                + (Pub.empty(sjdm)?"": " union all "
                +
                "select OPERATIONOID,2 num,dwdm from ap_tasks where EVENTTYPE='" +
                vo.getYWLX() + "' and dwdm='" + sjdm + "'")
             + (Pub.empty(stdm)?"": " union all "
             +
             "select OPERATIONOID,1 num,dwdm from ap_tasks where EVENTTYPE='" +
             vo.getYWLX() + "' and dwdm='" + stdm + "'")
                +
                ") order by num desc,dwdm desc";
            String[][] listDefault = DBUtil.query(conn, sql);
            ApproveRule[] res = new ApproveRule[list.length +
                (listDefault == null ? 0 : listDefault.length)];
            for (int i = 0; i < list.length; i++)
            {
                ApproveRule rule = new ApproveRule();
                rule.setUnitid(list[i][0]);
                rule.setYwlx(list[i][1]);
                rule.setPcsdm(list[i][2]);
                rule.setFjdm(list[i][3]);
                rule.setSjdm(list[i][4]);
                rule.setSsxq(list[i][5]);
                rule.setValue1(list[i][6]);
                rule.setValue2(list[i][7]);
                rule.setValue3(list[i][8]);
                rule.setOperationOID(list[i][9]);
                rule.setRuleName(list[i][10]);
                rule.setMemo(list[i][11]);
                rule.setRuleOID(list[i][12]);
                res[i] = rule;
            }
            if (listDefault != null)
            {
                for (int i = 0; i < listDefault.length; i++)
                {
                    ApproveRule rule = new ApproveRule();
                    rule.setYwlx(vo.getYWLX());
                    rule.setOperationOID(listDefault[i][0]);
                    res[list.length + i] = rule;
                }
            }
            return res;
        }
        else
        {
            sql =
                "select * from (select OPERATIONOID,4 num,dwdm from ap_tasks where EVENTTYPE='" +
                vo.getYWLX() + "' and dwdm='" + dept.getDeptID() + "'"
                + (Pub.empty(fjdm)?"": " union all "
                +
                "select OPERATIONOID,3 num,dwdm from ap_tasks where EVENTTYPE='" +
                vo.getYWLX() + "' and dwdm='" + fjdm + "'")
                + (Pub.empty(sjdm)?"": " union all "
                +
                "select OPERATIONOID,2 num,dwdm from ap_tasks where EVENTTYPE='" +
                vo.getYWLX() + "' and dwdm='" + sjdm + "'")
             + (Pub.empty(stdm)?"": " union all "
             +
             "select OPERATIONOID,1 num,dwdm from ap_tasks where EVENTTYPE='" +
             vo.getYWLX() + "' and dwdm='" + stdm + "'")
          +
                ") order by num desc,dwdm desc";
            list = DBUtil.query(conn, sql);
            if (list != null)
            {
                ApproveRule[] res = new ApproveRule[list.length];
                for (int i = 0; i < list.length; i++)
                {
                    ApproveRule rule = new ApproveRule();
                    rule.setYwlx(vo.getYWLX());
                    rule.setOperationOID(list[i][0]);
                    res[i] = rule;
                }
                return res;
            }
        }
        return null;
    }

    public TaskVO doApproveTask(Connection conn, String id, String result,
                                String dscr, String dbdw, User user,JSONObject jo)
        throws Exception
    {
        return doApproveTask(conn, id, null, result, dscr, null, dbdw, null, null,
                             user, jo);
    }

    public TaskVO doApproveTask(Connection conn, String id, String seq,
                                String result, String dscr, String dbdw,
                                User user,JSONObject jo)
        throws Exception
    {
        return doApproveTask(conn, id, seq, result, dscr, null, dbdw, null, null,
                             user, jo);
    }


    public TaskVO doApproveTask(Connection conn, String id, String seq,
                                String result, String dscr, String spbh,
                                String dbdw, String dbrole, String dbr,
                                User user,JSONObject jo)
        throws Exception
    {
        if (Pub.empty(result))
        {
            throw new Exception("审批结果为空！");
        }
        if (dscr == null)
        {
            dscr = "";
        }
        TaskVO vo = null;
        String ccFlag = (String)jo.get("ccFlag");
        try
        {
	        //特送下一级审批
	        boolean Tsnext = false;
	        String isSpecialNextStep = (String)jo.get("isSpecialNextStep");
	        if("1".equals(isSpecialNextStep)){
	        	Tsnext = true;
	        }
            String sql = null;
            String[][] list = null;
            String sjbh = null, ywlx = null, ydbdw = null, ydbr = null, ydbrole = null,
                memo = null;
            sql ="select id,seq,sjbh,ywlx,spbh,memo,dbdwdm,dbryid,dbrole from ap_task_schedule where id='" + id + "' and rwzt='" + TaskVO.TASK_STATUS_VALID +"'";
            if (Pub.empty(seq))
            {
                sql += " order by to_number(seq) desc";
            }
            else
            {
                sql += " and seq='" + seq + "'";
            }
            list = DBUtil.querySql(conn, sql);
            if (list == null)
            {
                throw new Exception("任务未找到！");
            }
            if (Pub.empty(seq))
            {
                seq = list[0][1];
            }
            if (Pub.empty(spbh))
            {
                spbh = list[0][4];
            }
            sjbh = list[0][2];
            ywlx = list[0][3];
            memo = list[0][5];
            ydbdw = list[0][6];
            ydbr = list[0][7];
            ydbrole = list[0][8];
            if (!Pub.empty(ydbr) && !ydbr.equals(user.getAccount()))
            {
              //  throw new Exception("当前用户无执行权限！");
            }
//            if(!Pub.empty(ydbrole))
//            {
//                Role[] roles = user.getRoles();
//                if (roles != null)
//                {
//                    boolean found = false;
//                    for (int i = 0; i < roles.length; i++)
//                    {
//                        if(ydbrole.equals(roles[i].getName()))
//                        {
//                            found = true;
//                            break;
//                        }
//                    }
//                    if(!found) throw new Exception("当前用户无执行权限！");
//                }
//            }
            //if(!user.getLevelName().equals(OrgDeptManager.getInstance().getDeptByID(ydbdw).getDeptType()))
            //    throw new Exception("当前用户无执行权限！");
            Process proc = ProcessMgr.getProcessByID(conn, spbh);
            //add by wangzh begin  2013 10 18 对于发文流程特殊处理，如果分管副主任选择建管中心副主任或主任，那么最后一个节点自动删除，返回给发起人 
            if("200503".equals(ywlx)) {
            	 String user_role = user.getRoleListIdString();
            	 if(user_role.indexOf("219d48ca-866c-4bfd-a02a-613d15ee4c94")>-1||user_role.indexOf("e9635cef-e899-441e-a76f-384b37e92107")>-1){
            		Step nowStep = proc.open();
            		int currentStep = nowStep.getStepSequence();
            		int sumSept = proc.getSteps().size();
            		// 发文流程。第五个节点时
            		if(currentStep==5) {
            			// 并且当前节点数少于发文流程的总结点数时
            			if (currentStep < sumSept) {
                			proc.deleteLastStep();
						}

                    	String temp_role_id = "";
                    	// user_role 存在常务副主任（219d48ca-866c-4bfd-a02a-613d15ee4c94）
                    	if(user_role.indexOf("219d48ca-866c-4bfd-a02a-613d15ee4c94") != -1) {
                    		temp_role_id = "219d48ca-866c-4bfd-a02a-613d15ee4c94";
                    	// user_role 存在监管中心主任（e9635cef-e899-441e-a76f-384b37e92107）
                    	} else if(user_role.indexOf("e9635cef-e899-441e-a76f-384b37e92107") != -1) {
                    		temp_role_id = "e9635cef-e899-441e-a76f-384b37e92107";
                    	}
                    	String update_ap_process_ws = "update ap_process_ws set approverole='" + temp_role_id + "' "
                    			+ "where APPROVELEVEL=5 and fieldname='YGBMHQ' and sjbh='"+sjbh+"'";
                    	DBUtil.execUpdateSql(conn, update_ap_process_ws);
            		}
            	 }
            }
            //add by wangzh end  2013 10 18 
            
            
            
            Step step = proc.execute(conn, user.getAccount(),user.getDepartment(),id, seq,result, dscr);
            String fbs = proc.getFbs();
            if (step == null)
            {
                throw new Exception("审批条件不满足!");
            }
            else
            {
        		int currentStep = step.getStepSequence();
        		int nextStep = step.getNextStepOID();
                if (proc.getState() == proc.AP_ERROR)
                {
                    throw new Exception("审批流执行错误!");
                }
                if (result.equals("2")) //不同意
                {
                    //生成审批不通过确认任务...
                    String adscr = null;
                    String tempStr = "请您办理，请确认！";

                    if (!Pub.empty(memo))
                    {
                        if (memo.lastIndexOf("，") > 0 &&
                            memo.lastIndexOf("，") > memo.lastIndexOf("]"))
                        {
                            adscr = memo.substring(0, memo.lastIndexOf("，")) +
                                "，" + tempStr;
                        }
                        else
                            adscr = memo.substring(0, memo.length() - 2) + "，" +
                                tempStr;
                    }
                    else
                    {
                        adscr = Pub.getDictValueByCode("YWLX", ywlx) + "业务，";
                        adscr += tempStr;
                    }

                    vo = createNextApproveTask(conn, id, seq, dbdw, dbrole,
                            dbr, user);
                    vo.setSPBH(spbh);
                    vo.setSpyj(dscr);
                    vo.setSPJG(result);
                    vo.setSpr(user.getAccount());
                    vo.setDBRYID(dbr);
                    vo.setFbs("0");
                    vo.setDBDWDM("");
                    vo.setDbRole("");
                    vo.SetStepsequence(String.valueOf(step.getStepSequence()));
                    ApproveTask approveTask = new ApproveTask();
                    //vo = approveTask.RollbackTask(conn, vo, sjbh, ywlx);
                    //vo.setFbs(fbs);
                    if (vo != null)
                    	copyTask(conn,vo);
                    return vo;
                }
                else if (result.equals("1")) //同意
                {
                	//add by cbl start 重新生成设置ap_task_shedule表的memo信息
                	 String tempStrMemo = "请办理。";
                     String adscrMemo = null;
                     if (!Pub.empty(memo))
                     {
                         if (memo.lastIndexOf("，") > 0 && memo.lastIndexOf("，") > memo.lastIndexOf("]"))
                         {
                        	 adscrMemo = memo.substring(0, memo.lastIndexOf("，")) + "，" + tempStrMemo;
                         }
                         else
                         {
                        	 adscrMemo = memo.substring(0, memo.length() - 2)+ "，" + tempStrMemo;
                         }
                     }
                   //add by cbl end 重新生成设置ap_task_shedule表的memo信息
                	//是否特送下一级
                	if(!Tsnext){
                		//
                		if (proc.getState() == proc.AP_PROCESSING)
                        {
                            if(ProcessMgr.getStepValid(conn,step,id,seq,user.getAccount())||("1".equals(ccFlag)))//是否是多人处理节点判断是否全部办理
                            {
                            	doPrintTask(conn, id, seq,result, dscr, user);
                            }else
                            {
                            step = proc.getStepByIndex(step.getStepSequence() +1);
                            while(step.getState()==Process.AP_INVALID&&step.getState()==Process.AP_ERROR){
                                step = proc.getStepByIndex(step.getStepSequence() +1);
                            }
                            proc.rollbackTo(step.getStepSequence());
                            int nextdepttype = step.getDeptLevel();
//                            if (Pub.empty(dbdw))
//                                dbdw = step.getDeptID();
                            if (Pub.empty(dbr))
                                dbr = step.getActor();
                            if (Pub.empty(dbrole))
                                dbrole = step.getRole();

                            vo = createNextApproveTask(conn, id, seq, dbdw, dbrole,
                                dbr, user);
                            //重新设置memo start
                            vo.setMEMO(adscrMemo);
                            //重新设置memo end
                            vo.setSpyj(dscr);
                            vo.setSpr(user.getName());
                            vo.setSPJG(result);
                            vo.setFbs(fbs);
                            // add by guanchb 2008-05-06 start
                            // 审批同意时，不保存代办人员ID信息
                            // modified by hongpeng_dong 2009.9.10
                            // 可以选择人员,如果不选默认按角色过滤
                            //vo.setDBRYID(null);
                            vo.setDBRYID(Pub.empty(dbr)?null:dbr);
                            // add by guanchb 2008-05-06 end
                            vo.SetStepsequence(String.valueOf(step.getStepSequence()));
                            
                          //alter by cbl 发起审批后,获取ap_processdetail表的实例记录，更新此记录的超期日期维护到系统中
                            String SHEDULE_DAYS = step.getShedule_days();//获取超期天数
                            	if(!Pub.empty(SHEDULE_DAYS)){
                            		//计算超期日期，在当前工作日期后增加特定天数,取时间为当前日期
                       			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                       			WorkdayUtils workdayUtils = new WorkdayUtils();
                       			Date date = workdayUtils.getWorkday(new Date(), Integer.parseInt(SHEDULE_DAYS));//获取计算后的时间
                       			//转换为字符串更新到ap_processdetail表中
                       			String updatesql = "update AP_PROCESSDETAIL a  set a.SHEDULE_TIME = to_date('"+format.format(date)+" 23:59:59','yyyy-MM-dd hh24:mi:ss') where PROCESSOID = '"+step.getProcessOID()+"' and STEPSEQUENCE='"+step.getStepSequence()+"'";
                       			DBUtil.exec(conn, updatesql);
                       			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                       			vo.setShedule_time(sdf.parse(format.format(date)+" 23:59:59"));
                       		
                            	}
                            	boolean isms = false;
                           //alter by cbl end
                                //if(step!=null && "1".equals(step.getIsMS()))
                                {
                                	String actors = dbr;
                                	if(actors!=null &&actors.length() > 0)
                                	{
                                		isms = true;
                                		String s[] = actors.split(",");
                                		for(int k=0;k<s.length;k++)
                                		{
                                			
                                			vo.setDBRYID(s[k]);
                                			//vo.setDBDWDM("");
                                			//vo.setDbRole("");
                                			vo.setFbs("1");
                                			if(k==0)
                                			{
                                				saveTask(conn, vo);
                                			}
                                			else
                                			{
                                				copyTask(conn,vo);
                                			}
                                		}
                                	}
                                	
                                }
                                if(step!=null && "1".equals(step.getIsCC()))
                                {
                                	String actors = step.getCcActor();
                                	if(actors!=null &&actors.length() > 0)
                                	{
                                		String s[] = actors.split(",");
                                		for(int k=0;k<s.length;k++)
                                		{
                                			//TaskVO vo1 = vo;
                                			vo.setDBRYID(s[k]);
                                			//vo.setDBDWDM("");
                                			//vo.setDbRole("");
                                			vo.setFbs("0");
                                			if(k==0 && (null==step.getActor()||"".equals(step.getActor())))
                                			{
                                				saveTask(conn, vo);
                                			}
                                			else
                                			{
                                				copyTask(conn,vo);
                                			}
                                		}
                                	}
                                	
                                }
                            
                                if(step!=null&&"0".equals(step.getIsCC())&&!isms)
                                {
                        			vo.setDBDWDM("");
                        			vo.setFbs("1");
                        			//vo.setDbRole("");
                                	saveTask(conn, vo);
                                }
                            }

                        }
                        if (proc.getState() == proc.AP_SUCCESS)
                        {
                            //生成归档（打印）任务...
                            if(ProcessMgr.getStepValid(conn,step,id,seq,user.getAccount())||("1".equals(ccFlag)))//是否是多人处理节点判断是否全部办理
                            {
                            	doPrintTask(conn, id, seq,result, dscr, user);
                            }else
                            {
								Collection arr = getTasks(conn, id);
								if (arr == null)
									throw new Exception("无法创建审批默认任务！");
								TaskVO firstTask = (TaskVO) arr.toArray()[arr
										.size() - 1];
								String printTaskDbdwdm = getPrintTaskDbdwdm(
										conn, firstTask);
								if (Pub.empty(printTaskDbdwdm))
									throw new Exception(
											"无法创建审批默认任务，没有设置任务处理单位！");
								String adscr = null;
								String dlevel = Pub.getDictValueByCode("BMJB",
										user.getOrgDept().getDeptType());
								String tempStr = "审批通过";
								if (dlevel != null)
									tempStr = dlevel + tempStr;
								if (!Pub.empty(memo)) {
									if (memo.lastIndexOf("，") > 0
											&& memo.lastIndexOf("，") > memo
													.lastIndexOf("]")) {
										adscr = memo.substring(0,
												memo.lastIndexOf("，"))
												+ "，" + tempStr;
									} else
										adscr = memo.substring(0,
												memo.length() - 2)
												+ "，"
												+ tempStr;
								} else {
									adscr = Pub
											.getDictValueByCode("YWLX", ywlx) +

									"业务，";
									adscr += tempStr;
								}
								// vo = createDefaultTask(conn, sjbh, ywlx,
								// adscr,
								// printTaskDbdwdm, null, null,
								// user);
								// vo.setSPBH(spbh);
								// vo.setSPJG(result);
								// vo.setSpr(user.getName());
								// vo.setSpyj(dscr);
								// saveTask(conn, vo);
								vo = createPrintTask(conn, sjbh, ywlx, adscr,
										printTaskDbdwdm, null, null, user);
								vo.setSPBH(spbh);
								vo.setSPJG(result);
								vo.setSpr(user.getName());
								vo.setSpyj(dscr);
								ApproveTask approveTask = new ApproveTask();
								vo = approveTask.ArchiveTask(conn, vo, sjbh,
										ywlx);
								vo.setFbs(fbs);
								if(!Pub.empty(dbr)){
									vo.setDBRYID(dbr);
								}
								if (vo != null)
									saveTask(conn, vo);
								setEventState(conn, sjbh, ywlx,
										Globals.EVENT_STATE_APPROVE_SUCCESS);
								proc.finished(1);
								
								// 当是合同会签单审批通过时，发送给流程发起人提示信息。    add by xiahongbo by 2014-10-14
								if("700101".equals(ywlx)) {
									String hthqdName = adscr.substring(0, adscr.indexOf("】")+1);
									String fqr = vo.getDBRYID();
									String[] person = new String[1];
									person[0] = fqr;
						            PushMessage.push(conn, null, PushMessage.HT_SPTG, "合同会签单"+hthqdName+"已通过，将此合同会签单归档以后，再办理签订审核业务。", "url", sjbh, ywlx, "", person,"合同会签单已通过，请查看");
								}
                            }
                        }

                		String UpdateNextStepDetailSql = "UPDATE AP_PROCESSDETAIL SET ACTOR='"+dbr+"' WHERE PROCESSOID='"+spbh+"' AND STEPSEQUENCE='"+nextStep+"'";
//                		System.out.println("UpdateNextStepDetailSql : " + UpdateNextStepDetailSql);
                        DBUtil.executeUpdate(conn, UpdateNextStepDetailSql, null);
                	} else {

                        // 待办信息是办还是阅，此字段只在收文中用到。1：办，2：阅
                		String isRead = jo.getString("isRead");
                		
                		
                		if("1".equals(isRead)) {
                			// 工作联系单（自由报送）
                			if("000002".equals(ywlx) || "000003".equals(ywlx)) {
                				String fqr = DBUtil.query(conn, "select CJRID from AP_PROCESSINFO t where EVENTID = '"+sjbh+"'")[0][0];
                    			String isCjrSql = "select count(1) from AP_TASK_SCHEDULE t " 
                    					+ "where sjbh='"+sjbh+"' and t.dbryid='" + fqr + "' and wcsj is null";
                    			String count = DBUtil.query(conn, isCjrSql)[0][0];
                    			
                    			if(!fqr.equals((String)jo.get("TSPERSON"))) {
                    				vo = createNextApproveTask(conn, id, seq, (String)jo.get("TSDEPT"), "",
        	                        		(String)jo.get("TSPERSON"), user);
        	                        vo.setSpyj(dscr);
        	                        vo.setSpr(user.getName());
        	                        vo.setSPJG(result);
        	                        vo.setFbs(fbs);
        	                        
        	                        // 待办信息是办还是阅，此字段只在收文中用到。1：办，2：阅
        	                        vo.setXb(isRead);
        	                        
        							String actors = jo.getString("TSPERSON");
                                 	if(actors!=null &&actors.length() > 0)
                                 	{
                                 		String s[] = actors.split(",");
                                 		for(int k=0;k<s.length;k++)
                                 		{
                                 			vo.setDBRYID(s[k]);
                                 			vo.SetStepsequence("0");
                                 			vo.setDBDWDM("");
                                 			vo.setDbRole("");
                                 			//vo.setFbs("1");
                                 			if(k==0)
                                 			{
                                 				saveTask(conn, vo);
                                 			}
                                 			else
                                 			{
                                 				copyTask(conn,vo);
                                 			}
                                 		}
                                 	}
                    			} else if(fqr.equals((String)jo.get("TSPERSON")) && "0".equals(count)) {
                    				vo = createNextApproveTask(conn, id, seq, (String)jo.get("TSDEPT"), "",
        	                        		(String)jo.get("TSPERSON"), user);
        	                        vo.setSpyj(dscr);
        	                        vo.setSpr(user.getName());
        	                        vo.setSPJG(result);
        	                        vo.setFbs(fbs);
        	                        
        	                        // 待办信息是办还是阅，此字段只在收文中用到。1：办，2：阅
        	                        vo.setXb(isRead);
        	                        
        							String actors = jo.getString("TSPERSON");
                                 	if(actors!=null &&actors.length() > 0)
                                 	{
                                 		String s[] = actors.split(",");
                                 		for(int k=0;k<s.length;k++)
                                 		{
                                 			vo.setDBRYID(s[k]);
                                 			vo.SetStepsequence("0");
                                 			vo.setDBDWDM("");
                                 			vo.setDbRole("");
                                 			//vo.setFbs("1");
                                 			if(k==0)
                                 			{
                                 				saveTask(conn, vo);
                                 			}
                                 			else
                                 			{
                                 				copyTask(conn,vo);
                                 			}
                                 		}
                                 	}
								}
                			} else {
                				vo = createNextApproveTask(conn, id, seq, (String)jo.get("TSDEPT"), "",
    	                        		(String)jo.get("TSPERSON"), user);
    	                        vo.setSpyj(dscr);
    	                        vo.setSpr(user.getName());
    	                        vo.setSPJG(result);
    	                        vo.setFbs(fbs);
    	                        
    	                        // 待办信息是办还是阅，此字段只在收文中用到。1：办，2：阅
    	                        vo.setXb(isRead);
    	                        
    							String actors = jo.getString("TSPERSON");
                             	if(actors!=null &&actors.length() > 0)
                             	{
                             		String s[] = actors.split(",");
                             		for(int k=0;k<s.length;k++)
                             		{
                             			vo.setDBRYID(s[k]);
                             			vo.SetStepsequence("0");
                             			vo.setDBDWDM("");
                             			vo.setDbRole("");
                             			//vo.setFbs("1");
                             			if(k==0)
                             			{
                             				saveTask(conn, vo);
                             			}
                             			else
                             			{
                             				copyTask(conn,vo);
                             			}
                             		}
                             	}
                			}
                		} else {

                    		/***
                    	     * 收文的流程是：
                    	     * 发起人（1）——>办公室主任（2）——>发起人（3）——>发送多人（4）——>返回发起人（5）——>【反复步骤4和步骤5，直到发起人结束流程】
                    	     * 
                    	     * 多人发送时，需要所有人员的及节点全部办理完成，才能发送给发起人，否则不返回。
                    	     * 
                    	     * 此处的copy是先 判断事件编号（SJBH）和创建时间（CJSJ）的结束时间有几条记录，如果有一条，说明是多人审批中的最后一个人。
                    	     * 此人审批以后，返回给发起人；如果结束时间有多条记录，说明多人审批还没有全部结束。这样不返回发起人。
                    	     * 
                    	     */
                    		
                    		// 通过AP_TASK_SCHEDULE表的子关联方法，查询出没有办理的记录。如果结果等于0，说明此人是最后一个人，此人
                    		// 办理完成以后需要插入一条待办记录；否则此人不是最后一个人（还有其他人没有办理），则不向发起人发送待办记录
                    		String isLastDbrSql = "select count(*) cnt from AP_TASK_SCHEDULE t1,AP_TASK_SCHEDULE t2 "
                    				+ "where t1.sjbh=t2.sjbh and t1.spbh=t2.spbh and t1.ywlx=t2.ywlx and "
                    				+ "t2.id='" + id + "' and t2.seq='" + seq + "' and "
                    				+ "t1.rwzt='" + TaskVO.TASK_STATUS_VALID + "' and t1.dbryid!='" + user.getAccount() + "'";
                    		String[][] isLastDbrCnt = DBUtil.query(conn, isLastDbrSql);
                    		
                			if (!"0".equals(isLastDbrCnt[0][0])) {
                    			doPrintTask(conn, id, seq,result, dscr, user);
    						} else {
    							vo = createNextApproveTask(conn, id, seq, (String)jo.get("TSDEPT"), "",
    	                        		(String)jo.get("TSPERSON"), user);
    	                        vo.setSpyj(dscr);
    	                        vo.setSpr(user.getName());
    	                        vo.setSPJG(result);
    	                        vo.setFbs(fbs);
    	                        
    	                        // 待办信息是办还是阅，此字段只在收文中用到。1：办，2：阅
    	                        vo.setXb(isRead);
    	                        
    							String actors = jo.getString("TSPERSON");
                             	if(actors!=null &&actors.length() > 0)
                             	{
                             		String s[] = actors.split(",");
                             		for(int k=0;k<s.length;k++)
                             		{
                             			vo.setDBRYID(s[k]);
                             			vo.SetStepsequence("0");
                             			vo.setDBDWDM("");
                             			vo.setDbRole("");
                             			//vo.setFbs("1");
                             			if(k==0)
                             			{
                             				saveTask(conn, vo);
                             			}
                             			else
                             			{
                             				copyTask(conn,vo);
                             			}
                             		}
                             	}
    						}
                		}
                		
                		
                		//点击报送下一级审批,只处理待办信息
                        
                         
                   /*      if(step!=null && "1".equals(step.getIsMS()))
                         {*/
                         	
                         	
//                         }
                        // vo.setDBRYID(aDBRYID)
                         // add by guanchb 2008-05-06 start
                         // 审批同意时，不保存代办人员ID信息
                         // modified by hongpeng_dong 2009.9.10
                         // 可以选择人员,如果不选默认按角色过滤
                         //  vo.setDBRYID(null);
                         //  vo.setDBRYID(Pub.empty(dbr)?null:dbr);
                         // add by guanchb 2008-05-06 end
                        //saveTask(conn, vo);
                            //proc.close();
                	}
                	//add by cbl end
                    
                }else  if (result.equals("3")) //退回
                {
                    //生成审批不通过确认任务...
                    int tempStep = "temp".equals(jo.getString("stepsequence")) ? 0 : Integer.parseInt(jo.getString("stepsequence"));
                    
          //      	String stepsequence = jo.getString("stepsequence");
                	
          /*          String tempSql = "select t.stepsequence from AP_TASK_SCHEDULE t,AP_TASK_SCHEDULE d where t.sjbh=d.sjbh and t.spbh=d.spbh and t.ywlx=d.ywlx " +
                    				" and d.id ='"+id+"' and d.seq = '"+seq+"' and d.dbryid = '"+user.getAccount()+"' and t.dbryid = '"+dbr+"' and t.yxbs = '1'"+
                    				" and d.rwzt='"+TaskVO.TASK_STATUS_VALID+"' and t.rwzt='"+TaskVO.TASK_STATUS_EXECUTED+"'";
                    String res[][] = DBUtil.query(conn, tempSql);
                    if(res!=null&&res[0][0].length()>0)
                    {
                    	tempStep = Integer.parseInt(res[0][0]);
                    }*/
                    Step temp_step = proc.getStepByIndex(tempStep);
                    String tempStr = "被退回；请确认！";
                    if(!Pub.empty(memo)&&memo.indexOf(tempStr)>0) {
                        tempStr = "";
                    }
                    String adscr = null;
                    if (!Pub.empty(memo))
                    {
                        if (memo.lastIndexOf("，") > 0 && memo.lastIndexOf("，") > memo.lastIndexOf("]"))
                        {
                            adscr = memo.substring(0, memo.lastIndexOf("，")) + "，" + tempStr;
                        }
                        else
                        {
                            adscr = memo.substring(0, memo.length() - 2)+ "，" + tempStr;
                        }
                    }
                    else
                    {
                        adscr = Pub.getDictValueByCode("YWLX", ywlx) + "业务，";
                        adscr += tempStr;
                    }
                     if(temp_step == null)//无上一审批办理节点，退回给发起人
                     {
                         Collection arr = getTasks(conn, id);
                         if (arr == null)
                             throw new Exception("无法创建审批不通过的确认任务！");
                         TaskVO firstTask = (TaskVO) arr.toArray()[arr.size() - 1];
                         vo = createRollBackTask(conn, sjbh, ywlx, adscr,firstTask.getCjdwdm(), null, null,user);
                         vo.setSPBH(spbh);
                         vo.setSpyj(dscr);
                         vo.setSPJG(result);
                         vo.setSpr(user.getName());
                         ApproveTask approveTask = new ApproveTask();
                         vo = approveTask.RollbackTask(conn, vo, sjbh, ywlx);
                         
                         vo.setFbs("1");
                         if (vo != null)
                             saveTask(conn, vo);
                         setEventState(conn, sjbh, ywlx,Globals.EVENT_STATE_ROLLBACK);
                         //added by andy 20090109 退回发起人，更新审批文书审批状态为7，同时更新审批关联表中文书状态为7
                         String update_spws = " update pub_blob set spzt='7' where sjbh='"+sjbh+"' and ywlx='"+ywlx+"' and spzt='1' and zfbs='0' ";
                         //String update_glws = " update pub_blob set spzt='7' where fjbh in (select t1.fjbh from za_zfba_jcxx_ws_spckws t1,za_zfba_jcxx_ws_tycqbg t2 where t1.sjbh='"+sjbh+"' and t1.sjbh=t2.sjbh and t1.cqbgxh=t2.cqxh and t2.ywlx='"+ywlx+"' and t1.ws_template_id is not null and t1.zhux='0')";
                         DBUtil.executeUpdate(conn, update_spws, null);
                         //DBUtil.executeUpdate(conn, update_glws, null);
//                         proc.finished(0);
                     }else{//有上一办理审批节点，返回时考虑节点内特送情况
                         while(temp_step.getState()==Process.AP_INVALID&&temp_step.getState()==Process.AP_ERROR){
                             temp_step = proc.getStepByIndex(temp_step.getStepSequence() - 1);
                         }
                         proc.rollbackTo(temp_step.getStepSequence());
                         if(temp_step !=null){
                             step = temp_step;
                             //删除上一级办理人的签名和印章
                             //DBUtil.exec(conn,"delete from ws_electron_print where sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"' and APPROVEROLE = '"+step.getRole()+"' and APPROVELEVEL='"+step.getDeptLevel()+"'");
                             dbdw = step.getDeptID();
                             //dbr = step.getActor();
                             dbrole = step.getRole();
                             vo = createBackApproveTask(conn, id, seq ,adscr , dbdw, dbrole,dbr, user);
                             String SHEDULE_DAYS = step.getShedule_days();//获取超期天数
                         	if(!Pub.empty(SHEDULE_DAYS)){
                         		//计算超期日期，在当前工作日期后增加特定天数,取时间为当前日期
                    			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    			WorkdayUtils workdayUtils = new WorkdayUtils();
                    			Date date = workdayUtils.getWorkday(new Date(), Integer.parseInt(SHEDULE_DAYS));//获取计算后的时间
                    			//转换为字符串更新到ap_processdetail表中
                    			String updatesql = "update AP_PROCESSDETAIL a  set a.SHEDULE_TIME = to_date('"+format.format(date)+" 23:59:59','yyyy-MM-dd hh24:mi:ss') where PROCESSOID = '"+step.getProcessOID()+"' and STEPSEQUENCE='"+step.getStepSequence()+"'";
                    			DBUtil.exec(conn, updatesql);
                    			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    			vo.setShedule_time(sdf.parse(format.format(date)+" 23:59:59"));
                    		
                         	}
                             vo.SetStepsequence(String.valueOf(step.getStepSequence()));
                             vo.setDBDWDM("");
                             vo.setDbRole("");
                             vo.setSpyj(dscr);
                             vo.setSpr(user.getName());
                             vo.setSPJG(result);
                             vo.setFbs("1");
                             saveTask(conn, vo);
                             //proc.close();
                         }

                     }
                }
                if(Tsnext==true){
                	  sql = "update ap_task_schedule set RWZT='" +TaskVO.TASK_STATUS_EXECUTED +"'"
                              + ",WCSJ=to_date('"+Pub.getDate("yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS'),WCRID='" + user.getAccount() +
                              "',RESULT='" +result + "',RESULTDSCR=?,WCRXM='" +user.getName() + "',"
                              + "WCDWDM='" + user.getDepartment() + "' where sjbh='" + sjbh +"' and dbryid='" + user.getAccount() + "' and rwzt='01'";
                }else{
                	  sql = "update ap_task_schedule set RWZT='" +TaskVO.TASK_STATUS_EXECUTED +"'"
                              + ",WCSJ=to_date('"+Pub.getDate("yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS'),WCRID='" + user.getAccount() +
                              "',RESULT='" +result + "',RESULTDSCR=?,WCRXM='" +user.getName() + "',"
                              + "WCDWDM='" + user.getDepartment() + "' where id='" + id +"' and seq='" + seq + "'";
                }
                
              
                String objs[] = {dscr};
                DBUtil.executeUpdate(conn, sql, objs);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        return vo;
    }

    private String getPrintTaskDbdwdm(Connection conn, TaskVO vo)
    {
        try
        {
            String sldw = vo.getCjdwdm();
            String ywlx = vo.getYWLX();
            AppParaVO[] list = ParaManager.getInstance().getAppParameter(sldw,
                ywlx, Globals.PRINT_DBDW);
            if (list == null)
                list = ParaManager.getInstance().getAppParameter("0", ywlx,
                    Globals.PRINT_DBDW);
            if (list == null)
                return sldw;
            AppParaVO para = list[0];
            if (Pub.empty(para.getAppParaParavalue1()))
            {
                OrgDept dbdwdept = null,
                    sldwdept = OrgDeptManager.getInstance().getDeptByID(sldw);
                String sldwLevel = sldwdept.getDeptType();
                String dbdwLevel = para.getAppParaOrglevel();
                if (Pub.toInt(sldwLevel) > Pub.toInt(dbdwLevel))
                {
                    dbdwdept = sldwdept;
                    while (true)
                    {
                        dbdwdept = dbdwdept.getParent();
                        if (dbdwdept == null)
                            return null;
                        if (dbdwdept.getDeptType().equals(dbdwLevel))
                            return dbdwdept.getDeptID();
                    }
                }
                else if (Pub.toInt(sldwLevel) < Pub.toInt(dbdwLevel))
                { //分局受理，派出所打印
                    switch (Pub.toInt(dbdwLevel))
                    {
                        case 1:
                            return sldw.substring(0, 2) + "0000000000";
                        case 2:
                            return Pub.empty(vo.getSJDM()) ?
                                sldw.substring(0, 4) + "00000000" : vo.getSJDM();
                        case 3:
                            return Pub.empty(vo.getFJDM()) ?
                                sldw.substring(0, 6) + "000000" : vo.getFJDM();
                        case 4:
                        default:
                            return Pub.empty(vo.getPCSDM()) ? sldw :
                                vo.getPCSDM();
                    }
                }
                else
                    return sldw;
            }
            return para.getAppParaParavalue1();
        }
        catch (Exception e)
        {
            return null;
        }
    }

////////////////////////////////////////////////////////////////////////////////

    private String getDefaultOperation(Connection conn, TaskVO vo, User user)
    {
        try
        {
            ApproveRule[] rules = this.getApproveRule(conn, vo, user);
            return rules == null ? null : rules[0].getOperationOID();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Collection getAllTasks(String sjbh)
        throws Exception
    {
        /**@todo Complete this method*/
        return getTasks(sjbh, null, null);
    }
    public String showSelectPersion(String operationoid,User user) throws Exception{
    	   String html ="";
    	   Process process = null;
    	   Step step = null;
    	   Connection conn = DBUtil.getConnection();
    	   try {
    	    process = ProcessTypeMgr.getProcessByOperationOID(conn,operationoid);
    	    step = process.getFirstStep();
    	    String[][] t = DBUtil.query(conn, "select ywlx from ap_ws_typz where OPERATIONOID='"+operationoid+"'");
    	    String ywlx = "";
    	    if(t!=null&&t.length>0){
    	    	ywlx = t[0][0];
    	    }
    	    html = SelectPersonHtml(step,user,ywlx);
    	   } catch (Exception e) {
    			conn.rollback();
    	   } finally {
    			if (conn != null) {
    				conn.close();
    			}
    	   }
    	   return html;
    }
    
    
    public String SelectPersonHtml(Step step,User user,String ywlx){
		String html = "";
		String bslx = step.getIsMS();
		if(Pub.empty(bslx)){
			bslx = "1";
		}
		String bsDeptid = step.getDeptID();
		String bsRoleName = step.getRole();
		String actor = step.getActor();
		String actorName = "";
		if(actor!=null){
			actorName = Pub.getUserNameByLoginId(actor);
		}
		if ("1".equals(bslx)) {
			html +="<tr style=\"display:none\">";
			html +="<th class=\"right-border bottom-border\" style=\"font-size:14px;\">办理人</th>";
			html +="<td class=\"bottom-border right-border\"  colspan=\"7\">";
			html +="<input class=\"span12\"  type=\"text\"  code=\""+actor+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" value=\""+actorName+"\" disabled /></td></tr>";
		}else if ("2".equals(bslx)) {
			String sql = "";
			if (!Pub.empty(bsDeptid) && !Pub.empty(bsRoleName)) {
				sql = "select account,name from fs_org_person where DEPARTMENT='"
						+ bsDeptid
						+ "' and account in(select PERSON_ACCOUNT from fs_org_role_psn_map where ROLE_ID='"
						+ bsRoleName + "')";
			} else if (!Pub.empty(bsDeptid)) {
				sql = "select account,name from fs_org_person where DEPARTMENT='"
						+ bsDeptid + "' ";
			} else if (!Pub.empty(bsRoleName)) {
				sql = "select account,name from fs_org_person where account in(select PERSON_ACCOUNT from fs_org_role_psn_map where ROLE_ID='"
						+ bsRoleName + "')";
			}
			String[][] s = DBUtil.query(sql);
			String isshow = "";
			if (s != null && s.length == 1) {
				isshow = "none";
			}
			html +="<tr style=\"display:"+isshow+"\">";
			html +="<th class=\"right-border bottom-border\" style=\"font-size:14px;\">办理人</th>";
			html +="<td class=\"bottom-border right-border\"  colspan=\"7\">";
			html +="<select class=\"span12\"  type=\"text\" code=\""+actor+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\"  />" ;
			if(s!=null&&s.length>0){
				if(s.length>1){
					html +="<option value=''>请选择办理人</option>";
				}
				for(int i=0;i<s.length;i++){
					html +="<option value='"+s[i][0]+"'>"+s[i][1]+"</option>";
				}
			}
			html +=	"</select></td></tr>";

		}else if ("3".equals(bslx)) {
			html +="<tr >";
			html +="<th class=\"right-border bottom-border\" style=\"font-size:14px;\">办理人</th>";
			html +="<td class=\"bottom-border right-border\"  colspan=\"7\">";
			html +="<input class=\"span12\"  type=\"text\" dept=\""+user.getDepartment()+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled />" ;
			html +="<button class=\"btn btn-link\"  type=\"button\" id=\"blrBtn\" title=\"点击选择办理人\"><i class=\"icon-edit\"></i></button>";
			html +=	"</td></tr>";

		}if ("5".equals(bslx)) {
			
			if("200503".equals(ywlx)){//特殊判断，如果是发文则由办公室文书归档 add by wangzh
				String s = "";
				String n = "";
				String sql = "";
				if("100000000001".equals(user.getDepartment())){
					 sql = "select account,name from fs_org_person " +
								"where account in(select PERSON_ACCOUNT from fs_org_role_psn_map where ROLE_ID='1cf93796-b1cd-484a-8f7f-06c8c3b33895') and DEPARTMENT='"+user.getDepartment()+"'";
				}else{
					 sql = "select account,name from fs_org_person " +
								"where account in(select FZR from fs_org_dept  where ROW_ID='"+user.getDepartment()+"')";

				}
				
				String[][] t = DBUtil.query(sql);
				if (t != null && t.length > 0) {
					if(t.length==1){
						s = t[0][0];
						n = t[0][1];
						html +="<tr style=\"display:none\">";
						html +="<th class=\"right-border bottom-border\" style=\"font-size:14px;\">办理人</th>";
						html +="<td class=\"bottom-border right-border\"  colspan=\"7\">";
						html +="<input class=\"span12\"  type=\"text\"  code=\""+s+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" value=\""+n+"\" disabled /></td></tr>";
					}else{
						html +="<tr style=>";
						html +="<th class=\"right-border bottom-border\" style=\"font-size:14px;\">办理人</th>";
						html +="<td class=\"bottom-border right-border\"  colspan=\"7\">";
						html +="<select class=\"span12\"  type=\"text\" code=\""+actor+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\"  />" ;
							if(t.length>1){
								html +="<option value=''>请选择办理人</option>";
							}
							for(int i=0;i<t.length;i++){
								html +="<option value='"+t[i][0]+"'>"+t[i][1]+"</option>";
							}
						html +=	"</select></td></tr>";
					}
				}else{
					html +="<tr >";
					html +="<th class=\"right-border bottom-border\" style=\"font-size:14px;\">办理人</th>";
					html +="<td class=\"bottom-border right-border\"  colspan=\"7\">";
					html +="<input class=\"span12\"  type=\"text\" dept=\""+user.getDepartment()+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled />" ;
					html +="<button class=\"btn btn-link\"  type=\"button\" id=\"blrBtn\" title=\"点击选择办理人\"><i class=\"icon-edit\"></i></button>";
					html +=	"</td></tr>";
				}
				
			}else{
			
				String deptid = user.getDepartment();
				String[][] s = DBUtil.query("select fzr from fs_org_dept where row_id='"+deptid+"'");
				String fzr = "";
				String fzrName = "";
				if(s!=null&&s.length>0){
					fzr = s[0][0];
					fzrName = Pub.getUserNameByLoginId(fzr);
				}
				html +="<tr style=\"display:none\">";
				html +="<th class=\"right-border bottom-border\" style=\"font-size:14px;\">办理人</th>";
				html +="<td class=\"bottom-border right-border\"  colspan=\"7\">";
				html +="<input class=\"span12\"  type=\"text\"  code=\""+fzr+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" value=\""+fzrName+"\" disabled /></td></tr>";
			}
		}if ("6".equals(bslx)) {
			String deptid = user.getDepartment();
			String[][] s = DBUtil.query("select fgzr from fs_org_dept where row_id='"+deptid+"'");
			String fgzr = "";
			String fgzrName = null;
			if(s!=null&&s.length>0){
				fgzr = s[0][0];
				fgzrName = Pub.getUserNameByLoginId(fgzr);
			}
			html +="<tr style=\"display:none\">";
			html +="<th class=\"right-border bottom-border\" style=\"font-size:14px;\">办理人</th>";
			html +="<td class=\"bottom-border right-border\"  colspan=\"7\">";
			html +="<input class=\"span12\"  type=\"text\"  code=\""+fgzr+"\" value=\""+fgzrName+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td></tr>";
		}if ("7".equals(bslx)) {
			String deptid = step.getDeptID();
			boolean isselect = false;
			String [][] s = null;
			String fzr = "";

			if(Pub.empty(deptid)){
				isselect = true;
			}else{
				s = DBUtil.query("select fzr from fs_org_dept where row_id='"+deptid+"'");
				if(s!=null&&s.length>0){
					fzr = s[0][0];
				}
			}
			if(isselect == true){
				html +="<tr >";
				html +="<th class=\"right-border bottom-border\" style=\"font-size:14px;\">办理单位</th>";
				html +="<td class=\"bottom-border right-border\"  colspan=\"7\">";
				html +="<input class=\"hidden\"  type=\"text\"  fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled />" ;
				html +="<input class=\"span12\"  type=\"text\"  fieldname=\"DEPTID\" id=\"DEPTID\" name =\"DEPTID\" style=\"width:75%\" disabled />" ;
				html +="<button class=\"btn btn-link\"  type=\"button\" id=\"blbmBtn\" title=\"点击选择办理单位\"><i class=\"icon-edit\"></i></button>";
				html +=	"</td></tr>";

			}else{
				String fzrName = "";
				if(fzr!=null){
					fzrName = Pub.getUserNameByLoginId(fzr);
				}

				html +="<tr style=\"display:none\">";
				html +="<th class=\"right-border bottom-border\" style=\"font-size:14px;\">办理人</th>";
				html +="<td class=\"bottom-border right-border\"  colspan=\"7\">";
				html +="<input class=\"span12\"  type=\"text\"  code=\""+fzr+"\" value=\""+fzrName+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td></tr>";
			}
		}if ("8".equals(bslx)) {//同类别5
			String deptid = user.getDepartment();
			String[][] s = DBUtil.query("select fzr from fs_org_dept where row_id='"+deptid+"'");
			String fzr = "";
			String fzrName = "";
			if(s!=null&&s.length>0){
				fzr = s[0][0];
				fzrName = Pub.getUserNameByLoginId(fzr);
			}

			html +="<tr style=\"display:none\">";
			html +="<th class=\"right-border bottom-border\" style=\"font-size:14px;\">办理人</th>";
			html +="<td class=\"bottom-border right-border\"  colspan=\"7\">";
			html +="<input class=\"span12\"  type=\"text\"  code=\""+fzr+"\" value=\""+fzrName+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td></tr>";
		}if ("9".equals(bslx)) {//同类别5
			String deptid = user.getDepartment();
			String[][] s = DBUtil.query("select fgzr from fs_org_dept where row_id='"+deptid+"'");
			String fgzr = "";
			String fgzrName = "";
			if(s!=null&&s.length>0){
				fgzr = s[0][0];
				fgzrName = Pub.getUserNameByLoginId(fgzr);
			}

			html +="<tr style=\"display:none\">";
			html +="<th class=\"right-border bottom-border\" style=\"font-size:14px;\">办理人</th>";
			html +="<td class=\"bottom-border right-border\"  colspan=\"7\">";
			html +="<input class=\"span12\"  type=\"text\"  code=\""+fgzr+"\" value=\""+fgzrName+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td></tr>";
		}
		
		
    	return html;
    	
    }
    public String SelectPersonHtml_SPBL(String operation,int seq,String spbh,User user) throws Exception{
		String html = "";
	   Process process = null;
  	   Step step = null;
  	   Connection conn = DBUtil.getConnection();
  	   try {
  	    process = ProcessTypeMgr.getProcessByOperationOID(conn,operation);
  	    step = process.getStepByIndex(seq);
  	 
  	   } catch (Exception e) {
  			conn.rollback();
  	   } finally {
  			if (conn != null) {
  				conn.close();
  			}
  	   }
		if (step != null) {
			String bslx = step.getIsMS();
			if (Pub.empty(bslx)) {
				bslx = "4";
			}
			//String spbh = step.getProcessOID();
			String bsDeptid = step.getDeptID();
			String bsRoleName = step.getRole();
			String actor = step.getActor();
			String actorName = "";
			if(actor!=null){
				actorName = Pub.getUserNameByLoginId(actor);
			}
			if ("1".equals(bslx)) {
				html += "<th style=\"display:none\" align=\"center\" valign=\"middle\" style=\"font-size:14px;vertical-align:middle;\">办理人选择</th>";
				html += "<td style=\"display:none\" style=\"font-size:14px;vertical-align:middle;\" id=\"task_dbrid\">";
				html += "<input class=\"span12\"  type=\"text\"  code=\""
						+ actor
						+ "\" value=\""+actorName+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td>";
			} else if ("2".equals(bslx)) {
				
				String stepRoleName = bsRoleName;
				//对于发文的最后两个节点单独处理，分管副主任角色要包含常务副主任和主管主任，主管主任角色要包括常务副主任和主管主任
				if("bf565585-35fc-4cd8-8e48-25ed4da9b071".equals(bsRoleName)||"e9635cef-e899-441e-a76f-384b37e92107".equals(bsRoleName)){
					String ywlx = "";
					String[][] y = DBUtil.query("select value3 from ap_processinfo where PROCESSOID = '"+spbh+"'");
					if(y!=null&&y.length>0){
						ywlx = y[0][0];
					}
					if ("200503".equals(ywlx)) {//特殊判断，如果是发文则由办公室文书归档 add by wangzh
					    String sql = "";
						if("bf565585-35fc-4cd8-8e48-25ed4da9b071".equals(bsRoleName)){
							bsRoleName = "'bf565585-35fc-4cd8-8e48-25ed4da9b071','e9635cef-e899-441e-a76f-384b37e92107'";//分管主任+主管主任
						}else{
							bsRoleName = "'219d48ca-866c-4bfd-a02a-613d15ee4c94','e9635cef-e899-441e-a76f-384b37e92107'";//常务副主任+主管主任
						}
						sql = "select distinct (account),name,a.sort from fs_org_person a ,(select PERSON_ACCOUNT from fs_org_role_psn_map where ROLE_ID in("
								+ bsRoleName + "))  b where a.account = b.PERSON_ACCOUNT order by to_number(a.sort) asc";
						String[][] s = DBUtil.query(sql);
						String isshow = "";
						if (s != null && s.length == 1) {
							isshow = "none";
						}
						// 发文归档人（办公室文书）
						if("e9635cef-e899-441e-a76f-384b37e92107".equals(stepRoleName)){
							String user_role = user.getRoleListIdString();
							if(user_role.indexOf("219d48ca-866c-4bfd-a02a-613d15ee4c94")>-1||user_role.indexOf("e9635cef-e899-441e-a76f-384b37e92107")>-1){
								String sql1 = "select account,name from fs_org_person " +
										"where account in(select PERSON_ACCOUNT from fs_org_role_psn_map where ROLE_ID='34604923-f6b3-4ca7-a367-e1c361e55e99') order by to_number(sort) asc";
								String[][] t = DBUtil.query(sql1);
								String s1 = "";
								String n1 = "";
								if (t != null && t.length > 0) {
									s1 = t[0][0];
									n1 = t[0][1];
								}
								html += "<th style=\"display:none\" align=\"center\" valign=\"middle\" style=\"font-size:14px;vertical-align:middle;\">办理人选择</th>";
								html += "<td style=\"display:none\" style=\"font-size:14px;vertical-align:middle;\" id=\"task_dbrid\">";
								html += "<input class=\"span12\"  type=\"text\"  code=\""
										+ s1
										+ "\" value=\""+n1+"\" fieldname=\"ACTOR\"  id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td>";
								return html;
							}
						}
						
						
						html += "<th style=\"text-align:center;font-size:14px;width:15%\" style=\"display:"+ isshow
								+ "\" align=\"center\" valign=\"middle\" >办理人选择</th>";
						html += "<td style=\"display:" + isshow
								+ "\" style=\"font-size:14px;vertical-align:middle;\" colspan=2>";
						html += "<select class=\"span12\"  type=\"text\" code=\""
								+ actor
								+ "\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\"  />";
						if (s != null && s.length > 0) {
							if (s.length > 1) {
								html += "<option value=''>请选择</option>";
							}
							for (int i = 0; i < s.length; i++) {
								html += "<option value='" + s[i][0] + "'>" + s[i][1]
										+ "</option>";
							}
						}
						html += "</select></td>";
						return html;
					}
					
				}
				

				String sql = "";
				if (!Pub.empty(bsDeptid) && !Pub.empty(bsRoleName)) {
					sql = "select account,name from fs_org_person where DEPARTMENT='"
							+ bsDeptid
							+ "' and account in(select PERSON_ACCOUNT from fs_org_role_psn_map where ROLE_ID='"
							+ bsRoleName + "')";
				} else if (!Pub.empty(bsDeptid)) {
					sql = "select account,name from fs_org_person where DEPARTMENT='"
							+ bsDeptid + "' ";
				} else if (!Pub.empty(bsRoleName)) {
					sql = "select account,name from fs_org_person where account in(select PERSON_ACCOUNT from fs_org_role_psn_map where ROLE_ID='"
							+ bsRoleName + "')";
				}
				String[][] s = DBUtil.query(sql);
				String isshow = "";
				if (s != null && s.length == 1) {
					isshow = "style=\"display:none";
				}
				html += "<th " + isshow + "\" align=\"center\" valign=\"middle\" style=\"font-size:14px;vertical-align:middle;\">办理人选择</th>";
				html += "<td " + isshow + "\" style=\"font-size:14px;vertical-align:middle;\" id=\"task_dbrid\">";
				html += "<select class=\"span12\"  type=\"text\" code=\""
						+ actor
						+ "\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\"  />";
				if (s != null && s.length > 0) {
					if (s.length > 1) {
						html += "<option value=''>请选择</option>";
					}
					for (int i = 0; i < s.length; i++) {
						html += "<option value='" + s[i][0] + "'>" + s[i][1]
								+ "</option>";
					}
				}
				html += "</select></td>";

			} else if ("3".equals(bslx)) {
				String deptSx = "";
				if(!"43481".equals(operation)) {
					deptSx = "dept=\""+user.getDepartment()+"\"";
				}
				html += "<th align=\"center\" valign=\"middle\" style=\"font-size:14px;vertical-align:middle;\">办理人选择</th>";
				html += "<td style=\"display:\" style=\"font-size:14px;vertical-align:middle;\" id=\"task_dbrid\">";
				html += "<input class=\"span12\"  type=\"text\" "+deptSx+" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled />";
				html += "<button class=\"btn btn-link\"  type=\"button\" id=\"blrBtn\" title=\"点击选择办理人\"><i class=\"icon-edit\"></i></button>";
				html += "</td>";
			}
			if ("4".equals(bslx)) {
				// step.get
				String[][] t = DBUtil
						.query("select * from (select cjrid from ap_task_schedule where spbh ='"
								+ spbh
								+ "' order by cjsj asc ) where rownum<=1 ");
				String s = "";
				String s_name = "";
				if (t != null && t.length > 0){
					s = t[0][0];
					s_name = Pub.getUserNameByLoginId(s);
				}

				html += "<th style=\"display:none\" align=\"center\" valign=\"middle\" style=\"font-size:14px;vertical-align:middle;\">办理人选择</th>";
				html += "<td style=\"display:none\" style=\"font-size:14px;vertical-align:middle;\" id=\"task_dbrid\">";
				html += "<input class=\"span12\"  type=\"text\"  code=\""
						+ s
						+ "\" value=\""+s_name+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td>";
			}
			if ("5".equals(bslx)) {
				String deptid = user.getDepartment();
				String[][] s = DBUtil.query("select fzr from fs_org_dept where row_id='"+deptid+"'");
				String fzr = "";
				String fzrName = "";
				if(s!=null&&s.length>0){
					fzr = s[0][0];
					fzrName =  Pub.getUserNameByLoginId(fzr);
				}
				html += "<th style=\"display:none\" align=\"center\" valign=\"middle\" style=\"font-size:14px;vertical-align:middle;\">办理人选择</th>";
				html += "<td style=\"display:none\" style=\"font-size:14px;vertical-align:middle;\" id=\"task_dbrid\">";
				html += "<input class=\"span12\"  type=\"text\"  code=\""
						+ fzr
						+ "\" value=\""+fzrName+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td>";
			}if ("6".equals(bslx)) {
				String deptid = user.getDepartment();
				if(!Pub.empty(bsDeptid)){
					deptid = bsDeptid;
				}
				String[][] s = DBUtil.query("select fgzr from fs_org_dept where row_id='"+deptid+"'");
				String fgzr = "";
				String fgzrName = "";
				if(s!=null&&s.length>0){
					fgzr = s[0][0];
					fgzrName =  Pub.getUserNameByLoginId(fgzr);
				}
				html += "<th style=\"display:none\" align=\"center\" valign=\"middle\" style=\"font-size:14px;vertical-align:middle;\">办理人选择</th>";
				html += "<td style=\"display:none\" style=\"font-size:14px;vertical-align:middle;\" id=\"task_dbrid\">";
				html += "<input class=\"span12\"  type=\"text\"  code=\""
						+ fgzr
						+ "\" value=\""+fgzrName+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td>";
			}if ("7".equals(bslx)) {
				String deptid = step.getDeptID();
				boolean isselect = false;
				String [][] s = null;
				String fgzr = "";

				if(Pub.empty(deptid)){
					isselect = true;
				}else{
					s = DBUtil.query("select fzr from fs_org_dept where row_id='"+deptid+"'");
					if(s!=null&&s.length>0){
						fgzr = s[0][0];
					}
				}
				if(isselect == true){
					String processoid = process.getOperationOID();
					String type = "single";
					if("43478".equals(processoid))
					{
						type = "multi";
					}
					
					html += "<th  align=\"center\" valign=\"middle\" style=\"font-size:14px;vertical-align:middle;\">部门负责人选择</th>";
					html += "<td style=\"display:none\" style=\"font-size:14px;vertical-align:middle;\">";
					html += "<input class=\"span12\"  type=\"text\"  code=\""
							+ fgzr
							+ "\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td>";
					html += "<td style=\"display:\" style=\"font-size:14px;vertical-align:middle;\" id=\"task_dbrid\">";
					html += "<input class=\"span12\"  type=\"text\"  fieldname=\"DEPTID\" id=\"DEPTID\" name =\"DEPTID\" style=\"width:85%\" disabled />";
					html += "<button class=\"btn btn-link\"  type=\"button\" id=\"blbmBtn\" selectType = \""+type+"\" title=\"点击选择部门\"><i class=\"icon-edit\"></i></button>";
					html += "</td>";

				}else{
					String fgzrName = "";
					if(fgzr!=null){
						fgzrName =Pub.getUserNameByLoginId(fgzr);
					}
					html += "<th style=\"display:none\" align=\"center\" valign=\"middle\" style=\"text-align:center;font-size:14px;\">部门负责人选择</th>";
					html += "<td style=\"display:none\" style=\"font-size:14px;vertical-align:middle;\" id=\"task_dbrid\">";
					html += "<input class=\"span12\"  type=\"text\"  code=\""
							+ fgzr
							+ "\" value=\""+fgzrName+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td>";
				}

			}if ("8".equals(bslx)) {
				// step.get
				String[][] t = DBUtil
						.query("select * from (select cjrid from ap_task_schedule where spbh ='"
								+ spbh
								+ "' order by cjsj asc ) where rownum<=1 ");
				String fqr = "";
				if (t != null && t.length > 0)
					fqr = t[0][0];
				String [][] s = null;
				String fzr = "";
				String fzrName = "";
				if(!Pub.empty(fqr)){
					s = DBUtil.query("select fzr from fs_org_dept where row_id in(select DEPARTMENT from fs_org_person where account='"+fqr+"')");
					if(s!=null&&s.length>0){
						fzr = s[0][0];
						fzrName =  Pub.getUserNameByLoginId(fzr);
					}
				}
				html += "<th style=\"display:none\" align=\"center\" valign=\"middle\" style=\"font-size:14px;vertical-align:middle;\">办理人选择</th>";
				html += "<td style=\"display:none\" style=\"font-size:14px;vertical-align:middle;\" id=\"task_dbrid\">";
				html += "<input class=\"span12\"  type=\"text\"  code=\""
						+ fzr
						+ "\" value=\""+fzrName+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td>";
			}if ("9".equals(bslx)) {
				String ywlx = "";
				String sjbh = "";
				String[][] y = DBUtil.query("select value3,EVENTID from ap_processinfo where PROCESSOID = '"+spbh+"'");
				if(y!=null&&y.length>0){
					ywlx = y[0][0];
					sjbh = y[0][1];
				}
				
				// step.get
				String[][] t = DBUtil
						.query("select * from (select cjrid from ap_task_schedule where spbh ='"
								+ spbh
								+ "' order by cjsj asc ) where rownum<=1 ");
				String fqr = "";
				if (t != null && t.length > 0)
					fqr = t[0][0];
				String [][] s = null;
				String fgzr = "";
				String fgzrName = "";
				if("700204".equals(ywlx)){
					String sql_fq = "SELECT d.fgzr FROM gc_zjgl_tqkbm   gzt, gc_zjgl_tqkbmmx gzt2, gc_zjgl_tqkmx   gzt3, gc_zjgl_tqk     gzt4,fs_org_dept d WHERE gzt.ID = gzt2.tqkid AND gzt2.ID = gzt3.bmmxid AND gzt3.tqkid = gzt4.ID and d.row_id = gzt.sqdw and gzt4.sjbh = '"+sjbh+"'"; 
					s = DBUtil.query(sql_fq);
					if(s!=null&&s.length>0){
						fgzr = s[0][0];
						fgzrName =  Pub.getUserNameByLoginId(fgzr);
					}
				}else{
					if(!Pub.empty(fqr)){
						s = DBUtil.query("select fgzr from fs_org_dept where row_id in(select DEPARTMENT from fs_org_person where account='"+fqr+"')");
						if(s!=null&&s.length>0){
							fgzr = s[0][0];
							fgzrName =  Pub.getUserNameByLoginId(fgzr);
						}
					}
				}
				
				
				
				html += "<th style=\"display:none\" align=\"center\" valign=\"middle\" style=\"font-size:14px;vertical-align:middle;\">办理人选择</th>";
				html += "<td style=\"display:none\" style=\"font-size:14px;vertical-align:middle;\" id=\"task_dbrid\">";
				html += "<input class=\"span12\"  type=\"text\"  code=\""
						+ fgzr
						+ "\" value=\""+fgzrName+"\" fieldname=\"ACTOR\" id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td>";
			}
			
			
		}else{
			String s = "";
			String n = "";
			String ywlx = "";
			String[][] y = DBUtil.query("select value3 from ap_processinfo where PROCESSOID = '"+spbh+"'");
			if(y!=null&&y.length>0){
				ywlx = y[0][0];
			}
			if ("200503".equals(ywlx)) {//特殊判断，如果是发文则由办公室文书归档 add by wangzh
				String sql = "select account,name from fs_org_person " +
						"where account in(select PERSON_ACCOUNT from fs_org_role_psn_map where ROLE_ID='34604923-f6b3-4ca7-a367-e1c361e55e99')";
				String[][] t = DBUtil.query(sql);
				if (t != null && t.length > 0) {
					s = t[0][0];
					n = t[0][1];
				}

			} else {

				String[][] t = DBUtil.query("select CJRID from AP_PROCESSINFO t where processoid = '"+spbh+"'");
				if (t != null && t.length > 0) {
					s = t[0][0];
					n = Pub.getUserNameByLoginId(s);
				}
			}
			
			html += "<th style=\"display:none\" align=\"center\" valign=\"middle\" style=\"font-size:14px;vertical-align:middle;\">办理人选择</th>";
			html += "<td style=\"display:none\" style=\"font-size:14px;vertical-align:middle;\" id=\"task_dbrid\">";
			html += "<input class=\"span12\"  type=\"text\"  code=\""
					+ s
					+ "\" value=\""+n+"\" fieldname=\"ACTOR\"  id=\"ACTOR\" name =\"ACTOR\" style=\"width:75%\" disabled /></td>";
		}
		
    	return html;
    	
    }
    
}
