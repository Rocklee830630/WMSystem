//Source file: D:\\projects\\rkyw\\src\\com\\ccthanking\\aplink\\ProcessDetail.java

package com.ccthanking.framework.coreapp.aplink;

import java.util.Date;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.common.DBUtil;

/**
 @author leo <oss@tom.com>
 处理流程详情
 */
public class ProcessDetail
	extends ProcessStep
{
	private String processOID;
	private Date createTime;
	private Date closeTime;
	private Date processTime;
	private StepEvent handleEvent;
    private String taskOID;
    private String taskSequence;
    private int result;
    private String resultDscr;
    //add by cbl start
    private Date SHEDULE_TIME;
    private String isTh;

    
    public String getIsTh() {
		return isTh;
	}
	public void setIsTh(String isTh) {
		this.isTh = isTh;
	}
	public Date getShedule_time()
    {
      return SHEDULE_TIME;
    }
    public void setShedule_time(Date aSHEDULE_TIME)
    {
    	SHEDULE_TIME = aSHEDULE_TIME;
    }
    //add by cbl end
    public String getResultDscr()
    {
      return this.resultDscr==null?"":this.resultDscr;
    }
    public void setResultDscr(String dscr)
    {
      this.resultDscr = dscr;
    }
    public String getTaskOID()
    {
      return this.taskOID==null?"":this.taskOID;
    }
    public void setTaskOID(String oid)
    {
      this.taskOID = oid;
    }
    public String getTaskSequence()
    {
      return this.taskSequence==null?"":this.taskSequence;
    }
    public void setTaskSequence(String sequence)
    {
      this.taskSequence = sequence;
    }
    public int getResult()
    {
      return this.result;
    }
    public void setResult(int res)
    {
      this.result = res;
    }
	public ProcessDetail()
	{
	}
    public ProcessDetail(Connection conn)
    {
      super(conn);
    }
	public String getProcessOID()
	{
		return processOID==null?"":processOID;
	}
	public void setProcessOID(String aProcessOID)
	{
		processOID = aProcessOID;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date aCreateTime)
	{
		createTime = aCreateTime;
	}
	public Date getCloseTime()
	{
		return closeTime;
	}
	public void setCloseTime(Date aCloseTime)
	{
		closeTime = aCloseTime;
	}
	public Date getProcessTime()
	{
		return processTime;
	}
	public void setProcessTime(Date aProcessTime)
	{
		processTime = aProcessTime;
	}
	public StepEvent getHandleEvent()
	{
		if(this.validateHandle()) return handleEvent;
		else return null;
	}
	private boolean validateHandle()
	{
		if (!Pub.empty(this.getStepEvent()))
		{
			if (handleEvent == null)
			{
				try
				{
					handleEvent = (StepEvent)
						Class.forName(this.getStepEvent()).newInstance();
					return (handleEvent == null)?false:true;
				}
				catch (Exception e)
				{
					e.printStackTrace(System.out);
					return false;
				}
			}
			else return true;
		}
		else return false;
	}

	public void delete()
		throws Exception
	{
		if (this.validateHandle())
			handleEvent.onDelete(this);
		Connection conn = this.getConnection();
		try
		{
			String sql = "update ap_processdetail set STATE='"+Step.AS_INVALID+"' where PROCESSOID='" +
				this.getProcessOID()
				+ "' and STEPOID='" + this.stepOID+"' and STEPSEQUENCE='"+this.getStepSequence()+"'";
			DBUtil.execSql(conn, sql);
			sql = "update ap_processdetail set NEXTSTEPOID='" + this.nextStepOID +
				"' where PROCESSOID='" + this.getProcessOID()
				+ "' and STEPOID='" + this.preStepOID+"' and STEPSEQUENCE='"+(this.stepSequence-1)+"'";
			DBUtil.execSql(conn, sql);
			sql = "update ap_processdetail set PRESTEPOID='" + this.preStepOID +
				"',STEPSEQUENCE='"+this.stepSequence+"' where PROCESSOID='" + this.getProcessOID()
				+ "' and STEPOID='" + this.nextStepOID+"' and STEPSEQUENCE='"+(this.stepSequence+1)+"'";
            DBUtil.execSql(conn,sql);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			throw e;
		}
	}

	public int open(String actor, String role, String deptid)
		throws Exception
	{
		if(Pub.empty(actor) && Pub.empty(role) && Pub.empty(deptid))
			return Step.AS_ERROR;
		if(actor == null) actor = "";
		if(role == null) role = "";
		if(deptid == null) deptid = "";
		if(!role.equals(this.getRole()) && !actor.equals(this.getActor()) && !deptid.equals(this.getDeptID()))
			return Step.AS_ERROR;

		int status = Step.AS_PROCESSING;
		if (this.validateHandle())
		{
			try
			{
				handleEvent.onOpen(this);
			}
			catch(Exception e)
			{
				e.printStackTrace(System.out);
				status = Step.AS_BREAK;
				//throw e;
				return status;
			}
		}

		Connection conn = this.getConnection();
		try
		{
			String sql =
				"update ap_processdetail set STATE='"+status+"',CREATETIME=sysdate "
				+ (Pub.empty(role) ? "" : " ,ROLENAME='" + role + "'")
				+ (Pub.empty(actor) ? "" : " ,ACTOR='" + actor + "'")
				+ (Pub.empty(deptid) ? "" : " ,DEPTID='" + deptid + "'")
				+ (Pub.empty(memo) ? "" : " ,MEMO='" + memo + "'")
				+ (deptLevel<=0 ? "" : " ,deptlevel='" + deptLevel + "'")
				+ (Pub.empty(application) ? "" : " ,application='" + application + "'")
				+ " where PROCESSOID='" + this.getProcessOID()
				+ "' and STEPOID='" + this.stepOID+"' and STEPSEQUENCE='"+this.getStepSequence()+"'";
			DBUtil.execSql(conn, sql);
			status = Step.AS_SUCCESS;
			this.setState(status);
			this.setActor(actor);
			this.setRole(role);
			this.setDeptID(deptid);
			this.setCreateTime(Pub.getCurrentDate());
		}
		catch (SQLException e)
		{
			e.printStackTrace(System.out);
			status = Step.AS_ERROR;
			throw e;
		}
		finally
		{
			this.setState(status);
		}
		return status;
	}

	public int close(String actor, String role, String deptid)
		throws Exception
	{
		if(Pub.empty(actor) && Pub.empty(role) && Pub.empty(deptid))
			return Step.AS_ERROR;
		if(actor == null) actor = "";
		if(role == null) role = "";
		if(deptid == null) deptid = "";
		if(!role.equals(this.getRole()) && !actor.equals(this.getActor()) && !deptid.equals(this.getDeptID()))
			return Step.AS_ERROR;

		int status = Step.AS_SUCCESS;
		if (this.validateHandle())
		{
			try
			{
				handleEvent.onClose(this);
			}
			catch(Exception e)
			{
				e.printStackTrace(System.out);
				status = Step.AS_BREAK;
				//throw e;
				return status;
			}
		}

		Connection conn = this.getConnection();
		try
		{
			String sql =
				"update ap_processdetail set STATE=nvl(decode(state,"+Step.AS_SUCCESS+","+Step.AS_SUCCESS+"),"+Step.AS_ERROR+"),CLOSETIME=sysdate "
				+ (Pub.empty(role) ? "" : " ,ROLENAME='" + role + "'")
				+ (Pub.empty(actor) ? "" : " ,ACTOR='" + actor + "'")
				+ (Pub.empty(deptid) ? "" : " ,DEPTID='" + deptid + "'")
				+ (Pub.empty(memo) ? "" : " ,memo='" + memo + "'")
				+ " where PROCESSOID='" + this.getProcessOID()
				+ "' and STEPOID=" + this.stepOID+"' and STEPSEQUENCE='"+this.getStepSequence()+"'";
			DBUtil.execSql(conn, sql);
			status = Step.AS_SUCCESS;
			this.setActor(actor);
			this.setRole(role);
			this.setDeptID(deptid);
			this.setCloseTime(Pub.getCurrentDate());
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			status = Step.AS_ERROR;
			throw e;
		}
		finally
		{
			this.setState(status);
		}
		return status;
	}

	public int execute(String actor, String role, String deptid)
		throws Exception
	{
		if(Pub.empty(actor) && Pub.empty(role) && Pub.empty(deptid))
			return Step.AS_ERROR;
		if(actor == null) actor = "";
		if(role == null) role = "";
		if(deptid == null) deptid = "";
		if(!role.equals(this.getRole()) || !actor.equals(this.getActor()) || !deptid.equals(this.getDeptID()))
			return Step.AS_ERROR;

		int status = Step.AS_SUCCESS;
		if (this.validateHandle())
		{
			try
			{
				handleEvent.onExecute(this);
			}
			catch(Exception e)
			{
				e.printStackTrace(System.out);
				status = Step.AS_BREAK;
				//throw e;
				return status;
			}
		}

		Connection conn = this.getConnection();
		try
		{
			String sql =
				"update ap_processdetail set STATE='"+status+"',PROCESSTIME=sysdate "
				+ (Pub.empty(role) ? "" : " ,ROLENAME='" + role + "'")
				+ (Pub.empty(actor) ? "" : " ,ACTOR='" + actor + "'")
				+ (Pub.empty(deptid) ? "" : " ,DEPTID='" + deptid + "'")
				+ (Pub.empty(memo) ? "" : " ,MEMO='" + memo + "'")
				+ " where PROCESSOID='" + this.getProcessOID()
				+ "' and STEPOID='" + this.stepOID+"' and STEPSEQUENCE='"+this.getStepSequence()+"'";
			DBUtil.execSql(conn, sql);
			status = Step.AS_SUCCESS;
			this.setActor(actor);
			this.setRole(role);
			this.setDeptID(deptid);
			this.setProcessTime(Pub.getCurrentDate());
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			status = Step.AS_ERROR;
			throw e;
		}
		finally
		{
			this.setState(status);
		}
		return status;
	}

	public int rollback()throws Exception
	{
		return rollbackTo(this.getStepSequence()-1);
	}

	public int rollbackTo(int index)throws Exception
	{
		if(index < 0) index = 0;
		if(index >= this.getStepSequence()) return Step.AS_ERROR;
		Connection conn = this.getConnection();
		try
		{
			ArrayList steps = (ArrayList) (this.getProcess().getSteps());
			for(int i=index;i<this.getStepSequence();i++)
			{
				Step step = (Step) steps.get(i);
				Step stepDefine = step.getStepDefine();
				if(stepDefine == null) stepDefine = step;
				String sql =
					"update ap_processdetail set STATE='"+Step.AS_CREATED+"',CLOSETIME=null "
					+ " ,ROLENAME='" + stepDefine.getRole() + "'"
					+ " ,ACTOR='" + stepDefine.getActor() + "'"
					+ " ,DEPTID='" + stepDefine.getDeptID() + "'"
					+ " where PROCESSOID='" + this.getProcessOID()
					+ "' and STEPOID='" + step.getStepOID()+"' and STEPSEQUENCE='"+this.getStepSequence()+"'";
				DBUtil.execSql(conn, sql);
			}
			return Step.AS_CREATED;
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			throw e;
		}
	}
	public Process getProcess() throws Exception
	{
		return ProcessMgr.getProcessByID(conn,this.getProcessOID());
	}

	public Step getStepDefine() throws Exception
	{
		return ProcessTypeMgr.getProcessByID(conn,this.getProcessTypeOID()).getStepByID(this.getStepOID());
	}

}