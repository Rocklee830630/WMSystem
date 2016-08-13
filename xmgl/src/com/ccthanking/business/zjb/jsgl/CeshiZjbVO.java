package com.ccthanking.business.zjb.jsgl;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class CeshiZjbVO extends BaseVO{

	public CeshiZjbVO(){
		this.addField("CESHI_ZJB_ID",OP_STRING|this.TP_PK);
		this.addField("HTMC",OP_STRING);
		this.addField("HTBH",OP_STRING);
		this.addField("XMLX",OP_STRING);
		this.addField("XMNF",OP_STRING);
		this.addField("QZD",OP_STRING);
		this.addField("HTNF",OP_STRING);
		this.addField("HTJE",OP_STRING);
		this.addField("HTQDFS",OP_STRING);
		this.addField("XMXDKID",OP_STRING);
		this.setVOTableName("CESHI_ZJB");
	}

	public void setCeshi_zjb_id(String ceshi_zjb_id){
		this.setInternal("CESHI_ZJB_ID",ceshi_zjb_id);
	}
	public String getCeshi_zjb_id(){
		return (String)this.getInternal("CESHI_ZJB_ID");
	}
	public void setHtmc(String htmc){
		this.setInternal("HTMC",htmc);
	}
	public String getHtmc(){
		return (String)this.getInternal("HTMC");
	}
	public void setHtbh(String htbh){
		this.setInternal("HTBH",htbh);
	}
	public String getHtbh(){
		return (String)this.getInternal("HTBH");
	}
	public void setXmlx(String xmlx){
		this.setInternal("XMLX",xmlx);
	}
	public String getXmlx(){
		return (String)this.getInternal("XMLX");
	}
	public void setXmnf(String xmnf){
		this.setInternal("XMNF",xmnf);
	}
	public String getXmnf(){
		return (String)this.getInternal("XMNF");
	}
	public void setQzd(String qzd){
		this.setInternal("QZD",qzd);
	}
	public String getQzd(){
		return (String)this.getInternal("QZD");
	}
	public void setHtnf(String htnf){
		this.setInternal("HTNF",htnf);
	}
	public String getHtnf(){
		return (String)this.getInternal("HTNF");
	}
	public void setHtje(String htje){
		this.setInternal("HTJE",htje);
	}
	public String getHtje(){
		return (String)this.getInternal("HTJE");
	}
	public void setHtqdfs(String htqdfs){
		this.setInternal("HTQDFS",htqdfs);
	}
	public String getHtqdfs(){
		return (String)this.getInternal("HTQDFS");
	}
	public void setXmxdkid(String xmxdkid){
		this.setInternal("XMXDKID",xmxdkid);
	}
	public String getXmxdkid(){
		return (String)this.getInternal("XMXDKID");
	}
}