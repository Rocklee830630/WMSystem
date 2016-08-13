package com.ccthanking.business.lcgl;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class ApProcessstepVO extends BaseVO{

	public ApProcessstepVO(){
		this.addField("PROCESSTYPEOID",TP_PK);
		this.addField("STEPSEQUENCE",OP_STRING);
		this.addField("STEPOID",OP_STRING);
		this.addField("NAME",OP_STRING);
		this.addField("ROLENAME",OP_STRING);
		this.addField("ACTOR",OP_STRING);
		this.addField("STATE",OP_STRING);
		this.addField("PRECONDITION2",OP_STRING);
		this.addField("PRECONDITION3",OP_STRING);
		this.addField("PRECONDITION1",OP_STRING);
		this.addField("PRESTEPOID",OP_STRING);
		this.addField("NEXTSTEPOID",OP_STRING);
		this.addField("DEPTLEVEL",OP_STRING);
		this.addField("APPLICATION",OP_STRING);
		this.addField("MEMO",OP_STRING);
		this.addField("DEPTID",OP_STRING);
		this.addField("STEPEVENT",OP_STRING);
		this.addField("SHEDULE_DAYS",OP_STRING);
		this.addField("STEPACTOR",OP_STRING);
		this.addField("ACTORDEPT",OP_STRING);
		this.addField("ISMS",OP_STRING);
		this.addField("ISCC",OP_STRING);
		this.addField("CCACTOR",OP_STRING);
		this.setVOTableName("ap_processstep");
	}

	public void setProcesstypeoid(String processtypeoid){
		this.setInternal("PROCESSTYPEOID",processtypeoid);
	}
	public String getProcesstypeoid(){
		return (String)this.getInternal("PROCESSTYPEOID");
	}
	public void setStepsequence(String stepsequence){
		this.setInternal("STEPSEQUENCE",stepsequence);
	}
	public String getStepsequence(){
		return (String)this.getInternal("STEPSEQUENCE");
	}
	public void setStepoid(String stepoid){
		this.setInternal("STEPOID",stepoid);
	}
	public String getStepoid(){
		return (String)this.getInternal("STEPOID");
	}
	public void setName(String name){
		this.setInternal("NAME",name);
	}
	public String getName(){
		return (String)this.getInternal("NAME");
	}
	public void setRolename(String rolename){
		this.setInternal("ROLENAME",rolename);
	}
	public String getRolename(){
		return (String)this.getInternal("ROLENAME");
	}
	public void setActor(String actor){
		this.setInternal("ACTOR",actor);
	}
	public String getActor(){
		return (String)this.getInternal("ACTOR");
	}
	public void setState(String state){
		this.setInternal("STATE",state);
	}
	public String getState(){
		return (String)this.getInternal("STATE");
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
	public void setPrecondition1(String precondition1){
		this.setInternal("PRECONDITION1",precondition1);
	}
	public String getPrecondition1(){
		return (String)this.getInternal("PRECONDITION1");
	}
	public void setPrestepoid(String prestepoid){
		this.setInternal("PRESTEPOID",prestepoid);
	}
	public String getPrestepoid(){
		return (String)this.getInternal("PRESTEPOID");
	}
	public void setNextstepoid(String nextstepoid){
		this.setInternal("NEXTSTEPOID",nextstepoid);
	}
	public String getNextstepoid(){
		return (String)this.getInternal("NEXTSTEPOID");
	}
	public void setDeptlevel(String deptlevel){
		this.setInternal("DEPTLEVEL",deptlevel);
	}
	public String getDeptlevel(){
		return (String)this.getInternal("DEPTLEVEL");
	}
	public void setApplication(String application){
		this.setInternal("APPLICATION",application);
	}
	public String getApplication(){
		return (String)this.getInternal("APPLICATION");
	}
	public void setMemo(String memo){
		this.setInternal("MEMO",memo);
	}
	public String getMemo(){
		return (String)this.getInternal("MEMO");
	}
	public void setDeptid(String deptid){
		this.setInternal("DEPTID",deptid);
	}
	public String getDeptid(){
		return (String)this.getInternal("DEPTID");
	}
	public void setStepevent(String stepevent){
		this.setInternal("STEPEVENT",stepevent);
	}
	public String getStepevent(){
		return (String)this.getInternal("STEPEVENT");
	}
	public void setShedule_days(String shedule_days){
		this.setInternal("SHEDULE_DAYS",shedule_days);
	}
	public String getShedule_days(){
		return (String)this.getInternal("SHEDULE_DAYS");
	}
	public void setStepactor(String stepactor){
		this.setInternal("STEPACTOR",stepactor);
	}
	public String getStepactor(){
		return (String)this.getInternal("STEPACTOR");
	}
	public void setActordept(String actordept){
		this.setInternal("ACTORDEPT",actordept);
	}
	public String getActordept(){
		return (String)this.getInternal("ACTORDEPT");
	}
	public void setIsms(String isms){
		this.setInternal("ISMS",isms);
	}
	public String getIsms(){
		return (String)this.getInternal("ISMS");
	}
	public void setIscc(String iscc){
		this.setInternal("ISCC",iscc);
	}
	public String getIscc(){
		return (String)this.getInternal("ISCC");
	}
	public void setCcactor(String ccactor){
		this.setInternal("CCACTOR",ccactor);
	}
	public String getCcactor(){
		return (String)this.getInternal("CCACTOR");
	}
}