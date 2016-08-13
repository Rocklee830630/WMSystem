package com.ccthanking.business.lcgl;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class ApProcesstypeVO extends BaseVO{

	public ApProcesstypeVO(){
		this.addField("PROCESSTYPEOID",TP_PK);
		this.addField("NAME",OP_STRING);
		this.addField("OPERATIONOID",OP_STRING);
		this.addField("ACTOR",OP_STRING);
		this.addField("CREATETIME",OP_DATE);
		this.addField("STATE",OP_STRING);
		this.addField("PRECONDITION1",OP_STRING);
		this.addField("PRECONDITION2",OP_STRING);
		this.addField("PRECONDITION3",OP_STRING);
		this.addField("MEMO",OP_STRING);
		this.addField("PROCESSEVENT",OP_STRING);
		this.addField("PROCESSTYPE",OP_STRING);
		this.setFieldDateFormat("CREATETIME","yyyy-MM-dd");
		this.setVOTableName("ap_processtype");
		this.bindFieldToUserid("ACTOR");
	}

	public void setProcesstypeoid(String processtypeoid){
		this.setInternal("PROCESSTYPEOID",processtypeoid);
	}
	public String getProcesstypeoid(){
		return (String)this.getInternal("PROCESSTYPEOID");
	}
	public void setName(String name){
		this.setInternal("NAME",name);
	}
	public String getName(){
		return (String)this.getInternal("NAME");
	}
	public void setOperationoid(String operationoid){
		this.setInternal("OPERATIONOID",operationoid);
	}
	public String getOperationoid(){
		return (String)this.getInternal("OPERATIONOID");
	}
	public void setActor(String actor){
		this.setInternal("ACTOR",actor);
	}
	public String getActor(){
		return (String)this.getInternal("ACTOR");
	}
	public void setCreatetime(Date createtime){
		this.setInternal("CREATETIME",createtime);
	}
	public Date getCreatetime(){
		return (Date)this.getInternal("CREATETIME");
	}
	public void setState(String state){
		this.setInternal("STATE",state);
	}
	public String getState(){
		return (String)this.getInternal("STATE");
	}
	public void setPrecondition1(String precondition1){
		this.setInternal("PRECONDITION1",precondition1);
	}
	public String getPrecondition1(){
		return (String)this.getInternal("PRECONDITION1");
	}
	public void setPrecondition2(String precondition2){
		this.setInternal("PRECONDITION2",precondition2);
	}
	public String getPrecondition2(){
		return (String)this.getInternal("PRECONDITION2");
	}
	public void setPrecondition3(String precondition3){
		this.setInternal("PRECONDITION3",precondition3);
	}
	public String getPrecondition3(){
		return (String)this.getInternal("PRECONDITION3");
	}
	public void setMemo(String memo){
		this.setInternal("MEMO",memo);
	}
	public String getMemo(){
		return (String)this.getInternal("MEMO");
	}
	public void setProcessevent(String processevent){
		this.setInternal("PROCESSEVENT",processevent);
	}
	public String getProcessevent(){
		return (String)this.getInternal("PROCESSEVENT");
	}
	public void setProcesstype(String processtype){
		this.setInternal("PROCESSTYPE",processtype);
	}
	public String getProcesstype(){
		return (String)this.getInternal("PROCESSTYPE");
	}
}