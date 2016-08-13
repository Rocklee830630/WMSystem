package com.ccthanking.business.wttb.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class WttbEventVO extends BaseVO{

	public WttbEventVO(){
		this.addField("PFID",OP_STRING|this.TP_PK);
		this.addField("WTID",OP_STRING);
		this.addField("JSDW",OP_STRING);
		this.addField("JSR",OP_STRING);
		this.addField("JSSJ",OP_DATE);
		this.addField("SFWC",OP_STRING);
		this.addField("PFNR",OP_STRING);
		this.addField("PFSJ",OP_DATE);
		this.addField("SJBH",OP_STRING);
		this.addField("SJMJ",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.setFieldDateFormat("JSSJ","yyyy-MM-dd");
		this.setFieldDateFormat("PFSJ","yyyy-MM-dd");
		this.setVOTableName("WTTB_EVENT");
	}

	public void setPfid(String pfid){
		this.setInternal("PFID",pfid);
	}
	public String getPfid(){
		return (String)this.getInternal("PFID");
	}
	public void setWtid(String wtid){
		this.setInternal("WTID",wtid);
	}
	public String getWtid(){
		return (String)this.getInternal("WTID");
	}
	public void setJsdw(String jsdw){
		this.setInternal("JSDW",jsdw);
	}
	public String getJsdw(){
		return (String)this.getInternal("JSDW");
	}
	public void setJsr(String jsr){
		this.setInternal("JSR",jsr);
	}
	public String getJsr(){
		return (String)this.getInternal("JSR");
	}
	public void setJssj(Date jssj){
		this.setInternal("JSSJ",jssj);
	}
	public Date getJssj(){
		return (Date)this.getInternal("JSSJ");
	}
	public void setSfwc(String sfwc){
		this.setInternal("SFWC",sfwc);
	}
	public String getSfwc(){
		return (String)this.getInternal("SFWC");
	}
	public void setPfnr(String pfnr){
		this.setInternal("PFNR",pfnr);
	}
	public String getPfnr(){
		return (String)this.getInternal("PFNR");
	}
	public void setPfsj(Date pfsj){
		this.setInternal("PFSJ",pfsj);
	}
	public Date getPfsj(){
		return (Date)this.getInternal("PFSJ");
	}
	public void setSjbh(String sjbh){
		this.setInternal("SJBH",sjbh);
	}
	public String getSjbh(){
		return (String)this.getInternal("SJBH");
	}
	public void setSjmj(String sjmj){
		this.setInternal("SJMJ",sjmj);
	}
	public String getSjmj(){
		return (String)this.getInternal("SJMJ");
	}
	public void setSfyx(String sfyx){
		this.setInternal("SFYX",sfyx);
	}
	public String getSfyx(){
		return (String)this.getInternal("SFYX");
	}
	public void setYwlx(String ywlx){
		this.setInternal("YWLX",ywlx);
	}
	public String getYwlx(){
		return (String)this.getInternal("YWLX");
	}
}