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

public class FieldVO {

	private int intIndex;
	private String strName;
	private String strType;
	private String strValue;
	private String strDic;
	private String strFmt;
	private String strTable;
	private String strField;

    private String strDicTable;
  private String strKeyField;
  private String strValueField;

  public String getValueField()
  {
      return this.strValueField;
  }

  public String getKeyField()
  {
      return this.strKeyField;
  }

  public String getDicTable()
  {
      return this.strDicTable;
  }
  public void setValueField(String valueField)
  {
      this.strValueField = valueField;
  }

  public void setKeyField(String keyField)
  {
      this.strKeyField = keyField;
  }

  public void setDicTable(String dicTable)
  {
      this.strDicTable = dicTable;
  }


	public String getDic() {
		return strDic;
	}
	public String getFmt() {
		return strFmt;
	}
	public String getName() {
		return strName;
	}
	public String getType() {
		return strType;
	}
	public String getValue() {
		return strValue;
	}
	public String getTable() {
		return strTable;
	}
	public String getField() {
		return strField;
	}
	public int getIndex() {
		return intIndex;
	}

	public void setIndex(int p_Index) {
		intIndex = p_Index;
	}
	public void setTable(String p_Table) {
		strTable = p_Table;
	}
	public void setField(String p_Field) {
		strField = p_Field;
	}
	public void setDic(String p_Dic) {
		strDic = p_Dic;
	}
	public void setFmt(String p_Fmt) {
		strFmt = p_Fmt;
	}
	public void setName(String p_Name) {
		strName = p_Name;
	}
	public void setType(String p_Type) {
		strType = p_Type;
	}
	public void setValue(String p_Value) {
		strValue = p_Value;
	}


}
