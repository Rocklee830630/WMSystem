package com.ccthanking.common.vo;
import com.ccthanking.framework.base.BaseVO;

import java.util.Date;

public class NotesInfoVO extends BaseVO{
  public NotesInfoVO()
  { 
  	    	
  	  		//设置字段信息
  				this.addField("XH",this.OP_STRING|this.TP_PK);//序号
  				this.addField("TITLE",this.OP_STRING);//标题
  				this.addField("CONTENT",this.OP_CLOB);//内容
  				this.addField("TXSJ",this.OP_DATE);//提醒时间
  				this.addField("SJBH",this.OP_STRING);//事件编号
  				this.addField("YWLX",this.OP_STRING);//业务类型
  				this.addField("TBSJ",this.OP_DATE);//填报时间
  				this.addField("TBR",this.OP_STRING);//填报人
  				this.addField("ZT",this.OP_STRING);//提醒信息状态 0 已完成 1 已知晓 2 未处理
  				this.addField("ZHUX",this.OP_STRING);//注销
  				
  		  	//设置主键信息
            this.addField("XH",this.TP_PK);//序号
    	   //设置字典类型定义
            this.bindFieldToUserid("TBR");
    		//设置时间的显示格式  	
			     this.setFieldDateFormat("TXSJ","yyyy-MM-dd");
			     this.setFieldDateFormat("TBSJ","yyyy-MM-dd");
     //设置表名称
      //设置表名称
      this.setVOTableName("NOTES_INFO");
    
  }
     /**
   * 设置序号
   * @param xh String
   */
  public void setXh(String xh){ 
    this.setInternal("XH" ,xh );//序号
  }
     /**
   * 设置标题
   * @param title String
   */
  public void setTitle(String title){ 
    this.setInternal("TITLE" ,title );//标题
  }
     /**
   * 设置内容
   * @param content String
   */
  public void setContent(String content){ 
    this.setInternal("CONTENT" ,content );//内容
  }
     /**
   * 设置提醒时间
   * @param txsj String
   */
  public void setTxsj(Date txsj){ 
    this.setInternal("TXSJ" ,txsj );//提醒时间
  }
     /**
   * 设置事件编号
   * @param sjbh String
   */
  public void setSjbh(String sjbh){ 
    this.setInternal("SJBH" ,sjbh );//事件编号
  }
  /**
   * 设置业务类型
   * @param ywlx String
   */
  public void setYwlx(String ywlx){ 
    this.setInternal("YWLX" ,ywlx );//业务类型
  }  
     /**
   * 设置填报时间
   * @param tbsj String
   */
  public void setTbsj(Date tbsj){ 
    this.setInternal("TBSJ" ,tbsj );//填报时间
  }
     /**
   * 设置填报人
   * @param tbr String
   */
  public void setTbr(String tbr){ 
    this.setInternal("TBR" ,tbr );//填报人
  }
     /**
   * 设置提醒信息状态 0 已完成 1 已知晓 2 未处理
   * @param zt String
   */
  public void setZt(String zt){ 
    this.setInternal("ZT" ,zt );//提醒信息状态 0 已完成 1 已知晓 2 未处理
  }
  /**
   * 设置注销
   * @param zhux String
   */
  public void setZhux(String zhux){ 
    this.setInternal("ZHUX" ,zhux );//注销
  }      
    /**
   * 获得序号
   * @return String
   */
  public String getXh(){
    return (String)this.getInternal("XH");
  }
  
  /**
   * 获得标题
   * @return String
   */
  public String getTitle(){
    return (String)this.getInternal("TITLE");
  }
  
  /**
   * 获得内容
   * @return String
   */
  public String getContent(){
    return (String)this.getInternal("CONTENT");
  }
  
  /**
   * 获得提醒时间
   * @return String
   */
  public Date getTxsj(){
    return (Date)this.getInternal("TXSJ");
  }
  
  /**
   * 获得事件编号
   * @return String
   */
  public String getSjbh(){
    return (String)this.getInternal("SJBH");
  }
  /**
   * 获得业务类型
   * @return String
   */
  public String getYwlx(){
    return (String)this.getInternal("YWLX");
  }  
  /**
   * 获得填报时间
   * @return String
   */
  public Date getTbsj(){
    return (Date)this.getInternal("TBSJ");
  }
  
  /**
   * 获得填报人
   * @return String
   */
  public String getTbr(){
    return (String)this.getInternal("TBR");
  }
  
  /**
   * 获得提醒信息状态 0 已完成 1 已知晓 2 未处理
   * @return String
   */
  public String getZt(){
    return (String)this.getInternal("ZT");
  }
  /**
   * 获得注销
   * @return String
   */
  public String getZhux(){
    return (String)this.getInternal("ZHUX");
  }  
}
