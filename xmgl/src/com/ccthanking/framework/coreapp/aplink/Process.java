
package com.ccthanking.framework.coreapp.aplink;

import java.util.Collection;
import java.util.Date;
import java.sql.Connection;

/**
 @author leo <oss@tom.com>
 */
public interface Process
{
	public static final int AP_BARENESS = 0;
	public static final int AP_CREATED = 1;
	public static final int AP_INVALID = 2;
	public static final int AP_PROCESSING = 10;
	public static final int AP_SUCCESS = 11;
	public static final int AP_ERROR = 12;
	public static final int AP_BREAK = 13;

    public void setConnection(Connection conn);
    public Connection getConnection();
	public Collection getSteps();
	public Step getFirstStep();
	public Step getStepByID(int id);
	public Step getStepByIndex(int index);
	public Step getLastStep();
	public Step appendStep(Step step) throws Exception;
	public Step insertStep(int index, Step step) throws Exception;
	public void deleteStep(int index) throws Exception;
	public void deleteLastStep() throws Exception;
	public int execute(Connection conn,String actor,String role,String deptId,String memo) throws Exception;
    public Step execute(Connection conn,String actor,String deptId,String taskId,String taskSeq,String result,String resultDscr) throws Exception;
    public Step execute(Connection conn,String actor,String role,String deptId,
                       String memo,String taskSeq,String result,String resutlDscr) throws Exception;
   	public Step getNextStep();
	public Step open();
	public int close() throws Exception;
	public int finished(int state) throws Exception;
	public void delete() throws Exception;
	public Step createStep(String name,  String actor,String role,
						   String deptid, String level, String application,
						   String condition1, String condition2,
						   String condition3, String memo);
	public Step createStep(String name,  String actor,String role,
						   String deptid, String level, String application);
	public Step createStep(String name,String actor,String role,String deptid);
	public int getState();
	public void setMemo(String memo);
	public String getProcessOID();
	public String getEventID();
	public String getProcessTypeOID();
	public Date getCreateTime();
	public Date getCloseTime();
	public String getMemo();
	public abstract void rollback()throws Exception;
	public abstract void rollbackTo(int index)throws Exception;
    public String getTaskOID();
    public String getTaskSequence();
    public int getResult();
    public String getResultDscr();
    public String getOperationOID();
    public String getProcessEvent();
    public String getFbs();
}