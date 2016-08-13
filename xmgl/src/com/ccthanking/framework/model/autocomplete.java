package com.ccthanking.framework.model;

public class autocomplete implements java.io.Serializable{
	private String tableName = "";//表名
	private String regionName = "";//显示内容
	private String regionNameEn = "";//显示内容全拼
	private String regionShortnameEn = "";//显示内容简拼
	private String regionCode = "";//显示内容代码
	private String matchInfo = "";//查询自定义json字符串
	private String matchCount = "";//自定义显示条数
	private String matchInputValue = "";//input输入值
	private String tablePrefix = "";		//表名前缀

	public String getTablePrefix() {
		return tablePrefix;
	}
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}
	public String getMatchInputValue() {
		return matchInputValue;
	}
	public void setMatchInputValue(String matchInputValue) {
		this.matchInputValue = matchInputValue;
	}
	public String getMatchInfo() {
		return matchInfo;
	}
	public void setMatchInfo(String matchInfo) {
		this.matchInfo = matchInfo;
	}
	public String getMatchCount() {
		return matchCount;
	}
	public void setMatchCount(String matchCount) {
		this.matchCount = matchCount;
	}

	
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getRegionNameEn() {
		return regionNameEn;
	}
	public void setRegionNameEn(String regionNameEn) {
		this.regionNameEn = regionNameEn;
	}
	public String getRegionShortnameEn() {
		return regionShortnameEn;
	}
	public void setRegionShortnameEn(String regionShortnameEn) {
		this.regionShortnameEn = regionShortnameEn;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	

}
