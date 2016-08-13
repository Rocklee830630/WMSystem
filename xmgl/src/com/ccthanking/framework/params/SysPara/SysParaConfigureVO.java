package com.ccthanking.framework.params.SysPara;

import com.ccthanking.framework.base.BaseVO;

public class SysParaConfigureVO extends BaseVO { /* 系统参数配置表 */

	public String SN = null; /* 参数序号 */

	public String APPTYPE = null; /* 参数类型 */

	public String APPLICATION = null; /* 应用名 */

	public String PARAKEY = null; /* 参数key */

	public String PARANAME = null; /* 参数名称 */

	public String PARAVALUE1 = null; /* 参数值1 */

	public String PARAVALUE2 = null; /* 参数值2 */

	public String PARAVALUE3 = null; /* 参数值3 */

	public String PARAVALUE4 = null; /* 参数值4 */

	public String MEMO = null; /* 备注 */

	public SysParaConfigureVO() {
	}

	/**
	 * 设置参数序号
	 * 
	 * @param sysParaConfigureSn
	 *            String
	 */
	public void setSysParaConfigureSn(String sysParaConfigureSn) {

		this.SN = sysParaConfigureSn; /* 参数序号 */

	}

	/**
	 * 设置参数类型
	 * 
	 * @param sysParaConfigureApptype
	 *            String
	 */
	public void setSysParaConfigureApptype(String sysParaConfigureApptype) {

		this.APPTYPE = sysParaConfigureApptype; /* 参数类型 */

	}

	/**
	 * 设置应用名
	 * 
	 * @param sysParaConfigureApplicateion
	 *            String
	 */
	public void setSysParaConfigureApplicateion(
			String sysParaConfigureApplicateion) {

		this.APPLICATION = sysParaConfigureApplicateion; /* 应用名 */

	}

	/**
	 * 设置参数key
	 * 
	 * @param sysParaConfigureParakey
	 *            String
	 */
	public void setSysParaConfigureParakey(String sysParaConfigureParakey) {

		this.PARAKEY = sysParaConfigureParakey; /* 参数key */

	}

	/**
	 * 设置参数名称
	 * 
	 * @param sysParaConfigureParaname
	 *            String
	 */
	public void setSysParaConfigureParaname(String sysParaConfigureParaname) {

		this.PARANAME = sysParaConfigureParaname; /* 参数名称 */

	}

	/**
	 * 设置参数值1
	 * 
	 * @param sysParaConfigureParavalue1
	 *            String
	 */
	public void setSysParaConfigureParavalue1(String sysParaConfigureParavalue1) {

		this.PARAVALUE1 = sysParaConfigureParavalue1; /* 参数值1 */

	}

	/**
	 * 设置参数值2
	 * 
	 * @param sysParaConfigureParavalue2
	 *            String
	 */
	public void setSysParaConfigureParavalue2(String sysParaConfigureParavalue2) {

		this.PARAVALUE2 = sysParaConfigureParavalue2; /* 参数值2 */

	}

	/**
	 * 设置参数值3
	 * 
	 * @param sysParaConfigureParavalue3
	 *            String
	 */
	public void setSysParaConfigureParavalue3(String sysParaConfigureParavalue3) {

		this.PARAVALUE3 = sysParaConfigureParavalue3; /* 参数值3 */

	}

	/**
	 * 设置参数值4
	 * 
	 * @param sysParaConfigureParavalue4
	 *            String
	 */
	public void setSysParaConfigureParavalue4(String sysParaConfigureParavalue4) {

		this.PARAVALUE4 = sysParaConfigureParavalue4; /* 参数值4 */

	}

	/**
	 * 设置备注
	 * 
	 * @param sysParaConfigureMemo
	 *            String
	 */
	public void setSysParaConfigureMemo(String sysParaConfigureMemo) {

		this.MEMO = sysParaConfigureMemo; /* 备注 */

	}

	/**
	 * 获得参数序号
	 * 
	 * @return String
	 */
	public String getSysParaConfigureSn() {
		return this.SN; /* 参数序号 */
	}

	/**
	 * 获得参数类型
	 * 
	 * @return String
	 */
	public String getSysParaConfigureApptype() {
		return this.APPTYPE; /* 参数类型 */
	}

	/**
	 * 获得应用名
	 * 
	 * @return String
	 */
	public String getSysParaConfigureApplicateion() {
		return this.APPLICATION; /* 应用名 */
	}

	/**
	 * 获得参数key
	 * 
	 * @return String
	 */
	public String getSysParaConfigureParakey() {
		return this.PARAKEY; /* 参数key */
	}

	/**
	 * 获得参数名称
	 * 
	 * @return String
	 */
	public String getSysParaConfigureParaname() {
		return this.PARANAME; /* 参数名称 */
	}

	/**
	 * 获得参数值1
	 * 
	 * @return String
	 */
	public String getSysParaConfigureParavalue1() {
		return this.PARAVALUE1; /* 参数值1 */
	}

	/**
	 * 获得参数值2
	 * 
	 * @return String
	 */
	public String getSysParaConfigureParavalue2() {
		return this.PARAVALUE2; /* 参数值2 */
	}

	/**
	 * 获得参数值3
	 * 
	 * @return String
	 */
	public String getSysParaConfigureParavalue3() {
		return this.PARAVALUE3; /* 参数值3 */
	}

	/**
	 * 获得参数值4
	 * 
	 * @return String
	 */
	public String getSysParaConfigureParavalue4() {
		return this.PARAVALUE4; /* 参数值4 */
	}

	/**
	 * 获得备注
	 * 
	 * @return String
	 */
	public String getSysParaConfigureMemo() {
		return this.MEMO; /* 备注 */
	}

}
