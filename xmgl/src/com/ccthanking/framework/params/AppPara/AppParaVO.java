package com.ccthanking.framework.params.AppPara;

import java.io.Serializable;

public class AppParaVO implements Serializable { /* 应用参数配置表 */

	String AppParaSn = null; /* 参数序号 */

	String AppParaApptype = null; /* 参数类型 */

	String AppParaOperationtype = null; /* 业务类型 */

	String AppParaUnitid = null; /* 单位编号 */

	String AppParaOrglevel = null; /* 单位级别 */

	String AppParaApplicateion = null; /* 应用名 */

	String AppParaParakey = null; /* 参数key */

	String AppParaParaname = null; /* 参数名称 */

	String AppParaParavalue1 = null; /* 参数值1 */

	String AppParaParavalue2 = null; /* 参数值2 */

	String AppParaParavalue3 = null; /* 参数值3 */

	String AppParaParavalue4 = null; /* 参数值4 */

	String AppParaMemo = null; /* 备注 */

	public AppParaVO() {
	}

	public void setValue(

	String appParaSn /* 参数序号 */
	, String appParaApptype /* 参数类型 */
	, String appParaOperationtype /* 业务类型 */
	, String appParaUnitid /* 单位编号 */
	, String appParaOrglevel /* 单位级别 */
	, String appParaApplicateion /* 应用名 */
	, String appParaParakey /* 参数key */
	, String appParaParaname /* 参数名称 */
	, String appParaParavalue1 /* 参数值1 */
	, String appParaParavalue2 /* 参数值2 */
	, String appParaParavalue3 /* 参数值3 */
	, String appParaParavalue4 /* 参数值4 */
	, String appParaMemo /* 备注 */

	) {

		this.AppParaSn = appParaSn; /* 参数序号 */

		this.AppParaApptype = appParaApptype; /* 参数类型 */

		this.AppParaOperationtype = appParaOperationtype; /* 业务类型 */

		this.AppParaUnitid = appParaUnitid; /* 单位编号 */

		this.AppParaOrglevel = appParaOrglevel; /* 单位级别 */

		this.AppParaApplicateion = appParaApplicateion; /* 应用名 */

		this.AppParaParakey = appParaParakey; /* 参数key */

		this.AppParaParaname = appParaParaname; /* 参数名称 */

		this.AppParaParavalue1 = appParaParavalue1; /* 参数值1 */

		this.AppParaParavalue2 = appParaParavalue2; /* 参数值2 */

		this.AppParaParavalue3 = appParaParavalue3; /* 参数值3 */

		this.AppParaParavalue4 = appParaParavalue4; /* 参数值4 */

		this.AppParaMemo = appParaMemo; /* 备注 */

	}

	/**
	 * 设置参数序号
	 * 
	 * @param appParaSn
	 *            String
	 */
	public void setAppParaSn(String appParaSn) {
		this.AppParaSn = appParaSn; /* 参数序号 */
	}

	/**
	 * 设置参数类型
	 * 
	 * @param appParaApptype
	 *            String
	 */
	public void setAppParaApptype(String appParaApptype) {
		this.AppParaApptype = appParaApptype; /* 参数类型 */
	}

	/**
	 * 设置业务类型
	 * 
	 * @param appParaOperationtype
	 *            String
	 */
	public void setAppParaOperationtype(String appParaOperationtype) {
		this.AppParaOperationtype = appParaOperationtype; /* 业务类型 */
	}

	/**
	 * 设置单位编号
	 * 
	 * @param appParaUnitid
	 *            String
	 */
	public void setAppParaUnitid(String appParaUnitid) {
		this.AppParaUnitid = appParaUnitid; /* 单位编号 */
	}

	/**
	 * 设置单位级别
	 * 
	 * @param appParaOrglevel
	 *            String
	 */
	public void setAppParaOrglevel(String appParaOrglevel) {
		this.AppParaOrglevel = appParaOrglevel; /* 单位级别 */
	}

	/**
	 * 设置应用名
	 * 
	 * @param appParaApplicateion
	 *            String
	 */
	public void setAppParaApplicateion(String appParaApplicateion) {
		this.AppParaApplicateion = appParaApplicateion; /* 应用名 */
	}

