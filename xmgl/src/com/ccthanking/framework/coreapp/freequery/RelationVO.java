//v20071212-1
package com.ccthanking.framework.coreapp.freequery;

/*
 * author : michael wang
 * create time : 2007.04.08
 * you are free to copy,modify,rewrite this code,but please you reserve author's name.
 * 
 * version 1.0 2007.04.08 create
 *         1.1 2007.05.03 apply for jwzh 
 */

public class RelationVO {
	private String strSourceField;
	private String strSourceType;
	private String strDestField;
	private String strOp;
	private String strSourceTable;
	private String strDestTable;
	
	public String getSourceField() {
		return strSourceField;
	}
	public String getSourceType() {
		return strSourceType;
	}
	public String getDestField() {
		return strDestField;
	}
	public String getOp() {
		return strOp;
	}
	public String getSourceTable() {
		return strSourceTable;
	}
	public String getDestTable() {
		return strDestTable;
	}
	public void setSourceTable(String p_Table) {
		strSourceTable = p_Table;
	}
	public void setDestTable(String p_Table) {
		strDestTable = p_Table;
	}
	public void setDestField(String p_DestField) {
		strDestField = p_DestField;
	}
	public void setSourceField(String p_SourceField) {
		strSourceField = p_SourceField;
	}
	public void setSourceType(String p_SourceType) {
		strSourceType = p_SourceType;
	}
	public void setOp(String p_Op) {
		strOp = p_Op;
	}
}
