package com.ccthanking.framework.coreapp.aplink;

import java.util.*;
import java.sql.SQLException;
import java.sql.Connection;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.Pub;

public class ProcessTypeMgr
{

	public static Process createProcess(Connection conn,String name, String operationOID,String actor,
										String condition1, String condition2,
										String condition3, String memo,String processEvent)
	{
		String sql = "select ap_task_s.nextval from dual";
		String[][] list = DBUtil.query(conn,sql);
		if (list == null)
			return null;
		if(Pub.toInt(operationOID) <= 0) return null;
		if(condition1 == null) condition1 = "";
		if(condition2 == null) condition2 = "";
		if(condition3 == null) condition3 = "";
		if(memo == null) memo = "";
		if(name == null) name = "";
		if(actor == null) actor = "";
        if(processEvent == null) processEvent = "";
		sql = "insert into ap_processtype cols(PROCESSTYPEOID,NAME,OPERATIONOID,"
			+ "ACTOR,CREATETIME,STATE,PRECONDITION1,PRECONDITION2,PRECONDITION3,MEMO,PROCESSEVENT )"
		+ "	values("+list[0][0]+",'"+name+"',"+operationOID+",'"+actor+"',sysdate,'"+Process.AP_CREATED+"','"+condition1+"','"+condition2
		+ "','"+condition3+",'"+memo+"','"+processEvent+"')";
		try
		{
			if(!DBUtil.exec(conn,sql)) return null;
	    }catch(Exception e)
	    {
	    	System.out.println(e);
	    	//throw e;
	    }
	ProcessType proc = new ProcessType(conn);
	proc.setActor(actor);
	proc.setCreateTime(Pub.getCurrentDate());
	proc.setMemo(memo);
	proc.setName(name);
	proc.setOperationOID(operationOID);
	proc.setPreCondition1(condition1);
	proc.setPreCondition2(condition2);
	proc.setPreCondition3(condition3);
    proc.setProcessEvent(processEvent);
	proc.setState(Process.AP_CREATED);
	proc.setProcessTypeOID(list[0][0]);
		return (Process) proc;
	}
	public static Process getProcessByID(Connection conn,String id)
	{
		if(Pub.toInt(id) <= 0) return null;
		else return getProcessByID(conn,Pub.toInt(id));
	}
	public static Process getProcessByID(Connection conn,int id)
	{
		String sql = "select PROCESSTYPEOID,NAME,ACTOR,to_char(CREATETIME,'yyyy-mm-dd hh24:mi:ss'),PRECONDITION1,PRECONDITION2,PRECONDITION3,STATE,OPERATIONOID,MEMO,processEvent,PROCESSTYPE from ap_processtype"
			+ " where PROCESSTYPEOID='"+id+"'";
		String[][] list = DBUtil.query(conn,sql);
		return getProcess(conn,list);
	}
	private static Process getProcess(Connection conn,String[][] list)
	{
		if(list == null) return null;
		ProcessType proc = new ProcessType(conn);
		proc.setProcessTypeOID(list[0][0]);
		proc.setName(list[0][1]);
		proc.setActor(list[0][2]);
		proc.setCreateTime(Pub.toDate("yyyy-MM-dd HH:mm:ss",list[0][3]));
		proc.setPreCondition1(list[0][4]);
		proc.setPreCondition2(list[0][5]);
		proc.setPreCondition3(list[0][6]);
		proc.setState(Pub.toInt(list[0][7]));
		proc.setOperationOID(list[0][8]);
		proc.setMemo(list[0][9]);
        proc.setProcessEvent(list[0][10]);
        //add by cbl
        proc.setProcesstype(list[0][11]);
        //add by cbl end
		return proc;
	}
	public static Process getProcessByOperationOID(Connection conn,String oid)
	{
		String sql = "select PROCESSTYPEOID,NAME,ACTOR,to_char(CREATETIME,'yyyy-mm-dd hh24:mi:ss'),PRECONDITION1,PRECONDITION2,PRECONDITION3,STATE,OPERATIONOID,MEMO,processEvent,PROCESSTYPE from ap_processtype"
			+ " where OPERATIONOID='"+oid+"' and state!=2";
		return getProcess(conn,DBUtil.query(conn,sql));
	}
}