//Source file: D:\\projects\\rkyw\\src\\com\\ccthanking\\aplink\\ProcessStep.java

package com.ccthanking.framework.coreapp.aplink;

import java.sql.*;
import java.util.*;
import java.util.Date;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.common.DBUtil;
/**
 @author leo <oss@tom.com>
 处理环节定义
 */
public class ProcessStep
	extends Step
{

	protected String processTypeOID;
	protected int stepSequence;
	protected String preCondition1;
	protected String preCondition2;
	protected String preCondition3;
    protected int state;
    protected String stepEvent;
    protected String application;
    protected int preStepOID;
    protected int nextStepOID;

        public int getState()
        {
            return state;
        }
        public void setState(int aState)
        {
            state = aState;
        }
        public int getPreStepOID()
        {
            return preStepOID;
        }
        public void setPreStepOID(int aPreStepOID)
        {
            preStepOID = aPreStepOID;
        }
        public int getNextStepOID()
        {
            return nextStepOID;
        }
        public void setNextStepOID(int aNextStepOID)
        {
            nextStepOID = aNextStepOID;
        }

    public String getApplication()
    {
        return Pub.empty(this.application)?"":this.application;
    }
    public void setApplication(String app)
    {
        this.application = app;
    }

    public String getStepEvent()
    {
        return this.stepEvent;
    }
    public void setStepEvent(String eventClass)
    {
        this.stepEvent = eventClass;
    }

	public ProcessStep()
	{

	}
	public String getProcessTypeOID()
	{
		return processTypeOID;
	}
	public void setProcessTypeOID(String aProcessTypeOID)
	{
		processTypeOID = aProcessTypeOID;
	}
	public int getStepSequence()
	{
		return stepSequence;
	}
	public void setStepSequence(int aStepSequence)
	{
		stepSequence = aStepSequence;
	}
	public String getPreCondition1()
	{
		return preCondition1;
	}
	public void setPreCondition1(String aPreCondition1)
	{
		preCondition1 = aPreCondition1;
	}
	public String getPreCondition2()
	{
		return preCondition2;
	}
	public void setPreCondition2(String aPreCondition2)
	{
		preCondition2 = aPreCondition2;
	}
	public String getPreCondition3()
	{
		return preCondition3;
	}
	public void setPreCondition3(String aPreCondition3)
	{
		preCondition3 = aPreCondition3;
	}
	public String getProcessOID()
	{
		return this.processTypeOID;
	}
	public void setProcessOID(String oid)
	{
		this.processTypeOID = oid;
	}

	public void delete()	throws Exception
	{
		Connection conn = this.getConnection();
		try
		{
			String sql = "update ap_processstep set STATE='"+Step.AS_INVALID+"' where PROCESSTYPEOID='"+this.processTypeOID
				+ "' and STEPOID='"+this.stepOID+"' and STEPSEQUENCE='"+this.stepSequence+"'";
			DBUtil.execSql(conn,sql);
			sql = "update ap_processstep set NEXTSTEPOID='"+this.nextStepOID +"' where PROCESSTYPEOID='"+this.processTypeOID
				+ "' and STEPOID='"+this.preStepOID+"' and STEPSEQUENCE='"+(this.stepSequence-1)+"'";
			DBUtil.execSql(conn,sql);
			sql = "update ap_processstep set PRESTEPOID='"+this.preStepOID +"',STEPSEQUENCE='"+this.stepSequence+"' where PROCESSTYPEOID='"+this.processTypeOID
				+ "' and STEPOID='"+this.nextStepOID+"' and STEPSEQUENCE='"+(this.stepSequence+1)+"'";
			DBUtil.execSql(conn,sql);
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			throw e;
		}
	}

	public int open( String actor,String role,String deptid)throws Exception
	{
		return 0;
	}

	public int close( String actor,String role,String deptid)throws Exception
	{
		return 0;
	}

	public int execute( String actor,String role,String deptid)throws Exception
	{
		return 0;
	}
	public int rollback()throws Exception
	{
		return 0;
	}

	public int rollbackTo(int index)throws Exception
	{
		return 0;
	}
	public Process getProcess() throws Exception
	{
		return ProcessTypeMgr.getProcessByID(this.conn,this.getProcessOID());
	}

	public Step getStepDefine() throws Exception
	{
		return this;
	}
	public Date getCloseTime()
	{
		return null;
	}

	public Date getCreateTime()
	{
		return null;
	}

	public Date getProcessTime()
	{
		return null;
	}
	public void setCloseTime(Date date)
	{
	}

	public void setCreateTime(Date date)
	{
	}

	public void setProcessTime(Date date)
	{
	}

	public StepEvent getHandleEvent()
	{
		return null;
	}

  public ProcessStep(Connection connection)
  {
    this.conn = connection;
  }

  public String getTaskOID()
  {
    return "";
  }

  public void setTaskOID(String aTaskOID)
  {
  }

  public String getTaskSequence()
  {
    return "";
  }

  public void setTaskSequence(String aTaskSequence)
  {
  }

  public int getResult()
  {
    return 0;
  }

  public void setResult(int result)
  {
  }

  public String getResultDscr()
  {
    return "";
  }

  public void setResultDscr(String dscr)
  {
  }
}