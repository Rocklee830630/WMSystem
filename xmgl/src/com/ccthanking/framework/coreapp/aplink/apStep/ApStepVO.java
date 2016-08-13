package com.ccthanking.framework.coreapp.aplink.apStep;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class ApStepVO extends BaseVO{
  public ApStepVO()
  {

  	  		//设置字段信息
  				this.addField("ACTOR",this.OP_STRING);//操作员帐号
  				this.addField("DEPTID",this.OP_STRING);//部门ID
  				this.addField("DEPTLEVEL",this.OP_STRING);//级别(部门)
  				this.addField("MEMO",this.OP_STRING);//备注
  				this.addField("NAME",this.OP_STRING);//节点名称
  				this.addField("ROLENAME",this.OP_STRING);//角色
  				this.addField("STEPOID",this.TP_PK);//节点编号
  		  	//设置主键信息
    	   //设置字典类型定义
    		//设置时间的显示格式
     //设置表名称
      //设置表名称
      this.setVOTableName("AP_STEP");

  }
     /**
   * 设置操作员帐号
   * @param actor String
   */
  public void setActor(String actor){
    this.setInternal("ACTOR" ,actor );//操作员帐号
  }
     /**
   * 设置部门ID
   * @param deptid String
   */
  public void setDeptid(String deptid){
    this.setInternal("DEPTID" ,deptid );//部门ID
  }
     /**
   * 设置级别(部门)
   * @param deptlevel String
   */
  public void setDeptlevel(String deptlevel){
    this.setInternal("DEPTLEVEL" ,deptlevel );//级别(部门)
  }
     /**
   * 设置备注
   * @param memo String
   */
  public void setMemo(String memo){
    this.setInternal("MEMO" ,memo );//备注
  }
     /**
   * 设置节点名称
   * @param name String
   */
  public void setName(String name){
    this.setInternal("NAME" ,name );//节点名称
  }
     /**
   * 设置角色
   * @param rolename String
   */
  public void setRolename(String rolename){
    this.setInternal("ROLENAME" ,rolename );//角色
  }
     /**
   * 设置节点编号
   * @param stepoid String
   */
  public void setStepoid(String stepoid){
    this.setInternal("STEPOID" ,stepoid );//节点编号
  }

    /**
   * 获得操作员帐号
   * @return String
   */
  public String getActor(){
    return (String)this.getInternal("ACTOR");
  }

  /**
   * 获得部门ID
   * @return String
   */
  public String getDeptid(){
    return (String)this.getInternal("DEPTID");
  }

  /**
   * 获得级别(部门)
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
   * 获得节点名称
   * @return String
   */
  public String getName(){
    return (String)this.getInternal("NAME");
  }

  /**
   * 获得角色
   * @return String
   */
  public String getRolename(){
    return (String)this.getInternal("ROLENAME");
  }

  /**
   * 获得节点编号
   * @return String
   */
  public String getStepoid(){
    return (String)this.getInternal("STEPOID");
  }


}
