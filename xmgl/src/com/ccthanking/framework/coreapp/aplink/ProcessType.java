//Source file: D:\\projects\\rkyw\\src\\com\\ccthanking\\aplink\\ProcessType.java

package com.ccthanking.framework.coreapp.aplink;

import java.util.*;
import java.sql.SQLException;
import java.sql.Connection;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.Pub;


/**
@author leo <oss@tom.com>

流程定义
 */
public class ProcessType implements Process
{
				private String processTypeOID;
				private String name;
				private String operationOID;
				private String actor;
				private Date createTime;
				private int state;
				private String preCondition1;
				private String preCondition2;
				private String preCondition3;
				private String memo;
                private String processEvent;
				public ArrayList steps;
                private String fBs = "";
                private String processtype = "";
                public String getFbs()
                {
                    return this.fBs;
                }
                public void setFbs(String fBs)
                {
                    this.fBs = fBs;
                }
                private Connection conn;
				/**
				@roseuid 42DF713C034C
				 */
				public ProcessType()
				{

				}
                public ProcessType(Connection conn)
                {
                  this.conn = conn;
                }
				/**
				Access method for the processTypeOID property.

				@return   the current value of the processTypeOID property
				 */
				public String getProcessTypeOID()
				{
								return processTypeOID==null?"":processTypeOID;
				}

				/**
				Sets the value of the processTypeOID property.

				@param aProcessTypeOID the new value of the processTypeOID property
				 */
				public void setProcessTypeOID(String aProcessTypeOID)
				{
								processTypeOID = aProcessTypeOID;
				}

				/**
				Access method for the name property.

				@return   the current value of the name property
				 */
				public String getName()
				{
								return name==null?"":name;
				}

				/**
				Sets the value of the name property.

				@param aName the new value of the name property
				 */
				public void setName(String aName)
				{
								name = aName;
				}

				/**
				Access method for the OperationOID property.

				@return   the current value of the OperationOID property
				 */
				public String getOperationOID()
				{
								return operationOID==null?"":operationOID;
				}

				/**
				Sets the value of the OperationOID property.

				@param aOperationOID the new value of the OperationOID property
				 */
				public void setOperationOID(String aOperationOID)
				{
								operationOID = aOperationOID;
				}

				/**
				Access method for the actor property.

				@return   the current value of the actor property
				 */
				public String getActor()
				{
								return actor==null?"":actor;
				}

				/**
				Sets the value of the actor property.

				@param aActor the new value of the actor property
				 */
				public void setActor(String aActor)
				{
								actor = aActor;
				}

				/**
				Access method for the createTime property.

				@return   the current value of the createTime property
				 */
				public Date getCreateTime()
				{
								return createTime;
				}

				/**
				Sets the value of the createTime property.

				@param aCreateTime the new value of the createTime property
				 */
				public void setCreateTime(Date aCreateTime)
				{
								createTime = aCreateTime;
				}

				/**
				Access method for the state property.

				@return   the current value of the state property
				 */
				public int getState()
				{
								return state;
				}

				/**
				Sets the value of the state property.

				@param aState the new value of the state property
				 */
				public void setState(int aState)
				{
								state = aState;
				}

				/**
				Access method for the preCondition1 property.

				@return   the current value of the preCondition1 property
				 */
				public String getPreCondition1()
				{
								return preCondition1==null?"":preCondition1;
				}

				/**
				Sets the value of the preCondition1 property.

				@param aPreCondition1 the new value of the preCondition1 property
				 */
				public void setPreCondition1(String aPreCondition1)
				{
								preCondition1 = aPreCondition1;
				}

				/**
				Access method for the preCondition2 property.

				@return   the current value of the preCondition2 property
				 */
				public String getPreCondition2()
				{
								return preCondition2==null?"":preCondition2;
				}

				/**
				Sets the value of the preCondition2 property.

				@param aPreCondition2 the new value of the preCondition2 property
				 */
				public void setPreCondition2(String aPreCondition2)
				{
								preCondition2 = aPreCondition2;
				}

