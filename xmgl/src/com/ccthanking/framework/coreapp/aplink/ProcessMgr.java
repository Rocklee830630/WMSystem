package com.ccthanking.framework.coreapp.aplink;

import java.util.*;
import java.sql.SQLException;
import java.sql.Connection;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;

public class ProcessMgr
{
    public static Collection createProcess(Connection conn,String operationOID,TaskVO task,User user)
      throws Exception
  {
      try
      {
          String sql = null;
          //创建审批流程记录
          sql = "insert into ap_processinfo cols(PROCESSOID,PROCESSTYPEOID,CREATETIME,EVENTID,STATE,OPERATIONOID,MEMO,PROCESSEVENT,TASKOID,YXBS,VALUE1,VALUE2,VALUE3,VALUE4,CJRID,CJDWDM)"
              + " select '"+new RandomGUID().toString()+"',PROCESSTYPEOID,sysdate,'"+task.getSJBH()+"',"+Process.AP_CREATED+",OPERATIONOID,'"+task.getMEMO()+"'"
              + ",PROCESSEVENT,'"+task.getID()+"','"+task.getFbs()+"','"+task.getValue1()+"','"+task.getValue2()+"','"+task.getYWLX()+"','"+task.getValue4()+"','"+task.getCJRID()+"','"+task.getCjdwdm()+"' from ap_processtype where OPERATIONOID='"+operationOID+"' and STATE='"+Process.AP_CREATED+"'";
          DBUtil.execSql(conn, sql);
          //创建审批流程的具体节点
          /*
           * 通过配置表fs_para_app_configure获得办理日期的时限，如果从配置中没获得值或返回为0，则取ap_processstep 的 字段SHEDULE_DAYS
           * 
           */
          String s_field = "s.SHEDULE_DAYS";
          String[][] s = DBUtil.query(conn, "select PARAVALUE1 from fs_para_app_configure where OPERATIONTYPE='"+task.getYWLX()+"' and PARAKEY='SPSSPZ'");
          if(s!=null&&s.length>0){
        	  Object[] para = new Object[2];
        	  para[0] = task.getSJBH();
        	  para[1] = task.getYWLX();
        	 String[][] ss =  DBUtil.querySql(conn, s[0][0], para);
        	 if(ss!=null&&ss.length>0)
        	 {
        		 if(!"0".equals(ss[0][0])){
        			 s_field = ss[0][0];
        		 }
        	 }
          }
          
          sql = "insert into ap_processdetail cols(PROCESSOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,createtime,PRESTEPOID,NEXTSTEPOID,PRECONDITION2,PRECONDITION3,PRECONDITION1,DEPTID,PROCESSTYPEOID,deptlevel,APPLICATION,MEMO,STEPEVENT,TASKOID,SHEDULE_DAYS,ISMS,ISCC,ISWP,CCACTOR,ISTH) "
              +" select p.processoid,s.STEPSEQUENCE,s.STEPOID,s.NAME,s.ROLENAME,s.ACTOR,s.STATE,sysdate,s.PRESTEPOID,s.NEXTSTEPOID,"
              +" s.PRECONDITION2,s.PRECONDITION3,s.PRECONDITION1,s.DEPTID,s.PROCESSTYPEOID,s.deptlevel,"
              +" s.APPLICATION,s.MEMO,s.STEPEVENT,p.TASKOID,"+s_field+",s.isMS,s.isCC,s.isWP,s.ccActor,s.ISTH from ap_processstep s,ap_processinfo p"
              +" where s.PROCESSTYPEOID=p.processtypeoid and s.STATE!="+Step.AS_INVALID+" and p.TASKOID='"+task.getID()
              +"' and p.state="+Process.AP_CREATED;
          DBUtil.execSql(conn,sql);
          sql = "select PROCESSOID,PROCESSTYPEOID,to_char(CREATETIME,'yyyymmddhh24miss'),EVENTID,STATE,OPERATIONOID,MEMO,PROCESSEVENT,TASKOID from ap_processinfo p"
              +" where p.TASKOID='"+task.getID()+"' and p.state="+Process.AP_CREATED;
          String[][] list = DBUtil.query(conn,sql);
          if(list == null) throw new Exception("create process error!");
          ArrayList arr = new ArrayList(list.length);
          for(int i=0;i<list.length;i++)
          {
            ProcessInfo proc = new ProcessInfo(conn);
            proc.setProcessOID(list[i][0]);
            proc.setProcessTypeOID(list[i][1]);
            proc.setCreateTime(Pub.toDate("yyyyMMddHHmmss",list[i][2]));
            proc.setCreatedEventID(list[i][3]);
            proc.setState(Pub.toInt(list[i][4]));
            proc.setOperationOID(list[i][5]);
            proc.setMemo(list[i][6]);
            proc.setProcessEvent(list[i][7]);
            proc.setTaskOID(list[i][8]);
            arr.add(i,proc);
          }
          return arr;
      }
      catch (Exception e)
      {
          e.printStackTrace(System.out);
          throw e;
      }
    }
/*
 * 创建特送流程对象方法
 */
    public static Collection createProcessTs(Connection conn,String operationOID,User user,String dscr,String sjbh,String value1,String value2,String value3,String value4)
    	      throws Exception
    	  {
    	      try
    	      {
    	          String sql = null;
    	          String vid = new RandomGUID().toString();
    	          //创建审批流程记录
    	          sql = "insert into ap_processinfo cols(PROCESSOID,PROCESSTYPEOID,CREATETIME,EVENTID,STATE,OPERATIONOID,MEMO,PROCESSEVENT,TASKOID,YXBS,VALUE1,VALUE2,VALUE3,VALUE4,CJRID,CJDWDM)"
    	              + " select '"+new RandomGUID().toString()+"',PROCESSTYPEOID,sysdate,'"+sjbh+"',"+Process.AP_CREATED+",OPERATIONOID,'"+dscr+"'"
    	              + ",PROCESSEVENT,'"+vid+"','','','','','','"+user.getAccount()+"','"+user.getDepartment()+"' from ap_processtype where OPERATIONOID='"+operationOID+"' and STATE='"+Process.AP_CREATED+"'";
    	          DBUtil.execSql(conn, sql);
    	          //创建审批流程的具体节点
    	          sql = "insert into ap_processdetail cols(PROCESSOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,createtime,PRESTEPOID,NEXTSTEPOID,PRECONDITION2,PRECONDITION3,PRECONDITION1,DEPTID,PROCESSTYPEOID,deptlevel,APPLICATION,MEMO,STEPEVENT,TASKOID,SHEDULE_DAYS) "
    	              +" select p.processoid,s.STEPSEQUENCE,s.STEPOID,s.NAME,s.ROLENAME,s.ACTOR,s.STATE,sysdate,s.PRESTEPOID,s.NEXTSTEPOID,"
    	              +" s.PRECONDITION2,s.PRECONDITION3,s.PRECONDITION1,s.DEPTID,s.PROCESSTYPEOID,s.deptlevel,"
    	              +" s.APPLICATION,s.MEMO,s.STEPEVENT,p.TASKOID,s.SHEDULE_DAYS from ap_processstep s,ap_processinfo p"
    	              +" where s.PROCESSTYPEOID=p.processtypeoid and s.STATE!="+Step.AS_INVALID+" and p.TASKOID='"+vid
    	              +"' and p.state="+Process.AP_CREATED;
    	          DBUtil.execSql(conn,sql);
    	          sql = "select PROCESSOID,PROCESSTYPEOID,to_char(CREATETIME,'yyyymmddhh24miss'),EVENTID,STATE,OPERATIONOID,MEMO,PROCESSEVENT,TASKOID from ap_processinfo p"
    	              +" where p.TASKOID='"+vid+"' and p.state="+Process.AP_CREATED;
    	          String[][] list = DBUtil.query(conn,sql);
    	          if(list == null) throw new Exception("create process error!");
    	          ArrayList arr = new ArrayList(list.length);
    	          for(int i=0;i<list.length;i++)
    	          {
    	            ProcessInfo proc = new ProcessInfo(conn);
    	            proc.setProcessOID(list[i][0]);
    	            proc.setProcessTypeOID(list[i][1]);
    	            proc.setCreateTime(Pub.toDate("yyyyMMddHHmmss",list[i][2]));
    	            proc.setCreatedEventID(list[i][3]);
    	            proc.setState(Pub.toInt(list[i][4]));
    	            proc.setOperationOID(list[i][5]);
    	            proc.setMemo(list[i][6]);
    	            proc.setProcessEvent(list[i][7]);
    	            proc.setTaskOID(list[i][8]);
    	            arr.add(i,proc);
    	          }
    	          return arr;
    	      }
    	      catch (Exception e)
    	      {
    	          e.printStackTrace(System.out);
    	          throw e;
    	      }
    	    }
    public static Process createProcess(Connection conn,String processTypeOID,String taskOID,String eventID)
      throws Exception
  {
      try
      {
          ProcessInfo proc = new ProcessInfo(conn);
          proc.setCreatedEventID(eventID);
          proc.setState(Process.AP_CREATED);
          proc.setCreateTime(Pub.getCurrentDate());
          String sql = "select a.PROCESSTYPEOID,a.NAME,a.OPERATIONOID,a.PRECONDITION1,a.PRECONDITION2,a.PRECONDITION3,a.MEMO,PROCESSEVENT"
              + " from ap_processtype a where a.PROCESSTYPEOID='" +
              processTypeOID + "'";
          String[][] list = DBUtil.query(conn,sql);
          proc.setProcessTypeOID(list[0][0]);
          proc.setMemo(list[0][6]);
          proc.setOperationOID(list[0][2]);
          proc.setTaskOID(taskOID);
          proc.setProcessEvent(list[0][7]);
          sql = "select ap_task_s.nextval from dual";
          String processid = DBUtil.query(conn,sql)[0][0];
          proc.setProcessOID(processid);

          sql = "insert into ap_processinfo cols(PROCESSOID,PROCESSTYPEOID,CREATETIME,EVENTID,STATE,OPERATIONOID,MEMO,PROCESSEVENT,TASKOID)"
              + " values(" + processid + "," + proc.getProcessTypeOID() +
              ",sysdate,'" + eventID + "'," + proc.getState()
              + ",'" + proc.getOperationOID() + "','" + proc.getMemo() + "','"+proc.getProcessEvent()+"','"+proc.getTaskOID()+"')";
          DBUtil.execSql(conn, sql);
          sql = "insert into ap_processdetail cols(PROCESSOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,PRESTEPOID,NEXTSTEPOID,PRECONDITION2,PRECONDITION3,PRECONDITION1,DEPTID,PROCESSTYPEOID,deptlevel,APPLICATION,MEMO,STEPEVENT,TASKOID) "
              + " select '" + processid + "',STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,PRESTEPOID,NEXTSTEPOID,PRECONDITION2,PRECONDITION3,PRECONDITION1,DEPTID,PROCESSTYPEOID,deptlevel,APPLICATION,MEMO,STEPEVENT,'"+proc.getTaskOID()+"' from ap_processstep where PROCESSTYPEOID='" +
              proc.getProcessTypeOID() + "' and STATE!="+Step.AS_INVALID;
          DBUtil.execSql(conn,sql);
          return proc;
      }
      catch (Exception e)
      {
          e.printStackTrace(System.out);
          throw e;
      }
    }

