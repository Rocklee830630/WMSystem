//Source file: e:\\projects\\rkyw\\src\\com\\ccthanking\\hnpmi\\rkyw\\jcsj\\vo\\TASK_SCHEDULE.java

package com.ccthanking.framework.coreapp.aplink;

import java.util.Date;

/**
 * 待办任务表
 */
public class TaskVO implements java.io.Serializable {
	public static final String TASK_TYPE_APPROVE = "2"; // 审批任务
	public static final String TASK_TYPE_DEFAULT = "5"; // 查看审批流程任务
	public static final String TASK_TYPE_PRINT = "1"; // 归档任务
	public static final String TASK_TYPE_GENERAL = "3"; // 普通任务
	public static final String TASK_TYPE_AFFIRM = "4"; // 回退任务
	public static final String TASK_TYPE_ROLLBACK = "6";// 退回任务
	public static final String TASK_TYPE_BREAK = "8";// 中断任务

	public static final String TASK_STATUS_VALID = "01";
	public static final String TASK_STATUS_FINISHED = "02";
	public static final String TASK_STATUS_FAILED = "03";
	public static final String TASK_STATUS_LOGOUT = "04";
	public static final String TASK_STATUS_INVALID = "05";
	public static final String TASK_STATUS_EXECUTED = "06";
	public static final String TASK_STATUS_BREAK = "07"; //中断

	/**
	 * 任务序号
	 */
	private String ID = "";

	/**
	 * 事件编号
	 */
	private String SJBH = "";

	/**
	 * 业务类型
	 */
	private String YWLX = "";

	/**
	 * 审批编号
	 */
	private String SPBH = "";

	/**
	 * 审批结果
	 */
	private String SPJG = "";

	/**
	 * 任务状态
	 */
	private String RWZT = "";

	/**
	 * 任务类型
	 */
	private String RWLX = "";

	/**
	 * 待办单位代码
	 */
	private String DBDWDM = "";

	/**
	 * 待办人帐号
	 */
	private String DBRYID = "";

	/**
	 * 待办人人要素
	 */
	private String DBRRYS = "";

	/**
	 * 任务创建时间
	 */
	private Date CJSJ;

	/**
	 * 任务完成时间
	 */
	private Date WCSJ;

	/**
	 * 任务注销时间
	 */
	private Date ZXSJ;

	/**
	 * 任务创建人帐号
	 */
	private String CJRID = "";

	/**
	 * 任务完成人帐号
	 */
	private String WCRID = "";

	/**
	 * 任务注销人帐号
	 */
	private String ZXRID = "";

	/**
	 * 业务信息--人
	 */
	private String RYSBH = "";

	/**
	 * 业务信息--其他序号
	 */
	private String XH = "";

	/**
	 * 业务信息--户号
	 */
	private String HH = "";

	/**
	 * 业务信息--省市县区
	 */
	private String SSXQ = "";

	/**
	 * 业务信息--派出所
	 */
	private String PCSDM = "";

	/**
	 * 业务信息--分局
	 */
	private String FJDM = "";

	/**
	 * 业务信息--市局
	 */
	private String SJDM = "";

	/**
	 * 备注
	 */
	private String MEMO = "";

	/**
	 * 链接目标
	 */
	private String LINKURL = "";
	private String taskSequence = "";
	private String result = "";
	private String resultDscr = "";
	private String dbRole = "";
	private String cjrxm = "";
	private String cjdwdm = "";
	private String spyj = "";
	private String spr = "";
	private String wcrxm = "", wcdwdm = "", zxrxm = "", zxdwdm = "", xm = "",
			xb = "", sfhm = "", mz = "", yxbs = "";
	// add by wuxp
	private String value1 = "", value2 = "", value3 = "", value4 = "";
	// add by cbl start //规定完成时间
	private Date shedule_time;
	private String stepsequence;// 节点顺序号

	// add by cbl end
	public String getValue1() {
		return this.value1;
	}

	public String getValue2() {
		return this.value2;
	}

	public String getValue3() {
		return this.value3;
	}

	public String getValue4() {
		return this.value4;
	}

	public void setValue1(String Value) {
		this.value1 = Value;
	}

	public void setValue2(String Value) {
		this.value2 = Value;
	}

	public void setValue3(String Value) {
		this.value3 = Value;
	}

