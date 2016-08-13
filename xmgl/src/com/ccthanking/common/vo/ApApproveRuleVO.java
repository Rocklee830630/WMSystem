package com.ccthanking.common.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class ApApproveRuleVO extends BaseVO{
  public ApApproveRuleVO()
  {

  	  		//设置字段信息
  				this.addField("RULE_NAME",this.OP_STRING);//规则名称
  				this.addField("UNITID",this.OP_STRING);//部门代码
  				this.addField("YWLX",this.OP_STRING);//业务类型
  				this.addField("OPERATION_OID",this.OP_STRING);//操作类型
  				this.addField("PCSDM",this.OP_STRING);//派出所代码
  				this.addField("FJDM",this.OP_STRING);//分局代码
  				this.addField("SJDM",this.OP_STRING);//市局代码
  				this.addField("SSXQ",this.OP_STRING);//省市县区
  				this.addField("VALUE1",this.OP_STRING);//值1
  				this.addField("VALUE2",this.OP_STRING);//值2
  				this.addField("VALUE3",this.OP_STRING);//值3
  				this.addField("MEMO",this.OP_STRING);//规则描述
  				this.addField("RULE_OID",this.OP_STRING|this.TP_PK);//ID
  		  	//设置主键信息
            this.addField("RULE_OID",this.TP_PK);//ID
            this.bindFieldToSequence("RULE_OID","AP_TASK_S");
    	   //设置字典类型定义
    			this.bindFieldToOrgDept("FJDM");//分局代码
    			this.bindFieldToOrgDept("PCSDM");//派出所代码
    			this.bindFieldToOrgDept("SJDM");//市局代码
    			this.bindFieldToOrgDept("SSXQ");//省市县区
    			this.bindFieldToOrgDept("UNITID");//部门代码
    			this.bindFieldToDic("YWLX","YWLX");//业务类型
    		//设置时间的显示格式
     //设置表名称
      //设置表名称
      this.setVOTableName("AP_APPROVE_RULE");

  }
     /**
   * 设置规则名称
   * @param ruleName String
   */
  public void setRuleName(String ruleName){
    this.setInternal("RULE_NAME" ,ruleName );//规则名称
  }
     /**
   * 设置部门代码
   * @param unitid String
   */
  public void setUnitid(String unitid){
    this.setInternal("UNITID" ,unitid );//部门代码
  }
     /**
   * 设置业务类型
   * @param ywlx String
   */
  public void setYwlx(String ywlx){
    this.setInternal("YWLX" ,ywlx );//业务类型
  }
     /**
   * 设置操作类型
   * @param operationOid String
   */
  public void setOperationOid(String operationOid){
    this.setInternal("OPERATION_OID" ,operationOid );//操作类型
  }
     /**
   * 设置派出所代码
   * @param pcsdm String
   */
  public void setPcsdm(String pcsdm){
    this.setInternal("PCSDM" ,pcsdm );//派出所代码
  }
     /**
   * 设置分局代码
   * @param fjdm String
   */
  public void setFjdm(String fjdm){
    this.setInternal("FJDM" ,fjdm );//分局代码
  }
     /**
   * 设置市局代码
   * @param sjdm String
   */
  public void setSjdm(String sjdm){
    this.setInternal("SJDM" ,sjdm );//市局代码
  }
     /**
   * 设置省市县区
   * @param ssxq String
   */
  public void setSsxq(String ssxq){
    this.setInternal("SSXQ" ,ssxq );//省市县区
  }
     /**
   * 设置值1
   * @param value1 String
   */
  public void setValue1(String value1){
    this.setInternal("VALUE1" ,value1 );//值1
  }
     /**
   * 设置值2
   * @param value2 String
   */
  public void setValue2(String value2){
    this.setInternal("VALUE2" ,value2 );//值2
  }
     /**
   * 设置值3
   * @param value3 String
   */
  public void setValue3(String value3){
    this.setInternal("VALUE3" ,value3 );//值3
  }
     /**
   * 设置规则描述
   * @param memo String
   */
  public void setMemo(String memo){
    this.setInternal("MEMO" ,memo );//规则描述
  }
     /**
   * 设置ID
   * @param ruleOid String
   */
  public void setRuleOid(String ruleOid){
    this.setInternal("RULE_OID" ,ruleOid );//ID
  }

    /**
   * 获得规则名称
   * @return String
   */
  public String getRuleName(){
    return (String)this.getInternal("RULE_NAME");
  }

  /**
   * 获得部门代码
   * @return String
   */
  public String getUnitid(){
    return (String)this.getInternal("UNITID");
  }

  /**
   * 获得业务类型
   * @return String
   */
  public String getYwlx(){
    return (String)this.getInternal("YWLX");
  }

  /**
   * 获得操作类型
   * @return String
   */
  public String getOperationOid(){
    return (String)this.getInternal("OPERATION_OID");
  }

  /**
   * 获得派出所代码
   * @return String
   */
  public String getPcsdm(){
    return (String)this.getInternal("PCSDM");
  }

  /**
   * 获得分局代码
   * @return String
   */
  public String getFjdm(){
    return (String)this.getInternal("FJDM");
  }

  /**
   * 获得市局代码
   * @return String
   */
  public String getSjdm(){
    return (String)this.getInternal("SJDM");
  }

  /**
   * 获得省市县区
   * @return String
   */
  public String getSsxq(){
    return (String)this.getInternal("SSXQ");
  }

  /**
   * 获得值1
   * @return String
   */
  public String getValue1(){
    return (String)this.getInternal("VALUE1");
  }

  /**
   * 获得值2
   * @return String
   */
  public String getValue2(){
    return (String)this.getInternal("VALUE2");
  }

  /**
   * 获得值3
   * @return String
   */
  public String getValue3(){
    return (String)this.getInternal("VALUE3");
  }

  /**
   * 获得规则描述
   * @return String
   */
  public String getMemo(){
    return (String)this.getInternal("MEMO");
  }

  /**
   * 获得ID
   * @return String
   */
  public String getRuleOid(){
    return (String)this.getInternal("RULE_OID");
  }


}
