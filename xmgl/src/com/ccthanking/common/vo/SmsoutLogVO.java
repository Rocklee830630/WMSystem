package com.ccthanking.common.vo;
import com.ccthanking.framework.base.BaseVO;

import java.util.Date;

public class SmsoutLogVO extends BaseVO{
  public SmsoutLogVO()
  {

  	  		//设置字段信息
  				this.addField("ID",this.OP_STRING|this.TP_PK);//序号（取ap_task_schedule中的ID）
  				this.addField("YWLX",this.OP_STRING);//业务类型
  				this.addField("SJBH",this.OP_STRING);//事件编号
  				this.addField("USERID",this.OP_STRING);//系统内部需要发送短信的用户ID
  				this.addField("USERNAME",this.OP_STRING);//系统内部需要发送短信的用户姓名
  				this.addField("MBNO",this.OP_STRING);//手机号码
  				this.addField("CONTENT",this.OP_STRING);//短信内容
  				this.addField("TBR",this.OP_STRING);//操作员
  				this.addField("TBSJ",this.OP_DATE);//操作时间
  		  	//设置主键信息
            this.addField("ID",this.TP_PK);//序号（取ap_task_schedule中的ID）
    	   //设置字典类型定义
    		//设置时间的显示格式
			     this.setFieldDateFormat("TBSJ","yyyy-MM-dd");
     //设置表名称
      //设置表名称
      this.setVOTableName("SMSOUT_LOG");

  }
     /**
   * 设置序号（取ap_task_schedule中的ID）
   * @param id String
   */
  public void setId(String id){
    this.setInternal("ID" ,id );//序号（取ap_task_schedule中的ID）
  }
     /**
   * 设置业务类型
   * @param ywlx String
   */
  public void setYwlx(String ywlx){
    this.setInternal("YWLX" ,ywlx );//业务类型
  }
     /**
   * 设置事件编号
   * @param sjbh String
   */
  public void setSjbh(String sjbh){
    this.setInternal("SJBH" ,sjbh );//事件编号
  }
     /**
   * 设置系统内部需要发送短信的用户ID
   * @param userid String
   */
  public void setUserid(String userid){
    this.setInternal("USERID" ,userid );//系统内部需要发送短信的用户ID
  }
     /**
   * 设置系统内部需要发送短信的用户姓名
   * @param username String
   */
  public void setUsername(String username){
    this.setInternal("USERNAME" ,username );//系统内部需要发送短信的用户姓名
  }
     /**
   * 设置手机号码
   * @param mbno String
   */
  public void setMbno(String mbno){
    this.setInternal("MBNO" ,mbno );//手机号码
  }
     /**
   * 设置短信内容
   * @param content String
   */
  public void setContent(String content){
    this.setInternal("CONTENT" ,content );//短信内容
  }
     /**
   * 设置操作员
   * @param tbr String
   */
  public void setTbr(String tbr){
    this.setInternal("TBR" ,tbr );//操作员
  }
     /**
   * 设置操作时间
   * @param tbsj String
   */
  public void setTbsj(Date tbsj){
    this.setInternal("TBSJ" ,tbsj );//操作时间
  }

    /**
   * 获得序号（取ap_task_schedule中的ID）
   * @return String
   */
  public String getId(){
    return (String)this.getInternal("ID");
  }

  /**
   * 获得业务类型
   * @return String
   */
  public String getYwlx(){
    return (String)this.getInternal("YWLX");
  }

  /**
   * 获得事件编号
   * @return String
   */
  public String getSjbh(){
    return (String)this.getInternal("SJBH");
  }

  /**
   * 获得系统内部需要发送短信的用户ID
   * @return String
   */
  public String getUserid(){
    return (String)this.getInternal("USERID");
  }

  /**
   * 获得系统内部需要发送短信的用户姓名
   * @return String
   */
  public String getUsername(){
    return (String)this.getInternal("USERNAME");
  }

  /**
   * 获得手机号码
   * @return String
   */
  public String getMbno(){
    return (String)this.getInternal("MBNO");
  }

  /**
   * 获得短信内容
   * @return String
   */
  public String getContent(){
    return (String)this.getInternal("CONTENT");
  }

  /**
   * 获得操作员
   * @return String
   */
  public String getTbr(){
    return (String)this.getInternal("TBR");
  }

  /**
   * 获得操作时间
   * @return String
   */
  public Date getTbsj(){
    return (Date)this.getInternal("TBSJ");
  }


}
