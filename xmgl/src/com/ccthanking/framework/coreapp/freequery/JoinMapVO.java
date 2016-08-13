//v20071212-1
package com.ccthanking.framework.coreapp.freequery;

public class JoinMapVO {

	private String strSTableName;
	private String strSFieldName;
	private String strSFieldType;
	private String strSFieldDic;
	private String strSFieldFmt;
	private String strDRelationName;
	private String strDQueryID;
	private String strDQueryName;
	private String strSFieldValue;
	
	public JoinMapVO() {
		
	}

	public String getDQueryID() {
		return strDQueryID;
	}
	
	public String getDQueryName() {
		return strDQueryName;
	}

	public String getDRelationName() {
		return strDRelationName;
	}

	public String getSFieldDic() {
		return strSFieldDic;
	}

	public String getSFieldFmt() {
		return strSFieldFmt;
	}

	public String getSFieldName() {
		return strSFieldName;
	}

	public String getSFieldType() {
		return strSFieldType;
	}

	public String getSTableName() {
		return strSTableName;
	}

	public String getSFieldValue(){
		return strSFieldValue;
	}
	
	public void setDQueryID(String p_DQueryID) {
		strDQueryID = p_DQueryID;
	}
	
	public void setDQueryName(String p_DQueryName) {
		strDQueryName = p_DQueryName;
	}

	public void setDRelationName(String p_DRelationName) {
		strDRelationName = p_DRelationName;
	}

	public void setSFieldDic(String p_SFieldDic) {
		strSFieldDic = p_SFieldDic;
	}

	public void setSFieldFmt(String p_SFieldFmt) {
		strSFieldFmt = p_SFieldFmt;
	}

	public void setSFieldName(String p_SFieldName) {
		strSFieldName = p_SFieldName;
	}

	public void setSFieldType(String p_SFieldType) {
		strSFieldType = p_SFieldType;
	}

	public void setSTableName(String p_STableName) {
		strSTableName = p_STableName;
	}
	
	public void setSFieldValue(String p_SFieldValue){
		strSFieldValue = p_SFieldValue;
	}

}
