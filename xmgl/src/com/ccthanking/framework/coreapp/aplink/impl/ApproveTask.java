package com.ccthanking.framework.coreapp.aplink.impl;

import com.ccthanking.framework.coreapp.aplink.TaskVO;
import java.sql.*;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.common.EventManager;
import com.ccthanking.framework.coreapp.aplink.Process;
import com.ccthanking.framework.coreapp.aplink.ProcessMgr;
import com.ccthanking.framework.coreapp.aplink.Step;
import com.ccthanking.framework.common.DBUtil;

public class ApproveTask
{
    public ApproveTask()
    {
    }

    //自动归档事件，如果不需要创建任务，返回 null
    public TaskVO ArchiveTask(Connection conn, TaskVO task, String sjbh,
                              String ywlx)
    {
        try
        {
           String[][] fqr=null;
//            if("040930".equalsIgnoreCase(ywlx)){  协破案件审批流程暂时不修改 add wangly 2009 09 18
//                    Process process = ProcessMgr.getProcessByEvent(conn, sjbh); //案件协破审批通过后返回给审核员，而不是发起人，单独处理
//                    Step step = process.getFirstStep();
//
//                    String id = step.getTaskOID();
//                    fqr = DBUtil.querySql(conn, "select DBRYID, DBDWDM from ap_task_schedule where id='"+id+"' and seq = '"+id+"'");
//                    if (fqr != null)
//                    {
//                        task.setDBRYID(fqr[0][0]);
//                        task.setDBDWDM(fqr[0][1]);
//                    }
//            }else{

                    Process process = ProcessMgr.getProcessByEvent(conn, sjbh); //通过事件获得当前流程实例
                    Step step = process.getFirstStep();

                    String id = step.getTaskOID();
                    fqr = DBUtil.querySql(conn, "select CJRID from ap_task_schedule where id='"+id+"' order by cjsj asc");
                    if (fqr != null)
                        task.setDBRYID(fqr[0][0]);
                    else
                        task.setDBRYID(EventManager.getEventByID(conn,task.getSJBH()).getCzybh());

//            }


            return task;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return task;
    }

    //自动回退事件，如果不需要创建任务，返回 null
    public TaskVO RollbackTask(Connection conn, TaskVO task, String sjbh,
                               String ywlx)
    {

        try
        {
            switch (Pub.toInt(ywlx))
            {
                default:
                    Process process = ProcessMgr.getProcessByEvent(conn, sjbh); //通过事件获得当前流程实例
                    Step step = process.getFirstStep();

                    String id = step.getTaskOID();
                    String[][] fqr = DBUtil.querySql(conn,
                        "select CJRID from ap_task_schedule where id='" + id +
                        "' order by cjsj ");
                    if (fqr != null)
                    {
                        task.setDBRYID(fqr[0][0]);
                    }
                    else
                    {
                        task.setDBRYID(EventManager.getEventByID(conn,task.getSJBH()).getCzybh());
                    }
                    break;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

        }

        return task;

    }

}