	public static Process createProcess(Connection conn,String eventID, String eventType)
      throws Exception
  {
		try
		{
			ProcessInfo proc = new ProcessInfo(conn);
			proc.setCreatedEventID(eventID);
			proc.setState(1);
			proc.setCreateTime(Pub.getCurrentDate());
			String sql = "select a.PROCESSTYPEOID,a.NAME,a.OPERATIONOID,a.PRECONDITION1,a.PRECONDITION2,a.PRECONDITION3,a.MEMO"
				+ ",a.PROCESSEVENT from ap_processtype a, ap_tasks b where a.OPERATIONOID=b.OPERATIONOID and b.EVENTTYPE='" +
				eventType + "'";
			String[][] list = DBUtil.query(sql);
			proc.setProcessTypeOID(list[0][0]);
			proc.setMemo(list[0][6]);
            proc.setProcessEvent(list[0][7]);
			proc.setOperationOID(list[0][2]);
			sql = "select ap_task_s.nextval from dual";
			String processid = DBUtil.query(sql)[0][0];
			proc.setProcessOID(processid);

			sql = "insert into ap_processinfo cols(PROCESSOID,PROCESSTYPEOID,CREATETIME,EVENTID,STATE,OPERATIONOID,MEMO,PROCESSEVENT)"
				+ " values(" + processid + "," + proc.getProcessTypeOID() +
				",sysdate,'" + eventID + "'," + proc.getState()
				+ "," + proc.getOperationOID() + ",'" + proc.getMemo() + "','"+proc.getProcessEvent()+"')";
			DBUtil.execSql(conn, sql);
			sql = "insert into ap_processdetail cols(PROCESSOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,PRESTEPOID,NEXTSTEPOID,PRECONDITION2,PRECONDITION3,PRECONDITION1,DEPTID,PROCESSTYPEOID,deptlevel,APPLICATION,MEMO,STEPEVENT) "
				+ " select " + processid + ",STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,PRESTEPOID,NEXTSTEPOID,PRECONDITION2,PRECONDITION3,PRECONDITION1,DEPTID,PROCESSTYPEOID,deptlevel,APPLICATION,MEMO,STEPEVENT from ap_processstep where PROCESSTYPEOID='" +
				proc.getProcessTypeOID() + "' and STATE!=2";
			DBUtil.execSql(conn,sql);
			return proc;
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			throw e;
		}
	}

