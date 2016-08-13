//v20071212-1
package com.ccthanking.framework.coreapp.freequery;

public class QueryJoinVO {
	private String strRelationID;
	private String strQueryID;
	private String strTableID;
	private String strFieldName;
	private String strRelationName;
	private String strQueryName;
	
	public String getFieldName() {
		return strFieldName;
	}
	public String getQueryID() {
		return strQueryID;
	}
	public String getRelationID() {
		return strRelationID;
	}
	public String getRelationName() {
		return strRelationName;
	}
	public String getTableID() {
		return strTableID;
	}
	public String getQueryName() {
		return strQueryName;
	}
	public void setFieldName(String p_FieldName) {
		strFieldName = p_FieldName;
	}
	public void setQueryID(String p_QueryID) {
		strQueryID = p_QueryID;
	}
	public void setRelationID(String p_RelationID) {
		strRelationID = p_RelationID;
	}
	public void setRelationName(String p_RelationName) {
		strRelationName = p_RelationName;
	}
	public void setTableID(String p_TableID) {
		strTableID = p_TableID;
	}
	public void setQueryName(String p_QueryName) {
		strQueryName = p_QueryName;
	}
	
}