				/**
				Access method for the preCondition3 property.

				@return   the current value of the preCondition3 property
				 */
				public String getPreCondition3()
				{
								return preCondition3==null?"":preCondition3;
				}

				/**
				Sets the value of the preCondition3 property.

				@param aPreCondition3 the new value of the preCondition3 property
				 */
				public void setPreCondition3(String aPreCondition3)
				{
								preCondition3 = aPreCondition3;
				}

				/**
				Access method for the memo property.

				@return   the current value of the memo property
				 */
				public String getMemo()
				{
								return memo==null?"":memo;
				}

				/**
				Sets the value of the memo property.

				@param aMemo the new value of the memo property
				 */
				public void setMemo(String aMemo)
				{
								memo = aMemo;
				}
	public Collection getSteps()
	{
		String sql = "select PROCESSTYPEOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,"
            +" STATE,PRESTEPOID,NEXTSTEPOID,PRECONDITION1,PRECONDITION2,PRECONDITION3,"
            +" deptlevel,APPLICATION,MEMO,DEPTID,STEPEVENT,SHEDULE_DAYS,isms,iscc,ccactor from ap_processstep "
            +" where PROCESSTYPEOID='"+this.processTypeOID+"' and state='"+Step.AS_CREATED+"' order by STEPSEQUENCE";
		String[][] list = DBUtil.query(conn,sql);
		if(list == null) return null;
		steps = new ArrayList(list.length);
		for(int i=0;i<list.length;i++)
		{
			Step step = new ProcessStep();
            step.setConnection(conn);
			step.setProcessTypeOID(this.getProcessTypeOID());
			step.setStepSequence(Pub.toInt(list[i][1]));
			step.setStepOID(Pub.toInt(list[i][2]));
			step.setName(list[i][3]);
			step.setRole(list[i][4]);
			step.setActor(list[i][5]);
			step.setState(Pub.toInt(list[i][6]));
			step.setPreStepOID(Pub.toInt(list[i][7]));
			step.setNextStepOID(Pub.toInt(list[i][8]));
			step.setPreCondition1(list[i][9]);
			step.setPreCondition2(list[i][10]);
			step.setPreCondition3(list[i][11]);
			step.setDeptLevel(Pub.toInt(list[i][12]));
			step.setApplication(list[i][13]);
			step.setMemo(list[i][14]);
			step.setDeptID(list[i][15]);
            step.setStepEvent(list[i][16]);
            //add by cbl start
            step.setShedule_days(list[i][17]);
            step.setIsMS(list[i][18]);
            step.setIsCC(list[i][19]);
            step.setCcActor(list[i][20]);
            //add by cbl end
			steps.add(step);
		}
		return steps;
	}

	public Step getFirstStep()
	{
		if(steps == null)
			this.getSteps();
		if(steps == null)
			return null;
		else
			return (Step) steps.get(0);
	}

	public Step getLastStep()
	{
		if(steps == null)
			this.getSteps();
		if(steps == null)
			return null;
		else
			return (Step) steps.get(steps.size()-1);
	}

	public Step getStepByID(int id)
	{
		if(steps == null)
			this.getSteps();
		if(steps == null)
			return null;
		for(int i=0;i<steps.size();i++)
			if(((Step) steps.get(i)).getStepOID() == id)
				return (Step) steps.get(i);
		return null;
	}

	public Step getStepByIndex(int index)
	{
		if(steps == null)
			this.getSteps();
		if(steps == null)
			return null;
		for(int i=0;i<steps.size();i++)
			if(((Step) steps.get(i)).getStepSequence() == index)
				return (Step) steps.get(i);
		return null;
	}


