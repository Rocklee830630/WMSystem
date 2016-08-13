package com.ccthanking.framework.message.messagemgr;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;

public class MessageInfoVO extends BaseVO {
	
	public MessageInfoVO() {

		super(15);
		this.addField("OPID", this.TP_PK);
		this.addField("USERFROM");
		this.addField("USERFROMNAME");
		this.addField("USERTO");
		this.addField("USERTONAME");
		this.addField("TITLE");
		this.addField("CONTENT");
		this.addField("OPTIME", this.OP_DATE);
		this.addField("SYSMESSAGE");
		this.addField("EMAILMESSAGE");
		this.addField("SMSMESSAGE");
		this.addField("STATE");
		this.addField("DELOPER");
		this.addField("DELTIME", this.OP_DATE);
		this.addField("ACCESSORY");
		this.addField("LINKURL");
		this.addField("SJBH");
		this.addField("YWLX");
		this.addField("OPERATOR_NO");

		// this.bindFieldToSequence("OPID","MESSAGE_INFO_OPID");//绑定opid序列

		// this.setVOOwner("empty");
		this.setVOTableName("FS_MESSAGE_INFO");
	}

	public void setOPID(String OPID) {
		this.setInternal("OPID", OPID);
	}

	public String getOPID() {
		return (String) this.getInternal("OPID");
	}

	public void setUSERFROM(String USERFROM) {
		this.setInternal("USERFROM", USERFROM);
	}

	public String getUSERFROM() {
		return (String) this.getInternal("USERFROM");
	}

	public String getUSERFROMNAME() {
		return (String) this.getInternal("USERFROMNAME");
	}

	public void setUSERFROMNAME(String USERFROMNAME) {
		this.setInternal("USERFROMNAME", USERFROMNAME);
	}

	public void setUSERTO(String USERTO) {
		this.setInternal("USERTO", USERTO);
	}

	public String getUSERTO() {
		return (String) this.getInternal("USERTO");
	}

	public void setUSERTONAME(String USERTONAME) {
		this.setInternal("USERTONAME", USERTONAME);
	}

	public String getUSERTONAME() {
		return (String) this.getInternal("USERTONAME");
	}

	public void setTITLE(String TITLE) {
		this.setInternal("TITLE", TITLE);
	}

	public String getTITLE() {
		return (String) this.getInternal("TITLE");
	}

	public void setCONTENT(String CONTENT) {
		this.setInternal("CONTENT", CONTENT);
	}

	public String getCONTENT() {
		return (String) this.getInternal("CONTENT");
	}

	public void setOPTIME(Date OPTIME) {
		this.setInternal("OPTIME", OPTIME);
	}

	public Date getOPTIME() {
		return (Date) this.getInternal("OPTIME");
	}

	public void setSYSMESSAGE(String SYSMESSAGE) {
		this.setInternal("SYSMESSAGE", SYSMESSAGE);
	}

	public String getSYSMESSAGE() {
		return (String) this.getInternal("SYSMESSAGE");
	}

	public void setEMAILMESSAGE(String EMAILMESSAGE) {
		this.setInternal("EMAILMESSAGE", EMAILMESSAGE);
	}

	public String getEMAILMESSAGE() {
		return (String) this.getInternal("EMAILMESSAGE");
	}

	public void setSMSMESSAGE(String SMSMESSAGE) {
		this.setInternal("SMSMESSAGE", SMSMESSAGE);
	}

	public String getSMSMESSAGE() {
		return (String) this.getInternal("SMSMESSAGE");
	}

	public void setDELTIME(Date DELTIME) {
		this.setInternal("DELTIME", DELTIME);
	}

	public Date getDELTIME() {
		return (Date) this.getInternal("DELTIME");
	}

	public void setDELOPER(String DELOPER) {
		this.setInternal("DELOPER", DELOPER);
	}

	public String getDELOPER() {
		return (String) this.getInternal("DELOPER");
	}

	public void setSTATE(String STATE) {
		this.setInternal("STATE", STATE);
	}

	public String getSTATE() {
		return (String) this.getInternal("STATE");
	}

	public void setACCESSORY(String ACCESSORY) {
		this.setInternal("ACCESSORY", ACCESSORY);
	}

	public String getACCESSORY() {
		return (String) this.getInternal("ACCESSORY");
	}

	public void setLINKURL(String LINKURL) {
		this.setInternal("LINKURL", LINKURL);
	}

	public String getLINKURL() {
		return (String) this.getInternal("LINKURL");
	}

	public void setSJBH(String SJBH) {
		this.setInternal("SJBH", SJBH);
	}

	public String getSJBH() {
		return (String) this.getInternal("SJBH");
	}

	public void setYWLX(String YWLX) {
		this.setInternal("YWLX", YWLX);
	}

	public String getYWLX() {
		return (String) this.getInternal("YWLX");
	}

	public void setOPERATOR_NO(String OPERATOR_NO) {
		this.setInternal("OPERATOR_NO", OPERATOR_NO);
	}
	
	public String getOPERATOR_NO() {
		return (String) this.getInternal("OPERATOR_NO");
	}
}
