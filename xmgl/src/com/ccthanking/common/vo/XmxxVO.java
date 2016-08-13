package com.ccthanking.common.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class XmxxVO extends BaseVO{
  public XmxxVO()
  { 
  	    	
  	  		//设置字段信息
  				this.addField("ID",this.OP_STRING|this.TP_PK);//主键
  				this.addField("XMMC",this.OP_STRING);//项目名称
  				this.addField("XMNF",this.OP_STRING);//项目年份
  				this.addField("XMFL",this.OP_STRING);//项目分类
  				this.addField("XMQZD",this.OP_DATE);//项目起止点
  				this.addField("GS",this.OP_STRING);//概算
  				this.addField("XMLY",this.OP_STRING);//项目类型
  				this.addField("JSBYS",this.OP_STRING);//项目必要性
  				this.addField("JSYY",this.OP_STRING);//项目意义
  				this.addField("BZ",this.OP_STRING);//备注
  				this.addField("YWLX",this.OP_STRING);//业务类型
  				this.addField("SJBH",this.OP_STRING);//事件编号
  				this.addField("TBSJ",this.OP_DATE);//填报时间
  				this.addField("TBR",this.OP_STRING);//填报人
  				this.addField("TBDW",this.OP_STRING);//录入单位
  				this.addField("SJMJ",this.OP_STRING);//数据密级
  				this.addField("GXSJ",this.OP_DATE);//更新时间
  				this.addField("GXR",this.OP_STRING);//更新人
  				this.addField("GXDW",this.OP_STRING);//更新单位
  				this.addField("ZHUX",this.OP_STRING);//注销
  		  	//设置主键信息
    	   //设置字典类型定义
    		//设置时间的显示格式  	
			     this.setFieldDateFormat("XMQZD","yyyy-MM-dd");

			     this.setFieldDateFormat("TBSJ","yyyy-MM-dd");
			     this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
			     this.bindFieldToDic("XMNF", "XMNF");
			     this.bindFieldToThousand("GS");
     //设置表名称
      //设置表名称
      this.setVOTableName("XMXX");
    
  }
     /**
   * 设置主键
   * @param id String
   */
  public void setId(String id){ 
    this.setInternal("ID" ,id );//主键
  }
     /**
   * 设置项目名称
   * @param xmmc String
   */
  public void setXmmc(String xmmc){ 
    this.setInternal("XMMC" ,xmmc );//项目名称
  }
     /**
   * 设置项目年份
   * @param xmnf String
   */
  public void setXmnf(String xmnf){ 
    this.setInternal("XMNF" ,xmnf );//项目年份
  }
     /**
   * 设置项目分类
   * @param xmfl String
   */
  public void setXmfl(String xmfl){ 
    this.setInternal("XMFL" ,xmfl );//项目分类
  }
     /**
   * 设置项目起止点
   * @param xmqzd String
   */
  public void setXmqzd(Date xmqzd){ 
    this.setInternal("XMQZD" ,xmqzd );//项目起止点
  }
     /**
   * 设置概算
   * @param gs String
   */
  public void setGs(String gs){ 
    this.setInternal("GS" ,gs );//概算
  }
     /**
   * 设置项目类型
   * @param xmly String
   */
  public void setXmly(String xmly){ 
    this.setInternal("XMLY" ,xmly );//项目类型
  }
     /**
   * 设置项目必要性
   * @param jsbys String
   */
  public void setJsbys(String jsbys){ 
    this.setInternal("JSBYS" ,jsbys );//项目必要性
  }
     /**
   * 设置项目意义
   * @param jsyy String
   */
  public void setJsyy(String jsyy){ 
    this.setInternal("JSYY" ,jsyy );//项目意义
  }
     /**
   * 设置备注
   * @param bz String
   */
  public void setBz(String bz){ 
    this.setInternal("BZ" ,bz );//备注
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
   * 设置录入单位
   * @param tbdw String
   */
  public void setTbdw(String tbdw){ 
    this.setInternal("TBDW" ,tbdw );//录入单位
  }
     /**
   * 设置数据密级
   * @param sjmj String
   */
  public void setSjmj(String sjmj){ 
    this.setInternal("SJMJ" ,sjmj );//数据密级
  }
     /**
   * 设置更新时间
   * @param gxsj String
   */
  public void setGxsj(Date gxsj){ 
    this.setInternal("GXSJ" ,gxsj );//更新时间
  }
     /**
   * 设置更新人
   * @param gxr String
   */
  public void setGxr(String gxr){ 
    this.setInternal("GXR" ,gxr );//更新人
  }
     /**
   * 设置更新单位
   * @param gxdw String
   */
  public void setGxdw(String gxdw){ 
    this.setInternal("GXDW" ,gxdw );//更新单位
  }
     /**
   * 设置注销
   * @param zhux String
   */
  public void setZhux(String zhux){ 
    this.setInternal("ZHUX" ,zhux );//注销
  }
      
    /**
   * 获得主键
   * @return String
   */
  public String getId(){
    return (String)this.getInternal("ID");
  }
    /**
   * 获得项目名称
   * @return String
   */
  public String getXmmc(){
    return (String)this.getInternal("XMMC");
  }
    /**
   * 获得项目年份
   * @return String
   */
  public String getXmnf(){
    return (String)this.getInternal("XMNF");
  }
    /**
   * 获得项目分类
   * @return String
   */
  public String getXmfl(){
    return (String)this.getInternal("XMFL");
  }
    /**
   * 获得项目起止点
   * @return String
   */
  public Date getXmqzd(){
    return (Date)this.getInternal("XMQZD");
  }
    /**
   * 获得概算
   * @return String
   */
  public String getGs(){
    return (String)this.getInternal("GS");
  }
    /**
   * 获得项目类型
   * @return String
   */
  public String getXmly(){
    return (String)this.getInternal("XMLY");
  }
    /**
   * 获得项目必要性
   * @return String
   */
  public String getJsbys(){
    return (String)this.getInternal("JSBYS");
  }
    /**
   * 获得项目意义
   * @return String
   */
  public String getJsyy(){
    return (String)this.getInternal("JSYY");
  }
    /**
   * 获得备注
   * @return String
   */
  public String getBz(){
    return (String)this.getInternal("BZ");
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
   * 获得录入单位
   * @return String
   */
  public String getTbdw(){
    return (String)this.getInternal("TBDW");
  }
    /**
   * 获得数据密级
   * @return String
   */
  public String getSjmj(){
    return (String)this.getInternal("SJMJ");
  }
    /**
   * 获得更新时间
   * @return String
   */
  public Date getGxsj(){
    return (Date)this.getInternal("GXSJ");
  }
    /**
   * 获得更新人
   * @return String
   */
  public String getGxr(){
    return (String)this.getInternal("GXR");
  }
    /**
   * 获得更新单位
   * @return String
   */
  public String getGxdw(){
    return (String)this.getInternal("GXDW");
  }
    /**
   * 获得注销
   * @return String
   */
  public String getZhux(){
    return (String)this.getInternal("ZHUX");
  }
  }
