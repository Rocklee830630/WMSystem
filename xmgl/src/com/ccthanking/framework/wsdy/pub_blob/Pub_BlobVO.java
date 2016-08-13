package com.ccthanking.framework.wsdy.pub_blob;
import com.ccthanking.framework.base.BaseVO;
public class Pub_BlobVO extends BaseVO
{
   public Pub_BlobVO()
   {
    this.addField("FJBH",this.TP_PK);//附件编号
    this.addField("FILENAME",OP_STRING);//文件名(文件描述)
    this.addField("WSLBDM",OP_STRING);//文书类别代码-区分用于审批
    this.addField("MULTIMEDIA",OP_BLOB);//
    this.addField("ACTIVE_FLAG",OP_STRING);//删除标志
    this.addField("ZJBS",OP_STRING);//主卷标识
    this.addField("FJBS",OP_STRING);//副卷标识
    this.addField("ZJPXH",OP_STRING);//主卷排序号
    this.addField("FJPXH",OP_STRING);//副卷排序号
    this.addField("DAH",OP_STRING);//档案号
    this.addField("WSWH",OP_STRING);//文书文号
    this.addField("TBSJ",OP_DATE);//入库时间
    this.addField("SFGZ",OP_STRING);//是否盖章
    this.addField("SJBH",OP_STRING);//事件编号
    this.addField("YWLX",OP_STRING);//业务类型
    this.addField("KEY_ID",OP_STRING);//要素编号(WS_TEMPLETE：YW_KEY_VARIABLE)
    this.addField("WS_TEMPLATE_ID",OP_STRING);//文书模板编号
    this.addField("ZFBS", OP_STRING);//作废标识
    this.addField("SPZT", OP_STRING);//审批状态

    this.setVOOwner("empty");
    this.setVOTableName("Pub_Blob");
   }
  //设置附件编号
  public void setFjbh(String aFjbh)
  {
     this.setInternal("FJBH",aFjbh);
  }
  //获取附件编号
  public String getFjbh()
  {
      return (String)this.getInternal("FJBH");
  }
  //设置文件名(文件描述)
  public void setFilename(String aFilename)
  {
     this.setInternal("FILENAME",aFilename);
  }
  //获取文件名(文件描述)
  public String getFilename()
  {
      return (String)this.getInternal("FILENAME");
  }
  //设置文书类别代码-区分用于审批
  public void setWslbdm(String aWslbdm)
  {
     this.setInternal("WSLBDM",aWslbdm);
  }
  //获取文书类别代码-区分用于审批
  public String getWslbdm()
  {
      return (String)this.getInternal("WSLBDM");
  }
  //设置
  public void setMultimedia(byte[] aMultimedia)
  {
     this.setInternal("MULTIMEDIA",aMultimedia);
  }
  //获取
  public byte[] getMultimedia()
  {
      return (byte[])this.getInternal("MULTIMEDIA");
  }
  //设置删除标志
  public void setActive_flag(String aActive_flag)
  {
     this.setInternal("ACTIVE_FLAG",aActive_flag);
  }
  //获取删除标志
  public String getActive_flag()
  {
      return (String)this.getInternal("ACTIVE_FLAG");
  }
  //设置主卷标识
  public void setZjbs(String aZjbs)
  {
     this.setInternal("ZJBS",aZjbs);
  }
  //获取主卷标识
  public String getZjbs()
  {
      return (String)this.getInternal("ZJBS");
  }
  //设置副卷标识
  public void setFjbs(String aFjbs)
  {
     this.setInternal("FJBS",aFjbs);
  }
  //获取副卷标识
  public String getFjbs()
  {
      return (String)this.getInternal("FJBS");
  }
  //设置主卷排序号
  public void setZjpxh(String aZjpxh)
  {
     this.setInternal("ZJPXH",aZjpxh);
  }
  //获取主卷排序号
  public String getZjpxh()
  {
      return (String)this.getInternal("ZJPXH");
  }
  //设置副卷排序号
  public void setFjpxh(String aFjpxh)
  {
     this.setInternal("FJPXH",aFjpxh);
  }
  //获取副卷排序号
  public String getFjpxh()
  {
      return (String)this.getInternal("FJPXH");
  }
  //设置档案号
  public void setDah(String aDah)
  {
     this.setInternal("DAH",aDah);
  }
  //获取档案号
  public String getDah()
  {
      return (String)this.getInternal("DAH");
  }
  //设置文书文号
  public void setWswh(String aWswh)
  {
     this.setInternal("WSWH",aWswh);
  }
  //获取文书文号
  public String getWswh()
  {
      return (String)this.getInternal("WSWH");
  }
  //设置入库时间
  public void setTbsj(java.util.Date aTbsj)
  {
     this.setInternal("TBSJ",aTbsj);
  }
  //获取入库时间
  public java.util.Date getTbsj()
  {
      return (java.util.Date)this.getInternal("TBSJ");
  }
  //设置是否盖章
  public void setSfgz(String aSfgz)
  {
     this.setInternal("SFGZ",aSfgz);
  }
  //获取是否盖章
  public String getSfgz()
  {
      return (String)this.getInternal("SFGZ");
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
  //设置要素编号(WS_TEMPLETE：YW_KEY_VARIABLE)
  public void setKey_id(String aKey_id)
  {
     this.setInternal("KEY_ID",aKey_id);
  }
  //获取要素编号(WS_TEMPLETE：YW_KEY_VARIABLE)
  public String getKey_id()
  {
      return (String)this.getInternal("KEY_ID");
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
//设置文书作废标识
  public void setZfbs(String aZfbs)
  {
     this.setInternal("ZFBS",aZfbs);
  }
  //获取文书作废标识
  public String getZfbs()
  {
      return (String)this.getInternal("ZFBS");
  }
//设置文书作废标识
  public void setSpzt(String aSpzt)
  {
     this.setInternal("SPZT",aSpzt);
  }
  //获取文书作废标识
  public String getSpzt()
  {
      return (String)this.getInternal("SPZT");
  }
}
