package com.ccthanking.framework.model;

import java.util.Date;

import com.ccthanking.framework.common.MenuVo;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.common.Role;

/*
 * 系统帐户的接口定义
 * 该接口目前只定义系统需要的几个基本的数据项
 * 如果业务需要扩展字段的话可以自己扩充这个接口
 */
public interface User {
	
	public static final String COMMON_USER = "3";
	public static final String SUPER_USER = "1";
	public static final String ADMIN_USER = "2";

	public static final String FLAG_INVALIDATION = "2";
	public static final String FLAG_VALIDATION = "1";

	public String getAccount(); // 获取帐户名称

	public void setAccount(String account); // 设置帐户名称

	public String getPassWord(); // 获取帐户密码

	public void setPassWord(String password); // 设置账户密码

	public String getName(); // 获取用户名称

	public void setName(String name); // 设置用户名称

	public String getDepartment(); // 获取部门编号

	public void setDepartment(String department); // 设置部门编号

	public String getParent(); // 获取父亲帐户名称

	public void setParent(String parent); // 设置父亲帐户名称

	public String getPersonKind(); // 获取用户类型1、超级用户2、管理员3、普通用户4、受限用户、5过期无效用户

	public void setPersonKind(String personkind); // 设置用户类型

	public String getUserSN(); // 获取用户编号（警号）

	public void setUserSN(String usersn); // 设置用户编号

	public String getLevelName() throws Exception; // 获取所属级别

	public void setLevelName(String levelname); // 设置所属级别

	public String getScretLevel(); // 获取密级级别

	public void setScretLevel(String scretlevel); // 设置密级级别

	public String getFlag(); // 获取注销标识

	public void setFlag(String flag); // 设置注销标识

	public String getIdCard(); // 获取身份证号

	public void setIdCard(String idCard); // 设置身份证号

	public String getCertCode(); // 获取证书序号

	public void setCertCode(String certCode); // 设置证书序号

	public String getSex();

	public void setSex(String sex);

	public Role[] getRoles() throws Exception;

	public void setRoles(Role[] roles);

	public String getRoleListString() throws Exception;

	public MenuVo[] getAllowedMenus() throws Exception;

	public MenuVo[] getAllowedMenus(String parent);

	public void setAllowedMenus(MenuVo[] allowedMenus);

	public OrgDept getOrgDept() throws Exception;

	public String getIP();

	public void setIP(String ip);

	public String getLoginLogID();

	public void setLoginLogID(String id);

	public Date getLoginTime();

	public void setLoginTime(Date datetime);

	public void setSmtp(String smtp);

	public void setMailFrom(String MAILFROM);

	public void setMailName(String MAILNAME);

	public void setMailPsw(String MAILPSW);

	public String getSmtp();

	public String getMailFrom();

	public String getMailName();

	public String getMailPsw();

	public String getUserTemplate();

	public void setUserTemplate(String userTemplate);

	public String getZrqdm();

	public void setZrqdm(String zrq);

	public OrgDept getOrgDeptZrq() throws Exception;

	public String getJwqdm();

	public void setJwqdm(String jwq);

	public OrgDept getOrgDeptJwq() throws Exception;

	public String getAjzt();

	public void setAjzt(String ajzt);

	public String getYhbh();

	public void setYhbh(String yhbh);
}
