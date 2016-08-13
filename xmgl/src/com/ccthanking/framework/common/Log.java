package com.ccthanking.framework.common;

/**
 * @auther xhb 
 */
public class Log implements java.io.Serializable {
	
	private static final long serialVersionUID = 3437034067069350916L;
	
	private String loginId;
	private String userId;
	private String userName;
	private String userDeptId;
	private String loginIp;
	private String loginTime;
	private String loginStatus;
	private String loginOutTime;
	private String month;
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserDeptId() {
		return userDeptId;
	}
	public void setUserDeptId(String userDeptId) {
		this.userDeptId = userDeptId;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getLoginOutTime() {
		return loginOutTime;
	}
	public void setLoginOutTime(String loginOutTime) {
		this.loginOutTime = loginOutTime;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
