package com.ccthanking.common.vo;
import com.ccthanking.framework.base.BaseVO;

import java.util.Date;

public class AlertInfoVO extends BaseVO{
  public AlertInfoVO()
  { 
  	    	
  	  		//设置字段信息
  				this.addField("XH",this.OP_STRING|this.TP_PK);//序号
  				this.addField("DESR",this.OP_STRING);//提醒信息文字描述
  				this.addField("OVERRUN",this.OP_STRING);//提醒时限（小时）
  				this.addField("ZT",this.OP_STRING);//提醒信息状态 0 已完成 1 已知晓 2 未处理
  				this.addField("DJSJ",this.OP_DATE);//提醒信息登记时间
  				this.addField("TXR",this.OP_STRING);//提醒人
  				this.addField("TXROLE",this.OP_STRING);//提醒角色
  				this.addField("TXDW",this.OP_STRING);//提醒单位
  				this.addField("GDR",this.OP_STRING);//归档人
  				this.addField("GDSJ",this.OP_DATE);//归档时间
  				this.addField("YWLX",this.OP_STRING);//业务类型
  				this.addField("SJBH",this.OP_STRING);//事件编号
  				this.addField("LINKURL",this.OP_STRING);//链接地址
  		  	//设置主键信息
            this.addField("XH",this.TP_PK);//序号
    	   //设置字典类型定义
    		//设置时间的显示格式  	
			     this.setFieldDateFormat("DJSJ","yyyy-MM-dd");
			     this.setFieldDateFormat("GDSJ","yyyy-MM-dd");
     //设置表名称
      //设置表名称
      this.setVOTableName("ALERT_INFO");
    
  }
     /**
   * 设置序号
   * @param xh String
   */
  public void setXh(String xh){ 
    this.setInternal("XH" ,xh );//序号
  }
     /**
   * 设置提醒信息文字描述
   * @param desr String
   */
  public void setDesr(String desr){ 
    this.setInternal("DESR" ,desr );//提醒信息文字描述
  }
     /**
   * 设置提醒时限（小时）
   * @param overrun String
   */
  public void setOverrun(String overrun){ 
    this.setInternal("OVERRUN" ,overrun );//提醒时限（小时）
  }
     /**
   * 设置提醒信息状态 0 已完成 1 已知晓 2 未处理
   * @param zt String
   */
  public void setZt(String zt){ 
    this.setInternal("ZT" ,zt );//提醒信息状态 0 已完成 1 已知晓 2 未处理
  }
     /**
   * 设置提醒信息登记时间
   * @param djsj String
   */
  public void setDjsj(Date djsj){ 
    this.setInternal("DJSJ" ,djsj );//提醒信息登记时间
  }
     /**
   * 设置提醒人
   * @param txr String
   */
  public void setTxr(String txr){ 
    this.setInternal("TXR" ,txr );//提醒人
  }
     /**
   * 设置提醒角色
   * @param txrole String
   */
  public void setTxrole(String txrole){ 
    this.setInternal("TXROLE" ,txrole );//提醒角色
  }
     /**
   * 设置提醒单位
   * @param txdw String
   */
  public void setTxdw(String txdw){ 
    this.setInternal("TXDW" ,txdw );//提醒单位
  }
     /**
   * 设置归档人
   * @param gdr String
   */
  public void setGdr(String gdr){ 
    this.setInternal("GDR" ,gdr );//归档人
  }
     /**
   * 设置归档时间
   * @param gdsj String
   */
  public void setGdsj(Date gdsj){ 
    this.setInternal("GDSJ" ,gdsj );//归档时间
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
   * 设置链接地址
   * @param linkurl String
   */
  public void setLinkurl(String linkurl){ 
    this.setInternal("LINKURL" ,linkurl );//链接地址
  }
      
    /**
   * 获得序号
   * @return String
   */
  public String getXh(){
    return (String)this.getInternal("XH");
  }
  
  /**
   * 获得提醒信息文字描述
   * @return String
   */
  public String getDesr(){
    return (String)this.getInternal("DESR");
  }
  
  /**
   * 获得提醒时限（小时）
   * @return String
   */
  public String getOverrun(){
    return (String)this.getInternal("OVERRUN");
  }
  
  /**
   * 获得提醒信息状态 0 已完成 1 已知晓 2 未处理
   * @return String
   */
  public String getZt(){
    return (String)this.getInternal("ZT");
  }
  
  /**
   * 获得提醒信息登记时间
   * @return String
   */
  public Date getDjsj(){
    return (Date)this.getInternal("DJSJ");
  }
  
  /**
   * 获得提醒人
   * @return String
   */
  public String getTxr(){
    return (String)this.getInternal("TXR");
  }
  
  /**
   * 获得提醒角色
   * @return String
   */
  public String getTxrole(){
    return (String)this.getInternal("TXROLE");
  }
  
  /**
   * 获得提醒单位
   * @return String
   */
  public String getTxdw(){
    return (String)this.getInternal("TXDW");
  }
  
  /**
   * 获得归档人
   * @return String
   */
  public String getGdr(){
    return (String)this.getInternal("GDR");
  }
  
  /**
   * 获得归档时间
   * @return String
   */
  public Date getGdsj(){
    return (Date)this.getInternal("GDSJ");
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
   * 获得链接地址
   * @return String
   */
  public String getLinkurl(){
    return (String)this.getInternal("LINKURL");
  }
  
}
