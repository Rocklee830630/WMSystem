package com.ccthanking.common.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class OrgPersonVO extends BaseVO{

	public OrgPersonVO(){
		this.addField("ACCOUNT",OP_STRING|this.TP_PK);
		this.addField("PASSWORD",OP_STRING);
		this.addField("NAME",OP_STRING);
		this.addField("SEX",OP_STRING);
		this.addField("DEPARTMENT",OP_STRING);
		this.addField("PARENT",OP_STRING);
		this.addField("PERSON_KIND",OP_STRING);
		this.addField("USER_SN",OP_STRING);
		this.addField("LEVEL_NAME",OP_STRING);
		this.addField("SECRET_LEVEL",OP_STRING);
		this.addField("FLAG",OP_STRING);
		this.addField("IDCARD",OP_STRING);
		this.addField("CERTCODE",OP_STRING);
		this.addField("SMTP",OP_STRING);
		this.addField("MAILFROM",OP_STRING);
		this.addField("MAILNAME",OP_STRING);
		this.addField("MAILPSW",OP_STRING);
		this.addField("USERTEMPLATE",OP_STRING);
		this.addField("JWQ",OP_STRING);
		this.addField("ZRQ",OP_STRING);
		this.addField("SJHM",OP_STRING);
		this.setVOTableName("FS_ORG_PERSON");
	}

	public void setAccount(String account){
		this.setInternal("ACCOUNT",account);
	}
	public String getAccount(){
		return (String)this.getInternal("ACCOUNT");
	}
	public void setPassword(String password){
		this.setInternal("PASSWORD",password);
	}
	public String getPassword(){
		return (String)this.getInternal("PASSWORD");
	}
	public void setName(String name){
		this.setInternal("NAME",name);
	}
	public String getName(){
		return (String)this.getInternal("NAME");
	}
	public void setSex(String sex){
		this.setInternal("SEX",sex);
	}
	public String getSex(){
		return (String)this.getInternal("SEX");
	}
	public void setDepartment(String department){
		this.setInternal("DEPARTMENT",department);
	}
	public String getDepartment(){
		return (String)this.getInternal("DEPARTMENT");
	}
	public void setParent(String parent){
		this.setInternal("PARENT",parent);
	}
	public String getParent(){
		return (String)this.getInternal("PARENT");
	}
	public void setPerson_kind(String person_kind){
		this.setInternal("PERSON_KIND",person_kind);
	}
	public String getPerson_kind(){
		return (String)this.getInternal("PERSON_KIND");
	}
	public void setUser_sn(String user_sn){
		this.setInternal("USER_SN",user_sn);
	}
	public String getUser_sn(){
		return (String)this.getInternal("USER_SN");
	}
	public void setLevel_name(String level_name){
		this.setInternal("LEVEL_NAME",level_name);
	}
	public String getLevel_name(){
		return (String)this.getInternal("LEVEL_NAME");
	}
	public void setSecret_level(String secret_level){
		this.setInternal("SECRET_LEVEL",secret_level);
	}
	public String getSecret_level(){
		return (String)this.getInternal("SECRET_LEVEL");
	}
	public void setFlag(String flag){
		this.setInternal("FLAG",flag);
	}
	public String getFlag(){
		return (String)this.getInternal("FLAG");
	}
	public void setIdcard(String idcard){
		this.setInternal("IDCARD",idcard);
	}
	public String getIdcard(){
		return (String)this.getInternal("IDCARD");
	}
	public void setCertcode(String certcode){
		this.setInternal("CERTCODE",certcode);
	}
	public String getCertcode(){
		return (String)this.getInternal("CERTCODE");
	}
	public void setSmtp(String smtp){
		this.setInternal("SMTP",smtp);
	}
	public String getSmtp(){
		return (String)this.getInternal("SMTP");
	}
	public void setMailfrom(String mailfrom){
		this.setInternal("MAILFROM",mailfrom);
	}
	public String getMailfrom(){
		return (String)this.getInternal("MAILFROM");
	}
	public void setMailname(String mailname){
		this.setInternal("MAILNAME",mailname);
	}
	public String getMailname(){
		return (String)this.getInternal("MAILNAME");
	}
	public void setMailpsw(String mailpsw){
		this.setInternal("MAILPSW",mailpsw);
	}
	public String getMailpsw(){
		return (String)this.getInternal("MAILPSW");
	}
	public void setUsertemplate(String usertemplate){
		this.setInternal("USERTEMPLATE",usertemplate);
	}
	public String getUsertemplate(){
		return (String)this.getInternal("USERTEMPLATE");
	}
	public void setJwq(String jwq){
		this.setInternal("JWQ",jwq);
	}
	public String getJwq(){
		return (String)this.getInternal("JWQ");
	}
	public void setZrq(String zrq){
		this.setInternal("ZRQ",zrq);
	}
	public String getZrq(){
		return (String)this.getInternal("ZRQ");
	}
	public void setSjhm(String sjhm){
		this.setInternal("SJHM",sjhm);
	}
	public String getSjhm(){
		return (String)this.getInternal("SJHM");
	}
}