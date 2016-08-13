//Source file: D:\\projects\\rkyw\\src\\com\\ccthanking\\aplink\\ProcessInfo.java

package com.ccthanking.framework.coreapp.aplink;

import java.util.*;
import java.sql.SQLException;
import java.sql.Connection;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.util.Pub;

/**
 @author leo <oss@tom.com>
 处理流程信息
 */
public class ProcessInfo
    implements Process
{
  private String processOID;
  private String processTypeOID;
  private Date createTime;
  private Date closeTime;
  private String createdEventID;
  private int state;
  private String operationOID;
  private String memo;
  private String taskOID;
  private String taskSequence;
  private int result;
  private String resultDscr;
  private String processEvent;
  private ArrayList steps;
  private String fBs = "";
  //add by cbl start
  private String processtype;
  //add by cbl end
  public String getFbs()
  {
      return this.fBs;
  }
  public void setFbs(String fBs)
  {
      this.fBs = fBs;
  }
  /**
    @roseuid 42E0449F0165
   */
  public ProcessInfo()
  {

  }

  public ProcessInfo(Connection conn)
  {
    this.conn = conn;
  }

  /**
    Access method for the processOID property.
    @return   the current value of the processOID property
   */
  public String getProcessOID()
  {
    return processOID==null?"":processOID;
  }

  /**
    Sets the value of the processOID property.
    @param aProcessOID the new value of the processOID property
   */
  public void setProcessOID(String aProcessOID)
  {
    processOID = aProcessOID;
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
    Access method for the closeTime property.
    @return   the current value of the closeTime property
   */
  public Date getCloseTime()
  {
    return closeTime;
  }

  /**
    Sets the value of the closeTime property.
    @param aCloseTime the new value of the closeTime property
   */
  public void setCloseTime(Date aCloseTime)
  {
    closeTime = aCloseTime;
  }

  /**
    Access method for the createdEventID property.
    @return   the current value of the createdEventID property
   */
  public String getCreatedEventID()
  {
    return createdEventID==null?"":createdEventID;
  }

  /**
    Sets the value of the createdEventID property.
    @param aCreatedEventID the new value of the createdEventID property
   */
  public void setCreatedEventID(String aCreatedEventID)
  {
    createdEventID = aCreatedEventID;
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
    Access method for the operationOID property.
    @return   the current value of the operationOID property
   */
  public String getOperationOID()
  {
    return operationOID==null?"":operationOID;
  }

  /**
    Sets the value of the operationOID property.
    @param aOperationOID the new value of the operationOID property
   */
  public void setOperationOID(String aOperationOID)
  {
    operationOID = aOperationOID;
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
    ArrayList arr = null;
    Connection conn = this.getConnection();
    try
    {
      String sql =
          "select PROCESSOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,"
          + "to_char(CREATETIME,'YYYY-MM-DD HH24:MI:SS'),to_char(CLOSETIME,'YYYY-MM-DD HH24:MI:SS'),"
          + "to_char(PROCESSTIME,'YYYY-MM-DD HH24:MI:SS'),PRESTEPOID,NEXTSTEPOID,PRECONDITION2,"
          +
          "PRECONDITION3,PRECONDITION1,DEPTID,PROCESSTYPEOID,deptlevel,APPLICATION,MEMO,"
          +
          "STEPEVENT,TASKOID,TASKSEQ,RESULT,RESULTDSCR,SHEDULE_DAYS,to_char(SHEDULE_TIME,'YYYY-MM-DD HH24:MI:SS'),isms,iscc,iswp,ccactor,ISTH from ap_processdetail "
          + " where PROCESSOID='" + this.processOID +
          "' order by STEPSEQUENCE";
      String[][] list = DBUtil.querySql(conn, sql);
      if (list != null)
      {
        arr = new ArrayList(list.length);
        for (int i = 0; i < list.length; i++)
        {
          if(Pub.toInt(list[i][6]) == Step.AS_INVALID) continue;
          ProcessDetail step = new ProcessDetail(conn);
          step.setProcessOID(this.processOID);
          step.setStepSequence(Pub.toInt(list[i][1]));
          step.setStepOID(Pub.toInt(list[i][2]));
          step.setName(list[i][3]);
          step.setRole(list[i][4]);
          step.setActor(list[i][5]);
          step.setState(Pub.toInt(list[i][6]));
          step.setCreateTime(Pub.toDate("yyyy-MM-dd HH:mm:ss",
                                        list[i][7]));
          step.setCloseTime(Pub.toDate("yyyy-MM-dd HH:mm:ss",
                                       list[i][8]));
          step.setProcessTime(Pub.toDate("yyyy-MM-dd HH:mm:ss",
                                         list[i][9]));
          step.setPreStepOID(Pub.toInt(list[i][10]));
          step.setNextStepOID(Pub.toInt(list[i][11]));
          step.setPreCondition2(list[i][12]);
          step.setPreCondition3(list[i][13]);
          step.setPreCondition1(list[i][14]);
          step.setDeptID(list[i][15]);
          step.setProcessTypeOID(list[i][16]);
          step.setDeptLevel(Pub.toInt(list[i][17]));
          step.setApplication(list[i][18]);
          step.setMemo(list[i][19]);
          step.setStepEvent(list[i][20]);
          step.setTaskOID(list[i][21]);
          step.setTaskSequence(list[i][22]);
          step.setResult(Pub.toInt(list[i][23]));
          step.setResultDscr(list[i][24]);
          //add by cbl start
          step.setShedule_days(list[i][25]);
          step.setShedule_time(Pub.toDate("yyyy-MM-dd HH:mm:ss",list[i][26]));
          //add by cbl end
          step.setIsMS(list[i][27]);
          step.setIsCC(list[i][28]);
          step.setIsWP(list[i][29]);
          step.setCcActor(list[i][30]);
          // add bu xhb start
          step.setIsTh(list[i][31]);
          // add bu xhb end
          arr.add(step);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace(System.out);
      return null;
    }
    steps = arr;
    return arr;
  }

  public Step getFirstStep()
  {
    if (steps == null)
      this.getSteps();
    if (steps == null)
      return null;
    return (Step) steps.get(0);
  }

  public Step getLastStep()
  {
    if (steps == null)
      this.getSteps();
    if (steps == null)
      return null;
    return (Step) steps.get(steps.size() - 1);
  }

  public Step getStepByID(int id)
  {
    if (steps == null)
      this.getSteps();
    if (steps == null)
      return null;
    for (int i = 0; i < steps.size(); i++)
      if ( ( (Step) steps.get(i)).getStepOID() == id)
        return (Step) steps.get(i);
    return null;
  }
  public Step getNextStep()
  {
    Step step = this.open();
    if(step == null) return null;
    else
      return this.getStepByIndex(step.getStepSequence()+1);
  }
  public Step getStepByIndex(int index)
  {
    if (steps == null)
      this.getSteps();
    if (steps == null)
      return null;
    for (int i = 0; i < steps.size(); i++)
      if ( ( (Step) steps.get(i)).getStepSequence() == index)
        return (Step) steps.get(i);
    return null;
  }

  public Step appendStep(Step aStep)
      throws Exception
  {
    if (steps == null)
      this.getSteps();
    if (aStep == null)
      return null;
    if (steps == null)
      steps = new ArrayList(1);
    if (aStep.getConnection() == null)
      aStep.setConnection(conn);
    Step lastStep = (steps.size() < 1) ? null :
        (Step) steps.get(steps.size() - 1);
    if (lastStep != null)
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
    Connection conn = this.getConnection();
    try
    {
      String sql = "insert into ap_processdetail cols(PROCESSOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,"
          +
          "CREATETIME,CLOSETIME,PROCESSTIME,PRESTEPOID,NEXTSTEPOID,PRECONDITION2,"
          +
          "PRECONDITION3,PRECONDITION1,DEPTID,PROCESSTYPEOID,deptlevel,APPLICATION,MEMO,STEPEVENT,TASKOID,TASKSEQ,RESULT,RESULTDSCR) "
          + " values(" + this.getProcessOID() + "," +
          aStep.getStepSequence()
          + "," + aStep.getStepOID() + ",'" + aStep.getName() + "','" +
          aStep.getRole() + "','" + aStep.getActor()
          + "'," + aStep.getState() + ",sysdate,null,null," +
          aStep.getPreStepOID() + ",0,'" + aStep.getPreCondition2()
          + "','" + aStep.getPreCondition3() + "','" +
          aStep.getPreCondition1() + "','" + aStep.getDeptID()
          + "'," + aStep.getProcessTypeOID() + ",'"
          + aStep.getDeptLevel()
          + "','" + aStep.getApplication() + "','" + aStep.getMemo() +
          "','" + (aStep.getStepEvent() == null ? "" : aStep.getStepEvent())
          + "','" + aStep.getTaskOID() + "','" + aStep.getTaskSequence() + "',"
          + "'" + aStep.getResult() + "','" + aStep.getResultDscr() + "')";

      DBUtil.execSql(conn, sql);
      if (lastStep != null)
      {
        sql = "update ap_processdetail set NEXTSTEPOID=" +
            lastStep.getNextStepOID()
            + " where PROCESSOID='" + this.getProcessOID() +
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
    if (steps == null)
    {
      return this.appendStep(aStep);
    }
    if (aStep.getConnection() == null)
      aStep.setConnection(conn);
    String sql = null;
    try
    {
      if (index == 0) //插入头节点
      {
        aStep.setPreStepOID(0);
        sql = "update ap_processdetail set STEPSEQUENCE=STEPSEQUENCE+1"
            + " where PROCESSOID='" + this.getProcessOID()
            + "' and STEPSEQUENCE > 0 ";
        DBUtil.exec(conn, sql);
        sql = "insert into ap_processdetail cols(PROCESSOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,"
            +
            "CREATETIME,CLOSETIME,PROCESSTIME,PRESTEPOID,NEXTSTEPOID,PRECONDITION2,"
            +
            "PRECONDITION3,PRECONDITION1,DEPTID,PROCESSTYPEOID,deptlevel,APPLICATION,MEMO,STEPEVENT,TASKOID,TASKSEQ,RESULT,RESULTDSCR) "
            + " values(" + this.getProcessOID() + "," +
            1 //aStep.getStepSequence()
            + "," + aStep.getStepOID() + ",'" + aStep.getName() + "','" +
            aStep.getRole() + "','" + aStep.getActor()
            + "'," + aStep.getState() + ",sysdate,null,null," +
            0 + "," +
            ( (steps.size() > 0) ? ( (Step) steps.get(0)).getStepOID() :
             0) + ",'" + aStep.getPreCondition2()
            + "','" + aStep.getPreCondition3() + "','" +
            aStep.getPreCondition1() + "','" + aStep.getDeptID()
            + "'," + aStep.getProcessTypeOID() + ",'"
            + aStep.getDeptLevel()
            + "','" + aStep.getApplication() + "','" + aStep.getMemo() +
            "','" + (aStep.getStepEvent() == null ? "" : aStep.getStepEvent())
            + "','" + aStep.getTaskOID() + "','" + aStep.getTaskSequence() + "',"
          + "'" + aStep.getResult() + "','" + aStep.getResultDscr() + "')";

        DBUtil.execSql(conn, sql);
        sql = "update ap_processdetail set PRESTEPOID=" + aStep.getStepOID()
            + " where PROCESSOID='" + this.getProcessOID()
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
        sql = "insert into ap_processdetail cols(PROCESSOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,"
            +
            "CREATETIME,CLOSETIME,PROCESSTIME,PRESTEPOID,NEXTSTEPOID,PRECONDITION2,"
            +
            "PRECONDITION3,PRECONDITION1,DEPTID,PROCESSTYPEOID,deptlevel,APPLICATION,MEMO,STEPEVENT,TASKOID,TASKSEQ,RESULT,RESULTDSCR) "
            + " values('" + this.getProcessOID() + "'," +
            ( (steps.size() > 0) ?
             ( (Step) steps.get(steps.size() - 1)).getStepSequence() +
             1 : 1)
            + "," + aStep.getStepOID() + ",'" + aStep.getName() + "','" +
            aStep.getRole() + "','" + aStep.getActor()
            + "'," + aStep.getState() + ",sysdate,null,null," +
            ( (steps.size() > 0) ?
             ( (Step) steps.get(steps.size() - 1)).getStepOID() : 0) +
            "," + 0 + ",'" + aStep.getPreCondition2()
            + "','" + aStep.getPreCondition3() + "','" +
            aStep.getPreCondition1() + "','" + aStep.getDeptID()
            + "'," + aStep.getProcessTypeOID() + ",'"
            + aStep.getDeptLevel()
            + "','" + aStep.getApplication() + "','" + aStep.getMemo() +
            "','" + (aStep.getStepEvent() == null ? "" : aStep.getStepEvent())
            + "','" + aStep.getTaskOID() + "','" + aStep.getTaskSequence() + "',"
          + "'" + aStep.getResult() + "','" + aStep.getResultDscr() + "')";

        DBUtil.execSql(conn, sql);
        sql = "update ap_processdetail set NEXTSTEPOID=" +
            aStep.getStepOID()
            + " where PROCESSOID='" + this.getProcessOID()
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
        sql = "update ap_processdetail set STEPSEQUENCE=STEPSEQUENCE+1"
            + " where PROCESSOID='" + this.getProcessOID()
            + "' and STEPSEQUENCE >= " + nextStep.getStepSequence();
        DBUtil.execSql(conn, sql);
        sql = "insert into ap_processdetail cols(PROCESSOID,STEPSEQUENCE,STEPOID,NAME,ROLENAME,ACTOR,STATE,"
            +
            "CREATETIME,CLOSETIME,PROCESSTIME,PRESTEPOID,NEXTSTEPOID,PRECONDITION2,"
            +
            "PRECONDITION3,PRECONDITION1,DEPTID,PROCESSTYPEOID,deptlevel,APPLICATION,MEMO,STEPEVENT,TASKOID,TASKSEQ,RESULT,RESULTDSCR) "
            + " values(" + this.getProcessOID() + "," +
            nextStep.getStepSequence()
            + "," + aStep.getStepOID() + ",'" + aStep.getName() + "','" +
            aStep.getRole() + "','" + aStep.getActor()
            + "'," + aStep.getState() + ",sysdate,null,null,"
            + preStep.getStepOID() +
            "," + nextStep.getStepOID() + ",'" + aStep.getPreCondition2()
            + "','" + aStep.getPreCondition3() + "','" +
            aStep.getPreCondition1() + "','" + aStep.getDeptID()
            + "'," + aStep.getProcessTypeOID() + ",'"
            + aStep.getDeptLevel()
            + "','" + aStep.getApplication() + "','" + aStep.getMemo() +
            "','" + (aStep.getStepEvent() == null ? "" : aStep.getStepEvent())
            + "','" + aStep.getTaskOID() + "','" + aStep.getTaskSequence() + "',"
          + "'" + aStep.getResult() + "','" + aStep.getResultDscr() + "')";
        DBUtil.execSql(conn, sql);
        sql = "update ap_processdetail set NEXTSTEPOID='" +
            aStep.getStepOID()
            + "' where PROCESSOID=" + this.getProcessOID()
            + " and STEPOID='" + preStep.getStepOID()
            + "' and STEPSEQUENCE='"+preStep.getStepSequence()+"'";
        DBUtil.execSql(conn, sql);
        sql = "update ap_processdetail set PRESTEPOID='" +
            aStep.getStepOID()
            + "' where PROCESSOID='" + this.getProcessOID()
            + "' and STEPOID='" + nextStep.getStepOID()
            + "' and STEPSEQUENCE='"+(nextStep.getStepSequence()+1)+"'";
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
  
  public void deleteLastStep()throws Exception
  {
	  if (steps == null)
	      this.getSteps();
	    if (steps == null)
	      return;
	    Step preStep = steps.get(steps.size()-2)==null?null:(Step) steps.get(steps.size()-2);
	    Step aStep = steps.get(steps.size()-1)==null?null:(Step) steps.get(steps.size()-1);
	    if(aStep == null) return;
	    String sql = null;
	    try
	    {
	      

	      sql = "update ap_processdetail set state='"+Step.AS_INVALID+"' where PROCESSOID='" +
	          this.getProcessOID()
	          + "' and STEPOID='" + aStep.getStepOID()+"' and STEPSEQUENCE='"+aStep.getStepSequence()+"'";
	      DBUtil.execSql(conn, sql);
	      //////////////////////////////////////////////////////////
	      sql = "update ap_processdetail set NEXTSTEPOID='0' where PROCESSOID='" + this.getProcessOID()
	          + "' and STEPOID='" + (preStep == null ? 0 : preStep.getStepOID())+"' and STEPSEQUENCE='"+preStep.getStepSequence()+"'";
	      DBUtil.execSql(conn, sql);
	    
	      if (preStep != null)
	          preStep.setNextStepOID(0);
	      for (int i = steps.size()-1; i < steps.size(); i++)
	        ( (Step) steps.get(i)).setStepSequence( ( (Step) steps.get(i)).getStepSequence() - 1);
	      steps.remove(steps.size()-1);
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace(System.out);
	      throw e;
	    }
	    
  }

  public void deleteStep(int index) throws Exception
  {
    if (steps == null)
      this.getSteps();
    if (steps == null)
      return;
    if (index < 0 || index > steps.size())
      return;
    Step preStep = steps.get(index - 1)==null?null:(Step) steps.get(index - 1);
    Step aStep = steps.get(index)==null?null:(Step) steps.get(index);
    Step nextStep = steps.get(index + 1)==null?null:(Step) steps.get(index + 1);
    if(aStep == null) return;
    String sql = null;
    try
    {
      if (nextStep != null)
      {
        sql = "update ap_processdetail set STEPSEQUENCE=STEPSEQUENCE-1"
            + " where PROCESSOID='" + this.getProcessOID()
            + "' and STEPSEQUENCE >= " + nextStep.getStepSequence();
        DBUtil.execSql(conn, sql);
      }
      //////////////////////////////////////////////////////////
//      sql = "delete from ap_processdetail where PROCESSOID='" +
//          this.getProcessOID()
//          + "' and STEPOID='" + aStep.getStepOID()+"'";
      sql = "update ap_processdetail set state='"+Step.AS_INVALID+"' where PROCESSOID='" +
          this.getProcessOID()
          + "' and STEPOID='" + aStep.getStepOID()+"' and STEPSEQUENCE='"+aStep.getStepSequence()+"'";
      DBUtil.execSql(conn, sql);
      //////////////////////////////////////////////////////////
      sql = "update ap_processdetail set NEXTSTEPOID='" +
          (nextStep == null ? 0 : nextStep.getStepOID())
          + "' where PROCESSOID='" + this.getProcessOID()
          + "' and STEPOID='" + (preStep == null ? 0 : preStep.getStepOID())+"' and STEPSEQUENCE='"+preStep.getStepSequence()+"'";
      DBUtil.execSql(conn, sql);
      sql = "update ap_processdetail set PRESTEPOID='" +
          (preStep == null ? 0 : preStep.getStepOID())
          + "' where PROCESSOID='" + this.getProcessOID()
          + "' and stepoid='" + (nextStep == null ? 0 : nextStep.getStepOID())+"' and STEPSEQUENCE='"+(nextStep.getStepSequence()-1)+"'";
      DBUtil.execSql(conn, sql);
      if (preStep != null)
        if (nextStep != null)
        {
          preStep.setNextStepOID(nextStep.getStepOID());
          nextStep.setPreStepOID(preStep.getStepOID());
        }
        else
          preStep.setNextStepOID(0);
      else
      if (nextStep != null)
        nextStep.setPreStepOID(0);
      for (int i = index; i < steps.size(); i++)
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
    if (steps == null)
      this.getSteps();
    if (steps == null)
      return null;
    for (int i = 0; i < steps.size(); i++)
      if ( ( (Step) steps.get(i)).getState() != 2 &&
          ( (Step) steps.get(i)).getState() != 11)
        return (Step) steps.get(i);
    for (int i = steps.size() - 1; i >= 0; i--)
      if ( ( (Step) steps.get(i)).getState() != 2)
        return (Step) steps.get(i);
    return null;
  }

  public int finished(int state)
        throws Exception
  {
    String resultDscr = "";
    int result = 0;
    if(state <=0)
    {
      if (steps == null)
        this.getSteps();
      if (steps == null)
        state = Process.AP_ERROR;
      if(state != Process.AP_ERROR)
      {
        Step step = null;
        for (int i = 0; i < steps.size(); i++)
        {
          step = (Step) steps.get(i);
          if ( step.getState() != Step.AS_SUCCESS)
          {
            switch ( ( (Step) steps.get(i)).getState())
            {
              case Step.AS_BARENESS:
              case Step.AS_INVALID:
                continue;
              case Step.AS_ERROR:
                state = Process.AP_ERROR;
                break;
              case Step.AS_CREATED:
              case Step.AS_BREAK:
              case Step.AS_PROCESSING:
              default:
                state = Process.AP_BREAK;
                if(i>0) step = (Step) steps.get(i-1);
                break;
            }
            break;
          }
          else
          {
            state = Process.AP_SUCCESS;
            if(step.getResult() != 1) break;
          }
        }
        result = step.getResult();
        resultDscr = step.getResultDscr();
      }
    }
    else
    {
      Step step = this.getLastStep();
      result = step.getResult();
      resultDscr = step.getResultDscr();
    }
    if(resultDscr == null) resultDscr = "";
    try
    {
    	String objs[] = {resultDscr};
      DBUtil.executeUpdate(conn,
                     "update ap_processinfo set result='" + (result==0?"":""+result) +
                     "',STATE='" + state +
                     "',CLOSETIME=sysdate " +
                     ",resultdscr=? "+
                     " where PROCESSOID='" +
                     this.getProcessOID() + "'",objs);
    }
    catch (Exception ex1)
    {
      ex1.printStackTrace();
      state = Process.AP_ERROR;
      throw ex1;
    }
    this.setResult(result);
    this.setResultDscr(resultDscr);
    this.setState(state);
    return state;
  }

  public int close()
  {
    if (steps == null)
      this.getSteps();
    if (steps == null)
      return Process.AP_ERROR;
    int tag = Process.AP_PROCESSING;
    for (int i = 0; i < steps.size(); i++)
    {
      if ( ( (Step) steps.get(i)).getState() != Step.AS_SUCCESS)
      {
        switch ( ( (Step) steps.get(i)).getState())
        {
          case Step.AS_BARENESS:
            continue;
          case Step.AS_INVALID:
            continue;
          case Step.AS_CREATED:
            tag = Process.AP_PROCESSING;
            break;
          case Step.AS_ERROR:
            tag = Process.AP_ERROR;
            break;
          default:
            tag = Process.AP_BREAK;
            break;
        }
        try
        {
          DBUtil.execSql(conn,
                         "update ap_processinfo set result='"+this.getResult()+"',STATE='" + tag +
                         "',CLOSETIME=sysdate "+(this.getResultDscr()!=null?",resultdscr='"+this.getResultDscr()+"'":"")+" where PROCESSOID='" +
                         this.getProcessOID()+"'");
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
          return Process.AP_ERROR;
        }
        this.setState(tag);
        return tag;
      }
    }
    try
    {
      DBUtil.execSql(conn,
                     "update ap_processinfo set result='"+this.getResult()+"',STATE='" + Process.AP_SUCCESS +
                     "',CLOSETIME=sysdate "+(this.getResultDscr()!=null?",resultdscr='"+this.getResultDscr()+"'":"")+" where PROCESSOID='" +
                     this.getProcessOID()+"'");
    }
    catch (Exception ex1)
    {
      ex1.printStackTrace();
      return Process.AP_ERROR;
    }
    this.setState(Process.AP_SUCCESS);
    return Process.AP_SUCCESS;
  }

  public void delete()
      throws Exception
  {
    try
    {
      DBUtil.execSql(conn,
          "update ap_processinfo set STATE='"+Process.AP_INVALID+"',CLOSETIME=sysdate where PROCESSOID='" +
                  this.getProcessOID()+"'");
      //DBUtil.exec(conn,"update processdetail set state=2 where");
    }
    catch (Exception e)
    {
      e.printStackTrace(System.out);
      throw e;
    }
  }

  public String getEventID()
  {
    return this.getCreatedEventID();
  }

  public Step createStep(String name, String actor, String role,
                         String deptid, String level, String application,
                         String condition1, String condition2,
                         String condition3, String memo)
  {
    int oid = Pub.toInt(DBUtil.query("select ap_task_s.nextval from dual")[0][0]);
    ProcessDetail step = new ProcessDetail();
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
    step.setState(1);
    step.setStepOID(oid);
    step.setCreateTime(Pub.getCurrentDate());
    step.setProcessTypeOID(this.getProcessTypeOID());
    return step;
  }

  public Step createStep(String name, String actor, String role,
                         String deptid, String level, String application)
  {
    return this.createStep(name, actor, role , deptid, level, application, "",
                           "", "", "");
  }

  public Step createStep(String name, String actor, String role, String deptid)
  {
    return this.createStep(name, actor, role, deptid, "", "");
  }

  public void rollback()
      throws Exception
  {
    rollbackTo(this.open().getStepSequence() - 1);
  }
  //modify by wangzh for 退回
  public void rollbackTo(int index)
      throws Exception
  {
    if (index < 0)
      index = 0;
//    if (index >= this.open().getStepSequence())
//      return;
    try
    {
//      for (int i = index; i < this.open().getStepSequence(); i++)
//      {
        Step step = (Step) steps.get(index-1);
        Step stepDefine = step.getStepDefine();
        if (stepDefine == null)
          stepDefine = step;
        String sql =
            "update ap_processdetail set STATE=" + Step.AS_CREATED +
            ",CLOSETIME=null "
            + " ,ROLENAME='" + stepDefine.getRole() + "'"
            + " ,ACTOR='" + stepDefine.getActor() + "'"
            + " ,DEPTID='" + stepDefine.getDeptID() + "'"
            + " where PROCESSOID='" + this.getProcessOID()
            + "' and STEPOID='" + step.getStepOID()+"' and STEPSEQUENCE='"+step.getStepSequence()+"'";
        DBUtil.execSql(conn, sql);
//      }
    }
    catch (SQLException e)
    {
      e.printStackTrace(System.out);
      throw e;
    }
  }

  public Step execute(Connection conn,String actor,String deptId,String id,String taskSeq,String result,String resultDscr) throws Exception
  {
    return execute(conn,actor,null,deptId,null,id,taskSeq,result,resultDscr);
  }
  public Step execute(Connection conn ,String aActor,String aRole,String aDeptId,
                      String aMemo,String aTaskId,String aTaskSeq,String aResult,String aResultDscr) throws Exception
  {
    if(Pub.empty(aActor) || Pub.empty(aResult))
    {
      this.setState(Process.AP_ERROR);
      return null;
    }
    Step step = this.open();
    if (step == null) //return Process.AP_ERROR;
    {
      this.setState(Process.AP_ERROR);
      return null;
    }
    if (step.getState() == Step.AS_SUCCESS)
    { //流程结束
      this.setState(Process.AP_SUCCESS);
      return step;
    }
//    if (!Pub.empty(step.getActor()) && !(step.getActor().equals(aActor)||step.getActor().contains(aActor)))
//    {
//      this.setState(Process.AP_ERROR);
//      return null;
//    }
//    if(!Pub.empty(step.getDeptID()) && !Pub.empty(aDeptId) && !step.getDeptID().equals(aDeptId))
//    {
//       this.setState(Process.AP_ERROR);
//       return null;
//    }
//    if(!Pub.empty(step.getRole()) && !Pub.empty(aRole) && !step.getRole().equals(aRole))
//    {
//       this.setState(Process.AP_ERROR);
//       return null;
//    }

    int status = step.getState();
    int res = Process.AP_PROCESSING;
    String sql = null;
    try
    {
        if(ProcessMgr.getStepValid(conn,step,aTaskId,aTaskSeq,aActor))
        {
        	this.setState(res);
        	return step;
        }
      StepEvent handle = step.getHandleEvent();
      if(handle != null)
      {
        handle.onOpen(step);
        handle.onExecute(step);
      }
      
      sql = "update ap_processdetail set state='"+Step.AS_SUCCESS+"',result='"+aResult+"',closetime=sysdate,processtime=sysdate ,actor='"+aActor+"'";
      if(!Pub.empty(aRole)) sql += ",rolename='"+aRole+"'";
      if(!Pub.empty(aDeptId)) sql += ",deptid='"+aDeptId+"'";
      if(!Pub.empty(aMemo)) sql += ",memo='"+aMemo+"'";
      if(!Pub.empty(aResultDscr)) sql+= ",resultdscr=?";
      if(!Pub.empty(aTaskSeq)) sql += ",taskseq='"+aTaskSeq+"'";
      sql += " where PROCESSOID='" + this.getProcessOID() + "' and STEPOID='" +
          step.getStepOID()+"' and STEPSEQUENCE='"+step.getStepSequence()+"'";
      String objs[] = {aResultDscr} ;
      DBUtil.executeUpdate(conn, sql, objs);
      
      if (step.getNextStepOID() == 0)
        res = Process.AP_SUCCESS;
      else
        res = Process.AP_PROCESSING;
      step.setState(Step.AS_SUCCESS);
      step.setActor(aActor);
      if(!Pub.empty(aRole)) step.setRole(aRole);
      if(!Pub.empty(aDeptId)) step.setDeptID(aDeptId);
      if(!Pub.empty(aMemo)) step.setMemo(aMemo);
      if(!Pub.empty(aResultDscr))
      {
        step.setResultDscr(aResultDscr);
        this.setResultDscr(aResultDscr);
      }
      step.setResult(Pub.toInt(aResult));
      this.setResult(step.getResult());
      if(!Pub.empty(aTaskSeq)) step.setTaskSequence(aTaskSeq);
      step.setCloseTime(Pub.getCurrentDate());
      step.setProcessTime(step.getCloseTime());
      this.setState(res);
      if(handle != null)
        handle.onClose(step);
        //conn.commit();
      return step;
    }
    catch (SQLException e)
    {
      conn.rollback();
      e.printStackTrace(System.out);
      this.setState(Process.AP_ERROR);
      //throw e;
    }
    return null;
  }
  public int execute(Connection conn,String actor, String role, String deptId, String aMemo)
      throws Exception
  {
    if (actor == null)
      actor = "";
    if (role == null)
      role = "";
    if (deptId == null)
      deptId = "";
    if (aMemo == null)
      aMemo = "";
    if (Pub.empty(actor) && Pub.empty(role) && Pub.empty(deptId))
    {
      this.setState(Process.AP_ERROR);
      return Process.AP_ERROR;
    }
    Step step = this.open();
    if (step == null) //return Process.AP_ERROR;
    {
      this.setState(Process.AP_ERROR);
      return Process.AP_ERROR;
    }
    if (step.getState() == Step.AS_SUCCESS)
    { //流程结束
      this.setState(Process.AP_SUCCESS);
      return Process.AP_SUCCESS;
    }
    if (!role.equals(step.getRole()) && !actor.equals(step.getActor()) &&
        !deptId.equals(step.getDeptID()))
    {
      this.setState(Process.AP_ERROR);
      return Process.AP_ERROR;
    }
    int status = step.getState();
    int res = Process.AP_PROCESSING;
    String sql = null;
    try
    {
      StepEvent handle = step.getHandleEvent();
      if(handle != null)
      {
        handle.onOpen(step);
        handle.onExecute(step);
      }
      sql = "update ap_processdetail set state=" + Step.AS_SUCCESS
          + ",actor='" + actor + "',role='" + role + "',deptId='" + deptId +
          "'"
          + ",closetime=sysdate,processtime=sysdate,memo='" + aMemo + "'"
          + " where PROCESSOID='" + this.getProcessOID() + "' and STEPOID='" +
          step.getStepOID()+"' and STEPSEQUENCE='"+step.getStepSequence()+"'";
      try
      {
        DBUtil.execSql(conn, sql);
      }
      catch (Exception ex)
      {
        res = Process.AP_ERROR;
        status = Step.AS_ERROR;
        step.setState(status);
        this.setState(res);
        return res;
      }
      if (step.getNextStepOID() == 0)
        res = Process.AP_SUCCESS;
      else
        res = Process.AP_PROCESSING;
      step.setState(Step.AS_SUCCESS);
      step.setActor(actor);
      step.setRole(role);
      step.setDeptID(deptId);
      step.setCloseTime(Pub.getCurrentDate());
      step.setProcessTime(step.getCloseTime());
      this.setState(res);
      if(handle != null)
        handle.onClose(step);
    }
    catch (SQLException e)
    {
      e.printStackTrace(System.out);
      this.setState(Process.AP_ERROR);
      res = Process.AP_ERROR;
      //throw e;
    }
    return res;
  }

  public void setConnection(Connection conn)
  {
    this.conn = conn;
  }

  private Connection conn;
  public Connection getConnection()
  {
    return this.conn;
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
  public void setTaskSequence(String seq)
  {
    this.taskSequence = seq;
  }

  public int getResult()
  {
    return this.result;
  }
  public void setResult(int res)
  {
    this.result = res;
  }
  public String getResultDscr()
  {
    return this.resultDscr==null?"":this.resultDscr;
  }
  public void setResultDscr(String dscr)
  {
    this.resultDscr = dscr;
  }
  public String getProcessEvent()
  {
    return this.processEvent==null?"":this.processEvent;
  }
  public void setProcessEvent(String event)
  {
    this.processEvent = event;
  }
  

@Override
public Step execute(Connection conn, String actor, String role, String deptId,
		String memo, String taskSeq, String result, String resutlDscr)
		throws Exception {
	// TODO Auto-generated method stub
	return null;
}
}