	public static Process getProcessByID(Connection conn,String id)
	{
		if(Pub.empty(id)) return null;
        String sql = "select PROCESSOID,PROCESSTYPEOID,to_char(CREATETIME,'yyyy-mm-dd hh24:mi:ss'),to_char(CLOSETIME,'yyyy-mm-dd hh24:mi:ss'),EVENTID,STATE,OPERATIONOID,MEMO,TASKOID,PROCESSEVENT,RESULT,RESULTDSCR,YXBS from ap_processinfo"
            + " where PROCESSOID='"+id+"'";
        String[][] list = DBUtil.query(sql);
        return getProcess(conn,list);
	}
	public static Process getProcessByID(Connection conn,int id)
	{
      return getProcessByID(conn,""+id);
	}
	private static Process getProcess(Connection conn,String[][] list)
	{
		if(list == null) return null;
		ProcessInfo proc = new ProcessInfo(conn);
		proc.setProcessOID(list[0][0]);
		proc.setProcessTypeOID(list[0][1]);
		proc.setCreateTime(Pub.toDate("yyyy-MM-dd HH:mm:ss",list[0][2]));
		proc.setCloseTime(Pub.toDate("yyyy-MM-dd HH:mm:ss",list[0][3]));
		proc.setCreatedEventID(list[0][4]);
		proc.setState(Pub.toInt(list[0][5]));
		proc.setOperationOID(list[0][6]);
		proc.setMemo(list[0][7]);
        proc.setTaskOID(list[0][8]);
        proc.setProcessEvent(list[0][9]);
        proc.setResult(Pub.toInt(list[0][10]));
        proc.setResultDscr(list[0][11]);
        proc.setFbs(list[0][12]);
		return proc;
	}
	public static Process getProcessByEvent(Connection conn,String eventID)
	{
		String sql = "select PROCESSOID,PROCESSTYPEOID,to_char(CREATETIME,'yyyy-mm-dd hh24:mi:ss'),to_char(CLOSETIME,'yyyy-mm-dd hh24:mi:ss'),EVENTID,STATE,OPERATIONOID,MEMO,TASKOID,PROCESSEVENT,RESULT,RESULTDSCR,YXBS from ap_processinfo where eventid='"+eventID+"' and state!=2";
		return getProcess(conn,DBUtil.query(sql));
	}
    public static Collection getProcessByTaskOID(Connection conn,String taskoid)
    {
      if(Pub.empty(taskoid)) return null;
      String sql = "select PROCESSOID,PROCESSTYPEOID,to_char(CREATETIME,'yyyy-mm-dd hh24:mi:ss'),to_char(CLOSETIME,'yyyy-mm-dd hh24:mi:ss'),EVENTID,STATE,OPERATIONOID,MEMO,TASKOID,PROCESSEVENT,RESULT,RESULTDSCR from ap_processinfo"
          + " where TASKOID='"+taskoid+"'";
      String[][] list = DBUtil.query(sql);
      ArrayList arr = null;
      if(list != null)
      {
        arr = new ArrayList(list.length);
        for(int i=0;i<list.length;i++)
        {
          ProcessInfo proc = new ProcessInfo(conn);
          proc.setProcessOID(list[0][0]);
          proc.setProcessTypeOID(list[0][1]);
          proc.setCreateTime(Pub.toDate("yyyy-MM-dd HH:mm:ss",list[0][2]));
          proc.setCloseTime(Pub.toDate("yyyy-MM-dd HH:mm:ss",list[0][3]));
          proc.setCreatedEventID(list[0][4]);
          proc.setState(Pub.toInt(list[0][5]));
          proc.setOperationOID(list[0][6]);
          proc.setMemo(list[0][7]);
          proc.setTaskOID(list[0][8]);
          proc.setProcessEvent(list[0][9]);
          proc.setResult(Pub.toInt(list[0][10]));
          proc.setResultDscr(list[0][11]);
          arr.add(proc);
        }
      }
      return arr;
    }
    /*
     * 获取节点是否为多人处理或包含抄送
     */
    public static boolean getStepValid(Connection conn,Step step,String id,String seq,String actor) throws Exception
    {
		//if ("1".equals(step.getIsCC()))// 抄送节点
		{
			QuerySet qs = null;
			// String actors = step.getActor();
			// actors = actors.replaceAll(",", "','");
			String sql = "select t.dbryid from AP_TASK_SCHEDULE t where " +
					" t.id='"+ id + "' and t.seq='"+seq+"' and t.rwzt = '" + TaskVO.TASK_STATUS_VALID
					+ "' and t.yxbs='0' and t.dbryid='"+actor+"'";
			try {
				qs = DBUtil.executeQuery(sql, null, conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (qs != null && qs.getRowCount() > 0) {
				return true;
			}
		}

		if ("3".equals(step.getIsMS())||"7".equals(step.getIsMS()))// 多人处理节点
		{
			QuerySet qs = null;
			// String actors = step.getActor();
			// actors = actors.replaceAll(",", "','");
			String sql = "select t.dbryid from AP_TASK_SCHEDULE t,AP_TASK_SCHEDULE d where t.sjbh=d.sjbh and t.spbh=d.spbh and t.ywlx=d.ywlx" +
					" and d.id='"+id+"' and d.seq='"+seq+"' and t.rwzt = '"+TaskVO.TASK_STATUS_VALID+"' and t.yxbs='1' "+
					" and t.dbryid!='"+actor+"' and t.stepsequence='"+step.getStepSequence()+"'";
			try {
				qs = DBUtil.executeQuery(sql, null, conn);
			} catch (Exception e) {
				throw e;
			}
			if (qs != null && qs.getRowCount() > 0) {
				return true;
			}

		}

  	  return false;
    }
}