	/**
	 * 设置参数key
	 * 
	 * @param appParaParakey
	 *            String
	 */
	public void setAppParaParakey(String appParaParakey) {
		this.AppParaParakey = appParaParakey; /* 参数key */
	}

	/**
	 * 设置参数名称
	 * 
	 * @param appParaParaname
	 *            String
	 */
	public void setAppParaParaname(String appParaParaname) {
		this.AppParaParaname = appParaParaname; /* 参数名称 */
	}

	/**
	 * 设置参数值1
	 * 
	 * @param appParaParavalue1
	 *            String
	 */
	public void setAppParaParavalue1(String appParaParavalue1) {
		this.AppParaParavalue1 = appParaParavalue1; /* 参数值1 */
	}

	/**
	 * 设置参数值2
	 * 
	 * @param appParaParavalue2
	 *            String
	 */
	public void setAppParaParavalue2(String appParaParavalue2) {
		this.AppParaParavalue2 = appParaParavalue2; /* 参数值2 */
	}

	/**
	 * 设置参数值3
	 * 
	 * @param appParaParavalue3
	 *            String
	 */
	public void setAppParaParavalue3(String appParaParavalue3) {
		this.AppParaParavalue3 = appParaParavalue3; /* 参数值3 */
	}

	/**
	 * 设置参数值4
	 * 
	 * @param appParaParavalue4
	 *            String
	 */
	public void setAppParaParavalue4(String appParaParavalue4) {
		this.AppParaParavalue4 = appParaParavalue4; /* 参数值4 */
	}

	/**
	 * 设置备注
	 * 
	 * @param appParaMemo
	 *            String
	 */
	public void setAppParaMemo(String appParaMemo) {
		this.AppParaMemo = appParaMemo; /* 备注 */
	}

	/**
	 * 获得参数序号
	 * 
	 * @return String
	 */
	public String getAppParaSn() {
		return this.AppParaSn; /* 参数序号 */
	}

	/**
	 * 获得参数类型
	 * 
	 * @return String
	 */
	public String getAppParaApptype() {
		return this.AppParaApptype; /* 参数类型 */
	}

	/**
	 * 获得业务类型
	 * 
	 * @return String
	 */
	public String getAppParaOperationtype() {
		return this.AppParaOperationtype; /* 业务类型 */
	}

	/**
	 * 获得单位编号
	 * 
	 * @return String
	 */
	public String getAppParaUnitid() {
		return this.AppParaUnitid; /* 单位编号 */
	}

	/**
	 * 获得单位级别
	 * 
	 * @return String
	 */
	public String getAppParaOrglevel() {
		return this.AppParaOrglevel; /* 单位级别 */
	}

	/**
	 * 获得应用名
	 * 
	 * @return String
	 */
	public String getAppParaApplicateion() {
		return this.AppParaApplicateion; /* 应用名 */
	}

	/**
	 * 获得参数key
	 * 
	 * @return String
	 */
	public String getAppParaParakey() {
		return this.AppParaParakey; /* 参数key */
	}

	/**
	 * 获得参数名称
	 * 
	 * @return String
	 */
	public String getAppParaParaname() {
		return this.AppParaParaname; /* 参数名称 */
	}

	/**
	 * 获得参数值1
	 * 
	 * @return String
	 */
	public String getAppParaParavalue1() {
		return this.AppParaParavalue1; /* 参数值1 */
	}

	/**
	 * 获得参数值2
	 * 
	 * @return String
	 */
	public String getAppParaParavalue2() {
		return this.AppParaParavalue2; /* 参数值2 */
	}

	/**
	 * 获得参数值3
	 * 
	 * @return String
	 */
	public String getAppParaParavalue3() {
		return this.AppParaParavalue3; /* 参数值3 */
	}

	/**
	 * 获得参数值4
	 * 
	 * @return String
	 */
	public String getAppParaParavalue4() {
		return this.AppParaParavalue4; /* 参数值4 */
	}

	/**
	 * 获得备注
	 * 
	 * @return String
	 */
	public String getAppParaMemo() {
		return this.AppParaMemo; /* 备注 */
	}

}
