package com.ccthanking.framework.coreapp.aplink.ApProcessTemplete;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class ApProcessTempleteVO extends BaseVO{
  public ApProcessTempleteVO()
  {

  	  		//设置字段信息
  				this.addField("ACTOR",this.OP_STRING);//操作者
  				this.addField("APPLICATION",this.OP_STRING);//应用
  				this.addField("DEPTID",this.OP_STRING);//部门标识
  				this.addField("DEPTLEVEL",this.OP_STRING);//部门级别
  				this.addField("MEMO",this.OP_STRING);//备注
  				this.addField("NEXTSTEPOID",this.OP_STRING);//下一节点
  				this.addField("PRECONDITION1",this.OP_STRING);//条件1
  				this.addField("PRECONDITION2",this.OP_STRING);//条件2
  				this.addField("PRECONDITION3",this.OP_STRING);//条件3
  				this.addField("PRESTEPOID",this.OP_STRING);//上一节点
  				this.addField("PROCESSTYPEOID",this.OP_STRING);//流程定义编号
  				this.addField("PROCNAME",this.OP_STRING);//流程名称
  				this.addField("ROLENAME",this.OP_STRING);//角色
  				this.addField("STATE",this.OP_STRING);//状态
  				this.addField("STEPEVENT",this.OP_STRING);//节点事件
  				this.addField("STEPNAME",this.OP_STRING);//节点名称
  				this.addField("STEPOID",this.OP_STRING);//节点编号
  				this.addField("STEPSEQUENCE",this.OP_STRING);//节点顺序号
  		  	//设置主键信息
    	   //设置字典类型定义
    		//设置时间的显示格式
     //设置表名称
      //设置表名称
      this.setVOTableName("AP_PROCESS_TEMPLETE");

  }
     /**
   * 设置操作者
   * @param actor String
   */
  public void setActor(String actor){
    this.setInternal("ACTOR" ,actor );//操作者
  }
     /**
   * 设置应用
   * @param application String
   */
  public void setApplication(String application){
    this.setInternal("APPLICATION" ,application );//应用
  }
     /**
   * 设置部门标识
   * @param deptid String
   */
  public void setDeptid(String deptid){
    this.setInternal("DEPTID" ,deptid );//部门标识
  }
     /**
   * 设置部门级别
   * @param deptlevel String
   */
  public void setDeptlevel(String deptlevel){
    this.setInternal("DEPTLEVEL" ,deptlevel );//部门级别
  }
     /**
   * 设置备注
   * @param memo String
   */
  public void setMemo(String memo){
    this.setInternal("MEMO" ,memo );//备注
  }
     /**
   * 设置下一节点
   * @param nextstepoid String
   */
  public void setNextstepoid(String nextstepoid){
    this.setInternal("NEXTSTEPOID" ,nextstepoid );//下一节点
  }
     /**
   * 设置条件1
   * @param precondition1 String
   */
  public void setPrecondition1(String precondition1){
    this.setInternal("PRECONDITION1" ,precondition1 );//条件1
  }
     /**
   * 设置条件2
   * @param precondition2 String
   */
  public void setPrecondition2(String precondition2){
    this.setInternal("PRECONDITION2" ,precondition2 );//条件2
  }
     /**
   * 设置条件3
   * @param precondition3 String
   */
  public void setPrecondition3(String precondition3){
    this.setInternal("PRECONDITION3" ,precondition3 );//条件3
  }
     /**
   * 设置上一节点
   * @param prestepoid String
   */
  public void setPrestepoid(String prestepoid){
    this.setInternal("PRESTEPOID" ,prestepoid );//上一节点
  }
     /**
   * 设置流程定义编号
   * @param processtypeoid String
   */
  public void setProcesstypeoid(String processtypeoid){
    this.setInternal("PROCESSTYPEOID" ,processtypeoid );//流程定义编号
  }
     /**
   * 设置流程名称
   * @param procname String
   */
  public void setProcname(String procname){
    this.setInternal("PROCNAME" ,procname );//流程名称
  }
     /**
   * 设置角色
   * @param rolename String
   */
  public void setRolename(String rolename){
    this.setInternal("ROLENAME" ,rolename );//角色
  }
     /**
   * 设置状态
   * @param state String
   */
  public void setState(String state){
    this.setInternal("STATE" ,state );//状态
  }
     /**
   * 设置节点事件
   * @param stepevent String
   */
  public void setStepevent(String stepevent){
    this.setInternal("STEPEVENT" ,stepevent );//节点事件
  }
     /**
   * 设置节点名称
   * @param stepname String
   */
  public void setStepname(String stepname){
    this.setInternal("STEPNAME" ,stepname );//节点名称
  }
     /**
   * 设置节点编号
   * @param stepoid String
   */
  public void setStepoid(String stepoid){
    this.setInternal("STEPOID" ,stepoid );//节点编号
  }
     /**
   * 设置节点顺序号
   * @param stepsequence String
   */
  public void setStepsequence(String stepsequence){
    this.setInternal("STEPSEQUENCE" ,stepsequence );//节点顺序号
  }

    /**
   * 获得操作者
   * @return String
   */
  public String getActor(){
    return (String)this.getInternal("ACTOR");
  }

  /**
   * 获得应用
   * @return String
   */
  public String getApplication(){
    return (String)this.getInternal("APPLICATION");
  }

  /**
   * 获得部门标识
   * @return String
   */
  public String getDeptid(){
    return (String)this.getInternal("DEPTID");
  }

  /**
   * 获得部门级别
   * @return String
   */
  public String getDeptlevel(){
    return (String)this.getInternal("DEPTLEVEL");
  }

  /**
   * 获得备注
   * @return String
   */
  public String getMemo(){
    return (String)this.getInternal("MEMO");
  }

  /**
   * 获得下一节点
   * @return String
   */
  public String getNextstepoid(){
    return (String)this.getInternal("NEXTSTEPOID");
  }

  /**
   * 获得条件1
   * @return String
   */
  public String getPrecondition1(){
    return (String)this.getInternal("PRECONDITION1");
  }

  /**
   * 获得条件2
   * @return String
   */
  public String getPrecondition2(){
    return (String)this.getInternal("PRECONDITION2");
  }

  /**
   * 获得条件3
   * @return String
   */
  public String getPrecondition3(){
    return (String)this.getInternal("PRECONDITION3");
  }

  /**
   * 获得上一节点
   * @return String
   */
  public String getPrestepoid(){
    return (String)this.getInternal("PRESTEPOID");
  }

  /**
   * 获得流程定义编号
   * @return String
   */
  public String getProcesstypeoid(){
    return (String)this.getInternal("PROCESSTYPEOID");
  }

  /**
   * 获得流程名称
   * @return String
   */
  public String getProcname(){
    return (String)this.getInternal("PROCNAME");
  }

  /**
   * 获得角色
   * @return String
   */
  public String getRolename(){
    return (String)this.getInternal("ROLENAME");
  }

  /**
   * 获得状态
   * @return String
   */
  public String getState(){
    return (String)this.getInternal("STATE");
  }

  /**
   * 获得节点事件
   * @return String
   */
  public String getStepevent(){
    return (String)this.getInternal("STEPEVENT");
  }

  /**
   * 获得节点名称
   * @return String
   */
  public String getStepname(){
    return (String)this.getInternal("STEPNAME");
  }

  /**
   * 获得节点编号
   * @return String
   */
  public String getStepoid(){
    return (String)this.getInternal("STEPOID");
  }

  /**
   * 获得节点顺序号
   * @return String
   */
  public String getStepsequence(){
    return (String)this.getInternal("STEPSEQUENCE");
  }


}
