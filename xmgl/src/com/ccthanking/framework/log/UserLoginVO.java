package com.ccthanking.framework.log;

import java.util.Date;

import com.ccthanking.framework.util.Pub;

/**
 * 用户登录日志
 */
public class UserLoginVO implements java.io.Serializable {
	
	public UserLoginVO() {
	}

	/**
	 * 登录编号
	 */
	private String LOGINID;

	/**
	 * 用户ID
	 */
	private String USERID;

	/**
	 * 用户姓名
	 */
	private String UERNAME;

	/**
	 * 用户单位ID
	 */
	private String USERDEPTID;

	/**
	 * 用户登录IP
	 */
	private String LOGINIP;

	/**
	 * 用户登录时间
	 */
	private Date LOGINTIME;

	/**
	 * 用户登录状态
	 */
	private String LOGINSTATUS;

	/**
	 * 用户注销时间
	 */
	private Date LOGOUTTIME;
	private String MONTH;

	public String getMONTH() {
		return this.MONTH;
	}

	public void setMONTH(String aMONTH) {
		this.MONTH = aMONTH;
	}

	public String getLOGINID() {
		return LOGINID;
	}

	public void setLOGINID(String aLOGINID) {
		LOGINID = aLOGINID;
	}

	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String aUSERID) {
		USERID = aUSERID;
	}

	public String getUERNAME() {
		return UERNAME;
	}

	public void setUERNAME(String aUERNAME) {
		UERNAME = aUERNAME;
	}

	public String getUSERDEPTID() {
		return USERDEPTID;
	}

	public void setUSERDEPTID(String aUSERDEPTID) {
		USERDEPTID = aUSERDEPTID;
	}

	public String getLOGINIP() {
		return LOGINIP;
	}

	public void setLOGINIP(String aLOGINIP) {
		LOGINIP = aLOGINIP;
	}

	public Date getLOGINTIME() {
		return LOGINTIME;
	}

	public void setLOGINTIME(Date aLOGINTIME) {
		LOGINTIME = aLOGINTIME;
		if (LOGINTIME != null) {
			this.setMONTH(Pub.getDate("MM", LOGINTIME));
		}
	}

	public String getLOGINSTATUS() {
		return LOGINSTATUS;
	}

	public void setLOGINSTATUS(String aLOGINSTATUS) {
		LOGINSTATUS = aLOGINSTATUS;
	}

	public Date getLOGOUTTIME() {
		return LOGOUTTIME;
	}

	public void setLOGOUTTIME(Date aLOGOUTTIME) {
		LOGOUTTIME = aLOGOUTTIME;
		if (Pub.empty(this.getMONTH())) {
			if (LOGOUTTIME != null)
				this.setMONTH(Pub.getDate("MM", LOGOUTTIME));
		}
	}
}
