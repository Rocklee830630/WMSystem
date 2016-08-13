package com.ccthanking.framework.wsdy;

import java.io.Serializable;

public class WSFieldResult implements Serializable {
	
	  private String rname;
	  private String rdbtype;
	  private String rlength;
	  private String rfrom;
	  private String rename;
	  private String rtype;
	  private String rvalue;
	  private String rdate;
	  private String rempid;
	  private String rempname;
	  private String domaintype;
	  private String codepage;
	  private String approverole;
	  private String domainstyle;
	  //add by cbl
	  private String canedit;
	  private String qzrq;
	  private String approvelevel;
	  private String spylb;
	  
	  public String getSpylb(){
		  return this.spylb;
	  }
	  public void setSpylb(String spylb){
		  this.spylb = spylb;
	  }
	  
	  public String getApprovelevel(){
		  return approvelevel;
	  }
	  public void setApprovelevel(String approvelevel){
		  this.approvelevel = approvelevel;
	  }
	  
	  public String getQzrq(){
		  return qzrq;
	  }
	  public void setQzrq(String qzrq){
		  this.qzrq = qzrq;
	  }
	  
	  public String getCanedit(){
		  return canedit;
	  }
	  public void setCanedit(String canedit){
		  this.canedit = canedit;
	  }
	  //add by cbl end
	  public String getName()
	  {
	    return rname;
	  }
	  public void setName(String rname)
	  {
	    this.rname = rname;
	  }
	  
	  public String getDbType()
	  {
	    return rdbtype;
	  }
	  public void setDbType(String rdbtype)
	  {
	    this.rdbtype = rdbtype;
	  }
	  
	  public String getDbtype()
	  {
	    return rdbtype;
	  }
	  public void setLength(String rlength)
	  {
	    this.rlength = rlength;
	  }
	  
	  public String getLength()
	  {
	    return rlength;
	  }
	  public void setFrom(String rfrom)
	  {
	    this.rfrom = rfrom;
	  }
	  
	  public String getFrom()
	  {
	    return rfrom;
	  }
	  public void setRename(String rename)
	  {
	    this.rename = rename;
	  }
	  
	  public String getRename()
	  {
	    return rename;
	  }
	  public void setType(String rtype)
	  {
	    this.rtype = rtype;
	  }
	  
	  public String getType()
	  {
	    return rtype;
	  }
	  public void setValue(String rvalue)
	  {
	    this.rvalue = rvalue;
	  }
	  
	  public String getValue()
	  {
	    return rvalue;
	  }
	  public void setDate(String rdate)
	  {
	    this.rdate = rdate;
	  }
	  public String getDate()
	  {
	    return rdate;
	  }
	  public void setEmpid(String rempid)
	  {
	    this.rempid = rempid;
	  }
	  public String getEmpid()
	  {
	    return rempid;
	  }
	  public void setEmpname(String rempname)
	  {
	    this.rempname = rempname;
	  }
	  public String getEmpname()
	  {
	    return rempname;
	  }
	  public void setDomaintype(String domaintype)
	  {
	    this.domaintype = domaintype;
	  }
	  public String getDomaintype()
	  {
	    return domaintype;
	  }
	  public void setCodepage(String codepage)
	  {
	    this.codepage = codepage;
	  }
	  public String getCodepage()
	  {
	    return codepage;
	  }
	  public void setApproverole(String approverole)
	  {
	    this.approverole = approverole;
	  }
	  public String getApproverole()
	  {
	    return approverole;
	  }
	  public void setDomainstyle(String domainstyle)
	  {
	    this.domainstyle = domainstyle;
	  }
	  public String getDomainstyle()
	  {
	    return domainstyle;
	  }
	  
}