	public Step appendStep(Step aStep)
      throws Exception
  {
		if(aStep == null) return null;
		if(steps == null)
			this.getSteps();
		if(steps == null)
			steps = new ArrayList(1);
        if(aStep.getConnection() == null) aStep.setConnection(conn);
///////////////////
		Step lastStep = (steps.size()<1)?null:(Step) steps.get(steps.size() - 1);
		if(lastStep != null)
		{
			lastStep.setNextStepOID(aStep.getStepOID());
			aStep.setPreStepOID(lastStep.getStepOID());
			aStep.setStepSequence(lastStep.getStepSequence() + 1);
		}
		else
		{
			aStep.setPreStepOID(0);
			aStep.setStepSequence(1);
		}
		aStep.setNextStepOID(0);
		aStep.setProcessOID(this.getProcessOID());
		try

		{
			String sql = "insert into ap_processstep cols(PROCESSTYPEOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,"
				+
				"PRESTEPOID,NEXTSTEPOID,PRECONDITION2,"
				+
				"PRECONDITION3,PRECONDITION1,DEPTID,deptlevel,APPLICATION,MEMO,STEPEVENT) "
				+ " values('" + this.getProcessOID() + "','" +
				aStep.getStepSequence()
				+ "','" + aStep.getStepOID() + "','" + aStep.getName() + "','" +
				aStep.getRole() + "','" + aStep.getActor()
				+ "','" + aStep.getState() + "','" +
				aStep.getPreStepOID() + "','0','" + aStep.getPreCondition2()
				+ "','" + aStep.getPreCondition3() + "','" +
				aStep.getPreCondition1() + "','" + aStep.getDeptID()
				+ "','"
				+ aStep.getDeptLevel()
				+ "','" + aStep.getApplication() + "','" + aStep.getMemo() +
				"','"+(aStep.getStepEvent()==null?"":aStep.getStepEvent())
				+ "')";

			DBUtil.execSql(conn, sql);
			if(lastStep != null)
			{
				sql = "update ap_processstep set NEXTSTEPOID='" +
					lastStep.getNextStepOID()
					+ "' where PROCESSOID='" + this.getProcessOID() +
					"' and STEPSEQUENCE='"
					+ lastStep.getStepSequence() + "' and STEPOID='" +
					lastStep.getStepOID()+"'";
				DBUtil.execSql(conn, sql);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
            throw e;
		}
		steps.add(aStep);
		return aStep;
	}

	public Step insertStep(int index, Step aStep)
      throws Exception
  {
		if (steps == null)
			this.getSteps();
		if (aStep == null)
			return null;
		if(steps == null)
			steps = new ArrayList(1);
        if(aStep.getConnection()==null) aStep.setConnection(conn);
		String sql = null;
		try
		{
			if (index == 0) //插入头节点
			{
				aStep.setPreStepOID(0);
				sql = "update ap_processstep set STEPSEQUENCE=STEPSEQUENCE+1"
					+ " where PROCESSTYPEOID='" + this.getProcessOID()
					+ "' and STEPSEQUENCE > 0 ";
				DBUtil.execSql(conn, sql);
				sql = "insert into ap_processstep cols(PROCESSTYPEOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,"
					+

					"PRESTEPOID,NEXTSTEPOID,PRECONDITION2,"
					+
					"PRECONDITION3,PRECONDITION1,DEPTID,deptlevel,APPLICATION,MEMO,STEPEVENT) "
					+ " values('" + this.getProcessOID() + "','" +
					1 //aStep.getStepSequence()
					+ "','" + aStep.getStepOID() + "','" + aStep.getName() + "','" +
					aStep.getRole() + "','" + aStep.getActor()
					+ "','" + aStep.getState() + "','" +
					0 + "','" +
					( (steps.size() > 0) ? ( (Step) steps.get(0)).getStepOID() :
					 0) + "','" + aStep.getPreCondition2()
					+ "','" + aStep.getPreCondition3() + "','" +
					aStep.getPreCondition1() + "','" + aStep.getDeptID()
					+ "','"
					+ aStep.getDeptLevel()
					+ "','" + aStep.getApplication() + "','" + aStep.getMemo() +
					"','"+(aStep.getStepEvent()==null?"":aStep.getStepEvent())
					+ "')";

				DBUtil.execSql(conn, sql);
				sql = "update ap_processstep set PRESTEPOID='" + aStep.getStepOID()
					+ "' where PROCESSTYPEOID='" + this.getProcessOID()
					+ "' and STEPOID='" +
					( (steps.size() > 0) ? ( (Step) steps.get(0)).getStepOID() :
					 0)+"'";
				DBUtil.execSql(conn, sql);
				if (steps.size() > 0)
				{
					aStep.setNextStepOID( ( (Step) steps.get(0)).getStepOID());
					( (Step) steps.get(0)).setPreStepOID(aStep.getStepOID());
				}
				else
				{
					aStep.setNextStepOID(0);
				}
				aStep.setStepSequence(1);
				aStep.setProcessOID(this.getProcessOID());
				for (int i = 0; i < steps.size(); i++)
					( (Step) steps.get(i)).setStepSequence( ( (Step) steps.get(
						i)).getStepSequence() + 1);
				steps.add(index, aStep);
			}
			else if (index >= steps.size()) // 插入尾节点
			{
				aStep.setNextStepOID(0);
				sql = "insert into ap_processstep cols(PROCESSTYPEOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,"
					+
					"PRESTEPOID,NEXTSTEPOID,PRECONDITION2,"
					+
					"PRECONDITION3,PRECONDITION1,DEPTID,deptlevel,APPLICATION,MEMO,STEPEVENT) "
					+ " values('" + this.getProcessOID() + "','" +
					( (steps.size() > 0) ?
					 ( (Step) steps.get(steps.size() - 1)).getStepSequence() +
					 1 : 1)
					+ "','" + aStep.getStepOID() + "','" + aStep.getName() + "','" +
					aStep.getRole() + "','" + aStep.getActor()
					+ "','" + aStep.getState() + "','" +
					( (steps.size() > 0) ?
					 ( (Step) steps.get(steps.size() - 1)).getStepOID() : 0) +
					"','" + 0 + "','" + aStep.getPreCondition2()
					+ "','" + aStep.getPreCondition3() + "','" +
					aStep.getPreCondition1() + "','" + aStep.getDeptID()
					+ "','"
					+ aStep.getDeptLevel()
					+ "','" + aStep.getApplication() + "','" + aStep.getMemo() +
					"','"+(aStep.getStepEvent()==null?"":aStep.getStepEvent())
					+ "')";

				DBUtil.execSql(conn, sql);
				sql = "update ap_processstep set NEXTSTEPOID='" +
					aStep.getStepOID()
					+ "' where PROCESSTYPEOID='" + this.getProcessOID()
					+ "' and STEPOID='" +
					( (steps.size() > 0) ?
					 ( (Step) steps.get(steps.size() - 1)).getStepOID() : 0)+"'";
				DBUtil.execSql(conn, sql);

				if (steps.size() > 0)

				{
					aStep.setPreStepOID( ( (Step) steps.get(steps.size() - 1)).
										getStepOID());
					( (Step) steps.get(steps.size() - 1)).setNextStepOID(aStep.
						getStepOID());
				}
				else
					aStep.setPreStepOID(0);
				aStep.setStepSequence( ( (steps.size() > 0) ?
										( (Step) steps.get(steps.size() - 1)).
										getStepSequence() + 1 : 1));
				aStep.setProcessOID(this.getProcessOID());
				steps.add(steps.size(), aStep);
			}
			else //正常增加
			{
				Step preStep = (Step) steps.get(index - 1);
				Step nextStep = (Step) steps.get(index);
				sql = "update ap_processstep set STEPSEQUENCE=STEPSEQUENCE+1"
					+ " where PROCESSTYPEOID='" + this.getProcessOID()
					+ "' and STEPSEQUENCE >= " + nextStep.getStepSequence();
				DBUtil.execSql(conn, sql);
				sql = "insert into ap_processstep cols(PROCESSTYPEOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,"
					+
					"PRESTEPOID,NEXTSTEPOID,PRECONDITION2,"
					+
					"PRECONDITION3,PRECONDITION1,DEPTID,deptlevel,APPLICATION,MEMO,STEPEVENT) "
					+ " values('" + this.getProcessOID() + "','" +
					nextStep.getStepSequence()
					+ "','" + aStep.getStepOID() + ",'" + aStep.getName() + "','" +
					aStep.getRole() + "','" + aStep.getActor()
					+ "','" + aStep.getState() + "','"
					+ preStep.getStepOID() +
					"','" + nextStep.getStepOID() + "','" + aStep.getPreCondition2()
					+ "','" + aStep.getPreCondition3() + "','" +
					aStep.getPreCondition1() + "','" + aStep.getDeptID()
					+ "','"
					+ aStep.getDeptLevel()
					+ "','" + aStep.getApplication() + "','" + aStep.getMemo() +
					"','"+(aStep.getStepEvent()==null?"":aStep.getStepEvent())
					+ "')";
				DBUtil.execSql(conn, sql);
				sql = "update ap_processstep set NEXTSTEPOID='" +
					aStep.getStepOID()
					+ "' where PROCESSTYPEOID='" + this.getProcessOID()
					+ "' and STEPOID='" + preStep.getStepOID()+"' and STEPSEQUENCE='"+preStep.getStepSequence()+"'";
				DBUtil.execSql(conn, sql);
				sql = "update ap_processstep set PRESTEPOID='" +
					aStep.getStepOID()
					+ "' where PROCESSTYPEOID='" + this.getProcessOID()
					+ "' and STEPOID='" + nextStep.getStepOID()+"' and STEPSEQUENCE='"+(nextStep.getStepSequence()+1)+"'";
				DBUtil.execSql(conn, sql);

				aStep.setPreStepOID( ( (Step) steps.get(index - 1)).getStepOID());
				aStep.setNextStepOID( ( (Step) steps.get(index)).getStepOID());
				( (Step) steps.get(index - 1)).setNextStepOID(aStep.getStepOID());
				( (Step) steps.get(index)).setPreStepOID(aStep.getStepOID());
				for (int i = index; i < steps.size(); i++)
					( (Step) steps.get(i)).setStepSequence( ( (Step) steps.get(
						i)).getStepSequence() + 1);
				steps.add(index, aStep);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
            throw e;
		}
		return aStep;
	}

	public void deleteStep(int index)
      throws Exception
  {
		if (steps == null)
			this.getSteps();
		if (steps == null)
			return;
		if (index < 0 || index >= steps.size())
			return;
//		Step preStep = (Step) steps.get(index - 1);
//		Step aStep = (Step) steps.get(index);
//		Step nextStep = (Step) steps.get(index+1);

        Step preStep = null;
        if (index != 0)
          preStep = (Step) steps.get(index - 1);
        Step aStep = (Step) steps.get(index);

        Step nextStep=null;
        if (index+1 < steps.size())
          nextStep= (Step) steps.get(index+1);

		String sql = null;
		try
		{
			if(nextStep != null)
			{
				sql = "update ap_processstep set STEPSEQUENCE=STEPSEQUENCE-1"
					+ " where PROCESSTYPEOID='" + this.getProcessOID()
					+ "' and STEPSEQUENCE >= " + nextStep.getStepSequence();
				DBUtil.execSql(conn, sql);
			}
			sql = "delete from ap_processstep where PROCESSTYPEOID='" + this.getProcessOID()
				+ "' and STEPOID='"+aStep.getStepOID()+"' and STEPSEQUENCE='"+aStep.getStepSequence()+"'";
			DBUtil.execSql(conn,sql);
			sql = "update ap_processstep set NEXTSTEPOID='"+ (nextStep == null? 0:nextStep.getStepOID())
				+ "' where PROCESSTYPEOID='" + this.getProcessOID()
				+ "' and STEPOID='"+(preStep == null? 0:preStep.getStepOID())+"'";
			DBUtil.execSql(conn,sql);
			sql = "update ap_processstep set PRESTEPOID='"+ (preStep == null? 0:preStep.getStepOID())
				+ "' where PROCESSTYPEOID='" + this.getProcessOID()
				+ "' and stepoid='" + (nextStep == null? 0:nextStep.getStepOID())+"'";
			DBUtil.execSql(conn,sql);
			if(preStep != null)
				if(nextStep != null)
				{
					preStep.setNextStepOID(nextStep.getStepOID());
					nextStep.setPreStepOID(preStep.getStepOID());
				}
				else
					preStep.setNextStepOID(0);
			else
				if(nextStep != null)
					nextStep.setPreStepOID(0);
			for(int i=index;i<steps.size();i++)
				( (Step) steps.get(i)).setStepSequence( ( (Step) steps.get(
						i)).getStepSequence() - 1);
			steps.remove(index);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
            throw e;
		}
	}

	public Step open()
	{
		return null;
	}

	public int close()
	{
		return 0;
	}
    public int close(TaskVO vo)
    {
        return 0;
    }

	public void delete()
      throws Exception
  {
		try
		{
			DBUtil.execSql(conn,"update ap_processtype set STATE='"+Process.AP_INVALID+"' where PROCESSTYPEOID='"+this.getProcessOID()+"'");
			//DBUtil.execSql(conn,"update processdetail set state=2 where");
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
            throw e;
		}
	}

	public Step createStep(String name,  String actor,String role,
						   String deptid, String level, String application,
						   String condition1, String condition2,
						   String condition3, String memo)
	{
		int oid = Pub.toInt(DBUtil.query("select ap_task_s.nextval from dual")[0][0]);
		ProcessStep step = new ProcessStep();
        step.setConnection(conn);
		step.setName(name);
		step.setRole(role);
		step.setActor(actor);
		step.setDeptID(deptid);
		step.setDeptLevel(Pub.toInt(level));
		step.setApplication(application);
		step.setPreCondition1(condition1);
		step.setPreCondition2(condition2);
		step.setPreCondition3(condition3);
		step.setMemo(memo);
		step.setProcessOID(this.getProcessOID());
		step.setState(Step.AS_CREATED);
		step.setStepOID(oid);
		return step;
	}

	public Step createStep(String name, String actor,String role,
						   String deptid, String level, String application)
	{
		return this.createStep(name,actor,role,deptid,level,application,"","","","");
	}
	public Step createStep(String name,String actor,String role,String deptid)
	{
		return this.createStep(name,actor,role,deptid,"","");
	}


	public String getProcessOID()
	{
		return this.processTypeOID==null?"":this.processTypeOID;
	}

	public String getEventID()
	{
		return "";
	}

	public Date getCloseTime()
	{
		return null;
	}
	public int execute(Connection conn,String actor, String role, String deptId,String memo)
	{
		return 0;
	}
	public void rollback()
	{
	}

	public void rollbackTo(int index)
	{
	}

  public void setConnection(Connection conn)
  {
    this.conn = conn;
  }

  public Connection getConnection()
  {
    return this.conn;
  }

  public String getTaskOID()
  {
    return "";
  }

  public String getTaskSequence()
  {
    return "";
  }

  public int getResult()
  {
    return 0;
  }

  public String getResultDscr()
  {
    return "";
  }

  public String getProcessEvent()
  {
    return this.processEvent==null?"":this.processEvent;
  }
  public void setProcessEvent(String event)
  {
    this.processEvent = event;
  }

  public Step execute(Connection conn,String actor, String deptId, String taskSeq,
                      String result, String resultDscr)
  {
    return null;
  }

  public Step execute(Connection conn,String actor, String role, String deptId, String memo,
                      String taskSeq, String result, String resutlDscr)
  {
    return null;
  }

  public Step getNextStep()
  {
    return null;
  }

  public int finished(int state)
  {
    return 0;
  }
//add by cbl start
public String getProcesstype() {
	// TODO Auto-generated method stub
	return processtype==null?"":processtype;
}

public void setProcesstype(String aprocesstype)
{
	  processtype = aprocesstype;
}
//add by cbl end
@Override
public Step execute(Connection conn, String actor, String deptId,
		String taskId, String taskSeq, String result, String resultDscr)
		throws Exception {
	// TODO Auto-generated method stub
	return null;
}
@Override
public void deleteLastStep() throws Exception {
	// TODO Auto-generated method stub
	
}
}
