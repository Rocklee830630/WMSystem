package com.ccthanking.framework.coreapp.aplink.WsTemplate;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class WsTemplateVO extends BaseVO{
  public WsTemplateVO()
  {

  	  		//设置字段信息
  				this.addField("DCDY_FLAG",this.OP_STRING);//多次生成
  				this.addField("IS_SP_FLAG",this.OP_STRING);//是否为审批用文书
  				this.addField("SFGZ",this.OP_STRING);//是否需要盖章
  				this.addField("SFQM",this.OP_STRING);//是否签名
  				this.addField("SYBMJB",this.OP_STRING);//0：不取部门，2：市级，3：分局，4：派出所
  				this.addField("WS_FILE_TYPE",this.OP_STRING);//文书文件类型
  				this.addField("WS_KIND",this.OP_STRING);//文书类别
  				this.addField("WS_NO_CHINESE",this.OP_STRING);//文书文号汉字部分描述
  				this.addField("WS_TEMPLATE",this.OP_STRING);//文书模板
  				this.addField("WS_TEMPLATE_ID",this.OP_STRING);//文书模板编号
  				this.addField("WS_TEMPLATE_NAME",this.OP_STRING);//文书模板名称
  				this.addField("YW_KEY_VARIABLE",this.OP_STRING);//业务主键变量(人，组织，案件编号)
  		  	//设置主键信息
            this.addField("WS_TEMPLATE_ID",this.TP_PK);//文书模板编号
    	   //设置字典类型定义
    		//设置时间的显示格式
     //设置表名称
      //设置表名称
      this.setVOTableName("WS_TEMPLATE");

  }
     /**
   * 设置多次生成
   * @param dcdyFlag String
   */
  public void setDcdyFlag(String dcdyFlag){
    this.setInternal("DCDY_FLAG" ,dcdyFlag );//多次生成
  }
     /**
   * 设置是否为审批用文书
   * @param isSpFlag String
   */
  public void setIsSpFlag(String isSpFlag){
    this.setInternal("IS_SP_FLAG" ,isSpFlag );//是否为审批用文书
  }
     /**
   * 设置是否需要盖章
   * @param sfgz String
   */
  public void setSfgz(String sfgz){
    this.setInternal("SFGZ" ,sfgz );//是否需要盖章
  }
     /**
   * 设置是否签名
   * @param sfqm String
   */
  public void setSfqm(String sfqm){
    this.setInternal("SFQM" ,sfqm );//是否签名
  }
     /**
   * 设置0：不取部门，2：市级，3：分局，4：派出所
   * @param sybmjb String
   */
  public void setSybmjb(String sybmjb){
    this.setInternal("SYBMJB" ,sybmjb );//0：不取部门，2：市级，3：分局，4：派出所
  }
     /**
   * 设置文书文件类型
   * @param wsFileType String
   */
  public void setWsFileType(String wsFileType){
    this.setInternal("WS_FILE_TYPE" ,wsFileType );//文书文件类型
  }
     /**
   * 设置文书类别
   * @param wsKind String
   */
  public void setWsKind(String wsKind){
    this.setInternal("WS_KIND" ,wsKind );//文书类别
  }
     /**
   * 设置文书文号汉字部分描述
   * @param wsNoChinese String
   */
  public void setWsNoChinese(String wsNoChinese){
    this.setInternal("WS_NO_CHINESE" ,wsNoChinese );//文书文号汉字部分描述
  }
     /**
   * 设置文书模板
   * @param wsTemplate String
   */
  public void setWsTemplate(byte[] wsTemplate){
    this.setInternal("WS_TEMPLATE" ,wsTemplate );//文书模板
  }
     /**
   * 设置文书模板编号
   * @param wsTemplateId String
   */
  public void setWsTemplateId(String wsTemplateId){
    this.setInternal("WS_TEMPLATE_ID" ,wsTemplateId );//文书模板编号
  }
     /**
   * 设置文书模板名称
   * @param wsTemplateName String
   */
  public void setWsTemplateName(String wsTemplateName){
    this.setInternal("WS_TEMPLATE_NAME" ,wsTemplateName );//文书模板名称
  }
     /**
   * 设置业务主键变量(人，组织，案件编号)
   * @param ywKeyVariable String
   */
  public void setYwKeyVariable(String ywKeyVariable){
    this.setInternal("YW_KEY_VARIABLE" ,ywKeyVariable );//业务主键变量(人，组织，案件编号)
  }

    /**
   * 获得多次生成
   * @return String
   */
  public String getDcdyFlag(){
    return (String)this.getInternal("DCDY_FLAG");
  }

  /**
   * 获得是否为审批用文书
   * @return String
   */
  public String getIsSpFlag(){
    return (String)this.getInternal("IS_SP_FLAG");
  }

  /**
   * 获得是否需要盖章
   * @return String
   */
  public String getSfgz(){
    return (String)this.getInternal("SFGZ");
  }

  /**
   * 获得是否签名
   * @return String
   */
  public String getSfqm(){
    return (String)this.getInternal("SFQM");
  }

  /**
   * 获得0：不取部门，2：市级，3：分局，4：派出所
   * @return String
   */
  public String getSybmjb(){
    return (String)this.getInternal("SYBMJB");
  }

  /**
   * 获得文书文件类型
   * @return String
   */
  public String getWsFileType(){
    return (String)this.getInternal("WS_FILE_TYPE");
  }

  /**
   * 获得文书类别
   * @return String
   */
  public String getWsKind(){
    return (String)this.getInternal("WS_KIND");
  }

  /**
   * 获得文书文号汉字部分描述
   * @return String
   */
  public String getWsNoChinese(){
    return (String)this.getInternal("WS_NO_CHINESE");
  }

  /**
   * 获得文书模板
   * @return String
   */
  public byte[] getWsTemplate(){
    return (byte[])this.getInternal("WS_TEMPLATE");
  }

  /**
   * 获得文书模板编号
   * @return String
   */
  public String getWsTemplateId(){
    return (String)this.getInternal("WS_TEMPLATE_ID");
  }

  /**
   * 获得文书模板名称
   * @return String
   */
  public String getWsTemplateName(){
    return (String)this.getInternal("WS_TEMPLATE_NAME");
  }

  /**
   * 获得业务主键变量(人，组织，案件编号)
   * @return String
   */
  public String getYwKeyVariable(){
    return (String)this.getInternal("YW_KEY_VARIABLE");
  }


}
