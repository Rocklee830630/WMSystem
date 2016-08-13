package com.ccthanking.framework.common;

import java.util.ArrayList;
import java.util.Date;

import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.coreapp.orgmanage.MenuManager;
import com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager;
import com.ccthanking.framework.coreapp.orgmanage.OrgRoleManager;
import com.ccthanking.framework.util.Pub;

public class UserVO  extends BaseVO implements java.io.Serializable, User {
	
	
	
	public OrgDept getOrgDept() throws Exception {
		return OrgDeptManager.getInstance().getDeptByID(this.DEPARTMENT);
	}

	private Role[] roles;

	public Role[] getRoles() throws Exception {
		if (roles == null) {
			roles = OrgRoleManager.getInstance()
					.getUserRoles(this.getAccount());
		}
		return roles;
	}

	public String getRoleListString() throws Exception {
		if (roles == null)
			roles = this.getRoles();
		String role = "";
		if (roles != null)
			for (int i = 0; i < roles.length; i++) {
				if (i > 0)
					role += ",";
				role += "'" + roles[i].getName() + "'";
			}
		return role;
	}

	public String getRoleListIdString() throws Exception {
		if (roles == null)
			roles = this.getRoles();
		String role = "";
		if (roles != null)
			for (int i = 0; i < roles.length; i++) {
				if (i > 0)
					role += ",";
				role += "'" + roles[i].getRoleId() + "'";
			}
		return role;
	}
	
	public String getRoleIdInCondition() throws Exception {
		if (roles == null)
			roles = this.getRoles();
		String role = "";
		if (roles != null)
			for (int i = 0; i < roles.length; i++) {
				if (i > 0)
					role += ",";
				role += "'" + roles[i].getRoleId() + "'";
			}
		return role;
	}

	private MenuVo[] menus;

	public MenuVo[] getAllowedMenus() throws Exception {
		//if (menus == null) { add by song 用户的菜单每次都重新加载
		menus = MenuManager.getInstance().getAllowedMenus(this);
		//}
		return menus;
	}

