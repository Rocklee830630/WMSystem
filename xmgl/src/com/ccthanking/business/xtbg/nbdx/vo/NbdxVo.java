package com.ccthanking.business.xtbg.nbdx.vo;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;

/**
 * @auther xhb 
 */
public class NbdxVo extends BaseVO {
	
	public NbdxVo() {
		this.addField("OPID",OP_STRING|this.TP_PK);
		this.addField("USERFROM",OP_STRING);
		this.addField("USERFROMNAME",OP_STRING);
		this.addField("USERTO",OP_STRING);
		this.addField("USERTONAME",OP_STRING);
		this.addField("TITLE",OP_STRING);
		this.addField("CONTENT",OP_STRING);
		this.addField("OPTIME",OP_DATE);
		this.addField("SYSMESSAGE",OP_STRING);
		this.addField("EMAILMESSAGE",OP_STRING);
		this.addField("SMSMESSAGE",OP_STRING);
		this.addField("DELOPER",OP_STRING);
		this.addField("DELTIME",OP_DATE);
		this.addField("STATE",OP_STRING);
		this.addField("ACCESSORY",OP_STRING);
		this.addField("LINKURL",OP_STRING);
		this.addField("SJBH",OP_STRING);
		this.addField("YWLX",OP_STRING);
		
		this.setFieldDateFormat("OPTIME","yyyy-MM-dd");
		this.setFieldDateFormat("DELTIME","yyyy-MM-dd");
		
		this.setVOTableName("FS_MESSAGE_INFO");
	}
	
	public void setOpid(String opid){
		this.setInternal("OPID",opid);
	}
	public String getOpid(){
		return (String)this.getInternal("OPID");
	}
	public void setUserfrom(String userfrom){
		this.setInternal("USERFROM",userfrom);
	}
	public String getUserfrom(){
		return (String)this.getInternal("USERFROM");
	}
	public void setUserfromname(String userfromname){
		this.setInternal("USERFROMNAME",userfromname);
	}
	public String getUserfromname(){
		return (String)this.getInternal("USERFROMNAME");
	}
	public void setUserto(String userto){
		this.setInternal("USERTO",userto);
	}
	public String getUserto(){
		return (String)this.getInternal("USERTO");
	}
	public void setUsertoname(String usertoname){
		this.setInternal("USERTONAME",usertoname);
	}
	public String getUsertoname(){
		return (String)this.getInternal("USERTONAME");
	}
	public void setTitle(String title){
		this.setInternal("TITLE",title);
	}
	public String getTitle(){
		return (String)this.getInternal("TITLE");
	}
	public void setContent(String content){
		this.setInternal("CONTENT",content);
	}
	public String getContent(){
		return (String)this.getInternal("CONTENT");
	}
	public void setOptime(Date optime){
		this.setInternal("OPTIME",optime);
	}
	public Date getOptime(){
		return (Date)this.getInternal("OPTIME");
	}
	public void setSysmessage(String sysmessage){
		this.setInternal("SYSMESSAGE",sysmessage);
	}
	public String getSysmessage(){
		return (String)this.getInternal("SYSMESSAGE");
	}
	public void setEmailmessage(String emailmessage){
		this.setInternal("EMAILMESSAGE",emailmessage);
	}
	public String getEmailmessage(){
		return (String)this.getInternal("EMAILMESSAGE");
	}
	public void setSmsmessage(String smsmessage){
		this.setInternal("SMSMESSAGE",smsmessage);
	}
	public String getSmsmessage(){
		return (String)this.getInternal("SMSMESSAGE");
	}
	public void setDeloper(String deloper){
		this.setInternal("DELOPER",deloper);
	}
	public String getDeloper(){
		return (String)this.getInternal("DELOPER");
	}
	public void setDeltime(Date deltime){
		this.setInternal("DELTIME",deltime);
	}
	public Date getDeltime(){
		return (Date)this.getInternal("DELTIME");
	}
	public void setState(String state){
		this.setInternal("STATE",state);
	}
	public String getState(){
		return (String)this.getInternal("STATE");
	}
	public void setAccessory(String accessory){
		this.setInternal("ACCESSORY",accessory);
	}
	public String getAccessory(){
		return (String)this.getInternal("ACCESSORY");
	}
	public void setLinkurl(String linkurl){
		this.setInternal("LINKURL",linkurl);
	}
	public String getLinkurl(){
		return (String)this.getInternal("LINKURL");
	}
	public void setSjbh(String sjbh){
		this.setInternal("SJBH",sjbh);
	}
	public String getSjbh(){
		return (String)this.getInternal("SJBH");
	}
	public void setYwlx(String ywlx){
		this.setInternal("YWLX",ywlx);
	}
	public String getYwlx(){
		return (String)this.getInternal("YWLX");
	}
}
