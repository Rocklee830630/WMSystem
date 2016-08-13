package com.ccthanking.framework.log;

import java.util.Date;
import com.ccthanking.framework.util.Pub;


/**
 * 用户操作日志
 */
public class OperationLogVO implements java.io.Serializable {
	
	public OperationLogVO() {
	}

	/**
	 * 操作编号
	 */
	private String OPID;

	/**
	 * 用户ID
	 */
	private String USERID;

	/**
	 * 用户姓名
	 */
	private String USERNAME;

	/**
	 * 用户单位ID
	 */
	private String USERDEPTID;

	/**
	 * 用户操作IP
	 */
	private String OPERATEIP;

	/**
	 * 用户操作时间
	 */
	private Date OPERATETIME;

	/**
	 * 用户操作业务类型
	 */
	private String YWLX;

	/**
	 * 用户操作事件编号
	 */
	private String SJBH;

	/**
	 * 操作结果
	 */
	private String RESULT;

	/**
	 * 登陆日志编号
	 */
	private String LOGINID;
	private String MONTH;
	private String OPERATETYPE;
	/**
	 * 业务主键字段
	 */
	private String YWZJZD;
	/**
	 * 业务主键值
	 */
	private String YWZJZ;
	/**
	 * 用户单位名称
	 */
	private String USERDEPTNAME;


	
	public String getYWZJZD() {
		return YWZJZD;
	}

	public void setYWZJZD(String yWZJZD) {
		YWZJZD = yWZJZD;
	}

	public String getYWZJZ() {
		return YWZJZ;
	}

	public void setYWZJZ(String yWZJZ) {
		YWZJZ = yWZJZ;
	}

	public String getUSERDEPTNAME() {
		return this.USERDEPTNAME;
	}

	public void setUSERDEPTNAME(String type) {
		this.USERDEPTNAME = type;
	}

	public String getOPERATETYPE() {
		return this.OPERATETYPE;
	}

	public void setOPERATETYPE(String type) {
		this.OPERATETYPE = type;
	}

	public String getMONTH() {
		return this.MONTH;
	}

	public void setMONTH(String aMONTH) {
		this.MONTH = aMONTH;
	}

	private String MEMO;

	public String getMEMO() {
		return this.MEMO;
	}

	public void setMEMO(String aMEMO) {
		this.MEMO = aMEMO;
	}

	public String getOPID() {
		return OPID;
	}

	public void setOPID(String aOPID) {
		OPID = aOPID;
	}

	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String aUSERID) {
		USERID = aUSERID;
	}

	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String aUSERNAME) {
		USERNAME = aUSERNAME;
	}

	public String getUSERDEPTID() {
		return USERDEPTID;
	}

	public void setUSERDEPTID(String aUSERDEPTID) {
		USERDEPTID = aUSERDEPTID;
	}

	public String getOPERATEIP() {
		return OPERATEIP;
	}

	public void setOPERATEIP(String aOPERATEIP) {
		OPERATEIP = aOPERATEIP;
	}

	public Date getOPERATETIME() {
		return OPERATETIME;
	}

	public void setOPERATETIME(Date aOPERATETIME) {
		OPERATETIME = aOPERATETIME;
		if (OPERATETIME != null) {
			if (this.MONTH == null || this.MONTH.equals("")) {
				this.MONTH = Pub.getDate("MM", OPERATETIME);
			}
		}
	}

	public String getYWLX() {
		return YWLX;
	}

	public void setYWLX(String aYWLX) {
		YWLX = aYWLX;
	}

	public String getSJBH() {
		return SJBH;
	}

	public void setSJBH(String aSJBH) {
		SJBH = aSJBH;
	}

	public String getRESULT() {
		return RESULT;
	}

	public void setRESULT(String aRESULT) {
		RESULT = aRESULT;
	}

	public String getLOGINID() {
		return LOGINID;
	}

	public void setLOGINID(String aLOGINID) {
		LOGINID = aLOGINID;
	}
}
