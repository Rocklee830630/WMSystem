package com.ccthanking.framework.log;

import java.util.Date;

/**
 * 重要数据变更日志
 */
public class DataLogVO implements java.io.Serializable {

	/**
	 * 变更编号
	 */
	private String UPDATEID;

	/**
	 * 操作用户ID
	 */
	private String USERID;

	/**
	 * 操作用户姓名
	 */
	private String USERNAME;

	/**
	 * 操作用户单位ID
	 */
	private String DEPTID;

	/**
	 * 用户操作IP
	 */
	private String OPERATEIP;

	/**
	 * 用户操作时间
	 */
	private Date OPERATETIME;

	/**
	 * 变更数据项
	 */
	private String DATATYPE;

	/**
	 * 变更前内容
	 */
	private String BEFOREVALUE;

	/**
	 * 变更后内容
	 */
	private String AFTERVALUE;

	/**
	 * 变更结果
	 */
	private String RESULT;

	/**
	 * 登陆日志编号
	 */
	private String LOGINID;

	/**
	 * @roseuid 4406447E030D
	 */
	public DataLogVO() {

	}

	/**
	 * Access method for the UPDATEID property.
	 * 
	 * @return the current value of the UPDATEID property
	 */
	public String getUPDATEID() {
		return UPDATEID;
	}

	/**
	 * Sets the value of the UPDATEID property.
	 * 
	 * @param aUPDATEID
	 *            the new value of the UPDATEID property
	 */
	public void setUPDATEID(String aUPDATEID) {
		UPDATEID = aUPDATEID;
	}

	/**
	 * Access method for the USERID property.
	 * 
	 * @return the current value of the USERID property
	 */
	public String getUSERID() {
		return USERID;
	}

	/**
	 * Sets the value of the USERID property.
	 * 
	 * @param aUSERID
	 *            the new value of the USERID property
	 */
	public void setUSERID(String aUSERID) {
		USERID = aUSERID;
	}

	/**
	 * Access method for the USERNAME property.
	 * 
	 * @return the current value of the USERNAME property
	 */
	public String getUSERNAME() {
		return USERNAME;
	}

	/**
	 * Sets the value of the USERNAME property.
	 * 
	 * @param aUSERNAME
	 *            the new value of the USERNAME property
	 */
	public void setUSERNAME(String aUSERNAME) {
		USERNAME = aUSERNAME;
	}

	/**
	 * Access method for the DEPTID property.
	 * 
	 * @return the current value of the DEPTID property
	 */
	public String getDEPTID() {
		return DEPTID;
	}

	/**
	 * Sets the value of the DEPTID property.
	 * 
	 * @param aDEPTID
	 *            the new value of the DEPTID property
	 */
	public void setDEPTID(String aDEPTID) {
		DEPTID = aDEPTID;
	}

	/**
	 * Access method for the OPERATEIP property.
	 * 
	 * @return the current value of the OPERATEIP property
	 */
	public String getOPERATEIP() {
		return OPERATEIP;
	}

	/**
	 * Sets the value of the OPERATEIP property.
	 * 
	 * @param aOPERATEIP
	 *            the new value of the OPERATEIP property
	 */
	public void setOPERATEIP(String aOPERATEIP) {
		OPERATEIP = aOPERATEIP;
	}

	/**
	 * Access method for the OPERATETIME property.
	 * 
	 * @return the current value of the OPERATETIME property
	 */
	public Date getOPERATETIME() {
		return OPERATETIME;
	}

	/**
	 * Sets the value of the OPERATETIME property.
	 * 
	 * @param aOPERATETIME
	 *            the new value of the OPERATETIME property
	 */
	public void setOPERATETIME(Date aOPERATETIME) {
		OPERATETIME = aOPERATETIME;
	}

	/**
	 * Access method for the DATATYPE property.
	 * 
	 * @return the current value of the DATATYPE property
	 */
	public String getDATATYPE() {
		return DATATYPE;
	}

	/**
	 * Sets the value of the DATATYPE property.
	 * 
	 * @param aDATATYPE
	 *            the new value of the DATATYPE property
	 */
	public void setDATATYPE(String aDATATYPE) {
		DATATYPE = aDATATYPE;
	}

	/**
	 * Access method for the BEFOREVALUE property.
	 * 
	 * @return the current value of the BEFOREVALUE property
	 */
	public String getBEFOREVALUE() {
		return BEFOREVALUE;
	}

	/**
	 * Sets the value of the BEFOREVALUE property.
	 * 
	 * @param aBEFOREVALUE
	 *            the new value of the BEFOREVALUE property
	 */
	public void setBEFOREVALUE(String aBEFOREVALUE) {
		BEFOREVALUE = aBEFOREVALUE;
	}

	/**
	 * Access method for the AFTERVALUE property.
	 * 
	 * @return the current value of the AFTERVALUE property
	 */
	public String getAFTERVALUE() {
		return AFTERVALUE;
	}

	/**
	 * Sets the value of the AFTERVALUE property.
	 * 
	 * @param aAFTERVALUE
	 *            the new value of the AFTERVALUE property
	 */
	public void setAFTERVALUE(String aAFTERVALUE) {
		AFTERVALUE = aAFTERVALUE;
	}

	/**
	 * Access method for the RESULT property.
	 * 
	 * @return the current value of the RESULT property
	 */
	public String getRESULT() {
		return RESULT;
	}

	/**
	 * Sets the value of the RESULT property.
	 * 
	 * @param aRESULT
	 *            the new value of the RESULT property
	 */
	public void setRESULT(String aRESULT) {
		RESULT = aRESULT;
	}

	/**
	 * Access method for the LOGINID property.
	 * 
	 * @return the current value of the LOGINID property
	 */
	public String getLOGINID() {
		return LOGINID;
	}

	/**
	 * Sets the value of the LOGINID property.
	 * 
	 * @param aLOGINID
	 *            the new value of the LOGINID property
	 */
	public void setLOGINID(String aLOGINID) {
		LOGINID = aLOGINID;
	}
}