	public MenuVo[] getAllowedMenus(String parent) {
		try {
			ArrayList ml = null;
			if (menus == null)
				this.getAllowedMenus();
			if (menus != null) {
				ml = new ArrayList();
				for (int j = 0; j < menus.length; j++) {
					if (menus[j] == null) {
					}
					if (menus[j] != null && Pub.empty(menus[j].getParent())
							&& Pub.empty(parent))
						ml.add(menus[j]);
					else if (menus[j] != null && !Pub.empty(parent)
							&& parent.equals(menus[j].getParent()))
						ml.add(menus[j]);
				}
				return (MenuVo[]) ml.toArray(new MenuVo[ml.size()]);
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	private String ACCOUNT;
	private String PASSWORD;
	private String NAME;
	private String SEX;
	private String DEPARTMENT;
	private String UserSN;
	private String EMAIL;
	private String ScretLevel = "1";
	private String FLAG = "1";
	private String CERTCODE;
	private String PARENT;
	private String PERSON_KIND = "3";
	private String levelname;
	private String ip;
	private String loginLogID;
	private Date loginTime;
	private String idcard;
	private String certcode;
	private String smtp;
	private String mailfrom;
	private String mailname;
	private String mailpsw;
	private String userTemplate;
	private String jwq, zrq;
	private String ajzt;
	private String yhbh;
	private String ywid;
	private String sort;

	public UserVO() {
		//设置字段信息
		this.addField("ACCOUNT",OP_STRING|this.TP_PK);
		this.addField("PASSWORD",OP_STRING);
		this.addField("NAME",OP_STRING);
		this.addField("SEX",OP_STRING);
		this.addField("DEPARTMENT",OP_STRING);
		this.addField("PERSON_KIND",OP_STRING);
		this.addField("IDCARD",OP_STRING);
		this.addField("SJHM",OP_STRING);
		this.addField("FLAG",OP_STRING);
		this.addField("SORT",OP_STRING);


		//字典
		this.bindFieldToDic("SEX", "XB");
		this.bindFieldToDic("FLAG", "SF");
		this.bindFieldToDic("PERSON_KIND", "YHLB");
		this.bindFieldToOrgDept("DEPARTMENT");
		
		this.setVOTableName("FS_ORG_PERSON");
	}
	public void setSort(String sort){
		this.sort = sort;
	}
	public String getSort(String sort){
		return this.sort;
	}

	public void setAjzt(String ajzt) {
		this.ajzt = ajzt;
	}

	public String getAjzt() {
		return this.ajzt;
	}

	public void setRoles(Role[] roles) {
		this.roles = roles;
	}

	public void setAllowedMenus(MenuVo[] allowedMenus) {
		this.menus = allowedMenus;
	}

	public String getUserSN() {
		return UserSN;
	}

	public void setUserSN(String userSN) {
		UserSN = userSN;
	}

	public String getAccount() {
		return this.ACCOUNT;
	}

	public void setAccount(String account) {
		this.ACCOUNT = account;
	}

	public String getPassWord() {
		return this.PASSWORD;
	}

	public void setPassWord(String password) {
		this.PASSWORD = password;
	}

	public String getName() {
		return this.NAME;
	}

	public void setName(String name) {
		this.NAME = name;
	}

	public String getDepartment() {
		return this.DEPARTMENT;
	}

	public void setDepartment(String department) {
		this.DEPARTMENT = department;
	}

	public String getParent() {
		return this.PARENT;
	}

	public void setParent(String parent) {
		this.PARENT = parent;
	}

	public String getPersonKind() {
		return this.PERSON_KIND;
	}

	public void setPersonKind(String personkind) {
		this.PERSON_KIND = personkind;
	}

	public String getLevelName() throws Exception {
		return String.valueOf(this.getOrgDept().getDeptLevel());
		// return this.levelname;
	}

	public void setLevelName(String levelname) {
		this.levelname = levelname;
	}

	public void setScretLevel(String scretlevel) {
		this.ScretLevel = scretlevel;
	}

	public String getFlag() {
		return this.FLAG;
	}

	public void setFlag(String flag) {
		this.FLAG = flag;
	}

	public String getIdCard() {
		return this.idcard;
	}

	public void setIdCard(String idCard) {
		this.idcard = idCard;
	}

	public String getCertCode() {
		return this.CERTCODE;
	}

	public void setCertCode(String certCode) {
		this.CERTCODE = certCode;
	}

	public String getScretLevel() {
		return this.ScretLevel;
	}

	public String getSex() {
		return this.SEX;
	}

	public void setSex(String sex) {
		this.SEX = sex;
	}

	public String getIP() {
		return this.ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	public String getLoginLogID() {
		return this.loginLogID;
	}

	public void setLoginLogID(String id) {
		this.loginLogID = id;
	}

	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date datetime) {
		this.loginTime = datetime;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	public void setMailFrom(String MAILFROM) {
		this.mailfrom = MAILFROM;
	}

	public void setMailName(String MAILNAME) {
		this.mailname = MAILNAME;
	}

	public void setMailPsw(String MAILPSW) {
		this.mailpsw = MAILPSW;
	}

	public String getSmtp() {
		return this.smtp;
	}

	public String getMailFrom() {
		return this.mailfrom;
	}

	public String getMailName() {
		return this.mailname;
	}

	public String getUserTemplate() {
		return this.userTemplate;
	}

	public void setUserTemplate(String userTemplate) {
		this.userTemplate = userTemplate;
	}

	public String getMailPsw() {
		return this.mailpsw;
	}

	public String getZrqdm() {
		return zrq;
	}

	public void setZrqdm(String zrq) {
		this.zrq = zrq;
	}

	public OrgDept getOrgDeptZrq() throws Exception {
		return OrgDeptManager.getInstance().getDeptByID(this.zrq);
	}

	public String getJwqdm() {
		return jwq;
	}

	public void setJwqdm(String jwq) {
		this.jwq = jwq;
	}

	public OrgDept getOrgDeptJwq() throws Exception {
		return OrgDeptManager.getInstance().getDeptByID(this.jwq);
	}

	public String getYhbh() {
		return this.yhbh;
	}

	public void setYhbh(String yhbh) {
		this.yhbh = yhbh;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}
}