	public void setValue4(String Value) {
		this.value4 = Value;
	}

	// add by wuxp
	private Date csrq;
	private String fBs = "";// 优先标示

	public String getFbs() {
		return this.fBs;
	}

	public void setFbs(String fBs) {
		this.fBs = fBs;
	}

	public Date getCsrq() {
		return this.csrq;
	}

	public void setCsrq(Date date) {
		this.csrq = date;
	}

	public String getXm() {
		return this.xm;
	}

	public String getXb() {
		return this.xb;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getMz() {
		return this.mz;
	}

	public String getSfhm() {
		return this.sfhm;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public void setSfhm(String sfhm) {
		this.sfhm = sfhm;
	}

	public String getWcrxm() {
		return this.wcrxm;
	}

	public String getWcdwdm() {
		return this.wcdwdm;
	}

	public void setWcrxm(String xm) {
		this.wcrxm = xm;
	}

	public void setWcdwdm(String dwdm) {
		this.wcdwdm = dwdm;
	}

	public String getZxrxm() {
		return this.zxrxm;
	}

	public String getZxdwdm() {
		return this.zxdwdm;
	}

	public void setZxrxm(String xm) {
		this.zxrxm = xm;
	}

	public void setZxdwdm(String dwdm) {
		this.zxdwdm = dwdm;
	}

	public String getSpyj() {
		return this.spyj;
	}

	public String getSpr() {
		return this.spr;
	}

	public void setSpyj(String spyj) {
		this.spyj = spyj;
	}

	public void setSpr(String spr) {
		this.spr = spr;
	}

	public String getCjrxm() {
		return this.cjrxm;
	}

	public void setCjrxm(String xm) {
		this.cjrxm = xm;
	}

	public String getCjdwdm() {
		return this.cjdwdm;
	}

	public void setCjdwdm(String dwdm) {
		this.cjdwdm = dwdm;
	}

	public String getDbRole() {
		return this.dbRole;
	}

	public void setDbRole(String role) {
		this.dbRole = role;
	}

	public String getTaskSequence() {
		return this.taskSequence;
	}

	public String getResult() {
		return this.result;
	}

	public String getResultDscr() {
		return this.resultDscr;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setResultDscr(String dscr) {
		this.resultDscr = dscr;
	}

	public void setTaskSequence(String seq) {
		this.taskSequence = seq;
	}

	/**
	 * @roseuid 43682DFD032C
	 */
	public TaskVO() {

	}

	/**
	 * Access method for the ID property.
	 * 
	 * @return the current value of the ID property
	 */
	public String getID() {
		return ID;
	}

	/**
	 * Sets the value of the ID property.
	 * 
	 * @param aID
	 *            the new value of the ID property
	 */
	public void setID(String aID) {
		ID = aID;
	}

	/**
	 * Access method for the SJBH property.
	 * 
	 * @return the current value of the SJBH property
	 */
	public String getSJBH() {
		return SJBH;
	}

	/**
	 * Sets the value of the SJBH property.
	 * 
	 * @param aSJBH
	 *            the new value of the SJBH property
	 */
	public void setSJBH(String aSJBH) {
		SJBH = aSJBH;
	}

	/**
	 * Access method for the YWLX property.
	 * 
	 * @return the current value of the YWLX property
	 */
	public String getYWLX() {
		return YWLX;
	}

	/**
	 * Sets the value of the YWLX property.
	 * 
	 * @param aYWLX
	 *            the new value of the YWLX property
	 */
	public void setYWLX(String aYWLX) {
		YWLX = aYWLX;
	}

	/**
	 * Access method for the SPBH property.
	 * 
	 * @return the current value of the SPBH property
	 */
	public String getSPBH() {
		return SPBH;
	}

	/**
	 * Sets the value of the SPBH property.
	 * 
	 * @param aSPBH
	 *            the new value of the SPBH property
	 */
	public void setSPBH(String aSPBH) {
		SPBH = aSPBH;
	}

	/**
	 * Access method for the SPJG property.
	 * 
	 * @return the current value of the SPJG property
	 */
	public String getSPJG() {
		return SPJG;
	}

	/**
	 * Sets the value of the SPJG property.
	 * 
	 * @param aSPJG
	 *            the new value of the SPJG property
	 */
	public void setSPJG(String aSPJG) {
		SPJG = aSPJG;
	}

	/**
	 * Access method for the RWZT property.
	 * 
	 * @return the current value of the RWZT property
	 */
	public String getRWZT() {
		return RWZT;
	}

	/**
	 * Sets the value of the RWZT property.
	 * 
	 * @param aRWZT
	 *            the new value of the RWZT property
	 */
	public void setRWZT(String aRWZT) {
		RWZT = aRWZT;
	}

	/**
	 * Access method for the RWLX property.
	 * 
	 * @return the current value of the RWLX property
	 */
	public String getRWLX() {
		return RWLX;
	}

	/**
	 * Sets the value of the RWLX property.
	 * 
	 * @param aRWLX
	 *            the new value of the RWLX property
	 */
	public void setRWLX(String aRWLX) {
		RWLX = aRWLX;
	}

	/**
	 * Access method for the DBDWDM property.
	 * 
	 * @return the current value of the DBDWDM property
	 */
	public String getDBDWDM() {
		return DBDWDM;
	}

	/**
	 * Sets the value of the DBDWDM property.
	 * 
	 * @param aDBDWDM
	 *            the new value of the DBDWDM property
	 */
	public void setDBDWDM(String aDBDWDM) {
		DBDWDM = aDBDWDM;
	}

	/**
	 * Access method for the DBRYID property.
	 * 
	 * @return the current value of the DBRYID property
	 */
	public String getDBRYID() {
		return DBRYID;
	}

	/**
	 * Sets the value of the DBRYID property.
	 * 
	 * @param aDBRYID
	 *            the new value of the DBRYID property
	 */
	public void setDBRYID(String aDBRYID) {
		DBRYID = aDBRYID;
	}

	/**
	 * Access method for the DBRRYS property.
	 * 
	 * @return the current value of the DBRRYS property
	 */
	public String getDBRRYS() {
		return DBRRYS;
	}

	/**
	 * Sets the value of the DBRRYS property.
	 * 
	 * @param aDBRRYS
	 *            the new value of the DBRRYS property
	 */
	public void setDBRRYS(String aDBRRYS) {
		DBRRYS = aDBRRYS;
	}

	/**
	 * Access method for the CJSJ property.
	 * 
	 * @return the current value of the CJSJ property
	 */
	public Date getCJSJ() {
		return CJSJ;
	}

	/**
	 * Sets the value of the CJSJ property.
	 * 
	 * @param aCJSJ
	 *            the new value of the CJSJ property
	 */
	public void setCJSJ(Date aCJSJ) {
		CJSJ = aCJSJ;
	}

	/**
	 * Access method for the WCSJ property.
	 * 
	 * @return the current value of the WCSJ property
	 */
	public Date getWCSJ() {
		return WCSJ;
	}

	/**
	 * Sets the value of the WCSJ property.
	 * 
	 * @param aWCSJ
	 *            the new value of the WCSJ property
	 */
	public void setWCSJ(Date aWCSJ) {
		WCSJ = aWCSJ;
	}

	// add by cbl start
	/**
	 * Access method for the WCSJ property.
	 * 
	 * @return the current value of the WCSJ property
	 */
	public Date getShedule_time() {
		return shedule_time;

	}

	/**
	 * Sets the value of the WCSJ property.
	 * 
	 * @param aWCSJ
	 *            the new value of the WCSJ property
	 */
	public void setShedule_time(Date ashedule_time) {
		shedule_time = ashedule_time;
	}

	public String getStepsequence() {
		return stepsequence;
	}

	public void SetStepsequence(String qstepsequence) {
		stepsequence = qstepsequence;
	}

	// add by cbl end
	/**
	 * Access method for the ZXSJ property.
	 * 
	 * @return the current value of the ZXSJ property
	 */
	public Date getZXSJ() {
		return ZXSJ;
	}

	/**
	 * Sets the value of the ZXSJ property.
	 * 
	 * @param aZXSJ
	 *            the new value of the ZXSJ property
	 */
	public void setZXSJ(Date aZXSJ) {
		ZXSJ = aZXSJ;
	}

	/**
	 * Access method for the CJRID property.
	 * 
	 * @return the current value of the CJRID property
	 */
	public String getCJRID() {
		return CJRID;
	}

	/**
	 * Sets the value of the CJRID property.
	 * 
	 * @param aCJRID
	 *            the new value of the CJRID property
	 */
	public void setCJRID(String aCJRID) {
		CJRID = aCJRID;
	}

	/**
	 * Access method for the WCRID property.
	 * 
	 * @return the current value of the WCRID property
	 */
	public String getWCRID() {
		return WCRID;
	}

	/**
	 * Sets the value of the WCRID property.
	 * 
	 * @param aWCRID
	 *            the new value of the WCRID property
	 */
	public void setWCRID(String aWCRID) {
		WCRID = aWCRID;
	}

	/**
	 * Access method for the ZXRID property.
	 * 
	 * @return the current value of the ZXRID property
	 */
	public String getZXRID() {
		return ZXRID;
	}

	/**
	 * Sets the value of the ZXRID property.
	 * 
	 * @param aZXRID
	 *            the new value of the ZXRID property
	 */
	public void setZXRID(String aZXRID) {
		ZXRID = aZXRID;
	}

	/**
	 * Access method for the RYSBH property.
	 * 
	 * @return the current value of the RYSBH property
	 */
	public String getRYSBH() {
		return RYSBH;
	}

	/**
	 * Sets the value of the RYSBH property.
	 * 
	 * @param aRYSBH
	 *            the new value of the RYSBH property
	 */
	public void setRYSBH(String aRYSBH) {
		RYSBH = aRYSBH;
	}

	/**
	 * Access method for the XH property.
	 * 
	 * @return the current value of the XH property
	 */
	public String getXH() {
		return XH;
	}

	/**
	 * Sets the value of the XH property.
	 * 
	 * @param aXH
	 *            the new value of the XH property
	 */
	public void setXH(String aXH) {
		XH = aXH;
	}

	/**
	 * Access method for the HH property.
	 * 
	 * @return the current value of the HH property
	 */
	public String getHH() {
		return HH;
	}

	/**
	 * Sets the value of the HH property.
	 * 
	 * @param aHH
	 *            the new value of the HH property
	 */
	public void setHH(String aHH) {
		HH = aHH;
	}

	/**
	 * Access method for the SSXQ property.
	 * 
	 * @return the current value of the SSXQ property
	 */
	public String getSSXQ() {
		return SSXQ;
	}

	/**
	 * Sets the value of the SSXQ property.
	 * 
	 * @param aSSXQ
	 *            the new value of the SSXQ property
	 */
	public void setSSXQ(String aSSXQ) {
		SSXQ = aSSXQ;
	}

	/**
	 * Access method for the PCSDM property.
	 * 
	 * @return the current value of the PCSDM property
	 */
	public String getPCSDM() {
		return PCSDM;
	}

	/**
	 * Sets the value of the PCSDM property.
	 * 
	 * @param aPCSDM
	 *            the new value of the PCSDM property
	 */
	public void setPCSDM(String aPCSDM) {
		PCSDM = aPCSDM;
	}

	/**
	 * Access method for the FJDM property.
	 * 
	 * @return the current value of the FJDM property
	 */
	public String getFJDM() {
		return FJDM;
	}

	/**
	 * Sets the value of the FJDM property.
	 * 
	 * @param aFJDM
	 *            the new value of the FJDM property
	 */
	public void setFJDM(String aFJDM) {
		FJDM = aFJDM;
	}

	/**
	 * Access method for the SJDM property.
	 * 
	 * @return the current value of the SJDM property
	 */
	public String getSJDM() {
		return SJDM;
	}

	/**
	 * Sets the value of the SJDM property.
	 * 
	 * @param aSJDM
	 *            the new value of the SJDM property
	 */
	public void setSJDM(String aSJDM) {
		SJDM = aSJDM;
	}

	/**
	 * Access method for the MEMO property.
	 * 
	 * @return the current value of the MEMO property
	 */
	public String getMEMO() {
		return MEMO;
	}

	/**
	 * Sets the value of the MEMO property.
	 * 
	 * @param aMEMO
	 *            the new value of the MEMO property
	 */
	public void setMEMO(String aMEMO) {
		MEMO = aMEMO;
	}

	public String getLINKURL() {
		return LINKURL;
	}

	/**
	 * Sets the value of the MEMO property.
	 * 
	 * @param aMEMO
	 *            the new value of the MEMO property
	 */
	public void setLINKURL(String aURL) {
		LINKURL = aURL;
	}
}
