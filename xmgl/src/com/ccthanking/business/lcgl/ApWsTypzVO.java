package com.ccthanking.business.lcgl;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class ApWsTypzVO extends BaseVO{

	public ApWsTypzVO(){
		this.addField("YWLX",OP_STRING);
		this.addField("WS_TEMPLATE_ID",OP_STRING);
		this.addField("OPERATIONOID",OP_STRING);
		this.addField("DEPT_LEVEL",OP_STRING);
		this.addField("DEPTID",OP_STRING);
		this.addField("ROLENAME",OP_STRING);
		this.addField("CONDITION",OP_STRING);
		this.addField("WS_TYPZ_ID",OP_STRING|this.TP_PK);
		this.addField("PROCESSNAME",OP_STRING);
		this.setVOTableName("AP_WS_TYPZ");
		this.bindFieldToTranslater("WS_TEMPLATE_ID", "WS_TEMPLATE", "WS_TEMPLATE_ID", "WS_TEMPLATE_NAME");
		this.bindFieldToDic("YWLX", "YWLX");
		this.bindFieldToOrgDept("DEPTID");
	}

	public void setYwlx(String ywlx){
		this.setInternal("YWLX",ywlx);
	}
	public String getYwlx(){
		return (String)this.getInternal("YWLX");
	}
	public void setWs_template_id(String ws_template_id){
		this.setInternal("WS_TEMPLATE_ID",ws_template_id);
	}
	public String getWs_template_id(){
		return (String)this.getInternal("WS_TEMPLATE_ID");
	}
	public void setOperationoid(String operationoid){
		this.setInternal("OPERATIONOID",operationoid);
	}
	public String getOperationoid(){
		return (String)this.getInternal("OPERATIONOID");
	}
	public void setDept_level(String dept_level){
		this.setInternal("DEPT_LEVEL",dept_level);
	}
	public String getDept_level(){
		return (String)this.getInternal("DEPT_LEVEL");
	}
	public void setDeptid(String deptid){
		this.setInternal("DEPTID",deptid);
	}
	public String getDeptid(){
		return (String)this.getInternal("DEPTID");
	}
	public void setRolename(String rolename){
		this.setInternal("ROLENAME",rolename);
	}
	public String getRolename(){
		return (String)this.getInternal("ROLENAME");
	}
	public void setCondition(String condition){
		this.setInternal("CONDITION",condition);
	}
	public String getCondition(){
		return (String)this.getInternal("CONDITION");
	}
	public void setWs_typz_id(String ws_typz_id){
		this.setInternal("WS_TYPZ_ID",ws_typz_id);
	}
	public String getWs_typz_id(){
		return (String)this.getInternal("WS_TYPZ_ID");
	}
	public void setProcessname(String processname){
		this.setInternal("PROCESSNAME",processname);
	}
	public String getProcessname(){
		return (String)this.getInternal("PROCESSNAME");
	}
}