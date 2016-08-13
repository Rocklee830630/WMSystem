package com.ccthanking.common.vo;
import com.ccthanking.framework.base.BaseVO;

import java.util.Date;

public class PubXxfbVO extends BaseVO{
  public PubXxfbVO()
  {

  	  		//设置字段信息
  				this.addField("BH",this.OP_STRING|this.TP_PK);//编号
  				this.addField("XXLB",this.OP_STRING);//信息类别
  				this.addField("BT",this.OP_STRING);//标题
  				this.addField("FBT",this.OP_STRING);//副标题
  				this.addField("ZTC",this.OP_STRING);//主题词
  				this.addField("FWDW",this.OP_STRING);//发文单位
  				this.addField("FWR",this.OP_STRING);//发文人
  				this.addField("FWSJ",this.OP_DATE);//发文时间
  				this.addField("FWBS",this.OP_STRING);//发布方式
  				this.addField("TP",this.OP_BLOB);//图片
  				this.addField("YXQX",this.OP_STRING);//有效期限(天)
  				this.addField("FWNR",this.OP_CLOB);//发文内容
  		  	//设置主键信息
            this.addField("BH",this.TP_PK);//编号
    	   //设置字典类型定义
    			this.bindFieldToDic("FWBS","FWBS");//发布方式
                //this.bindFieldToOrgDept("ZZJG");
    		//设置时间的显示格式
			     this.setFieldDateFormat("FWSJ","yyyy-MM-dd");
     //设置表名称
      //设置表名称
      this.setVOTableName("PUB_XXFB");

  }
     /**
   * 设置编号
   * @param bh String
   */
  public void setBh(String bh){
    this.setInternal("BH" ,bh );//编号
  }
     /**
   * 设置信息类别
   * @param xxlb String
   */
  public void setXxlb(String xxlb){
    this.setInternal("XXLB" ,xxlb );//信息类别
  }
     /**
   * 设置标题
   * @param bt String
   */
  public void setBt(String bt){
    this.setInternal("BT" ,bt );//标题
  }
     /**
   * 设置副标题
   * @param fbt String
   */
  public void setFbt(String fbt){
    this.setInternal("FBT" ,fbt );//副标题
  }
     /**
   * 设置主题词
   * @param ztc String
   */
  public void setZtc(String ztc){
    this.setInternal("ZTC" ,ztc );//主题词
  }
     /**
   * 设置发文单位
   * @param fwdw String
   */
  public void setFwdw(String fwdw){
    this.setInternal("FWDW" ,fwdw );//发文单位
  }
     /**
   * 设置发文人
   * @param fwr String
   */
  public void setFwr(String fwr){
    this.setInternal("FWR" ,fwr );//发文人
  }
     /**
   * 设置发文时间
   * @param fwsj String
   */
  public void setFwsj(Date fwsj){
    this.setInternal("FWSJ" ,fwsj );//发文时间
  }
     /**
   * 设置发布方式
   * @param fwbs String
   */
  public void setFwbs(String fwbs){
    this.setInternal("FWBS" ,fwbs );//发布方式
  }
     /**
   * 设置图片
   * @param tp String
   */
  public void setTp(byte[] tp){
    this.setInternal("TP" ,tp );//图片
  }
     /**
   * 设置有效期限(天)
   * @param yxqx String
   */
  public void setYxqx(String yxqx){
    this.setInternal("YXQX" ,yxqx );//有效期限(天)
  }
     /**
   * 设置发文内容
   * @param fwnr String
   */
  public void setFwnr(byte[] fwnr){
    this.setInternal("FWNR" ,fwnr );//发文内容
  }

    /**
   * 获得编号
   * @return String
   */
  public String getBh(){
    return (String)this.getInternal("BH");
  }

  /**
   * 获得信息类别
   * @return String
   */
  public String getXxlb(){
    return (String)this.getInternal("XXLB");
  }

  /**
   * 获得标题
   * @return String
   */
  public String getBt(){
    return (String)this.getInternal("BT");
  }

  /**
   * 获得副标题
   * @return String
   */
  public String getFbt(){
    return (String)this.getInternal("FBT");
  }

  /**
   * 获得主题词
   * @return String
   */
  public String getZtc(){
    return (String)this.getInternal("ZTC");
  }

  /**
   * 获得发文单位
   * @return String
   */
  public String getFwdw(){
    return (String)this.getInternal("FWDW");
  }

  /**
   * 获得发文人
   * @return String
   */
  public String getFwr(){
    return (String)this.getInternal("FWR");
  }

  /**
   * 获得发文时间
   * @return String
   */
  public Date getFwsj(){
    return (Date)this.getInternal("FWSJ");
  }

  /**
   * 获得发布方式
   * @return String
   */
  public String getFwbs(){
    return (String)this.getInternal("FWBS");
  }

  /**
   * 获得图片
   * @return String
   */
  public byte[] getTp(){
    return (byte[])this.getInternal("TP");
  }

  /**
   * 获得有效期限(天)
   * @return String
   */
  public String getYxqx(){
    return (String)this.getInternal("YXQX");
  }

  /**
   * 获得发文内容
   * @return String
   */
  public byte[] getFwnr(){
    return (byte[])this.getInternal("FWNR");
  }


}
