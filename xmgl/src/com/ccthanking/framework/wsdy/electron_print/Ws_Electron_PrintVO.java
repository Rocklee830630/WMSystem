package com.ccthanking.framework.wsdy.electron_print;
import com.ccthanking.framework.base.BaseVO;
public class Ws_Electron_PrintVO extends BaseVO
{
   public Ws_Electron_PrintVO()
   {
    this.addField("XH",this.TP_PK);
    this.addField("WS_TEMPLATE_ID",this.OP_STRING);//文书模板编号
    this.addField("SJBH",this.OP_STRING);//事件编号
    this.addField("YWLX",this.OP_STRING);//业务类型
    this.addField("APPROVEROLE",OP_STRING);//审批角色
    this.addField("APPROVELEVEL",OP_STRING);//审批级别
    this.addField("MULTIMEDIA",OP_BLOB);//电子印章
    this.addField("YZLX",OP_STRING);//印章类型（1：签名，2：印章）


    this.setVOOwner("empty");
    this.setVOTableName("Ws_Electron_Print");
   }
  //印章类型（1：签名，2：印章）
  public void setYzlx(String yzlx)
  {
      this.setInternal("YZLX",yzlx);
  }
  public String getYzlx()
  {
      return(String)this.getInternal("YZLX");
  }

  public void setXh(String xh){
      this.setInternal("XH",xh);
  }
  public String getXh()
  {
      return(String)this.getInternal("XH");
  }
  //设置文书模板编号
  public void setWs_template_id(String aWs_template_id)
  {
     this.setInternal("WS_TEMPLATE_ID",aWs_template_id);
  }
  //获取文书模板编号
  public String getWs_template_id()
  {
      return (String)this.getInternal("WS_TEMPLATE_ID");
  }
  //设置事件编号
  public void setSjbh(String aSjbh)
  {
     this.setInternal("SJBH",aSjbh);
  }
  //获取事件编号
  public String getSjbh()
  {
      return (String)this.getInternal("SJBH");
  }
  //设置业务类型
  public void setYwlx(String aYwlx)
  {
     this.setInternal("YWLX",aYwlx);
  }
  //获取业务类型
  public String getYwlx()
  {
      return (String)this.getInternal("YWLX");
  }
  //设置审批角色
  public void setApproverole(String aApproverole)
  {
     this.setInternal("APPROVEROLE",aApproverole);
  }
  //获取审批角色
  public String getApproverole()
  {
      return (String)this.getInternal("APPROVEROLE");
  }
  //设置审批级别
  public void setApprovelevel(String aApprovelevel)
  {
     this.setInternal("APPROVELEVEL",aApprovelevel);
  }
  //获取审批级别
  public String getApprovelevel()
  {
      return (String)this.getInternal("APPROVELEVEL");
  }
  //设置电子印章
  public void setMultimedia(byte[] aMultimedia)
  {
     this.setInternal("MULTIMEDIA",aMultimedia);
  }
  //获取电子印章
  public byte[] getMultimedia()
  {
      return (byte[])this.getInternal("MULTIMEDIA");
  }
}
