package com.ccthanking.framework.common;

import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.coreapp.orgmanage.OrgRoleManager;

public class RoleVO extends BaseVO implements java.io.Serializable, Role {
	
	public RoleVO() {
		this.addField("ROLE_ID", this.OP_STRING | this.TP_PK);
		/* 角色名称 */
		this.addField("NAME", this.OP_STRING);
		/* 汉字描述 */
		this.addField("S_MEMO", this.OP_STRING);
		/* 应用的级别 */
		this.addField("LEVEL_NAME", this.OP_STRING);
		/* 创建者部门代码(owner/creator) */
		this.addField("DEPTID", this.OP_STRING);
		
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
		this.addField("BZ",OP_STRING);
		this.addField("LRR",OP_STRING);
		this.addField("LRSJ",OP_DATE);
		this.addField("LRBM",OP_STRING);
		this.addField("LRBMMC",OP_STRING);
		this.addField("GXR",OP_STRING);
		this.addField("GXSJ",OP_DATE);
		this.addField("GXBM",OP_STRING);
		this.addField("GXBMMC",OP_STRING);
		this.addField("SJMJ",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("ROLETYPE",OP_STRING);
		
		this.bindFieldToOrgDept("DEPTID");
		this.bindFieldToUserid("GXR");
		this.bindFieldToUserid("LRR");

        this.setVOTableName("FS_ORG_ROLE");
	}

	String name, memo, levelName, deptId, roleId,roletype,parentroleid;

	

	public String getName() {
		return this.name;
	}

	public String getMemo() {
		return this.memo;
	}

	public String getLevelName() {
		return this.levelName;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public void setDeptId(String id) {
		this.deptId = id;
	}

	private MenuVo[] menus;
	private User[] users;

	public MenuVo[] getMenus() throws Exception {
		//if (menus == null) { add by song 角色的菜单每次都重新加载
		// update by xhb on 2013-09-02
		menus = OrgRoleManager.getInstance().getRoleMenus(roleId);
		//}
		return menus;
	}

	public User[] getUsers() throws Exception {
		if (users == null)
			// update by xhb on 2013-09-02
			users = OrgRoleManager.getInstance().getRoleUsers(roleId);
		return users;
	}

	public void setUsers(User[] users) {
		this.users = users;
	}

	public void setMenus(MenuVo[] menus) {
		this.menus = menus;
	}

	public void setRoleId(String roleId) {
		// TODO Auto-generated method stub
		this.roleId = roleId;
	}

	public String getRoleId() {
		// TODO Auto-generated method stub
		return roleId;
	}
	public void setRoletype(String roletype) {
		// TODO Auto-generated method stub
		this.roletype = roletype;
	}

	public String getRoletype() {
		// TODO Auto-generated method stub
		return roletype;
	}
	public void setParentroleid(String parentroleid) {
		// TODO Auto-generated method stub
		this.parentroleid = parentroleid;
	}

	public String getParentroleid() {
		// TODO Auto-generated method stub
		return parentroleid;
	}
}
