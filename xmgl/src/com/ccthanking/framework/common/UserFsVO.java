package com.ccthanking.framework.common;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class UserFsVO extends BaseVO{

	public UserFsVO(){
		this.addField("FS_ORG_PERSON_FS_ID",OP_STRING);
		this.addField("ACCOUNT",OP_STRING);
		this.addField("FSID",OP_STRING);
		this.addField("FSLB",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.setVOTableName("FS_ORG_PERSON_FS");
	}

	public void setFs_org_person_fs_id(String fs_org_person_fs_id){
		this.setInternal("FS_ORG_PERSON_FS_ID",fs_org_person_fs_id);
	}
	public String getFs_org_person_fs_id(){
		return (String)this.getInternal("FS_ORG_PERSON_FS_ID");
	}
	public void setAccount(String account){
		this.setInternal("ACCOUNT",account);
	}
	public String getAccount(){
		return (String)this.getInternal("ACCOUNT");
	}
	public void setFsid(String fsid){
		this.setInternal("FSID",fsid);
	}
	public String getFsid(){
		return (String)this.getInternal("FSID");
	}
	public void setFslb(String fslb){
		this.setInternal("FSLB",fslb);
	}
	public String getFslb(){
		return (String)this.getInternal("FSLB");
	}
	public void setSfyx(String sfyx){
		this.setInternal("SFYX",sfyx);
	}
	public String getSfyx(){
		return (String)this.getInternal("SFYX");
	}
}