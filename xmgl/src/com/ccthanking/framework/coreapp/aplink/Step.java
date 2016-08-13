//Source file: D:\\projects\\rkyw\\src\\com\\ccthanking\\aplink\\Step.java

package com.ccthanking.framework.coreapp.aplink;

import java.util.Date;
import java.sql.Connection;
import com.ccthanking.framework.util.Pub;

/**
 @author leo <oss@tom.com>
 环节定义
 */
public abstract class Step
{
    protected Connection conn;
	protected int stepOID;
	protected String name;
	protected String role;
	protected int deptLevel;
	protected String memo;
	protected String actor;
    protected String deptID;
    protected String isMS;
    protected String isCC;
    protected String isWP;
    protected String ccActor;

    //add by cbl start
    protected String SHEDULE_DAYS;
    protected String STEPACTOR;
    protected String ACTORDEPT;
    //add by cbl end

	public static final int AS_BARENESS = 0;
	public static final int AS_CREATED = 1;
	public static final int AS_INVALID = 2;
	public static final int AS_PROCESSING = 10;
	public static final int AS_SUCCESS = 11;
	public static final int AS_ERROR = 12;
	public static final int AS_BREAK = 13;

/**
		 @roseuid 42DF713D0113
	 */
	
	//add by cbl start
    public String getShedule_days()
    {
        return this.SHEDULE_DAYS;
    }
    public void setShedule_days(String SHEDULE_DAYS)
    {
        this.SHEDULE_DAYS = SHEDULE_DAYS;
    }
    
    public String getStepactor()
    {
        return this.STEPACTOR;
    }
    public void setStepactor(String STEPACTOR)
    {
        this.STEPACTOR = STEPACTOR;
    }
    public String getActordept()
    {
        return this.ACTORDEPT;
    }
    public void setActordept(String ACTORDEPT)
    {
        this.ACTORDEPT = ACTORDEPT;
    }
	//add by cbl end
    public String getIsMS()
    {
        return this.isMS;
    }
    public void setIsMS(String isMS)
    {
        this.isMS = isMS;
    }
    public String getIsCC()
    {
        return this.isCC;
    }
    public void setIsCC(String isCC)
    {
        this.isCC = isCC;
    }
    public String getIsWP()
    {
        return this.isWP;
    }
    public void setIsWP(String isWP)
    {
        this.isWP = isWP;
    }
    public String getCcActor()
    {
        return this.ccActor;
    }
    public void setCcActor(String ccActor)
    {
        this.ccActor = ccActor;
    }    
    
	public Step()
	{

	}

    public Connection getConnection()
    {
      return this.conn;
    }
    public void setConnection(Connection conn)
    {
      this.conn = conn;
    }
    public String getDeptID()
    {
        return this.deptID;
    }
    public void setDeptID(String deptid)
    {
        this.deptID = deptid;
    }

	public int getStepOID()
	{
		return stepOID;
	}

	public void setStepOID(int aStepOID)
	{
		stepOID = aStepOID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String aName)
	{
		name = aName;
	}
	public String getRole()
	{
		return role;
	}
	public void setRole(String aRole)
	{
		role = aRole;
	}
	public int getDeptLevel()
	{
		return deptLevel;
	}
	public void setDeptLevel(int aLevel)
	{
		deptLevel = aLevel;
	}
	public String getMemo()
	{
		return memo;
	}
	public void setMemo(String aMemo)
	{
		memo = aMemo;
	}
    public String getActor()
    {
        return actor;
    }
    public void setActor(String aActor)
    {
        actor = aActor;
    }

	public abstract int getPreStepOID();
	public abstract void setPreStepOID(int aPreStepOID);
	public abstract int getNextStepOID();
	public abstract void setNextStepOID(int aNextStepOID);
	public abstract int getState();
	public abstract void setState(int aState);

	public abstract String getProcessOID();
	public abstract int getStepSequence();
	public abstract void setStepSequence(int seq);
	public abstract void setProcessOID(String oid);
	public abstract void setPreCondition1(String con);
	public abstract void setPreCondition2(String con);
	public abstract void setPreCondition3(String con);
	public abstract String getPreCondition1();
	public abstract String getPreCondition2();
	public abstract String getPreCondition3();
	public abstract Date getCloseTime();
	public abstract void setCloseTime(Date date);
	public abstract Date getCreateTime();
	public abstract void setCreateTime(Date date);
	public abstract Date getProcessTime();
	public abstract void setProcessTime(Date date);
    public abstract String getApplication();
    public abstract void setApplication(String app);
    public abstract String getStepEvent();
    public abstract void setStepEvent(String eventClass);
	public abstract String getProcessTypeOID();
	public abstract void setProcessTypeOID(String aProcessTypeOID);
    public abstract String getTaskOID();
    public abstract void setTaskOID(String aTaskOID);
    public abstract String getTaskSequence();
    public abstract void setTaskSequence(String aTaskSequence);
    public abstract int getResult();
    public abstract void setResult(int result);
    public abstract String getResultDscr();
    public abstract void setResultDscr(String dscr);


	public abstract void delete()
		throws Exception;
	public abstract int open(String actor, String role,String deptid)
		throws Exception;
	public abstract int close(String actor, String role,String deptid)
		throws Exception;
	public abstract int execute(String actor, String role,String deptid)
		throws Exception;
	public abstract int rollback()throws Exception;
	public abstract int rollbackTo(int index)throws Exception;
	public abstract Process getProcess()throws Exception;
	public abstract Step getStepDefine() throws Exception;
	public abstract StepEvent getHandleEvent();
}