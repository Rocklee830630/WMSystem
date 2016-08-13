package com.ccthanking.framework.log;

import java.util.Date;

import com.ccthanking.framework.util.Pub;

/**
 * 系统运行日志
 */
public class SysLogVO implements java.io.Serializable {

	/**
	 * 操作编号
	 */
	public String OPID;

	/**
	 * 操作时间
	 */
	public Date OPERATETIME;

	/**
	 * 操作结果
	 */
	public String RESULT;

	/**
	 * 操作日期中的月份（分区字段）
	 */
	public String MONTH;

	/**
	 * 操作描述
	 */
	public String MEMO;

	/**
	 * 操作类型
	 */
	public String OPERATETYPE;

	/**
	 * 运行模块名称
	 */
	public String MODULEID;

	public SysLogVO() {
	}

	public String getOPID() {
		return OPID;
	}

	public void setOPID(String aOPID) {
		OPID = aOPID;
	}

	public Date getOPERATETIME() {
		return OPERATETIME;
	}

	public void setOPERATETIME(Date aOPERATETIME) {
		OPERATETIME = aOPERATETIME;
		if (OPERATETIME != null) {
			this.setMONTH(Pub.getDate("MM", OPERATETIME));
		}

	}

	public String getRESULT() {
		return RESULT;
	}

	public void setRESULT(String aRESULT) {
		RESULT = aRESULT;
	}

	public String getMONTH() {
		return MONTH;
	}

	public void setMONTH(String aMONTH) {
		MONTH = aMONTH;
	}

	public String getMEMO() {
		return MEMO;
	}

	public void setMEMO(String aMEMO) {
		MEMO = aMEMO;
	}
	
	public String getOPERATETYPE() {
		return OPERATETYPE;
	}

	public void setOPERATETYPE(String aOPERATETYPE) {
		OPERATETYPE = aOPERATETYPE;
	}
	
	public String getMODULEID() {
		return MODULEID;
	}

	public void setMODULEID(String aMODULEID) {
		MODULEID = aMODULEID;
	}
}
