package com.ccthanking.framework.wsdy.doc_sequence;
import com.ccthanking.framework.base.BaseVO;
public class Doc_SequenceVO extends BaseVO
{
   public Doc_SequenceVO()
   {
    this.addField("DEPTID",this.TP_PK);//部门ID
    this.addField("DOCTYPEID",this.TP_PK);//文书标识
    this.addField("DOCID",this.TP_PK);//文书流水号
    this.addField("PREFIX",OP_STRING);//固定前缀
    this.addField("STATE",OP_STRING);//状态：1，使用中；2，未用；
    this.addField("ND", OP_STRING);//年度：格式YYYY

    this.setVOOwner("empty");
    this.setVOTableName("Ws_Doc_Sequence");
   }
  //设置部门ID
  public void setDeptid(String aDeptid)
  {
     this.setInternal("DEPTID",aDeptid);
  }
  //获取部门ID
  public String getDeptid()
  {
      return (String)this.getInternal("DEPTID");
  }
  //设置文书标识
  public void setDoctypeid(String aDoctypeid)
  {
     this.setInternal("DOCTYPEID",aDoctypeid);
  }
  //获取文书标识
  public String getDoctypeid()
  {
      return (String)this.getInternal("DOCTYPEID");
  }
  //设置文书流水号
  public void setDocid(String aDocid)
  {
     this.setInternal("DOCID",aDocid);
  }
  //获取文书流水号
  public String getDocid()
  {
      return (String)this.getInternal("DOCID");
  }
  //设置固定前缀
  public void setPrefix(String aPrefix)
  {
     this.setInternal("PREFIX",aPrefix);
  }
  //获取固定前缀
  public String getPrefix()
  {
      return (String)this.getInternal("PREFIX");
  }
  //设置状态：1，使用中；2，未用；
  public void setState(String aState)
  {
     this.setInternal("STATE",aState);
  }
  //获取状态：1，使用中；2，未用；
  public String getState()
  {
      return (String)this.getInternal("STATE");
  }
  //设置年度
  public void setNd(String aNd)
  {
     this.setInternal("ND",aNd);
  }
  //获取年度
  public String getNd()
  {
      return (String)this.getInternal("ND");
  }
}
