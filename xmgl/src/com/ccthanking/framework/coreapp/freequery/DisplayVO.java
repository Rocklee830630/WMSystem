//v20071212-1
//v20081224-2
package com.ccthanking.framework.coreapp.freequery;

/*
 * author : michael wang
 * create time : 2007.04.08
 * you are free to copy,modify,rewrite this code,but please you reserve author's name.
 *
 * version 1.0 2007.04.08 create
 *         1.1 2007.05.03 apply for jwzh
 */

public class DisplayVO {

	private String strField = null;
	private String strName = null;
	private String strType = null;
	private String strDic = null;
	private String strFmt = null;
	private String strTable = null;
	private String strWidth = null;
	private String strHeight = null;
	private String strLength = null;
    private String strDicTable = null;
    private String strKeyField = null;
    private String strValueField = null;

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
	public String getField() {
		return strField;
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
	public String getTable() {
		return strTable;
	}
	public String getWidth() {
		return strWidth;
	}
	public String getHeight() {
		return strHeight;
	}
	public String getLength() {
		return strLength;
	}
	public void setTable(String p_Table) {
		strTable = p_Table;
	}
	public void setDic(String p_Dic) {
		strDic = p_Dic;
	}
	public void setField(String p_Field) {
		strField = p_Field;
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
	public void setWidth(String p_Width) {
		strWidth = p_Width;
	}
	public void setHeight(String p_Height) {
		strHeight = p_Height;
	}
	public void setLength(String p_Length) {
		strLength = p_Length;
	}

}
