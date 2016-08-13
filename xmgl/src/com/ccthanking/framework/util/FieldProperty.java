/*****************************************************************************
 * Field property class. for html and java(jsp/servlet) application with Data-
 * base operation.
 * Version 1.0
 * You can copy and/or use and/or modify this program free,but please reserve
 * the segment above. Please mail me if you have any question, Good day!
 ******************************************************************************
*/

package com.ccthanking.framework.util;


public class FieldProperty implements java.io.Serializable
{
	protected		String 	colName;
	protected 	String[]  list = null;
	protected		int 		colType;
	protected		int 		colIndex;
	protected		int 		colWidth;
	private			int 		colWidthType = 0;
	protected		String 	colSum;
	protected		int 		colGroup;
	protected		String 	colLink=null;
	protected		String[][]  linkParameter;
	protected		int 		linkParameterNum=0;
    protected		String  fgColor=null,bgColor=null,align=null,valign=null;
    protected       String style,classes,alias;
    protected       String dicCode = null;

    public String getFieldDicCode()
    {
        return this.dicCode;
    }
    public void setFieldDicCode(String code)
    {
        this.dicCode = code;
    }
    public String getAlias()
    {
      return this.alias;
    }
    public void setAlias(String name)
    {
      this.alias = name;
    }
	public boolean bindParameter(String para,String colName)
	{
		if(this.linkParameterNum > 63) return false;
		this.linkParameter[this.linkParameterNum][0] = (para==null)?"":para;
		this.linkParameter[this.linkParameterNum][1] = (colName==null)?"":colName;
		this.linkParameterNum++;
		return true;
	}
	public boolean bindParameter(String para,FieldProperty fd)
	{
		if(this.linkParameterNum > 63) return false;
		this.linkParameter[this.linkParameterNum][0] = (para==null)?"":para;
		this.linkParameter[this.linkParameterNum][1] = (fd==null)?"":fd.colName;
		this.linkParameterNum++;
		return true;
	}

    public void setStyle(String style)
    {
        this.style = style;
    }
    public String getStyle()
    {
      return this.style;
    }
   public void setClasses(String style)
   {
       this.classes = style;
   }
   public String getClasses()
   {
     return this.classes;
   }

	public boolean setValign(String align)
	{
		if(align == null) this.valign = "middle";
		else this.valign = align;
		return (align==null)?false:true;
	}
	public String getValign()
	{
		return this.valign;
	}

	public boolean setAlign(String align)
	{
		if(align == null) this.align = "center";
		else this.align = align;
		return (align==null)?false:true;
	}
	public String getAlign()
	{
		return this.align;
	}

	public boolean setFgColor(String color)
	{
		this.fgColor = color;
		return (color==null)?false:true;
	}
	public String getFgColor()
	{
		return this.fgColor;
	}

	public boolean setBgColor(String color)
	{
		this.bgColor = color;
		return (color==null)?false:true;
	}
	public String getBgColor()
	{
		return this.bgColor;
	}

	public boolean setColWidthType(int type)
	{
		this.colWidthType = type;
		return true;
	}
	public int getColWidthType()
	{
		return this.colWidthType;
	}

	public boolean setColName(String colName)
	{
		if(colName==null) return false;
		this.colName = colName;
		this.list = colName.split("->");
		return true;
	}
	public String getFieldName()
	{
		return this.list[this.list.length-1];
	}
	public String getColName()
	{
		return this.colName;
	}

	public boolean setColType(int colType)
	{
		if(colType < 0) return false;
		this.colType = colType;
		return true;
	}
	public int getColType()
	{
		return this.colType;
	}

	public boolean setColIndex(int colIndex)
	{
		if(colIndex < 0) return false;
		this.colIndex = colIndex;
		return true;
	}
	public int getColIndex()
	{
		return this.colIndex;
	}

	public boolean setColWidth(int colWidth)
	{
		if(colWidth < 0) return false;
		this.colWidth = colWidth;
		return true;
	}
	public int getColWidth()
	{
		return this.colWidth;
	}

	public boolean setColSum(String colSum)
	{
		if(colSum == null) return false;
		this.colSum = colSum;
		return true;
	}
	public String getColSum()
	{
		return this.colSum;
	}

	public boolean setColGroup(int colGroup)
	{
		if(colGroup < 0) return false;
		this.colGroup = colGroup;
		return true;
	}
	public int getColGroup()
	{
		return this.colGroup;
	}

	public boolean setColLink(String colLink)
	{
		if(colLink == null) return false;
		this.colLink = colLink;
		return true;
	}
	public String getColLink()
	{
		return this.colLink;
	}

	public FieldProperty()
	{
		this.colName = null;
		this.colType = 0;
		this.colIndex = 0;
		this.colWidth = 0;
		this.colSum = null;
		this.colGroup = 0;
		this.colLink = null;
		this.linkParameter = new String[64][2];
		this.linkParameterNum = 0;
		this.align = null;
		this.valign = null;
		this.bgColor = null;
		this.fgColor = null;
	}

	public FieldProperty(String colName,int colType,int colIndex,int colWidth,String colSum,int colGroup,String colLink)
	{
		this.colName 	= (colName	!=	null)	?	colName	:	"";
		this.list = colName.split("->");
		this.colType 	= (colType	>		0)		?	colType	:	0;
		this.colIndex = (colIndex	>		0)		?	colIndex:	0;
		this.colWidth = (colWidth	>		0)		?	colWidth:	0;
		this.colSum		=	(colSum		!=	null)	?	colSum	:	"";
		this.colGroup = (colGroup	>		0)		?	colGroup:	0;
		this.colLink 	= (colLink	!=	null)	?	colLink	:	"";
		this.linkParameter = new String[64][2];
		this.linkParameterNum = 0;
		this.align = null;
		this.valign = null;
		this.bgColor = null;
		this.fgColor = null;
	}
	public boolean setDefault()
	{
		this.align = "center";
		this.valign = "middle";
		this.bgColor = "#ffffff";
		this.fgColor = "#000000";
		return true;
	}
}
