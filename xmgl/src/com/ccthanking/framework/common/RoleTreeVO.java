package com.ccthanking.framework.common;
import com.ccthanking.framework.base.BaseVO;

public class RoleTreeVO extends BaseVO{

	public RoleTreeVO(){
		this.addField("FS_ORG_ROLE_TREE_ID",OP_STRING|this.TP_PK);
		this.addField("NODEID",OP_STRING);
		this.addField("NODENAME",OP_STRING);
		this.addField("PARENTID",OP_STRING);
		this.addField("SORT",OP_STRING);
		this.addField("NODELEVEL",OP_STRING);
		this.setVOTableName("FS_ORG_ROLE_TREE");
	}

	public void setFs_org_role_tree_id(String fs_org_role_tree_id){
		this.setInternal("FS_ORG_ROLE_TREE_ID",fs_org_role_tree_id);
	}
	public String getFs_org_role_tree_id(){
		return (String)this.getInternal("FS_ORG_ROLE_TREE_ID");
	}
	public void setNodeid(String nodeid){
		this.setInternal("NODEID",nodeid);
	}
	public String getNodeid(){
		return (String)this.getInternal("NODEID");
	}
	public void setNodename(String nodename){
		this.setInternal("NODENAME",nodename);
	}
	public String getNodename(){
		return (String)this.getInternal("NODENAME");
	}
	public void setParentid(String parentid){
		this.setInternal("PARENTID",parentid);
	}
	public String getParentid(){
		return (String)this.getInternal("PARENTID");
	}
	public void setSort(String sort){
		this.setInternal("SORT",sort);
	}
	public String getSort(){
		return (String)this.getInternal("SORT");
	}
	public void setNodelevel(String nodelevel){
		this.setInternal("NODELEVEL",nodelevel);
	}
	public String getNodelevel(){
		return (String)this.getInternal("NODELEVEL");
	}
}