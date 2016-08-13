package com.ccthanking.framework.common;

/*
 * 角色的接口定义
 * 该接口目前只定义系统需要的几个基本的数据项
 * 如果业务需要扩展字段的话可以自己扩充这个接口
 */
public interface Role {

	/* start update by xhb on 2013-09-02 */
	public void setRoleId(String roleId);
	
	public String getRoleId();
	/* end */
	
	public String getName(); // 获取角色名称

	public void setName(String name); // 设置角色名称

	public String getMemo(); // 获取角色描述

	public void setMemo(String s_Memo); // 设置角色描述

	public String getLevelName(); // 获取角色级别

	public void setLevelName(String levelName); // 设置角色级别

	public String getDeptId(); // 获取创建者部门代码

	public void setDeptId(String deptId); // 设置创建者部门代码

	public MenuVo[] getMenus() throws Exception;

	public User[] getUsers() throws Exception;

	public void setUsers(User[] users);

	public void setMenus(MenuVo[] menus);
}
